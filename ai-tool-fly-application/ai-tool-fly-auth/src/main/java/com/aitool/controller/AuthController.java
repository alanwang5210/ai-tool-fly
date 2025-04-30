package com.aitool.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.aitool.common.Result;
import com.aitool.dto.Captcha;
import com.aitool.dto.LoginDTO;
import com.aitool.dto.user.LoginUserInfo;
import com.aitool.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统用户认证管理
 *
 * @author 10100
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "系统用户认证管理")
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "系统用户登录")
    @PostMapping("/auth/login")
    public Result<LoginUserInfo> login(@Validated @RequestBody LoginDTO loginDTO) {
        return Result.success(authService.login(loginDTO));
    }

    @SaIgnore
    @ApiOperation(value = "获取滑块验证码")
    @GetMapping("/auth/getCaptcha")
    public Result<Captcha> getCaptcha() {
        return Result.success(authService.getCaptcha());
    }

    @ApiOperation(value = "系统用户登出")
    @PostMapping("/auth/logout")
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.success(null);
    }

    @GetMapping("/auth/info")
    public Result<LoginUserInfo> getUserInfo(@RequestParam(defaultValue = "admin") String source) {
        return Result.success(authService.getLoginUserInfo(source));
    }
}