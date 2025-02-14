<template>
  <main>
    <h1>회원가입</h1>
    <form @submit.prevent="validateForm">
      <!-- 이메일 입력 -->
      <div class="input-group">
        <label for="email">이메일</label>
        <input type="email" id="email" v-model="email" @input="validateEmail" :disabled="emailVerified" placeholder="example@gmail.com" />
        <span v-if="emailError" class="error">{{ emailError }}</span>
      </div>

      <!-- 비밀번호 입력 -->
      <div class="input-group">
        <label for="password">비밀번호</label>
        <input type="password" id="password" v-model="password" @input="validatePassword" :disabled="emailVerified" placeholder="비밀번호 입력" />
        <span v-if="passwordError" class="error">{{ passwordError }}</span>
      </div>

      <!-- 비밀번호 확인 -->
      <div class="input-group">
        <label for="confirmPassword">비밀번호 확인</label>
        <input type="password" id="confirmPassword" v-model="confirmPassword" @input="validatePasswordMatch" :disabled="emailVerified" placeholder="비밀번호 확인" />
        <span v-if="confirmPasswordError" class="error">{{ confirmPasswordError }}</span>
      </div>

      <!-- 이름 입력 -->
      <div class="input-group">
        <label for="name">이름</label>
        <input type="text" id="name" v-model="name" @input="validateName" :disabled="emailVerified" placeholder="이름 입력" />
        <span v-if="nameError" class="error">{{ nameError }}</span>
      </div>

      <!-- 연락처 입력 -->
      <div class="input-group">
        <label for="contact">연락처</label>
        <input type="text" id="contact" v-model="contact" @input="validateContact" :disabled="emailVerified" placeholder="010-0000-0000" />
        <span v-if="contactError" class="error">{{ contactError }}</span>
      </div>

      <!-- 이메일 인증 & 회원가입 버튼 -->
      <div class="button-group">
        <button type="button" @click="sendEmailVerification" :disabled="emailVerified" class="btn">
          {{ emailVerified ? '이메일 인증' : '이메일 인증' }}
        </button>
        <button type="submit" class="btn" :disabled="isFormInvalid || !emailVerified">회원가입</button>
      </div>
    </form>
  </main>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { toast } from 'vue3-toastify'
import axios from '@/plugins/axios'

const router = useRouter()

// 폼 데이터
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const name = ref('')
const contact = ref('')  // phone으로 전송될 데이터

// 에러 상태
const emailError = ref('')
const passwordError = ref('')
const confirmPasswordError = ref('')
const nameError = ref('')
const contactError = ref('')
const emailVerified = ref(false)

// 유효성 검사 함수들
const validateEmail = () => {
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  emailError.value = emailPattern.test(email.value) ? '' : '유효한 이메일 주소를 입력하세요.'
}

const validatePassword = () => {
  passwordError.value = password.value.length >= 8 ? '' : '비밀번호는 8자 이상 입력해주세요.'
}

const validatePasswordMatch = () => {
  confirmPasswordError.value = password.value === confirmPassword.value ? '' : '비밀번호가 일치하지 않습니다.'
}

const validateName = () => {
  nameError.value = name.value.length >= 2 ? '' : '이름은 2자 이상 입력해주세요.'
}

const validateContact = () => {
  const contactPattern = /^010-\d{4}-\d{4}$/
  contactError.value = contactPattern.test(contact.value) ? '' : '010-0000-0000 형식으로 입력해주세요.'
}

const sendEmailVerification = () => {
  validateEmail()
  validatePassword()
  validatePasswordMatch()
  validateName()
  validateContact()

  if (isFormInvalid.value) {
    toast.error("유효한 데이터를 먼저 입력해주세요!")
    return
  }

  emailVerified.value = true
  toast.success("이메일 인증을 요청했습니다!")
}

const validateForm = async () => {
  validateEmail()
  validatePassword()
  validatePasswordMatch()
  validateName()
  validateContact()

  if (!isFormInvalid.value) {
    try {
      const response = await axios.post('/v1/users', {
        email: email.value,
        password: password.value,
        name: name.value,
        phone: contact.value  // contact를 phone으로 전송
      })

      if (response.data.status === 'SUCCESS') {
        toast.success('회원가입 성공! 로그인 페이지로 이동합니다.')
        setTimeout(() => {
          router.push('/login/email')
        }, 1000)
      } else {
        toast.error('회원가입에 실패했습니다.')
      }
    } catch (error) {
      console.error('회원가입 에러:', error)
      if (error.response?.data?.message) {
        toast.error(error.response.data.message)
      } else {
        toast.error('회원가입 중 오류가 발생했습니다.')
      }
    }
  }
}

// computed
const isFormInvalid = computed(() => {
  return !!(emailError.value ||
            passwordError.value ||
            confirmPasswordError.value ||
            nameError.value ||
            contactError.value)
})
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

input:disabled {
  background-color: #f0f0f0;
  cursor: not-allowed;
}

/* 에러 메시지 스타일 */
.error {
  color: red;
  font-size: 12px;
  margin-top: 5px;
}

/* 버튼 그룹 스타일 수정 */
.button-group {
  display: flex;
  flex-direction: column;  /* 버튼을 세로로 배치 */
  gap: 10px;
  width: 100%;  /* 부모 너비에 맞춤 */
}

/* 개별 버튼 스타일은 common.css에서 관리하므로 제거 */
</style>
