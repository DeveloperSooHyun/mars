<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuth } from '@/composables/useAuth'
import { useMessage } from '@/composables/useMessage'

/* =========================
 *  로그인 상태 관리
 * ========================= */
const { login } = useAuth()
const router = useRouter()
const route = useRoute() 
const message = useMessage()

/* =========================
 *  입력 정보 및 로딩
 * ========================= */
const username = ref('')
const password = ref('')
const remember = ref(false)
const loading = ref(false)

/* =========================
 *  로그인 처리 
 * ========================= */
const handleLogin = async () => {
  if (!username.value || !password.value) {
    message.warn('아이디와 비밀번호를 입력하세요.')
    return
  }

  try {
    loading.value = true
    await login(username.value, password.value)
    message.success('로그인 성공')
    router.push('/')
  } catch (e) {
    message.error('로그인 실패')
  } finally {
    loading.value = false
  }
}

/* =========================
 *  화면 초기 설정
 * ========================= */
onMounted(() => {
  if (route.query.reason === 'expired') {
    message.error('세션이 만료되었습니다.')
  }
})
</script>

<template>
 <div class="login-wrapper">
    <!-- 상단 타이틀 -->
    <div style="text-align: center; margin-bottom: 2rem;">
      <h1 style="font-size: 2.5rem; font-weight: 700; margin-bottom: 0.5rem; color: black;">Mars Admin</h1>
      <p style="font-size: 1rem; color: #4b5563;">관리자 시스템 로그인</p>
    </div>

    <!-- 로그인 폼 박스 -->
    <div
      style="background: white; padding: 2rem; border-radius: 1rem; box-shadow: 0 10px 30px rgba(0,0,0,0.1); width: 100%; max-width: 400px; display: flex; flex-direction: column; gap: 1rem;"
    >
      <!-- 아이디 -->
      <InputText
        placeholder="아이디를 입력해주세요"
        v-model="username"
        style="background: white; color: black; border-radius: 0.5rem;"
      />

      <!-- 비밀번호 -->
      <Password
        placeholder="비밀번호를 입력해주세요"
        v-model="password"
        toggleMask
        :feedback="false"
        class="custom-password"
      />

      <!-- 옵션 + 링크 -->
      <div style="display: flex; justify-content: space-between; align-items: center; font-size: 0.9rem;">
        <div style="display: flex; align-items: center; gap: 0.5rem;">
          <Checkbox v-model="remember" binary class="custom-checkbox"/>
          <label style="color: #4b5563; text-decoration: none;">아이디 저장</label>
        </div>
        <a href="#" style="color: #2563eb; text-decoration: none;">아이디 / 비밀번호 찾기</a>
      </div>

      <!-- 로그인 버튼 -->
      <Button
        label="로그인"
        icon="pi pi-sign-in"
        :loading="loading"
        style="width: 100%; height: 48px; border-radius: 0.75rem; font-weight: 600;"
        @click="handleLogin"
      />
    </div>

    <!-- 관리자 문의 -->
    <div style="margin-top: 1.5rem; font-size: 0.85rem; color: #6b7280; text-align: center;">
      문제가 있으신가요? 
      <a href="mailto:admin@mars.com" style="color: #2563eb; text-decoration: none;">관리자에게 문의하세요</a>
    </div>
  </div>
</template>


<style scoped>
/* Password 내부 input 접근 */
.custom-password :deep(input) {
  background-color: white !important;
  color: black !important;
  border-radius: 0.5rem;
}

.custom-checkbox :deep(.p-checkbox-box) {
  background-color: white !important;
  border-color: #d1d5db; /* 연한 회색 테두리 */
}
</style>

