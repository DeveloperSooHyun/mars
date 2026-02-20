import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { createPinia } from 'pinia';

import PrimeVue from 'primevue/config';
import Aura from '@primeuix/themes/aura';
import 'primeicons/primeicons.css';

import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import Checkbox from 'primevue/checkbox';
import Button from 'primevue/button';

const app = createApp(App);

// PrimeVue 설정
app.use(PrimeVue, {
    theme: { preset: Aura }
});

// Pinia, Router
app.use(createPinia());
app.use(router);

// 전역 컴포넌트 등록 (로그인 화면에서 사용할 컴포넌트)
app.component('InputText', InputText);
app.component('Password', Password);
app.component('Checkbox', Checkbox);
app.component('Button', Button);

router.isReady().then(() => {
    app.mount('#app');
});


