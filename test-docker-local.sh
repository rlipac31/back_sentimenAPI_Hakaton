#!/bin/bash

# Script para probar el Dockerfile localmente antes de Render
echo "🐳 Probando Dockerfile para Render..."

# Variables de entorno de prueba
export PORT=8090
export SPRING_DATASOURCE_URL="jdbc:h2:mem:testdb"
export SPRING_DATASOURCE_USERNAME="sa"
export SPRING_DATASOURCE_PASSWORD="password"
export DS_API_URL="http://host.docker.internal:8000/sentiment"
export API_SECURITY_TOKEN_SECRET="test-secret-local-123"
export SPRING_PROFILES_ACTIVE="test"

# Limpiar contenedores previos
echo "🧹 Limpiando contenedores anteriores..."
docker rm -f sentiment-backend-test 2>/dev/null || true

# Build
echo "🔨 Construyendo imagen Docker..."
docker build -f Dockerfile.render -t sentiment-backend:test .

if [ $? -ne 0 ]; then
    echo "❌ Error al construir la imagen"
    exit 1
fi

# Run
echo "🚀 Iniciando contenedor..."
docker run -d \
  --name sentiment-backend-test \
  -p 8090:8090 \
  -e PORT=$PORT \
  -e SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL \
  -e SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME \
  -e SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD \
  -e DS_API_URL=$DS_API_URL \
  -e API_SECURITY_TOKEN_SECRET=$API_SECURITY_TOKEN_SECRET \
  -e SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE \
  sentiment-backend:test

if [ $? -ne 0 ]; then
    echo "❌ Error al iniciar el contenedor"
    exit 1
fi

# Esperar a que inicie
echo "⏳ Esperando 30 segundos para que inicie la aplicación..."
sleep 30

# Verificar logs
echo ""
echo "📋 Últimos logs:"
docker logs sentiment-backend-test --tail 20

# Health check
echo ""
echo "🏥 Verificando health check..."
curl -s http://localhost:8090/actuator/health | python -m json.tool || echo "❌ Health check falló"

# Test básico
echo ""
echo "🧪 Probando endpoint de registro..."
curl -s -X POST http://localhost:8090/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Test123!"
  }' | python -m json.tool || echo "❌ Test de registro falló"

echo ""
echo "✅ Pruebas completadas!"
echo ""
echo "📌 Comandos útiles:"
echo "   Ver logs:      docker logs sentiment-backend-test"
echo "   Ver logs live: docker logs -f sentiment-backend-test"
echo "   Detener:       docker stop sentiment-backend-test"
echo "   Limpiar:       docker rm -f sentiment-backend-test"
echo "   Entrar:        docker exec -it sentiment-backend-test sh"
