import request from '@/utils/request'

export function login(data: { username: string; password: string }) {
  return request.post('/user/login', data)
}

export function register(data: { username: string; password: string; email?: string; phone?: string }) {
  return request.post('/user/register', data)
}

export function getUserInfo() {
  return request.get('/user/info')
}
