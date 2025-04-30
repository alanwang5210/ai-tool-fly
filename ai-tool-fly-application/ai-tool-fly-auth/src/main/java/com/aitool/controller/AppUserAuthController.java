package com.aitool.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.aitool.common.Result;
import com.aitool.dto.Captcha;
import com.aitool.dto.EmailRegisterDto;
import com.aitool.dto.AppLoginDTO;
import com.aitool.dto.user.LoginAppUserInfo;
import com.aitool.service.AppUserAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * App用户认证管理
 * 支持账号密码、Google、微信、GitHub等多种登录方式
 * 与系统用户认证流程完全分离
 *
 * @author 10100
 */
@RestController
@RequestMapping("/api/app")
@RequiredArgsConstructor
@Api(tags = "App用户认证管理")
public class AppUserAuthController {

    private final AppUserAuthService appUserAuthService;

    /**
     * 获取第三方授权地址
     * 支持Google、GitHub等第三方平台登录
     *
     * @param source 第三方平台类型(google,github,gitee,qq,weibo,wechat)
     * @return 授权地址
     */
    @RequestMapping("/auth/render/{source}")
    @ApiOperation(value = "获取第三方授权地址")
    public Result<String> renderAuth(@PathVariable String source) {
        return Result.success(appUserAuthService.renderAuth(source));
    }

    /**
     * 第三方授权回调处理
     *
     * @param callback            回调参数
     * @param source              第三方平台类型
     * @param httpServletResponse 响应对象
     * @throws IOException IO异常
     */
    @RequestMapping("/auth/callback/{source}")
    @ApiOperation(value = "第三方授权回调")
    public void login(AuthCallback callback, @PathVariable String source, HttpServletResponse httpServletResponse) throws IOException {
        appUserAuthService.authLogin(callback, source, httpServletResponse);
    }

    /**
     * App用户账号密码登录
     *
     * @param appLoginDTO 登录参数
     * @return 登录用户信息
     */
    @ApiOperation(value = "App用户账号密码登录")
    @PostMapping("/login")
    public Result<LoginAppUserInfo> login(@Validated @RequestBody AppLoginDTO appLoginDTO) {
        return Result.success(appUserAuthService.login(appLoginDTO));
    }

    /**
     * 获取滑块验证码
     *
     * @return 验证码信息
     */
    @SaIgnore
    @ApiOperation(value = "获取滑块验证码")
    @GetMapping("/getCaptcha")
    public Result<Captcha> getCaptcha() {
        return Result.success(appUserAuthService.getCaptcha());
    }

    /**
     * App用户登出
     *
     * @return 操作结果
     */
    @ApiOperation(value = "App用户登出")
    @PostMapping("/logout")
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.success(null);
    }

    /**
     * 发送注册邮箱验证码
     *
     * @param email 邮箱地址
     * @return 发送结果
     * @throws MessagingException 邮件异常
     */
    @ApiOperation(value = "发送注册邮箱验证码")
    @GetMapping("/email/sendCode")
    public Result<Boolean> sendEmailCode(String email) throws MessagingException {
        return Result.success(appUserAuthService.sendEmailCode(email));
    }

    /**
     * 邮箱账号注册
     *
     * @param dto 注册参数
     * @return 注册结果
     */
    @ApiOperation(value = "邮箱账号注册")
    @PostMapping("/email/register")
    public Result<Boolean> register(@RequestBody EmailRegisterDto dto) {
        return Result.success(appUserAuthService.register(dto));
    }

    /**
     * 根据邮箱修改密码
     *
     * @param dto 修改密码参数
     * @return 修改结果
     */
    @ApiOperation(value = "根据邮箱修改密码")
    @PostMapping("/email/forgot")
    public Result<Boolean> forgot(@RequestBody EmailRegisterDto dto) {
        return Result.success(appUserAuthService.forgot(dto));
    }

    /**
     * 获取微信扫码登录验证码
     *
     * @return 登录验证码
     */
    @ApiOperation(value = "获取微信扫码登录验证码")
    @GetMapping("/wechat/getCode")
    public Result<String> getWechatLoginCode() {
        return Result.success(appUserAuthService.getWechatLoginCode());
    }

    /**
     * 获取微信扫码登录状态
     *
     * @param loginCode 登录验证码
     * @return 登录用户信息
     */
    @ApiOperation(value = "获取微信扫码登录状态")
    @GetMapping("/wechat/isLogin/{loginCode}")
    public Result<LoginAppUserInfo> getWechatIsLogin(@PathVariable String loginCode) {
        return Result.success(appUserAuthService.getWechatIsLogin(loginCode));
    }

    /**
     * 微信小程序登录
     *
     * @param code 微信授权码
     * @return 登录用户信息
     */
    @ApiOperation(value = "微信小程序登录")
    @GetMapping("/wechat/appletLogin/{code}")
    public Result<LoginAppUserInfo> appletLogin(@PathVariable String code) {
        return Result.success(appUserAuthService.appletLogin(code));
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 登录用户信息
     */
    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/info")
    public Result<LoginAppUserInfo> getUserInfo() {
        return Result.success(appUserAuthService.getLoginUserInfo());
    }

    /**
     * Google登录
     *
     * @param idToken Google身份令牌
     * @return 登录用户信息
     */
    @ApiOperation(value = "Google登录")
    @PostMapping("/google/login")
    public Result<LoginAppUserInfo> googleLogin(@RequestBody String idToken) {
        // 调用Google登录服务
        return Result.success(appUserAuthService.googleLogin(idToken));
    }

    /**
     * GitHub登录
     *
     * @param code GitHub授权码
     * @return 登录用户信息
     */
    @ApiOperation(value = "GitHub登录")
    @PostMapping("/github/login/{code}")
    public Result<LoginAppUserInfo> githubLogin(@PathVariable String code) {
        // 调用GitHub登录服务
        return Result.success();
    }
}