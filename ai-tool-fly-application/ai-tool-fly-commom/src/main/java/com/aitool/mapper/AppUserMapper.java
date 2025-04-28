package com.aitool.mapper;

import com.aitool.entity.AppUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 第三方登录用户Mapper接口
 *
 * @author 10100
 */
@Mapper
public interface AppUserMapper extends BaseMapper<AppUser> {

    /**
     * 根据第三方平台类型和用户ID查询用户
     *
     * @param oauthType 第三方平台类型(google,wechat,github)
     * @param oauthId   第三方平台用户ID
     * @return 用户信息
     */
    AppUser selectByOauthTypeAndOauthId(@Param("oauthType") String oauthType, @Param("oauthId") String oauthId);
}