<template>
  <main>
    <h1>회원정보</h1>
    <form @submit.prevent="updateProfile">
      <!-- 이메일 표시 (수정 불가) -->
      <div class="input-group">
        <label>이메일</label>
        <div class="static-text">{{ authStore.user?.email }}</div>
      </div>

      <!-- 이름 입력 -->
      <div class="input-group">
        <label for="name">이름</label>
        <input
          type="text"
          id="name"
          v-model="userInfo.name"
          :placeholder="authStore.user?.name || '이름 입력'"
        />
      </div>

      <!-- 연락처 입력 -->
      <div class="input-group">
        <label for="phone">연락처</label>
        <input
          type="tel"
          id="phone"
          v-model="userInfo.phone"
          :placeholder="authStore.user?.phone || '010-0000-0000'"
          @input="formatPhoneNumber"
        />
      </div>

      <!-- 기본 정보 수정 버튼 -->
      <button type="submit" class="btn">회원정보 수정하기</button>

      <!-- 비밀번호 변경 섹션 -->
      <div class="password-section">
        <h2>비밀번호 변경</h2>
        <div class="input-group">
          <label for="currentPassword">현재 비밀번호</label>
          <input type="password" id="currentPassword" v-model="passwords.current" />
        </div>
        <div class="input-group">
          <label for="newPassword">새 비밀번호</label>
          <input type="password" id="newPassword" v-model="passwords.new" />
        </div>
        <button type="button" @click="updatePassword" class="btn btn-secondary">비밀번호 변경하기</button>
      </div>

      <!-- 회원탈퇴 섹션 -->
      <div class="withdrawal-section">
        <h2>회원탈퇴</h2>
        <p class="warning-text">탈퇴 시 모든 데이터가 삭제되며 복구할 수 없습니다.</p>
        <button type="button" @click="confirmWithdrawal" class="btn btn-danger">회원탈퇴</button>
      </div>
    </form>
  </main>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useAuthStore } from '@/store/auth'
import { toast } from 'vue3-toastify'
import { useRouter } from 'vue-router'
import axios from '@/plugins/axios'

const authStore = useAuthStore()
const router = useRouter()

const userInfo = ref({
  name: '',
  phone: ''
})

const passwords = ref({
  current: '',
  new: ''
})

// 디버깅을 위한 watch 추가
watch(() => authStore.user, (newValue) => {
  console.log('authStore.user 변경:', newValue)
}, { immediate: true })

onMounted(() => {
  console.log('컴포넌트 마운트 시 authStore.user:', authStore.user)
  console.log('authStore.user?.name:', authStore.user?.name)
  console.log('authStore.user?.phone:', authStore.user?.phone)

  if (authStore.user) {
    userInfo.value = {
      name: authStore.user.name || '',
      phone: authStore.user.phone || ''
    }
  }
})

// 전화번호 형식 자동 변환 (010-0000-0000)
const formatPhoneNumber = (event) => {
  let value = event.target.value.replace(/[^0-9]/g, '')
  if (value.length > 3 && value.length <= 7) {
    value = value.slice(0, 3) + '-' + value.slice(3)
  } else if (value.length > 7) {
    value = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7, 11)
  }
  userInfo.value.phone = value
}

// 회원정보 수정
const updateProfile = async () => {
  try {
    const userId = authStore.user.userId
    const response = await axios.put(`/v1/users/${userId}`, {
      name: userInfo.value.name,
      phone: userInfo.value.phone
    })

    if (response.data.status === 'SUCCESS') {
      // 응답 데이터로 authStore 업데이트
      const updatedUser = response.data.data
      await authStore.updateUserInfo(updatedUser.name, updatedUser.phone)
      toast.success('회원정보가 수정되었습니다.')
    } else {
      toast.error('회원정보 수정에 실패했습니다.')
    }
  } catch (error) {
    console.error('회원정보 수정 에러:', error)
    if (error.response?.data?.message) {
      toast.error(error.response.data.message)
    } else {
      toast.error('회원정보 수정 중 오류가 발생했습니다.')
    }
  }
}

// 비밀번호 변경
const updatePassword = async () => {
  if (!passwords.value.current || !passwords.value.new) {
    toast.error('비밀번호를 모두 입력해주세요.')
    return
  }

  try {
    const userId = authStore.user.userId // 현재 로그인한 사용자의 ID
    const response = await axios.patch(`/v1/users/${userId}/reset-password`, {
      password: passwords.value.current,    // 현재 비밀번호
      newPassword: passwords.value.new      // 새 비밀번호
    })

    if (response.data.status === 'SUCCESS') {
      toast.success('비밀번호가 변경되었습니다.')
      // 입력 필드 초기화
      passwords.value = { current: '', new: '' }
    } else {
      toast.error('비밀번호 변경에 실패했습니다.')
    }
  } catch (error) {
    console.error('비밀번호 변경 에러:', error)
    if (error.response?.data?.message) {
      toast.error(error.response.data.message)
    } else {
      toast.error('비밀번호 변경 중 오류가 발생했습니다.')
    }
  }
}

// 회원탈퇴 확인
const confirmWithdrawal = () => {
  if (confirm('정말 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
    handleWithdrawal()
  }
}

// 회원탈퇴 처리
const handleWithdrawal = async () => {
  try {
    const userId = authStore.user.userId
    const response = await axios.delete(`/v1/users/${userId}`, {
      data: {

      }
    })

    if (response.data.status === 'SUCCESS') {
      await authStore.withdrawUser()
      toast.success('회원탈퇴가 완료되었습니다.')
      setTimeout(() => {
        router.push('/')
      }, 1000)
    } else {
      toast.error('회원탈퇴에 실패했습니다.')
    }
  } catch (error) {
    console.error('회원탈퇴 에러:', error)
    if (error.response?.data?.message) {
      toast.error(error.response.data.message)
    } else {
      toast.error('회원탈퇴 중 오류가 발생했습니다.')
    }
  }
}
</script>

<style scoped>
:root {
  /* 메인 색상 */
  --primary-black: #2C3E50;    /* 짙은 차콜 블랙 */
  --primary-white: #F5F6FA;    /* 은은한 화이트 */

  /* 강조 색상 */
  --accent-gray: #95A5A6;      /* 중간 톤 그레이 */
  --light-gray: #ECF0F1;       /* 밝은 그레이 */

  /* 기능적 색상 */
  --error-color: #E74C3C;      /* 경고색 */
  --success-color: #27AE60;    /* 성공색 */
}

main {
  max-width: 300px;
  margin: 0 auto;
  padding: 20px;
  background-color: var(--primary-white);
  color: var(--primary-black);
}

.input-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--primary-black);
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid var(--accent-gray);
  border-radius: 4px;
  background-color: white;
  transition: border-color 0.3s ease;
}

input:focus {
  outline: none;
  border-color: var(--primary-black);
}

.static-text {
  width: 100%;
  padding: 10px;
  background-color: var(--light-gray);
  border-radius: 4px;
  color: var(--primary-black);
}

.btn {
  width: 100%;
  padding: 12px;
  background-color: var(--primary-black);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn:hover {
  background-color: #34495E;  /* 살짝 밝은 톤 */
}

.btn-secondary {
  background-color: var(--accent-gray);
}

.btn-secondary:hover {
  background-color: #7F8C8D;
}

.btn-danger {
  background-color: var(--error-color);
}

.btn-danger:hover {
  background-color: #C0392B;
}

.password-section, .withdrawal-section {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid var(--light-gray);
}

.warning-text {
  color: var(--error-color);
  font-size: 14px;
  margin-bottom: 15px;
}
</style>
