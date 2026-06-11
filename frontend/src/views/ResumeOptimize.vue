<template>
  <div class="optimize-page">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <div class="loading-card">
        <div class="loading-animation">
          <div class="pulse-ring"></div>
          <el-icon class="is-loading" :size="48"><Edit /></el-icon>
        </div>
        <h3>AI正在优化简历</h3>
        <p>正在分析目标岗位要求，生成优化建议...</p>
      </div>
    </div>

    <!-- 优化结果 -->
    <template v-else-if="optimization">
      <!-- 页面头部 -->
      <div class="page-header">
        <div class="header-content">
          <h1>AI简历优化</h1>
          <p>针对目标岗位的个性化优化建议</p>
        </div>
        <el-button @click="router.back()">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
      </div>

      <!-- 目标岗位 -->
      <el-card class="target-card" shadow="never">
        <div class="target-content">
          <div class="target-icon">
            <el-icon :size="32"><Briefcase /></el-icon>
          </div>
          <div class="target-info">
            <h3>目标岗位</h3>
            <p>{{ optimization.company }} - {{ optimization.position }}</p>
          </div>
        </div>
      </el-card>

      <!-- 问题与建议 -->
      <el-row :gutter="24">
        <el-col :span="12">
          <el-card class="issue-card" shadow="never">
            <template #header>
              <div class="card-header danger">
                <el-icon><WarningFilled /></el-icon>
                <span>当前问题</span>
              </div>
            </template>
            <ul class="issue-list">
              <li v-for="(issue, index) in optimization.currentIssues" :key="index">
                <span class="issue-number">{{ index + 1 }}</span>
                <span>{{ issue }}</span>
              </li>
            </ul>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="suggestion-card" shadow="never">
            <template #header>
              <div class="card-header success">
                <el-icon><SuccessFilled /></el-icon>
                <span>优化建议</span>
              </div>
            </template>
            <ul class="suggestion-list">
              <li v-for="(suggestion, index) in optimization.suggestions" :key="index">
                <span class="suggestion-number">{{ index + 1 }}</span>
                <span>{{ suggestion }}</span>
              </li>
            </ul>
          </el-card>
        </el-col>
      </el-row>

      <!-- 优化后内容 -->
      <el-card class="content-card" shadow="never">
        <template #header>
          <div class="card-header primary">
            <el-icon><Document /></el-icon>
            <span>优化后内容</span>
          </div>
        </template>
        <div class="content-text">{{ optimization.optimizedContent }}</div>
      </el-card>

      <!-- STAR法则 -->
      <el-card class="star-card" shadow="never">
        <template #header>
          <div class="card-header warning">
            <el-icon><Star /></el-icon>
            <span>STAR法则改写</span>
          </div>
        </template>
        <div class="star-intro">
          <el-alert
            title="STAR法则"
            description="Situation（情境）、Task（任务）、Action（行动）、Result（结果），用于结构化描述项目经历"
            type="info"
            :closable="false"
            show-icon
          />
        </div>
        <div class="content-text">{{ optimization.starVersion }}</div>
      </el-card>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { optimizeResume } from '@/api/ai'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const optimization = ref<any>(null)

onMounted(async () => {
  try {
    optimization.value = await optimizeResume(
      Number(route.params.resumeId),
      Number(route.params.jobId)
    )
  } catch (error) {
    // Error handled by interceptor
  } finally {
    loading.value = false
  }
})
</script>

<style scoped lang="scss">
.optimize-page {
  padding-bottom: 40px;
}

// 加载状态
.loading-state {
  display: flex;
  justify-content: center;
  padding: 120px 0;
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

// 页面头部
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

// 目标岗位
.target-card {
  margin-bottom: 24px;

  :deep(.el-card__body) {
    padding: 24px 32px;
  }
}

.target-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.target-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.target-info {
  h3 {
    font-size: 14px;
    color: var(--text-secondary);
    margin-bottom: 4px;
  }

  p {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
  }
}

// 卡片头部
.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;

  &.danger {
    color: var(--danger-color);
  }

  &.success {
    color: var(--success-color);
  }

  &.primary {
    color: var(--primary-color);
  }

  &.warning {
    color: var(--warning-color);
  }
}

// 问题与建议
.issue-card,
.suggestion-card {
  margin-bottom: 24px;
  height: calc(100% - 24px);
}

.issue-list,
.suggestion-list {
  list-style: none;
  padding: 0;
  margin: 0;

  li {
    display: flex;
    align-items: flex-start;
    gap: 12px;
    padding: 12px 0;
    border-bottom: 1px solid var(--border-light);
    font-size: 14px;
    color: var(--text-regular);
    line-height: 1.6;

    &:last-child {
      border-bottom: none;
    }
  }
}

.issue-number,
.suggestion-number {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  flex-shrink: 0;
}

.issue-number {
  background: rgba(245, 108, 108, 0.1);
  color: var(--danger-color);
}

.suggestion-number {
  background: rgba(103, 194, 58, 0.1);
  color: var(--success-color);
}

// 内容卡片
.content-card,
.star-card {
  margin-bottom: 24px;
}

.star-intro {
  margin-bottom: 20px;
}

.content-text {
  font-size: 14px;
  color: var(--text-regular);
  line-height: 1.8;
  white-space: pre-wrap;
  background: var(--bg-color);
  padding: 20px;
  border-radius: var(--radius-md);
}
</style>
