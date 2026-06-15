import { describe, expect, it, vi, beforeEach } from 'vitest'
import { fetchConsumedMessages, fetchInfos, publishMessage } from './infos'

function mockFetch(response) {
  vi.stubGlobal('fetch', vi.fn().mockResolvedValue(response))
}

describe('infos API', () => {
  beforeEach(() => {
    vi.unstubAllGlobals()
  })

  it('fetchInfos returns backend infos payload', async () => {
    const payload = {
      brokerVersion: 'Apache Kafka',
      clusterId: 'cluster-123',
      topics: [],
      fetchedAt: '2026-06-13T12:00:00'
    }

    mockFetch({
      ok: true,
      status: 200,
      text: async () => JSON.stringify(payload)
    })

    await expect(fetchInfos()).resolves.toEqual(payload)
    expect(fetch).toHaveBeenCalledWith('/api/infos', expect.any(Object))
  })

  it('fetchConsumedMessages returns message list', async () => {
    const payload = [
      {
        id: 'msg-1',
        topic: 'demo-topic',
        partition: 0,
        offset: 1,
        payload: '{"event":"demo"}',
        consumedAt: '2026-06-13T12:00:00'
      }
    ]

    mockFetch({
      ok: true,
      status: 200,
      text: async () => JSON.stringify(payload)
    })

    await expect(fetchConsumedMessages()).resolves.toEqual(payload)
    expect(fetch).toHaveBeenCalledWith('/api/messages', expect.any(Object))
  })

  it('publishMessage posts JSON and returns location header', async () => {
    mockFetch({
      ok: true,
      status: 201,
      headers: {
        get: (name) => (name === 'Location' ? 'uuid-123' : null)
      },
      text: async () => ''
    })

    await expect(publishMessage({ event: 'demo' })).resolves.toEqual({
      location: 'uuid-123'
    })

    expect(fetch).toHaveBeenCalledWith('/api/messages', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ event: 'demo' })
    })
  })
})
