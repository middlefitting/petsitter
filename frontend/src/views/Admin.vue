<template>
  <main class="admin-container">
    <h1 class="text-center mb-20">관리자 페이지</h1>

    <!-- 탭 메뉴 -->
    <div class="tabs">
      <button
        :class="['tab-button', { active: currentTab === 'code' }]"
        @click="currentTab = 'code'"
      >
        코드 관리
      </button>
      <button
        :class="['tab-button', { active: currentTab === 'role' }]"
        @click="currentTab = 'role'"
      >
        권한 관리
      </button>
    </div>

    <!-- 탭 컨텐츠 -->
    <div class="tab-content">
      <CodeManagement v-if="currentTab === 'code'" />
      <RoleManagement v-else-if="currentTab === 'role'" />
    </div>
  </main>
</template>

<script setup>
import { ref } from 'vue'
import CodeManagement from '@/views/admin/CodeManage.vue'
import RoleManagement from '@/components/admin/RoleManagement.vue'

const currentTab = ref('code') // 기본값으로 코드 관리 탭 선택
</script>

<style scoped>
.admin-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.tabs {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  border-bottom: 1px solid #ddd;
}

.tab-button {
  padding: 10px 20px;
  border: none;
  background: none;
  cursor: pointer;
  font-size: 16px;
  color: #666;
  position: relative;
}

.tab-button.active {
  color: #ff4757;
  font-weight: bold;
}

.tab-button.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #ff4757;
}

.tab-content {
  background: white;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
