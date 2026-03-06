import { useEffect, useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import api from '../api/axios'

interface Spot {
  spotId: number
  spotNumber: number
  occupied: boolean
}

interface LocationState {
  licensePlate: string
  make: string
  model: string
  carType: string
  isSmall: boolean
}

export default function FloorMap() {
  const navigate = useNavigate()
  const location = useLocation()
  const state = location.state as LocationState | null

  const isSmall = state?.isSmall ?? true

  const [spots, setSpots] = useState<Spot[]>([])
  const [previewSpotId, setPreviewSpotId] = useState<number | null>(null)
  const [previewSpotNumber, setPreviewSpotNumber] = useState<number | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(false)
  const [showConfirm, setShowConfirm] = useState(false)
  const [confirming, setConfirming] = useState(false)
  const [apiError, setApiError] = useState('')

  useEffect(() => {
    Promise.all([
      api.get<Spot[]>(`/spots/floor?small=${isSmall}`),
      api.get<{ spotId: number; spotNumber: number }>(`/spots/preview?small=${isSmall}`),
    ])
      .then(([floorRes, previewRes]) => {
        setSpots(floorRes.data)
        if (previewRes.data.spotId !== -1) {
          setPreviewSpotId(previewRes.data.spotId)
          setPreviewSpotNumber(previewRes.data.spotNumber)
        }
      })
      .catch(() => setError(true))
      .finally(() => setLoading(false))
  }, [isSmall])

  async function handleConfirmPark() {
    if (!state) return
    setApiError('')
    setConfirming(true)
    try {
      await api.post('/park', {
        licensePlate: state.licensePlate,
        make: state.make,
        model: state.model,
        carType: state.carType,
      })
      navigate('/')
    } catch (err: unknown) {
      const msg =
        (err as { response?: { data?: { message?: string } } })
          ?.response?.data?.message ?? 'Something went wrong. Please try again.'
      setApiError(msg)
      setShowConfirm(false)
    } finally {
      setConfirming(false)
    }
  }

  const floorLabel = isSmall ? '1F — Small Cars' : '2F — Large Cars'
  const cols = isSmall ? 20 : 10

  return (
    <div className="min-h-screen bg-gray-950 text-white flex flex-col">

      {/* Confirmation modal */}
      {showConfirm && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm">
          <div className="bg-gray-900 border border-gray-700 rounded-2xl p-8 w-full max-w-sm shadow-2xl">
            <h3 className="text-lg font-bold mb-1">Confirm Parking</h3>
            <p className="text-gray-400 text-sm mb-5">
              Please review your details before confirming.
            </p>

            {/* Car summary */}
            <div className="bg-gray-800 rounded-xl px-4 py-4 space-y-2 text-sm mb-5">
              <div className="flex justify-between">
                <span className="text-gray-400">License Plate</span>
                <span className="text-white font-semibold">{state?.licensePlate}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-400">Vehicle</span>
                <span className="text-white">{state?.make} {state?.model}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-400">Type</span>
                <span className="text-white">{isSmall ? '🚗 Small' : '🚙 Large'}</span>
              </div>
              <div className="flex justify-between border-t border-gray-700 pt-2 mt-2">
                <span className="text-gray-400">Assigned Spot</span>
                <span className="text-blue-400 font-bold">
                  #{previewSpotNumber} · {floorLabel}
                </span>
              </div>
            </div>

            {/* Buttons */}
            <div className="flex gap-3">
              <button
                onClick={() => setShowConfirm(false)}
                disabled={confirming}
                className="flex-1 py-2.5 rounded-xl border border-gray-600 text-gray-300 hover:border-gray-400 hover:text-white text-sm font-medium transition-all disabled:opacity-50"
              >
                Cancel
              </button>
              <button
                onClick={handleConfirmPark}
                disabled={confirming}
                className="flex-1 py-2.5 rounded-xl bg-blue-600 hover:bg-blue-500 text-white text-sm font-bold transition-all disabled:opacity-50"
              >
                {confirming ? 'Parking…' : 'Confirm Park'}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Header */}
      <header className="flex items-center px-8 py-5 border-b border-gray-800">
        <button
          onClick={() => navigate('/park')}
          className="text-gray-400 hover:text-white transition-colors text-sm"
        >
          ← Back
        </button>
        <h1 className="text-xl font-bold mx-auto">ParkSmart</h1>
        <span className="w-16" />
      </header>

      <div className="flex flex-col items-center px-6 py-10 gap-8">

        {/* Floor label */}
        <div className="text-center">
          <h2 className="text-2xl font-bold">{floorLabel}</h2>
          <p className="text-gray-400 text-sm mt-1">
            {spots.filter(s => !s.occupied).length} of {spots.length} spots available
          </p>
        </div>

        {/* Legend */}
        <div className="flex gap-6 text-sm text-gray-400">
          <span className="flex items-center gap-2">
            <span className="w-5 h-5 rounded bg-emerald-600 inline-block" /> Available
          </span>
          <span className="flex items-center gap-2">
            <span className="w-5 h-5 rounded bg-red-600 inline-block" /> Occupied
          </span>
          <span className="flex items-center gap-2">
            <span className="w-5 h-5 rounded bg-blue-500 ring-2 ring-white inline-block" /> Your spot
          </span>
        </div>

        {/* Grid */}
        {loading && <p className="text-gray-500 animate-pulse mt-10">Loading floor map…</p>}
        {error   && <p className="text-red-400 mt-10">Failed to load floor map.</p>}

        {!loading && !error && (
          <div
            className="grid gap-2"
            style={{ gridTemplateColumns: `repeat(${cols}, minmax(0, 1fr))` }}
          >
            {spots.map(spot => {
              const isPreview = spot.spotId === previewSpotId
              const base = 'w-10 h-10 rounded flex items-center justify-center text-xs font-bold transition-transform hover:scale-110 cursor-default select-none'
              const color = isPreview
                ? 'bg-blue-500 ring-2 ring-white text-white shadow-lg shadow-blue-500/50'
                : spot.occupied
                  ? 'bg-red-600 text-red-200'
                  : 'bg-emerald-700 text-emerald-200'
              return (
                <div
                  key={spot.spotId}
                  className={`${base} ${color}`}
                  title={`Spot ${spot.spotNumber}${isPreview ? ' — Your spot' : spot.occupied ? ' — Occupied' : ' — Available'}`}
                >
                  {spot.spotNumber}
                </div>
              )
            })}
          </div>
        )}

        {/* Assigned spot callout */}
        {previewSpotNumber && !loading && !error && (
          <div className="bg-blue-600/20 border border-blue-500/40 rounded-2xl px-8 py-5 text-center">
            <p className="text-blue-300 text-sm font-medium">Your spot will be</p>
            <p className="text-white text-4xl font-bold mt-1">#{previewSpotNumber}</p>
            <p className="text-blue-300 text-sm mt-1">{floorLabel}</p>
          </div>
        )}

        {/* API error */}
        {apiError && (
          <div className="bg-red-500/10 border border-red-500/40 rounded-lg px-4 py-3 text-red-400 text-sm w-full max-w-md text-center">
            {apiError}
          </div>
        )}

        {/* Confirm button */}
        {!loading && !error && (
          <button
            onClick={() => setShowConfirm(true)}
            className="px-12 py-3 bg-blue-600 hover:bg-blue-500 rounded-xl font-bold text-base transition-all active:scale-95"
          >
            Park Now
          </button>
        )}

      </div>
    </div>
  )
}
