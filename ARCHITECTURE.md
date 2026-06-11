# Offer Hunter AI - 系统架构设计文档

## 1. 项目概述

**项目名称**：Offer Hunter AI（Offer 捕手）

**项目定位**：面向大学生的 AI 求职助手，通过 AI 技术实现简历解析、岗位匹配、简历优化、模拟面试和成长规划的完整闭环。

**核心价值**：用户上传简历后，AI 自动完成从简历分析到成长规划的全流程，帮助学生快速找到适合自己的岗位。

---

## 2. 系统架构

### 2.1 整体架构

```
┌─────────────────────────────────────────────────────────────┐
│                      前端 (Vue3 + TypeScript)                │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐       │
│  │ 简历上传 │  │ AI分析  │  │ 岗位匹配 │  │ 模拟面试 │       │
│  └─────────┘  └─────────┘  └─────────┘  └─────────┘       │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   API 网关 (Nginx)                          │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                 后端 (Spring Boot 3.x)                      │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                   Controller 层                      │   │
│  │  ResumeController │ JobController │ AIController    │   │
│  └─────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                    Service 层                        │   │
│  │  ResumeService │ JobService │ MatchService          │   │
│  │  AIService │ InterviewService │ GrowthService       │   │
│  └─────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                   Mapper 层 (MyBatis Plus)           │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                              │
              ┌───────────────┼───────────────┐
              ▼               ▼               ▼
        ┌─────────┐    ┌─────────┐    ┌─────────────┐
        │  MySQL  │    │  文件存储 │    │ DeepSeek API│
        │  数据库  │    │ (本地/OSS)│    │  (AI模型)   │
        └─────────┘    └─────────┘    └─────────────┘
```

### 2.2 技术架构分层

| 层级 | 技术选型 | 说明 |
|------|---------|------|
| 前端展示层 | Vue3 + TypeScript + Element Plus + ECharts | 响应式 UI，数据可视化 |
| API 网关层 | Nginx | 反向代理，负载均衡，静态资源服务 |
| 后端业务层 | Spring Boot 3.x + Spring AI | RESTful API，AI 能力集成 |
| 数据持久层 | MyBatis Plus + MySQL | ORM 映射，数据持久化 |
| 文件处理层 | Apache POI + PDFBox | 简历文件解析 |
| AI 服务层 | DeepSeek API | 简历分析、岗位匹配、面试模拟等 |

---

## 3. 模块设计

### 3.1 模块一：简历上传模块

**功能描述**：支持 PDF/DOCX 格式简历上传，自动解析提取关键信息。

**技术实现**：
- 前端：Element Plus Upload 组件，支持拖拽上传
- 后端：Apache POI 解析 DOCX，PDFBox 解析 PDF
- 解析内容：学校、专业、技能、项目经历、实习经历、获奖经历

**接口设计**：
```
POST /api/resume/upload
Content-Type: multipart/form-data

Response: {
  "code": 200,
  "data": {
    "resumeId": "uuid",
    "parsedContent": { ... }
  }
}
```

### 3.2 模块二：AI 简历画像模块

**功能描述**：调用大模型生成求职画像、技能标签和能力雷达图。

**技术实现**：
- 使用 Spring AI 集成 DeepSeek API
- Prompt 工程：设计结构化提示词提取画像信息
- 前端：ECharts 雷达图展示能力维度

**输出内容**：
- 求职画像：软件开发方向、AI 应用方向、产品方向等
- 技能标签：Java、SpringBoot、MySQL、Redis 等
- 能力雷达图：开发能力、项目能力、学习能力、沟通能力、实践能力

**接口设计**：
```
POST /api/ai/analyze-resume
Request: { "resumeId": "uuid" }

Response: {
  "code": 200,
  "data": {
    "jobDirection": "软件开发方向",
    "skillTags": ["Java", "SpringBoot", "MySQL"],
    "abilityScores": {
      "development": 85,
      "project": 80,
      "learning": 90,
      "communication": 75,
      "practice": 82
    }
  }
}
```

### 3.3 模块三：岗位匹配引擎

**功能描述**：建立本地岗位库，计算简历与岗位的匹配度。

**技术实现**：
- 本地岗位库：100-200 条模拟数据，覆盖主流公司和岗位
- 匹配算法：技能重合度 + 项目匹配度 + 综合评分
- 排序：按匹配度降序排列

**匹配逻辑**：
```
匹配度 = 技能重合度 × 0.6 + 项目匹配度 × 0.4

技能重合度 = 匹配技能数 / 岗位要求技能数
项目匹配度 = AI 评估项目与岗位的相关性 (0-1)
```

