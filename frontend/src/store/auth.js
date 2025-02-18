import { defineStore } from "pinia";
import axios from '@/plugins/axios'

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null,
    isAuthenticated: false,
  }),
  actions: {
    login(userData) {
      this.user = userData;
      this.isAuthenticated = true;
    },
    async logout() {
      try {
        // 서버에 로그아웃 요청
        await axios.post('/v1/users/logout');
        // 로컬 상태 초기화
        this.user = null;
        this.isAuthenticated = false;
      } catch (error) {
        console.error('로그아웃 실패:', error);
      }
    },
    updateUserInfo(name, phone) {
      this.user.name = name;
      this.user.phone = phone;
    },
    async withdrawUser() {
      try {
        await axios.delete('/v1/users/withdraw');
        this.logout();
      } catch (error) {
        console.error('회원탈퇴 실패:', error);
      }
    },
  },
});
