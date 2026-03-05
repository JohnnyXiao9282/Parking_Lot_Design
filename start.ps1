# ─── ParkSmart Dev Launcher ───────────────────────────────────────────────────
# Starts both the Spring Boot backend and the Vite frontend in parallel.
# Usage:  .\start.ps1
# Stop :  Ctrl+C  (kills both processes)
# ─────────────────────────────────────────────────────────────────────────────

$ROOT = $PSScriptRoot

# ── Tool paths ────────────────────────────────────────────────────────────────
$JAVA_HOME = "C:\Program Files\Microsoft\jdk-21.0.10.7-hotspot"
$M2_HOME   = "C:\maven\apache-maven-3.9.6"
$MVN       = "$M2_HOME\bin\mvn.cmd"

# ── Load .env into a hashtable ────────────────────────────────────────────────
$envVars = @{}
$envFile  = Join-Path $ROOT ".env"
if (Test-Path $envFile) {
    Get-Content $envFile | ForEach-Object {
        if ($_ -match '^\s*([^#=][^=]*)=(.*)$') {
            $envVars[$matches[1].Trim()] = $matches[2].Trim()
        }
    }
    Write-Host "[start] Loaded .env ($($envVars.Count) vars)" -ForegroundColor Cyan
} else {
    Write-Host "[start] WARNING: .env not found — database config may be missing" -ForegroundColor Yellow
}

# Build a block of $env:VAR=VALUE lines to inject into the child shell
$envBlock = ($envVars.GetEnumerator() | ForEach-Object {
    "`$env:$($_.Key) = '$($_.Value)'"
}) -join "; "

# ── Backend ───────────────────────────────────────────────────────────────────
Write-Host "[start] Starting Spring Boot backend..." -ForegroundColor Green

$backendCmd = "
    `$env:JAVA_HOME = '$JAVA_HOME';
    `$env:M2_HOME   = '$M2_HOME';
    $envBlock;
    Set-Location '$ROOT';
    & '$MVN' spring-boot:run
"

$backend = Start-Process powershell.exe `
    -ArgumentList "-NoExit", "-Command", $backendCmd `
    -PassThru

# ── Frontend ──────────────────────────────────────────────────────────────────
Write-Host "[start] Starting Vite frontend..." -ForegroundColor Green

$frontendDir = Join-Path $ROOT "frontend"
$frontend = Start-Process powershell.exe `
    -ArgumentList "-NoExit", "-Command", "Set-Location '$frontendDir'; npm run dev" `
    -PassThru

# ── Info ──────────────────────────────────────────────────────────────────────
Write-Host ""
$backendPort = if ($envVars['SERVER_PORT']) { $envVars['SERVER_PORT'] } else { '8082' }
Write-Host "  Backend  -> http://localhost:$backendPort" -ForegroundColor Yellow
Write-Host "  Frontend -> http://localhost:5173" -ForegroundColor Yellow
Write-Host ""
Write-Host "[start] Both processes running. Close their windows or Ctrl+C here to stop all." -ForegroundColor Cyan

try {
    Wait-Process -Id $backend.Id, $frontend.Id
} finally {
    Write-Host "`n[start] Stopping all processes..." -ForegroundColor Red
    if (!$backend.HasExited)  { Stop-Process -Id $backend.Id  -Force -ErrorAction SilentlyContinue }
    if (!$frontend.HasExited) { Stop-Process -Id $frontend.Id -Force -ErrorAction SilentlyContinue }
    Write-Host "[start] Done." -ForegroundColor Red
}
