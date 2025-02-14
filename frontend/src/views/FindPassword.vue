<template>
  <main>
    <div class="center-container">
      <div class="form-container">
        <h1 class="text-center">비밀번호 찾기</h1>
        <form @submit.prevent="handleSubmit">
          <div class="input-group">
            <label for="email">이메일</label>
            <input
              type="email"
              id="email"
              v-model="email"
              required
              placeholder="가입하신 이메일을 입력하세요"
            />
          </div>

          <button type="submit" class="btn mb-20">비밀번호 찾기</button>

          <div class="links-container text-center">
            <router-link to="/login/email" class="nav-link">로그인으로 돌아가기</router-link>
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
const email = ref('')

const handleSubmit = async () => {
  try {
    const response = await axios.post('/v1/users/find-password', {
      email: email.value
    })

    if (response.data.status === 'SUCCESS') {
      toast.success('이메일로 임시 비밀번호가 발송되었습니다.')
      setTimeout(() => {
        router.push('/login')
      }, 2000)
    }
  } catch (error) {
    console.error('비밀번호 찾기 에러:', error)
    if (error.response?.data?.message) {
      toast.error(error.response.data.message)
    } else {
      toast.error('비밀번호 찾기에 실패했습니다.')
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
