#!/bin/bash

# Offer Hunter AI 项目验证脚本

echo "=========================================="
echo "  Offer Hunter AI - 项目验证"
echo "=========================================="
echo ""

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# 验证结果
PASS=0
FAIL=0

# 检查函数
check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✅ $2${NC}"
        ((PASS++))
    else
        echo -e "${RED}❌ $2${NC}"
        ((FAIL++))
    fi
}

check_dir() {
    if [ -d "$1" ]; then
        echo -e "${GREEN}✅ $2${NC}"
        ((PASS++))
    else
        echo -e "${RED}❌ $2${NC}"
        ((FAIL++))
    fi
}

echo "📋 检查项目文件..."
echo ""

check_file "docker-compose.yml" "docker-compose.yml"
check_file "backend/Dockerfile" "backend/Dockerfile"
check_file "frontend/Dockerfile" "frontend/Dockerfile"
check_file "frontend/nginx.conf" "frontend/nginx.conf"
check_file ".env.example" ".env.example"
check_file ".gitignore" ".gitignore"

echo ""
echo "📋 检查数据库脚本..."
echo ""

check_file "database/schema.sql" "database/schema.sql"
check_file "database/init-data.sql" "database/init-data.sql"
check_file "database/jobs_seed.sql" "database/jobs_seed.sql"

echo ""
echo "📋 检查文档文件..."
echo ""

check_file "README.md" "README.md"
check_file "DELIVERY_REPORT.md" "DELIVERY_REPORT.md"
check_file "MANUAL_ACTION_REQUIRED.md" "MANUAL_ACTION_REQUIRED.md"
check_file "PROJECT_AUDIT.md" "PROJECT_AUDIT.md"

echo ""
echo "📋 检查启动脚本..."
echo ""

check_file "start.sh" "start.sh"
check_file "start.bat" "start.bat"

echo ""
echo "📋 检查后端构建..."
echo ""

check_file "backend/target/offer-hunter-ai-1.0.0.jar" "后端JAR文件"

echo ""
echo "📋 检查前端构建..."
echo ""

check_dir "frontend/dist" "前端构建产物"

echo ""
echo "=========================================="
echo "  验证结果"
echo "=========================================="
echo ""
echo -e "通过: ${GREEN}${PASS}${NC}"
echo -e "失败: ${RED}${FAIL}${NC}"
echo ""

if [ $FAIL -eq 0 ]; then
    echo -e "${GREEN}✅ 项目验证通过！${NC}"
    echo ""
    echo "🚀 启动方式："
    echo "   1. 配置 .env 文件"
    echo "   2. 运行 docker-compose up -d"
    echo "   3. 访问 http://localhost"
    echo ""
    exit 0
else
    echo -e "${RED}❌ 项目验证失败，请检查上述问题${NC}"
    echo ""
    exit 1
fi
