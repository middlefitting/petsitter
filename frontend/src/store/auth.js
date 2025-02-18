import { defineStore } from "pinia";
import axios from '@/plugins/axios'

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null,
    isAuthenticated: false,
    isLoading: true,
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
    async checkLoginStatus() {
      try {
        const response = await axios.get('/v1/users/check-login');
        if (response.data.status === 'SUCCESS') {
          this.login(response.data.data);
        } else {
          this.isAuthenticated = false;
          this.user = null;
        }
      } catch (error) {
        console.error('로그인 체크 실패:', error);
        this.isAuthenticated = false;
        this.user = null;
      } finally {
        this.isLoading = false;
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
