SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `blog_id` varchar(10) NOT NULL COMMENT '博客ID',
  `p_blog_id` varchar(10) DEFAULT NULL COMMENT '父ID',
  `title` varchar(200) DEFAULT NULL COMMENT '标题',
  `category_id` int(11) DEFAULT NULL COMMENT '分类ID',
  `category_name` varchar(100) DEFAULT NULL COMMENT '分类名称',
  `cover` varchar(100) DEFAULT NULL COMMENT '封面',
  `summary` varchar(300) DEFAULT NULL COMMENT '摘要',
  `content` mediumtext COMMENT '内容',
  `markdown_content` text COMMENT 'markdown编辑内容',
  `editor_type` tinyint(4) DEFAULT NULL COMMENT '0:富文本 1:markdown编辑器',
  `tag` varchar(200) DEFAULT NULL COMMENT '标签',
  `type` tinyint(4) DEFAULT NULL COMMENT '0:原创 1:转载',
  `reprint_url` varchar(200) DEFAULT NULL COMMENT '转载地址',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `nick_name` varchar(20) DEFAULT NULL COMMENT '昵称',
  `allow_comment` tinyint(4) DEFAULT NULL COMMENT '0:不允许评论 1:允许评论',
  `status` tinyint(4) DEFAULT '1' COMMENT '0:草稿 1:已发布',
  `del_type` tinyint(4) DEFAULT NULL COMMENT '0:删除 1:正常',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `blog_type` tinyint(4) DEFAULT NULL COMMENT '0:博客  1:专题',
  `sort` tinyint(4) DEFAULT NULL COMMENT '排序号',
  PRIMARY KEY (`blog_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客';

-- ----------------------------
-- Table structure for blog_category
-- ----------------------------
DROP TABLE IF EXISTS `blog_category`;
CREATE TABLE `blog_category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `cover` varchar(100) DEFAULT NULL COMMENT '封面',
  `category_name` varchar(100) DEFAULT NULL COMMENT '分类名称',
  `category_desc` varchar(200) DEFAULT NULL COMMENT '分类描述',
  `category_type` tinyint(4) DEFAULT NULL COMMENT '0:博客分类  1:专题',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `nick_name` varchar(20) DEFAULT NULL COMMENT '昵称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10016 DEFAULT CHARSET=utf8mb4 COMMENT='博客分类';

-- ----------------------------
-- Table structure for blog_team_user
-- ----------------------------
DROP TABLE IF EXISTS `blog_team_user`;
CREATE TABLE `blog_team_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `nick_name` varchar(30) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(100) DEFAULT NULL COMMENT '头像',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `password` varchar(32) DEFAULT NULL COMMENT '密码',
  `profession` varchar(20) DEFAULT NULL COMMENT '职业',
  `introduction` varchar(300) DEFAULT NULL COMMENT '简介',
  `editor_type` tinyint(4) DEFAULT NULL COMMENT '0:富文本 1:markdown编辑器',
  `role_type` tinyint(4) DEFAULT NULL COMMENT '0:普通用户 1:超级管理员',
  `status` tinyint(4) DEFAULT NULL COMMENT '0:禁用 1:启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `idx_key` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=100001 DEFAULT CHARSET=utf8mb4 COMMENT='博客成员';

