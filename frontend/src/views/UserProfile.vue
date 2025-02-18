<template>
  <main>
    <div class="container my-40">
      <h1 class="text-center mb-20">마이페이지</h1>

      <!-- 탭 메뉴 -->
      <div class="tabs mb-20">
        <button
          :class="['tab-button', { active: activeTab === 'profile' }]"
          @click="activeTab = 'profile'"
        >
          프로필
        </button>
        <button
          :class="['tab-button', { active: activeTab === 'pets' }]"
          @click="activeTab = 'pets'"
        >
          내 반려동물 관리
        </button>
        <button
          :class="['tab-button', { active: activeTab === 'petsitter' }]"
          @click="activeTab = 'petsitter'"
        >
          펫시터 정보
        </button>
        <button
          :class="['tab-button', { active: activeTab === 'reservations' }]"
          @click="goToReservations"
        >
          예약관리
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

      <!-- 반려동물 관리 탭 -->
      <div v-else-if="activeTab === 'pets'" class="form-container">
        <h2 class="text-center mb-20">내 반려동물</h2>

        <!-- 반려동물 목록 -->
        <div class="pets-list mb-20">
          <div v-for="pet in pets" :key="pet.id" class="pet-card">
            <img :src="getImageUrl(pet.imageUrl)" :alt="pet.name" class="pet-image">
            <div class="pet-info">
              <h3>{{ pet.name }}</h3>
              <p class="text-gray">{{ pet.age }}살</p>
              <p class="text-gray">{{ pet.petGroupName }} - {{ pet.petTypeName }}</p>
            </div>
            <div class="pet-actions">
              <button class="btn-icon" @click="editPet(pet)">수정</button>
              <button class="btn-icon delete" @click="deletePet(pet)">삭제</button>
            </div>
          </div>
        </div>

        <!-- 반려동물 추가 버튼 -->
        <button class="btn" @click="showAddPetModal = true">
          반려동물 등록
        </button>

        <!-- 반려동물 등록/수정 모달 -->
        <Modal v-if="showAddPetModal" @close="closeModal">
          <template #header>
            <h3>{{ editingPet ? '반려동물 정보 수정' : '새 반려동물 등록' }}</h3>
          </template>
          <template #body>
            <form @submit.prevent="handlePetSubmit" class="pet-form">
              <div class="input-group">
                <label for="petName">이름</label>
                <input
                  id="petName"
                  v-model="petForm.name"
                  type="text"
                  required
                  placeholder="반려동물 이름"
                />
              </div>

              <div class="input-group">
                <label for="petAge">나이</label>
                <input
                  id="petAge"
                  v-model="petForm.age"
                  type="text"
                  required
                  placeholder="반려동물 나이"
                />
              </div>

              <div class="input-group">
                <label for="petGroup">동물 종류</label>
                <select
                  id="petGroup"
                  v-model="selectedPetGroup"
                  @change="handlePetGroupChange"
                  required
                >
                  <option value="">동물 종류를 선택하세요</option>
                  <option
                    v-for="group in petGroups"
                    :key="group.id"
                    :value="group.id"
                  >
                    {{ group.groupname }}
                  </option>
                </select>
              </div>

              <div class="input-group" v-if="selectedPetGroup">
                <label for="petGroupType">품종</label>
                <select
                  id="petGroupType"
                  v-model="petForm.petGroupTypeId"
                  required
                >
                  <option value="">품종을 선택하세요</option>
                  <option
                    v-for="type in petTypes"
                    :key="type.id"
                    :value="type.id"
                  >
                    {{ type.typename }}
                  </option>
                </select>
              </div>

              <div class="input-group">
                <label for="petSize">크기</label>
                <select
                  id="petSize"
                  v-model="petForm.petsize"
                  required
                >
                  <option value="">크기를 선택하세요</option>
                  <option value="1">소형(10kg 이하)</option>
                  <option value="2">중형(20kg 이하)</option>
                  <option value="3">대형(30kg 이상)</option>
                </select>
              </div>

              <div class="input-group">
                <label for="petImage">사진</label>
                <div class="image-upload">
                  <img
                    v-if="imagePreview"
                    :src="imagePreview"
                    class="preview-image"
                    alt="미리보기"
                  />
                  <input
                    type="file"
                    id="petImage"
                    @change="handleImageChange"
                    accept="image/*"
                  />
                </div>
              </div>

              <div class="button-group">
                <button type="submit" class="btn">
                  {{ editingPet ? '수정하기' : '등록하기' }}
                </button>
                <button type="button" class="btn btn-secondary" @click="closeModal">
                  취소
                </button>
              </div>
            </form>
          </template>
        </Modal>
      </div>

      <!-- 펫시터 정보 탭 -->
      <div v-else-if="activeTab === 'petsitter'" class="form-container">
        <div v-if="petsitterInfo">
          <!-- 기존 펫시터 정보 표시 -->
          <h2 class="text-center mb-20">펫시터 정보</h2>
          <div class="petsitter-info">
            <div class="info-group">
              <label>이름</label>
              <div class="info-value">{{ petsitterInfo.name }}</div>
            </div>
            <div class="info-group">
              <label>연락처</label>
              <div class="info-value">{{ petsitterInfo.mobile }}</div>
            </div>
            <div class="info-group">
              <label>지역</label>
              <div class="info-value">{{ petsitterInfo.location }}</div>
            </div>

            <!-- 서비스 정보 -->
            <div class="info-group">
              <label>제공 서비스</label>
              <div class="service-list">
                <div v-for="service in petsitterInfo.services" :key="service.serviceId" class="service-item">
                  <span>{{ service.type }}</span>
                  <span class="price">{{ service.price }}원/시간</span>
                </div>
              </div>
            </div>

            <!-- 돌봄 가능한 동물 종류 -->
            <div class="info-group">
              <label>돌봄 가능한 동물</label>
              <div class="pet-types">
                <div v-for="type in petsitterInfo.petTypes" :key="type.typeName" class="pet-type-item">
                  {{ type.groupName }} - {{ type.typeName }}
                </div>
              </div>
            </div>

            <!-- 돌봄 가능한 크기 -->
            <div class="info-group">
              <label>돌봄 가능한 크기</label>
              <div class="size-list">
                <div v-for="size in petsitterInfo.petSizes" :key="size" class="size-item">
                  {{ size }}
                </div>
              </div>
            </div>

            <!-- 근무 가능 시간 -->
            <div class="info-group">
              <label>근무 가능 시간</label>
              <div class="working-hours">
                <div v-for="(hours, day) in petsitterInfo.workingHours" :key="day" class="day-schedule">
                  <span class="day">{{ getDayName(day) }}</span>
                  <div class="hours">
                    <span v-for="(isAvailable, hour) in hours" :key="hour"
                          :class="['hour-block', { available: isAvailable }]">
                      {{ hour }}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else>
          <!-- 기존 펫시터 등록 폼 -->
          <h2 class="text-center mb-20">펫시터 등록</h2>
          <form @submit.prevent="registerPetSitter">
            <div class="form-group">
              <label>위치</label>
              <select v-model="petsitterForm.addressId" class="form-select" required>
                <option value="">지역을 선택하세요</option>
                <option v-for="address in addresses" :key="address.id" :value="address.id">
                  {{ address.city }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label>돌봄 품종</label>
              <div class="pet-type-selectors">
                <div class="select-group">
                  <select v-model="selectedPetGroup" class="form-select" @change="loadPetGroupTypes">
                    <option value="">동물 종류 선택</option>
                    <option v-for="group in petGroups" :key="group.id" :value="group">
                      {{ group.groupname }}
                    </option>
                  </select>
                  <select
                    v-if="selectedPetGroup"
                    v-model="selectedPetGroupType"
                    class="form-select"
                  >
                    <option value="">세부 품종 선택</option>
                    <option v-for="type in petGroupTypes" :key="type.id" :value="type">
                      {{ type.typename }}
                    </option>
                  </select>
                  <button type="button" class="btn-add" @click="addPetGroupType">
                    추가
                  </button>
                </div>
                <div class="selected-types">
                  <span v-for="(type, index) in petsitterForm.petGroupTypes" :key="index" class="type-tag">
                    {{ type.groupName }} - {{ type.typeName }}
                    <button type="button" class="btn-remove" @click="removePetGroupType(index)">×</button>
                  </span>
                </div>
              </div>
            </div>

            <div class="form-group">
              <label>돌봄 크기</label>
              <div class="checkbox-group">
                <label v-for="size in petSizes" :key="size.id" class="checkbox-label">
                  <input
                    type="checkbox"
                    :value="size.id"
                    v-model="petsitterForm.petSizes"
                  >
                  {{ size.sizeInfo }}
                </label>
              </div>
            </div>

            <div class="form-group">
              <label>서비스 종류 및 요금</label>
              <div class="service-selectors">
                <div class="select-group">
                  <select v-model="selectedService" class="form-select">
                    <option value="">서비스 선택</option>
                    <option v-for="service in petServices" :key="service.petservice_id" :value="service">
                      {{ service.servicename }}
                    </option>
                  </select>
                  <input
                    type="number"
                    v-model="servicePrice"
                    class="form-select"
                    placeholder="시급 (원)"
                    min="0"
                  >
                  <button type="button" class="btn-add" @click="addService">
                    추가
                  </button>
                </div>
                <div class="selected-types">
                  <span v-for="(service, index) in petsitterForm.petServices" :key="index" class="type-tag">
                    {{ service.serviceName }} - {{ service.price.toLocaleString() }}원/시간
                    <button type="button" class="btn-remove" @click="removeService(index)">×</button>
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
    </div>
  </main>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useAuthStore } from '@/store/auth'
