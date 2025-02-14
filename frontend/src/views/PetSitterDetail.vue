<template>
  <main>
    <div class="container my-40">
      <!-- 탭 메뉴 -->
      <div class="tabs mb-20">
        <button
          class="tab-btn"
          :class="{ active: activeTab === 'info' }"
          @click="activeTab = 'info'"
        >
          펫시터 정보
        </button>
        <button
          class="tab-btn"
          :class="{ active: activeTab === 'reviews' }"
          @click="activeTab = 'reviews'"
        >
          리뷰 {{ reviews.length }}개
        </button>
      </div>

      <!-- 펫시터 정보 탭 -->
      <div v-if="activeTab === 'info'" class="detail-card card">
        <!-- 기본 정보 섹션 -->
        <div class="section">
          <h1>{{ petsitter.name }}</h1>
          <p class="location">{{ petsitter.location }}</p>
          <p class="contact">
            {{ formatPhoneNumber(petsitter.phone) }}
          </p>
        </div>

        <!-- 서비스 섹션 -->
        <div class="section">
          <h2>제공 서비스</h2>
          <div class="service-tags">
            <div v-for="service in petsitter.services" :key="service.type" class="service-tag">
              <span class="service-type">{{ service.type }}</span>
              <span class="service-price">{{ service.price.toLocaleString() }}원/시간</span>
            </div>
          </div>
        </div>

        <!-- 돌봄 가능 품종 섹션 -->
        <div class="section">
          <h2>돌봄 가능 품종</h2>
          <div class="pet-type-groups">
            <div v-for="(breeds, mainType) in groupedPetTypes" :key="mainType" class="pet-type-group">
              <div class="main-type">{{ mainType }}</div>
              <div class="breed-tags">
                <span v-for="breed in breeds" :key="breed" class="breed-tag">
                  {{ breed }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- 근무 가능 시간 섹션 -->
        <div class="section">
          <h2>근무 가능 시간</h2>
          <div class="schedule-container">
            <div
              v-for="day in weekdays"
              :key="day.value"
              class="schedule-day"
              :class="{ 'disabled': !hasWorkingHours(day.value) }"
            >
              <div class="day-label">{{ day.label }}</div>
              <div v-if="hasWorkingHours(day.value)" class="time-ranges">
                <div v-for="(range, index) in getTimeRanges(day.value)" :key="index" class="time-range">
                  {{ range.start }} ~ {{ range.end }}
                </div>
              </div>
              <div v-else class="time-ranges">
                근무 불가
              </div>
            </div>
          </div>
        </div>

        <!-- 예약 버튼 -->
        <div class="section text-center">
          <button class="btn-primary">예약하기</button>
        </div>
      </div>

      <!-- 리뷰 탭 -->
      <div v-else class="detail-card card">
        <div class="reviews-container">
          <div v-for="review in reviews" :key="review.id" class="review-card">
            <div class="review-header">
              <span class="reviewer-name">{{ review.userName }}</span>
              <span class="review-date">{{ formatDate(review.date) }}</span>
            </div>
            <p class="review-content">{{ review.content }}</p>
            <div class="review-service">
              <span class="service-badge">{{ review.service }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import axios from '@/plugins/axios'

const route = useRoute()
const petsitter = ref({
  name: '홍길동',
  location: '서울 강남구',
  phone: '01012345678',
  services: [
    { type: '산책', price: 15000 },
    { type: '방문', price: 30000 }
  ],
  petTypes: [
    { mainType: '강아지', breed: '푸들' },
    { mainType: '강아지', breed: '말티즈' },
    { mainType: '강아지', breed: '치와와' },
    { mainType: '고양이', breed: '페르시안' },
    { mainType: '고양이', breed: '러시안블루' }
  ],
  workingHours: {
    mon: Array(24).fill(false).map((_, i) =>
      (i >= 9 && i < 12) || (i >= 15 && i < 18)
    ), // 9시~12시, 15시~18시
    tue: Array(24).fill(false).map((_, i) =>
      (i >= 13 && i < 16) || (i >= 19 && i < 22)
    ), // 13시~16시, 19시~22시
    wed: Array(24).fill(false).map((_, i) =>
      (i >= 9 && i < 12) || (i >= 14 && i < 17) || (i >= 19 && i < 22)
    ), // 9시~12시, 14시~17시, 19시~22시
    thu: Array(24).fill(false).map((_, i) => i >= 9 && i < 18), // 9시~18시
    fri: Array(24).fill(false).map((_, i) =>
      (i >= 10 && i < 13) || (i >= 16 && i < 19)
    ), // 10시~13시, 16시~19시
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

const timeSlots = [
  '00:00', '01:00', '02:00', '03:00', '04:00', '05:00',
  '06:00', '07:00', '08:00', '09:00', '10:00', '11:00',
  '12:00', '13:00', '14:00', '15:00', '16:00', '17:00',
  '18:00', '19:00', '20:00', '21:00', '22:00', '23:00'
]

const activeTab = ref('info')  // 탭 상태 추가

// 해당 요일에 근무 가능 시간이 있는지 확인
const hasWorkingHours = (day) => {
  return petsitter.value.workingHours[day].some(time => time)
}

// 연속된 시간대를 범위로 변환
const getTimeRanges = (day) => {
  const ranges = []
  let start = null

  petsitter.value.workingHours[day].forEach((isAvailable, index) => {
    if (isAvailable && start === null) {
      start = timeSlots[index]
    } else if (!isAvailable && start !== null) {
      ranges.push({
        start,
        end: timeSlots[index - 1]
      })
      start = null
    }
  })

  // 마지막 범위 처리
  if (start !== null) {
    ranges.push({
      start,
      end: timeSlots[timeSlots.length - 1]
    })
  }

  return ranges
}

// 품종 데이터를 그룹화하는 computed 속성 추가
const groupedPetTypes = computed(() => {
  const groups = {}
  petsitter.value.petTypes.forEach(type => {
    if (!groups[type.mainType]) {
      groups[type.mainType] = []
    }
    if (type.breed) {
      groups[type.mainType].push(type.breed)
    }
  })
  return groups
})

const formatPhoneNumber = (phone) => {
  if (!phone) return ''
  return phone.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3')
}

const reviews = ref([
  {
    id: 1,
    userName: '김OO',
    date: '2024-01-15',
    content: '산책을 너무 잘 시켜주셔서 우리 강아지가 매우 좋아했어요! 다음에도 꼭 부탁드리고 싶습니다.',
    service: '산책'
  },
  {
    id: 2,
    userName: '이OO',
    date: '2024-01-13',
    content: '급하게 맡겼는데도 친절하게 잘 돌봐주셨어요. 중간중간 사진도 보내주셔서 안심이 되었습니다.',
    service: '돌봄'
  },
  {
    id: 3,
    userName: '박OO',
    date: '2024-01-10',
    content: '기본적인 훈련도 해주시고 산책도 잘 시켜주셔서 좋았습니다.',
    service: '훈련'
  },
  {
    id: 4,
    userName: '최OO',
    date: '2024-01-08',
    content: '매번 친절하게 잘 봐주셔서 감사합니다. 우리 냥이가 편안해하네요.',
    service: '돌봄'
  },
  {
    id: 5,
    userName: '정OO',
    date: '2024-01-05',
    content: '산책 코스도 다양하게 데리고 다니시고 잘 보살펴주셨어요!',
    service: '산책'
  },
  {
    id: 6,
    userName: '강OO',
    date: '2024-01-03',
    content: '훈련 효과가 금방 나타나서 놀랐어요. 전문성이 느껴집니다.',
    service: '훈련'
  },
  {
    id: 7,
    userName: '조OO',
    date: '2024-01-01',
    content: '급하게 연말에 맡겼는데 정말 감사했습니다. 다음에도 또 맡기고 싶어요.',
    service: '돌봄'
  },
  {
    id: 8,
    userName: '윤OO',
    date: '2023-12-30',
    content: '산책하면서 찍어주신 사진들이 너무 귀여워요. 우리 강아지가 즐거워보여서 좋았습니다.',
    service: '산책'
  },
  {
    id: 9,
    userName: '장OO',
    date: '2023-12-28',
    content: '항상 꼼꼼하게 봐주셔서 감사합니다. 믿고 맡길 수 있어요.',
    service: '돌봄'
  },
  {
    id: 10,
    userName: '임OO',
    date: '2023-12-25',
    content: '크리스마스에도 친절하게 봐주셔서 감사했습니다. 다음에 또 부탁드릴게요!',
    service: '돌봄'
  }
])

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}.${String(date.getDate()).padStart(2, '0')}`
}

onMounted(async () => {
  try {
    const response = await axios.get(`/v1/petsitters/${route.params.id}`)
    petsitter.value = response.data
  } catch (error) {
    console.error('펫시터 정보 로딩 실패:', error)
  }
})
</script>

<style scoped>
.detail-card {
  max-width: 800px;
  margin: 0 auto;
  padding: 30px;
}

.section {
  margin-bottom: 40px;
}

.section:last-child {
  margin-bottom: 0;
}

h1 {
  font-size: 1.8rem;
  margin-bottom: 8px;
}

h2 {
  font-size: 1.2rem;
  margin-bottom: 16px;
}

.location {
  color: var(--gray);
  font-size: 1.1rem;
  margin-bottom: 8px;
}

.contact {
  color: var(--gray);
  font-size: 1.1rem;
  display: flex;
  align-items: center;
  gap: 8px;
}

.contact .label {
  color: var(--black);
  font-weight: 500;
}

.service-tags, .pet-type-groups {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.service-tag, .pet-type-group {
  background-color: var(--gray-light);
  padding: 8px 16px;
  border-radius: 8px;
  font-size: 1rem;
}

.service-tag {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.service-price {
  font-weight: 600;
}

.schedule-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.schedule-day {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 12px;
  border-radius: 8px;
  background-color: var(--bg-secondary);
  transition: all 0.2s ease;
}

.schedule-day.disabled {
  opacity: 0.5;
}

.day-label {
  width: 30px;
  font-weight: 600;
}

.time-ranges {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  flex: 1;
}

.time-range {
  background-color: var(--gray-light);
  padding: 8px 12px;
  border-radius: 6px;
  font-size: 0.9rem;
  white-space: nowrap;
}

.time-ranges:empty::after {
  content: '근무 불가';
  color: var(--gray);
  font-size: 0.9rem;
}

.btn-primary {
  padding: 12px 40px;
  background-color: var(--black);
  color: var(--white);
  border: none;
  border-radius: 8px;
  font-size: 1.1rem;
  cursor: pointer;
  transition: opacity 0.2s ease;
}

.btn-primary:hover {
  opacity: 0.9;
}

.pet-type-groups {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.pet-type-group {
  background-color: var(--bg-secondary);
  padding: 16px;
  border-radius: 8px;
}

.main-type {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 12px;
  color: var(--black);
}

.breed-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.breed-tag {
  background-color: var(--gray-light);
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 0.9rem;
}

.reviews-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.review-card {
  background-color: var(--bg-secondary);
  padding: 20px;
  border-radius: 8px;
}

.review-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
}

.reviewer-name {
  font-weight: 600;
}

.review-date {
  color: var(--gray);
  font-size: 0.9rem;
}

.review-content {
  margin-bottom: 12px;
  line-height: 1.5;
}

.service-badge {
  background-color: var(--gray-light);
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.9rem;
}

.tabs {
  display: flex;
  justify-content: center;
  gap: 20px;
  max-width: 800px;
  margin: 0 auto 20px;
}

.tab-btn {
  padding: 10px 20px;
  border: 2px solid var(--black);
  background: none;
  color: var(--black);
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 120px;
  text-align: center;
}

.tab-btn.active {
  background: var(--black);
  color: var(--white);
}

.my-40 {
  margin-top: 40px;
  margin-bottom: 40px;
}

@media (max-width: 768px) {
  .detail-card {
    margin: 0;
    border-radius: 0;
    padding: 20px;
    min-height: calc(100vh - 120px); /* 헤더/푸터 고려한 최소 높이 */
  }

  .schedule-day {
    flex-direction: row;
    align-items: center;
    padding: 16px;
  }

  .time-ranges {
    flex-direction: row;
    flex-wrap: wrap;
  }

  .pet-type-group {
    padding: 12px;
  }

  .review-card {
    padding: 16px;
  }

  .tabs {
    margin: 0 20px 20px;
  }

  .tab-btn {
    flex: 1;
    min-width: 0;
    padding: 10px;
  }

  .my-40 {
    margin-top: 20px;
    margin-bottom: 20px;
  }
}
</style>
