-- Offer Hunter AI 数据库初始化脚本

CREATE DATABASE IF NOT EXISTS offer_hunter DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE offer_hunter;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码(加密)',
  `email` VARCHAR(100) COMMENT '邮箱',
  `phone` VARCHAR(20) COMMENT '手机号',
  `avatar` VARCHAR(255) COMMENT '头像URL',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_username` (`username`),
  INDEX `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 简历表
CREATE TABLE IF NOT EXISTS `resume` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '简历ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
  `file_path` VARCHAR(500) NOT NULL COMMENT '文件存储路径',
  `raw_text` LONGTEXT COMMENT '简历原始文本',
  `parsed_content` JSON COMMENT '解析后的结构化内容',
  `status` TINYINT DEFAULT 0 COMMENT '状态: 0-待解析, 1-已解析, 2-解析失败',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简历表';

-- 简历分析表
CREATE TABLE IF NOT EXISTS `resume_analysis` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分析ID',
  `resume_id` BIGINT NOT NULL COMMENT '简历ID',
  `job_direction` VARCHAR(100) COMMENT '求职方向',
  `skill_tags` JSON COMMENT '技能标签',
  `ability_scores` JSON COMMENT '能力评分',
  `summary` TEXT COMMENT '综合评价',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`) ON DELETE CASCADE,
  INDEX `idx_resume_id` (`resume_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简历分析表';

-- 岗位表
CREATE TABLE IF NOT EXISTS `job` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '岗位ID',
  `company` VARCHAR(100) NOT NULL COMMENT '公司名称',
  `position` VARCHAR(100) NOT NULL COMMENT '职位名称',
  `description` TEXT COMMENT '职位描述',
  `requirements` TEXT COMMENT '任职要求',
  `salary_range` VARCHAR(50) COMMENT '薪资范围',
  `location` VARCHAR(100) COMMENT '工作地点',
  `job_type` VARCHAR(20) COMMENT '岗位类型: full-time/part-time/intern',
  `company_logo` VARCHAR(255) COMMENT '公司Logo',
  `is_hot` TINYINT DEFAULT 0 COMMENT '是否热门: 0-否, 1-是',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 0-下架, 1-上架',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_company` (`company`),
  INDEX `idx_position` (`position`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位表';

-- 岗位技能表
CREATE TABLE IF NOT EXISTS `job_skill` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
  `job_id` BIGINT NOT NULL COMMENT '岗位ID',
  `skill_name` VARCHAR(50) NOT NULL COMMENT '技能名称',
  `skill_level` VARCHAR(20) COMMENT '技能要求: proficient/familiar/plus',
  `is_required` TINYINT DEFAULT 1 COMMENT '是否必须: 0-加分项, 1-必须项',
  FOREIGN KEY (`job_id`) REFERENCES `job`(`id`) ON DELETE CASCADE,
  INDEX `idx_job_id` (`job_id`),
  INDEX `idx_skill_name` (`skill_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位技能表';

-- 匹配结果表
CREATE TABLE IF NOT EXISTS `match_result` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '匹配ID',
  `resume_id` BIGINT NOT NULL COMMENT '简历ID',
  `job_id` BIGINT NOT NULL COMMENT '岗位ID',
  `match_score` DECIMAL(5,2) COMMENT '匹配分数(0-100)',
  `skill_score` DECIMAL(5,2) COMMENT '技能匹配分',
  `project_score` DECIMAL(5,2) COMMENT '项目匹配分',
  `matched_skills` JSON COMMENT '匹配的技能',
  `missing_skills` JSON COMMENT '缺失的技能',
  `match_reason` TEXT COMMENT '匹配原因',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`job_id`) REFERENCES `job`(`id`) ON DELETE CASCADE,
  INDEX `idx_resume_id` (`resume_id`),
  INDEX `idx_job_id` (`job_id`),
  INDEX `idx_match_score` (`match_score` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='匹配结果表';

-- 面试会话表
CREATE TABLE IF NOT EXISTS `interview_session` (
  `id` VARCHAR(50) PRIMARY KEY COMMENT '会话ID',
  `resume_id` BIGINT NOT NULL COMMENT '简历ID',
  `job_id` BIGINT NOT NULL COMMENT '岗位ID',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 0-已结束, 1-进行中',
  `total_score` INT COMMENT '总分',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`job_id`) REFERENCES `job`(`id`) ON DELETE CASCADE,
  INDEX `idx_resume_id` (`resume_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='面试会话表';

-- 面试问题表
CREATE TABLE IF NOT EXISTS `interview_question` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '问题ID',
  `session_id` VARCHAR(50) NOT NULL COMMENT '会话ID',
  `question_type` VARCHAR(20) NOT NULL COMMENT '问题类型: technical/project/hr/behavioral',
  `question_content` TEXT NOT NULL COMMENT '问题内容',
  `answer_content` TEXT COMMENT '用户回答',
  `ai_comment` TEXT COMMENT 'AI点评',
  `ai_score` INT COMMENT 'AI评分(0-100)',
  `improvement_suggestion` TEXT COMMENT '改进建议',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (`session_id`) REFERENCES `interview_session`(`id`) ON DELETE CASCADE,
  INDEX `idx_session_id` (`session_id`),
  INDEX `idx_question_type` (`question_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='面试问题表';

-- 成长计划表
CREATE TABLE IF NOT EXISTS `growth_plan` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '计划ID',
  `resume_id` BIGINT NOT NULL COMMENT '简历ID',
  `phase` INT NOT NULL COMMENT '阶段: 1-30天, 2-60天, 3-90天',
  `duration` VARCHAR(20) COMMENT '时长描述',
  `title` VARCHAR(100) COMMENT '阶段标题',
  `goals` JSON COMMENT '阶段目标',
  `tasks` JSON COMMENT '具体任务',
  `resources` JSON COMMENT '学习资源',
  `status` TINYINT DEFAULT 0 COMMENT '状态: 0-未开始, 1-进行中, 2-已完成',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`) ON DELETE CASCADE,
  INDEX `idx_resume_id` (`resume_id`),
  INDEX `idx_phase` (`phase`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成长计划表';

-- 简历优化记录表
CREATE TABLE IF NOT EXISTS `resume_optimization` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '优化ID',
  `resume_id` BIGINT NOT NULL COMMENT '简历ID',
  `job_id` BIGINT NOT NULL COMMENT '目标岗位ID',
  `current_issues` JSON COMMENT '当前问题',
  `suggestions` JSON COMMENT '优化建议',
  `optimized_content` JSON COMMENT '优化后内容',
  `star_version` TEXT COMMENT 'STAR法则改写版本',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`job_id`) REFERENCES `job`(`id`) ON DELETE CASCADE,
  INDEX `idx_resume_id` (`resume_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简历优化记录表';
