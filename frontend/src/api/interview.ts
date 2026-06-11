import request from '@/utils/request'

export function startInterview(resumeId: number, jobId: number) {
  return request.post(`/interview/start?resumeId=${resumeId}&jobId=${jobId}`)
}

export function answerQuestion(data: { sessionId: string; questionId: number; answer: string }) {
  return request.post('/interview/answer', data)
}
