import { createRouter, createWebHistory } from 'vue-router'
import { useAuth } from '@/composables/useAuth'

import LoginLayout from '@/layouts/auth/LoginLayout.vue'
import MainLayout from '@/layouts/main/MainLayout.vue'
import Login from '@/views/auth/Login.vue'
import Register from '@/views/auth/Register.vue'
import FindUserInfo from '@/views/auth/FindUserInfo.vue'
import Dashboard from '@/views/common/Dashboard.vue'
import NotFound from '@/views/common/NotFound.vue'

const routes = [
  {
    path: '/login',
    component: LoginLayout,
    children: [
          { path: '', name: 'Login', component: Login },
          { path: 'register', name: 'Register', component: Register },
          { path: 'findUserInfo', name: 'FindUserInfo', component: FindUserInfo },
    ]
  },
  {
    path: '/',
    component: MainLayout,
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: Dashboard
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: NotFound
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, _from, next) => {
  const { isAuthenticated } = useAuth()
  
  if (to.meta.requiresAuth && !isAuthenticated.value) {
    next('/login')
  }
  else if (to.path.startsWith('/login') && isAuthenticated.value) {
    next('/')
  }
  else {
    next()
  }
})

export default router