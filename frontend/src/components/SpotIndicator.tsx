import { useEffect } from 'react'
import { useAvailability } from '../hooks/useAvailability'

function Bar({ available, total }: { available: number; total: number }) {
  const pct = total > 0 ? (available / total) * 100 : 0
  const color = pct > 40 ? 'bg-emerald-500' : pct > 15 ? 'bg-yellow-400' : 'bg-red-500'

  return (
    <div className="w-full bg-gray-700 rounded-full h-1.5 mt-1">
      <div className={`${color} h-1.5 rounded-full transition-all duration-500`} style={{ width: `${pct}%` }} />
    </div>
  )
}

export default function SpotIndicator() {
  const { data, error, refetch } = useAvailability(10000)

  // Refetch immediately every time this component mounts
  // (fires on every return to the home page)
  useEffect(() => {
    refetch()
  }, [refetch])

  return (
    <div className="fixed bottom-6 right-6 bg-gray-900 border border-gray-700 rounded-2xl px-5 py-4 w-52 shadow-xl text-sm">
      <p className="text-gray-400 font-semibold text-xs uppercase tracking-widest mb-3">
        Available Spots
      </p>

      {error && (
        <p className="text-red-400 text-xs">Unable to load</p>
      )}

      {!error && !data && (
        <p className="text-gray-500 text-xs animate-pulse">Loading…</p>
      )}

      {!error && data && (
        <div className="space-y-3">
          {/* Floor 1 — Small */}
          <div>
            <div className="flex justify-between items-baseline">
              <span className="text-gray-300">1F · Small</span>
              <span className="text-white font-bold tabular-nums">
                {data.smallCar.available}
                <span className="text-gray-500 font-normal text-xs"> / {data.smallCar.total}</span>
              </span>
            </div>
            <Bar available={data.smallCar.available} total={data.smallCar.total} />
          </div>

          {/* Floor 2 — Large */}
          <div>
            <div className="flex justify-between items-baseline">
              <span className="text-gray-300">2F · Large</span>
              <span className="text-white font-bold tabular-nums">
                {data.largeCar.available}
                <span className="text-gray-500 font-normal text-xs"> / {data.largeCar.total}</span>
              </span>
            </div>
            <Bar available={data.largeCar.available} total={data.largeCar.total} />
          </div>
        </div>
      )}
    </div>
  )
}
