import { request } from './client'

export function fetchInfos() {
  return request('/infos')
}

export function fetchConsumedMessages() {
  return request('/messages')
}

export function publishMessage(data) {
  return request('/messages', { method: 'POST', body: data })
}
