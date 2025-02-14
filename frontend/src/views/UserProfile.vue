<template>
  <main>
    <div class="container">
      <div class="tabs mb-20">
        <button
          class="tab-btn"
          :class="{ active: activeTab === 'profile' }"
          @click="activeTab = 'profile'"
        >
          회원정보 관리
        </button>
        <button
          class="tab-btn"
          :class="{ active: activeTab === 'petsitter' }"
          @click="activeTab = 'petsitter'"
        >
          펫시터 등록
        </button>
      </div>

      <!-- 회원정보 관리 탭 -->
      <div v-if="activeTab === 'profile'" class="form-container">
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
      </div>

      <!-- 펫시터 등록 탭 -->
      <div v-else class="form-container">
        <h2 class="text-center mb-20">펫시터 정보 등록</h2>
        <form @submit.prevent="registerPetSitter">
          <div class="form-group">
            <label>위치</label>
            <select v-model="petsitterForm.location" class="form-select" required>
              <option value="">지역을 선택하세요</option>
              <option value="서울">서울</option>
              <option value="경기">경기</option>
            </select>
          </div>

          <div class="form-group">
            <label>돌봄 품종</label>
            <div class="pet-type-selectors">
              <div class="select-group">
                <select v-model="selectedPetType" class="form-select">
                  <option value="">동물 종류 선택</option>
                  <option value="강아지">강아지</option>
                  <option value="고양이">고양이</option>
                </select>
                <select
                  v-if="selectedPetType === '강아지'"
                  v-model="selectedBreed"
                  class="form-select"
                >
                  <option value="">세부 품종 선택</option>
                  <option value="도베르만">도베르만</option>
                  <option value="진돗개">진돗개</option>
                  <option value="말티즈">말티즈</option>
                  <!-- 더 많은 품종 추가 가능 -->
                </select>
                <button
                  type="button"
                  class="btn-add"
                  @click="addPetType"
                >
                  추가
                </button>
              </div>
              <div class="selected-types">
                <span
                  v-for="(type, index) in petsitterForm.petTypes"
                  :key="index"
                  class="type-tag"
                >
                  {{ type.mainType }} - {{ type.breed }}
                  <button
                    type="button"
                    class="btn-remove"
                    @click="removePetType(index)"
                  >
                    ×
                  </button>
                </span>
              </div>
            </div>
          </div>

          <div class="form-group">
            <label>돌봄 크기</label>
            <div class="checkbox-group">
              <label class="checkbox-label">
                <input type="checkbox" v-model="petsitterForm.petSizes" value="소형">
                소형(10kg 이하)
              </label>
            </div>
          </div>

          <div class="form-group">
            <label>서비스 종류 및 요금</label>
            <div class="service-selectors">
              <div class="select-group">
                <select v-model="selectedService" class="form-select">
                  <option value="">서비스 선택</option>
                  <option value="산책">산책</option>
                  <option value="돌봄">돌봄</option>
                  <option value="훈련">훈련</option>
                </select>
                <input
                  type="number"
                  v-model="servicePrice"
                  class="form-select"
                  placeholder="시급 (원)"
                >
                <button
                  type="button"
                  class="btn-add"
                  @click="addService"
                >
                  추가
                </button>
              </div>
              <div class="selected-types">
                <span
                  v-for="(service, index) in petsitterForm.services"
                  :key="index"
                  class="type-tag"
                >
                  {{ service.type }} - {{ service.price.toLocaleString() }}원/시간
                  <button
                    type="button"
                    class="btn-remove"
                    @click="removeService(index)"
                  >
                    ×
                  </button>
                </span>
              </div>
            </div>
          </div>

          <div class="form-group">
            <label>근무 가능 시간</label>
            <div class="working-hours">
              <div class="weekday-selector">
                <button
                  v-for="day in weekdays"
                  :key="day.value"
                  class="day-btn"
                  :class="{ active: selectedDay === day.value }"
                  @click.prevent="selectedDay = day.value"
                  type="button"
                >
                  {{ day.label }}
                </button>
              </div>

              <div class="time-grid"
                @mousedown.prevent="startDragging"
                @mouseup.prevent="stopDragging"
                @mouseleave="stopDragging"
              >
                <button
                  v-for="time in timeSlots"
                  :key="time"
                  class="time-btn"
                  :class="{
                    selected: isTimeSelected(selectedDay, time)
                  }"
                  @mouseenter="handleDragSelect(selectedDay, time)"
                  type="button"
                >
                  {{ time }}
                </button>
              </div>
            </div>
          </div>

          <button type="submit" class="btn">등록하기</button>
        </form>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
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

