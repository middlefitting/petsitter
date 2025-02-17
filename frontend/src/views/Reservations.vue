<template>
  <main>
    <div class="container my-40">
      <h1 class="text-center mb-20">예약 관리</h1>

      <!-- 예약 유형 탭 -->
      <div class="tabs mb-20">
        <button
          :class="['tab-button', { active: activeTab === 'my' }]"
          @click="activeTab = 'my'"
        >
          내 예약 내역
        </button>
        <button
          :class="['tab-button', { active: activeTab === 'customer' }]"
          @click="activeTab = 'customer'"
        >
          고객 예약 내역
        </button>
      </div>

      <!-- 예약 목록 -->
      <div class="reservations-container">
        <div v-if="isLoading" class="text-center">
          <p>예약 정보를 불러오는 중...</p>
        </div>

        <div v-else-if="displayedReservations.length === 0" class="text-center">
          <p>예약 내역이 없습니다.</p>
        </div>

        <div v-else class="reservation-list">
          <div v-for="reservation in displayedReservations" :key="reservation.id" class="reservation-card">
            <div class="reservation-header">
              <h3>{{ reservation.petsitterName }}</h3>
              <span :class="['status-badge', getStatusClass(reservation.status)]">
                {{ getStatusText(reservation.status) }}
              </span>
            </div>

            <div class="reservation-details">
              <p><strong>반려동물:</strong> {{ reservation.petName }}</p>
              <p><strong>서비스:</strong> {{ reservation.serviceType }}</p>
              <p><strong>예약 시간:</strong> {{ formatDateTime(reservation.startTime) }} ~ {{ formatDateTime(reservation.endTime) }}</p>
              <p><strong>예약 금액:</strong> {{ reservation.totalPrice.toLocaleString() }}원</p>
            </div>

            <div class="reservation-actions">
              <!-- 예약 상태에 따른 버튼 -->
              <template v-if="activeTab === 'customer'">
                <button
                  v-if="!reservation.status"
                  class="btn btn-primary mr-10"
                  @click="acceptReservation(reservation.id)"
                >
                  예약 승인
                </button>
                <button
                  v-if="!reservation.status"
                  class="btn btn-danger"
                  @click="rejectReservation(reservation.id)"
                >
                  예약 거절
                </button>
              </template>
              <template v-else>
                <button
                  v-if="!reservation.status"
                  class="btn btn-danger"
                  @click="cancelReservation(reservation.id)"
                >
                  예약 취소
                </button>
              </template>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useAuthStore } from '@/store/auth'
import axios from '@/plugins/axios'
import { toast } from 'vue3-toastify'

const authStore = useAuthStore()
const isLoading = ref(false)
const myReservations = ref([])
const customerReservations = ref([])
const activeTab = ref('my')

// 현재 탭에 따른 예약 목록 표시
const displayedReservations = computed(() => {
  return activeTab.value === 'my' ? myReservations.value : customerReservations.value
})

// 예약 목록 로드
const loadReservations = async () => {
  isLoading.value = true
  try {
    // 내 예약 내역 로드
    const myResponse = await axios.get(`/v1/reservations/user/${authStore.user.userId}`)
    myReservations.value = myResponse.data.data

    // 고객 예약 내역도 로드 (탭이 'customer'일 때만)
    if (activeTab.value === 'customer') {
      const customerResponse = await axios.get(`/v1/reservations/petsitter/users/${authStore.user.userId}`)
      customerReservations.value = customerResponse.data.data
    }
  } catch (error) {
    console.error('예약 목록 로드 실패:', error)
    toast.error('예약 목록을 불러오는데 실패했습니다.')
  } finally {
    isLoading.value = false
  }
}

// 예약 승인
const acceptReservation = async (reservationId) => {
  try {
    const response = await axios.post(`/v1/reservations/${reservationId}/accept`)
    if (response.data.status === 'SUCCESS') {
      toast.success('예약이 승인되었습니다.')
      loadReservations() // 목록 새로고침
    }
  } catch (error) {
    console.error('예약 승인 실패:', error)
    toast.error('예약 승인에 실패했습니다.')
  }
}

// 예약 거절
const rejectReservation = async (reservationId) => {
  if (!confirm('예약을 거절하시겠습니까?')) return

  try {
    const response = await axios.post(`/v1/reservations/${reservationId}/reject`)
    if (response.data.status === 'SUCCESS') {
      toast.success('예약이 거절되었습니다.')
      loadReservations() // 목록 새로고침
    }
  } catch (error) {
    console.error('예약 거절 실패:', error)
    toast.error('예약 거절에 실패했습니다.')
  }
}

// 예약 취소 (기존 함수 수정)
const cancelReservation = async (reservationId) => {
  if (!confirm('예약을 취소하시겠습니까?')) return

  try {
    const response = await axios.post(`/v1/reservations/${reservationId}/reject`) // 취소도 reject API 사용
    if (response.data.status === 'SUCCESS') {
      toast.success('예약이 취소되었습니다.')
      loadReservations()
    }
  } catch (error) {
    console.error('예약 취소 실패:', error)
    toast.error('예약 취소에 실패했습니다.')
  }
}

// 날짜 포맷팅
const formatDateTime = (dateTimeStr) => {
  if (!dateTimeStr) return ''
  const date = new Date(dateTimeStr)
  return date.toLocaleString('ko-KR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 예약 상태에 따른 클래스
const getStatusClass = (status) => {
  if (status === true) return 'status-confirmed'
  if (status === false) return 'status-pending'
  return 'status-cancelled'
}

// 예약 상태 텍스트
const getStatusText = (status) => {
  if (status === true) return '승인됨'
  if (status === false) return '대기중'
  return '취소됨'
}

// activeTab이 변경될 때마다 예약 목록 다시 로드
watch(activeTab, () => {
  loadReservations()
})

onMounted(() => {
  loadReservations()
})
</script>

<style scoped>
.reservations-container {
  max-width: 800px;
  margin: 0 auto;
}

.reservation-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.reservation-card {
  background-color: var(--white);
  border: 1px solid var(--gray-light);
  border-radius: 8px;
  padding: 20px;
}

.reservation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.reservation-header h3 {
  margin: 0;
  font-size: 1.2rem;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.9rem;
}

.status-pending {
  background-color: #fff3cd;
  color: #856404;
}

.status-confirmed {
  background-color: #cce5ff;
  color: #004085;
}

.status-completed {
  background-color: #d4edda;
  color: #155724;
}

.status-cancelled {
  background-color: #f8d7da;
  color: #721c24;
}

.reservation-details {
  margin-bottom: 16px;
}

.reservation-details p {
  margin: 8px 0;
}

.reservation-actions {
  display: flex;
  justify-content: flex-end;
}

.btn-danger {
  background-color: var(--error);
  color: var(--white);
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.btn-danger:hover {
  background-color: #dc3545;
}

@media (max-width: 768px) {
  .reservation-card {
    margin: 0 20px;
  }
}

.tabs {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 30px;
}

.tab-button {
  padding: 10px 20px;
  border: none;
  background: none;
  color: var(--gray);
  cursor: pointer;
  font-size: 16px;
  border-bottom: 2px solid transparent;
  transition: all 0.3s ease;
}

.tab-button:hover {
  color: var(--black);
}

.tab-button.active {
  color: var(--black);
  border-bottom-color: var(--black);
}
</style>
