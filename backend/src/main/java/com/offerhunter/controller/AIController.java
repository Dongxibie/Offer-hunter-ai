package com.offerhunter.controller;

import com.offerhunter.common.Result;
import com.offerhunter.service.AIService;
import com.offerhunter.vo.AnalysisVO;
import com.offerhunter.vo.GrowthPlanVO;
import com.offerhunter.vo.OptimizationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/analyze-resume")
    public Result<AnalysisVO> analyzeResume(@RequestParam Long resumeId) {
        return Result.success(aiService.analyzeResume(resumeId));
    }

    @GetMapping("/analysis/{resumeId}")
    public Result<AnalysisVO> getAnalysis(@PathVariable Long resumeId) {
        return Result.success(aiService.getAnalysis(resumeId));
    }

    @PostMapping("/optimize-resume")
    public Result<OptimizationVO> optimizeResume(@RequestParam Long resumeId, @RequestParam Long jobId) {
        return Result.success(aiService.optimizeResume(resumeId, jobId));
    }

    @PostMapping("/growth-plan")
    public Result<GrowthPlanVO> generateGrowthPlan(@RequestParam Long resumeId) {
        return Result.success(aiService.generateGrowthPlan(resumeId));
    }
}
