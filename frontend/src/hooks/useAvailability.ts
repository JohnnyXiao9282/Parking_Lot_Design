import { useEffect, useState } from 'react'
import api from '../api/axios'

interface LevelAvailability {
  available: number
  total: number
}

interface Availability {
  smallCar: LevelAvailability
  largeCar: LevelAvailability
}

export function useAvailability(intervalMs = 10000) {
  const [data, setData] = useState<Availability | null>(null)
  const [error, setError] = useState(false)

  useEffect(() => {
    let cancelled = false

    const fetch = () => {
      api.get<Availability>('/spots/availability')
        .then(res => { if (!cancelled) { setData(res.data); setError(false) } })
        .catch(() => { if (!cancelled) setError(true) })
    }

    fetch()
    const id = setInterval(fetch, intervalMs)
    return () => { cancelled = true; clearInterval(id) }
  }, [intervalMs])

  return { data, error }
}

