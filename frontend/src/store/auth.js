import { defineStore } from "pinia";

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
    logout() {
      this.user = null;
      this.isAuthenticated = false;
    },
    updateUserInfo(name, phone) {
      this.user.name = name;
      this.user.phone = phone;
    },
    async withdrawUser() {
      try {
        // await this.$axios.delete('/api/users/withdraw');
        this.logout();
      } catch (error) {
        console.error('회원탈퇴 실패:', error);
      }
    },
  },
});
