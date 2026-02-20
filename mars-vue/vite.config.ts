// 파일 경로, 플러그인, PrimeVue components Import
import { fileURLToPath, URL } from 'node:url';
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import Components from 'unplugin-vue-components/vite';
import { PrimeVueResolver } from '@primevue/auto-import-resolver';
import path from 'path';

export default defineConfig({
  // 배포 시 기본 URL
  base: '/',
  build: {
    outDir: 'dist',         // 빌드 결과물 디렉토리
    emptyOutDir: true,      // 매번 빌드 시 dist 폴더 초기화 여부
    assetsDir: '',          // 이미지/폰트/CSS 파일 경로 (dist 폴더)
    cssCodeSplit: true,     // CSS 파일을 컴포넌트 단위로 분리
    rollupOptions: {
      input: {
        main: path.resolve(__dirname, 'index.html')
      },
      output: { // JS, 청크, CSS, 이미지 등 파일 규칙 지정 (hash 포함 시 캐싱 문제 방지)
        entryFileNames: 'js/[name]-[hash].js',
        chunkFileNames: 'js/[name]-[hash].js',
        assetFileNames: ({ name }) => {
          if (/\.(css)$/.test(name ?? '')) return 'css/[name]-[hash][extname]';
          if (/\.(png|jpe?g|svg|gif|webp)$/.test(name ?? '')) return 'images/[name]-[hash][extname]';
          if (/\.(woff2?|eot|ttf|otf)$/.test(name ?? '')) return 'fonts/[name]-[hash][extname]';
          return 'assets/[name]-[hash][extname]';
        }
      }
    }
  },
  plugins: [ //Vue3 + PrimeVue 컴포넌트 자동 Import
    vue(),
    Components({
      resolvers: [PrimeVueResolver()]
    })
  ],
  server: {
    port: 18010, // 개발 서버 포트
    proxy: { // /api 요청을 백엔드 17010 포트로 전달
      '/api': {
        target: 'http://localhost:17010',
        changeOrigin: true,
        secure: false,
        rewrite: path => path.replace(/^\/api/, '') // /api prefix 제거 후 백엔드로 전달
      }
    }
  },
  resolve: { // @를 src/ 경로로 매핑
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
});
