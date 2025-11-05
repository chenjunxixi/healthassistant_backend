# Health Assistant 后端（Spring Boot 3）

本仓库为 Health Assistant 的 Spring Boot 后端服务，提供用户认证、数据存储与业务接口能力。

- 前端仓库：[healthassistant_frontend](https://github.com/chenjunxixi/healthassistant_frontend)
- Android 客户端：[healthassistant_app](https://github.com/chenjunxixi/healthassistant_app)

## 技术栈

- 运行框架：Spring Boot 3.5.x（JDK 17）
- Web：spring-boot-starter-web（同时包含 webflux 依赖）
- 数据访问：spring-boot-starter-data-jpa
- 安全与认证：spring-boot-starter-security、jjwt（JWT）
- 数据库：MySQL（mysql-connector-j）
- 其他：Lombok、DevTools

依赖来源：请见 `pom.xml`

## 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.x（推荐）
- 可选：Docker（用于快速启动数据库）

## 数据库初始化

默认使用数据库名：`health_assistant_db`（可修改）。手动创建示例：
```sql
CREATE DATABASE IF NOT EXISTS health_assistant_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

JPA 配置默认：
- `spring.jpa.hibernate.ddl-auto=update`（开发环境便捷，生产建议改为 validate）
- `spring.jpa.show-sql=true`（开发调试用）

## 应用配置

请在 `src/main/resources/application.properties`（或 `.yml`）中设置数据库、JWT 与外部接口 Key。务必不要提交真实密钥到仓库。

推荐使用占位符与环境变量：

```properties
# ==============================
# 数据源配置
# ==============================
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/health_assistant_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:password}

spring.jackson.time-zone=UTC

# ==============================
# JPA / Hibernate
# ==============================
spring.jpa.hibernate.ddl-auto=${JPA_DDL_AUTO:update}
spring.jpa.show-sql=${JPA_SHOW_SQL:true}

# ==============================
# JWT
# ==============================
jwt.secret=${JWT_SECRET:please-change-me}

# ==============================
# 外部 API（按需）
# ==============================
doubao.api.key=${DOUBAO_API_KEY:}
ai.deepseek.base-url=${DEEPSEEK_BASE_URL:https://api.deepseek.com/v1}
ai.deepseek.model=${DEEPSEEK_MODEL:deepseek-chat}
ai.deepseek.api-key=${DEEPSEEK_API_KEY:}
serpapi.key=${SERPAPI_KEY:}
deepseek.key=${DEEPSEEK_KEY:}

# 日志级别（开发调试）
logging.level.org.springframework.web=DEBUG
logging.level.org.springframework.security=TRACE
```

将敏感信息以系统环境变量或启动参数传入，例如：
```bash
export DB_USERNAME=myuser
export DB_PASSWORD=secret
export JWT_SECRET=change-this
mvn spring-boot:run
```

或：
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="\
  -DDB_USERNAME=myuser \
  -DDB_PASSWORD=secret \
  -DJWT_SECRET=change-this"
```

## 本地运行

1. 启动数据库（确保账号密码可用）
2. 启动应用
   ```bash
   mvn clean spring-boot:run
   ```
   默认端口：`8080`

3. 前端联调
   - 前端开发代理已将 `/api` 转发至 `http://localhost:8080`
   - 后端建议统一接口前缀（如 `/api`），以便与前端代理规则对齐

## 构建与部署

- 生成可执行 Jar
  ```bash
  mvn clean package -DskipTests
  java -jar target/HealthAssistant-Backend-0.0.1-SNAPSHOT.jar
  ```

- 生产环境建议：
  - 调整 `spring.jpa.hibernate.ddl-auto` 为 `validate`
  - 使用反向代理（Nginx）统一域名，处理 HTTPS 与跨域
  - 使用外部化配置（环境变量/配置中心/Secrets）

## 安全与合规

- 切勿将数据库口令、JWT Secret、第三方 API Key 提交到仓库
- 使用 `.gitignore` 忽略本地配置文件，或使用环境变量管理敏感信息
- 如需开放外网，务必完善 CORS、鉴权、限流与日志审计

## 项目结构（简要）

- `src/main/java/...` 业务代码（控制器、服务、仓库、实体等）
- `src/main/resources/` 配置（`application.properties|yml`）、静态资源、模板（如需要）
- `pom.xml` 依赖与插件管理
