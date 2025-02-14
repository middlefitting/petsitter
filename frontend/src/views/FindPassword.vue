<template>
  <main>
    <div v-if="isPasswordSent">
      <!-- 성공 화면 -->
      <h2>🐶 가입하신 이메일로 임시 비밀번호를 전송했어요.</h2>
      <button @click="goToLogin" class="btn">로그인하러 가기</button>
    </div>

    <div v-else>
      <!-- 비밀번호 찾기 화면 -->
      <h1>비밀번호 찾기</h1>
      <form @submit.prevent="sendTemporaryPassword">
        <!-- 이메일 입력 -->
        <div class="input-group">
          <label for="email">이메일</label>
          <input
            type="email"
            id="email"
            v-model="email"
            @input="validateEmail"
            placeholder="example@gmail.com"
          />
          <span v-if="emailError" class="error">{{ emailError }}</span>
        </div>

        <!-- 임시 비밀번호 전송 버튼 -->
        <button type="submit" class="btn" :disabled="isFormInvalid">
          임시비밀번호 전송
        </button>

        <!-- 로그인 페이지 이동 링크 -->
        <div @click="goToLogin" class="link">로그인으로 돌아가기</div>
      </form>
    </div>
  </main>
</template>

<script>
import { toast } from "vue3-toastify";

export default {
  data() {
    return {
      email: '',
      emailError: '',
      isPasswordSent: false // ✅ 상태 추가 (임시 비밀번호 발송 여부)
    };
  },
  computed: {
    isFormInvalid() {
      return !!this.emailError;
    }
  },
  methods: {
    validateEmail() {
      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      this.emailError = emailPattern.test(this.email) ? '' : '유효한 이메일 주소를 입력하세요.';
    },
    sendTemporaryPassword() {
      this.validateEmail();
      if (this.isFormInvalid) {
        toast.error("올바른 이메일을 입력하세요!");
        return;
      }

      toast.success("임시 비밀번호를 이메일로 전송했습니다!");
      setTimeout(() => {
        this.isPasswordSent = true; // ✅ 성공 시 화면 변경
      }, 1000);
    },
    goToLogin() {
      this.$router.push("/login/email");
    }
  }
};
</script>

<style scoped>
main {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  min-height: 70vh;
}

h1, h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

form {
  width: 100%;
  max-width: 400px;
}

/* 입력 그룹 스타일 */
.input-group {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 15px;
}

label {
  font-weight: bold;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
}

.error {
  color: #dc3545;
  font-size: 12px;
  margin-top: 5px;
}

/* 버튼 스타일 */
.btn {
  padding: 10px 15px;
  border: 1px solid black;
  font-size: 16px;
  cursor: pointer;
}

.btn:disabled {
  background-color: lightgray;
  cursor: not-allowed;
}

</style>
