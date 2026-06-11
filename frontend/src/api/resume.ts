import request from '@/utils/request'

export function uploadResume(file: File) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/resume/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

export function getResume(id: number) {
  return request.get(`/resume/${id}`)
}

export function getResumeList() {
  return request.get('/resume/list')
}
