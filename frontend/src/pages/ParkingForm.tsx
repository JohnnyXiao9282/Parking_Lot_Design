import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

type CarType = 'SMALL' | 'LARGE'

interface FormState {
  licensePlate: string
  make: string
  model: string
  carType: CarType
}

interface FieldError {
  licensePlate?: string
  make?: string
  model?: string
}

export default function ParkingForm() {
  const navigate = useNavigate()

  const [form, setForm] = useState<FormState>({
    licensePlate: '',
    make: '',
    model: '',
    carType: 'SMALL',
  })
  const [errors, setErrors] = useState<FieldError>({})

  function validate(): boolean {
    const e: FieldError = {}
    if (!form.licensePlate.trim()) e.licensePlate = 'License plate is required'
    if (!form.make.trim()) e.make = 'Make is required'
    if (!form.model.trim()) e.model = 'Model is required'
    setErrors(e)
    return Object.keys(e).length === 0
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!validate()) return
    navigate('/park/floor', {
      state: {
        licensePlate: form.licensePlate.trim().toUpperCase(),
        make: form.make.trim(),
        model: form.model.trim(),
        carType: form.carType,
        isSmall: form.carType === 'SMALL',
      },
    })
  }

  function handleChange(field: keyof FormState, value: string) {
    setForm(prev => ({ ...prev, [field]: value }))
    setErrors(prev => ({ ...prev, [field]: undefined }))
  }

  return (
    <div className="min-h-screen bg-gray-950 text-white flex flex-col">

      {/* Header */}
      <header className="flex items-center px-8 py-5 border-b border-gray-800">
        <button
          onClick={() => navigate('/')}
          className="text-gray-400 hover:text-white transition-colors text-sm flex items-center gap-2"
        >
          ← Back
        </button>
        <h1 className="text-xl font-bold mx-auto">ParkSmart</h1>
        <span className="w-16" />
      </header>

      {/* Form card */}
      <div className="flex flex-1 items-center justify-center px-4 py-12">
        <div className="w-full max-w-md bg-gray-900 border border-gray-800 rounded-2xl p-8 shadow-2xl">
          <h2 className="text-2xl font-bold mb-1">Park your car</h2>
          <p className="text-gray-400 text-sm mb-8">Fill in the details below to find a spot.</p>

          <form onSubmit={handleSubmit} noValidate className="space-y-5">

            {/* License plate */}
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-1.5">
                License Plate
              </label>
              <input
                type="text"
                placeholder="e.g. ABC-1234"
                value={form.licensePlate}
                onChange={e => handleChange('licensePlate', e.target.value)}
                className={`w-full bg-gray-800 border rounded-lg px-4 py-2.5 text-white placeholder-gray-500 outline-none focus:ring-2 transition-all
                  ${errors.licensePlate ? 'border-red-500 focus:ring-red-500/40' : 'border-gray-700 focus:ring-blue-500/40 focus:border-blue-500'}`}
              />
              {errors.licensePlate && (
                <p className="text-red-400 text-xs mt-1">{errors.licensePlate}</p>
              )}
            </div>

            {/* Make */}
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-1.5">
                Make
              </label>
              <input
                type="text"
                placeholder="e.g. Toyota"
                value={form.make}
                onChange={e => handleChange('make', e.target.value)}
                className={`w-full bg-gray-800 border rounded-lg px-4 py-2.5 text-white placeholder-gray-500 outline-none focus:ring-2 transition-all
                  ${errors.make ? 'border-red-500 focus:ring-red-500/40' : 'border-gray-700 focus:ring-blue-500/40 focus:border-blue-500'}`}
              />
              {errors.make && (
                <p className="text-red-400 text-xs mt-1">{errors.make}</p>
              )}
            </div>

            {/* Model */}
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-1.5">
                Model
              </label>
              <input
                type="text"
                placeholder="e.g. Corolla"
                value={form.model}
                onChange={e => handleChange('model', e.target.value)}
                className={`w-full bg-gray-800 border rounded-lg px-4 py-2.5 text-white placeholder-gray-500 outline-none focus:ring-2 transition-all
                  ${errors.model ? 'border-red-500 focus:ring-red-500/40' : 'border-gray-700 focus:ring-blue-500/40 focus:border-blue-500'}`}
              />
              {errors.model && (
                <p className="text-red-400 text-xs mt-1">{errors.model}</p>
              )}
            </div>

            {/* Car type toggle */}
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-1.5">
                Car Type
              </label>
              <div className="grid grid-cols-2 gap-3">
                {(['SMALL', 'LARGE'] as CarType[]).map(type => (
                  <button
                    key={type}
                    type="button"
                    onClick={() => handleChange('carType', type)}
                    className={`py-3 rounded-xl border text-sm font-semibold transition-all
                      ${form.carType === type
                        ? 'bg-blue-600 border-blue-500 text-white'
                        : 'bg-gray-800 border-gray-700 text-gray-400 hover:border-gray-500 hover:text-white'
                      }`}
                  >
                    {type === 'SMALL' ? '🚗 Small' : '🚙 Large'}
                  </button>
                ))}
              </div>
            </div>

            {/* Submit */}
            <button
              type="submit"
              className="w-full py-3 bg-blue-600 hover:bg-blue-500 rounded-xl font-semibold text-base transition-all active:scale-95 mt-2"
            >
              Next
            </button>

          </form>
        </div>
      </div>
    </div>
  )
}

