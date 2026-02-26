import instance from '@/assets/js/interceptor';

export const api = {
  // 인증
  login: (id: string, password: string) =>
    instance.post('/api/auth/login', { userId : id, userPw : password }),

  // 토큰 재갱신
  refresh: () =>
    instance.post('/api/auth/refresh', {}),
  
  // 로그아웃
  logout: () =>
    instance.post('/api/auth/logout', {}),

  // 사용자 정보
  getUserInfo: () =>
    instance.post('/api/user/userInfo', {}),

  // 메뉴
  getMenus: () =>
    instance.post('/api/menu/menuList', {}),
};
