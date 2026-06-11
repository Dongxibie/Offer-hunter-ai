package com.offerhunter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.offerhunter.common.BusinessException;
import com.offerhunter.entity.*;
import com.offerhunter.mapper.*;
import com.offerhunter.service.JobService;
import com.offerhunter.service.ResumeService;
import com.offerhunter.util.JsonExtractorUtil;
import com.offerhunter.vo.JobMatchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;
    private final JobSkillMapper jobSkillMapper;
    private final MatchResultMapper matchResultMapper;
    private final ResumeService resumeService;
    private final ResumeAnalysisMapper analysisMapper;
    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;

    @Override
    public List<Job> listJobs() {
        return jobMapper.selectList(
                new LambdaQueryWrapper<Job>()
                        .eq(Job::getStatus, 1)
                        .orderByDesc(Job::getIsHot)
                        .orderByDesc(Job::getCreateTime)
        );
    }

    @Override
    public Job getJob(Long jobId) {
        Job job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException("岗位不存在");
        }
        return job;
    }

    @Override
    public List<JobMatchVO> matchJobs(Long resumeId) {
        Resume resume = resumeService.getResume(resumeId);
        ResumeAnalysis analysis = analysisMapper.selectOne(
                new LambdaQueryWrapper<ResumeAnalysis>()
                        .eq(ResumeAnalysis::getResumeId, resumeId)
                        .orderByDesc(ResumeAnalysis::getCreateTime)
                        .last("LIMIT 1")
        );

        if (analysis == null) {
            throw new BusinessException("请先进行AI简历分析");
        }

        List<Job> jobs = listJobs();
        List<JobMatchVO> matchResults = new ArrayList<>();

        for (Job job : jobs) {
            List<JobSkill> skills = jobSkillMapper.selectList(
                    new LambdaQueryWrapper<JobSkill>()
                            .eq(JobSkill::getJobId, job.getId())
            );

            String prompt = """
                    请评估以下简历与岗位的匹配度。

                    简历技能标签：%s
                    简历求职方向：%s
                    简历内容摘要：%s

                    目标岗位：%s - %s
                    岗位要求技能：%s
                    岗位描述：%s

                    请按以下JSON格式输出（不要输出其他内容）：
                    {
                        "matchScore": 0-100的匹配分数,
                        "skillScore": 0-100的技能匹配分,
                        "projectScore": 0-100的项目匹配分,
                        "matchedSkills": ["匹配的技能1", "匹配的技能2"],
                        "missingSkills": ["缺失的技能1", "缺失的技能2"],
                        "matchReason": "50字左右的匹配原因说明"
                    }
                    """.formatted(
                    analysis.getSkillTags(),
                    analysis.getJobDirection(),
                    analysis.getSummary(),
                    job.getCompany(),
                    job.getPosition(),
                    skills.stream().map(JobSkill::getSkillName).collect(Collectors.joining(", ")),
                    job.getDescription()
            );

            try {
                String response = chatModel.call(new Prompt(List.of(
                        new SystemMessage("你是一个专业的HR，只输出JSON格式的结果"),
                        new UserMessage(prompt)
                ))).getResult().getOutput().getContent();

                Map<String, Object> result = objectMapper.readValue(JsonExtractorUtil.extractJson(response), new TypeReference<>() {});

                JobMatchVO vo = new JobMatchVO();
                vo.setJobId(job.getId());
                vo.setCompany(job.getCompany());
                vo.setPosition(job.getPosition());
                vo.setSalaryRange(job.getSalaryRange());
                vo.setLocation(job.getLocation());
                vo.setMatchScore(BigDecimal.valueOf(((Number) result.get("matchScore")).doubleValue()));
                vo.setMatchedSkills((List<String>) result.get("matchedSkills"));
                vo.setMissingSkills((List<String>) result.get("missingSkills"));
                vo.setMatchReason((String) result.get("matchReason"));
                matchResults.add(vo);

                MatchResult matchResult = new MatchResult();
                matchResult.setResumeId(resumeId);
                matchResult.setJobId(job.getId());
                matchResult.setMatchScore(vo.getMatchScore());
                matchResult.setSkillScore(BigDecimal.valueOf(((Number) result.get("skillScore")).doubleValue()));
                matchResult.setProjectScore(BigDecimal.valueOf(((Number) result.get("projectScore")).doubleValue()));
                matchResult.setMatchedSkills(objectMapper.writeValueAsString(vo.getMatchedSkills()));
                matchResult.setMissingSkills(objectMapper.writeValueAsString(vo.getMissingSkills()));
                matchResult.setMatchReason(vo.getMatchReason());
                matchResultMapper.insert(matchResult);

            } catch (Exception e) {
                log.error("匹配岗位失败: {}", job.getPosition(), e);
            }
        }

        matchResults.sort((a, b) -> b.getMatchScore().compareTo(a.getMatchScore()));
        return matchResults;
    }
}
