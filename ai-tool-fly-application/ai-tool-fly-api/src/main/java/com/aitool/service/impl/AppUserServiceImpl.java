package com.aitool.service.impl;

import com.aitool.entity.AppUser;
import com.aitool.entity.AppUserThirdAuth;
import com.aitool.mapper.AppUserMapper;
import com.aitool.mapper.AppUserThirdAuthMapper;
import com.aitool.service.AppUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 第三方登录用户服务实现类
 *
 * @author 10100
 */
@Service
@Slf4j
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements AppUserService {

    private final AppUserThirdAuthMapper appUserThirdAuthMapper;

    @Autowired
    public AppUserServiceImpl(AppUserThirdAuthMapper appUserThirdAuthMapper) {
        this.appUserThirdAuthMapper = appUserThirdAuthMapper;
    }

    /**
     * 根据第三方平台类型和用户ID查询用户
     *
     * @param oauthType 第三方平台类型(google,wechat,github)
     * @param oauthId   第三方平台用户ID
     * @return 用户信息
     */
    @Override
    public AppUser getByOauthTypeAndOauthId(String oauthType, String oauthId) {
        // 从第三方认证表中查询
        LambdaQueryWrapper<AppUserThirdAuth> queryWrapper = new LambdaQueryWrapper<AppUserThirdAuth>()
                .eq(AppUserThirdAuth::getOauthType, oauthType)
                .eq(AppUserThirdAuth::getOauthId, oauthId);
        AppUserThirdAuth thirdAuth = appUserThirdAuthMapper.selectOne(queryWrapper);

        if (thirdAuth != null) {
            // 返回对应的用户信息
            return getById(thirdAuth.getUserId());
        }

        return null;
    }

    /**
     * 保存或更新第三方登录用户信息
     *
     * @param appUser      用户信息
     * @param oauthType    第三方平台类型(google,wechat,github)
     * @param oauthId      第三方平台用户ID
     * @param accessToken  访问令牌
     * @param refreshToken 刷新令牌
     * @return 保存后的用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppUser saveOrUpdateOauthUser(AppUser appUser, String oauthType, String oauthId, String accessToken, String refreshToken) {
        log.info("处理第三方登录用户信息，类型: {}, ID: {}", oauthType, oauthId);

        // 参数校验
        if (oauthType == null || oauthId == null) {
            log.error("第三方认证参数错误，类型: {}, ID: {}", oauthType, oauthId);
            throw new IllegalArgumentException("第三方认证类型和ID不能为空");
        }

        if (appUser == null) {
            log.error("第三方登录用户信息为空");
            throw new IllegalArgumentException("用户信息不能为空");
        }

        // 确保基本用户信息不为空
        if (!StringUtils.hasText(appUser.getNickname())) {
            log.warn("用户昵称为空，设置默认昵称");
            appUser.setNickname("用户" + oauthId.substring(0, Math.min(8, oauthId.length())));
        }

        try {
            // 查询是否已存在该第三方认证
            LambdaQueryWrapper<AppUserThirdAuth> thirdAuthQuery = new LambdaQueryWrapper<AppUserThirdAuth>()
                    .eq(AppUserThirdAuth::getOauthType, oauthType)
                    .eq(AppUserThirdAuth::getOauthId, oauthId);
            AppUserThirdAuth thirdAuth = appUserThirdAuthMapper.selectOne(thirdAuthQuery);

            AppUser existUser = null;
            LocalDateTime now = LocalDateTime.now();

            if (thirdAuth != null) {
                log.info("找到已存在的第三方认证记录，用户ID: {}", thirdAuth.getUserId());
                // 已存在第三方认证，获取对应的用户
                existUser = getById(thirdAuth.getUserId());
                if (existUser == null) {
                    log.error("第三方认证对应的用户不存在，用户ID: {}", thirdAuth.getUserId());
                    throw new RuntimeException("用户不存在，第三方认证记录可能已损坏");
                }

                // 更新用户信息
                existUser.setUpdateTime(now);
                existUser.setLastLoginTime(now);

                // 更新可能变化的用户信息，只在有值时更新
                if (StringUtils.hasText(appUser.getNickname())) {
                    existUser.setNickname(appUser.getNickname());
                }
                if (StringUtils.hasText(appUser.getAvatar())) {
                    existUser.setAvatar(appUser.getAvatar());
                }
                if (StringUtils.hasText(appUser.getEmail())) {
                    existUser.setEmail(appUser.getEmail());
                }
                if (appUser.getSex() != null) {
                    existUser.setSex(appUser.getSex());
                }
                if (StringUtils.hasText(appUser.getSignature())) {
                    existUser.setSignature(appUser.getSignature());
                }

                boolean updateResult = updateById(existUser);
                if (!updateResult) {
                    log.error("更新用户信息失败，用户ID: {}", existUser.getId());
                    throw new RuntimeException("更新用户信息失败");
                }

                // 更新第三方认证信息
                thirdAuth.setName(existUser.getNickname());
                thirdAuth.setEmail(existUser.getEmail());
                thirdAuth.setAvatar(existUser.getAvatar());
                // 只在有值时更新令牌信息
                if (StringUtils.hasText(accessToken)) {
                    thirdAuth.setAccessToken(accessToken);
                }
                if (StringUtils.hasText(refreshToken)) {
                    thirdAuth.setRefreshToken(refreshToken);
                }
                thirdAuth.setUpdatedAt(now);

                int updateCount = appUserThirdAuthMapper.updateById(thirdAuth);
                if (updateCount != 1) {
                    log.error("更新第三方认证信息失败，ID: {}", thirdAuth.getId());
                    throw new RuntimeException("更新第三方认证信息失败");
                }

                log.info("成功更新已存在用户的第三方登录信息，用户ID: {}", existUser.getId());
            } else {
                log.info("未找到第三方认证记录，检查是否存在相同邮箱的用户");
                // 查询是否有相同邮箱的用户，只在邮箱不为空时查询
                if (StringUtils.hasText(appUser.getEmail())) {
                    LambdaQueryWrapper<AppUser> userQuery = new LambdaQueryWrapper<AppUser>()
                            .eq(AppUser::getEmail, appUser.getEmail());
                    existUser = getOne(userQuery);
                }

                if (existUser == null) {
                    log.info("创建新用户");
                    // 新增用户
                    existUser = new AppUser();
                    // 设置用户名，优先使用邮箱，如果没有则使用第三方平台类型+ID
                    if (StringUtils.hasText(appUser.getEmail())) {
                        existUser.setUsername(appUser.getEmail());
                    } else {
                        existUser.setUsername(oauthType + "_" + oauthId);
                    }
                    existUser.setNickname(appUser.getNickname());
                    existUser.setAvatar(appUser.getAvatar());
                    existUser.setEmail(appUser.getEmail());
                    existUser.setSex(appUser.getSex() != null ? appUser.getSex() : 0);
                    // 默认状态为正常
                    existUser.setStatus(1);
                    existUser.setCreateTime(now);
                    existUser.setUpdateTime(now);
                    existUser.setLastLoginTime(now);
                    existUser.setSignature(appUser.getSignature());

                    boolean saveResult = save(existUser);
                    if (!saveResult) {
                        log.error("保存新用户失败");
                        throw new RuntimeException("保存新用户失败");
                    }
                } else {
                    log.info("找到相同邮箱的用户，ID: {}", existUser.getId());
                    // 更新已有用户的登录时间和其他信息
                    existUser.setUpdateTime(now);
                    existUser.setLastLoginTime(now);

                    // 可选更新其他信息
                    if (StringUtils.hasText(appUser.getNickname())) {
                        existUser.setNickname(appUser.getNickname());
                    }
                    if (StringUtils.hasText(appUser.getAvatar())) {
                        existUser.setAvatar(appUser.getAvatar());
                    }
                    if (appUser.getSex() != null) {
                        existUser.setSex(appUser.getSex());
                    }
                    if (StringUtils.hasText(appUser.getSignature())) {
                        existUser.setSignature(appUser.getSignature());
                    }

                    boolean updateResult = updateById(existUser);
                    if (!updateResult) {
                        log.error("更新已有用户信息失败，用户ID: {}", existUser.getId());
                        throw new RuntimeException("更新已有用户信息失败");
                    }
                }

                // 创建新的第三方认证记录
                AppUserThirdAuth newThirdAuth = AppUserThirdAuth.builder()
                        .userId(existUser.getId())
                        .email(existUser.getEmail())
                        .name(existUser.getNickname())
                        .oauthType(oauthType)
                        .oauthId(oauthId)
                        .avatar(existUser.getAvatar())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .createdAt(now)
                        .updatedAt(now)
                        .build();

                int insertCount = appUserThirdAuthMapper.insert(newThirdAuth);
                if (insertCount != 1) {
                    log.error("插入第三方认证记录失败");
                    throw new RuntimeException("插入第三方认证记录失败");
                }

                log.info("成功创建第三方登录用户，用户ID: {}", existUser.getId());
            }
            return existUser;
        } catch (Exception e) {
            log.error("处理第三方登录用户信息异常", e);
            // 重新抛出异常，触发事务回滚
            throw e;
        }
    }
}