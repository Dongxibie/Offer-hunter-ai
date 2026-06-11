import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, register as registerApi, getUserInfo } from '@/api/user'

interface UserInfo {
  id: number
  username: string
  email?: string
  phone?: string
  avatar?: string
}

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref<UserInfo | null>(null)

  async function login(username: string, password: string) {
    const data: any = await loginApi({ username, password })
    token.value = data.token
    userInfo.value = data
    localStorage.setItem('token', data.token)
    return data
  }

  async function register(username: string, password: string, email?: string, phone?: string) {
    const data: any = await registerApi({ username, password, email, phone })
    token.value = data.token
    userInfo.value = data
    localStorage.setItem('token', data.token)
    return data
  }

  async function fetchUserInfo() {
    const data: any = await getUserInfo()
    userInfo.value = data
    return data
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
  }

  return {
    token,
    userInfo,
    login,
    register,
    fetchUserInfo,
    logout,
  }
})
