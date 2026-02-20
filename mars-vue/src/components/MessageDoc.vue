<script setup lang="ts">
import { ref, reactive, defineExpose } from 'vue'

export type MessageSeverity = 'info' | 'success' | 'warn' | 'error'

interface ToastMessage {
  id: number
  severity: MessageSeverity
  content: string
}

const messages = reactive<ToastMessage[]>([])
let idCounter = 0

function addMessage(severity: MessageSeverity, content: string, duration = 3000) {
  const id = idCounter++
  messages.push({ id, severity, content })

  setTimeout(() => {
    const index = messages.findIndex(m => m.id === id)
    if (index !== -1) messages.splice(index, 1)
  }, duration)
}

defineExpose({ addMessage })
</script>

<template>
  <!-- 우측 하단 고정 -->
  <div style="position: fixed; bottom: 1rem; right: 1rem; display: flex; flex-direction: column; gap: 0.5rem; z-index: 9999;">
    <transition-group name="toast-fade" tag="div">
      <div
        v-for="msg in messages"
        :key="msg.id"
        style="
          min-width: 300px;
          min-height: 100px;
          display: flex;
          align-items: center; /* 세로 가운데 정렬 */
          justify-content: flex-start; /* 글씨 왼쪽 정렬 */
          padding: 0.5rem 1rem;
          border-radius: 0.5rem;
          color: white;
          font-size: 0.9rem;
          box-shadow: 0 2px 6px rgba(0,0,0,0.2);
        "
        class="px-4 py-2 rounded shadow-lg text-white"
        :style="{
          backgroundColor:
            msg.severity === 'info' ? '#2563eb' :
            msg.severity === 'success' ? '#16a34a' :
            msg.severity === 'warn' ? '#f59e0b' :
            msg.severity === 'error' ? '#dc2626' :
            '#000'
        }"
      >
        {{ msg.content }}
      </div>
    </transition-group>
  </div>
</template>

<style scoped>
.toast-fade-enter-active,
.toast-fade-leave-active {
  transition: all 0.3s ease;
}
.toast-fade-enter-from {
  opacity: 0;
  transform: translateY(20px);
}
.toast-fade-leave-to {
  opacity: 0;
  transform: translateY(20px);
}
</style>
