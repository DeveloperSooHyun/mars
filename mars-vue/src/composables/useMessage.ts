import { inject } from 'vue'

export type MessageSeverity = 'info' | 'success' | 'warn' | 'error'

interface MessageApi {
  addMessage: (severity: MessageSeverity, content: string) => void
}

export function useMessage() {
  const message = inject<MessageApi>('message')
  if (!message) throw new Error('Message provider not found')

  return {
    info: (msg: string) => message.addMessage('info', msg),
    success: (msg: string) => message.addMessage('success', msg),
    warn: (msg: string) => message.addMessage('warn', msg),
    error: (msg: string) => message.addMessage('error', msg)
  }
}
