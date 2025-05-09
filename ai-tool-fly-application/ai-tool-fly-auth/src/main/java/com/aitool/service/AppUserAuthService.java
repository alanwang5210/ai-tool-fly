package com.aitool.service;

import com.aitool.dto.AppLoginDTO;
import com.aitool.dto.Captcha;
import com.aitool.dto.EmailRegisterDto;
import com.aitool.dto.user.LoginAppUserInfo;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.zhyd.oauth.model.AuthCallback;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * App用户认证服务接口
 *
 * @author 10100
 */
public interface AppUserAuthService {

    /**
     * App用户登录
     */
    LoginAppUserInfo login(AppLoginDTO appLoginDTO);

    /**
     * 获取当前登录用户信息
     */
    LoginAppUserInfo getLoginUserInfo();

    /**
     * 发送注册邮箱验证码
     *
     * @param email email
     * @return java.lang.Boolean
     */
    Boolean sendEmailCode(String email) throws MessagingException;

    /**
     * 邮箱账号注册
     *
     * @param dto dto
     * @return java.lang.Boolean
     */
    Boolean register(EmailRegisterDto dto);

    /**
     * 邮箱账号重置密码
     *
     * @param dto dto
     * @return java.lang.Boolean
     */
    Boolean forgot(EmailRegisterDto dto);

    /**
     * 获取微信扫码登录验证码
     *
     * @return java.lang.String
     */
    String getWechatLoginCode();


    /**
     * 验证微信是否扫码登录
     *
     * @param loginCode loginCode
     * @return com.aitool.dto.user.LoginAppUserInfo
     */
    LoginAppUserInfo getWechatIsLogin(String loginCode);

    /**
     * 获取第三方授权地址
     *
     * @param source source
     * @return java.lang.String
     */
    String renderAuth(String source);

    /**
     * 第三方授权登录
     *
     * @param source              source
     * @param httpServletResponse httpServletResponse
     */
    void authLogin(AuthCallback callback, String source, HttpServletResponse httpServletResponse) throws IOException;

    /**
     * 小程序登录
     *
     * @param code code
     * @return com.aitool.dto.user.LoginAppUserInfo
     */
    LoginAppUserInfo appletLogin(String code);

    /**
     * 微信公众号登录
     *
     * @param message message
     * @return java.lang.String
     */
    String wechatLogin(WxMpXmlMessage message);

    /**
     * 获取滑块验证码
     *
     * @return com.aitool.dto.Captcha
     */
    Captcha getCaptcha();
    
    /**
     * Google登录
     *
     * @param idToken Google身份令牌
     * @return 登录用户信息
     */
    LoginAppUserInfo googleLogin(String idToken);
}