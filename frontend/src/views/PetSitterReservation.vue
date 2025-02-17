<template>
  <main>
    <div class="container my-40">
      <h1 class="text-center mb-20">펫시터 예약</h1>

      <div class="reservation-form card">
        <!-- 펫시터 기본 정보 -->
        <div class="section">
          <h2>펫시터 정보</h2>
          <div class="petsitter-info">
            <p><strong>이름:</strong> {{ petsitter.name }}</p>
            <p><strong>지역:</strong> {{ petsitter.location }}</p>
          </div>
        </div>

        <!-- 반려동물 선택 -->
        <div class="section">
          <h2>반려동물 선택</h2>
          <select v-model="reservation.petId" class="form-select" required>
            <option value="">반려동물을 선택하세요</option>
            <option
              v-for="pet in availablePets"
              :key="pet.id"
              :value="pet.id"
            >
              {{ pet.name }} ({{ pet.petTypeName }})
            </option>
          </select>
          <p v-if="myPets.length > 0 && availablePets.length === 0" class="error-message mt-10">
            이 펫시터가 돌볼 수 있는 반려동물이 없습니다.
          </p>
          <p v-if="unavailablePets.length > 0" class="warning-message mt-10">
            다음 반려동물은 펫시터의 돌봄 조건과 맞지 않아 선택할 수 없습니다:
            <span v-for="pet in unavailablePets" :key="pet.id">
              {{ pet.name }} ({{ getUnavailableReason(pet) }})
            </span>
          </p>
        </div>

        <!-- 서비스 선택 -->
        <div class="section">
          <h2>서비스 선택</h2>
          <select v-model="reservation.serviceId" class="form-select" required @change="updatePrice">
            <option value="">서비스를 선택하세요</option>
            <option v-for="service in petsitter.services" :key="service.type" :value="service.type">
              {{ service.type }} ({{ service.price.toLocaleString() }}원/시간)
            </option>
          </select>
        </div>

        <!-- 날짜 및 시간 선택 -->
        <div class="section">
          <h2>예약 일시</h2>
          <div class="date-select mb-20">
            <label>날짜</label>
            <input type="date" v-model="reservation.date" required :min="minDate" @change="updateAvailableHours">
          </div>

          <!-- 시간 선택 UI -->
          <div v-if="reservation.date" class="time-selection">
            <h3 class="mb-10">가능한 시간</h3>
            <div class="time-grid">
              <button
                v-for="hour in 24"
                :key="hour-1"
                class="time-btn"
                :class="{
                  'available': isTimeAvailable(hour-1),
                  'selected': isTimeSelected(hour-1)
                }"
                :disabled="!isTimeAvailable(hour-1)"
                @click="toggleTimeSelection(hour-1)"
              >
                {{ (hour-1).toString().padStart(2, '0') }}:00
              </button>
            </div>
          </div>

          <!-- 선택된 시간 표시 -->
          <div v-if="selectedTimeRange" class="selected-time mt-20">
            <p>선택된 시간: {{ selectedTimeRange }}</p>
          </div>
        </div>

        <!-- 예약 금액 -->
        <div class="section">
          <h2>예약 금액</h2>
          <div class="price-info">
            <p>시간당 요금: {{ hourlyPrice.toLocaleString() }}원</p>
            <p>총 시간: {{ totalHours }}시간</p>
            <p class="total-price">총 금액: {{ totalPrice.toLocaleString() }}원</p>
          </div>
        </div>

        <!-- 예약하기 버튼 -->
        <div class="section text-center">
          <button class="btn-primary" @click="submitReservation" :disabled="!isFormValid">
            예약하기
          </button>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import axios from '@/plugins/axios'
import { toast } from 'vue3-toastify'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

// 초기 상태값 설정
const petsitter = ref({
  name: '',
  location: '',
  services: [],
  workingHours: {},
  petTypes: [],
  petSizes: []
})
const myPets = ref([])
const hourlyPrice = ref(0)
const selectedTimes = ref(new Set())
const availableHours = ref([])

