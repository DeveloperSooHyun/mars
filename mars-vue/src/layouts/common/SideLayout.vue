<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import MenuItem from '@/layouts/common/MenuItem.vue'
import type { MenuItemType } from '@/layouts/common/MenuItem.vue'
import { useMenu } from '@/composables/useMenu'

const route = useRoute()

/* =========================
 *  상태
 * ========================= */
const collapsed = ref(false)
const { menus, loading, error, loadMenus, retryMenus } = useMenu()
const openMap = ref<Record<number, number>>({})

/* =========================
 *   Collapse
 * ========================= */
function toggleCollapse() {
  collapsed.value = !collapsed.value
  if (collapsed.value) {
    openMap.value = {}
  } else {
    syncOpenKeysWithRoute()
  }
}

/* =========================
 *  메뉴 토글
 * ========================= */
function toggleMenu(id: number, depth: number) {
  if (openMap.value[depth] === id) {
    delete openMap.value[depth]
  } else {
    openMap.value[depth] = id
  }
}

/* =========================
 *  부모 키 탐색
 * ========================= */
function findParentKeys(
  items: MenuItemType[],
  path: string,
  parents: number[] = []
): number[] | null {
  for (const item of items) {
    if (item.path === path) return parents
    if (item.children?.length) {
      const result = findParentKeys(item.children, path, [...parents, item.id])
      if (result !== null) return result
    }
  }
  return null
}

/* =========================
 *  route 동기화
 * ========================= */
function syncOpenKeysWithRoute() {
  const parents = findParentKeys(menus.value, route.path)
  if (!parents) return

  const newMap: Record<number, number> = {}
  parents.forEach((id, depth) => {
    newMap[depth] = id
  })

  openMap.value = newMap
}

/* =========================
 *  재시도
 * ========================= */
async function retry() {
  await retryMenus()
  if (!error.value) syncOpenKeysWithRoute()
}

onMounted(async () => {
  await loadMenus()
  if (!error.value) {
    syncOpenKeysWithRoute()
  }
})

</script>

<template>
  <aside class="side-menu" :class="{ collapsed }">

    <!-- 로고 -->
    <div class="side-logo">
      <span v-if="!collapsed" class="logo-text">MARS</span>
      <span v-else class="logo-icon">M</span>
    </div>

    <!-- 토글 버튼 -->
    <button
      class="toggle-btn"
      :title="collapsed ? '메뉴 펼치기' : '메뉴 접기'"
      @click="toggleCollapse"
    >
      <i :class="collapsed ? 'pi pi-angle-right' : 'pi pi-angle-left'" />
    </button>

    <!-- 메뉴 목록 -->
    <nav class="menu-nav">

      <!-- 로딩 -->
      <div v-if="loading" class="menu-loading">
        <i class="pi pi-spin pi-spinner" />
      </div>

      <!-- 에러 -->
      <div v-else-if="error" class="menu-error">
        <i class="pi pi-exclamation-triangle" />
        <span>메뉴를 불러오지 못했습니다.</span>
        <button class="retry-btn" @click="retry">
          <i class="pi pi-refresh" />
          재시도
        </button>
      </div>

      <!-- 메뉴 -->
      <template v-else>
        <MenuItem
          v-for="menu in menus"
          :key="menu.id"
          :item="menu"
          :depth="0"
          :collapsed="collapsed"
          :open-map="openMap"
          :active-path="route.path"
          @toggle="toggleMenu"
        />
      </template>

    </nav>

  </aside>
</template>

<style scoped>
.side-menu {
  width: 280px;
  min-height: 100%;
  background: #1e2433;
  display: flex;
  flex-direction: column;
  transition: width 0.25s ease;
  overflow: visible;
  position: relative;
  flex-shrink: 0;
}

.side-menu.collapsed {
  width: 70px;
}

.side-logo {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  flex-shrink: 0;
}

.logo-text {
  font-size: 1.3rem;
  font-weight: 700;
  color: #60a5fa;
  letter-spacing: 0.15em;
}

.logo-icon {
  font-size: 1.4rem;
  font-weight: 700;
  color: #60a5fa;
}

.toggle-btn {
  position: absolute;
  top: 14px;
  right: -12px;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: #3b82f6;
  border: none;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
  z-index: 10;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
  transition: background 0.2s;
}

.toggle-btn:hover {
  background: #2563eb;
}

.menu-nav {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 0.5rem 0;
}

.menu-nav::-webkit-scrollbar {
  width: 4px;
}

.menu-nav::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 2px;
}

.menu-loading {
  display: flex;
  justify-content: center;
  padding: 2rem;
  color: rgba(255, 255, 255, 0.4);
}

.menu-error {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.75rem;
  padding: 2rem 1rem;
  color: rgba(255, 255, 255, 0.5);
  font-size: 0.85rem;
  text-align: center;
}

.menu-error .pi-exclamation-triangle {
  font-size: 1.5rem;
  color: #f59e0b;
}

.retry-btn {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0.4rem 1rem;
  border-radius: 6px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: transparent;
  color: rgba(255, 255, 255, 0.6);
  cursor: pointer;
  font-size: 0.8rem;
  transition: all 0.2s;
}

.retry-btn:hover {
  background: rgba(255, 255, 255, 0.08);
  color: white;
}
</style>