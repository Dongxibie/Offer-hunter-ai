# Offer Hunter AI — 部署任务 Prompt（投喂给 Agent 用）

> 用法：把下面代码块内的整段文字复制给目标 Agent。它已自包含项目背景、环境约定与可执行步骤。
> 投喂前请把 `<...>` 占位符替换成你的真实信息（服务器 IP、DeepSeek Key、域名等）。

```
你是一名 DevOps 工程师 Agent。任务：将 "Offer Hunter AI" 项目部署到一台阿里云 Linux 云服务器（ECS）并验证可用，使其成为公网所有人可访问的程序。

【项目背景】
- Offer Hunter AI 是 AI 求职助手 Web 应用。技术栈：前端 Vue3+Vite+Element Plus（Nginx 托管，反代 /api 到后端）；后端 Spring Boot 3.2.5 + Java 21 + MyBatis-Plus + Spring AI；数据库 MySQL 8.0；AI 用 DeepSeek（OpenAI 兼容接口，需 API Key）。
- 已提供 docker-compose.yml，定义三个服务：mysql、backend、frontend。MySQL 首次启动自动执行 database/ 下的 schema.sql 和 init-data.sql 建表灌数据。
- 暴露端口：frontend=80（对外），backend=8080，mysql=3306（后两者不对公网开放）。
- 预置测试账号：testuser / 123456。预置 12 条名企实习岗位数据。

【目标服务器】
- 阿里云 ECS，Ubuntu 22.04，2 核 4G 起，已有 sudo 权限，公网 IP：<服务器IP>。
- 阿里云安全组需放行入方向端口：22（SSH）、80（HTTP）、可选 443（HTTPS）。3306/8080 不放行公网。
- 若无 Docker 则先安装 Docker + Compose v2。

【必须完成的步骤】
1. 安装 Docker 与 docker compose（若已装则跳过）。国内服务器建议配置阿里云镜像加速器。
2. 获取项目代码到 /opt/offer-hunter-ai（通过 scp 上传压缩包或 git clone）。
3. 编辑项目根目录 .env，设置以下三项为真实/强值（不得保留占位符）：
   - DEEPSEEK_API_KEY = <真实 DeepSeek key>
   - MYSQL_PASSWORD   = <生成的强密码>
   - JWT_SECRET       = <生成的长随机串>
4. 执行 `docker compose up -d --build`（首次构建较慢，需联网拉取 Maven 里程碑仓库 repo.spring.io/milestone 与 npm 依赖；耐心等待）。
5. 验证部署：
   - `docker compose ps` 三容器均 running/healthy；
   - `curl http://localhost/api/job/list` 返回 12 条岗位 JSON；
   - 浏览器访问 http://<服务器IP>，用 testuser / 123456 能登录并跑通：上传简历→AI分析→岗位匹配→模拟面试→成长规划。
6. （若提供了域名 <域名>）用 Caddy 反代 localhost:80 自动配置 HTTPS。

【约束与注意】
- 不要把真实密钥写入会提交到 git 远程的文件；仅写入服务器本地 .env。
- 不要对公网开放 3306 和 8080。
- 内存不足导致构建被 OOM kill 时，先加 4G swap 再重试：
  `fallocate -l 4G /swapfile && chmod 600 /swapfile && mkswap /swapfile && swapon /swapfile`
- 构建失败优先排查：网络能否访问 repo.spring.io/milestone 和 api.deepseek.com。
- 阿里云需在「安全组」里手动放行端口，否则即使容器起来了公网也访问不到。

【完成后请汇报】
- 各容器状态、对外访问 URL、是否已配置 HTTPS、testuser 登录验证结果、遇到的问题与处理方式。
- 如有失败步骤，附上 `docker compose logs` 中的关键报错。
```
