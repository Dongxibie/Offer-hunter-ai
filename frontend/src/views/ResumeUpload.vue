<template>
  <div class="upload-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1>上传简历</h1>
        <p>支持 PDF 和 DOCX 格式，AI 将自动解析您的简历内容</p>
      </div>
    </div>

    <!-- 上传区域 -->
    <el-card class="upload-card" shadow="never">
      <el-upload
        class="upload-area"
        drag
        :auto-upload="false"
        :on-change="handleFileChange"
        :on-remove="handleFileRemove"
        :file-list="fileList"
        accept=".pdf,.docx"
        :limit="1"
      >
        <div class="upload-content">
          <div class="upload-icon">
            <el-icon :size="48"><UploadFilled /></el-icon>
          </div>
          <div class="upload-text">
            <p class="primary-text">拖拽文件到此处，或 <em>点击上传</em></p>
            <p class="secondary-text">仅支持 PDF / DOCX 格式，文件大小不超过 10MB</p>
          </div>
        </div>
      </el-upload>

      <div class="upload-actions">
        <el-button
          type="primary"
          size="large"
          :loading="uploading"
          :disabled="!selectedFile"
          @click="handleUpload"
        >
          <el-icon><Upload /></el-icon>
          上传并解析
        </el-button>
      </div>
    </el-card>

    <!-- 简历列表 -->
    <div v-if="resumes.length > 0" class="resume-section">
      <div class="section-header">
        <h2>我的简历</h2>
        <span class="count">共 {{ resumes.length }} 份</span>
      </div>

      <div class="resume-grid">
        <el-card
          v-for="resume in resumes"
          :key="resume.id"
          class="resume-card"
          shadow="hover"
        >
          <div class="resume-info">
            <div class="resume-icon">
              <el-icon :size="32" :color="resume.fileName.endsWith('.pdf') ? '#f56c6c' : '#409eff'">
                <Document />
              </el-icon>
            </div>
            <div class="resume-detail">
              <h3>{{ resume.fileName }}</h3>
              <div class="resume-meta">
                <el-tag
                  :type="resume.status === 1 ? 'success' : 'warning'"
                  size="small"
                >
                  {{ resume.status === 1 ? '已解析' : '待解析' }}
                </el-tag>
                <span class="time">{{ resume.createTime }}</span>
              </div>
            </div>
          </div>
          <div class="resume-actions">
            <el-button
              type="primary"
              size="small"
              @click="handleAnalyze(resume)"
              :loading="analyzingId === resume.id"
            >
              <el-icon><DataAnalysis /></el-icon>
              AI分析
            </el-button>
            <el-button
              size="small"
              @click="router.push(`/analysis/${resume.id}`)"
            >
              查看分析
            </el-button>
            <el-button
              size="small"
              @click="router.push(`/growth/${resume.id}`)"
            >
              成长规划
            </el-button>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 空状态 -->
    <el-empty
      v-else-if="!loading"
      description="暂无简历，上传一份开始体验吧"
      :image-size="120"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useResumeStore } from '@/stores/resume'
import { analyzeResume } from '@/api/ai'
import { ElMessage } from 'element-plus'

const router = useRouter()
const resumeStore = useResumeStore()

const selectedFile = ref<File | null>(null)
const fileList = ref<any[]>([])
const uploading = ref(false)
const loading = ref(true)
const resumes = ref<any[]>([])
const analyzingId = ref<number | null>(null)

onMounted(async () => {
  try {
    resumes.value = await resumeStore.fetchResumes()
  } catch (error) {
    // Empty list
  } finally {
    loading.value = false
  }
})

function handleFileChange(file: any) {
  selectedFile.value = file.raw
}

function handleFileRemove() {
  selectedFile.value = null
}

async function handleUpload() {
  if (!selectedFile.value) return
  uploading.value = true
  try {
    await resumeStore.upload(selectedFile.value)
    ElMessage.success('上传成功')
    resumes.value = await resumeStore.fetchResumes()
    selectedFile.value = null
    fileList.value = []
  } catch (error) {
    // Error handled by interceptor
  } finally {
    uploading.value = false
  }
}

async function handleAnalyze(resume: any) {
  analyzingId.value = resume.id
  try {
    await analyzeResume(resume.id)
    ElMessage.success('AI分析完成')
    router.push(`/analysis/${resume.id}`)
  } catch (error) {
    // Error handled by interceptor
  } finally {
    analyzingId.value = null
  }
}
</script>

<style scoped lang="scss">
.upload-page {
  padding-bottom: 40px;
}

.page-header {
  margin-bottom: 24px;

  .header-content {
    h1 {
      font-size: 24px;
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: 8px;
    }

    p {
      font-size: 14px;
      color: var(--text-secondary);
    }
  }
}

.upload-card {
  margin-bottom: 32px;

  :deep(.el-card__body) {
    padding: 32px;
  }
}

.upload-area {
  :deep(.el-upload) {
    width: 100%;
  }

  :deep(.el-upload-dragger) {
    width: 100%;
    padding: 48px 24px;
    border: 2px dashed var(--border-color);
    border-radius: var(--radius-lg);
    background: var(--bg-color);
    transition: all 0.2s;

    &:hover {
      border-color: var(--primary-color);
      background: rgba(26, 115, 232, 0.02);
    }
  }
}

.upload-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.upload-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(26, 115, 232, 0.1) 0%, rgba(26, 115, 232, 0.05) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-color);
}

.upload-text {
  text-align: center;

  .primary-text {
    font-size: 16px;
    color: var(--text-primary);
    margin-bottom: 8px;

    em {
      color: var(--primary-color);
      font-style: normal;
      cursor: pointer;
    }
  }

  .secondary-text {
    font-size: 13px;
    color: var(--text-secondary);
  }
}

.upload-actions {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}

.resume-section {
  .section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16px;

    h2 {
      font-size: 18px;
      font-weight: 600;
      color: var(--text-primary);
    }

    .count {
      font-size: 13px;
      color: var(--text-secondary);
    }
  }
}

.resume-grid {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.resume-card {
  :deep(.el-card__body) {
    padding: 20px 24px;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}

.resume-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.resume-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  background: var(--bg-color);
  display: flex;
  align-items: center;
  justify-content: center;
}

.resume-detail {
  h3 {
    font-size: 15px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 8px;
  }
}

.resume-meta {
  display: flex;
  align-items: center;
  gap: 12px;

  .time {
    font-size: 13px;
    color: var(--text-secondary);
  }
}

.resume-actions {
  display: flex;
  gap: 8px;
}
</style>
