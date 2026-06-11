<template>
  <div class="growth-page">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <div class="loading-card">
        <div class="loading-animation">
          <div class="pulse-ring"></div>
          <el-icon class="is-loading" :size="48"><TrendCharts /></el-icon>
        </div>
        <h3>AI正在生成成长计划</h3>
        <p>正在分析您的能力，制定个性化学习路径...</p>
      </div>
    </div>

    <!-- 成长计划 -->
    <template v-else-if="growthPlan">
      <!-- 页面头部 -->
      <div class="page-header">
        <div class="header-content">
          <h1>AI成长规划</h1>
          <p>基于您的能力评估，制定30-60-90天学习路径</p>
        </div>
        <el-button @click="router.back()">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
      </div>

      <!-- 总体概览 -->
      <el-card class="overview-card" shadow="never">
        <div class="overview-content">
          <div class="overview-item" v-for="phase in growthPlan.phases" :key="phase.phase">
            <div class="phase-badge" :style="{ background: getPhaseGradient(phase.phase) }">
              <span class="phase-number">{{ phase.phase }}</span>
            </div>
            <div class="phase-info">
              <span class="phase-duration">{{ phase.duration }}</span>
              <span class="phase-title">{{ phase.title }}</span>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 详细计划 -->
      <div class="phases-list">
        <el-card
          v-for="phase in growthPlan.phases"
          :key="phase.phase"
          class="phase-card"
          shadow="never"
        >
          <div class="phase-header">
            <div class="phase-icon" :style="{ background: getPhaseGradient(phase.phase) }">
              <span>{{ phase.phase }}</span>
            </div>
            <div class="phase-title-area">
              <h2>{{ phase.title }}</h2>
              <el-tag :type="getPhaseTagType(phase.phase)" effect="plain">
                {{ phase.duration }}
              </el-tag>
            </div>
          </div>

          <div class="phase-content">
            <!-- 阶段目标 -->
            <div class="content-section">
              <h3>
                <el-icon :style="{ color: getPhaseColor(phase.phase) }"><Aim /></el-icon>
                阶段目标
              </h3>
              <ul class="goals-list">
                <li v-for="(goal, index) in phase.goals" :key="index">
                  <el-icon :color="getPhaseColor(phase.phase)"><CircleCheck /></el-icon>
                  <span>{{ goal }}</span>
                </li>
              </ul>
            </div>

            <!-- 具体任务 -->
            <div class="content-section">
              <h3>
                <el-icon :style="{ color: getPhaseColor(phase.phase) }"><List /></el-icon>
                具体任务
              </h3>
              <ul class="tasks-list">
                <li v-for="(task, index) in phase.tasks" :key="index">
                  <span class="task-number">{{ index + 1 }}</span>
                  <span>{{ task }}</span>
                </li>
              </ul>
            </div>

            <!-- 学习资源 -->
            <div class="content-section">
              <h3>
                <el-icon :style="{ color: getPhaseColor(phase.phase) }"><Reading /></el-icon>
                学习资源
              </h3>
              <div class="resources-grid">
                <div
                  v-for="(resource, index) in phase.resources"
                  :key="index"
                  class="resource-item"
                >
                  <el-icon><Link /></el-icon>
                  <span>{{ resource }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 行动建议 -->
      <el-card class="action-card" shadow="never">
        <div class="action-content">
          <el-icon :size="32" color="#1a73e8"><Promotion /></el-icon>
          <div class="action-text">
            <h3>立即行动</h3>
            <p>按照计划逐步执行，每天坚持学习，相信您一定能找到心仪的Offer！</p>
          </div>
        </div>
      </el-card>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { generateGrowthPlan } from '@/api/ai'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const growthPlan = ref<any>(null)

onMounted(async () => {
  try {
    growthPlan.value = await generateGrowthPlan(Number(route.params.id))
  } catch (error) {
    // Error handled by interceptor
  } finally {
    loading.value = false
  }
})

function getPhaseColor(phase: number) {
  const colors = ['#67c23a', '#e6a23c', '#1a73e8']
  return colors[phase - 1] || '#999'
}

function getPhaseGradient(phase: number) {
  const gradients = [
    'linear-gradient(135deg, #67c23a 0%, #85ce61 100%)',
    'linear-gradient(135deg, #e6a23c 0%, #f0c78a 100%)',
    'linear-gradient(135deg, #1a73e8 0%, #4a90e2 100%)',
  ]
  return gradients[phase - 1] || gradients[0]
}

function getPhaseTagType(phase: number) {
  const types = ['success', 'warning', '' ]
  return types[phase - 1] || ''
}
</script>

<style scoped lang="scss">
.growth-page {
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

// 总体概览
.overview-card {
  margin-bottom: 24px;

  :deep(.el-card__body) {
    padding: 32px;
  }
}

.overview-content {
  display: flex;
  justify-content: space-around;
  gap: 24px;
}

.overview-item {
  display: flex;
  align-items: center;
  gap: 16px;
}

.phase-badge {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.phase-number {
  font-size: 20px;
  font-weight: 700;
}

.phase-info {
  display: flex;
  flex-direction: column;
}

.phase-duration {
  font-size: 12px;
  color: var(--text-secondary);
}

.phase-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

// 详细计划
.phases-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.phase-card {
  :deep(.el-card__body) {
    padding: 32px;
  }
}

.phase-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 32px;
}

.phase-icon {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;

  span {
    font-size: 24px;
    font-weight: 700;
  }
}

.phase-title-area {
  display: flex;
  align-items: center;
  gap: 12px;

  h2 {
    font-size: 20px;
    font-weight: 600;
    color: var(--text-primary);
  }
}

.phase-content {
  display: flex;
  flex-direction: column;
  gap: 28px;
}

.content-section {
  h3 {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 16px;
  }
}

.goals-list {
  list-style: none;
  padding: 0;
  margin: 0;

  li {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 10px 0;
    font-size: 14px;
    color: var(--text-regular);
    line-height: 1.6;
  }
}

.tasks-list {
  list-style: none;
  padding: 0;
  margin: 0;

  li {
    display: flex;
    align-items: flex-start;
    gap: 12px;
    padding: 10px 0;
    font-size: 14px;
    color: var(--text-regular);
    line-height: 1.6;
  }
}

.task-number {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: var(--bg-color);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  color: var(--text-secondary);
  flex-shrink: 0;
}

.resources-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.resource-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: var(--bg-color);
  border-radius: var(--radius-md);
  font-size: 13px;
  color: var(--text-regular);

  .el-icon {
    color: var(--primary-color);
  }
}

// 行动建议
.action-card {
  margin-top: 32px;

  :deep(.el-card__body) {
    padding: 32px;
  }
}

.action-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.action-text {
  h3 {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 8px;
  }

  p {
    font-size: 14px;
    color: var(--text-secondary);
    line-height: 1.6;
  }
}
</style>
