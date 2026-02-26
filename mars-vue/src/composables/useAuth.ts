import { ref, computed } from 'vue'
import { api } from '@/api/api'
import { setAccessToken, removeAccessToken, getAccessToken } from '@/assets/js/interceptor'
import { useMenu } from '@/composables/useMenu'

const user = ref<any>(null)
// 토큰 존재 여부로 인증 판단
const isAuthenticated = computed(() => !!getAccessToken())
const { resetMenus } = useMenu()

export function useAuth() {

  const login = async (id: string, password: string) => {
    const res = await api.login(id, password)
    const data = res.data.data
    const accessToken = data.accessToken

    // AccessToken 토큰 설정 
    setAccessToken(accessToken)
    // 사용자 ID 지정
    user.value = data.userId

  }

  const fetchUser = async () => {
    const res = await api.getUserInfo()
    
    user.value = res.data
  }

  const logout = async () => {
    await api.logout()
    removeAccessToken()
    user.value = null
    resetMenus()
  }

  return {
    user,
    isAuthenticated,
    login,
    logout,
    fetchUser,
  }
}