const activeTab = ref('profile')

const selectedPetType = ref('')
const selectedBreed = ref('')

const selectedService = ref('')
const servicePrice = ref('')

const petsitterForm = ref({
  location: '',
  petTypes: [],
  petSizes: [],
  services: [],
  workingHours: {
    mon: Array(24).fill(false),
    tue: Array(24).fill(false),
    wed: Array(24).fill(false),
    thu: Array(24).fill(false),
    fri: Array(24).fill(false),
    sat: Array(24).fill(false),
    sun: Array(24).fill(false)
  }
})

const weekdays = [
  { value: 'mon', label: '월' },
  { value: 'tue', label: '화' },
  { value: 'wed', label: '수' },
  { value: 'thu', label: '목' },
  { value: 'fri', label: '금' },
  { value: 'sat', label: '토' },
  { value: 'sun', label: '일' }
]

const selectedDay = ref('mon')

const timeSlots = [
  '00:00', '01:00', '02:00', '03:00', '04:00', '05:00',
  '06:00', '07:00', '08:00', '09:00', '10:00', '11:00',
  '12:00', '13:00', '14:00', '15:00', '16:00', '17:00',
  '18:00', '19:00', '20:00', '21:00', '22:00', '23:00'
]

const isDragging = ref(false)
const dragStartValue = ref(null)
const dragStartTime = ref(null)

const startDragging = (event) => {
  isDragging.value = true
  dragStartValue.value = null
  const timeBtn = event.target.closest('.time-btn')
  if (timeBtn) {
    const time = timeBtn.textContent.trim()
    dragStartTime.value = timeSlots.indexOf(time)
    handleDragSelect(selectedDay.value, time)
  }
}

const stopDragging = () => {
  isDragging.value = false
  dragStartValue.value = null
  dragStartTime.value = null
}

const handleDragSelect = (day, time) => {
  if (!isDragging.value) return

  const currentTimeIndex = timeSlots.indexOf(time)

  if (dragStartValue.value === null) {
    dragStartValue.value = isTimeSelected(day, time)
    petsitterForm.value.workingHours[day][currentTimeIndex] = !dragStartValue.value
    return
  }

  const startIdx = Math.min(dragStartTime.value, currentTimeIndex)
  const endIdx = Math.max(dragStartTime.value, currentTimeIndex)

  for (let i = startIdx; i <= endIdx; i++) {
    petsitterForm.value.workingHours[day][i] = !dragStartValue.value
  }
}

const addPetType = () => {
  petsitterForm.value.petTypes.push({
    mainType: selectedPetType.value,
    breed: selectedBreed.value
  })
  selectedBreed.value = ''
  selectedPetType.value = ''
}

const removePetType = (index) => {
  petsitterForm.value.petTypes.splice(index, 1)
}

const addService = () => {
  petsitterForm.value.services.push({
    type: selectedService.value,
    price: Number(servicePrice.value)
  })
  selectedService.value = ''
  servicePrice.value = ''
}

const removeService = (index) => {
  petsitterForm.value.services.splice(index, 1)
}

const isTimeSelected = (day, time) => {
  const timeIndex = timeSlots.indexOf(time)
  return petsitterForm.value.workingHours[day][timeIndex]
}

const formatTimeRange = (times) => {
  if (times.length === 0) return ''
  return times.join(', ')
}

