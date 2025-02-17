<template>
  <main>
    <div class="center-container">
      <div class="form-container">
        <h1 class="text-center">로그인</h1>
        <form @submit.prevent="handleSubmit">
          <div class="input-group">
            <label for="email">이메일</label>
            <input
              type="email"
              id="email"
              v-model="email"
              required
              placeholder="example@email.com"
            />
          </div>

          <div class="input-group">
            <label for="password">비밀번호</label>
            <input
              type="password"
              id="password"
              v-model="password"
              required
              placeholder="비밀번호를 입력하세요"
            />
          </div>

          <button type="submit" class="btn mb-20">로그인</button>

          <div class="links-container text-center">
            <router-link to="/register" class="nav-link">회원가입</router-link>
            <router-link to="/find-password" class="nav-link">비밀번호 찾기</router-link>
          </div>
        </form>
      </div>
    </div>
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
const handleSubmit = async () => {
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
          roleGroups: userData.roleGroups,
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
</script>

<style scoped>
.input-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  color: var(--black);
}

.links-container {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.nav-link {
  color: var(--gray);
  text-decoration: none;
}

.nav-link:hover {
  color: var(--black);
}
</style>