**接口设计**：
```
GET /api/job/match?resumeId={resumeId}

Response: {
  "code": 200,
  "data": [
    {
      "jobId": 1,
      "company": "腾讯",
      "position": "Java开发实习生",
      "matchScore": 92,
      "matchedSkills": ["Java", "SpringBoot", "MySQL"],
      "missingSkills": ["Redis", "Docker"],
      "matchReason": "具备扎实的 Java 基础和 SpringBoot 项目经验..."
    }
  ]
}
```

### 3.4 模块四：AI 简历优化

**功能描述**：根据目标岗位生成简历优化建议。

**技术实现**：
- 用户选择目标岗位
- AI 分析简历与岗位的差距
- 生成优化建议和 STAR 法则改写版本

**输出内容**：
- 当前问题分析
- 优化建议
- 项目经历优化版本
- STAR 法则改写版本

**接口设计**：
```
POST /api/ai/optimize-resume
Request: {
  "resumeId": "uuid",
  "targetJobId": 1
}

Response: {
  "code": 200,
  "data": {
    "currentIssues": ["项目描述过于简单", "缺少量化成果"],
    "suggestions": ["使用 STAR 法则重写项目经历", "添加量化数据"],
    "optimizedContent": { ... }
  }
}
```

### 3.5 模块五：AI 模拟面试

**功能描述**：根据岗位自动生成面试题，支持用户回答和 AI 点评。

**技术实现**：
- AI 生成技术面试题、项目追问、HR 问题、行为面试题
- 用户输入回答后 AI 进行点评
- 提供改进建议

**接口设计**：
```
POST /api/interview/start
Request: { "jobId": 1 }

Response: {
  "code": 200,
  "data": {
    "interviewId": "uuid",
    "questions": [
      {
        "id": 1,
        "type": "TECHNICAL",
        "question": "请解释 Java 中的多态性"
      }
    ]
  }
}

POST /api/interview/answer
Request: {
  "interviewId": "uuid",
  "questionId": 1,
  "answer": "用户回答内容"
}

Response: {
  "code": 200,
  "data": {
    "score": 85,
    "comment": "回答准确，但可以补充实际案例",
    "improvement": "建议结合项目经验说明..."
  }
}
```

### 3.6 模块六：成长路线规划

**功能描述**：根据用户当前能力生成 30/60/90 天成长路线。

**技术实现**：
- AI 分析用户能力短板
- 生成分阶段成长计划
- 提供学习资源推荐

**输出内容**：
- 30 天：基础强化阶段
- 60 天：项目实战阶段
- 90 天：面试冲刺阶段

**接口设计**：
```
POST /api/growth/plan
Request: { "resumeId": "uuid" }

Response: {
  "code": 200,
  "data": {
    "phases": [
      {
        "duration": "30天",
        "title": "基础强化阶段",
        "goals": ["强化 Java 基础", "学习 SpringBoot"],
        "tasks": [...],
        "resources": [...]
      },
      ...
    ]
  }
}
```

---

## 4. 数据库设计

### 4.1 ER 图

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│    user     │       │   resume    │       │    job      │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ id (PK)     │──┐    │ id (PK)     │       │ id (PK)     │
│ username    │  │    │ user_id(FK) │──┐    │ company     │
│ password    │  └───>│ content     │  │    │ position    │
│ email       │       │ raw_text    │  │    │ description │
│ phone       │       │ file_path   │  │    │ salary_range│
│ create_time │       │ create_time │  │    │ location    │
└─────────────┘       └─────────────┘  │    │ create_time │
                                       │    └─────────────┘
                                       │
                       ┌───────────────┼───────────────┐
                       ▼               ▼               ▼
              ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
              │resume_analysis│ │ match_result │ │job_skill    │
              ├─────────────┤ ├─────────────┤ ├─────────────┤
              │ id (PK)     │ │ id (PK)     │ │ id (PK)     │
              │ resume_id   │ │ resume_id   │ │ job_id (FK) │
              │ job_direction│ │ job_id (FK) │ │ skill_name  │
              │ skill_tags  │ │ match_score │ │ skill_level │
              │ ability_json│ │ matched_    │ └─────────────┘
              │ create_time │ │  skills     │
              └─────────────┘ │ missing_    │
                              │  skills     │
                              │ create_time │
                              └─────────────┘

              ┌─────────────────────┐ ┌─────────────┐
              │ interview_question  │ │growth_plan  │
              ├─────────────────────┤ ├─────────────┤
              │ id (PK)             │ │ id (PK)     │
              │ job_id (FK)         │ │ resume_id   │
              │ question_type       │ │ phase       │
              │ question_content    │ │ title       │
              │ answer_content      │ │ goals_json  │
              │ ai_comment          │ │ tasks_json  │
              │ ai_score            │ │ resources   │
              │ create_time         │ │ create_time │
              └─────────────────────┘ └─────────────┘
