<template>
  <div class="code-manage">
    <h2>코드 관리</h2>

    <!-- 상위코드(PetGroup) 섹션 -->
    <div class="code-group">
      <div class="code-header">
        <h3>코드 그룹</h3>
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
    <Modal v-if="showAddGroupModal" @close="showAddGroupModal = false">
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
            <button type="button" @click="showAddGroupModal = false">취소</button>
          </div>
        </form>
      </template>
    </Modal>

    <!-- 하위코드 추가/수정 모달 -->
    <Modal v-if="showAddTypeModal" @close="showAddTypeModal = false">
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
            <button type="button" @click="showAddTypeModal = false">취소</button>
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
const groupForm = ref({ groupname: '', code: '' })
const typeForm = ref({ typename: '' })

// 초기 데이터 로드
onMounted(() => {
  loadGroups()
})

// API 호출 함수들
async function loadGroups() {
  try {
    const response = await axios.get('/v1/petgroups')
    petGroups.value = response.data.data
  } catch (error) {
    console.error('그룹 로드 에러:', error)
    toast.error('데이터 로드 실패')
  }
}

async function loadTypes(groupId) {
  try {
    const response = await axios.get(`/v1/petgroups/${groupId}/types`)
    petGroupTypes.value = response.data.data
  } catch (error) {
    console.error('타입 로드 에러:', error)
    toast.error('하위코드 로드 실패')
  }
}

// 그룹 관련 함수들
function editGroup(group) {
  editingGroup.value = group
  groupForm.value = { ...group }
  showAddGroupModal.value = true
}

async function saveGroup() {
  try {
    if (editingGroup.value) {
      await axios.put(`/v1/petgroups/${editingGroup.value.id}`, groupForm.value)
    } else {
      await axios.post('/v1/petgroups', groupForm.value)
    }

    showAddGroupModal.value = false
    resetGroupForm()
    loadGroups()
    toast.success('저장되었습니다')
  } catch (error) {
    console.error('그룹 저장 에러:', error)
    toast.error('저장 실패')
  }
}

async function deleteGroup(id) {
  if (!confirm('정말 삭제하시겠습니까?')) return

  try {
    await axios.delete(`/v1/petgroups/${id}`)
    toast.success('삭제되었습니다')
    loadGroups()
    if (selectedGroup.value?.id === id) {
      selectedGroup.value = null
      petGroupTypes.value = []
    }
  } catch (error) {
    console.error('그룹 삭제 에러:', error)
    toast.error('삭제 실패')
  }
}

// 타입 관련 함수들
function editType(type) {
  editingType.value = type
  typeForm.value = { ...type }
  showAddTypeModal.value = true
}

async function saveType() {
  if (!selectedGroup.value) return

  try {
    if (editingType.value) {
      await axios.put(`/v1/petgrouptypes/${editingType.value.id}`, {
        ...typeForm.value,
        petGroupId: selectedGroup.value.id
      })
    } else {
      await axios.post('/v1/petgrouptypes', {
        ...typeForm.value,
        petGroupId: selectedGroup.value.id
      })
    }

    showAddTypeModal.value = false
    resetTypeForm()
    loadTypes(selectedGroup.value.id)
    toast.success('저장되었습니다')
  } catch (error) {
    console.error('타입 저장 에러:', error)
    toast.error('저장 실패')
  }
}

async function deleteType(id) {
  if (!confirm('정말 삭제하시겠습니까?')) return

  try {
    await axios.delete(`/v1/petgrouptypes/${id}`)
    toast.success('삭제되었습니다')
    loadTypes(selectedGroup.value.id)
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

