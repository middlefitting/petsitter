<template>
  <div class="code-manage">
    <h1>코드 관리</h1>

    <!-- 상위코드(PetGroup) 섹션 -->
    <div class="code-group">
      <div class="code-header">
        <h2>코드 그룹</h2>
        <button class="btn-add" @click="showAddGroupModal = true">+ 추가</button>
      </div>

      <table class="code-table">
        <thead>
          <tr>
            <th>그룹명</th>
            <th>코드ID</th>
            <th>작업</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="group in petGroups" :key="group.id">
            <td>{{ group.groupname }}</td>
            <td>{{ group.code }}</td>
            <td class="actions">
              <button class="btn-edit" @click="editGroup(group)">수정</button>
              <button class="btn-delete" @click="deleteGroup(group.id)">삭제</button>
              <button class="btn-detail" @click="selectGroup(group)">하위코드 관리</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 하위코드(PetGroupType) 섹션 -->
    <div v-if="selectedGroup" class="code-detail">
      <div class="code-header">
        <h3>{{ selectedGroup.groupname }} - 하위코드</h3>
        <button class="btn-add" @click="showAddTypeModal = true">+ 추가</button>
      </div>

      <table class="code-table">
        <thead>
          <tr>
            <th>품종명</th>
            <th>작업</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="type in petGroupTypes" :key="type.id">
            <td>{{ type.typename }}</td>
            <td class="actions">
              <button class="btn-edit" @click="editType(type)">수정</button>
              <button class="btn-delete" @click="deleteType(type.id)">삭제</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 상위코드 추가/수정 모달 -->
    <Modal v-if="showAddGroupModal" @close="closeGroupModal">
      <template #header>
        <h3>{{ editingGroup ? '코드 그룹 수정' : '코드 그룹 추가' }}</h3>
      </template>
      <template #body>
        <form @submit.prevent="saveGroup">
          <div class="form-group">
            <label>그룹명</label>
            <input v-model="groupForm.groupname" required>
          </div>
          <div class="form-group">
            <label>코드ID</label>
            <input v-model="groupForm.code" required>
          </div>
          <div class="button-group">
            <button type="submit" class="btn-primary">저장</button>
            <button type="button" @click="closeGroupModal">취소</button>
          </div>
        </form>
      </template>
    </Modal>

    <!-- 하위코드 추가/수정 모달 -->
    <Modal v-if="showAddTypeModal" @close="closeTypeModal">
      <template #header>
        <h3>{{ editingType ? '하위코드 수정' : '하위코드 추가' }}</h3>
      </template>
      <template #body>
        <form @submit.prevent="saveType">
          <div class="form-group">
            <label>품종명</label>
            <input v-model="typeForm.typename" required>
          </div>
          <div class="button-group">
            <button type="submit" class="btn-primary">저장</button>
            <button type="button" @click="closeTypeModal">취소</button>
          </div>
        </form>
      </template>
    </Modal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { toast } from 'vue3-toastify'
import axios from '@/plugins/axios'
import Modal from '@/components/Modal.vue'

// 상태 관리
const petGroups = ref([])
const petGroupTypes = ref([])
const selectedGroup = ref(null)
const showAddGroupModal = ref(false)
const showAddTypeModal = ref(false)
const editingGroup = ref(null)
const editingType = ref(null)

const groupForm = ref({
  groupname: '',
  code: ''
})

const typeForm = ref({
  typename: ''
})

// 초기 데이터 로드
onMounted(async () => {
  await loadGroups()
})

// 데이터 로드 함수들
async function loadGroups() {
  try {
    const response = await axios.get('/v1/admin/pet-groups')
    petGroups.value = response.data.data
  } catch (error) {
    console.error('그룹 로드 에러:', error)
    toast.error('코드 그룹 로드 실패')
  }
}

