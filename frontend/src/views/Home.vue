<template>
  <div class="home-page">
    <!-- 顶部导航 -->
    <header class="home-header">
      <div class="header-inner">
        <div class="logo" @click="router.push('/')">
          <svg viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect width="40" height="40" rx="8" fill="rgba(255,255,255,0.15)"/>
            <path d="M20 8L30 14V26L20 32L10 26V14L20 8Z" stroke="white" stroke-width="2" fill="none"/>
            <path d="M20 14L25 17V23L20 26L15 23V17L20 14Z" fill="white"/>
          </svg>
          <span>Offer Hunter AI</span>
        </div>
        <div class="header-actions">
          <el-button text class="nav-btn" @click="router.push('/upload')">功能介绍</el-button>
          <el-button text class="nav-btn" @click="scrollToFeatures">核心功能</el-button>
          <el-button type="primary" round @click="router.push('/login')">开始体验</el-button>
        </div>
      </div>
    </header>

    <!-- Hero区域 -->
    <section class="hero-section">
      <div class="hero-bg">
        <div class="hero-particles"></div>
      </div>
      <div class="hero-content">
        <div class="hero-badge">
          <el-icon><Star /></el-icon>
          腾讯AI-HR培训营项目
        </div>
        <h1 class="hero-title">
          <span class="gradient-text">AI驱动</span>的智能求职助手
        </h1>
        <p class="hero-subtitle">
          上传简历，AI自动完成岗位匹配、简历优化、模拟面试、成长规划
          <br/>
          帮助大学生快速找到适合自己的岗位
        </p>
        <div class="hero-actions">
          <el-button type="primary" size="large" round @click="router.push('/login')">
            <el-icon><Upload /></el-icon>
            上传简历开始体验
          </el-button>
          <el-button size="large" round @click="scrollToFeatures">
            了解更多
            <el-icon><ArrowDown /></el-icon>
          </el-button>
        </div>
        <div class="hero-stats">
          <div class="stat-item">
            <span class="stat-value">100+</span>
            <span class="stat-label">岗位数据</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">5大</span>
            <span class="stat-label">核心功能</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">AI</span>
            <span class="stat-label">智能驱动</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 功能流程 -->
    <section class="flow-section" ref="flowSection">
      <div class="section-header">
        <h2>使用流程</h2>
        <p>简单四步，开启智能求职之旅</p>
      </div>
      <div class="flow-steps">
        <div class="flow-step" v-for="(step, index) in flowSteps" :key="index">
          <div class="step-number">{{ index + 1 }}</div>
          <div class="step-icon">
            <el-icon :size="32"><component :is="step.icon" /></el-icon>
          </div>
          <h3>{{ step.title }}</h3>
          <p>{{ step.desc }}</p>
        </div>
      </div>
    </section>

    <!-- 核心功能 -->
    <section class="features-section" ref="featuresSection">
      <div class="section-header">
        <h2>核心功能</h2>
        <p>AI赋能，让求职更高效</p>
      </div>
      <div class="features-grid">
        <div
          v-for="(feature, index) in features"
          :key="index"
          class="feature-card"
          :class="{ 'featured': feature.featured }"
        >
          <div class="feature-icon" :style="{ background: feature.gradient }">
            <el-icon :size="28"><component :is="feature.icon" /></el-icon>
          </div>
          <h3>{{ feature.title }}</h3>
          <p>{{ feature.desc }}</p>
          <div class="feature-tags">
            <el-tag v-for="tag in feature.tags" :key="tag" size="small" effect="plain">{{ tag }}</el-tag>
          </div>
        </div>
      </div>
    </section>

    <!-- 技术栈 -->
    <section class="tech-section">
      <div class="section-header">
        <h2>技术架构</h2>
        <p>企业级技术栈，稳定可靠</p>
      </div>
      <div class="tech-grid">
        <div class="tech-card" v-for="tech in techStack" :key="tech.name">
          <div class="tech-logo">{{ tech.icon }}</div>
          <span class="tech-name">{{ tech.name }}</span>
          <span class="tech-desc">{{ tech.desc }}</span>
        </div>
      </div>
    </section>

    <!-- CTA -->
    <section class="cta-section">
      <div class="cta-content">
        <h2>准备好开始了吗？</h2>
        <p>上传你的简历，让AI帮你找到心仪的Offer</p>
        <el-button type="primary" size="large" round @click="router.push('/login')">
          立即开始
          <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>
    </section>

    <!-- 底部 -->
    <footer class="home-footer">
      <p>© 2026 Offer Hunter AI - 腾讯AI-HR培训营项目</p>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const featuresSection = ref<HTMLElement>()
