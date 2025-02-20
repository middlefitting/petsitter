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

        <transition-group name="fade" tag="div" class="reservation-list">
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
                  class="btn btn-success"
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
                <button
                  v-if="reservation.status && !reservation.isPaid"
                  class="btn btn-primary"
                  @click="onPayment(reservation)"
                >
                  결제하기
                </button>
                <span v-if="reservation.isPaid" class="payment-status" @click="showOrderDetails(reservation.id)">
                  결제완료
                </span>
              </template>
            </div>
          </div>
        </transition-group>
      </div>

      <div v-if="orderDetails" class="modal">
        <div class="modal-content">
          <span class="close" @click="orderDetails = null">&times;</span>
          <h2>결제 내역</h2>
          <p><strong>주문번호:</strong> {{ orderDetails.merchantUid }}</p>
          <p><strong>총 금액:</strong> {{ orderDetails.totalPrice.toLocaleString() }}원</p>
          <p><strong>결제 수단:</strong> {{ orderDetails.payMethod }}</p>
          <p><strong>반려동물:</strong> {{ orderDetails.petName }}</p>
          <p><strong>서비스:</strong> {{ orderDetails.serviceName }}</p>
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
const orderDetails = ref(null)

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

// 결제 처리
const onPayment = (reservation) => {
  const { IMP } = window;
  IMP.init('imp71318136'); // 가맹점 식별코드

  const data = {
    pg: 'uplus', // PG사 (간편결제 또는 일반결제 구분)
    pay_method: 'card', // 결제수단
    merchant_uid: `mid_${new Date().getTime()}`, // 주문번호
    amount: reservation.totalPrice, // 결제금액
    name: '펫시터 예약 결제', // 주문명
    buyer_name: authStore.user.name, // 구매자 이름
    buyer_tel: authStore.user.phone, // 구매자 전화번호
    buyer_email: authStore.user.email, // 구매자 이메일
    buyer_addr: '주소', // 구매자 주소
    buyer_postcode: '우편번호' // 구매자 우편번호
  };

  IMP.request_pay(data, (response) => {
    const { success, merchant_uid, pay_method, error_msg } = response;
    if (success) {
      alert('결제 성공');
      // 결제 성공 시 서버에 결제 완료 상태 업데이트
      axios.post(`/v1/reservations/${reservation.id}/pay`, {
        merchantUid: merchant_uid,
        payMethod: pay_method // 결제 유형을 pg_provider로 설정
      })
      .then(() => {
        toast.success('결제가 완료되었습니다.');
        loadReservations(); // 예약 목록 다시 불러오기
      })
      .catch((error) => {
        console.error('결제 상태 업데이트 실패:', error);
        toast.error('결제 상태 업데이트에 실패했습니다.');
      });
    } else {
      alert(`결제 실패: ${error_msg}`);
    }
  });
};

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

const showOrderDetails = async (reservationId) => {
  try {
    const response = await axios.get(`/v1/reservations/${reservationId}/order-details`)
    orderDetails.value = response.data.data
  } catch (error) {
    console.error('결제 내역 조회 실패:', error)
    toast.error('결제 내역을 불러오는데 실패했습니다.')
  }
}

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
  gap: 10px;
  margin-top: 16px;
}

.btn {
  display: inline-block;
  padding: 8px 16px;
  border-radius: 4px;
  font-weight: 500;
  cursor: pointer;
  border: none;
  color: var(--white);
}

.btn-success {
  background-color: var(--success);
}

.btn-success:hover {
  background-color: #218838;
}

.btn-danger {
  background-color: var(--error);
}

.btn-danger:hover {
  background-color: #c82333;
}

.btn-primary {
  background-color: var(--primary);
}

.btn-primary:hover {
  background-color: #0056b3;
}

.btn {
  background-color: #f0f0f0; /* 연한 회색 */
  color: #333; /* 검은색 텍스트 */
}

.mr-10, .ml-10 {
  margin: 0;
}

.payment-status {
  display: inline-block;
  color: var(--success);
  font-weight: bold;
  padding: 8px 16px;
  background-color: #d4edda;
  border-radius: 4px;
  cursor: pointer;
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

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active in <2.1.8 */ {
  opacity: 0;
}

.modal {
  display: block;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgb(0,0,0);
  background-color: rgba(0,0,0,0.4);
}

.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}
</style>
