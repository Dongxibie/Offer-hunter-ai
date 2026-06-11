import { defineStore } from 'pinia'
import { ref } from 'vue'
import { uploadResume, getResumeList } from '@/api/resume'

interface Resume {
  id: number
  fileName: string
  status: number
  createTime: string
}

export const useResumeStore = defineStore('resume', () => {
  const resumes = ref<Resume[]>([])
  const currentResume = ref<Resume | null>(null)

  async function upload(file: File) {
    const data: any = await uploadResume(file)
    currentResume.value = data
    await fetchResumes()
    return data
  }

  async function fetchResumes() {
    const data: any = await getResumeList()
    resumes.value = data
    return data
  }

  function setCurrentResume(resume: Resume) {
    currentResume.value = resume
  }

  return {
    resumes,
    currentResume,
    upload,
    fetchResumes,
    setCurrentResume,
  }
})
