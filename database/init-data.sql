USE offer_hunter;

-- 插入测试用户 (密码: 123456, BCrypt加密)
INSERT INTO `user` (`username`, `password`, `email`) VALUES
('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'test@example.com');

-- 插入岗位数据 (初始12条)
INSERT INTO `job` (`company`, `position`, `description`, `requirements`, `salary_range`, `location`, `job_type`, `is_hot`) VALUES
('腾讯', 'Java开发实习生', '参与腾讯核心业务系统的开发和维护，使用Java技术栈', '1. 熟悉Java基础，了解JVM原理\n2. 熟悉SpringBoot框架\n3. 熟悉MySQL数据库\n4. 了解Redis缓存\n5. 了解Docker容器技术', '200-300/天', '深圳', 'intern', 1),
('腾讯', '前端开发实习生', '参与腾讯Web前端项目开发', '1. 熟悉Vue3/React框架\n2. 熟悉TypeScript\n3. 了解Webpack/Vite构建工具\n4. 了解前端性能优化', '200-300/天', '深圳', 'intern', 1),
('阿里', 'Java开发实习生', '参与阿里巴巴电商平台后端开发', '1. 扎实的Java基础\n2. 熟悉Spring全家桶\n3. 熟悉MySQL、Redis\n4. 了解分布式系统\n5. 了解消息队列', '250-350/天', '杭州', 'intern', 1),
('字节跳动', 'AI应用开发实习生', '参与AI应用产品开发，集成大模型能力', '1. 熟悉Python/Java\n2. 了解LLM大模型\n3. 了解Prompt Engineering\n4. 了解RAG技术\n5. 有AI项目经验优先', '300-400/天', '北京', 'intern', 1),
('美团', '后端开发实习生', '参与美团核心业务系统开发', '1. 熟悉Java语言\n2. 熟悉SpringBoot\n3. 熟悉MySQL\n4. 了解微服务架构\n5. 了解Redis', '200-300/天', '北京', 'intern', 1),
('京东', 'Java开发实习生', '参与京东电商平台开发', '1. Java基础扎实\n2. 熟悉Spring框架\n3. 熟悉MySQL数据库\n4. 了解Linux操作系统\n5. 了解Git版本控制', '180-280/天', '北京', 'intern', 1),
('百度', 'AI研发实习生', '参与百度AI平台研发工作', '1. 熟悉Python/C++\n2. 了解机器学习基础\n3. 了解深度学习框架\n4. 有NLP/CV项目经验优先', '250-350/天', '北京', 'intern', 1),
('腾讯', '产品经理实习生', '参与腾讯产品策划和运营工作', '1. 有产品思维\n2. 熟悉Axure等原型工具\n3. 有数据分析能力\n4. 良好的沟通能力\n5. 有产品经验优先', '200-300/天', '深圳', 'intern', 0),
('阿里', '测试工程师实习生', '参与阿里巴巴质量保障工作', '1. 了解测试理论\n2. 熟悉Python/Java\n3. 了解自动化测试\n4. 了解性能测试\n5. 细心认真', '200-300/天', '杭州', 'intern', 0),
('字节跳动', '数据分析师实习生', '参与数据分析和挖掘工作', '1. 熟悉SQL\n2. 熟悉Python数据分析\n3. 了解数据可视化\n4. 有统计学基础\n5. 有数据分析项目经验', '250-350/天', '北京', 'intern', 0),
('腾讯', '全栈开发实习生', '参与全栈项目开发', '1. 熟悉Java/Python\n2. 熟悉Vue/React\n3. 熟悉MySQL\n4. 了解Docker\n5. 有全栈项目经验优先', '220-320/天', '深圳', 'intern', 1),
('美团', '算法工程师实习生', '参与推荐/搜索算法研发', '1. 熟悉Python\n2. 了解机器学习算法\n3. 了解深度学习\n4. 有算法竞赛经验优先\n5. 有推荐系统经验优先', '300-400/天', '北京', 'intern', 0);

-- 插入岗位技能数据
INSERT INTO `job_skill` (`job_id`, `skill_name`, `skill_level`, `is_required`) VALUES
-- 腾讯Java开发
(1, 'Java', 'proficient', 1),
(1, 'SpringBoot', 'proficient', 1),
(1, 'MySQL', 'familiar', 1),
(1, 'Redis', 'familiar', 1),
(1, 'Docker', 'familiar', 0),
(1, 'Git', 'familiar', 1),
-- 腾讯前端开发
(2, 'Vue3', 'proficient', 1),
(2, 'TypeScript', 'proficient', 1),
(2, 'HTML/CSS', 'proficient', 1),
(2, 'Webpack', 'familiar', 0),
(2, 'Git', 'familiar', 1),
-- 阿里Java开发
(3, 'Java', 'proficient', 1),
(3, 'Spring', 'proficient', 1),
(3, 'MySQL', 'proficient', 1),
(3, 'Redis', 'familiar', 1),
(3, '分布式系统', 'familiar', 0),
(3, '消息队列', 'familiar', 0),
-- 字节AI开发
(4, 'Python', 'proficient', 1),
(4, 'LLM', 'familiar', 1),
(4, 'Prompt Engineering', 'familiar', 1),
(4, 'RAG', 'familiar', 0),
(4, 'Java', 'familiar', 0),
-- 美团后端
(5, 'Java', 'proficient', 1),
(5, 'SpringBoot', 'proficient', 1),
(5, 'MySQL', 'familiar', 1),
(5, 'Redis', 'familiar', 1),
(5, '微服务', 'familiar', 0),
-- 京东Java
(6, 'Java', 'proficient', 1),
(6, 'Spring', 'proficient', 1),
(6, 'MySQL', 'familiar', 1),
(6, 'Linux', 'familiar', 0),
(6, 'Git', 'familiar', 1),
-- 百度AI
(7, 'Python', 'proficient', 1),
(7, '机器学习', 'familiar', 1),
(7, '深度学习', 'familiar', 1),
(7, 'C++', 'familiar', 0),
-- 腾讯产品
(8, '产品设计', 'proficient', 1),
(8, 'Axure', 'familiar', 1),
(8, '数据分析', 'familiar', 1),
-- 阿里测试
(9, 'Python', 'familiar', 1),
(9, '测试理论', 'familiar', 1),
(9, '自动化测试', 'familiar', 0),
-- 字节数据
(10, 'SQL', 'proficient', 1),
(10, 'Python', 'proficient', 1),
(10, '数据可视化', 'familiar', 0),
-- 腾讯全栈
(11, 'Java', 'proficient', 1),
(11, 'Vue', 'familiar', 1),
(11, 'MySQL', 'familiar', 1),
(11, 'Docker', 'familiar', 0),
(11, 'Python', 'familiar', 0),
-- 美团算法
(12, 'Python', 'proficient', 1),
(12, '机器学习', 'proficient', 1),
(12, '深度学习', 'familiar', 1),
(12, '推荐系统', 'familiar', 0);
