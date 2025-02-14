import { createApp } from 'vue';
import { createPinia } from "pinia"; // ✅ 올바른 Pinia 임포트
import App from './App.vue';
import router from './router'; // Vue Router
import Toast from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';
import './assets/styles/common.css'
import axios from './plugins/axios'

const app = createApp(App);
const pinia = createPinia(); // ✅ Pinia 인스턴스 생성

app.use(pinia); // 🔥 Pinia 등록
app.use(router); // Vue Router 등록
app.use(Toast); // 토스트 사용

app.config.globalProperties.$axios = axios

app.mount("#app");
