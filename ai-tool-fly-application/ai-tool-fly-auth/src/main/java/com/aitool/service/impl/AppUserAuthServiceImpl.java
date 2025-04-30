package com.aitool.service.impl;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.aitool.common.Constants;
import com.aitool.common.RedisConstants;
import com.aitool.config.properties.*;
import com.aitool.dto.AppLoginDTO;
import com.aitool.dto.Captcha;
import com.aitool.dto.EmailRegisterDto;
import com.aitool.dto.user.LoginAppUserInfo;
import com.aitool.entity.AppUser;
import com.aitool.entity.AppUserThirdAuth;
import com.aitool.enums.UserTypeEnum;
import com.aitool.exception.ServiceException;
import com.aitool.mapper.AppUserMapper;
import com.aitool.mapper.AppUserThirdAuthMapper;
import com.aitool.service.AppUserAuthService;
import com.aitool.service.AppUserService;
import com.aitool.service.AuthService;
import com.aitool.utils.BeanCopyUtil;
import com.aitool.utils.EmailUtil;
import com.aitool.utils.IpUtil;
import com.aitool.utils.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.*;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * App用户认证服务实现类
 *
 * @author 10100
 */
@Slf4j
@Service
public class AppUserAuthServiceImpl implements AppUserAuthService {

    private final AuthService authService;

    private final AppUserMapper appUserMapper;

    private final RedisUtil redisUtil;

    private final AppUserThirdAuthMapper appUserThirdAuthMapper;

    private final GiteeConfigProperties giteeConfigProperties;

    private final GithubConfigProperties githubConfigProperties;

    private final QqConfigProperties qqConfigProperties;

    private final WeiboConfigProperties weiboConfigProperties;

    private final WechatProperties wechatProperties;

    private final GoogleConfigProperties googleConfigProperties;

    private final EmailUtil emailUtil;

    private final AppUserService appUserService;

    public AppUserAuthServiceImpl(AuthService authService, AppUserMapper appUserMapper, RedisUtil redisUtil, EmailUtil emailUtil,
                                  AppUserThirdAuthMapper appUserThirdAuthMapper,
                                  GiteeConfigProperties giteeConfigProperties,
                                  GithubConfigProperties githubConfigProperties, QqConfigProperties qqConfigProperties,
                                  WeiboConfigProperties weiboConfigProperties, WechatProperties wechatProperties,
                                  GoogleConfigProperties googleConfigProperties, AppUserService appUserService) {
        this.authService = authService;
        this.appUserMapper = appUserMapper;
        this.redisUtil = redisUtil;
        this.appUserThirdAuthMapper = appUserThirdAuthMapper;
        this.giteeConfigProperties = giteeConfigProperties;
        this.githubConfigProperties = githubConfigProperties;
        this.qqConfigProperties = qqConfigProperties;
        this.weiboConfigProperties = weiboConfigProperties;
        this.wechatProperties = wechatProperties;
        this.googleConfigProperties = googleConfigProperties;
        this.appUserService = appUserService;
        this.emailUtil = emailUtil;
    }

    private final String[] avatarList = {
            "https://api.dicebear.com/6.x/pixel-art/svg?seed=Raccoon",
            "https://api.dicebear.com/6.x/pixel-art/svg?seed=Kitty",
            "https://api.dicebear.com/6.x/pixel-art/svg?seed=Puppy",
            "https://api.dicebear.com/6.x/pixel-art/svg?seed=Bunny",
            "https://api.dicebear.com/6.x/pixel-art/svg?seed=Fox"
    };

    @Override
    public LoginAppUserInfo login(AppLoginDTO appLoginDTO) {
        // 查询APP用户
        LambdaQueryWrapper<AppUser> queryWrapper = new LambdaQueryWrapper<AppUser>()
                .eq(AppUser::getEmail, appLoginDTO.getUsername())
                .or()
                .eq(AppUser::getMobile, appLoginDTO.getUsername());
        AppUser appUser = appUserMapper.selectOne(queryWrapper);

        if (appUser == null) {
            throw new ServiceException("用户名或密码错误");
        }

        // 验证状态
        if (appUser.getStatus() != 1) {
            throw new ServiceException("账号已被禁用");
        }

        // 执行登录 (使用不同的登录类型标识)
        StpUtil.login(appUser.getId());
        String tokenValue = StpUtil.getTokenValue();

        // 返回用户信息
        LoginAppUserInfo loginUserInfo = LoginAppUserInfo.builder()
                .id(appUser.getId().intValue())
                .username(appUser.getUsername() != null ? appUser.getUsername() : (appUser.getEmail() != null ? appUser.getEmail() : appUser.getMobile()))
                .nickname(appUser.getNickname())
                .avatar(appUser.getAvatar())
                .email(appUser.getEmail())
                .mobile(appUser.getMobile())
                .sex(appUser.getSex())
                .signature(appUser.getSignature())
                .token(tokenValue)
                .build();
        StpUtil.getSession().set(Constants.CURRENT_USER, loginUserInfo);
        return loginUserInfo;
    }

