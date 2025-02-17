<template>
  <div class="role-management">
    <h2 class="mb-20">권한 관리</h2>

    <!-- 사용자 검색 -->
    <div class="search-box mb-20">
      <input
        type="text"
        v-model="searchTerm"
        placeholder="이메일 또는 이름으로 검색"
        @input="searchUsers"
      />
    </div>

    <!-- 사용자 목록 테이블 -->
    <table class="user-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>이메일</th>
          <th>이름</th>
          <th>현재 권한</th>
          <th>권한 변경</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.name }}</td>
          <td>
            <span
              v-for="role in user.roleGroups"
              :key="role.id"
              class="role-badge"
            >
              {{ role.groupname }}
            </span>
          </td>
          <td>
            <select
              v-model="user.newRole"
              @change="updateUserRole(user)"
              class="role-select"
            >
              <option value="">권한 선택</option>
              <option value="user">일반 사용자</option>
              <option value="admin">관리자</option>
              <option value="petsitter">펫시터</option>
            </select>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from '@/plugins/axios'
import { toast } from 'vue3-toastify'

const searchTerm = ref('')
const users = ref([])

// 사용자 검색
const searchUsers = async () => {
  if (searchTerm.value.length < 2) return

  try {
    const response = await axios.get(`/v1/users/search?term=${searchTerm.value}`)
    users.value = response.data.data
  } catch (error) {
    toast.error('사용자 검색에 실패했습니다.')
  }
}

// 사용자 권한 업데이트
const updateUserRole = async (user) => {
  try {
    await axios.put(`/v1/users/${user.id}/role`, {
      roleGroupName: user.newRole
    })
    toast.success('권한이 업데이트되었습니다.')
    user.roleGroups = [{ groupname: user.newRole }]
    user.newRole = ''
  } catch (error) {
    toast.error('권한 업데이트에 실패했습니다.')
  }
}
</script>

<style scoped>
.search-box input {
  width: 300px;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
}

.user-table th,
.user-table td {
  padding: 12px;
  border: 1px solid #ddd;
  text-align: left;
}

.user-table th {
  background-color: #f8f8f8;
}

.role-badge {
  display: inline-block;
  padding: 2px 8px;
  margin: 2px;
  background-color: #e0e0e0;
  border-radius: 12px;
  font-size: 12px;
}

.role-select {
  padding: 4px;
  border-radius: 4px;
  border: 1px solid #ddd;
}
</style>
