import { useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import api from '../api/axios'

interface LocationState {
  carId: number
  licensePlate: string
  make: string
  model: string
  amountDue: number
}

export default function CardPayment() {
  const navigate = useNavigate()
  const location = useLocation()
  const state = location.state as LocationState | null

  const [cardNumber, setCardNumber] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!cardNumber.trim()) { setError('Card number is required'); return }
    setError('')
    setLoading(true)
    try {
      await api.post('/payments/card', {
        carId:      state?.carId,
        amount:     state?.amountDue,
        cardNumber: cardNumber.trim(),
      })
      navigate('/')
    } catch (err: unknown) {
      const msg =
        (err as { response?: { data?: { message?: string } } })
          ?.response?.data?.message ?? 'Payment failed. Please try again.'
      setError(msg)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gray-950 text-white flex flex-col">

      {/* Header */}
      <header className="flex items-center px-8 py-5 border-b border-gray-800">
        <button onClick={() => navigate(-1)}
          className="text-gray-400 hover:text-white transition-colors text-sm">
          ← Back
        </button>
        <h1 className="text-xl font-bold mx-auto">ParkSmart</h1>
        <span className="w-16" />
      </header>

      <div className="flex flex-1 items-center justify-center px-4 py-12">
        <div className="w-full max-w-md bg-gray-900 border border-gray-800 rounded-2xl p-8 shadow-2xl">

          {/* Test feature badge */}
          <span className="inline-block text-xs font-semibold bg-yellow-500/20 text-yellow-400 border border-yellow-500/30 rounded-full px-3 py-0.5 mb-5">
            🧪 Test Feature
          </span>

          <h2 className="text-2xl font-bold mb-1">Card Payment</h2>
          <p className="text-gray-400 text-sm mb-6">
            {state?.make} {state?.model} · {state?.licensePlate}
          </p>

          {/* Amount */}
          <div className="bg-gray-800 rounded-xl px-4 py-3 flex justify-between items-center text-sm mb-6">
            <span className="text-gray-400">Amount Due</span>
            <span className="text-emerald-400 text-lg font-bold">
              ${state?.amountDue.toFixed(2)}
            </span>
          </div>

          <form onSubmit={handleSubmit} noValidate className="space-y-5">
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-1.5">
                Card Number
              </label>
              <input
                type="text"
                placeholder="e.g. 4111 1111 1111 1111"
                value={cardNumber}
                onChange={e => { setCardNumber(e.target.value); setError('') }}
                autoFocus
                className={`w-full bg-gray-800 border rounded-lg px-4 py-2.5 text-white placeholder-gray-500 outline-none focus:ring-2 transition-all
                  ${error ? 'border-red-500 focus:ring-red-500/40' : 'border-gray-700 focus:ring-blue-500/40 focus:border-blue-500'}`}
              />
              {error && <p className="text-red-400 text-xs mt-1.5">{error}</p>}
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full py-3 bg-blue-600 hover:bg-blue-500 disabled:opacity-50 rounded-xl font-semibold text-base transition-all active:scale-95"
            >
              {loading ? 'Processing…' : 'Pay Now'}
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}