const activeSchedules = computed(() => {
  return Object.entries(petsitterForm.value.workingHours)
    .filter(([_, times]) => times.length > 0)
    .map(([day, times]) => ({ day, times }))
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

const registerPetSitter = async () => {
  try {
    // 여기서만 유효성 검사 수행
    if (!petsitterForm.value.location) {
      toast.error('위치를 선택해주세요.')
      return
    }
    if (petsitterForm.value.petTypes.length === 0) {
      toast.error('돌봄 품종을 선택해주세요.')
      return
    }
    if (petsitterForm.value.services.length === 0) {
      toast.error('서비스를 선택해주세요.')
      return
    }

    // API 호출 로직
    const response = await axios.post('/v1/petsitters', petsitterForm.value)

    if (response.data.status === 'SUCCESS') {
      toast.success('펫시터 정보가 등록되었습니다.')
      // 등록 성공 후 초기화 또는 리다이렉트
      router.push('/petsitters')
    }
  } catch (error) {
    console.error('펫시터 등록 에러:', error)
    if (error.response?.data?.message) {
      toast.error(error.response.data.message)
    } else {
      toast.error('등록에 실패했습니다.')
    }
  }
}
</script>

<style scoped>
main {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background-color: var(--bg-primary);
  color: var(--black);
}

.container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
}

.tabs {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.tab-btn {
  padding: 10px 20px;
  border: 2px solid var(--black);
  background: none;
  color: var(--black);
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab-btn.active {
  background: var(--black);
  color: var(--white);
}

.form-container {
  padding: 20px;
  background-color: var(--bg-secondary);
  border-radius: 4px;
  max-width: 600px;
  width: 100%;
  overflow-x: hidden;
}

.input-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--black);
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid var(--gray-light);
  border-radius: 4px;
  background-color: var(--white);
  transition: border-color 0.3s ease;
}

input:focus {
  outline: none;
  border-color: var(--gray-dark);
}

.static-text {
  width: 100%;
  padding: 10px;
  background-color: var(--bg-secondary);
  border-radius: 4px;
  color: var(--gray-dark);
}

.btn {
  width: auto;
  min-width: 200px;
  padding: 12px;
  background-color: var(--black);
  color: var(--white);
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: opacity 0.2s ease;
  display: block;
  margin: 0 auto;
}

.btn:hover {
  opacity: 0.9;
}

.btn-secondary {
  background-color: var(--gray);
}

.btn-secondary:hover {
  opacity: 0.9;
}

.btn-danger {
  background-color: var(--error);
}

.btn-danger:hover {
  opacity: 0.9;
}

.password-section, .withdrawal-section {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid var(--gray-light);
  text-align: center;
}

.warning-text {
  color: var(--error);
  font-size: 14px;
  margin-bottom: 15px;
}

.form-group {
  margin-bottom: 20px;
}

.form-select {
  width: 100%;
  padding: 12px;
  border: 1px solid var(--gray-light);
  border-radius: 4px;
  background-color: var(--white);
}

.checkbox-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.checkbox-label input[type="checkbox"] {
  width: auto;
  margin: 0;
}

.pet-type-selectors {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.select-group {
  display: flex;
  gap: 10px;
}

.select-group .form-select,
.select-group input[type="number"] {
  flex: 1;
}

.btn-add {
  padding: 0 15px;
  background-color: var(--black);
  color: var(--white);
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.selected-types {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.type-tag {
  display: inline-flex;
  align-items: center;
  padding: 4px 8px;
  background-color: var(--gray-light);
  border-radius: 4px;
  font-size: 14px;
}

.btn-remove {
  background: none;
  border: none;
  margin-left: 4px;
  cursor: pointer;
  padding: 0 4px;
  font-size: 16px;
  color: var(--gray);
}

.btn-remove:hover {
  color: var(--error);
}

.service-selectors {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.working-hours {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.weekday-selector {
  display: flex;
  gap: 8px;
  margin-bottom: 10px;
  width: 100%;
}

.day-btn {
  padding: 8px 12px;
  border: 1px solid var(--gray-light);
  background: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s ease;
  flex: 1;
  min-width: 0;
}

.day-btn.active {
  background-color: var(--black);
  color: var(--white);
  border-color: var(--black);
}

.time-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 8px;
  width: 100%;
  overflow-x: auto;
  user-select: none;
}

.time-btn {
  padding: 8px;
  min-width: 70px;
  border: 1px solid var(--gray-light);
  background: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s ease;
  user-select: none;
}

.time-btn.selected {
  background-color: var(--gray-light);
  border-color: var(--gray);
}

@media (max-width: 768px) {
  .tabs {
    flex-direction: column;
    align-items: center;
  }

  .tab-btn {
    width: 200px;
  }

  .select-group {
    flex-direction: column;
  }

  .btn {
    width: 100%;
    min-width: auto;
  }

  .time-grid {
    grid-template-columns: repeat(4, minmax(70px, 1fr));
    padding-bottom: 10px;
  }

  .weekday-selector {
    flex-wrap: wrap;
    justify-content: space-between;
  }

  .day-btn {
    flex: 0 1 calc(14.28% - 8px);  /* 7개의 버튼을 위한 너비 계산 */
    text-align: center;
  }
}

@media (max-width: 480px) {
  .time-grid {
    grid-template-columns: repeat(3, minmax(70px, 1fr));
  }
}
</style>
