# 🐳 Docker 快速参考卡

## 🚀 快速命令

```bash
# 启动（开发）
docker-compose --profile dev up -d

# 启动（生产）
docker-compose up -d

# 停止
docker-compose stop

# 下线（删除容器，保留数据）
docker-compose down

# 下线（删除所有，包括数据）
docker-compose down -v

# 查看日志
docker-compose logs -f app

# 重启
docker-compose restart

# 构建
docker-compose build
```

---

## 🔗 访问地址

| 服务 | URL | 用户名 | 密码 | 说明 |
|------|-----|--------|------|------|
| **App** | http://localhost:8082 | - | - | Spring Boot 应用 |
| **Health** | http://localhost:8082/actuator/health | - | - | 健康检查 |
| **pgAdmin** | http://localhost:5050 | admin@parksmart.local | admin | 数据库管理（开发用） |
| **Redis-Cmd** | http://localhost:8081 | - | - | Redis 管理（开发用） |
| **Database** | localhost:5434 | postgres | postgres | PostgreSQL 连接 |
| **Redis** | localhost:6379 | - | redis123 | Redis 连接 |

---

## 📊 服务状态检查

```bash
# 查看所有容器
docker-compose ps

# 查看特定容器日志
docker-compose logs postgres
docker-compose logs redis
docker-compose logs app

# 检查健康状态
docker-compose exec postgres pg_isready
docker-compose exec redis redis-cli ping
curl http://localhost:8082/actuator/health
```

---

## 💾 数据备份

```bash
# 备份数据库
docker-compose exec -T postgres \
  pg_dump -U postgres ParkingLot > backup-$(date +%Y%m%d).sql

# 恢复数据库
docker-compose exec -T postgres \
  psql -U postgres -d ParkingLot < backup-20260314.sql

# 备份 Redis
docker cp parksmart-cache:/data/dump.rdb ./redis-backup.rdb
```

---

## 🔧 故障排除

| 问题 | 原因 | 解决 |
|------|------|------|
| 无法连接应用 | 容器未启动 | `docker-compose ps` → `docker-compose logs` |
| 端口被占用 | 本地已有服务 | 修改 `.env` 中的端口号 |
| 数据库错误 | 连接失败 | `docker-compose exec postgres pg_isready` |
| Redis 连接失败 | 缓存未启动 | `docker-compose exec redis redis-cli ping` |
| 内存不足 | Docker 资源限制 | 增加 Docker Desktop 内存 |

---

## 🔐 环境配置

编辑 `.env` 文件修改：

```env
# 数据库
DB_NAME=ParkingLot
DB_PORT=5434
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Redis
REDIS_PORT=6379
REDIS_PASSWORD=redis123

# 应用
SERVER_PORT=8082
```

---

## 📈 监控指标

```bash
# 实时监控
docker stats

# 应用健康
curl http://localhost:8082/actuator/health

# JVM 指标
curl http://localhost:8082/actuator/metrics/jvm.memory.used

# 数据库连接池
curl http://localhost:8082/actuator/metrics/db.connection.pool.connections

# HTTP 请求
curl http://localhost:8082/actuator/metrics/http.server.requests
```

---

## 🎯 常用场景

### 场景 1：第一次启动
```bash
cp .env.example .env
docker-compose --profile dev up -d
sleep 30
curl http://localhost:8082/actuator/health
```

### 场景 2：查看实时日志
```bash
docker-compose logs -f app
```

### 场景 3：进入数据库
```bash
docker-compose exec postgres psql -U postgres -d ParkingLot
```

### 场景 4：完全重置
```bash
docker-compose down -v
docker volume prune -f
cp .env.example .env
docker-compose build --no-cache
docker-compose --profile dev up -d
```

### 场景 5：生产部署
```bash
# 生成 .env（生产参数）
cat > .env << EOF
DB_PASSWORD=your-strong-password
REDIS_PASSWORD=your-redis-password
SERVER_PORT=8082
EOF

# 启动生产环境（无开发工具）
docker-compose up -d

# 验证
curl https://your-domain/actuator/health
```

---

## 📋 启动脚本

### Linux/Mac
```bash
chmod +x docker-start.sh
./docker-start.sh dev
```

### Windows
```cmd
docker-start.bat dev
```

---

## 🔗 相关文件

- `Dockerfile` - 多阶段构建配置
- `docker-compose.yml` - 服务编排配置
- `.env.example` - 环境变量示例
- `DOCKER_GUIDE.md` - 完整指南

---

**提示**: 将此卡片加入书签或保存为快捷方式！

