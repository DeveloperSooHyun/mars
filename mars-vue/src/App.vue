<script setup lang="ts">
import { ref, provide } from 'vue'
import MessageDoc from '@/components/MessageDoc.vue'
import LoginLayout from '@/layouts/auth/LoginLayout.vue'
import Login from '@/views/auth/Login.vue'
import MainLayout from '@/layouts/main/MainLayout.vue'
import { useAuth } from '@/composables/useAuth'
import type { ComponentPublicInstance } from 'vue'

const { isAuthenticated } = useAuth()

const messageRef = ref<InstanceType<typeof MessageDoc> | null>(null)

provide('message', {
  addMessage: (severity: any, content: string) => {
    messageRef.value?.addMessage(severity, content)
  }
})
</script>

<template>
  <LoginLayout v-if="!isAuthenticated">
    <Login />
  </LoginLayout>
  <MainLayout v-else />

  <MessageDoc ref="messageRef" />
</template>

<style>
html, body, #app {
  margin: 0;
  padding: 0;
  height: 100%;
}
</style>