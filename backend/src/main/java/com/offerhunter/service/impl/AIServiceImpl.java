package com.offerhunter.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.offerhunter.common.BusinessException;
import com.offerhunter.entity.Resume;
import com.offerhunter.entity.ResumeAnalysis;
import com.offerhunter.entity.ResumeOptimization;
import com.offerhunter.entity.GrowthPlan;
import com.offerhunter.entity.Job;
import com.offerhunter.mapper.*;
import com.offerhunter.service.AIService;
import com.offerhunter.service.ResumeService;
import com.offerhunter.util.JsonExtractorUtil;
import com.offerhunter.vo.AnalysisVO;
import com.offerhunter.vo.GrowthPlanVO;
import com.offerhunter.vo.OptimizationVO;
import com.offerhunter.vo.PhaseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    private final ResumeService resumeService;
    private final ResumeAnalysisMapper analysisMapper;
    private final ResumeOptimizationMapper optimizationMapper;
    private final GrowthPlanMapper growthPlanMapper;
    private final JobMapper jobMapper;
    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;

    @Override
    public AnalysisVO analyzeResume(Long resumeId) {
        Resume resume = resumeService.getResume(resumeId);

        String prompt = """
                你是一个专业的HR顾问和职业规划师。请分析以下简历内容，生成详细的求职画像。

                简历内容：
                %s

                请按以下JSON格式输出（不要输出其他内容）：
                {
                    "jobDirection": "最匹配的求职方向（如：软件开发、AI应用、产品经理、数据分析等）",
                    "skillTags": ["技能1", "技能2", "技能3", ...],
                    "abilityScores": {
                        "开发能力": 0-100的分数,
                        "项目能力": 0-100的分数,
                        "学习能力": 0-100的分数,
                        "沟通能力": 0-100的分数,
                        "实践能力": 0-100的分数
                    },
                    "summary": "100字左右的综合评价"
                }
                """.formatted(resume.getRawText());

        String response = chatModel.call(new Prompt(List.of(
                new SystemMessage("你是一个专业的HR顾问，只输出JSON格式的结果"),
                new UserMessage(prompt)
        ))).getResult().getOutput().getContent();

        try {
            Map<String, Object> result = objectMapper.readValue(JsonExtractorUtil.extractJson(response), new TypeReference<>() {});

            ResumeAnalysis analysis = new ResumeAnalysis();
            analysis.setResumeId(resumeId);
            analysis.setJobDirection((String) result.get("jobDirection"));
            analysis.setSkillTags(objectMapper.writeValueAsString(result.get("skillTags")));
            analysis.setAbilityScores(objectMapper.writeValueAsString(result.get("abilityScores")));
            analysis.setSummary((String) result.get("summary"));
            analysisMapper.insert(analysis);

            return convertToVO(analysis);
        } catch (Exception e) {
            log.error("解析AI响应失败: {}", response, e);
            throw new BusinessException("AI分析失败，请重试");
        }
    }

    @Override
    public AnalysisVO getAnalysis(Long resumeId) {
        ResumeAnalysis analysis = analysisMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ResumeAnalysis>()
                        .eq(ResumeAnalysis::getResumeId, resumeId)
                        .orderByDesc(ResumeAnalysis::getCreateTime)
                        .last("LIMIT 1")
        );
        if (analysis == null) {
            throw new BusinessException("未找到分析结果，请先进行AI分析");
        }
        return convertToVO(analysis);
    }

    @Override
    public OptimizationVO optimizeResume(Long resumeId, Long jobId) {
        Resume resume = resumeService.getResume(resumeId);
        Job job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException("岗位不存在");
        }

        String prompt = """
                你是一个专业的简历优化顾问。请根据目标岗位要求，优化以下简历内容。

                简历内容：
                %s

                目标岗位：%s - %s
                岗位要求：
                %s

                请按以下JSON格式输出（不要输出其他内容）：
                {
                    "currentIssues": ["问题1", "问题2", ...],
                    "suggestions": ["建议1", "建议2", ...],
                    "optimizedContent": "优化后的简历内容",
                    "starVersion": "使用STAR法则改写的项目经历"
                }
                """.formatted(resume.getRawText(), job.getCompany(), job.getPosition(), job.getRequirements());

        String response = chatModel.call(new Prompt(List.of(
                new SystemMessage("你是一个专业的简历优化顾问，只输出JSON格式的结果"),
                new UserMessage(prompt)
        ))).getResult().getOutput().getContent();

        try {
            Map<String, Object> result = objectMapper.readValue(JsonExtractorUtil.extractJson(response), new TypeReference<>() {});

            ResumeOptimization optimization = new ResumeOptimization();
            optimization.setResumeId(resumeId);
            optimization.setJobId(jobId);
            optimization.setCurrentIssues(objectMapper.writeValueAsString(result.get("currentIssues")));
            optimization.setSuggestions(objectMapper.writeValueAsString(result.get("suggestions")));
            optimization.setOptimizedContent((String) result.get("optimizedContent"));
            optimization.setStarVersion((String) result.get("starVersion"));
            optimizationMapper.insert(optimization);

            OptimizationVO vo = new OptimizationVO();
            vo.setResumeId(resumeId);
            vo.setJobId(jobId);
            vo.setCompany(job.getCompany());
            vo.setPosition(job.getPosition());
            vo.setCurrentIssues((List<String>) result.get("currentIssues"));
            vo.setSuggestions((List<String>) result.get("suggestions"));
            vo.setOptimizedContent((String) result.get("optimizedContent"));
            vo.setStarVersion((String) result.get("starVersion"));
            return vo;
        } catch (Exception e) {
            log.error("解析AI响应失败: {}", response, e);
            throw new BusinessException("简历优化失败，请重试");
        }
    }

    @Override
    public GrowthPlanVO generateGrowthPlan(Long resumeId) {
        Resume resume = resumeService.getResume(resumeId);

        String prompt = """
                你是一个专业的职业规划师。请根据以下简历内容，生成30天、60天、90天的成长路线规划。

                简历内容：
                %s

                请按以下JSON格式输出（不要输出其他内容）：
                {
                    "phases": [
                        {
                            "phase": 1,
                            "duration": "30天",
                            "title": "阶段标题",
                            "goals": ["目标1", "目标2"],
                            "tasks": ["任务1", "任务2"],
                            "resources": ["资源1", "资源2"]
                        },
                        {
                            "phase": 2,
                            "duration": "60天",
                            "title": "阶段标题",
                            "goals": ["目标1", "目标2"],
                            "tasks": ["任务1", "任务2"],
                            "resources": ["资源1", "资源2"]
                        },
                        {
                            "phase": 3,
                            "duration": "90天",
                            "title": "阶段标题",
                            "goals": ["目标1", "目标2"],
                            "tasks": ["任务1", "任务2"],
                            "resources": ["资源1", "资源2"]
                        }
                    ]
                }
                """.formatted(resume.getRawText());

        String response = chatModel.call(new Prompt(List.of(
                new SystemMessage("你是一个专业的职业规划师，只输出JSON格式的结果"),
                new UserMessage(prompt)
        ))).getResult().getOutput().getContent();

        try {
            Map<String, Object> result = objectMapper.readValue(JsonExtractorUtil.extractJson(response), new TypeReference<>() {});
            List<Map<String, Object>> phases = (List<Map<String, Object>>) result.get("phases");

            GrowthPlanVO vo = new GrowthPlanVO();
            vo.setResumeId(resumeId);

            List<PhaseVO> phaseVOs = phases.stream().map(phase -> {
                PhaseVO phaseVO = new PhaseVO();
                phaseVO.setPhase((Integer) phase.get("phase"));
                phaseVO.setDuration((String) phase.get("duration"));
                phaseVO.setTitle((String) phase.get("title"));
                phaseVO.setGoals((List<String>) phase.get("goals"));
                phaseVO.setTasks((List<String>) phase.get("tasks"));
                phaseVO.setResources((List<String>) phase.get("resources"));

                GrowthPlan plan = new GrowthPlan();
                plan.setResumeId(resumeId);
                plan.setPhase(phaseVO.getPhase());
                plan.setDuration(phaseVO.getDuration());
                plan.setTitle(phaseVO.getTitle());
                try {
                    plan.setGoals(objectMapper.writeValueAsString(phaseVO.getGoals()));
                    plan.setTasks(objectMapper.writeValueAsString(phaseVO.getTasks()));
                    plan.setResources(objectMapper.writeValueAsString(phaseVO.getResources()));
                } catch (Exception ex) {
                    throw new BusinessException("数据序列化失败");
                }
                plan.setStatus(0);
                growthPlanMapper.insert(plan);

                return phaseVO;
            }).toList();

            vo.setPhases(phaseVOs);
            return vo;
        } catch (Exception e) {
            log.error("解析AI响应失败: {}", response, e);
            throw new BusinessException("生成成长计划失败，请重试");
        }
    }

    private AnalysisVO convertToVO(ResumeAnalysis analysis) {
        AnalysisVO vo = new AnalysisVO();
        vo.setResumeId(analysis.getResumeId());
        vo.setJobDirection(analysis.getJobDirection());
        vo.setSummary(analysis.getSummary());
        try {
            vo.setSkillTags(objectMapper.readValue(analysis.getSkillTags(), new TypeReference<>() {}));
            vo.setAbilityScores(objectMapper.readValue(analysis.getAbilityScores(), new TypeReference<>() {}));
        } catch (Exception e) {
            log.error("解析JSON失败", e);
        }
        return vo;
    }
}
