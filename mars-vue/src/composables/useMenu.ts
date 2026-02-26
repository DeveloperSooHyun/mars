import { ref } from 'vue'
import { api } from '@/api/api'

export interface MenuItem {
  id: number
  label: string
  icon?: string
  path?: string
  children?: MenuItem[]
}

const menus = ref<MenuItem[]>([])
const loading = ref(false)
const loaded = ref(false)
const error = ref(false)

export function useMenu() {

  async function loadMenus() {
    if (loaded.value || loading.value) return

    loading.value = true
    error.value = false
    
    try {
        const res = await api.getMenus()
        menus.value = res.data
    } catch (e) {
        error.value = true
    } finally {
        loaded.value = true   // 성공/실패 무관하게 시도했음을 기록
        loading.value = false
    }
  }

  async function retryMenus() {
    if (loading.value) return   // ✅ 이미 호출 중이면 무시
    
    loaded.value = false
    await loadMenus()
  }

  function resetMenus() {
    menus.value = []
    loaded.value = false
    error.value = false
  }

  return {
    menus,
    loading,
    error,
    loadMenus,
    retryMenus,
    resetMenus,
  }
}