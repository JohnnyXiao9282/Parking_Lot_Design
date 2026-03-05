import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api/axios'

interface Quote {
  carId: number
  licensePlate: string
  make: string
  model: string
  hourlyRate: number
  hours: number
  amountDue: number
}

export default function LeaveForm() {
  const navigate = useNavigate()

  const [licensePlate, setLicensePlate] = useState('')
  const [fieldError, setFieldError] = useState('')
  const [quoteLoading, setQuoteLoading] = useState(false)

  // Payment modal state
  const [quote, setQuote] = useState<Quote | null>(null)
  const [selectedMethod, setSelectedMethod] = useState<'CARD' | 'CASH' | null>(null)
  const [quoteError, setQuoteError] = useState('')

  function handleInput(value: string) {
    setLicensePlate(value.toUpperCase())
    setFieldError('')
  }

  async function handleNext(e: React.FormEvent) {
    e.preventDefault()
    if (!licensePlate.trim()) {
      setFieldError('License plate is required')
      return
    }
    setQuoteLoading(true)
    setQuoteError('')
    try {
      const res = await api.get<Quote>(`/spots/quote/${licensePlate.trim()}`)
      setQuote(res.data)
      setSelectedMethod(null)
    } catch (err: unknown) {
      const msg =
        (err as { response?: { data?: { message?: string } } })
          ?.response?.data?.message ?? 'Car not found or not currently parked.'
      setFieldError(msg)
    } finally {
      setQuoteLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gray-950 text-white flex flex-col">

      {/* Payment modal */}
      {quote && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm">
          <div className="bg-gray-900 border border-gray-700 rounded-2xl p-8 w-full max-w-sm shadow-2xl">

            <h3 className="text-lg font-bold mb-1">Payment Due</h3>
            <p className="text-gray-400 text-sm mb-5">Choose how you'd like to pay before leaving.</p>

            {/* Car + amount summary */}
            <div className="bg-gray-800 rounded-xl px-4 py-4 space-y-2 text-sm mb-5">
              <div className="flex justify-between">
                <span className="text-gray-400">License Plate</span>
                <span className="text-white font-bold tracking-widest">{quote.licensePlate}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-400">Vehicle</span>
                <span className="text-white">{quote.make} {quote.model}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-400">Duration</span>
                <span className="text-white">{quote.hours} hr{quote.hours !== 1 ? 's' : ''}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-400">Rate</span>
                <span className="text-white">${quote.hourlyRate} / hr</span>
              </div>
              <div className="flex justify-between border-t border-gray-700 pt-2 mt-1">
                <span className="text-gray-300 font-semibold">Amount Due</span>
                <span className="text-emerald-400 text-lg font-bold">${quote.amountDue.toFixed(2)}</span>
              </div>
            </div>

            {/* Payment method toggle */}
            <p className="text-sm text-gray-400 mb-2">Select payment method</p>
            <div className="grid grid-cols-2 gap-3 mb-5">
              {(['CARD', 'CASH'] as const).map(method => (
                <button
                  key={method}
                  type="button"
                  onClick={() => setSelectedMethod(method)}
                  className={`py-3 rounded-xl border text-sm font-semibold transition-all
                    ${selectedMethod === method
                      ? 'bg-emerald-600 border-emerald-500 text-white'
                      : 'bg-gray-800 border-gray-700 text-gray-400 hover:border-gray-500 hover:text-white'
                    }`}
                >
                  {method === 'CARD' ? '💳 Card' : '💵 Cash'}
                </button>
              ))}
            </div>

            {quoteError && (
              <p className="text-red-400 text-xs mb-3">{quoteError}</p>
            )}

            <div className="flex gap-3">
              <button
                onClick={() => { setQuote(null); setSelectedMethod(null) }}
                className="flex-1 py-2.5 rounded-xl border border-gray-600 text-gray-300 hover:border-gray-400 hover:text-white text-sm font-medium transition-all"
              >
                Cancel
              </button>
              <button
                disabled={!selectedMethod}
                className="flex-1 py-2.5 rounded-xl bg-emerald-600 hover:bg-emerald-500 disabled:opacity-40 disabled:cursor-not-allowed text-white text-sm font-bold transition-all"
              >
                Proceed
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
            Enter your license plate to release your spot.
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
                  ${fieldError ? 'border-red-500 focus:ring-red-500/40' : 'border-gray-700 focus:ring-emerald-500/40 focus:border-emerald-500'}`}
              />
              {fieldError && (
                <p className="text-red-400 text-xs mt-1.5">{fieldError}</p>
              )}
            </div>

            <button
              type="submit"
              disabled={quoteLoading}
              className="w-full py-3 bg-emerald-600 hover:bg-emerald-500 disabled:opacity-50 rounded-xl font-semibold text-base transition-all active:scale-95"
            >
              {quoteLoading ? 'Looking up…' : 'Next'}
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}

