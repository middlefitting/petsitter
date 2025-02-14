<template>
  <main>
    <h1>로그인</h1>
    <form @submit.prevent="validateForm">
      <!-- 이메일 입력 -->
      <div class="input-group">
        <label for="email">이메일</label>
        <input type="email" id="email" v-model="email" @input="validateEmail" placeholder="example@gmail.com" />
        <span v-if="emailError" class="error">{{ emailError }}</span>
      </div>

      <!-- 비밀번호 입력 -->
      <div class="input-group">
        <label for="password">비밀번호</label>
        <input type="password" id="password" v-model="password" @input="validatePassword" placeholder="비밀번호 입력" />
        <span v-if="passwordError" class="error">{{ passwordError }}</span>
      </div>

      <!-- 로그인 버튼 -->
      <button type="submit" class="btn">로그인</button>

      <!-- 비밀번호 찾기 링크 -->
      <p class="link" @click="goToForgotPassword">비밀번호 찾기</p>
    </form>
  </main>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import { toast } from 'vue3-toastify'
import axios from '@/plugins/axios'

const router = useRouter()
const authStore = useAuthStore()

const email = ref('')
const password = ref('')
const emailError = ref('')
const passwordError = ref('')

// 이메일 유효성 검사
const validateEmail = () => {
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  emailError.value = emailPattern.test(email.value) ? '' : '유효한 이메일 주소를 입력하세요.'
}

// 비밀번호 유효성 검사
const validatePassword = () => {
  passwordError.value = password.value.length >= 8 ? '' : '비밀번호는 8자 이상 입력해주세요.'
}

// 폼 유효성 검사
const isFormInvalid = computed(() => {
  return !email.value || !password.value || emailError.value || passwordError.value
})

// 로그인 처리
const validateForm = async () => {
  validateEmail()
  validatePassword()

  if (!isFormInvalid.value) {
    try {
      const response = await axios.post('/v1/users/login/email', {
        email: email.value,
        password: password.value
      })

      if (response.data.status === 'SUCCESS') {
        const userData = response.data.data
        // Pinia store에 사용자 정보 저장
        authStore.login({
          userId: userData.userId,
          email: userData.email,
          roleGroupId: userData.roleGroupId,
          roleGroupName: userData.roleGroupName,
          name: userData.name,
          phone: userData.phone
        })

        toast.success('로그인 성공!')
        setTimeout(() => {
          router.push('/')
        }, 1000)
      }
    } catch (error) {
      console.error('로그인 에러:', error)
      if (error.response?.data?.message) {
        toast.error(error.response.data.message)
      } else {
        toast.error('로그인에 실패했습니다.')
      }
    }
  }
}

// 비밀번호 찾기 페이지로 이동
const goToForgotPassword = () => {
  router.push('/find-password')
}
</script>


<style scoped>
/* 메인 컨텐츠 중앙 정렬 */
main {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  min-height: 70vh;
}

h1 {
  font-size: 32px;
  margin-bottom: 20px;
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
  width: 300px;
  padding: 10px;
  border: 1px solid black;
  font-size: 16px;
}

.error {
  color: red;
  font-size: 12px;
  margin-top: 5px;
}

/* 버튼 스타일 */
button {
  padding: 10px 15px;
  border: 1px solid black;
  font-size: 16px;
  cursor: pointer;
}

button:disabled {
  background-color: lightgray;
  cursor: not-allowed;
}

</style>
