# Offer Hunter AI

AI 求职助手 —— 面向大学生的一站式求职平台，通过 AI 技术实现简历解析、岗位匹配、简历优化、模拟面试和成长规划。

## 功能

- **简历解析** — 上传 PDF/DOCX，智能提取关键信息
- **岗位匹配** — 基于技能和项目经验的智能推荐
- **简历优化** — 针对目标岗位的个性化建议，STAR 法则改写
- **模拟面试** — AI 面试题生成与回答评估
- **成长规划** — 30-60-90 天个性化学习路径

## 技术栈

| 层 | 技术 |
|---|------|
| 后端 | Java 21 · Spring Boot 3.2.5 · Spring AI · MyBatis Plus · JWT |
| 前端 | Vue 3 · TypeScript · Vite · Element Plus · ECharts |
| 数据库 | MySQL 8.0 |
| 部署 | Docker Compose · Nginx |

## 快速开始

### 环境要求

- Docker + Docker Compose

### 启动

```bash
# 1. 克隆仓库
git clone https://github.com/<your-username>/offer-hunter-ai.git
cd offer-hunter-ai

# 2. 配置环境变量
cp .env.example .env
# 编辑 .env，填入必要的密钥

# 3. 启动
docker-compose up -d
```

访问 http://localhost 即可使用。

### 环境变量

在 `.env` 中配置：

| 变量 | 说明 | 必填 |
|------|------|------|
| `DEEPSEEK_API_KEY` | DeepSeek API Key（[获取地址](https://platform.deepseek.com/)） | 是 |
| `MYSQL_PASSWORD` | MySQL root 密码 | 是 |
| `JWT_SECRET` | JWT 签名密钥（随机字符串） | 是 |

## 项目结构

```
offer-hunter-ai/
├── backend/                 # Spring Boot 后端
│   ├── src/main/java/       # Java 源码
│   └── pom.xml
├── frontend/                # Vue 3 前端
│   ├── src/
│   └── package.json
├── database/                # 数据库脚本
│   ├── schema.sql           # 表结构
│   ├── init-data.sql        # 初始数据
│   └── jobs_seed.sql        # 岗位种子数据
├── docker-compose.yml
└── .env.example
```

## API

| 模块 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 用户 | POST | /api/user/register | 注册 |
| 用户 | POST | /api/user/login | 登录 |
| 简历 | POST | /api/resume/upload | 上传简历 |
| 简历 | GET | /api/resume/list | 简历列表 |
| AI | POST | /api/ai/analyze-resume | 分析简历 |
| AI | POST | /api/ai/optimize-resume | 优化简历 |
| AI | POST | /api/ai/growth-plan | 成长计划 |
| 岗位 | GET | /api/job/list | 岗位列表 |
| 岗位 | GET | /api/job/match | 岗位匹配 |
| 面试 | POST | /api/interview/start | 开始面试 |
| 面试 | POST | /api/interview/answer | 提交回答 |

## License

MIT