    @Override
    public LoginAppUserInfo getLoginUserInfo() {
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        AppUser user = appUserMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return BeanCopyUtil.copyObj(user, LoginAppUserInfo.class);
    }

    @Override
    public Boolean sendEmailCode(String email) throws MessagingException {
        emailUtil.sendCode(email);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean register(EmailRegisterDto dto) {

        validateEmailCode(dto);

        AppUser appUser = appUserMapper.selectOne(new LambdaQueryWrapper<AppUser>().eq(AppUser::getEmail, dto.getEmail()));
        if (appUser != null) {
            throw new ServiceException("当前邮箱已注册，请前往登录");
        }

        //获取随机头像
        String avatar = avatarList[(int) (Math.random() * avatarList.length)];
        appUser = AppUser.builder()
                .username(dto.getEmail()) // 默认使用邮箱作为用户名
                .password(BCrypt.hashpw(dto.getPassword()))
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .avatar(avatar)
                .status(Constants.YES)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        appUserMapper.insert(appUser);

        redisUtil.delete(RedisConstants.CAPTCHA_CODE_KEY + dto.getEmail());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean forgot(EmailRegisterDto dto) {
        validateEmailCode(dto);
        AppUser appUser = appUserMapper.selectOne(new LambdaQueryWrapper<AppUser>().eq(AppUser::getEmail, dto.getEmail()));
        if (appUser == null) {
            throw new ServiceException("当前邮箱未注册，请前往注册");
        }
        appUser.setPassword(BCrypt.hashpw(dto.getPassword()));
        appUserMapper.updateById(appUser);
        redisUtil.delete(RedisConstants.CAPTCHA_CODE_KEY + dto.getEmail());
        return true;
    }

    @Override
    public String getWechatLoginCode() {
        //随机获取4位数字
        String code = "DL" + (int) ((Math.random() * 9 + 1) * 1000);
        redisUtil.set(RedisConstants.WX_LOGIN_USER_CODE + code, "NOT-LOGIN", RedisConstants.MINUTE_EXPIRE, TimeUnit.SECONDS);
        return code;
    }

    @Override
    public LoginAppUserInfo getWechatIsLogin(String loginCode) {
        Object value = redisUtil.get(RedisConstants.WX_LOGIN_USER + loginCode);

        if (value == null) {
            throw new ServiceException("登录失败");
        }

        LoginAppUserInfo loginUserInfo = JSONUtil.toBean(JSONUtil.parseObj(value), LoginAppUserInfo.class);

        StpUtil.login(loginUserInfo.getId());
        loginUserInfo.setToken(StpUtil.getTokenValue());

        return loginUserInfo;
    }

    @Override
    public String renderAuth(String source) {
        AuthRequest authRequest = getAuthRequest(source);
        return authRequest.authorize(AuthStateUtils.createState());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authLogin(AuthCallback callback, String source, HttpServletResponse httpServletResponse) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse<AuthUser> response = authRequest.login(callback);

        if (response.getData() == null) {
            log.info("用户取消了 {} 第三方登录", source);
            httpServletResponse.sendRedirect("https://www.shiyit.com");
            return;
        }
        String result = com.alibaba.fastjson.JSONObject.toJSONString(response.getData());
        log.info("第三方登录验证结果:{}", result);

        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
        Object uuid = jsonObject.get("uuid");
        String name = jsonObject.getString("username");
        String avatar = jsonObject.getString("avatar");
        String email = jsonObject.getString("email");
        
        // 获取用户ip信息
        String ipAddress = IpUtil.getIp();
        String ipSource = IpUtil.getIp2region(ipAddress);
        
        // 查询是否已存在该第三方认证的用户
        AppUser appUser = appUserService.getByOauthTypeAndOauthId(source, uuid.toString());

        if (appUser == null) {
            // 设置一个默认头像URL，如果第三方没有提供头像
            if (avatar == null || avatar.isEmpty()) {
                avatar = avatarList[(int) (Math.random() * avatarList.length)];
            }
            
            // 创建新的App用户信息
            appUser = new AppUser();
            appUser.setNickname(source.toUpperCase() + "-" + getRandomString(6));
            appUser.setAvatar(avatar);
            if (email != null && !email.isEmpty()) {
                appUser.setEmail(email);
            }
            // 设置IP信息
            appUser.setIp(ipAddress);
            appUser.setIpLocation(ipSource);
            // 默认性别
            appUser.setSex(0);
            // 正常状态
            appUser.setStatus(1);
            appUser.setCreateTime(LocalDateTime.now());
            appUser.setUpdateTime(LocalDateTime.now());
            appUser.setLastLoginTime(LocalDateTime.now());

            // 保存或更新第三方登录用户信息
            appUser = appUserService.saveOrUpdateOauthUser(appUser, source, uuid.toString(), null, null);
        } else {
            // 检查用户状态
            if (appUser.getStatus() != 1) {
                throw new ServiceException("账号已被禁用，请联系管理员");
            }

            // 更新最后登录时间和IP信息
            appUser.setLastLoginTime(LocalDateTime.now());
            appUser.setUpdateTime(LocalDateTime.now());
            appUser.setIp(ipAddress);
            appUser.setIpLocation(ipSource);
            appUserMapper.updateById(appUser);
        }

        // 执行登录
        StpUtil.login(appUser.getId());
        
        // 构建用户信息并保存到会话
        LoginAppUserInfo loginUserInfo = LoginAppUserInfo.builder()
                .id(appUser.getId().intValue())
                .username(appUser.getEmail() != null ? appUser.getEmail() : source + "_" + uuid)
                .nickname(appUser.getNickname())
                .avatar(appUser.getAvatar())
                .email(appUser.getEmail())
                .mobile(appUser.getMobile())
                .sex(appUser.getSex())
                .signature(appUser.getSignature())
                .token(StpUtil.getTokenValue())
                .build();
        StpUtil.getSession().set(Constants.CURRENT_USER, loginUserInfo);
        
        // 重定向到前端页面，带上token参数
        httpServletResponse.sendRedirect("https://www.shiyit.com/?token=" + StpUtil.getTokenValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginAppUserInfo appletLogin(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + wechatProperties.getAppletAppId()
                + "&secret=" + wechatProperties.getAppletSecret() + "&js_code=" + code + "&grant_type=authorization_code";
        String result = HttpUtil.get(url);
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
        Object openid = jsonObject.get("openid");
        if (openid == null) {
            throw new ServiceException("登录失败");
        }

        // 查询是否已存在微信小程序登录的用户
        AppUser appUser = appUserService.getByOauthTypeAndOauthId("applet", openid.toString());

        if (appUser == null) {
            // 设置一个默认头像URL
            String randomAvatar = avatarList[(int) (Math.random() * avatarList.length)];

            // 创建新的App用户信息
            appUser = new AppUser();
            appUser.setUsername("applet_" + openid.toString()); // 设置用户名
            appUser.setNickname("APPLET-" + getRandomString(6));
            appUser.setAvatar(randomAvatar);
            // 默认性别
            appUser.setSex(0);
            // 正常状态
            appUser.setStatus(1);
            appUser.setCreateTime(LocalDateTime.now());
            appUser.setUpdateTime(LocalDateTime.now());
            appUser.setLastLoginTime(LocalDateTime.now());

            // 保存或更新第三方登录用户信息
            appUser = appUserService.saveOrUpdateOauthUser(appUser, "applet", openid.toString(), null, null);
        } else {
            // 检查用户状态
            if (appUser.getStatus() != 1) {
                throw new ServiceException("账号已被禁用，请联系管理员");
            }

            // 更新最后登录时间
            appUser.setLastLoginTime(LocalDateTime.now());
            appUser.setUpdateTime(LocalDateTime.now());
            appUserMapper.updateById(appUser);
        }

        // 执行登录
        StpUtil.login(appUser.getId());

        // 返回用户信息
        LoginAppUserInfo loginUserInfo = LoginAppUserInfo.builder()
                .id(appUser.getId().intValue())
                .username(appUser.getUsername() != null ? appUser.getUsername() : (appUser.getEmail() != null ? appUser.getEmail() : "applet_" + openid))
                .nickname(appUser.getNickname())
                .avatar(appUser.getAvatar())
                .email(appUser.getEmail())
                .mobile(appUser.getMobile())
                .sex(appUser.getSex())
                .signature(appUser.getSignature())
                .token(StpUtil.getTokenValue())
                .build();
        StpUtil.getSession().set(Constants.CURRENT_USER, loginUserInfo);

        return loginUserInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String wechatLogin(WxMpXmlMessage message) {
        String code = message.getContent().toUpperCase();
        //先判断登录码是否已过期
        Object e = redisUtil.hasKey(RedisConstants.WX_LOGIN_USER_CODE + code);
        if (e == null) {
            return "验证码已过期";
        }
        LoginAppUserInfo loginUserInfo = wechatLogin(message.getFromUser());
        //修改redis缓存 以便监听是否已经授权成功
        redisUtil.set(RedisConstants.WX_LOGIN_USER + code, JSONUtil.toJsonStr(loginUserInfo), RedisConstants.MINUTE_EXPIRE, TimeUnit.SECONDS);
        return "网站登录成功！(若页面长时间未跳转请刷新验证码)";
    }

    @Override
    public Captcha getCaptcha() {
        return authService.getCaptcha();
    }

    @Override
    public LoginAppUserInfo googleLogin(String idToken) {
        try {
            // 解析Google idToken
            JWT jwt = JWTUtil.parseToken(idToken);
            Map<String, Object> payload = jwt.getPayload().getClaimsJson();

            // 获取用户信息
            String email = (String) payload.get("email");
            String name = (String) payload.get("name");
            String picture = (String) payload.get("picture");
            String googleId = (String) payload.get("sub");

            // 验证邮箱是否已验证
            Boolean emailVerified = (Boolean) payload.get("email_verified");
            if (emailVerified == null || !emailVerified) {
                throw new ServiceException("Google邮箱未验证");
            }

            // 查询是否已存在该Google第三方认证
            LambdaQueryWrapper<AppUserThirdAuth> thirdAuthQuery = new LambdaQueryWrapper<AppUserThirdAuth>()
                    .eq(AppUserThirdAuth::getOauthType, "google")
                    .eq(AppUserThirdAuth::getOauthId, googleId);
            AppUserThirdAuth thirdAuth = appUserThirdAuthMapper.selectOne(thirdAuthQuery);

            AppUser appUser;

            if (thirdAuth != null) {
                // 已存在第三方认证，获取对应的用户
                appUser = appUserMapper.selectById(thirdAuth.getUserId());
                if (appUser == null) {
                    throw new ServiceException("用户不存在");
                }

                // 更新第三方认证信息
                thirdAuth.setName(name);
                thirdAuth.setEmail(email);
                thirdAuth.setAvatar(picture);
                thirdAuth.setUpdatedAt(LocalDateTime.now());
                appUserThirdAuthMapper.updateById(thirdAuth);
            } else {
                // 查询是否有相同邮箱的用户
                LambdaQueryWrapper<AppUser> userQuery = new LambdaQueryWrapper<AppUser>()
                        .eq(AppUser::getEmail, email);
                appUser = appUserMapper.selectOne(userQuery);

                if (appUser == null) {
                    // 创建新用户
                    appUser = AppUser.builder()
                            .username(email) // 使用邮箱作为用户名
                            .nickname(name)
                            .avatar(picture)
                            .email(email)
                            // 默认性别
                            .sex(0)
                            // 正常状态
                            .status(1)
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .lastLoginTime(LocalDateTime.now())
                            .build();
                    appUserMapper.insert(appUser);
                }

                // 创建新的第三方认证记录
                thirdAuth = AppUserThirdAuth.builder()
                        .userId(appUser.getId())
                        .email(email)
                        .name(name)
                        .oauthType("google")
                        .oauthId(googleId)
                        .avatar(picture)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                appUserThirdAuthMapper.insert(thirdAuth);
            }

            // 更新用户信息
            appUser.setLastLoginTime(LocalDateTime.now());
            appUser.setUpdateTime(LocalDateTime.now());
            appUserMapper.updateById(appUser);

            // 执行登录
            SaManager.getStpLogic(UserTypeEnum.APP.getSource()).login(appUser.getId());
            String tokenValue = StpUtil.getTokenValue();

            // 返回用户信息
            LoginAppUserInfo loginUserInfo = LoginAppUserInfo.builder()
                    .id(appUser.getId().intValue())
                    .username(appUser.getEmail())
                    .nickname(appUser.getNickname())
                    .avatar(appUser.getAvatar())
                    .email(appUser.getEmail())
                    .mobile(appUser.getMobile())
                    .sex(appUser.getSex())
                    .signature(appUser.getSignature())
                    .token(tokenValue)
                    .build();
            SaManager.getStpLogic(UserTypeEnum.APP.getSource()).getSession().set(Constants.CURRENT_USER, loginUserInfo);
            return loginUserInfo;
        } catch (Exception e) {
            log.error("Google登录失败", e);
            throw new ServiceException("Google登录失败: " + e.getMessage());
        }
    }

    private LoginAppUserInfo wechatLogin(String openId) {
        // 查询是否已存在微信登录的用户
        AppUser appUser = appUserService.getByOauthTypeAndOauthId("wechat", openId);

        if (appUser == null) {
            // 设置一个默认头像URL
            String randomAvatar = avatarList[(int) (Math.random() * avatarList.length)];

            // 创建新的App用户信息
            appUser = new AppUser();
            appUser.setNickname("WECHAT-" + getRandomString(6));
            appUser.setAvatar(randomAvatar);
            // 默认性别
            appUser.setSex(0);
            // 正常状态
            appUser.setStatus(1);
            appUser.setCreateTime(LocalDateTime.now());
            appUser.setUpdateTime(LocalDateTime.now());
            appUser.setLastLoginTime(LocalDateTime.now());

            // 保存或更新第三方登录用户信息
            appUser = appUserService.saveOrUpdateOauthUser(appUser, "wechat", openId, null, null);
        } else {
            // 更新最后登录时间
            appUser.setLastLoginTime(LocalDateTime.now());
            appUser.setUpdateTime(LocalDateTime.now());
            appUserMapper.updateById(appUser);
        }

        // 执行登录
        StpUtil.login(appUser.getId());

        // 返回用户信息
        LoginAppUserInfo loginUserInfo = LoginAppUserInfo.builder()
                .id(appUser.getId().intValue())
                .username(appUser.getEmail() != null ? appUser.getEmail() : "wechat_" + openId)
                .nickname(appUser.getNickname())
                .avatar(appUser.getAvatar())
                .email(appUser.getEmail())
                .mobile(appUser.getMobile())
                .sex(appUser.getSex())
                .signature(appUser.getSignature())
                .token(StpUtil.getTokenValue())
                .build();
        StpUtil.getSession().set(Constants.CURRENT_USER, loginUserInfo);

        return loginUserInfo;
    }


    private @NotNull AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        switch (source) {
            case "gitee":
                authRequest = new AuthGiteeRequest(AuthConfig.builder()
                        .clientId(giteeConfigProperties.getAppId())
                        .clientSecret(giteeConfigProperties.getAppSecret())
                        .redirectUri(giteeConfigProperties.getRedirectUrl())
                        .build());
                break;
            case "qq":
                authRequest = new AuthQqRequest(AuthConfig.builder()
                        .clientId(qqConfigProperties.getAppId())
                        .clientSecret(qqConfigProperties.getAppSecret())
                        .redirectUri(qqConfigProperties.getRedirectUrl())
                        .build());
                break;
            case "weibo":
                authRequest = new AuthWeiboRequest(AuthConfig.builder()
                        .clientId(weiboConfigProperties.getAppId())
                        .clientSecret(weiboConfigProperties.getAppSecret())
                        .redirectUri(weiboConfigProperties.getRedirectUrl())
                        .build());
                break;
            case "github":
                authRequest = new AuthGithubRequest(AuthConfig.builder()
                        .clientId(githubConfigProperties.getAppId())
                        .clientSecret(githubConfigProperties.getAppSecret())
                        .redirectUri(githubConfigProperties.getRedirectUrl())
                        .build());
                break;
            case "google":
                authRequest = new AuthGoogleRequest(AuthConfig.builder()
                        .clientId(googleConfigProperties.getAppId())
                        .clientSecret(googleConfigProperties.getAppSecret())
                        .redirectUri(googleConfigProperties.getRedirectUrl())
                        .build());
                break;
            default:
                break;
        }
        if (null == authRequest) {
            throw new AuthException("授权地址无效");
        }
        return authRequest;
    }

    /**
     * 随机生成6位数的字符串
     */
    public static String getRandomString(int length) {
        String str = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    private void validateEmailCode(EmailRegisterDto dto) {
        Object code = redisUtil.get(RedisConstants.CAPTCHA_CODE_KEY + dto.getEmail());
        if (code == null || !code.equals(dto.getCode())) {
            throw new ServiceException("验证码已过期或输入错误");
        }
    }
}