const flowSection = ref<HTMLElement>()

const flowSteps = [
  { icon: 'Upload', title: '上传简历', desc: '支持PDF/DOCX格式，一键上传' },
  { icon: 'DataAnalysis', title: 'AI分析', desc: '智能解析简历，生成求职画像' },
  { icon: 'Connection', title: '岗位匹配', desc: '基于技能和经验的智能推荐' },
  { icon: 'TrendCharts', title: '成长规划', desc: '个性化学习路径，持续提升' },
]

const features = [
  {
    icon: 'Document',
    title: 'AI简历解析',
    desc: '智能提取简历关键信息，自动识别学校、专业、技能、项目经历等，生成结构化数据',
    tags: ['PDF解析', 'DOCX解析', '信息提取'],
    gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    featured: false,
  },
  {
    icon: 'Connection',
    title: 'AI岗位匹配',
    desc: '基于技能重合度和项目匹配度的智能算法，精准推荐最适合的岗位',
    tags: ['技能匹配', '项目匹配', '智能排序'],
    gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    featured: true,
  },
  {
    icon: 'Edit',
    title: 'AI简历优化',
    desc: '针对目标岗位生成个性化优化建议，使用STAR法则重写项目经历',
    tags: ['STAR法则', '优化建议', '前后对比'],
    gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    featured: false,
  },
  {
    icon: 'ChatDotRound',
    title: 'AI模拟面试',
    desc: '根据岗位自动生成面试题，支持用户回答和AI点评，提供改进建议',
    tags: ['技术面试', 'HR面试', '行为面试'],
    gradient: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
    featured: false,
  },
  {
    icon: 'TrendCharts',
    title: 'AI成长规划',
    desc: '根据用户当前能力生成30/60/90天成长路线，提供学习资源推荐',
    tags: ['30天计划', '60天计划', '90天计划'],
    gradient: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
    featured: false,
  },
  {
    icon: 'DataAnalysis',
    title: '能力雷达图',
    desc: '多维度能力评估和可视化展示，直观了解自身优势和短板',
    tags: ['ECharts', '可视化', '能力评估'],
    gradient: 'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)',
    featured: false,
  },
]

const techStack = [
  { icon: '☕', name: 'Java 21', desc: '后端语言' },
  { icon: '🍃', name: 'Spring Boot', desc: '后端框架' },
  { icon: '🤖', name: 'Spring AI', desc: 'AI集成' },
  { icon: '🐬', name: 'MySQL', desc: '数据库' },
  { icon: '💚', name: 'Vue3', desc: '前端框架' },
  { icon: '📦', name: 'Vite', desc: '构建工具' },
  { icon: '🎨', name: 'Element Plus', desc: 'UI组件库' },
  { icon: '📊', name: 'ECharts', desc: '数据可视化' },
  { icon: '🐳', name: 'Docker', desc: '容器部署' },
  { icon: '🧠', name: 'DeepSeek', desc: 'AI大模型' },
]

function scrollToFeatures() {
  featuresSection.value?.scrollIntoView({ behavior: 'smooth' })
}
</script>

<style scoped lang="scss">
.home-page {
  min-height: 100vh;
}

// Header
.home-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.header-inner {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;

  svg {
    width: 36px;
    height: 36px;
    background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);
    border-radius: 8px;
  }

  span {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
  }
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-btn {
  color: var(--text-regular) !important;

  &:hover {
    color: var(--primary-color) !important;
  }
}

// Hero
.hero-section {
  position: relative;
  padding: 160px 24px 100px;
  background: linear-gradient(135deg, #1a1f36 0%, #0d1025 100%);
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(ellipse at 20% 50%, rgba(26, 115, 232, 0.15) 0%, transparent 60%),
    radial-gradient(ellipse at 80% 50%, rgba(118, 75, 162, 0.15) 0%, transparent 60%);
}

.hero-content {
  position: relative;
  max-width: 900px;
  margin: 0 auto;
  text-align: center;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 20px;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 100px;
  color: rgba(255, 255, 255, 0.8);
  font-size: 13px;
  margin-bottom: 32px;
}

.hero-title {
  font-size: 56px;
  font-weight: 700;
  color: white;
  line-height: 1.2;
  margin-bottom: 24px;
}

.gradient-text {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.6);
  line-height: 1.8;
  margin-bottom: 48px;
}

