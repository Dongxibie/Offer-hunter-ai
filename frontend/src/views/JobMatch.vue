<template>
  <div class="job-match-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1>岗位匹配推荐</h1>
        <p>基于AI智能分析，为您推荐最适合的岗位</p>
      </div>
    </div>

    <!-- 选择简历 -->
    <el-card class="select-card" shadow="never">
      <div class="select-content">
        <div class="select-info">
          <el-icon :size="24"><Document /></el-icon>
          <span>选择简历进行匹配</span>
        </div>
        <div class="select-actions">
          <el-select
            v-model="selectedResumeId"
            placeholder="请选择简历"
            size="large"
            style="width: 300px"
          >
            <el-option
              v-for="resume in resumes"
              :key="resume.id"
              :label="resume.fileName"
              :value="resume.id"
            />
          </el-select>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            :disabled="!selectedResumeId"
            @click="handleMatch"
          >
            <el-icon><Connection /></el-icon>
            开始匹配
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <div class="loading-card">
        <div class="loading-animation">
          <div class="pulse-ring"></div>
          <el-icon class="is-loading" :size="48"><Connection /></el-icon>
        </div>
        <h3>AI正在匹配岗位</h3>
        <p>正在分析简历与岗位的匹配度...</p>
      </div>
    </div>

    <!-- 匹配结果 -->
    <div v-else-if="jobs.length > 0" class="job-list">
      <div class="list-header">
        <h2>匹配结果</h2>
        <span class="count">共找到 {{ jobs.length }} 个匹配岗位</span>
      </div>

      <div class="job-grid">
        <el-card
          v-for="(job, index) in jobs"
          :key="job.jobId"
          class="job-card"
          shadow="hover"
          :class="{ 'top-match': index < 3 }"
        >
          <!-- 排名标签 -->
          <div v-if="index < 3" class="rank-badge" :class="`rank-${index + 1}`">
            {{ index === 0 ? '最佳匹配' : index === 1 ? '优质匹配' : '推荐岗位' }}
          </div>

          <div class="job-main">
            <div class="job-info">
              <div class="job-title-row">
                <h3>{{ job.position }}</h3>
                <el-tag v-if="job.salaryRange" type="success" effect="plain">
                  {{ job.salaryRange }}
                </el-tag>
              </div>
              <div class="job-meta">
                <span class="company">
                  <el-icon><OfficeBuilding /></el-icon>
                  {{ job.company }}
                </span>
                <span class="location">
                  <el-icon><Location /></el-icon>
                  {{ job.location }}
                </span>
              </div>
            </div>
            <div class="match-score">
              <el-progress
                type="dashboard"
                :percentage="job.matchScore"
                :width="90"
                :color="getScoreColor(job.matchScore)"
              >
                <template #default="{ percentage }">
                  <div class="score-content">
                    <span class="score-value">{{ percentage }}</span>
                    <span class="score-unit">%</span>
                  </div>
                </template>
              </el-progress>
              <span class="score-label">匹配度</span>
            </div>
          </div>

          <div class="job-detail">
            <div class="skills-section">
              <div class="skills-row">
                <span class="skills-label">
                  <el-icon color="#67c23a"><CircleCheck /></el-icon>
                  匹配技能
                </span>
                <div class="skills-tags">
                  <el-tag
                    v-for="skill in job.matchedSkills"
                    :key="skill"
                    type="success"
                    size="small"
                    effect="plain"
                  >
                    {{ skill }}
                  </el-tag>
                </div>
              </div>
              <div class="skills-row">
                <span class="skills-label">
                  <el-icon color="#e6a23c"><WarningFilled /></el-icon>
                  缺失技能
                </span>
                <div class="skills-tags">
                  <el-tag
                    v-for="skill in job.missingSkills"
                    :key="skill"
                    type="warning"
                    size="small"
                    effect="plain"
                  >
                    {{ skill }}
                  </el-tag>
                </div>
              </div>
            </div>
            <div class="match-reason">
              <p>{{ job.matchReason }}</p>
            </div>
          </div>

          <div class="job-actions">
            <el-button
              type="primary"
              @click="router.push(`/optimize/${selectedResumeId}/${job.jobId}`)"
            >
              <el-icon><Edit /></el-icon>
              简历优化
            </el-button>
            <el-button
              @click="router.push(`/interview/${selectedResumeId}/${job.jobId}`)"
            >
              <el-icon><ChatDotRound /></el-icon>
              模拟面试
            </el-button>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 空状态 -->
    <el-empty
      v-else-if="!loading && hasSearched"
      description="暂未找到匹配的岗位，请尝试其他简历"
      :image-size="120"
    />
    <el-empty
      v-else-if="!loading"
      description="请先选择简历进行岗位匹配"
      :image-size="120"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getResumeList } from '@/api/resume'
