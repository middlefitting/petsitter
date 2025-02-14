<template>
  <div class="container">
    <section class="header text-center mb-20">
      <h1>펫 용품 쇼핑</h1>
      <p class="text-gray">반려동물을 위한 다양한 용품을 만나보세요</p>
    </section>

    <section class="filters mb-20">
      <div class="card">
        <div class="grid grid-cols-3">
          <div class="input-group">
            <label for="category">카테고리</label>
            <select id="category" v-model="filters.category">
              <option value="">전체</option>
              <option value="food">사료/간식</option>
              <option value="supplies">용품</option>
              <option value="hygiene">위생용품</option>
            </select>
          </div>
          <div class="input-group">
            <label for="petType">반려동물</label>
            <select id="petType" v-model="filters.petType">
              <option value="">전체</option>
              <option value="dog">강아지</option>
              <option value="cat">고양이</option>
            </select>
          </div>
          <div class="input-group">
            <label for="sort">정렬</label>
            <select id="sort" v-model="filters.sort">
              <option value="popular">인기순</option>
              <option value="recent">최신순</option>
              <option value="price-low">가격 낮은순</option>
              <option value="price-high">가격 높은순</option>
            </select>
          </div>
        </div>
      </div>
    </section>

    <section class="products grid grid-cols-3">
      <div v-for="product in products" :key="product.id" class="card">
        <img :src="product.image" :alt="product.name" class="product-image">
        <h3>{{ product.name }}</h3>
        <p class="text-gray">{{ product.price.toLocaleString() }}원</p>
        <button @click="addToCart(product.id)" class="btn">장바구니 담기</button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { toast } from 'vue3-toastify'

const filters = ref({
  category: '',
  petType: '',
  sort: 'popular'
})

// 임시 데이터
const products = ref([
  {
    id: 1,
    name: '프리미엄 강아지 사료',
    price: 45000,
    image: '/images/product1.jpg'
  },
  // ... 더 많은 상품 데이터
])

const addToCart = (productId) => {
  // 장바구니 추가 로직
  toast.success('장바구니에 추가되었습니다.')
}
</script>

<style scoped>
.header {
  padding: 40px 0;
}

.filters .input-group {
  margin-bottom: 0;
}

select {
  width: 100%;
  padding: 12px;
  border: 1px solid var(--gray-light);
  border-radius: 4px;
  background-color: var(--white);
  cursor: pointer;
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 1rem;
}

@media (max-width: 768px) {
  .filters .grid {
    gap: 10px;
  }
}
</style>
