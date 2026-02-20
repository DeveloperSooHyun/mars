import { ref } from 'vue'
import { api } from '@/api/api'
import { setAccessToken, removeAccessToken, getAccessToken } from '@/assets/js/interceptor'

const user = ref<any>(null)
const isAuthenticated = ref(!!getAccessToken())

export function useAuth() {

  const login = async (id: string, password: string) => {
    const res = await api.login(id, password)
    const accessToken = res.data.accessToken

    setAccessToken(accessToken)

    await fetchUser()
  }

  const fetchUser = async () => {
    const res = await api.getUserInfo()
    user.value = res.data
    isAuthenticated.value = true
  }

  const logout = async () => {
    await api.logout()
    removeAccessToken()
    user.value = null
    isAuthenticated.value = false
  }

  return {
    user,
    isAuthenticated,
    login,
    logout,
    fetchUser,
  }
}
