package com.aitool.service.impl;

import com.aitool.entity.AppUser;
import com.aitool.mapper.AppUserMapper;
import com.aitool.service.AppUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 第三方登录用户服务实现类
 * @author 10100
 */
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements AppUserService {

    /**
     * 根据第三方平台类型和用户ID查询用户
     *
     * @param oauthType 第三方平台类型(google,wechat,github)
     * @param oauthId   第三方平台用户ID
     * @return 用户信息
     */
    @Override
    public AppUser getByOauthTypeAndOauthId(String oauthType, String oauthId) {
        return baseMapper.selectByOauthTypeAndOauthId(oauthType, oauthId);
    }

    /**
     * 保存或更新第三方登录用户信息
     *
     * @param appUser 用户信息
     * @return 保存后的用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppUser saveOrUpdateOauthUser(AppUser appUser) {
        // 查询是否已存在该用户
        AppUser existUser = getByOauthTypeAndOauthId(appUser.getOauthType(), appUser.getOauthId());

        if (existUser != null) {
            // 更新用户信息
            appUser.setId(existUser.getId());
            appUser.setUpdateTime(LocalDateTime.now());
            appUser.setLastLoginTime(LocalDateTime.now());
            updateById(appUser);
            // 返回更新后的用户信息
            return getById(existUser.getId());
        } else {
            // 新增用户
            appUser.setCreateTime(LocalDateTime.now());
            appUser.setUpdateTime(LocalDateTime.now());
            appUser.setLastLoginTime(LocalDateTime.now());
            // 默认状态为正常
            appUser.setStatus(1);
            save(appUser);
            return appUser;
        }
    }
}