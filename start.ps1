# ─── ParkSmart Dev Launcher ───────────────────────────────────────────────────
# Starts both the Spring Boot backend and the Vite frontend in parallel.
# Usage:  .\start.ps1
# Stop :  Ctrl+C  (kills both processes)
# ─────────────────────────────────────────────────────────────────────────────

$ROOT = $PSScriptRoot

# ── Env ───────────────────────────────────────────────────────────────────────
$env:JAVA_HOME  = "C:\Program Files\Microsoft\jdk-21.0.10.7-hotspot"
$env:M2_HOME    = "C:\maven\apache-maven-3.9.6"
$MVN            = "$env:M2_HOME\bin\mvn.cmd"

# Load .env file if present
$envFile = Join-Path $ROOT ".env"
if (Test-Path $envFile) {
    Get-Content $envFile | ForEach-Object {
        if ($_ -match '^\s*([^#][^=]+)=(.*)$') {
            $key   = $matches[1].Trim()
            $value = $matches[2].Trim()
            [System.Environment]::SetEnvironmentVariable($key, $value, "Process")
        }
    }
    Write-Host "[start] Loaded .env" -ForegroundColor Cyan
}

# ── Backend ───────────────────────────────────────────────────────────────────
Write-Host "[start] Starting Spring Boot backend..." -ForegroundColor Green
$backend = Start-Process -FilePath "powershell.exe" `
    -ArgumentList "-NoExit", "-Command",
        "cd '$ROOT'; `$env:JAVA_HOME='$env:JAVA_HOME'; `$env:M2_HOME='$env:M2_HOME'; & '$MVN' spring-boot:run" `
    -PassThru

# ── Frontend ──────────────────────────────────────────────────────────────────
Write-Host "[start] Starting Vite frontend..." -ForegroundColor Green
$frontendDir = Join-Path $ROOT "frontend"
$frontend = Start-Process -FilePath "powershell.exe" `
    -ArgumentList "-NoExit", "-Command",
        "cd '$frontendDir'; npm run dev" `
    -PassThru

# ── Wait / cleanup ────────────────────────────────────────────────────────────
Write-Host ""
Write-Host "  Backend  → http://localhost:8082" -ForegroundColor Yellow
Write-Host "  Frontend → http://localhost:5173" -ForegroundColor Yellow
Write-Host ""
Write-Host "[start] Both processes are running. Close their windows or press Ctrl+C here to stop." -ForegroundColor Cyan

try {
    # Keep this script alive so Ctrl+C can be used to stop everything
    Wait-Process -Id $backend.Id, $frontend.Id
} finally {
    Write-Host "`n[start] Stopping all processes..." -ForegroundColor Red
    if (!$backend.HasExited)  { Stop-Process -Id $backend.Id  -Force -ErrorAction SilentlyContinue }
    if (!$frontend.HasExited) { Stop-Process -Id $frontend.Id -Force -ErrorAction SilentlyContinue }
    Write-Host "[start] Done." -ForegroundColor Red
}

