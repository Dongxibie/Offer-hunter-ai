<template>
  <header class="app-header">
    <div class="header-content">
      <!-- Logo -->
      <div class="logo" @click="router.push('/')">
        <div class="logo-icon">
          <svg viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect width="40" height="40" rx="8" fill="white" fill-opacity="0.15"/>
            <path d="M20 8L30 14V26L20 32L10 26V14L20 8Z" stroke="white" stroke-width="2" fill="none"/>
            <path d="M20 14L25 17V23L20 26L15 23V17L20 14Z" fill="white"/>
          </svg>
        </div>
        <div class="logo-text">
          <span class="title">Offer Hunter</span>
          <span class="subtitle">AI求职助手</span>
        </div>
      </div>

      <!-- 导航菜单 -->
      <nav class="nav-menu">
        <el-menu
          :default-active="activeMenu"
          mode="horizontal"
          :ellipsis="false"
          @select="handleMenuSelect"
        >
          <el-menu-item index="/">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/upload">
            <el-icon><Upload /></el-icon>
            <span>上传简历</span>
          </el-menu-item>
          <el-menu-item index="/jobs">
            <el-icon><Briefcase /></el-icon>
            <span>岗位匹配</span>
          </el-menu-item>
        </el-menu>
      </nav>

      <!-- 右侧操作区 -->
      <div class="header-actions">
        <template v-if="userStore.token">
          <el-dropdown trigger="click" @command="handleCommand">
            <div class="user-info">
              <el-avatar :size="32" :src="userStore.userInfo?.avatar">
                {{ userStore.userInfo?.username?.charAt(0)?.toUpperCase() }}
              </el-avatar>
              <span class="username">{{ userStore.userInfo?.username }}</span>
              <el-icon class="arrow"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="resume">
                  <el-icon><Document /></el-icon>
                  我的简历
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button type="primary" round @click="router.push('/login')">
            登录 / 注册
          </el-button>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => {
  return route.path
})

function handleMenuSelect(index: string) {
  router.push(index)
}

function handleCommand(command: string) {
  switch (command) {
    case 'profile':
      // TODO: 个人中心页面
      break
    case 'resume':
      router.push('/upload')
      break
    case 'logout':
      userStore.logout()
      router.push('/login')
      break
  }
}
</script>

<style scoped lang="scss">
.app-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);
  box-shadow: var(--shadow-md);
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  height: var(--header-height);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: opacity 0.2s;

  &:hover {
    opacity: 0.9;
  }
}

.logo-icon {
  width: 40px;
  height: 40px;
}

.logo-text {
  display: flex;
  flex-direction: column;
}

.logo-text .title {
  font-size: 18px;
  font-weight: 600;
  color: white;
  letter-spacing: 0.5px;
}

.logo-text .subtitle {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.75);
  margin-top: -2px;
}

.nav-menu {
  flex: 1;
  display: flex;
  justify-content: center;

  :deep(.el-menu) {
    background: transparent;
    border: none;
  }

  :deep(.el-menu--horizontal) {
    border: none;
  }

  :deep(.el-menu-item) {
    color: rgba(255, 255, 255, 0.85);
    height: var(--header-height);
    line-height: var(--header-height);
    border-bottom: none;
    font-size: 14px;

    &:hover {
      background: rgba(255, 255, 255, 0.1);
      color: white;
    }

    &.is-active {
      color: white;
      background: rgba(255, 255, 255, 0.15);
      border-bottom: none;
    }

    .el-icon {
      margin-right: 4px;
    }
  }
}

.header-actions {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: var(--radius-md);
  transition: background 0.2s;

  &:hover {
    background: rgba(255, 255, 255, 0.1);
  }
}

.username {
  color: white;
  font-size: 14px;
}

.arrow {
  color: rgba(255, 255, 255, 0.7);
  font-size: 12px;
}
</style>
