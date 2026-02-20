# Vue 3 + TypeScript + Vite

# Composables Package 란 뭔가?
Vue3 에서는 공통 로직을 처리할 때, composable 을 사용한다.
즉, Vue3 는 기본적으로 컴포넌트(화면)들이 공통으로 사용하는 로직을 composable 로 설정한다.
또한, Composable 은 하나의 규칙이 있는데 바로 useXXXX 라는 접두사를 사용한다는 것이다.
예)
useAuth : 인증 관련 공통 로직 
useUser : 유저 관련 공통 로직
useMenu : 메뉴 관련 공통 로직

# Composable 이 어떻게 API 를 호출하고 그 상태를 컴포넌트에 그리는가?
composable 에서 값을 세팅하여 리턴하면 그 값은 ref 가 되고 Vue 의 컴포넌트는 ref 의 값이 바뀜에 따라 랜더링하여 자동으로 화면이 변경된다.
역할로 보자면 아래와 같다.
component  : 파라미터 설정(입력)
composable : API 호출, 상태 설정(변경)
component  : 화면 표시(변경)

# Vue3 API 입력 흐름
사용자 액션 > Component (화면) > useXXXX (Composable) > api.ts > interceptor > Backend



