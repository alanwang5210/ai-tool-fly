package com.aitool.service;

import com.aitool.dto.Captcha;
import com.aitool.dto.EmailRegisterDto;
import com.aitool.dto.LoginDTO;
import com.aitool.dto.user.LoginUserInfo;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.zhyd.oauth.model.AuthCallback;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 10100
 */
public interface AuthService {

    /**
     * 用户登录
     */
    LoginUserInfo login(LoginDTO loginDTO);

    /**
     * 获取当前登录用户信息
     */
    LoginUserInfo getLoginUserInfo(String source);

    /**
     * 获取滑块验证码
     *
     * @return com.aitool.dto.Captcha
     */
    Captcha getCaptcha();
}