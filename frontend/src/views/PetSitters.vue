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
              <option v-for="address in addresses" :key="address.id" :value="address.city">
                {{ address.city }}
              </option>
            </select>
          </div>
          <div class="input-group">
            <select v-model="filters.petGroup" class="form-select" @change="handlePetGroupChange">
              <option value="">동물 종류 선택</option>
              <option v-for="group in petGroups" :key="group.id" :value="group.id">
                {{ group.groupname }}
              </option>
            </select>
          </div>
          <div class="input-group">
            <select v-model="filters.petType" class="form-select" :disabled="!filters.petGroup">
              <option value="">품종 선택</option>
              <option v-for="type in currentPetTypes" :key="type.id" :value="type.id">
                {{ type.typename }}
              </option>
            </select>
          </div>
          <div class="input-group">
            <select v-model="filters.service" class="form-select">
              <option value="">서비스 선택</option>
              <option v-for="service in petServices" :key="service.petServiceId" :value="service.petServiceId">
                {{ service.servicename }}
              </option>
            </select>
          </div>
          <!-- <div class="input-group">
            <select v-model="filters.sort" class="form-select">
              <option value="price">가격순</option>
              <option value="rating">평점순</option>
            </select>
          </div> -->
        </div>
      </div>

      <!-- 로딩 상태 표시 -->
      <div v-if="isLoading" class="text-center">
        <p>펫시터 목록을 불러오는 중...</p>
      </div>

      <!-- 펫시터 목록 -->
      <div v-else class="petsitter-list">
        <div v-for="sitter in filteredPetsitters" :key="sitter.id" class="petsitter-card card">
          <div class="sitter-info">
            <h2>{{ sitter.name }}</h2>
            <p class="location">{{ sitter.location }}</p>
            <div class="tags">
              <span v-for="service in sitter.services" :key="service.type" class="tag">
                {{ service.type }} {{ service.price.toLocaleString() }}원/시간
              </span>
            </div>
            <!-- <div class="tags mt-8">
              <span v-for="type in sitter.petTypes" :key="type" class="tag">
                {{ type }}
              </span>
            </div> -->
            <!-- <div class="tags mt-8">
              <span v-for="size in sitter.petSizes" :key="size" class="tag">
                {{ size }}
              </span>
            </div> -->
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
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from '@/plugins/axios'
import { toast } from 'vue3-toastify'

const router = useRouter()
const petsitters = ref([])
const addresses = ref([])
const petGroups = ref([])
const currentPetTypes = ref([])
const petServices = ref([])
const isLoading = ref(false)

const filters = ref({
  location: '',
  petGroup: '',
  petType: '',
  service: '',
})

// 데이터 로드 함수들
const loadAddresses = async () => {
  try {
    const response = await axios.get('/v1/addresses')
    addresses.value = response.data.data
  } catch (error) {
    console.error('주소 목록 로드 에러:', error)
    toast.error('주소 목록을 불러오는데 실패했습니다.')
  }
}

const loadPetGroups = async () => {
  try {
    const response = await axios.get('/v1/pet-groups')
    petGroups.value = response.data.data
  } catch (error) {
    console.error('펫 그룹 로드 에러:', error)
    toast.error('펫 그룹을 불러오는데 실패했습니다.')
  }
}

const loadPetGroupTypes = async (groupId) => {
  try {
    const response = await axios.get(`/v1/pet-groups/${groupId}/pet-group-types`)
    currentPetTypes.value = response.data.data
  } catch (error) {
    console.error('펫 타입 로드 에러:', error)
    toast.error('펫 타입을 불러오는데 실패했습니다.')
  }
}

const handlePetGroupChange = async () => {
  filters.value.petType = ''
  if (filters.value.petGroup) {
    await loadPetGroupTypes(filters.value.petGroup)
  } else {
    currentPetTypes.value = []
  }
}

const loadPetServices = async () => {
  try {
    const response = await axios.get('/v1/pet-services')
    petServices.value = response.data.data
  } catch (error) {
    console.error('펫 서비스 로드 에러:', error)
    toast.error('펫 서비스를 불러오는데 실패했습니다.')
  }
}

// 펫시터 목록 로드
async function loadPetSitters() {
  try {
    isLoading.value = true
    const response = await axios.get('/v1/petsitters')
    petsitters.value = response.data.data
  } catch (error) {
    console.error('펫시터 목록 로드 에러:', error)
    toast.error('펫시터 목록을 불러오는데 실패했습니다.')
  } finally {
    isLoading.value = false
  }
}

// 필터링된 펫시터 목록
const filteredPetsitters = computed(() => {
  return petsitters.value.filter(sitter => {
    // 지역 필터
    if (filters.value.location && sitter.location !== filters.value.location) {
      return false
    }

    // 품종 필터
    if (filters.value.petType) {
      const hasPetType = sitter.petTypes.some(type =>
        type.groupName === getPetGroupName(filters.value.petGroup) &&
        type.typeName === getPetTypeName(filters.value.petType)
      )
      if (!hasPetType) return false
    }

    // 서비스 필터
    if (filters.value.service) {
      const hasService = sitter.services.some(service =>
        service.type === getPetServiceName(filters.value.service)
      )
      if (!hasService) return false
    }

    return true
  })
})

// 그룹 ID로 그룹 이름 가져오기
const getPetGroupName = (groupId) => {
  const group = petGroups.value.find(g => g.id === groupId)
  return group ? group.groupname : ''
}

// 타입 ID로 타입 이름 가져오기
const getPetTypeName = (typeId) => {
  const type = currentPetTypes.value.find(t => t.id === typeId)
  return type ? type.typename : ''
}

// 서비스 ID로 서비스 이름 가져오기
const getPetServiceName = (serviceId) => {
  const service = petServices.value.find(s => s.petServiceId === serviceId)
  return service ? service.servicename : ''
}

const ITEMS_PER_PAGE = 5
const currentPage = ref(1)

// 현재 페이지에 표시할 펫시터 목록
const displayedPetsitters = computed(() => {
  return filteredPetsitters.value.slice(0, currentPage.value * ITEMS_PER_PAGE)
})

// 더 보여줄 항목이 있는지 확인
const hasMorePetsitters = computed(() => {
  return currentPage.value * ITEMS_PER_PAGE < filteredPetsitters.value.length
})

// 더보기 버튼 클릭 핸들러
const loadMore = () => {
  currentPage.value++
}

const viewDetails = (sitterId) => {
  router.push(`/petsitters/${sitterId}`)
}

// 컴포넌트 마운트 시 데이터 로드
onMounted(async () => {
  isLoading.value = true
  try {
    await Promise.all([
      loadAddresses(),
      loadPetGroups(),
      loadPetServices(),
      loadPetSitters()
    ])
  } catch (error) {
    console.error('데이터 로드 에러:', error)
    toast.error('데이터를 불러오는데 실패했습니다.')
  } finally {
    isLoading.value = false
  }
})
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

.mt-8 {
  margin-top: 8px;
}
</style>
