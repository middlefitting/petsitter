<template>
  <main>
    <div class="center-container">
      <div class="form-container">
        <h1 class="text-center">회원가입</h1>
        <form @submit.prevent="handleSubmit">
          <div class="input-group">
            <label for="email">이메일</label>
            <input
              type="email"
              id="email"
              v-model="formData.email"
              required
              placeholder="example@email.com"
            />
          </div>

          <div class="input-group">
            <label for="password">비밀번호</label>
            <input
              type="password"
              id="password"
              v-model="formData.password"
              required
              placeholder="8자 이상 입력해주세요"
            />
          </div>

          <div class="input-group">
            <label for="name">이름</label>
            <input
              type="text"
              id="name"
              v-model="formData.name"
              required
              placeholder="이름을 입력하세요"
            />
          </div>

          <div class="input-group">
            <label for="phone">연락처</label>
            <input
              type="tel"
              id="phone"
              v-model="formData.phone"
              required
              placeholder="010-0000-0000"
              @input="formatPhoneNumber"
            />
          </div>

          <button type="submit" class="btn mb-20">가입하기</button>

          <div class="links-container text-center">
            <router-link to="/login/email" class="nav-link">이미 계정이 있으신가요? 로그인</router-link>
          </div>
        </form>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { toast } from 'vue3-toastify'
import axios from '@/plugins/axios'

const router = useRouter()
const formData = ref({
  email: '',
  password: '',
  name: '',
  phone: ''
})

const formatPhoneNumber = (event) => {
  let value = event.target.value.replace(/[^0-9]/g, '')
  if (value.length > 3 && value.length <= 7) {
    value = value.slice(0, 3) + '-' + value.slice(3)
  } else if (value.length > 7) {
    value = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7, 11)
  }
  formData.value.phone = value
}

const handleSubmit = async () => {
  try {
    const response = await axios.post('/v1/users', formData.value)

    if (response.data.status === 'SUCCESS') {
      toast.success('회원가입이 완료되었습니다.')
      setTimeout(() => {
        router.push('/login')
      }, 1000)
    }
  } catch (error) {
    console.error('회원가입 에러:', error)
    if (error.response?.data?.message) {
      toast.error(error.response.data.message)
    } else {
      toast.error('회원가입에 실패했습니다.')
    }
  }
}
</script>

<style scoped>
.links-container {
  display: flex;
  justify-content: center;
}

.nav-link {
  color: var(--gray);
  text-decoration: none;
}

.nav-link:hover {
  color: var(--black);
}
</style>