async function loadTypes(groupId) {
  try {
    const response = await axios.get(`/v1/admin/pet-group-types?groupId=${groupId}`)
    petGroupTypes.value = response.data.data
  } catch (error) {
    console.error('타입 로드 에러:', error)
    toast.error('하위코드 로드 실패')
  }
}

// 상위코드(PetGroup) 관련 함수들
async function saveGroup() {
  try {
    if (editingGroup.value) {
      await axios.put(`/v1/admin/pet-groups/${editingGroup.value.id}`, groupForm.value)
      toast.success('코드 그룹 수정 완료')
    } else {
      await axios.post('/v1/admin/pet-groups', groupForm.value)
      toast.success('코드 그룹 추가 완료')
    }
    await loadGroups()
    showAddGroupModal.value = false
    resetGroupForm()
  } catch (error) {
    console.error('그룹 저장 에러:', error)
    toast.error('저장 실패')
  }
}

function editGroup(group) {
  editingGroup.value = group
  groupForm.value = {
    groupname: group.groupname,
    code: group.code
  }
  showAddGroupModal.value = true
}

async function deleteGroup(id) {
  if (!confirm('정말 삭제하시겠습니까?')) return
  try {
    await axios.delete(`/v1/admin/pet-groups/${id}`)
    toast.success('코드 그룹 삭제 완료')
    await loadGroups()
    if (selectedGroup.value?.id === id) {
      selectedGroup.value = null
      petGroupTypes.value = []
    }
  } catch (error) {
    console.error('그룹 삭제 에러:', error)
    toast.error('삭제 실패')
  }
}

// 하위코드(PetGroupType) 관련 함수들
async function saveType() {
  try {
    const typeData = {
      ...typeForm.value,
      petGroupId: selectedGroup.value.id
    }

    if (editingType.value) {
      await axios.put(`/v1/admin/pet-group-types/${editingType.value.id}`, typeData)
      toast.success('하위코드 수정 완료')
    } else {
      await axios.post('/v1/admin/pet-group-types', typeData)
      toast.success('하위코드 추가 완료')
    }
    await loadTypes(selectedGroup.value.id)
    showAddTypeModal.value = false
    resetTypeForm()
  } catch (error) {
    console.error('타입 저장 에러:', error)
    toast.error('저장 실패')
  }
}

function editType(type) {
  editingType.value = type
  typeForm.value = {
    typename: type.typename
  }
  showAddTypeModal.value = true
}

async function deleteType(id) {
  if (!confirm('정말 삭제하시겠습니까?')) return
  try {
    await axios.delete(`/v1/admin/pet-group-types/${id}`)
    toast.success('하위코드 삭제 완료')
    await loadTypes(selectedGroup.value.id)
  } catch (error) {
    console.error('타입 삭제 에러:', error)
    toast.error('삭제 실패')
  }
}

// 그룹 선택 시 하위코드 로드
function selectGroup(group) {
  selectedGroup.value = group
  loadTypes(group.id)
}

// 폼 초기화 함수들
function resetGroupForm() {
  groupForm.value = { groupname: '', code: '' }
  editingGroup.value = null
}

function resetTypeForm() {
  typeForm.value = { typename: '' }
  editingType.value = null
}

// 모달 닫기 함수 추가
function closeGroupModal() {
  showAddGroupModal.value = false
  resetGroupForm()
}

function closeTypeModal() {
  showAddTypeModal.value = false
  resetTypeForm()
}
</script>

<style scoped>
.code-manage {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.code-group, .code-detail {
  margin-top: 20px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.code-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.code-table {
  width: 100%;
  border-collapse: collapse;
}

.code-table th,
.code-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.actions {
  display: flex;
  gap: 8px;
}

button {
  padding: 6px 12px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
}

.btn-add {
  background: #4CAF50;
  color: white;
}

.btn-edit {
  background: #2196F3;
  color: white;
}

.btn-delete {
  background: #f44336;
  color: white;
}

.btn-detail {
  background: #9E9E9E;
  color: white;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.button-group {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
