import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue'; // 메인 페이지
import Shop from '../views/Shop.vue'; // 쇼핑 페이지
import PetSitters from '../views/PetSitters.vue'; // 펫시터 목록 페이지
import Register from '../views/Register.vue'; // 회원가입 페이지
import EmailLogin from '../views/EmailLogin.vue'; // 로그인 페이지
import FindPassword from '../views/FindPassword.vue'; // 비밀번호 찾기 페이지
import UserProfile from '../views/UserProfile.vue'; // 회원정보 페이지

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/shop',
    name: 'Shop',
    component: Shop
  },
  {
    path: '/petsitters',
    name: '',
    component: PetSitters
  },
  {
    path: '/register',
    name: '',
    component: Register
  },
  {
    path: '/login/email',
    name: '',
    component: EmailLogin
  },
  {
    path: '/find-password',
    name: '',
    component: FindPassword
  },
  {
    path: '/user-profile',
    name: '',
    component: UserProfile
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