```

### 4.2 表结构详细设计

#### user 表
```sql
CREATE TABLE `user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(100),
  `phone` VARCHAR(20),
  `avatar` VARCHAR(255),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### resume 表
```sql
CREATE TABLE `resume` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `file_name` VARCHAR(255) NOT NULL,
  `file_path` VARCHAR(500) NOT NULL,
  `raw_text` LONGTEXT,
  `parsed_content` JSON,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);
```

#### resume_analysis 表
```sql
CREATE TABLE `resume_analysis` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `resume_id` BIGINT NOT NULL,
  `job_direction` VARCHAR(100),
  `skill_tags` JSON,
  `ability_scores` JSON,
  `summary` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`)
);
```

#### job 表
```sql
CREATE TABLE `job` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `company` VARCHAR(100) NOT NULL,
  `position` VARCHAR(100) NOT NULL,
  `description` TEXT,
  `requirements` TEXT,
  `salary_range` VARCHAR(50),
  `location` VARCHAR(100),
  `job_type` VARCHAR(20),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

#### job_skill 表
```sql
CREATE TABLE `job_skill` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `job_id` BIGINT NOT NULL,
  `skill_name` VARCHAR(50) NOT NULL,
  `skill_level` VARCHAR(20),
  `is_required` TINYINT DEFAULT 1,
  FOREIGN KEY (`job_id`) REFERENCES `job`(`id`)
);
```

#### match_result 表
```sql
CREATE TABLE `match_result` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `resume_id` BIGINT NOT NULL,
  `job_id` BIGINT NOT NULL,
  `match_score` DECIMAL(5,2),
  `matched_skills` JSON,
  `missing_skills` JSON,
  `match_reason` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`),
  FOREIGN KEY (`job_id`) REFERENCES `job`(`id`)
);
```

#### interview_question 表
```sql
CREATE TABLE `interview_question` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `interview_id` VARCHAR(50) NOT NULL,
  `job_id` BIGINT NOT NULL,
  `question_type` VARCHAR(20),
  `question_content` TEXT NOT NULL,
  `answer_content` TEXT,
  `ai_comment` TEXT,
  `ai_score` INT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`job_id`) REFERENCES `job`(`id`)
);
```

#### growth_plan 表
```sql
CREATE TABLE `growth_plan` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `resume_id` BIGINT NOT NULL,
  `phase` INT NOT NULL,
  `duration` VARCHAR(20),
  `title` VARCHAR(100),
  `goals` JSON,
  `tasks` JSON,
  `resources` JSON,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`)
);
```

---

## 5. API 接口设计

### 5.1 接口规范

- 基础路径：`/api`
- 数据格式：JSON
- 认证方式：JWT Token
- 响应格式：

```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

### 5.2 接口列表

| 模块 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 用户 | POST | /api/user/register | 用户注册 |
| 用户 | POST | /api/user/login | 用户登录 |
| 简历 | POST | /api/resume/upload | 上传简历 |
| 简历 | GET | /api/resume/{id} | 获取简历详情 |
| 简历 | GET | /api/resume/list | 获取简历列表 |
| AI分析 | POST | /api/ai/analyze-resume | AI 分析简历 |
| AI分析 | GET | /api/ai/analysis/{resumeId} | 获取分析结果 |
| 岗位 | GET | /api/job/list | 获取岗位列表 |
| 岗位 | GET | /api/job/{id} | 获取岗位详情 |
| 岗位 | GET | /api/job/match | 岗位匹配 |
| 优化 | POST | /api/ai/optimize-resume | 简历优化 |
| 面试 | POST | /api/interview/start | 开始面试 |
| 面试 | POST | /api/interview/answer | 提交回答 |
| 面试 | GET | /api/interview/{id}/result | 获取面试结果 |
| 成长 | POST | /api/growth/plan | 生成成长计划 |
| 成长 | GET | /api/growth/{resumeId} | 获取成长计划 |

---

## 6. 前端架构设计

### 6.1 目录结构

