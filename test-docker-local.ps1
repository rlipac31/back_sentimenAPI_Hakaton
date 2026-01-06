# Script para probar el Dockerfile localmente antes de Render (Windows)

Write-Host "🐳 Probando Dockerfile para Render..." -ForegroundColor Cyan

# Variables de entorno de prueba
$env:PORT = "8090"
$env:SPRING_DATASOURCE_URL = "jdbc:h2:mem:testdb"
$env:SPRING_DATASOURCE_USERNAME = "sa"
$env:SPRING_DATASOURCE_PASSWORD = "password"
$env:DS_API_URL = "http://host.docker.internal:8000/sentiment"
$env:API_SECURITY_TOKEN_SECRET = "test-secret-local-123"
$env:SPRING_PROFILES_ACTIVE = "test"

# Limpiar contenedores previos
Write-Host "🧹 Limpiando contenedores anteriores..." -ForegroundColor Yellow
docker rm -f sentiment-backend-test 2>$null

# Build
Write-Host "🔨 Construyendo imagen Docker..." -ForegroundColor Green
docker build -f Dockerfile.render -t sentiment-backend:test .

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Error al construir la imagen" -ForegroundColor Red
    exit 1
}

# Run
Write-Host "🚀 Iniciando contenedor..." -ForegroundColor Green
docker run -d `
  --name sentiment-backend-test `
  -p 8090:8090 `
  -e PORT=$env:PORT `
  -e SPRING_DATASOURCE_URL=$env:SPRING_DATASOURCE_URL `
  -e SPRING_DATASOURCE_USERNAME=$env:SPRING_DATASOURCE_USERNAME `
  -e SPRING_DATASOURCE_PASSWORD=$env:SPRING_DATASOURCE_PASSWORD `
  -e DS_API_URL=$env:DS_API_URL `
  -e API_SECURITY_TOKEN_SECRET=$env:API_SECURITY_TOKEN_SECRET `
  -e SPRING_PROFILES_ACTIVE=$env:SPRING_PROFILES_ACTIVE `
  sentiment-backend:test

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Error al iniciar el contenedor" -ForegroundColor Red
    exit 1
}

# Esperar a que inicie
Write-Host "⏳ Esperando 30 segundos para que inicie la aplicación..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Verificar logs
Write-Host ""
Write-Host "📋 Últimos logs:" -ForegroundColor Cyan
docker logs sentiment-backend-test --tail 20

# Health check
Write-Host ""
Write-Host "🏥 Verificando health check..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8090/actuator/health" -Method Get
    $response | ConvertTo-Json
    Write-Host "✅ Health check exitoso" -ForegroundColor Green
} catch {
    Write-Host "❌ Health check falló: $_" -ForegroundColor Red
}

# Test básico
Write-Host ""
Write-Host "🧪 Probando endpoint de registro..." -ForegroundColor Cyan
try {
    $body = @{
        username = "testuser"
        email = "test@example.com"
        password = "Test123!"
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "http://localhost:8090/auth/register" -Method Post -Body $body -ContentType "application/json"
    $response | ConvertTo-Json
    Write-Host "✅ Test de registro exitoso" -ForegroundColor Green
} catch {
    Write-Host "⚠️ Test de registro: $_" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "✅ Pruebas completadas!" -ForegroundColor Green
Write-Host ""
Write-Host "📌 Comandos útiles:" -ForegroundColor Cyan
Write-Host "   Ver logs:      docker logs sentiment-backend-test"
Write-Host "   Ver logs live: docker logs -f sentiment-backend-test"
Write-Host "   Detener:       docker stop sentiment-backend-test"
Write-Host "   Limpiar:       docker rm -f sentiment-backend-test"
Write-Host "   Entrar:        docker exec -it sentiment-backend-test sh"
