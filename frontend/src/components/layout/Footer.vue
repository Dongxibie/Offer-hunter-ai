<template>
  <footer class="app-footer">
    <div class="footer-content">
      <div class="footer-main">
        <div class="footer-brand">
          <div class="footer-logo">
            <svg viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect width="32" height="32" rx="6" fill="rgba(255,255,255,0.1)"/>
              <path d="M16 6L24 11V21L16 26L8 21V11L16 6Z" stroke="rgba(255,255,255,0.5)" stroke-width="1.5" fill="none"/>
              <path d="M16 11L20 13.5V18.5L16 21L12 18.5V13.5L16 11Z" fill="rgba(255,255,255,0.5)"/>
            </svg>
            <span>Offer Hunter AI</span>
          </div>
          <p class="footer-desc">AI驱动的智能求职助手，帮助大学生快速找到适合自己的岗位</p>
        </div>
        <div class="footer-links">
          <div class="link-group">
            <h4>功能</h4>
            <a href="/upload">简历上传</a>
            <a href="/jobs">岗位匹配</a>
            <a href="#">简历优化</a>
            <a href="#">模拟面试</a>
          </div>
          <div class="link-group">
            <h4>关于</h4>
            <a href="#">项目介绍</a>
            <a href="#">技术架构</a>
            <a href="#">使用指南</a>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <p>© 2026 Offer Hunter AI - 腾讯AI-HR培训营项目</p>
        <p class="tech-stack">Powered by Spring Boot + Vue3 + DeepSeek AI</p>
      </div>
    </div>
  </footer>
</template>

<style scoped lang="scss">
.app-footer {
  background: linear-gradient(180deg, #1a1f36 0%, #0d1025 100%);
  color: rgba(255, 255, 255, 0.7);
  padding: 60px 0 30px;
  margin-top: 80px;
}

.footer-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
}

.footer-main {
  display: flex;
  justify-content: space-between;
  margin-bottom: 48px;
}

.footer-brand {
  max-width: 360px;
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;

  span {
    font-size: 18px;
    font-weight: 600;
    color: white;
  }

  svg {
    width: 32px;
    height: 32px;
  }
}

.footer-desc {
  font-size: 14px;
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.5);
}

.footer-links {
  display: flex;
  gap: 80px;
}

.link-group {
  h4 {
    font-size: 15px;
    font-weight: 600;
    color: white;
    margin-bottom: 20px;
  }

  a {
    display: block;
    font-size: 14px;
    color: rgba(255, 255, 255, 0.5);
    margin-bottom: 12px;
    transition: color 0.2s;

    &:hover {
      color: white;
    }
  }
}

.footer-bottom {
  padding-top: 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.35);
}

.tech-stack {
  color: rgba(255, 255, 255, 0.25);
}
</style>
