import axios from 'axios'

const instance = axios.create({
  baseURL: 'http://localhost:8080/api',  // 백엔드 서버 포트 명시
  headers: {
    'Content-Type': 'application/json',
	'Accept': 'application/json'
  },
  withCredentials: true, // CORS 오류 방지
  timeout: 5000 // 5초 후 타임아웃
})

// 요청 인터셉터 추가
instance.interceptors.request.use(
  (config) => {
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 응답 인터셉터 추가
instance.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.code === 'ECONNABORTED') {
      console.error('요청 시간이 초과되었습니다.')
    }
    return Promise.reject(error)
  }
)

export default instance