import { matchJobs } from '@/api/job'

const router = useRouter()

const resumes = ref<any[]>([])
const jobs = ref<any[]>([])
const selectedResumeId = ref<number | null>(null)
const loading = ref(false)
const hasSearched = ref(false)

onMounted(async () => {
  try {
    const res = await getResumeList()
    resumes.value = res.data || []
    if (resumes.value.length > 0) {
      selectedResumeId.value = resumes.value[0].id
    }
  } catch (error) {
    // Empty list
  }
})

async function handleMatch() {
  if (!selectedResumeId.value) return
  loading.value = true
  hasSearched.value = true
  try {
    const res = await matchJobs(selectedResumeId.value)
    jobs.value = res.data || []
  } catch (error) {
    // Error handled by interceptor
  } finally {
    loading.value = false
  }
}

function getScoreColor(score: number) {
  if (score >= 80) return '#67c23a'
  if (score >= 60) return '#e6a23c'
  return '#f56c6c'
}
</script>

<style scoped lang="scss">
.job-match-page {
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

.select-card {
  margin-bottom: 24px;

  :deep(.el-card__body) {
    padding: 24px 32px;
  }
}

.select-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.select-info {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 16px;
  font-weight: 500;
  color: var(--text-primary);

  .el-icon {
    color: var(--primary-color);
  }
}

.select-actions {
  display: flex;
  gap: 12px;
}

// 加载状态
.loading-state {
  display: flex;
  justify-content: center;
  padding: 80px 0;
}

.loading-card {
  text-align: center;
}

.loading-animation {
  position: relative;
  width: 100px;
  height: 100px;
  margin: 0 auto 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-color);
}

.pulse-ring {
  position: absolute;
  inset: 0;
  border-radius: 50%;
  border: 2px solid var(--primary-color);
  animation: pulse 2s ease-out infinite;
}

@keyframes pulse {
  0% {
    transform: scale(0.8);
    opacity: 1;
  }
  100% {
    transform: scale(1.3);
    opacity: 0;
  }
}

.loading-card h3 {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.loading-card p {
  font-size: 14px;
  color: var(--text-secondary);
}

// 匹配结果
.list-header {
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

.job-grid {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.job-card {
  position: relative;
  overflow: visible;

  &.top-match {
    border-color: var(--primary-color);
  }

  :deep(.el-card__body) {
    padding: 24px;
  }
}

.rank-badge {
  position: absolute;
  top: -1px;
  right: 24px;
  padding: 4px 16px;
  border-radius: 0 0 8px 8px;
  font-size: 12px;
  font-weight: 600;
  color: white;

  &.rank-1 {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }

  &.rank-2 {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  }

  &.rank-3 {
    background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  }
}

.job-main {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.job-info {
  flex: 1;
}

.job-title-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;

  h3 {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
  }
}

.job-meta {
  display: flex;
  gap: 24px;

  span {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 14px;
    color: var(--text-secondary);
  }

  .el-icon {
    font-size: 16px;
  }
}

.match-score {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.score-content {
  display: flex;
  align-items: baseline;
}

.score-value {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
}

.score-unit {
  font-size: 14px;
  color: var(--text-secondary);
  margin-left: 2px;
}

.score-label {
  font-size: 12px;
  color: var(--text-secondary);
}

.job-detail {
  padding-top: 20px;
  border-top: 1px solid var(--border-light);
}

.skills-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 16px;
}

.skills-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.skills-label {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--text-secondary);
  white-space: nowrap;
  min-width: 100px;
}

.skills-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.match-reason {
  p {
    font-size: 14px;
    color: var(--text-regular);
    line-height: 1.6;
  }
}

.job-actions {
  display: flex;
  gap: 12px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid var(--border-light);
}
</style>
