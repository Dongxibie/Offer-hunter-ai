USE offer_hunter;

-- 清空现有岗位数据（可选）
-- TRUNCATE TABLE job_skill;
-- TRUNCATE TABLE job;

-- 插入20条岗位数据
INSERT INTO `job` (`company`, `position`, `description`, `requirements`, `salary_range`, `location`, `job_type`, `is_hot`) VALUES
-- Java开发类
('腾讯', 'Java开发实习生', '参与腾讯核心业务系统的开发和维护', '1. 熟悉Java基础，了解JVM原理\n2. 熟悉SpringBoot框架\n3. 熟悉MySQL数据库\n4. 了解Redis缓存\n5. 了解Docker容器技术', '200-300/天', '深圳', 'intern', 1),
('阿里巴巴', 'Java后端开发', '参与电商平台后端系统开发', '1. 扎实的Java基础\n2. 熟悉Spring全家桶\n3. 熟悉MySQL、Redis\n4. 了解分布式系统\n5. 了解消息队列', '250-350/天', '杭州', 'intern', 1),
('美团', '后端开发工程师', '参与美团核心业务系统开发', '1. 熟悉Java语言\n2. 熟悉SpringBoot\n3. 熟悉MySQL\n4. 了解微服务架构\n5. 了解Redis', '200-300/天', '北京', 'intern', 1),
('京东', 'Java开发实习生', '参与京东电商平台开发', '1. Java基础扎实\n2. 熟悉Spring框架\n3. 熟悉MySQL数据库\n4. 了解Linux操作系统\n5. 了解Git版本控制', '180-280/天', '北京', 'intern', 0),

-- 前端开发类
('腾讯', '前端开发实习生', '参与腾讯Web前端项目开发', '1. 熟悉Vue3/React框架\n2. 熟悉TypeScript\n3. 了解Webpack/Vite构建工具\n4. 了解前端性能优化', '200-300/天', '深圳', 'intern', 1),
('字节跳动', '前端开发工程师', '参与抖音Web前端开发', '1. 熟悉Vue3或React\n2. 熟悉TypeScript\n3. 了解Node.js\n4. 有移动端H5开发经验优先', '220-320/天', '北京', 'intern', 1),
('网易', '前端开发实习生', '参与网易云音乐前端开发', '1. 熟悉HTML/CSS/JavaScript\n2. 熟悉Vue或React\n3. 了解响应式布局\n4. 有音乐播放器开发经验优先', '180-260/天', '杭州', 'intern', 0),

-- AI应用开发类
('字节跳动', 'AI应用开发实习生', '参与AI应用产品开发，集成大模型能力', '1. 熟悉Python/Java\n2. 了解LLM大模型\n3. 了解Prompt Engineering\n4. 了解RAG技术\n5. 有AI项目经验优先', '300-400/天', '北京', 'intern', 1),
('百度', 'AI研发实习生', '参与百度AI平台研发工作', '1. 熟悉Python/C++\n2. 了解机器学习基础\n3. 了解深度学习框架\n4. 有NLP/CV项目经验优先', '250-350/天', '北京', 'intern', 1),
('商汤科技', 'AI算法工程师', '参与计算机视觉算法研发', '1. 熟悉Python\n2. 熟悉PyTorch/TensorFlow\n3. 了解CNN、Transformer等模型\n4. 有CV项目经验优先', '280-380/天', '上海', 'intern', 1),

-- 产品经理类
('腾讯', '产品经理实习生', '参与腾讯产品策划和运营工作', '1. 有产品思维\n2. 熟悉Axure等原型工具\n3. 有数据分析能力\n4. 良好的沟通能力\n5. 有产品经验优先', '200-300/天', '深圳', 'intern', 0),
('字节跳动', '产品经理实习生', '参与抖音产品策划工作', '1. 有产品设计思维\n2. 熟悉Figma等设计工具\n3. 有用户研究能力\n4. 有短视频产品经验优先', '220-300/天', '北京', 'intern', 0),

-- 测试工程师类
('阿里巴巴', '测试工程师实习生', '参与阿里巴巴质量保障工作', '1. 了解测试理论\n2. 熟悉Python/Java\n3. 了解自动化测试\n4. 了解性能测试\n5. 细心认真', '200-300/天', '杭州', 'intern', 0),
('华为', '测试开发工程师', '参与华为产品质量保障', '1. 熟悉Python/Java\n2. 了解Selenium等自动化工具\n3. 了解CI/CD流程\n4. 有测试框架开发经验优先', '200-280/天', '深圳', 'intern', 0),

-- 数据分析类
('字节跳动', '数据分析师实习生', '参与数据分析和挖掘工作', '1. 熟悉SQL\n2. 熟悉Python数据分析\n3. 了解数据可视化\n4. 有统计学基础\n5. 有数据分析项目经验', '250-350/天', '北京', 'intern', 0),
('滴滴', '数据分析师', '参与出行数据分析工作', '1. 熟悉SQL和Python\n2. 了解Tableau等可视化工具\n3. 有数据建模能力\n4. 有出行领域经验优先', '220-300/天', '北京', 'intern', 0),

-- 全栈开发类
('腾讯', '全栈开发实习生', '参与全栈项目开发', '1. 熟悉Java/Python\n2. 熟悉Vue/React\n3. 熟悉MySQL\n4. 了解Docker\n5. 有全栈项目经验优先', '220-320/天', '深圳', 'intern', 1),
('小米', '全栈开发工程师', '参与小米IoT平台开发', '1. 熟悉Java/Go\n2. 熟悉Vue3/React\n3. 了解物联网协议\n4. 有嵌入式开发经验优先', '200-300/天', '北京', 'intern', 0),

