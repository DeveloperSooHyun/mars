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

// ✅ 추가 - flat 배열 → tree 구조 변환
function buildTree(flatList: any[]): MenuItem[] {
  const map = new Map<string, MenuItem>()
  const roots: MenuItem[] = []

  flatList.forEach(item => {
    map.set(
        item.MENU_ID, 
        {
            id:    item.MENU_NO,
            label: item.MENU_NAME,
            path:  item.MENU_URL ?? undefined,
            icon:  item.MENU_ICON ?? undefined,
            children: []
        }
    )
  })

  flatList.forEach(item => {
    const current = map.get(item.MENU_ID)!
    if (item.MENU_LEVEL === 1) {
      roots.push(current)
    } else {
      const parentId = item.MENU_ID.slice(0, -2)
      const parent = map.get(parentId)
      if (parent) parent.children!.push(current)
    }
  })

  map.forEach(item => {
    if (item.children?.length === 0) delete item.children
  })

  return roots
}

export function useMenu() {

  async function loadMenus() {
    if (loaded.value || loading.value) return

    loading.value = true
    error.value = false
    
    try {
        const res = await api.getMenus()
        menus.value = buildTree(res.data.data)
    } catch (e) {
        error.value = true
    } finally {
        loaded.value = true
        loading.value = false
    }
  }

  async function retryMenus() {
    if (loading.value) return
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