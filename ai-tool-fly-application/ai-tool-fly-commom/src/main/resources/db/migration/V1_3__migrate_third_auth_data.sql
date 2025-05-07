-- 数据迁移脚本：将app_user表中的第三方认证数据迁移到app_user_third_auth表

-- 插入已有的第三方认证数据到新表
INSERT INTO `app_user_third_auth` (`user_id`, `email`, `name`, `oauth_type`, `oauth_id`, `avatar`, `access_token`, `refresh_token`, `created_at`, `updated_at`)
SELECT 
  `id` AS `user_id`, 
  `email`, 
  `nickname` AS `name`, 
  `oauth_type`, 
  `oauth_id`, 
  `avatar`, 
  `access_token`, 
  `refresh_token`, 
  `create_time` AS `created_at`, 
  `update_time` AS `updated_at`
FROM `app_user` 
WHERE `oauth_type` IS NOT NULL AND `oauth_id` IS NOT NULL;