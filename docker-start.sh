#!/bin/bash

# ParkSmart Docker Startup Script
# Usage: ./docker-start.sh [dev|prod]

set -e

PROFILE=${1:-dev}
DOCKER_COMPOSE_FILE="docker-compose.yml"

echo "==================================="
echo "ParkSmart Docker Startup Script"
echo "==================================="
echo "Profile: $PROFILE"
echo ""

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

# Check if Docker daemon is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker daemon is not running. Please start Docker."
    exit 1
fi

# Check if .env file exists
if [ ! -f ".env" ]; then
    echo "⚠️  .env file not found. Creating from .env.example..."
    cp .env.example .env
    echo "✅ Created .env file. Please edit it with your configuration."
fi

# Build image
echo "📦 Building Docker image..."
docker-compose build --no-cache

# Start services
echo ""
echo "🚀 Starting services..."

if [ "$PROFILE" = "dev" ]; then
    echo "Starting in DEVELOPMENT mode (with pgAdmin and Redis Commander)..."
    docker-compose --profile dev up -d
else
    echo "Starting in PRODUCTION mode..."
    docker-compose up -d
fi

# Wait for services to be healthy
echo ""
echo "⏳ Waiting for services to be healthy..."
sleep 10

# Check service health
echo ""
echo "🔍 Checking service health..."

# Check PostgreSQL
if docker-compose exec -T postgres pg_isready -U postgres > /dev/null 2>&1; then
    echo "✅ PostgreSQL is running"
else
    echo "❌ PostgreSQL failed to start"
fi

# Check Redis
if docker-compose exec -T redis redis-cli -a redis123 ping > /dev/null 2>&1; then
    echo "✅ Redis is running"
else
    echo "❌ Redis failed to start"
fi

# Check App
if curl -s http://localhost:8082/actuator/health | grep -q "UP"; then
    echo "✅ ParkSmart App is running"
else
    echo "⏳ ParkSmart App is starting... (may take a moment)"
fi

echo ""
echo "==================================="
echo "🎉 Services are starting!"
echo "==================================="
echo ""
echo "📋 Service URLs:"
echo "  - ParkSmart App: http://localhost:8082"
echo "  - Health Check: http://localhost:8082/actuator/health"
echo ""

if [ "$PROFILE" = "dev" ]; then
    echo "  - pgAdmin: http://localhost:5050 (admin@parksmart.local / admin)"
    echo "  - Redis Commander: http://localhost:8081"
fi

echo ""
echo "📊 Database Access:"
echo "  - Host: localhost"
echo "  - Port: 5434"
echo "  - User: postgres"
echo "  - Password: postgres"
echo "  - Database: ParkingLot"
echo ""
echo "🔴 To stop all services: docker-compose down"
echo "📜 To view logs: docker-compose logs -f app"
echo "🔄 To restart services: docker-compose restart"
echo ""

