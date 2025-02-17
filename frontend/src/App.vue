<script>
import { useAuthStore } from "@/store/auth";
import { computed } from "vue";

export default {
  setup() {
    const authStore = useAuthStore();
    const isLoggedIn = computed(() => authStore.isAuthenticated);
    const isAdmin = computed(() => {
      return authStore.user?.roleGroups?.some(role => role.roleGroupName === 'admin') || false;
    });

    const logout = () => {
      authStore.logout();
    };

    return { isLoggedIn, isAdmin, logout };
  }
};
</script>

<template>
  <div class="container">
    <!-- 네비게이션 바 -->
    <nav>
      <div class="nav-left">
        <router-link to="/" class="logo">🐶</router-link>
        <!-- <router-link to="/shop" active-class="active">쇼핑몰</router-link> -->
        <router-link to="/petsitters" active-class="active">펫시터</router-link>
        <router-link v-if="isAdmin" to="/admin" active-class="active" class="admin-link">관리자</router-link>
      </div>
      <div class="nav-right">
        <template v-if="isLoggedIn">
          <router-link to="/user-profile" active-class="active">마이페이지</router-link>
          <a href="#" @click.prevent="logout">로그아웃</a>
        </template>
        <template v-else>
          <router-link to="/login/email" active-class="active">로그인</router-link>
          <router-link to="/register" active-class="active">회원가입</router-link>
        </template>
      </div>
    </nav>

    <!-- 메인 컨텐츠 -->
    <router-view />

    <!-- 푸터 -->
    <footer>
      <p>&copy; 2025 Pet Sitter. All Rights Reserved.</p>
      <a href="https://github.com/middlefitting/petsitter" target="_blank">
        <img src="https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png" alt="GitHub" class="github-icon" />
      </a>
    </footer>
  </div>
</template>


<style scoped>
/* 전체 화면을 구성하는 컨테이너 */
.container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* 네비게이션 바 */
nav {
  display: flex;
  justify-content: space-between;
  padding: 15px;
  border-bottom: 1px solid black;
}

.nav-left a, .nav-right a {
  margin: 0 10px;
  text-decoration: none;
  font-size: 18px;
  color: black;
}

.active {
  font-weight: bold;
}

/* 로고 스타일 */
.logo {
  font-size: 30px;
  text-decoration: none;
}

.logo:hover {
  transform: scale(1.1);
}

/* 푸터 스타일 */
footer {
  text-align: center;
  padding: 15px;
  border-top: 1px solid black;
  font-size: 14px;
  background-color: #f8f8f8;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 깃허브 아이콘 스타일 */
.github-icon {
  width: 30px;
  height: 30px;
  margin-top: 10px;
  transition: transform 0.3s ease;
}

.github-icon:hover {
  transform: scale(1.1);
}

.admin-link {
  color: #ff4757 !important; /* 관리자 메뉴 강조 */
  font-weight: 500;
}

.admin-link:hover {
  color: #ff6b81 !important;
}
</style>