```
frontend/
├── public/
│   └── favicon.ico
├── src/
│   ├── api/                    # API 接口封装
│   │   ├── index.ts           # axios 实例配置
│   │   ├── resume.ts          # 简历相关接口
│   │   ├── job.ts             # 岗位相关接口
│   │   ├── ai.ts              # AI 相关接口
│   │   ├── interview.ts       # 面试相关接口
│   │   └── growth.ts          # 成长规划接口
│   ├── assets/                # 静态资源
│   │   ├── images/
│   │   └── styles/
│   ├── components/            # 公共组件
│   │   ├── layout/            # 布局组件
│   │   │   ├── Header.vue
│   │   │   ├── Footer.vue
│   │   │   └── Sidebar.vue
│   │   ├── common/            # 通用组件
│   │   │   ├── RadarChart.vue
│   │   │   ├── SkillTag.vue
│   │   │   └── MatchScore.vue
│   │   └── business/          # 业务组件
│   │       ├── ResumeCard.vue
│   │       ├── JobCard.vue
│   │       └── InterviewChat.vue
│   ├── router/                # 路由配置
│   │   └── index.ts
│   ├── stores/                # Pinia 状态管理
│   │   ├── user.ts
│   │   ├── resume.ts
│   │   └── job.ts
│   ├── views/                 # 页面视图
│   │   ├── Home.vue           # 首页
│   │   ├── ResumeUpload.vue   # 简历上传
│   │   ├── Analysis.vue       # AI 分析页
│   │   ├── JobMatch.vue       # 岗位匹配页
│   │   ├── ResumeOptimize.vue # 简历优化页
│   │   ├── Interview.vue      # 模拟面试页
│   │   └── GrowthPlan.vue     # 成长规划页
│   ├── utils/                 # 工具函数
│   │   ├── request.ts
│   │   └── storage.ts
│   ├── App.vue
│   └── main.ts
├── index.html
├── package.json
├── tsconfig.json
└── vite.config.ts
```

### 6.2 页面路由设计

| 路径 | 页面 | 说明 |
|------|------|------|
| / | Home | 首页，项目介绍 |
| /upload | ResumeUpload | 简历上传页 |
| /analysis/:id | Analysis | AI 分析结果页 |
| /jobs | JobMatch | 岗位匹配推荐页 |
| /optimize/:id | ResumeOptimize | 简历优化页 |
| /interview/:jobId | Interview | 模拟面试页 |
| /growth/:id | GrowthPlan | 成长规划页 |

---

## 7. 后端架构设计

### 7.1 目录结构

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── offerhunter/
│   │   │           ├── OfferHunterApplication.java
│   │   │           ├── config/              # 配置类
│   │   │           │   ├── WebConfig.java
│   │   │           │   ├── SecurityConfig.java
│   │   │           │   ├── AiConfig.java
│   │   │           │   └── FileConfig.java
│   │   │           ├── controller/          # 控制器
│   │   │           │   ├── UserController.java
│   │   │           │   ├── ResumeController.java
│   │   │           │   ├── JobController.java
│   │   │           │   ├── AiController.java
│   │   │           │   ├── InterviewController.java
│   │   │           │   └── GrowthController.java
│   │   │           ├── service/             # 服务层
│   │   │           │   ├── UserService.java
│   │   │           │   ├── ResumeService.java
│   │   │           │   ├── JobService.java
│   │   │           │   ├── MatchService.java
│   │   │           │   ├── AiService.java
│   │   │           │   ├── InterviewService.java
│   │   │           │   └── GrowthService.java
│   │   │           ├── service/impl/        # 服务实现
│   │   │           │   ├── UserServiceImpl.java
│   │   │           │   ├── ResumeServiceImpl.java
│   │   │           │   ├── JobServiceImpl.java
│   │   │           │   ├── MatchServiceImpl.java
│   │   │           │   ├── AiServiceImpl.java
│   │   │           │   ├── InterviewServiceImpl.java
│   │   │           │   └── GrowthServiceImpl.java
│   │   │           ├── mapper/              # MyBatis Mapper
│   │   │           │   ├── UserMapper.java
│   │   │           │   ├── ResumeMapper.java
│   │   │           │   ├── JobMapper.java
│   │   │           │   ├── JobSkillMapper.java
│   │   │           │   ├── MatchResultMapper.java
│   │   │           │   ├── InterviewQuestionMapper.java
│   │   │           │   └── GrowthPlanMapper.java
│   │   │           ├── entity/              # 实体类
│   │   │           │   ├── User.java
│   │   │           │   ├── Resume.java
│   │   │           │   ├── ResumeAnalysis.java
│   │   │           │   ├── Job.java
│   │   │           │   ├── JobSkill.java
│   │   │           │   ├── MatchResult.java
│   │   │           │   ├── InterviewQuestion.java
│   │   │           │   └── GrowthPlan.java
│   │   │           ├── dto/                 # 数据传输对象
│   │   │           ├── vo/                  # 视图对象
│   │   │           ├── common/              # 公共类
│   │   │           │   ├── Result.java
│   │   │           │   ├── ResultCode.java
│   │   │           │   └── PageResult.java
│   │   │           ├── exception/           # 异常处理
│   │   │           │   ├── BusinessException.java
│   │   │           │   └── GlobalExceptionHandler.java
│   │   │           ├── util/                # 工具类
│   │   │           │   ├── JwtUtil.java
│   │   │           │   ├── FileUtil.java
│   │   │           │   └── ResumeParser.java
│   │   │           └── constant/            # 常量
│   │   │               └── Constants.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       ├── mapper/                    # MyBatis XML
│   │       └── prompts/                   # AI 提示词模板
│   │           ├── resume-analysis.st
│   │           ├── job-match.st
│   │           ├── resume-optimize.st
│   │           ├── interview-question.st
│   │           └── growth-plan.st
│   └── test/
├── pom.xml
└── Dockerfile
```

### 7.2 核心类设计

#### Result 统一响应类
```java
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) { ... }
    public static <T> Result<T> error(String message) { ... }
}
```

#### AI 服务接口
```java
public interface AiService {
    ResumeAnalysis analyzeResume(String resumeText);
    List<JobMatchVO> matchJobs(String resumeId);
    ResumeOptimization optimizeResume(String resumeId, Long jobId);
    InterviewQuestion generateQuestion(Long jobId, String type);
    InterviewEvaluation evaluateAnswer(String question, String answer);
    GrowthPlan generateGrowthPlan(String resumeId);
}
```

---

## 8. AI 集成设计

### 8.1 Spring AI 集成

```yaml
# application.yml
spring:
  ai:
    openai:
      api-key: ${DEEPSEEK_API_KEY}
      base-url: https://api.deepseek.com
      chat:
        options:
          model: deepseek-chat
          temperature: 0.7
