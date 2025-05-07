-- 创建第三方认证表
CREATE TABLE IF NOT EXISTS `app_user_third_auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `oauth_type` varchar(20) NOT NULL COMMENT '第三方平台类型(google,wechat,github)',
  `oauth_id` varchar(100) NOT NULL COMMENT '第三方平台用户ID',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `access_token` text DEFAULT NULL COMMENT '访问令牌',
  `refresh_token` text DEFAULT NULL COMMENT '刷新令牌',
  `expires_in` datetime DEFAULT NULL COMMENT '令牌过期时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_oauth_type_oauth_id` (`oauth_type`, `oauth_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方认证信息表';

-- 修改app_user表，移除第三方认证相关字段
ALTER TABLE `app_user`
  DROP COLUMN IF EXISTS `oauth_type`,
  DROP COLUMN IF EXISTS `oauth_id`,
  DROP COLUMN IF EXISTS `access_token`,
  DROP COLUMN IF EXISTS `refresh_token`,
  DROP COLUMN IF EXISTS `token_expires_in`,
  DROP COLUMN IF EXISTS `scope`;