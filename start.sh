#!/bin/bash

# Offer Hunter AI 启动脚本

echo "=========================================="
echo "  Offer Hunter AI - AI求职助手"
echo "=========================================="
echo ""

# 检查 Docker 是否安装
if ! command -v docker &> /dev/null; then
    echo "❌ 错误：未安装 Docker"
    echo "请先安装 Docker：https://docs.docker.com/get-docker/"
    exit 1
fi

# 检查 Docker Compose 是否安装
if ! command -v docker-compose &> /dev/null; then
    echo "❌ 错误：未安装 Docker Compose"
    echo "请先安装 Docker Compose：https://docs.docker.com/compose/install/"
    exit 1
fi

# 检查 .env 文件是否存在
if [ ! -f .env ]; then
    echo "⚠️  警告：.env 文件不存在"
    echo "正在从 .env.example 复制..."
    cp .env.example .env
    echo ""
    echo "请编辑 .env 文件，填入你的 DeepSeek API Key："
    echo "  DEEPSEEK_API_KEY=your-api-key-here"
    echo ""
    read -p "按 Enter 键继续（已配置好 API Key）..."
fi

# 检查 API Key 是否配置
source .env
if [ "$DEEPSEEK_API_KEY" = "your-deepseek-api-key-here" ] || [ -z "$DEEPSEEK_API_KEY" ]; then
    echo "❌ 错误：未配置 DeepSeek API Key"
    echo ""
    echo "请编辑 .env 文件，填入有效的 API Key："
    echo "  DEEPSEEK_API_KEY=sk-xxxxxxxxxxxxxxxx"
    echo ""
    echo "获取 API Key：https://platform.deepseek.com/"
    exit 1
fi

echo "✅ 环境检查通过"
echo ""

# 启动服务
echo "🚀 正在启动服务..."
echo ""

docker-compose up -d

echo ""
echo "=========================================="
echo "  服务启动完成！"
echo "=========================================="
echo ""
echo "📱 访问地址："
echo "   前端：http://localhost"
echo "   后端：http://localhost:8080"
echo ""
echo "👤 测试账号："
echo "   用户名：testuser"
echo "   密码：123456"
echo ""
echo "📋 常用命令："
echo "   查看日志：docker-compose logs -f"
echo "   停止服务：docker-compose down"
echo "   重启服务：docker-compose restart"
echo ""
