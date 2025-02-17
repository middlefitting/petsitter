import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue'; // 메인 페이지
import Shop from '../views/Shop.vue'; // 쇼핑 페이지
import PetSitters from '../views/PetSitters.vue'; // 펫시터 목록 페이지
import Register from '../views/Register.vue'; // 회원가입 페이지
import EmailLogin from '../views/EmailLogin.vue'; // 로그인 페이지
import FindPassword from '../views/FindPassword.vue'; // 비밀번호 찾기 페이지
import UserProfile from '../views/UserProfile.vue'; // 회원정보 페이지
import PetSitterDetail from '../views/PetSitterDetail.vue'; // 펫시터 상세 페이지
import PaymentTest from '../views/PaymentTest.vue';
import CodeManage from '@/views/admin/CodeManage.vue'
import Admin from '@/views/Admin.vue'

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
    name: 'PetSitters',
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
  },
  {
    path: '/petsitters/:id',
    name: 'PetSitterDetail',
    component: PetSitterDetail
  },
  {
    path: '/payment-test',
    name: 'PaymentTest',
    component: PaymentTest
  },
  {
    path: '/admin/code-manage',
    name: 'CodeManage',
    component: CodeManage,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/petsitters/:id/reservation',
    name: 'PetSitterReservation',
    component: () => import('../views/PetSitterReservation.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/reservations',
    name: 'Reservations',
    component: () => import('@/views/Reservations.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: Admin,
    meta: { requiresAuth: true, requiresAdmin: true }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
