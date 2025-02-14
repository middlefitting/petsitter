<template>
  <main>
    <div class="container">
      <h1 class="text-center mb-20">펫시터 찾기</h1>

      <!-- 필터 섹션 -->
      <div class="filters mb-20">
        <div class="filter-row">
          <div class="input-group">
            <select v-model="filters.location" class="form-select">
              <option value="">지역 선택</option>
              <option value="서울">서울</option>
              <option value="경기">경기</option>
            </select>
          </div>
          <div class="input-group">
            <select v-model="filters.petType" class="form-select">
              <option value="">품종 선택</option>
              <optgroup label="강아지">
                <option value="푸들">푸들</option>
                <option value="말티즈">말티즈</option>
                <option value="치와와">치와와</option>
                <option value="도베르만">도베르만</option>
              </optgroup>
              <optgroup label="고양이">
                <option value="페르시안">페르시안</option>
                <option value="러시안블루">러시안블루</option>
                <option value="샴">샴</option>
              </optgroup>
            </select>
          </div>
          <div class="input-group">
            <select v-model="filters.service" class="form-select">
              <option value="">서비스 선택</option>
              <option value="산책">산책</option>
              <option value="돌봄">돌봄</option>
              <option value="훈련">훈련</option>
            </select>
          </div>
          <div class="input-group">
            <select v-model="filters.sort" class="form-select">
              <option value="price">가격순</option>
              <option value="rating">평점순</option>
            </select>
          </div>
        </div>
      </div>

      <!-- 펫시터 목록 -->
      <div class="petsitter-list">
        <div v-for="sitter in displayedPetsitters" :key="sitter.id" class="petsitter-card card">
          <div class="sitter-info">
            <h2>{{ sitter.name }}</h2>
            <p class="location">{{ sitter.location }}</p>
            <div class="tags">
              <span v-for="service in sitter.services" :key="service.type" class="tag">
                {{ service.type }} {{ service.price.toLocaleString() }}원/시간
              </span>
            </div>
          </div>
          <div class="sitter-actions">
            <button class="btn" @click="viewDetails(sitter.id)">상세 보기</button>
          </div>
        </div>
      </div>

      <!-- 더보기 버튼 -->
      <div v-if="hasMorePetsitters" class="load-more">
        <button @click="loadMore" class="btn-outline">더보기</button>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const filters = ref({
  location: '',
  petType: '',
  service: '',
  sort: 'price'
})

const ITEMS_PER_PAGE = 5
const currentPage = ref(1)

// 임시 데이터 10개로 확장
const petsitters = ref([
  {
    id: 1,
    name: '홍길동',
    location: '서울 강남구',
    services: [{ type: '산책', price: 15000 }, { type: '방문', price: 30000 }]
  },
  {
    id: 2,
    name: '김철수',
    location: '서울 서초구',
    services: [{ type: '산책', price: 20000 }, { type: '훈련', price: 40000 }]
  },
  {
    id: 3,
    name: '이영희',
    location: '경기 성남시',
    services: [{ type: '돌봄', price: 25000 }]
  },
  {
    id: 4,
    name: '박지민',
    location: '서울 송파구',
    services: [{ type: '산책', price: 18000 }]
  },
  {
    id: 5,
    name: '최유진',
    location: '경기 분당구',
    services: [{ type: '훈련', price: 35000 }]
  },
  {
    id: 6,
    name: '정민수',
    location: '서울 마포구',
    services: [{ type: '산책', price: 16000 }, { type: '돌봄', price: 28000 }]
  },
  {
    id: 7,
    name: '강다희',
    location: '경기 일산동구',
    services: [{ type: '돌봄', price: 22000 }]
  },
  {
    id: 8,
    name: '윤서준',
    location: '서울 용산구',
    services: [{ type: '훈련', price: 45000 }]
  },
  {
    id: 9,
    name: '임하늘',
    location: '경기 수원시',
    services: [{ type: '산책', price: 17000 }]
  },
  {
    id: 10,
    name: '송민아',
    location: '서울 중구',
    services: [{ type: '돌봄', price: 23000 }, { type: '훈련', price: 38000 }]
  }
])

// 현재 페이지에 표시할 펫시터 목록
const displayedPetsitters = computed(() => {
  return petsitters.value.slice(0, currentPage.value * ITEMS_PER_PAGE)
})

// 더 보여줄 항목이 있는지 확인
const hasMorePetsitters = computed(() => {
  return currentPage.value * ITEMS_PER_PAGE < petsitters.value.length
})

// 더보기 버튼 클릭 핸들러
const loadMore = () => {
  currentPage.value++
}

const viewDetails = (sitterId) => {
  router.push(`/petsitters/${sitterId}`)
}
</script>

<style scoped>
.petsitter-list {
  display: grid;
  gap: 20px;
}

.petsitter-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
}

.sitter-info {
  flex: 1;
}

.sitter-info h2 {
  margin-bottom: 8px;
  font-size: 1.2rem;
}

.location {
  color: var(--gray);
  margin-bottom: 12px;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag {
  background-color: var(--gray-light);
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 14px;
}

.sitter-actions {
  margin-left: 20px;
}

.filters {
  max-width: 800px;
  margin: 0 auto;
}

.filter-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.filter-row .input-group {
  flex: 1;
  min-width: 0; /* 오버플로우 방지 */
}

.form-select {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid var(--gray-light);
  border-radius: 8px;
  background-color: var(--white);
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
  appearance: none;
  background-image: url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3e%3cpolyline points='6 9 12 15 18 9'%3e%3c/polyline%3e%3c/svg%3e");
  background-repeat: no-repeat;
  background-position: right 12px center;
  background-size: 16px;
}

.form-select:hover {
  border-color: var(--gray);
}

.form-select:focus {
  outline: none;
  border-color: var(--black);
}

.form-select optgroup {
  font-weight: 600;
  color: var(--black);
}

.form-select option {
  font-weight: normal;
  padding: 8px;
}

@media (max-width: 768px) {
  .petsitter-card {
    flex-direction: column;
    align-items: flex-start;
  }

  .sitter-actions {
    margin-left: 0;
    margin-top: 16px;
    width: 100%;
  }

  .sitter-actions .btn {
    width: 100%;
  }

  .filter-row {
    grid-template-columns: 1fr;
    flex-direction: column;
    gap: 12px;
    padding: 0 16px;
  }

  .filter-row .input-group {
    width: 100%;
  }
}

.load-more {
  text-align: center;
  margin-top: 40px;
  margin-bottom: 40px;
}

.btn-outline {
  padding: 12px 24px;
  border: 2px solid var(--black);
  background: none;
  color: var(--black);
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.2s ease;
  min-width: 200px;
}

.btn-outline:hover {
  background-color: var(--black);
  color: var(--white);
}

@media (max-width: 768px) {
  .btn-outline {
    width: 100%;
    max-width: 400px;
  }
}
</style>
