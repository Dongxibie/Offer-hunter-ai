package com.offerhunter.controller;

import com.offerhunter.common.Result;
import com.offerhunter.dto.AnswerDTO;
import com.offerhunter.service.InterviewService;
import com.offerhunter.vo.InterviewVO;
import com.offerhunter.vo.QuestionVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/start")
    public Result<InterviewVO> startInterview(@RequestParam Long resumeId, @RequestParam Long jobId) {
        return Result.success(interviewService.startInterview(resumeId, jobId));
    }

    @PostMapping("/answer")
    public Result<QuestionVO> answerQuestion(@Valid @RequestBody AnswerDTO dto) {
        return Result.success(interviewService.answerQuestion(dto.getSessionId(), dto.getQuestionId(), dto.getAnswer()));
    }
}
