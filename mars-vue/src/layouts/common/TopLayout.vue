<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '@/composables/useAuth'
import { useMenu } from '@/composables/useMenu'

const router = useRouter()
const { user, logout } = useAuth()
const { resetMenus, loadMenus } = useMenu()

/* =========================
 *  상태
 * ========================= */
const alarmCount = ref(3)
const alarmVisible = ref(false)
const myPageVisible = ref(false)
const refreshing = ref(false)

/* =========================
 *  샘플 알람 데이터
 * ========================= */
const alarms = ref([
  { id: 1, message: '새로운 사용자가 등록되었습니다.', time: '5분 전',   read: false },
  { id: 2, message: '시스템 점검이 예정되어 있습니다.', time: '1시간 전', read: false },
  { id: 3, message: '권한 변경 요청이 있습니다.',       time: '3시간 전', read: true  },
])

/* =========================
 *  새로고침
 * ========================= */
async function handleRefresh() {
  if (refreshing.value) return
  refreshing.value = true
  try {
    resetMenus()
    await loadMenus()
    router.go(0)
  } finally {
    refreshing.value = false
  }
}

/* =========================
 *  알람
 * ========================= */
function toggleAlarm() {
  alarmVisible.value = !alarmVisible.value
  myPageVisible.value = false
}

function markAllRead() {
  alarms.value.forEach(a => (a.read = true))
  alarmCount.value = 0
}

/* =========================
 *  마이페이지
 * ========================= */
function toggleMyPage() {
  myPageVisible.value = !myPageVisible.value
  alarmVisible.value = false
}

function goToMyPage() {
  myPageVisible.value = false
  router.push('/mypage')
}

async function handleLogout() {
  myPageVisible.value = false
  await logout()
  router.push('/login')
}
</script>

<template>
  <header class="top-layout">

    <!-- 좌측 - 페이지 타이틀 슬롯 -->
    <div class="top-left">
      <slot name="title">
        <span class="page-title">Mars</span>
      </slot>
    </div>

    <!-- 우측 - 액션 버튼 -->
    <div class="top-right">

      <!-- 새로고침 -->
      <button class="top-btn" title="화면 새로고침" @click="handleRefresh">
        <i :class="['pi pi-refresh', { spin: refreshing }]" />
      </button>

      <!-- 알람 -->
      <div class="dropdown-wrap">
        <button class="top-btn" title="알림" @click="toggleAlarm">
          <i class="pi pi-bell" />
          <span v-if="alarmCount > 0" class="badge">{{ alarmCount }}</span>
        </button>

        <Transition name="dropdown">
          <div v-if="alarmVisible" class="dropdown">
            <div class="dropdown-header">
              <span class="dropdown-title">알림</span>
              <button v-if="alarmCount > 0" class="text-btn" @click="markAllRead">
                모두 읽음
              </button>
            </div>

            <ul class="alarm-list">
              <li
                v-for="alarm in alarms"
                :key="alarm.id"
                class="alarm-item"
                :class="{ unread: !alarm.read }"
              >
                <span class="alarm-dot" :class="{ visible: !alarm.read }" />
                <div class="alarm-body">
                  <p class="alarm-msg">{{ alarm.message }}</p>
                  <span class="alarm-time">{{ alarm.time }}</span>
                </div>
              </li>
            </ul>

            <div class="dropdown-footer">
              <button class="text-btn">전체 알림 보기</button>
            </div>
          </div>
        </Transition>
      </div>

      <!-- 구분선 -->
      <span class="divider" />

      <!-- 마이페이지 -->
      <div class="dropdown-wrap">
        <button class="top-btn user-btn" @click="toggleMyPage">
          <span class="user-avatar">{{ user?.charAt(0)?.toUpperCase() ?? 'U' }}</span>
          <span class="user-name">{{ user ?? '사용자' }}</span>
          <i :class="['pi', myPageVisible ? 'pi-angle-up' : 'pi-angle-down', 'angle-icon']" />
        </button>

        <Transition name="dropdown">
          <div v-if="myPageVisible" class="dropdown mypage-dropdown">
            <div class="mypage-info">
              <span class="mypage-avatar">{{ user?.charAt(0)?.toUpperCase() ?? 'U' }}</span>
              <div>
                <p class="mypage-name">{{ user ?? '사용자' }}</p>
                <span class="mypage-role">관리자</span>
              </div>
            </div>

            <div class="mypage-divider" />

            <ul class="mypage-menu">
              <li>
                <button class="mypage-item" @click="goToMyPage">
                  <i class="pi pi-user" />
                  마이페이지
                </button>
              </li>
              <li>
                <button class="mypage-item logout" @click="handleLogout">
                  <i class="pi pi-sign-out" />
                  로그아웃
                </button>
              </li>
            </ul>
          </div>
        </Transition>
      </div>

    </div>

    <!-- 외부 클릭 오버레이 -->
    <div
      v-if="alarmVisible || myPageVisible"
      class="overlay"
      @click="alarmVisible = false; myPageVisible = false"
    />

  </header>
</template>

<style scoped>
.top-layout {
  height: 56px;
  background: #1e2433;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 1.5rem;
  position: relative;
  z-index: 100;
  flex-shrink: 0;
}

