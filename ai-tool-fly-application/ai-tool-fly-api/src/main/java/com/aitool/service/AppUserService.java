package com.aitool.service;

import com.aitool.entity.AppUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 第三方登录用户服务接口
 */
public interface AppUserService extends IService<AppUser> {
    
    /**
     * 根据第三方平台类型和用户ID查询用户
     * 
     * @param oauthType 第三方平台类型(google,wechat,github)
     * @param oauthId 第三方平台用户ID
     * @return 用户信息
     */
    AppUser getByOauthTypeAndOauthId(String oauthType, String oauthId);
    
    /**
     * 保存或更新第三方登录用户信息
     * 
     * @param appUser 用户信息
     * @return 保存后的用户信息
     */
    AppUser saveOrUpdateOauthUser(AppUser appUser);
}