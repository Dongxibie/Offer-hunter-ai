package com.offerhunter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.offerhunter.common.BusinessException;
import com.offerhunter.entity.InterviewQuestion;
import com.offerhunter.entity.InterviewSession;
import com.offerhunter.entity.Job;
import com.offerhunter.entity.Resume;
import com.offerhunter.mapper.InterviewQuestionMapper;
import com.offerhunter.mapper.InterviewSessionMapper;
import com.offerhunter.mapper.JobMapper;
import com.offerhunter.service.InterviewService;
import com.offerhunter.service.ResumeService;
import com.offerhunter.util.JsonExtractorUtil;
import com.offerhunter.vo.InterviewVO;
import com.offerhunter.vo.QuestionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewSessionMapper sessionMapper;
    private final InterviewQuestionMapper questionMapper;
    private final JobMapper jobMapper;
    private final ResumeService resumeService;
    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;

    @Override
    public InterviewVO startInterview(Long resumeId, Long jobId) {
        Resume resume = resumeService.getResume(resumeId);
        Job job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException("岗位不存在");
        }

        String sessionId = UUID.randomUUID().toString().replace("-", "");

        String prompt = """
                你是一个专业的面试官。请根据以下岗位信息，生成5道面试题。

                岗位：%s - %s
                岗位要求：%s

                简历内容：
                %s

                请生成以下类型的面试题各1道：
                1. 技术面试题（考察专业技能）
                2. 项目追问（针对简历中的项目经历）
                3. HR问题（考察职业规划和动机）
                4. 行为面试题（考察软技能）
                5. 综合问题（考察综合能力）

                请按以下JSON格式输出（不要输出其他内容）：
                {
                    "questions": [
                        {"type": "technical", "content": "问题内容"},
                        {"type": "project", "content": "问题内容"},
                        {"type": "hr", "content": "问题内容"},
                        {"type": "behavioral", "content": "问题内容"},
                        {"type": "comprehensive", "content": "问题内容"}
                    ]
                }
                """.formatted(job.getCompany(), job.getPosition(), job.getRequirements(), resume.getRawText());

        try {
            String response = chatModel.call(new Prompt(List.of(
                    new SystemMessage("你是一个专业的面试官，只输出JSON格式的结果"),
                    new UserMessage(prompt)
            ))).getResult().getOutput().getContent();

            Map<String, Object> result = objectMapper.readValue(JsonExtractorUtil.extractJson(response), new TypeReference<>() {});
            List<Map<String, Object>> questions = (List<Map<String, Object>>) result.get("questions");

            InterviewSession session = new InterviewSession();
            session.setId(sessionId);
            session.setResumeId(resumeId);
            session.setJobId(jobId);
            session.setStatus(1);
            sessionMapper.insert(session);

            List<QuestionVO> questionVOs = new ArrayList<>();
            for (Map<String, Object> q : questions) {
                InterviewQuestion question = new InterviewQuestion();
                question.setSessionId(sessionId);
                question.setQuestionType((String) q.get("type"));
                question.setQuestionContent((String) q.get("content"));
                questionMapper.insert(question);

                QuestionVO vo = new QuestionVO();
                vo.setId(question.getId());
                vo.setType(question.getQuestionType());
                vo.setContent(question.getQuestionContent());
                questionVOs.add(vo);
            }

            InterviewVO vo = new InterviewVO();
            vo.setSessionId(sessionId);
            vo.setCompany(job.getCompany());
            vo.setPosition(job.getPosition());
            vo.setQuestions(questionVOs);
            return vo;

        } catch (Exception e) {
            log.error("生成面试题失败", e);
            throw new BusinessException("生成面试题失败，请重试");
        }
    }

    @Override
    public QuestionVO answerQuestion(String sessionId, Long questionId, String answer) {
        InterviewQuestion question = questionMapper.selectById(questionId);
        if (question == null) {
            throw new BusinessException("问题不存在");
        }

        String prompt = """
                你是一个专业的面试官。请对以下面试回答进行点评和评分。

                问题类型：%s
                问题内容：%s

                用户回答：
                %s

                请按以下JSON格式输出（不要输出其他内容）：
                {
                    "score": 0-100的分数,
                    "comment": "50字左右的点评",
                    "improvement": "30字左右的改进建议"
                }
                """.formatted(question.getQuestionType(), question.getQuestionContent(), answer);

        try {
            String response = chatModel.call(new Prompt(List.of(
                    new SystemMessage("你是一个专业的面试官，只输出JSON格式的结果"),
                    new UserMessage(prompt)
            ))).getResult().getOutput().getContent();

            Map<String, Object> result = objectMapper.readValue(JsonExtractorUtil.extractJson(response), new TypeReference<>() {});

            question.setAnswerContent(answer);
            question.setAiComment((String) result.get("comment"));
            question.setAiScore(((Number) result.get("score")).intValue());
            question.setImprovementSuggestion((String) result.get("improvement"));
            questionMapper.updateById(question);

            QuestionVO vo = new QuestionVO();
            vo.setId(question.getId());
            vo.setType(question.getQuestionType());
            vo.setContent(question.getQuestionContent());
            vo.setUserAnswer(answer);
            vo.setAiComment(question.getAiComment());
            vo.setAiScore(question.getAiScore());
            vo.setImprovement(question.getImprovementSuggestion());
            return vo;

        } catch (Exception e) {
            log.error("评估回答失败", e);
            throw new BusinessException("评估回答失败，请重试");
        }
    }
}
