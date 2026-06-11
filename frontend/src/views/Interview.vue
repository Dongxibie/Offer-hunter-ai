<template>
  <div class="interview-page">
    <!-- 开始面试 -->
    <div v-if="!interview" class="start-section">
      <div class="start-card">
        <div class="start-icon">
          <el-icon :size="64"><ChatDotRound /></el-icon>
        </div>
        <h2>AI模拟面试</h2>
        <p>AI将根据目标岗位生成面试题，帮助您准备面试</p>
        <div class="start-features">
          <div class="feature-item">
            <el-icon><Check /></el-icon>
            <span>技术面试题</span>
          </div>
          <div class="feature-item">
            <el-icon><Check /></el-icon>
            <span>项目追问</span>
          </div>
          <div class="feature-item">
            <el-icon><Check /></el-icon>
            <span>HR问题</span>
          </div>
          <div class="feature-item">
            <el-icon><Check /></el-icon>
            <span>行为面试</span>
          </div>
        </div>
        <el-button
          type="primary"
          size="large"
          :loading="loading"
          @click="handleStart"
        >
          开始面试
        </el-button>
      </div>
    </div>

    <!-- 面试进行中 -->
    <template v-else>
      <!-- 面试头部 -->
      <div class="interview-header">
        <div class="header-content">
          <h1>AI模拟面试</h1>
          <p>{{ interview.company }} - {{ interview.position }}</p>
        </div>
        <div class="header-progress">
          <span class="progress-text">
            已完成 {{ answeredCount }} / {{ interview.questions.length }} 题
          </span>
          <el-progress
            :percentage="(answeredCount / interview.questions.length) * 100"
            :show-text="false"
          />
        </div>
      </div>

      <!-- 面试问题列表 -->
      <div class="questions-list">
        <el-card
          v-for="(question, index) in interview.questions"
          :key="question.id"
          class="question-card"
          shadow="never"
          :class="{ 'answered': question.userAnswer }"
        >
          <div class="question-header">
            <div class="question-meta">
              <span class="question-number">{{ index + 1 }}</span>
              <el-tag :type="getTagType(question.type)" effect="plain">
                {{ getTypeName(question.type) }}
              </el-tag>
            </div>
            <el-icon v-if="question.userAnswer" class="check-icon" color="#67c23a">
              <CircleCheckFilled />
            </el-icon>
          </div>

          <p class="question-content">{{ question.content }}</p>

          <!-- 回答区域 -->
          <div v-if="!question.userAnswer" class="answer-area">
            <el-input
              v-model="answers[question.id]"
              type="textarea"
              :rows="5"
              placeholder="请输入您的回答..."
              resize="vertical"
            />
            <div class="answer-actions">
              <el-button
                type="primary"
                :loading="answeringId === question.id"
                :disabled="!answers[question.id]?.trim()"
                @click="handleAnswer(question.id)"
              >
                提交回答
              </el-button>
            </div>
          </div>

          <!-- 回答结果 -->
          <div v-else class="answer-result">
            <div class="user-answer">
              <h4>
                <el-icon><User /></el-icon>
                您的回答
              </h4>
              <p>{{ question.userAnswer }}</p>
            </div>

            <div class="ai-feedback">
              <div class="feedback-header">
                <div class="score-circle" :style="{ borderColor: getScoreColor(question.aiScore) }">
                  <span class="score-value">{{ question.aiScore }}</span>
                  <span class="score-label">分</span>
                </div>
                <div class="feedback-title">
                  <h4>AI点评</h4>
                  <el-tag :type="getScoreTagType(question.aiScore)" size="small">
                    {{ getScoreLevel(question.aiScore) }}
                  </el-tag>
                </div>
              </div>
              <div class="feedback-content">
                <p class="comment">{{ question.aiComment }}</p>
                <div class="improvement">
                  <el-icon><Promotion /></el-icon>
                  <span>改进建议：{{ question.improvement }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 面试完成 -->
      <div v-if="answeredCount === interview.questions.length" class="complete-section">
        <el-card class="complete-card" shadow="never">
          <div class="complete-content">
            <el-icon :size="48" color="#67c23a"><SuccessFilled /></el-icon>
            <h3>面试完成</h3>
            <p>您已完成所有面试题，平均得分：{{ averageScore }}分</p>
            <div class="complete-actions">
              <el-button type="primary" @click="router.push('/jobs')">
                查看更多岗位
              </el-button>
              <el-button @click="router.push(`/growth/${route.params.resumeId}`)">
                查看成长规划
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { startInterview, answerQuestion } from '@/api/interview'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const answeringId = ref<number | null>(null)
const interview = ref<any>(null)
const answers = reactive<Record<number, string>>({})

const answeredCount = computed(() => {
  if (!interview.value) return 0
  return interview.value.questions.filter((q: any) => q.userAnswer).length
})

const averageScore = computed(() => {
  if (!interview.value) return 0
  const answered = interview.value.questions.filter((q: any) => q.userAnswer)
  if (answered.length === 0) return 0
  const sum = answered.reduce((acc: number, q: any) => acc + q.aiScore, 0)
  return Math.round(sum / answered.length)
})

async function handleStart() {
  loading.value = true
  try {
    interview.value = await startInterview(
      Number(route.params.resumeId),
      Number(route.params.jobId)
    )
  } catch (error) {
    // Error handled by interceptor
  } finally {
    loading.value = false
  }
}

async function handleAnswer(questionId: number) {
  const answer = answers[questionId]
  if (!answer?.trim()) return

  answeringId.value = questionId
  try {
    const result: any = await answerQuestion({
      sessionId: interview.value.sessionId,
      questionId,
      answer,
    })
    const index = interview.value.questions.findIndex((q: any) => q.id === questionId)
    if (index !== -1) {
      interview.value.questions[index] = result
    }
  } catch (error) {
    // Error handled by interceptor
  } finally {
    answeringId.value = null
  }
}

function getTagType(type: string) {
  const map: Record<string, string> = {
    technical: '',
    project: 'success',
    hr: 'warning',
    behavioral: 'info',
    comprehensive: 'danger',
  }
  return map[type] || ''
}

function getTypeName(type: string) {
  const map: Record<string, string> = {
    technical: '技术面试',
    project: '项目追问',
    hr: 'HR问题',
    behavioral: '行为面试',
    comprehensive: '综合问题',
  }
  return map[type] || type
}

function getScoreColor(score: number) {
  if (score >= 80) return '#67c23a'
  if (score >= 60) return '#e6a23c'
  return '#f56c6c'
}

function getScoreTagType(score: number) {
  if (score >= 80) return 'success'
  if (score >= 60) return 'warning'
  return 'danger'
}

function getScoreLevel(score: number) {
  if (score >= 90) return '优秀'
  if (score >= 80) return '良好'
  if (score >= 60) return '及格'
  return '需改进'
}
</script>

<style scoped lang="scss">
.interview-page {
  padding-bottom: 40px;
}

// 开始面试
.start-section {
  display: flex;
  justify-content: center;
  padding: 80px 0;
}

.start-card {
  text-align: center;
  max-width: 480px;
}

.start-icon {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(26, 115, 232, 0.1) 0%, rgba(26, 115, 232, 0.05) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px;
  color: var(--primary-color);
}

.start-card h2 {
  font-size: 28px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 12px;
}

.start-card > p {
  font-size: 15px;
  color: var(--text-secondary);
  margin-bottom: 32px;
}

.start-features {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 40px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: var(--text-regular);

  .el-icon {
    color: var(--success-color);
  }
}

// 面试头部
.interview-header {
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

.header-progress {
  text-align: right;

  .progress-text {
    font-size: 13px;
    color: var(--text-secondary);
    margin-bottom: 8px;
    display: block;
  }

  .el-progress {
    width: 200px;
  }
}

// 问题列表
.questions-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.question-card {
  &.answered {
    border-color: var(--success-color);
  }

  :deep(.el-card__body) {
    padding: 24px;
  }
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.question-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.question-number {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--primary-color);
  color: white;
  font-size: 14px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
}

.check-icon {
  font-size: 24px;
}

.question-content {
  font-size: 15px;
  color: var(--text-primary);
  line-height: 1.8;
  margin-bottom: 20px;
}

// 回答区域
.answer-area {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.answer-actions {
  display: flex;
  justify-content: flex-end;
}

// 回答结果
.answer-result {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.user-answer {
  h4 {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 12px;
  }

  p {
    font-size: 14px;
    color: var(--text-regular);
    line-height: 1.8;
    background: var(--bg-color);
    padding: 16px;
    border-radius: var(--radius-md);
  }
}

.ai-feedback {
  background: var(--bg-color);
  border-radius: var(--radius-md);
  padding: 20px;
}

.feedback-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
}

.score-circle {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  border: 3px solid;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.score-value {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1;
}

.score-label {
  font-size: 11px;
  color: var(--text-secondary);
}

.feedback-title {
  display: flex;
  align-items: center;
  gap: 8px;

  h4 {
    font-size: 15px;
    font-weight: 600;
    color: var(--text-primary);
  }
}

.feedback-content {
  .comment {
    font-size: 14px;
    color: var(--text-regular);
    line-height: 1.8;
    margin-bottom: 16px;
  }

  .improvement {
    display: flex;
    align-items: flex-start;
    gap: 8px;
    font-size: 14px;
    color: var(--primary-color);
    background: rgba(26, 115, 232, 0.05);
    padding: 12px 16px;
    border-radius: var(--radius-md);

    .el-icon {
      margin-top: 2px;
    }
  }
}

// 面试完成
.complete-section {
  margin-top: 32px;
}

.complete-card {
  :deep(.el-card__body) {
    padding: 48px;
  }
}

.complete-content {
  text-align: center;

  h3 {
    font-size: 24px;
    font-weight: 700;
    color: var(--text-primary);
    margin: 16px 0 8px;
  }

  p {
    font-size: 15px;
    color: var(--text-secondary);
    margin-bottom: 32px;
  }
}

.complete-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
}
</style>
