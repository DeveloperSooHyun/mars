/// <reference types="vite/client" />

// Vue 파일 인식
declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

// 플러그인 타입 무시
declare module 'unplugin-vue-components/vite';
declare module '@primevue/auto-import-resolver';
