import axios, { AxiosError } from 'axios';
import { jwtDecode } from 'jwt-decode'

const ACCESS_TOKEN_KEY = 'ACCESS_TOKEN';

// axios 인스턴스
const instance = axios.create({
  timeout: 10000,
  withCredentials: true,
  headers: { 'Content-Type': 'application/json; charset=UTF-8' }
});

// refresh 전용 인터셉터 없는 순수 instance
const refreshInstance = axios.create({
  timeout: 10000,
  withCredentials: true,
  headers: { 'Content-Type': 'application/json; charset=UTF-8' }
});


// =========================
// 토큰 관리
// =========================
export const getAccessToken = () => localStorage.getItem(ACCESS_TOKEN_KEY);
export const setAccessToken = (token: string) => localStorage.setItem(ACCESS_TOKEN_KEY, token);
export const removeAccessToken = () => localStorage.removeItem(ACCESS_TOKEN_KEY);

export function isAccessTokenExpired(token?: string) {
  if (!token) return true;
  try {
    const { exp } = jwtDecode<{ exp: number }>(token);
    return exp * 1000 < Date.now();
  } catch {
    return true;
  }
}

// =========================
// 요청 인터셉터
// =========================
instance.interceptors.request.use(
  (config) => {
    const token = getAccessToken();
    if (token && token !== 'null' && token !== 'undefined') config.headers.Authorization = `Bearer ${token}`;
    return config;
  },
  (error) => Promise.reject(error)
);

// =========================
// 응답 인터셉터
// =========================
instance.interceptors.response.use(
  (response) => response,
  async (error: AxiosError & { config: any }) => {
    const originalRequest = error.config;

    if (
      error.response?.status === 401 &&
      !originalRequest._retry &&
      !originalRequest.url?.includes('/auth/refresh')
    ) {
      originalRequest._retry = true;

      try {
        const res = await refreshInstance.post('/api/auth/refresh', {})
        const newAccessToken = res.data.accessToken;

        setAccessToken(newAccessToken);

        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;

        return instance(originalRequest)
      } catch (refreshError) {
        removeAccessToken();
        window.location.href = '/login?reason=expired';
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);


export default instance;