import { toast } from 'vue3-toastify'
import { useRouter } from 'vue-router'
import axios from '@/plugins/axios'
import Modal from '@/components/Modal.vue'

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

const selectedPetGroup = ref(null)
const selectedPetGroupType = ref(null)

const selectedService = ref(null)
const servicePrice = ref('')

const petsitterForm = ref({
  addressId: '',
  userId: authStore.user.userId,
  petGroupTypes: [],
  petSizes: [],
  petServices: [],
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

const addPetGroupType = () => {
  if (!selectedPetGroup.value || !selectedPetGroupType.value) return

  const newType = {
    petGroupTypeId: selectedPetGroupType.value.id,
    groupName: selectedPetGroup.value.groupname,
    typeName: selectedPetGroupType.value.typename
  }

  if (petsitterForm.value.petGroupTypes.some(type => type.petGroupTypeId === newType.petGroupTypeId)) {
    toast.warning('이미 선택된 품종입니다.')
    return
  }

  petsitterForm.value.petGroupTypes.push(newType)
  selectedPetGroupType.value = null
}

const removePetGroupType = (index) => {
  petsitterForm.value.petGroupTypes.splice(index, 1)
}

const addService = () => {
  if (!selectedService.value || !servicePrice.value) return

  const newService = {
    petServiceId: selectedService.value.petServiceId,
    serviceName: selectedService.value.servicename,
    price: parseInt(servicePrice.value)
  }

  if (petsitterForm.value.petServices.some(service => service.petServiceId === newService.petServiceId)) {
    toast.warning('이미 선택된 서비스입니다.')
    return
  }

  petsitterForm.value.petServices.push(newService)
  selectedService.value = null
  servicePrice.value = ''
}

const removeService = (index) => {
  petsitterForm.value.petServices.splice(index, 1)
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

// 공통 데이터 상태
const addresses = ref([])
const petGroups = ref([])
const petGroupTypes = ref([])
const petSizes = ref([])
const petServices = ref([])

// 반려동물 관련 상태
const pets = ref([])
const showAddPetModal = ref(false)
const editingPet = ref(null)
const petTypes = ref([])

// 반려동물 폼 데이터
const petForm = ref({
  name: '',
  age: '',
  imageUrl: '',
  petsize: null,
  petGroupTypeId: null
})

const imagePreview = ref(null)
const selectedImage = ref(null)

const handleImageChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    selectedImage.value = file
    imagePreview.value = URL.createObjectURL(file)
  }
}

const handlePetSubmit = async () => {
  try {
    const formData = new FormData()
    const petData = {
      name: petForm.value.name,
      age: petForm.value.age,
      petsize: petForm.value.petsize,
      petGroupTypeId: petForm.value.petGroupTypeId
    }

    formData.append('data', new Blob([JSON.stringify(petData)], { type: 'application/json' }))
    if (selectedImage.value) {
      formData.append('image', selectedImage.value)
    }

    const url = editingPet.value
      ? `/v1/users/${authStore.user.userId}/pets/${editingPet.value.id}`
      : `/v1/users/${authStore.user.userId}/pets`

    const method = editingPet.value ? 'put' : 'post'
    const response = await axios[method](url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (response.data.status === 'SUCCESS') {
      toast.success(editingPet.value ? '반려동물 정보가 수정되었습니다.' : '반려동물이 등록되었습니다.')
      await loadPets()
      closeModal()
    }
  } catch (error) {
    console.error('반려동물 저장 실패:', error)
    toast.error('반려동물 정보 저장에 실패했습니다.')
  }
}

const closeModal = () => {
  showAddPetModal.value = false
  editingPet.value = null
  imagePreview.value = null
  selectedImage.value = null
  petForm.value = {
    name: '',
    age: '',
    imageUrl: '',
    petsize: null,
    petGroupTypeId: null
  }
}

// 품종 정보 로드 (공통 사용)
const loadPetGroups = async () => {
  try {
    const response = await axios.get('/v1/pet-groups')
    if (response.data.status === 'SUCCESS') {
      petGroups.value = response.data.data
    }
  } catch (error) {
    console.error('품종 그룹 조회 실패:', error)
  }
}

// 탭 변경 시 데이터 로드
watch(activeTab, async (newTab) => {
  if (newTab === 'pets' || newTab === 'petsitter') {
    await loadPetGroups() // 두 탭 모두에서 필요한 데이터
    if (newTab === 'pets') {
      await loadPets()
    } else if (newTab === 'petsitter') {
      await Promise.all([
        loadAddresses(),
        loadPetServices(),
        loadPetSizes()
      ])
    }
  }
})

// 반려동물 목록 조회
const loadPets = async () => {
  try {
    const response = await axios.get(`/v1/users/${authStore.user.userId}/pets`)
    if (response.data.status === 'SUCCESS') {
      pets.value = response.data.data
    }
  } catch (error) {
    console.error('반려동물 목록 조회 실패:', error)
    toast.error('반려동물 목록을 불러오는데 실패했습니다.')
  }
}

// 선택된 그룹의 세부 품종 로드
const loadPetTypes = async (groupId) => {
  try {
    const response = await axios.get(`/v1/pet-groups/${groupId}/pet-group-types`)
    // const response = await axios.get(`/v1/pet-group-types?groupId=${groupId}`)
    if (response.data.status === 'SUCCESS') {
      petTypes.value = response.data.data
    }
  } catch (error) {
    console.error('품종 타입 조회 실패:', error)
  }
}

// petGroup 선택 시 petTypes 로드
const handlePetGroupChange = async (event) => {
  const selectedGroupId = event.target.value
  if (selectedGroupId) {
    await loadPetTypes(selectedGroupId)
  } else {
    petTypes.value = []
  }
  petForm.value.petGroupTypeId = null // 품종 선택 초기화
}

// 반려동물 삭제
const deletePet = async (pet) => {
  if (!confirm('정말 삭제하시겠습니까?')) return

  try {
    const response = await axios.delete(`/v1/users/${authStore.user.userId}/pets/${pet.id}`)
    if (response.data.status === 'SUCCESS') {
      toast.success('반려동물이 삭제되었습니다.')
      await loadPets()
    }
  } catch (error) {
    console.error('반려동물 삭제 실패:', error)
    toast.error('반려동물 삭제에 실패했습니다.')
  }
}

// 수정 모달 열기
const editPet = (pet) => {
  editingPet.value = pet
  petForm.value = {
    name: pet.name,
    age: pet.age,
    imageUrl: pet.imageUrl,
    petsize: pet.petsize,
    petGroupTypeId: pet.petGroupTypeId
  }
  showAddPetModal.value = true
}

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

// 시간 선택을 CateTime enum 형식으로 변환
function convertToCateTime(hour) {
  const nextHour = (hour + 1) % 24
  return `H${hour.toString().padStart(2, '0')}${nextHour.toString().padStart(2, '0')}`
}

// 선택된 시간을 PetCareTime 형식으로 변환
function getSelectedCareTimes() {
  const careTimes = []

  Object.entries(petsitterForm.value.workingHours).forEach(([day, hours]) => {
    hours.forEach((isSelected, hour) => {
      if (isSelected) {
        careTimes.push({
          weekday: day.toUpperCase(),
          cateTime: convertToCateTime(hour)
        })
      }
    })
  })

  return careTimes
}

// 펫시터 등록 시 데이터 전송
async function registerPetSitter() {
  try {
    const petSitterData = {
      ...petsitterForm.value,
      careTimes: getSelectedCareTimes()
    }

    const response = await axios.post('/v1/petsitters', petSitterData)
    if (response.data.success) {
      toast.success('펫시터 등록이 완료되었습니다.')
      router.push('/profile')
    }
  } catch (error) {
    console.error('펫시터 등록 에러:', error)
    toast.error('펫시터 등록에 실패했습니다.')
  }
}

// 품종 타입 로드
async function loadPetGroupTypes() {
  if (!selectedPetGroup.value) return
  try {
    const response = await axios.get(`/v1/pet-groups/${selectedPetGroup.value.id}/pet-group-types`)
    petGroupTypes.value = response.data.data
  } catch (error) {
    toast.error('품종 정보를 불러오는데 실패했습니다.')
  }
}

// 서버 기본 URL 설정
const API_BASE_URL = 'http://localhost:8080'

// 이미지 URL을 완성된 형태로 반환하는 함수
const getImageUrl = (imageUrl) => {
  if (!imageUrl) return '/default-pet.png'
  return imageUrl.startsWith('http') ? imageUrl : `${API_BASE_URL}${imageUrl}`
}

// 펫시터 등록 관련 상태 (새로 추가)
const selectedPetServices = ref([])
const selectedPetSizes = ref([])
const servicePrices = ref({})

// 기초 데이터 로드 함수들
const loadAddresses = async () => {
  try {
    const response = await axios.get('/v1/addresses')
    if (response.data.status === 'SUCCESS') {
      addresses.value = response.data.data
    }
  } catch (error) {
    console.error('주소 목록 조회 실패:', error)
    toast.error('주소 목록을 불러오는데 실패했습니다.')
  }
}

const loadPetServices = async () => {
  try {
    const response = await axios.get('/v1/pet-services')
    if (response.data.status === 'SUCCESS') {
      petServices.value = response.data.data
    }
  } catch (error) {
    console.error('서비스 목록 조회 실패:', error)
    toast.error('서비스 목록을 불러오는데 실패했습니다.')
  }
}

const loadPetSizes = async () => {
  try {
    const response = await axios.get('/v1/pet-sizes')
    if (response.data.status === 'SUCCESS') {
      petSizes.value = response.data.data
    }
  } catch (error) {
    console.error('크기 목록 조회 실패:', error)
    toast.error('크기 목록을 불러오는데 실패했습니다.')
  }
}

// 서비스 선택 처리
const handleServiceSelect = (serviceId) => {
  const index = selectedPetServices.value.indexOf(serviceId)
  if (index === -1) {
    selectedPetServices.value.push(serviceId)
    servicePrices.value[serviceId] = ''
  } else {
    selectedPetServices.value.splice(index, 1)
    delete servicePrices.value[serviceId]
  }
}

// 크기 선택 처리
const handleSizeSelect = (sizeId) => {
  const index = selectedPetSizes.value.indexOf(sizeId)
  if (index === -1) {
    selectedPetSizes.value.push(sizeId)
  } else {
    selectedPetSizes.value.splice(index, 1)
  }
}

const goToReservations = () => {
  router.push('/reservations')
}

const petsitterInfo = ref(null)

// 컴포넌트 마운트 시 펫시터 정보 조회
onMounted(async () => {
  try {
    const response = await axios.get(`/v1/petsitters/me`)
    if (response.data.status === 'SUCCESS') {
      petsitterInfo.value = response.data.data
    }
  } catch (error) {
    console.error('펫시터 정보 조회 실패:', error)
  }
})

// 요일 변환 함수
const getDayName = (day) => {
  const dayNames = {
    mon: '월요일',
    tue: '화요일',
    wed: '수요일',
    thu: '목요일',
    fri: '금요일',
    sat: '토요일',
    sun: '일요일'
  }
  return dayNames[day]
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
    gap: 10px;
  }

  .tab-button {
    padding: 8px 16px;
    font-size: 14px;
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

.pets-list {
  display: grid;
  gap: 20px;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
}

.pet-card {
  background: var(--white);
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  gap: 16px;
  align-items: center;
}

.pet-image {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}

.pet-info {
  flex: 1;
}

.pet-info h3 {
  margin: 0 0 4px 0;
}

.pet-actions {
  display: flex;
  gap: 8px;
}

.btn-icon {
  padding: 8px;
  background: none;
  border: none;
  cursor: pointer;
  color: var(--gray);
}

.btn-icon:hover {
  color: var(--black);
}

.btn-icon.delete:hover {
  color: var(--error);
}

.pet-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.image-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.preview-image {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
}

.button-group {
  display: flex;
  gap: 8px;
  margin-top: 16px;
}

@media (max-width: 768px) {
  .pets-list {
    grid-template-columns: 1fr;
  }
}

.service-list, .size-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.service-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.price-input {
  width: 150px;
  padding: 8px;
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

.petsitter-info {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.info-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-group label {
  font-weight: bold;
  color: var(--gray-dark);
}

.info-value {
  padding: 8px;
  background-color: var(--bg-secondary);
  border-radius: 4px;
}

.service-item {
  display: flex;
  justify-content: space-between;
  padding: 8px;
  background-color: var(--bg-secondary);
  border-radius: 4px;
}

.working-hours {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.day-schedule {
  display: flex;
  gap: 10px;
  align-items: center;
}

.day {
  width: 80px;
}

.hours {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}

.hour-block {
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--gray-light);
  border-radius: 4px;
  font-size: 12px;
}

.hour-block.available {
  background-color: #4CAF50;
  color: white;
}

.pet-type-item, .size-item {
  padding: 8px;
  background-color: var(--bg-secondary);
  border-radius: 4px;
}
</style>
