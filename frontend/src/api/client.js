const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || '/api').replace(/\/$/, '')

export class ApiError extends Error {
  constructor(message, status) {
    super(message)
    this.name = 'ApiError'
    this.status = status
  }
}

export async function request(path, { method = 'GET', body, headers = {} } = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    method,
    headers: {
      ...(body !== undefined ? { 'Content-Type': 'application/json' } : {}),
      ...headers
    },
    ...(body !== undefined ? { body: JSON.stringify(body) } : {})
  })

  const text = await response.text()

  if (!response.ok) {
    let message = `Request failed: ${response.status}`

    try {
      const errorBody = JSON.parse(text)
      if (errorBody.message) {
        message = errorBody.message
      }
    } catch {
      if (text.startsWith('<!DOCTYPE')) {
        message = 'Frontend received HTML instead of JSON. Check Nginx /api proxy.'
      }
    }

    throw new ApiError(message, response.status)
  }

  if (!text) {
    return {
      location: response.headers.get('Location')
    }
  }

  try {
    return JSON.parse(text)
  } catch {
    throw new ApiError(
        'Response is not JSON. The frontend probably received index.html instead of backend data.',
        response.status
    )
  }
}