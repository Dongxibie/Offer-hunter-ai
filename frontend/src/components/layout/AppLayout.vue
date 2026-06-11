<template>
  <div class="app-layout">
    <Header />
    <main class="app-main">
      <div class="main-content">
        <router-view v-slot="{ Component, route }">
          <transition name="fade" mode="out-in">
            <component :is="Component" :key="route.path" />
          </transition>
        </router-view>
      </div>
    </main>
    <Footer />
  </div>
</template>

<script setup lang="ts">
import Header from './Header.vue'
import Footer from './Footer.vue'
</script>

<style scoped lang="scss">
.app-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-main {
  flex: 1;
  background: var(--bg-color);
}

.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px;
}
</style>
