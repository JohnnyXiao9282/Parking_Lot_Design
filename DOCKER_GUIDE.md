# 🐳 Docker 部署指南 - ParkSmart 停车场管理系统

## 📋 目录

1. [快速开始](#快速开始)
2. [文件说明](#文件说明)
3. [详细命令](#详细命令)
4. [开发环境 vs 生产环境](#开发环境-vs-生产环境)
5. [常见问题](#常见问题)
6. [监控与日志](#监控与日志)

---

## 🚀 快速开始

### 前置条件
- ✅ Docker Desktop 已安装（Windows/Mac）或 Docker Engine（Linux）
- ✅ Docker Compose 已安装
- ✅ Git 已安装（可选，用于克隆项目）

### 3 分钟启动

```bash
# 1. 进入项目目录
cd Parking_Lot_Design

# 2. 复制环境文件
cp .env.example .env

# 3. 启动所有服务（开发模式）
./docker-start.sh dev

# 或在 Windows 上
docker-start.bat dev

# 4. 验证服务
curl http://localhost:8082/actuator/health
```

**预期输出**：
```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "redis": { "status": "UP" },
    "diskSpace": { "status": "UP" },
    "livenessState": { "status": "UP" },
    "readinessState": { "status": "UP" }
  }
}
```

---

## 📁 文件说明

### Dockerfile
多阶段构建，优化镜像大小

| 阶段 | 名称 | 作用 |
|------|------|------|
| 1 | builder | Maven 编译 Java 代码 |
| 2 | runtime | 最小 JRE 运行应用 |

**特点**：
- ✅ 使用 Alpine Linux（最小镜像基础）
- ✅ 非 root 用户运行（安全）
- ✅ 健康检查配置
- ✅ G1GC JVM 优化参数
- ✅ 最终镜像大小：~400MB

### docker-compose.yml
完整的多服务编排配置

**包含服务**：
- **postgres**: PostgreSQL 15 数据库
- **redis**: Redis 7 缓存服务
- **app**: Spring Boot 应用
- **pgadmin**: PostgreSQL 管理界面（开发用）
- **redis-commander**: Redis 管理界面（开发用）

**特点**：
- ✅ 自动依赖管理（depends_on）
- ✅ 健康检查（healthcheck）
- ✅ 数据持久化（volumes）
- ✅ 网络隔离（custom network）
- ✅ 资源限制（deploy.resources）
- ✅ 环境变量管理（.env）
- ✅ 开发/生产 Profile 配置

### .env.example
环境变量示例文件

```env
# 数据库配置
DB_NAME=ParkingLot
DB_PORT=5434
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Redis 配置
REDIS_PORT=6379
REDIS_PASSWORD=redis123

# 应用配置
SERVER_PORT=8082

# 开发工具端口
PGADMIN_PORT=5050
REDIS_COMMANDER_PORT=8081
```

### docker-start.sh / docker-start.bat
自动化启动脚本

**功能**：
- ✅ 检查 Docker 安装
- ✅ 创建 .env 文件
- ✅ 构建镜像
- ✅ 启动服务
- ✅ 健康检查
- ✅ 显示访问 URL

---

## 🔧 详细命令

### 基础操作

#### 启动服务
```bash
# 开发模式（包含 pgAdmin 和 Redis Commander）
docker-compose --profile dev up -d

# 生产模式（仅核心服务）
docker-compose up -d

# 前台运行（可查看日志）
docker-compose up
```

#### 停止服务
```bash
# 停止但保留容器和数据
docker-compose stop

# 停止并删除容器（数据持久化在 volumes）
docker-compose down

# 完全删除（包括 volumes）
docker-compose down -v
```

#### 重启服务
```bash
# 重启所有服务
docker-compose restart

# 重启特定服务
docker-compose restart app
docker-compose restart postgres
docker-compose restart redis
```

### 日志查看

```bash
# 查看所有服务日志
docker-compose logs

# 实时查看应用日志
docker-compose logs -f app

# 查看最后 100 行
docker-compose logs --tail=100

# 查看特定服务
docker-compose logs -f postgres
docker-compose logs -f redis
```

### 执行命令

```bash
# 在应用容器中执行 shell 命令
docker-compose exec app sh

# 在数据库容器中执行 SQL
docker-compose exec postgres psql -U postgres -d ParkingLot -c "SELECT COUNT(*) FROM cars;"

# 在 Redis 容器中执行命令
docker-compose exec redis redis-cli -a redis123 PING
```

### 构建与镜像

```bash
# 构建镜像（不使用缓存）
docker-compose build --no-cache

# 只构建应用镜像
docker-compose build app

# 查看本地镜像
docker images | grep parksmart

# 删除镜像
docker rmi parking-lot-design:latest
```

### 数据管理

```bash
# 查看所有 volumes
docker volume ls | grep parksmart

# 备份数据库
docker-compose exec postgres pg_dump -U postgres ParkingLot > backup.sql

# 恢复数据库
docker-compose exec -T postgres psql -U postgres -d ParkingLot < backup.sql

# 查看容器占用空间
docker system df
```

---

## 🔄 开发环境 vs 生产环境

### 开发环境（--profile dev）

```bash
docker-compose --profile dev up -d
```

**包含服务**：
- ✅ PostgreSQL（无密码认证）
- ✅ Redis（基础密码）
- ✅ Spring Boot App
- ✅ pgAdmin（数据库 UI）
- ✅ Redis Commander（缓存 UI）

**特点**：
- 宽松的安全设置
- 日志级别 DEBUG
- 热重载支持
- 快速反馈开发

**访问地址**：
- App: http://localhost:8082
- pgAdmin: http://localhost:5050
- Redis Commander: http://localhost:8081
- DB: localhost:5434

### 生产环境（无 profile）

```bash
docker-compose up -d
```

**包含服务**：
- ✅ PostgreSQL（强密码）
- ✅ Redis（认证）
- ✅ Spring Boot App
- ❌ pgAdmin（不包含）
- ❌ Redis Commander（不包含）

**特点**：
- 严格的安全设置
- 日志级别 INFO
- 资源限制：CPU 1, Memory 1GB
- 自动重启策略
- 持久化备份

**访问地址**：
- App: http://localhost:8082
- DB: 需要配置的安全访问
- Redis: 需要密码认证

---

## 💾 数据持久化

### Volumes 详解

```yaml
# postgres-data: 数据库文件
postgres-data:/var/lib/postgresql/data

# redis-data: Redis 持久化
redis-data:/data

# pgadmin-data: pgAdmin 配置
pgadmin-data:/var/lib/pgadmin

# app-logs: 应用日志
app-logs:/app/logs
```

### 备份策略

```bash
# 完整备份（包括所有数据）
docker-compose down -v
tar -czf backup-$(date +%Y%m%d).tar.gz -C $(dirname $0) .

# 数据库备份
docker-compose exec -T postgres pg_dump -U postgres -Fc ParkingLot > db-$(date +%Y%m%d).dump

# Redis 备份
docker cp parksmart-cache:/data/dump.rdb ./redis-backup-$(date +%Y%m%d).rdb
```

---

## 🐛 常见问题

### Q1: 端口已被占用

**错误信息**：
```
Error response from daemon: Bind for 0.0.0.0:8082 failed
```

**解决方案**：
```bash
# 修改 .env 文件中的 SERVER_PORT
SERVER_PORT=8083

# 或释放占用的端口
lsof -i :8082  # 查看占用进程
kill -9 <PID>  # 杀死进程
```

### Q2: 数据库连接失败

**错误信息**：
```
Connection refused: localhost:5432
```

**解决方案**：
```bash
# 1. 检查 PostgreSQL 容器状态
docker-compose ps postgres

# 2. 查看 PostgreSQL 日志
docker-compose logs postgres

# 3. 确保健康检查通过
docker-compose exec postgres pg_isready

# 4. 检查 .env 配置
cat .env | grep DB_
```

### Q3: 应用无法启动

**错误信息**：
```
Application failed to start
```

**解决方案**：
```bash
# 1. 查看详细日志
docker-compose logs -f app

# 2. 检查依赖服务
docker-compose ps

# 3. 增加启动等待时间
docker-compose up -d --wait

# 4. 手动检查依赖
docker-compose exec app curl http://postgres:5432
docker-compose exec app redis-cli -h redis ping
```

### Q4: 内存不足

**错误信息**：
```
No space left on device
or
Cannot allocate memory
```

**解决方案**：
```bash
# 1. 清理 Docker 系统
docker system prune -a

# 2. 删除旧 volumes
docker volume prune

# 3. 增加 Docker Desktop 内存分配
# Settings → Resources → Memory → 增加到 4GB 或更多

# 4. 检查磁盘空间
df -h

# 5. 临时减少应用内存（.env）
JAVA_OPTS=-Xmx256m -Xms128m
```

### Q5: 无法访问 pgAdmin

**错误信息**：
```
Cannot GET http://localhost:5050
```

**解决方案**：
```bash
# 1. 确保用 dev profile 启动
docker-compose --profile dev up -d

# 2. 检查容器状态
docker-compose ps pgadmin

# 3. 等待服务启动（需要 30 秒）
sleep 30 && curl http://localhost:5050

# 4. 查看日志
docker-compose logs pgadmin
```

---

## 📊 监控与日志

### 实时监控

```bash
# 监控 CPU 和内存使用
docker stats

# 特定容器监控
docker stats parksmart-app parksmart-db parksmart-cache

# 持续监控（每 2 秒更新）
watch -n 2 docker stats
```

### 应用健康检查

```bash
# 健康状态端点
curl http://localhost:8082/actuator/health

# 详细信息
curl http://localhost:8082/actuator/info

# 指标
curl http://localhost:8082/actuator/metrics

# 数据库连接池
curl http://localhost:8082/actuator/metrics/db.connection.pool.connections
```

### 日志聚合

```bash
# 导出所有日志
docker-compose logs > all-logs-$(date +%Y%m%d).txt

# 仅导出最后 1000 行
docker-compose logs --tail 1000 > recent-logs.txt

# 按时间戳导出
docker-compose logs --timestamps > timestamped-logs.txt
```

### 性能指标

```bash
# JVM 指标
curl http://localhost:8082/actuator/metrics/jvm.memory.used

# 线程信息
curl http://localhost:8082/actuator/metrics/jvm.threads.live

# GC 信息
curl http://localhost:8082/actuator/metrics/jvm.gc.pause

# HTTP 请求
curl http://localhost:8082/actuator/metrics/http.server.requests
```

---

## 🔒 安全建议

### 生产环境检查清单

- [ ] 修改所有默认密码
- [ ] 启用 HTTPS/TLS
- [ ] 配置防火墙规则
- [ ] 定期备份数据
- [ ] 监控日志和告警
- [ ] 更新基础镜像
- [ ] 使用私有镜像仓库
- [ ] 配置资源限制
- [ ] 启用容器重启策略

### 密码更改

```bash
# 修改 .env 文件
DB_PASSWORD=your-secure-password-here
REDIS_PASSWORD=your-redis-password-here

# 重启服务
docker-compose down -v
docker-compose up -d
```

---

## 📞 故障排除流程

### 步骤 1: 检查容器状态
```bash
docker-compose ps
# 所有容器应显示 "Up" 状态
```

### 步骤 2: 查看日志
```bash
docker-compose logs --tail 50
# 查看最后 50 行日志
```

### 步骤 3: 测试连接
```bash
docker-compose exec app ping postgres
docker-compose exec app redis-cli -h redis ping
```

### 步骤 4: 重启服务
```bash
docker-compose restart
```

### 步骤 5: 完全重建
```bash
docker-compose down -v
rm -rf .env
cp .env.example .env
docker-compose build --no-cache
docker-compose --profile dev up -d
```

---

## 📚 相关文档

- [Docker 官方文档](https://docs.docker.com)
- [Docker Compose 参考](https://docs.docker.com/compose/compose-file/compose-file-v3/)
- [Spring Boot Docker 指南](https://spring.io/guides/gs/spring-boot-docker/)

---

**最后更新**: 2026-03-14  
**维护者**: ParkSmart 开发团队  
**版本**: 1.0

