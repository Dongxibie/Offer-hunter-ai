<template>
  <div class="analysis-page">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <div class="loading-card">
        <div class="loading-animation">
          <div class="pulse-ring"></div>
          <el-icon class="is-loading" :size="48"><DataAnalysis /></el-icon>
        </div>
        <h3>AI正在分析简历</h3>
        <p>正在提取关键信息，生成求职画像...</p>
      </div>
    </div>

    <!-- 分析结果 -->
    <template v-else-if="analysis">
      <!-- 页面头部 -->
      <div class="page-header">
        <div class="header-content">
          <h1>AI简历分析</h1>
          <p>基于AI深度分析的求职画像和能力评估</p>
        </div>
        <div class="header-actions">
          <el-button type="primary" @click="router.push('/jobs')">
            <el-icon><Connection /></el-icon>
            查看岗位匹配
          </el-button>
          <el-button @click="router.push(`/growth/${route.params.id}`)">
            <el-icon><TrendCharts /></el-icon>
            生成成长规划
          </el-button>
        </div>
      </div>

      <!-- 求职画像卡片 -->
      <el-row :gutter="24">
        <el-col :span="16">
          <el-card class="profile-card" shadow="never">
            <div class="profile-header">
              <div class="profile-icon">
                <el-icon :size="32"><User /></el-icon>
              </div>
              <div class="profile-info">
                <h3>求职画像</h3>
                <el-tag type="primary" size="large" effect="dark">
                  {{ analysis.jobDirection }}
                </el-tag>
              </div>
            </div>
            <div class="profile-summary">
              <h4>综合评价</h4>
              <p>{{ analysis.summary }}</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="skills-card" shadow="never">
            <template #header>
              <div class="card-header">
                <el-icon><Collection /></el-icon>
                <span>技能标签</span>
              </div>
            </template>
            <div class="skill-tags">
              <el-tag
                v-for="(skill, index) in analysis.skillTags"
                :key="skill"
                :type="getTagType(index)"
                class="skill-tag"
              >
                {{ skill }}
              </el-tag>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 能力雷达图 -->
      <el-card class="chart-card" shadow="never">
        <template #header>
          <div class="card-header">
            <el-icon><DataAnalysis /></el-icon>
            <span>能力雷达图</span>
          </div>
        </template>
        <div class="chart-container">
          <RadarChart :data="analysis.abilityScores" />
        </div>
      </el-card>

      <!-- 能力详情 -->
      <el-card class="ability-card" shadow="never">
        <template #header>
          <div class="card-header">
            <el-icon><TrendCharts /></el-icon>
            <span>能力详情</span>
          </div>
        </template>
        <div class="ability-list">
          <div
            v-for="(score, key) in analysis.abilityScores"
            :key="key"
            class="ability-item"
          >
            <div class="ability-label">{{ key }}</div>
            <el-progress
              :percentage="score"
              :color="getProgressColor(score)"
              :stroke-width="12"
            />
          </div>
        </div>
      </el-card>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAnalysis } from '@/api/ai'
import RadarChart from '@/components/common/RadarChart.vue'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const analysis = ref<any>(null)

onMounted(async () => {
  try {
    analysis.value = await getAnalysis(Number(route.params.id))
  } catch (error) {
    // Error handled by interceptor
  } finally {
    loading.value = false
  }
})

function getTagType(index: number) {
  const types = ['', 'success', 'warning', 'info', 'danger']
  return types[index % types.length]
}

function getProgressColor(score: number) {
  if (score >= 80) return '#67c23a'
  if (score >= 60) return '#e6a23c'
  return '#f56c6c'
}
</script>

<style scoped lang="scss">
.analysis-page {
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

.header-actions {
  display: flex;
  gap: 12px;
}

// 卡片通用
.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);

  .el-icon {
    color: var(--primary-color);
  }
}

// 求职画像
.profile-card {
  margin-bottom: 24px;

  :deep(.el-card__body) {
    padding: 32px;
  }
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 24px;
}

.profile-icon {
  width: 72px;
  height: 72px;
  border-radius: 20px;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.profile-info {
  h3 {
    font-size: 14px;
    color: var(--text-secondary);
    margin-bottom: 8px;
  }
}

.profile-summary {
  h4 {
    font-size: 14px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 12px;
  }

  p {
    font-size: 14px;
    color: var(--text-regular);
    line-height: 1.8;
  }
}

// 技能标签
.skills-card {
  margin-bottom: 24px;
  height: calc(100% - 24px);

  :deep(.el-card__body) {
    padding: 24px;
  }
}

.skill-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.skill-tag {
  margin: 0;
}

// 雷达图
.chart-card {
  margin-bottom: 24px;

  :deep(.el-card__body) {
    padding: 24px;
  }
}

.chart-container {
  height: 400px;
}

// 能力详情
.ability-card {
  :deep(.el-card__body) {
    padding: 24px;
  }
}

.ability-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.ability-item {
  .ability-label {
    font-size: 14px;
    font-weight: 500;
    color: var(--text-primary);
    margin-bottom: 8px;
  }
}
</style>
