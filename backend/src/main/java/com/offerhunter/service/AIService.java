package com.offerhunter.service;

import com.offerhunter.vo.AnalysisVO;
import com.offerhunter.vo.GrowthPlanVO;
import com.offerhunter.vo.OptimizationVO;

public interface AIService {

    AnalysisVO analyzeResume(Long resumeId);

    AnalysisVO getAnalysis(Long resumeId);

    OptimizationVO optimizeResume(Long resumeId, Long jobId);

    GrowthPlanVO generateGrowthPlan(Long resumeId);
}
