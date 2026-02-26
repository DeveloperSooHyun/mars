<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import MenuItem from './MenuItem.vue'

/* =========================
 *  타입 정의
 * ========================= */
export interface MenuItemType {
  id: number
  label: string
  icon?: string
  path?: string
  children?: MenuItemType[]
}

/* =========================
 *  Props / Emits
 * ========================= */
const props = defineProps<{
  item: MenuItemType
  depth: number
  collapsed: boolean
  openMap: Record<number, number>  // openKeys → openMap
  activePath: string
}>()

const emit = defineEmits<{
  (e: 'toggle', id: number, depth: number): void
}>()

const router = useRouter()

/* =========================
 *  상태
 * ========================= */
const hasChildren = computed(() => !!props.item.children?.length)

const isOpen = computed(() => props.openMap[props.depth] === props.item.id)  // openMap 기반으로 변경

const isActive = computed(() =>
  !!props.item.path && props.activePath === props.item.path
)

/* =========================
 *  경로 포함 활성화
 * ========================= */
const isChildActive = computed(() => {
  if (!props.item.path) return false
  if (props.item.path === '/') return false

  const base = props.item.path.endsWith('/')
    ? props.item.path
    : props.item.path + '/'

  return props.activePath.startsWith(base)
})


const paddingLeft = computed(() =>
  !props.collapsed ? `${1 + props.depth * 0.75}rem` : undefined
)

/* =========================
 *  이벤트
 * ========================= */
function handleClick() {
  // collapsed 상태에서도 path 있으면 이동 허용
  if (props.item.path && (!hasChildren.value || props.collapsed)) {
    router.push(props.item.path)
    return
  }
  if (hasChildren.value) {
    emit('toggle', props.item.id, props.depth)
  }
}
</script>

<template>
  <div class="menu-node">
    <div
      class="menu-item"
      :class="{
        active: isActive || isChildActive,
        collapsed,
        [`depth-${depth}`]: true,
      }"
      :style="{ paddingLeft }"
      :title="collapsed ? item.label : undefined"
      @click="handleClick"
    >
      <i v-if="item.icon" :class="['menu-icon', item.icon]" />
      <span v-if="!collapsed" class="menu-label">{{ item.label }}</span>
      <i
        v-if="hasChildren && !collapsed"
        class="menu-arrow pi"
        :class="isOpen ? 'pi-angle-down' : 'pi-angle-right'"
      />
    </div>

    <!-- 재귀 -->
    <transition name="slide">
      <div
        v-if="hasChildren && !collapsed && isOpen"
        class="menu-children"
      >
        <MenuItem
          v-for="child in item.children"
          :key="child.id"
          :item="child"
          :depth="depth + 1"
          :collapsed="collapsed"
          :open-map="openMap"
          :active-path="activePath"
          @toggle="emit('toggle', ...arguments)"
        />
      </div>
    </transition>
  </div>
</template>

<style scoped>
.menu-item {
  display: flex;
  align-items: center;
  gap: 0.65rem;
  padding: 0.65rem 1rem;
  cursor: pointer;
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.875rem;
  transition: all 0.2s ease;
  white-space: nowrap;
  border-left: 3px solid transparent;
}

.menu-item:hover {
  background: rgba(255, 255, 255, 0.07);
  color: rgba(255, 255, 255, 0.9);
}

.menu-item.active {
  background: rgba(59, 130, 246, 0.15);
  color: #60a5fa;
  border-left-color: #3b82f6;
}

/* depth 별 스타일 */
.menu-item.depth-1 {
  font-size: 0.825rem;
}

.menu-item.depth-2 {
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.5);
}

/* 접힌 상태 */
.menu-item.collapsed {
  justify-content: center;
  padding: 0.75rem;
  border-left: none;
}

.menu-item.collapsed.active {
  background: rgba(59, 130, 246, 0.25);
}

.menu-icon {
  font-size: 1rem;
  min-width: 1rem;
  text-align: center;
  flex-shrink: 0;
}

.menu-label {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
}

.menu-arrow {
  font-size: 0.75rem;
  margin-left: auto;
  transition: transform 0.2s;
  flex-shrink: 0;
}

.menu-children {
  background: rgba(0, 0, 0, 0.15);
}

/* 애니메이션 */
.slide-enter-active,
.slide-leave-active {
  transition: all 0.2s ease;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateY(-4px);
}
</style>