// 예약 정보
const reservation = ref({
  petId: '',
  serviceId: '',
  date: '',
  startTime: '',
  endTime: ''
})

// 최소 날짜 (오늘)
const minDate = computed(() => {
  const today = new Date()
  return today.toISOString().split('T')[0]
})

// 선택된 날짜의 요일에 따른 가능 시간 업데이트
const updateAvailableHours = () => {
  if (!reservation.value.date) return

  const date = new Date(reservation.value.date)
  const dayNames = ['sun', 'mon', 'tue', 'wed', 'thu', 'fri', 'sat']
  const dayName = dayNames[date.getDay()]

  // 펫시터의 해당 요일 가능 시간 가져오기
  availableHours.value = petsitter.value.workingHours[dayName] || []
  selectedTimes.value.clear() // 날짜 변경 시 선택 초기화
  updateReservationTimes()
}

// 시간 선택 가능 여부 확인
const isTimeAvailable = (hour) => {
  return availableHours.value[hour]
}

// 시간이 선택되었는지 확인
const isTimeSelected = (hour) => {
  return selectedTimes.value.has(hour)
}

// 시간 선택/해제
const toggleTimeSelection = (hour) => {
  if (!isTimeAvailable(hour)) return

  // 연속된 시간만 선택 가능하도록
  if (selectedTimes.value.has(hour)) {
    // 선택 해제 시 해당 시간 이후 선택된 시간들도 모두 해제
    const selected = Array.from(selectedTimes.value).sort((a, b) => a - b)
    const index = selected.indexOf(hour)
    const toRemove = selected.slice(index)
    toRemove.forEach(h => selectedTimes.value.delete(h))
  } else {
    // 선택 시 이전 선택된 시간과 연속되는지 확인
    const selected = Array.from(selectedTimes.value).sort((a, b) => a - b)
    if (selected.length === 0 || hour === selected[selected.length - 1] + 1) {
      selectedTimes.value.add(hour)
    }
  }

  updateReservationTimes()
}

// 선택된 시간 범위 표시
const selectedTimeRange = computed(() => {
  const selected = Array.from(selectedTimes.value).sort((a, b) => a - b)
  if (selected.length === 0) return ''

  const start = selected[0].toString().padStart(2, '0') + ':00'
  const end = (selected[selected.length - 1] + 1).toString().padStart(2, '0') + ':00'
  return `${start} - ${end}`
})

// reservation 객체 업데이트
const updateReservationTimes = () => {
  const selected = Array.from(selectedTimes.value).sort((a, b) => a - b)
  if (selected.length > 0) {
    reservation.value.startTime = selected[0].toString().padStart(2, '0') + ':00'
    reservation.value.endTime = (selected[selected.length - 1] + 1).toString().padStart(2, '0') + ':00'
  } else {
    reservation.value.startTime = ''
    reservation.value.endTime = ''
  }
}

// 총 시간 계산
const totalHours = computed(() => selectedTimes.value.size)

// 총 금액 계산
const totalPrice = computed(() => hourlyPrice.value * totalHours.value)

// 폼 유효성 검사
const isFormValid = computed(() => {
  return reservation.value.petId &&
         reservation.value.serviceId &&
         reservation.value.date &&
         selectedTimes.value.size > 0
})

// 펫시터 정보 로드
const loadPetsitter = async () => {
  try {
    const response = await axios.get(`/v1/petsitters/${route.params.id}`)
    if (response.data.status === 'SUCCESS') {
      petsitter.value = response.data.data
    }
  } catch (error) {
    console.error('펫시터 정보 로드 실패:', error)
    toast.error('펫시터 정보를 불러오는데 실패했습니다.')
  }
}

// 내 반려동물 목록 로드
const loadMyPets = async () => {
  try {
    const response = await axios.get(`/v1/users/${authStore.user.userId}/pets`)
    if (response.data.status === 'SUCCESS') {
      myPets.value = response.data.data
    }
  } catch (error) {
    console.error('반려동물 정보 로드 실패:', error)
    toast.error('반려동물 정보를 불러오는데 실패했습니다.')
  }
}

