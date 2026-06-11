import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('@/views/Home.vue'),
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue'),
    },
    {
      path: '/upload',
      component: () => import('@/components/layout/AppLayout.vue'),
      children: [
        {
          path: '',
          name: 'ResumeUpload',
          component: () => import('@/views/ResumeUpload.vue'),
          meta: { requiresAuth: true, title: '上传简历' },
        },
      ],
    },
    {
      path: '/analysis/:id',
      component: () => import('@/components/layout/AppLayout.vue'),
      children: [
        {
          path: '',
          name: 'Analysis',
          component: () => import('@/views/Analysis.vue'),
          meta: { requiresAuth: true, title: 'AI简历分析' },
        },
      ],
    },
    {
      path: '/jobs',
      component: () => import('@/components/layout/AppLayout.vue'),
      children: [
        {
          path: '',
          name: 'JobMatch',
          component: () => import('@/views/JobMatch.vue'),
          meta: { requiresAuth: true, title: '岗位匹配' },
        },
      ],
    },
    {
      path: '/optimize/:resumeId/:jobId',
      component: () => import('@/components/layout/AppLayout.vue'),
      children: [
        {
          path: '',
          name: 'ResumeOptimize',
          component: () => import('@/views/ResumeOptimize.vue'),
          meta: { requiresAuth: true, title: '简历优化' },
        },
      ],
    },
    {
      path: '/interview/:resumeId/:jobId',
      component: () => import('@/components/layout/AppLayout.vue'),
      children: [
        {
          path: '',
          name: 'Interview',
          component: () => import('@/views/Interview.vue'),
          meta: { requiresAuth: true, title: '模拟面试' },
        },
      ],
    },
    {
      path: '/growth/:id',
      component: () => import('@/components/layout/AppLayout.vue'),
      children: [
        {
          path: '',
          name: 'GrowthPlan',
          component: () => import('@/views/GrowthPlan.vue'),
          meta: { requiresAuth: true, title: '成长规划' },
        },
      ],
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      redirect: '/',
    },
  ],
})

router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
