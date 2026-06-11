import request from '@/utils/request'

export function analyzeResume(resumeId: number) {
  return request.post(`/ai/analyze-resume?resumeId=${resumeId}`)
}

export function getAnalysis(resumeId: number) {
  return request.get(`/ai/analysis/${resumeId}`)
}

export function optimizeResume(resumeId: number, jobId: number) {
  return request.post(`/ai/optimize-resume?resumeId=${resumeId}&jobId=${jobId}`)
}

export function generateGrowthPlan(resumeId: number) {
  return request.post(`/ai/growth-plan?resumeId=${resumeId}`)
}