```

### 8.2 Prompt 模板设计

#### 简历分析提示词
```
你是一个专业的 HR 顾问。请分析以下简历内容，生成求职画像。

简历内容：
{resumeText}

请按以下 JSON 格式输出：
{
  "jobDirection": "求职方向",
  "skillTags": ["技能1", "技能2"],
  "abilityScores": {
    "development": 0-100,
    "project": 0-100,
    "learning": 0-100,
    "communication": 0-100,
    "practice": 0-100
  },
  "summary": "综合评价"
}
```

---

## 9. 部署架构

### 9.1 Docker Compose 部署

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_PASSWORD}
      MYSQL_DATABASE: offer_hunter
    volumes:
      - mysql_data:/var/lib/mysql

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    depends_on:
      - mysql
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/offer_hunter
      DEEPSEEK_API_KEY: ${DEEPSEEK_API_KEY}

  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  mysql_data:
```

### 9.2 部署方案

| 组件 | 部署位置 | 说明 |
|------|---------|------|
| 前端 | Vercel | 静态资源托管，CDN 加速 |
| 后端 | 腾讯云/阿里云 | Docker 容器部署 |
| 数据库 | 云数据库 MySQL | 托管服务，自动备份 |

---

## 10. 开发计划

### 第一阶段：基础架构搭建（1-2天）
- [x] 项目架构设计
- [ ] 数据库设计
- [ ] 前后端目录结构
- [ ] 基础配置

### 第二阶段：核心功能开发（3-5天）
- [ ] 简历上传与解析
- [ ] AI 简历分析
- [ ] 岗位匹配引擎
- [ ] 简历优化功能

### 第三阶段：高级功能开发（2-3天）
- [ ] 模拟面试功能
- [ ] 成长规划功能
- [ ] 前端页面完善

### 第四阶段：测试与部署（1-2天）
- [ ] 功能测试
- [ ] Docker 部署
- [ ] 公网部署
- [ ] 演示脚本准备

---

## 11. 项目亮点总结

1. **AI 简历解析**：智能提取简历关键信息，生成结构化数据
2. **AI 岗位匹配**：基于技能和项目经验的智能匹配算法
3. **AI 简历优化**：针对目标岗位的个性化优化建议
4. **AI 模拟面试**：智能面试题生成和回答评估
5. **AI 成长规划**：个性化学习路径规划

---

## 12. 风险与应对

| 风险 | 应对措施 |
|------|---------|
| AI API 调用失败 | 实现重试机制，准备降级方案 |
| 简历解析不准确 | 多种解析策略，人工校验入口 |
| 匹配算法不精准 | 持续优化算法，收集用户反馈 |
| 性能问题 | 缓存优化，异步处理 |

---

*文档版本：v1.0*
*最后更新：2026-06-09*