/* 좌측 */
.top-left {
  display: flex;
  align-items: center;
}

.page-title {
  font-size: 0.9rem;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.85);
  letter-spacing: 0.02em;
}

/* 우측 */
.top-right {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

/* 공통 버튼 */
.top-btn {
  position: relative;
  width: 36px;
  height: 36px;
  border: none;
  background: transparent;
  color: rgba(255, 255, 255, 0.5);
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.95rem;
  transition: all 0.2s;
}

.top-btn:hover {
  background: rgba(255, 255, 255, 0.07);
  color: rgba(255, 255, 255, 0.9);
}

/* 새로고침 스핀 */
.spin {
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to   { transform: rotate(360deg); }
}

/* 배지 */
.badge {
  position: absolute;
  top: 4px;
  right: 4px;
  min-width: 15px;
  height: 15px;
  background: #ef4444;
  color: white;
  border-radius: 99px;
  font-size: 0.6rem;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 3px;
  line-height: 1;
}

/* 구분선 */
.divider {
  display: block;
  width: 1px;
  height: 20px;
  background: rgba(255, 255, 255, 0.1);
  margin: 0 0.5rem;
}

/* 유저 버튼 */
.user-btn {
  width: auto;
  padding: 0 0.75rem;
  gap: 0.5rem;
}

.user-avatar {
  width: 26px;
  height: 26px;
  border-radius: 50%;
  background: #3b82f6;
  color: white;
  font-size: 0.75rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.user-name {
  font-size: 0.85rem;
  color: rgba(255, 255, 255, 0.75);
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.angle-icon {
  font-size: 0.7rem !important;
  color: rgba(255, 255, 255, 0.35);
}

/* 드롭다운 공통 */
.dropdown-wrap {
  position: relative;
}

.dropdown {
  position: absolute;
  top: calc(100% + 10px);
  right: 0;
  background: #263045;
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 10px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.4);
  z-index: 200;
  overflow: hidden;
  min-width: 280px;
}

.dropdown-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 1.125rem 0.75rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
}

.dropdown-title {
  font-size: 0.875rem;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.85);
}

.text-btn {
  border: none;
  background: transparent;
  color: #60a5fa;
  font-size: 0.75rem;
  cursor: pointer;
  padding: 0.2rem 0.4rem;
  border-radius: 4px;
  transition: background 0.15s;
}

.text-btn:hover {
  background: rgba(96, 165, 250, 0.1);
}

.dropdown-footer {
  padding: 0.625rem 1rem;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
  text-align: center;
}

/* 알람 */
.alarm-list {
  list-style: none;
  margin: 0;
  padding: 0;
  max-height: 280px;
  overflow-y: auto;
}

.alarm-list::-webkit-scrollbar { width: 4px; }
.alarm-list::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 2px;
}

.alarm-item {
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
  padding: 0.875rem 1.125rem;
  transition: background 0.15s;
  border-left: 3px solid transparent;
}

.alarm-item:hover {
  background: rgba(255, 255, 255, 0.04);
}

.alarm-item.unread {
  border-left-color: #3b82f6;
  background: rgba(59, 130, 246, 0.06);
}

.alarm-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: transparent;
  flex-shrink: 0;
  margin-top: 5px;
  transition: background 0.2s;
}

.alarm-dot.visible {
  background: #3b82f6;
}

.alarm-body { flex: 1; }

.alarm-msg {
  font-size: 0.825rem;
  color: rgba(255, 255, 255, 0.75);
  margin: 0 0 0.25rem;
  line-height: 1.45;
}

.alarm-time {
  font-size: 0.73rem;
  color: rgba(255, 255, 255, 0.3);
}

/* 마이페이지 드롭다운 */
.mypage-dropdown { min-width: 200px; }

.mypage-info {
  display: flex;
  align-items: center;
  gap: 0.875rem;
  padding: 1.125rem;
}

.mypage-avatar {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: #3b82f6;
  color: white;
  font-size: 0.95rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.mypage-name {
  font-size: 0.875rem;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.85);
  margin: 0 0 0.2rem;
}

.mypage-role {
  font-size: 0.73rem;
  color: rgba(255, 255, 255, 0.35);
}

.mypage-divider {
  height: 1px;
  background: rgba(255, 255, 255, 0.06);
}

.mypage-menu {
  list-style: none;
  margin: 0;
  padding: 0.5rem;
}

.mypage-item {
  display: flex;
  align-items: center;
  gap: 0.7rem;
  width: 100%;
  padding: 0.625rem 0.75rem;
  border: none;
  background: transparent;
  border-radius: 7px;
  cursor: pointer;
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.85rem;
  text-align: left;
  transition: all 0.15s;
}

.mypage-item:hover {
  background: rgba(255, 255, 255, 0.07);
  color: rgba(255, 255, 255, 0.9);
}

.mypage-item.logout:hover {
  background: rgba(239, 68, 68, 0.1);
  color: #f87171;
}

.mypage-item .pi { font-size: 0.875rem; }

/* 오버레이 */
.overlay {
  position: fixed;
  inset: 0;
  z-index: 150;
}

/* 드롭다운 애니메이션 */
.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.15s ease;
}

.dropdown-enter-from,
.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-6px);
}
</style>