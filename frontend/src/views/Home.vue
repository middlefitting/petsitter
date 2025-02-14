<template>
  <div class="container">
    <section class="hero text-center">
      <h1>🐶 Pet Sitter</h1>
      <p class="text-gray mb-20">당신을 위한 반려동물 케어 서비스</p>
      <!-- <h1>반려동물과 함께하는 행복한 시간</h1> -->
      <!-- <p class="text-gray mb-20">믿을 수 있는 펫시터가 함께합니다</p> -->
      <router-link to="/petsitters" class="btn">펫시터 찾기</router-link>
    </section>

    <section class="features grid grid-cols-3">
      <div class="card">
        <h3>펫시터 찾기</h3>
        <p class="text-gray">누구나 펫시터, 고객이 될 수 있습니다.</p>
      </div>
      <div class="card">
        <h3>동네 서비스</h3>
        <p class="text-gray">같은 동네에 있는 사람들이 서로 돌봄 서비스를 요청할 수 있습니다.</p>
      </div>
      <div class="card">
        <h3>맞춤형 서비스</h3>
        <p class="text-gray">원하는 시간에 맞춤형 서비스를 받을 수 있습니다.</p>
      </div>
      <!-- <div class="card">
        <h3>안전한 서비스</h3>
        <p class="text-gray">실시간 위치 확인과 사진 전송으로 안심할 수 있습니다</p>
      </div>
      <div class="card">
        <h3>24/7 지원</h3>
        <p class="text-gray">언제든 문의할 수 있는 고객 지원 서비스</p>
      </div> -->
    </section>
  </div>
</template>

<script setup>
// 필요한 경우 스크립트 로직 추가
</script>

<style scoped>
.hero {
  padding: 60px 0;
}

.hero h1 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.hero p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
}

.hero .btn {
  max-width: 200px;
  margin: 0 auto;
}

.features {
  padding: 40px 0;
}

.features .card {
  text-align: center;
}

.features h3 {
  margin-bottom: 0.5rem;
}

@media (max-width: 768px) {
  .hero h1 {
    font-size: 2rem;
  }

  .hero p {
    font-size: 1rem;
  }
}
</style>
