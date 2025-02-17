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
              @blur="validateEmail"
            />
            <span class="error-message" v-if="errors.email">{{ errors.email }}</span>
          </div>

          <div class="input-group">
            <label for="password">비밀번호</label>
            <input
              type="password"
              id="password"
              v-model="formData.password"
              required
              placeholder="8자 이상 입력해주세요"
              @blur="validatePassword"
            />
            <span class="error-message" v-if="errors.password">{{ errors.password }}</span>
          </div>

          <div class="input-group">
            <label for="name">이름</label>
            <input
              type="text"
              id="name"
              v-model="formData.name"
              required
              placeholder="이름을 입력하세요"
              @blur="validateName"
            />
            <span class="error-message" v-if="errors.name">{{ errors.name }}</span>
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
              @blur="validatePhone"
            />
            <span class="error-message" v-if="errors.phone">{{ errors.phone }}</span>
          </div>
<!--
          <div class="validation-guide" v-if="showValidationGuide">
            <h3>입력 조건</h3>
            <ul>
              <li :class="{ valid: !errors.email && formData.email }">
                이메일: 유효한 이메일 주소 형식 (예: example@email.com)
              </li>
              <li :class="{ valid: !errors.password && formData.password }">
                비밀번호: 8자 이상
              </li>
              <li :class="{ valid: !errors.name && formData.name }">
                이름: 2자 이상
              </li>
              <li :class="{ valid: !errors.phone && formData.phone }">
                전화번호: 010-0000-0000 형식
              </li>
            </ul>
          </div> -->

          <button type="submit" class="btn mb-20" :disabled="!isFormValid">가입하기</button>

          <div class="links-container text-center">
            <router-link to="/login/email" class="nav-link">이미 계정이 있으신가요? 로그인</router-link>
          </div>
        </form>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref, computed } from 'vue'
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

const errors = ref({
  email: '',
  password: '',
  name: '',
  phone: ''
})

const showValidationGuide = ref(true)

// 이메일 유효성 검사
const validateEmail = () => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!formData.value.email) {
    errors.value.email = '이메일은 필수 입력 항목입니다.'
  } else if (!emailRegex.test(formData.value.email)) {
    errors.value.email = '유효한 이메일 주소를 입력하세요.'
  } else {
    errors.value.email = ''
  }
}

// 비밀번호 유효성 검사
const validatePassword = () => {
  if (!formData.value.password) {
    errors.value.password = '비밀번호는 필수 입력 항목입니다.'
  } else if (formData.value.password.length < 8) {
    errors.value.password = '비밀번호는 8자 이상이어야 합니다.'
  } else {
    errors.value.password = ''
  }
}

// 이름 유효성 검사
const validateName = () => {
  if (!formData.value.name) {
    errors.value.name = '이름은 필수 입력 항목입니다.'
  } else if (formData.value.name.length < 2) {
    errors.value.name = '이름은 2자 이상이어야 합니다.'
  } else {
    errors.value.name = ''
  }
}

// 전화번호 유효성 검사
const validatePhone = () => {
  const phoneRegex = /^\d{3}-\d{3,4}-\d{4}$/
  if (!formData.value.phone) {
    errors.value.phone = '전화번호는 필수 입력 항목입니다.'
  } else if (!phoneRegex.test(formData.value.phone)) {
    errors.value.phone = '유효한 전화번호 형식을 입력하세요.'
  } else {
    errors.value.phone = ''
  }
}

// 전화번호 형식 자동 변환
const formatPhoneNumber = (event) => {
  let value = event.target.value.replace(/[^0-9]/g, '')
  if (value.length > 3 && value.length <= 7) {
    value = value.slice(0, 3) + '-' + value.slice(3)
  } else if (value.length > 7) {
    value = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7, 11)
  }
  formData.value.phone = value
  validatePhone()
}

// 폼 전체 유효성 검사
const validateForm = () => {
  validateEmail()
  validatePassword()
  validateName()
  validatePhone()
}

// 폼이 유효한지 확인
const isFormValid = computed(() => {
  return !errors.value.email &&
         !errors.value.password &&
         !errors.value.name &&
         !errors.value.phone &&
         formData.value.email &&
         formData.value.password &&
         formData.value.name &&
         formData.value.phone
})

const handleSubmit = async () => {
  validateForm()

  if (!isFormValid.value) {
    toast.error('입력 정보를 확인해주세요.')
    return
  }

  try {
    const response = await axios.post('/v1/users', formData.value)

    if (response.data.status === 'SUCCESS') {
      toast.success('회원가입이 완료되었습니다.')
      setTimeout(() => {
        router.push('/login/email')
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
.input-group {
  margin-bottom: 20px;
}

.error-message {
  color: var(--error);
  font-size: 0.875rem;
  margin-top: 4px;
  display: block;
}

.validation-guide {
  background-color: var(--bg-secondary);
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.validation-guide h3 {
  margin-bottom: 10px;
  font-size: 1rem;
}

.validation-guide ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.validation-guide li {
  margin-bottom: 5px;
  color: var(--gray);
  position: relative;
  padding-left: 20px;
}

.validation-guide li::before {
  content: "•";
  position: absolute;
  left: 0;
}

.validation-guide li.valid {
  color: #2ecc71;
}

.validation-guide li.valid::before {
  content: "✓";
}

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

button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
