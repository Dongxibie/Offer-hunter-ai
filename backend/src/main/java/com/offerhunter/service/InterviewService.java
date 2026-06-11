package com.offerhunter.service;

import com.offerhunter.vo.InterviewVO;
import com.offerhunter.vo.QuestionVO;

public interface InterviewService {

    InterviewVO startInterview(Long resumeId, Long jobId);

    QuestionVO answerQuestion(String sessionId, Long questionId, String answer);
}
