import { useEffect, useState, useCallback } from 'react'
import api from '../api/axios'

interface LevelAvailability {
  available: number
  total: number
}

interface Availability {
  smallCar: LevelAvailability
  largeCar: LevelAvailability
}

export function useAvailability(intervalMs = 5000) {
  const [data, setData] = useState<Availability | null>(null)
  const [error, setError] = useState(false)

  const fetchNow = useCallback(() => {
    api.get<Availability>('/spots/availability')
      .then(res => { setData(res.data); setError(false) })
      .catch(() => setError(true))
  }, [])

  useEffect(() => {
    fetchNow()
    const id = setInterval(fetchNow, intervalMs)
    return () => clearInterval(id)
  }, [fetchNow, intervalMs])

  return { data, error, refetch: fetchNow }
}
