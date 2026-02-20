export {}

declare global {
  interface Window {
    customMessage?: {
      error: (msg: string) => void
      success?: (msg: string) => void
      info?: (msg: string) => void
    }
  }

  // Tailwind / CSS import용 타입 선언
  declare module '*.css';
}