// 서비스 선택 시 시간당 요금 업데이트
const updatePrice = () => {
  const selectedService = petsitter.value.services.find(
    s => s.type === reservation.value.serviceId
  )
  hourlyPrice.value = selectedService ? selectedService.price : 0
}

// 선택 가능한 반려동물 필터링
const availablePets = computed(() => {
  return myPets.value.filter(pet => {
    // 펫시터가 돌볼 수 있는 품종인지 확인
    const canCareForType = petsitter.value.petTypes.some(type =>
      type.groupName === pet.petGroupName && type.typeName === pet.petTypeName
    )

    // 펫시터가 돌볼 수 있는 크기인지 확인
    const canCareForSize = petsitter.value.petSizes.includes(getPetSizeInfo(pet.petsize))

    return canCareForType && canCareForSize
  })
})

// 선택 불가능한 반려동물 목록
const unavailablePets = computed(() => {
  return myPets.value.filter(pet => !availablePets.value.includes(pet))
})

// 반려동물 크기 정보 변환 함수
const getPetSizeInfo = (sizeId) => {
  const sizeMap = {
    1: '소형(10kg 이하)',
    2: '중형(20kg 이하)',
    3: '대형(30kg 이상)'
  }
  return sizeMap[sizeId] || ''
}

// 선택 불가 사유 반환
const getUnavailableReason = (pet) => {
  const typeMatch = petsitter.value.petTypes.some(type =>
    type.groupName === pet.petGroupName && type.typeName === pet.petTypeName
  )
  const sizeMatch = petsitter.value.petSizes.includes(getPetSizeInfo(pet.petsize))

  if (!typeMatch && !sizeMatch) {
    return '품종과 크기가 맞지 않음'
  } else if (!typeMatch) {
    return '품종이 맞지 않음'
  } else {
    return '크기가 맞지 않음'
  }
}

// 예약 제출
const submitReservation = async () => {
  try {
    const reservationData = {
      petsitterId: route.params.id,
      ...reservation.value,
      totalPrice: hourlyPrice.value * totalHours.value
    }

    const response = await axios.post('/v1/reservations', reservationData)
    if (response.data.status === 'SUCCESS') {
      toast.success('예약이 완료되었습니다.')
      router.push('/reservations')
    }
  } catch (error) {
    toast.error('예약에 실패했습니다.')
  }
}

onMounted(async () => {
  await Promise.all([loadPetsitter(), loadMyPets()])
})
</script>

<style scoped>
.reservation-form {
  max-width: 800px;
  margin: 0 auto;
}

.section {
  margin-bottom: 32px;
}

.section h2 {
  margin-bottom: 16px;
}

.datetime-picker {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 16px;
}

.form-select {
  width: 100%;
  padding: 12px;
  border: 1px solid var(--gray-light);
  border-radius: 4px;
}

.price-info {
  background-color: var(--bg-secondary);
  padding: 20px;
  border-radius: 8px;
}

.total-price {
  font-size: 1.2rem;
  font-weight: bold;
  margin-top: 12px;
}

.time-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 8px;
}

.time-btn {
  padding: 8px;
  border: 1px solid var(--gray-light);
  border-radius: 4px;
  background: none;
  cursor: pointer;
  transition: all 0.2s;
}

.time-btn.available {
  border-color: var(--black);
  color: var(--black);
}

.time-btn.selected {
  background-color: var(--black);
  color: var(--white);
  border-color: var(--black);
}

.time-btn:disabled {
  background-color: var(--gray-light);
  border-color: var(--gray-light);
  color: var(--gray);
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .datetime-picker {
    grid-template-columns: 1fr;
  }

  .time-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

.error-message {
  color: var(--error);
  font-size: 0.9rem;
}

.warning-message {
  color: #ff9800;
  font-size: 0.9rem;
}

.warning-message span {
  display: block;
  margin-top: 4px;
  padding-left: 12px;
}
</style>
