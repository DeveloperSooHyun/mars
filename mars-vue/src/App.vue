<script setup lang="ts">
import { ref, provide } from 'vue'
import MessageDoc from '@/components/MessageDoc.vue'
import { useAuth } from '@/composables/useAuth'

// ✅ LoginLayout, Login, MainLayout import 제거
const { isAuthenticated } = useAuth()

const messageRef = ref<InstanceType<typeof MessageDoc> | null>(null)

provide('message', {
  addMessage: (severity: any, content: string) => {
    messageRef.value?.addMessage(severity, content)
  }
})
</script>

<template>
  <!-- ✅ router-view 하나만 -->
  <router-view />
  <MessageDoc ref="messageRef" />
</template>

<style>
html, body, #app {
  margin: 0;
  padding: 0;
  height: 100%;
}
</style>