-- 算法工程师类
('美团', '算法工程师实习生', '参与推荐/搜索算法研发', '1. 熟悉Python\n2. 了解机器学习算法\n3. 了解深度学习\n4. 有算法竞赛经验优先\n5. 有推荐系统经验优先', '300-400/天', '北京', 'intern', 0),
('快手', '算法工程师', '参与短视频推荐算法研发', '1. 熟悉Python/C++\n2. 了解推荐系统架构\n3. 了解深度学习模型\n4. 有大规模数据处理经验优先', '280-380/天', '北京', 'intern', 0);

-- 插入岗位技能数据
INSERT INTO `job_skill` (`job_id`, `skill_name`, `skill_level`, `is_required`) VALUES
-- 腾讯Java开发 (job_id=1)
(1, 'Java', 'proficient', 1),
(1, 'SpringBoot', 'proficient', 1),
(1, 'MySQL', 'familiar', 1),
(1, 'Redis', 'familiar', 1),
(1, 'Docker', 'familiar', 0),
(1, 'Git', 'familiar', 1),

-- 阿里Java后端 (job_id=2)
(2, 'Java', 'proficient', 1),
(2, 'Spring', 'proficient', 1),
(2, 'MySQL', 'proficient', 1),
(2, 'Redis', 'familiar', 1),
(2, '分布式系统', 'familiar', 0),
(2, '消息队列', 'familiar', 0),

-- 美团后端 (job_id=3)
(3, 'Java', 'proficient', 1),
(3, 'SpringBoot', 'proficient', 1),
(3, 'MySQL', 'familiar', 1),
(3, 'Redis', 'familiar', 1),
(3, '微服务', 'familiar', 0),

-- 京东Java (job_id=4)
(4, 'Java', 'proficient', 1),
(4, 'Spring', 'proficient', 1),
(4, 'MySQL', 'familiar', 1),
(4, 'Linux', 'familiar', 0),
(4, 'Git', 'familiar', 1),

-- 腾讯前端 (job_id=5)
(5, 'Vue3', 'proficient', 1),
(5, 'TypeScript', 'proficient', 1),
(5, 'HTML/CSS', 'proficient', 1),
(5, 'Webpack', 'familiar', 0),
(5, 'Git', 'familiar', 1),

-- 字节前端 (job_id=6)
(6, 'Vue3', 'proficient', 1),
(6, 'TypeScript', 'proficient', 1),
(6, 'Node.js', 'familiar', 0),
(6, '移动端H5', 'familiar', 0),

-- 网易前端 (job_id=7)
(7, 'HTML/CSS', 'proficient', 1),
(7, 'JavaScript', 'proficient', 1),
(7, 'Vue', 'familiar', 1),
(7, 'React', 'familiar', 0),

-- 字节AI开发 (job_id=8)
(8, 'Python', 'proficient', 1),
(8, 'LLM', 'familiar', 1),
(8, 'Prompt Engineering', 'familiar', 1),
(8, 'RAG', 'familiar', 0),
(8, 'Java', 'familiar', 0),

-- 百度AI (job_id=9)
(9, 'Python', 'proficient', 1),
(9, '机器学习', 'familiar', 1),
(9, '深度学习', 'familiar', 1),
(9, 'C++', 'familiar', 0),

-- 商汤AI (job_id=10)
(10, 'Python', 'proficient', 1),
(10, 'PyTorch', 'proficient', 1),
(10, '计算机视觉', 'familiar', 1),
(10, '深度学习', 'familiar', 1),

-- 腾讯产品 (job_id=11)
(11, '产品设计', 'proficient', 1),
(11, 'Axure', 'familiar', 1),
(11, '数据分析', 'familiar', 1),

-- 字节产品 (job_id=12)
(12, '产品设计', 'proficient', 1),
(12, 'Figma', 'familiar', 1),
(12, '用户研究', 'familiar', 0),

-- 阿里测试 (job_id=13)
(13, 'Python', 'familiar', 1),
(13, '测试理论', 'familiar', 1),
(13, '自动化测试', 'familiar', 0),

-- 华为测试 (job_id=14)
(14, 'Python', 'proficient', 1),
(14, 'Selenium', 'familiar', 1),
(14, 'CI/CD', 'familiar', 0),

-- 字节数据 (job_id=15)
(15, 'SQL', 'proficient', 1),
(15, 'Python', 'proficient', 1),
(15, '数据可视化', 'familiar', 0),

-- 滴滴数据 (job_id=16)
(16, 'SQL', 'proficient', 1),
(16, 'Python', 'proficient', 1),
(16, 'Tableau', 'familiar', 0),

-- 腾讯全栈 (job_id=17)
(17, 'Java', 'proficient', 1),
(17, 'Vue', 'familiar', 1),
(17, 'MySQL', 'familiar', 1),
(17, 'Docker', 'familiar', 0),

-- 小米全栈 (job_id=18)
(18, 'Java', 'proficient', 1),
(18, 'Vue3', 'familiar', 1),
(18, '物联网', 'familiar', 0),

-- 美团算法 (job_id=19)
(19, 'Python', 'proficient', 1),
(19, '机器学习', 'proficient', 1),
(19, '深度学习', 'familiar', 1),
(19, '推荐系统', 'familiar', 0),

-- 快手算法 (job_id=20)
(20, 'Python', 'proficient', 1),
(20, 'C++', 'familiar', 0),
(20, '推荐系统', 'familiar', 1),
(20, '深度学习', 'familiar', 1);
