import SpotIndicator from './components/SpotIndicator'

function App() {
  return (
    <div className="relative min-h-screen bg-gray-950 text-white">

      {/* Admin Login — top right */}
      <div className="absolute top-6 right-8">
        <button className="px-4 py-2 text-sm font-medium border border-gray-500 rounded-lg text-gray-300 hover:border-white hover:text-white transition-colors">
          Admin Login
        </button>
      </div>

      {/* Hero — centered */}
      <div className="flex flex-col items-center justify-center min-h-screen gap-4">
        <h1 className="text-5xl font-bold tracking-tight mb-8">ParkSmart</h1>

        <div className="flex gap-6">
          <button className="px-10 py-4 text-lg font-semibold bg-blue-600 rounded-xl hover:bg-blue-500 active:scale-95 transition-all">
            Parking
          </button>
          <button className="px-10 py-4 text-lg font-semibold bg-emerald-600 rounded-xl hover:bg-emerald-500 active:scale-95 transition-all">
            Leaving
          </button>
        </div>
      </div>

      {/* Spot availability — bottom right */}
      <SpotIndicator />

    </div>
  )
}

export default App