.hero-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin-bottom: 64px;

  .el-button--primary {
    padding: 16px 32px;
    font-size: 16px;
  }

  .el-button--default {
    padding: 16px 32px;
    font-size: 16px;
    border-color: rgba(255, 255, 255, 0.3);
    color: white;

    &:hover {
      border-color: white;
      background: rgba(255, 255, 255, 0.1);
    }
  }
}

.hero-stats {
  display: flex;
  justify-content: center;
  gap: 64px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: white;
}

.stat-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.5);
  margin-top: 4px;
}

// Section通用
.section-header {
  text-align: center;
  margin-bottom: 48px;

  h2 {
    font-size: 36px;
    font-weight: 700;
    color: var(--text-primary);
    margin-bottom: 12px;
  }

  p {
    font-size: 16px;
    color: var(--text-secondary);
  }
}

// 流程
.flow-section {
  padding: 100px 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.flow-steps {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 32px;
}

.flow-step {
  text-align: center;
  position: relative;

  &::after {
    content: '';
    position: absolute;
    top: 40px;
    right: -16px;
    width: 32px;
    height: 2px;
    background: var(--primary-color);
    opacity: 0.3;
  }

  &:last-child::after {
    display: none;
  }
}

.step-number {
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
  margin: 0 auto 16px;
}

.step-icon {
  width: 80px;
  height: 80px;
  border-radius: 20px;
  background: linear-gradient(135deg, rgba(26, 115, 232, 0.1) 0%, rgba(26, 115, 232, 0.05) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  color: var(--primary-color);
}

.flow-step h3 {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.flow-step p {
  font-size: 14px;
  color: var(--text-secondary);
}

// 功能
.features-section {
  padding: 100px 24px;
  background: linear-gradient(180deg, #f8fafc 0%, #ffffff 100%);
}

.features-grid {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.feature-card {
  background: white;
  border-radius: var(--radius-lg);
  padding: 32px;
  transition: all 0.3s ease;
  border: 1px solid var(--border-light);

  &:hover {
    transform: translateY(-4px);
    box-shadow: var(--shadow-lg);
    border-color: transparent;
  }

  &.featured {
    grid-column: span 2;
    background: linear-gradient(135deg, #1a1f36 0%, #0d1025 100%);
    border: none;

    h3, p {
      color: white;
    }

    .feature-tags .el-tag {
      background: rgba(255, 255, 255, 0.1);
      border-color: rgba(255, 255, 255, 0.2);
      color: rgba(255, 255, 255, 0.8);
    }
  }
}

.feature-icon {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 12px;
}

.feature-card p {
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.8;
  margin-bottom: 20px;
}

.feature-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

// 技术栈
.tech-section {
  padding: 100px 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.tech-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
}

.tech-card {
  background: white;
  border-radius: var(--radius-md);
  padding: 24px 16px;
  text-align: center;
  border: 1px solid var(--border-light);
  transition: all 0.2s;

  &:hover {
    border-color: var(--primary-color);
    box-shadow: var(--shadow-sm);
  }
}

.tech-logo {
  font-size: 32px;
  margin-bottom: 12px;
}

.tech-name {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.tech-desc {
  font-size: 12px;
  color: var(--text-secondary);
}

// CTA
.cta-section {
  padding: 100px 24px;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);
}

.cta-content {
  max-width: 600px;
  margin: 0 auto;
  text-align: center;

  h2 {
    font-size: 36px;
    font-weight: 700;
    color: white;
    margin-bottom: 16px;
  }

  p {
    font-size: 16px;
    color: rgba(255, 255, 255, 0.8);
    margin-bottom: 32px;
  }

  .el-button {
    padding: 16px 40px;
    font-size: 16px;
    background: white;
    color: var(--primary-color);
    border: none;

    &:hover {
      background: rgba(255, 255, 255, 0.9);
    }
  }
}

// Footer
.home-footer {
  padding: 24px;
  text-align: center;
  background: #0d1025;
  color: rgba(255, 255, 255, 0.35);
  font-size: 13px;
}
</style>
