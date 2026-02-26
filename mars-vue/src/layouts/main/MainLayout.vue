<script setup lang="ts">
import SideLayout from '@/layouts/common/SideLayout.vue'
import TopLayout from '@/layouts/common/TopLayout.vue'
</script>

<template>
  <div class="layout">
    <TopLayout />

    <div class="layout-body">
      <SideLayout />

      <main class="content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<style>
.layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.layout-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.content {
  flex: 1;
  padding: 24px;
  background: #f4f6f9;
  overflow-y: auto;
}
</style>