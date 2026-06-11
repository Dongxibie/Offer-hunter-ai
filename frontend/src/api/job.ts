import request from '@/utils/request'

export function getJobList() {
  return request.get('/job/list')
}

export function getJob(id: number) {
  return request.get(`/job/${id}`)
}

export function matchJobs(resumeId: number) {
  return request.get(`/job/match?resumeId=${resumeId}`)
}
