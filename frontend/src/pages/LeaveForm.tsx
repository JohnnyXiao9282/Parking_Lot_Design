import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api/axios'

export default function LeaveForm() {
  const navigate = useNavigate()

  const [licensePlate, setLicensePlate] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const [showConfirm, setShowConfirm] = useState(false)

  function handleInput(value: string) {
    setLicensePlate(value.toUpperCase())
    setError('')
  }

  function handleNext(e: React.FormEvent) {
    e.preventDefault()
    if (!licensePlate.trim()) {
      setError('License plate is required')
      return
    }
    setShowConfirm(true)
  }

  async function handleConfirmLeave() {
    setError('')
    setLoading(true)
    try {
      await api.post(`/leave/${licensePlate.trim()}`)
      navigate('/')
    } catch (err: unknown) {
      const msg =
        (err as { response?: { data?: { message?: string } } })
          ?.response?.data?.message ?? 'Something went wrong. Please try again.'
      setError(msg)
      setShowConfirm(false)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gray-950 text-white flex flex-col">

      {/* Confirm modal */}
      {showConfirm && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm">
          <div className="bg-gray-900 border border-gray-700 rounded-2xl p-8 w-full max-w-sm shadow-2xl">
            <h3 className="text-lg font-bold mb-1">Confirm Departure</h3>
            <p className="text-gray-400 text-sm mb-5">
              Please confirm you want to release the spot for this vehicle.
            </p>

            <div className="bg-gray-800 rounded-xl px-4 py-4 text-sm mb-5">
              <div className="flex justify-between">
                <span className="text-gray-400">License Plate</span>
                <span className="text-white font-bold tracking-widest">{licensePlate}</span>
              </div>
            </div>

            <div className="flex gap-3">
              <button
                onClick={() => setShowConfirm(false)}
                disabled={loading}
                className="flex-1 py-2.5 rounded-xl border border-gray-600 text-gray-300 hover:border-gray-400 hover:text-white text-sm font-medium transition-all disabled:opacity-50"
              >
                Cancel
              </button>
              <button
                onClick={handleConfirmLeave}
                disabled={loading}
                className="flex-1 py-2.5 rounded-xl bg-emerald-600 hover:bg-emerald-500 text-white text-sm font-bold transition-all disabled:opacity-50"
              >
                {loading ? 'Processing…' : 'Confirm Leave'}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Header */}
      <header className="flex items-center px-8 py-5 border-b border-gray-800">
        <button
          onClick={() => navigate('/')}
          className="text-gray-400 hover:text-white transition-colors text-sm"
        >
          ← Back
        </button>
        <h1 className="text-xl font-bold mx-auto">ParkSmart</h1>
        <span className="w-16" />
      </header>

      {/* Card */}
      <div className="flex flex-1 items-center justify-center px-4 py-12">
        <div className="w-full max-w-md bg-gray-900 border border-gray-800 rounded-2xl p-8 shadow-2xl">
          <div className="flex items-center justify-center w-14 h-14 bg-emerald-600/20 rounded-2xl mb-6">
            <span className="text-3xl">🚗</span>
          </div>

          <h2 className="text-2xl font-bold mb-1">Leave the parking lot</h2>
          <p className="text-gray-400 text-sm mb-8">
            Enter your license plate.
          </p>

          <form onSubmit={handleNext} noValidate className="space-y-5">
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-1.5">
                License Plate
              </label>
              <input
                type="text"
                placeholder="e.g. ABC-1234"
                value={licensePlate}
                onChange={e => handleInput(e.target.value)}
                autoFocus
                className={`w-full bg-gray-800 border rounded-lg px-4 py-3 text-white text-lg font-semibold tracking-widest placeholder-gray-600 outline-none focus:ring-2 transition-all
                  ${error ? 'border-red-500 focus:ring-red-500/40' : 'border-gray-700 focus:ring-emerald-500/40 focus:border-emerald-500'}`}
              />
              {error && (
                <p className="text-red-400 text-xs mt-1.5">{error}</p>
              )}
            </div>

            <button
              type="submit"
              className="w-full py-3 bg-emerald-600 hover:bg-emerald-500 rounded-xl font-semibold text-base transition-all active:scale-95"
            >
              Next
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}

