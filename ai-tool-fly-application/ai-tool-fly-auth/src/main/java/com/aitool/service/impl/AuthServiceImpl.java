package com.aitool.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.aitool.common.Constants;
import com.aitool.dto.Captcha;
import com.aitool.dto.LoginDTO;
import com.aitool.dto.user.LoginUserInfo;
import com.aitool.entity.SysConfig;
import com.aitool.mapper.SysConfigMapper;
import com.aitool.service.AuthService;
import com.aitool.entity.SysUser;
import com.aitool.enums.MenuTypeEnum;
import com.aitool.exception.ServiceException;
import com.aitool.mapper.SysMenuMapper;
import com.aitool.mapper.SysRoleMapper;
import com.aitool.mapper.SysUserMapper;
import com.aitool.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 10100
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {


    private final SysUserMapper userMapper;

    private final SysRoleMapper roleMapper;

    private final SysMenuMapper menuMapper;

    private final SysConfigMapper sysConfigMapper;


    public AuthServiceImpl(SysUserMapper userMapper, SysRoleMapper roleMapper, SysMenuMapper menuMapper, SysConfigMapper sysConfigMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.menuMapper = menuMapper;
        this.sysConfigMapper = sysConfigMapper;
    }

    @Override
    public LoginUserInfo login(LoginDTO loginDTO) {


        // 系统用户登录逻辑
        SysConfig verifySwitch = sysConfigMapper.selectOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, "slider_verify_switch"));
        if (verifySwitch != null && "Y".equals(verifySwitch.getConfigValue())) {
            //校验验证码
            CaptchaUtil.checkImageCode(loginDTO.getNonceStr(), loginDTO.getValue());
        }
        return loginSysUser(loginDTO);

    }

    /**
     * 系统用户登录
     */
    private LoginUserInfo loginSysUser(LoginDTO loginDTO) {
        // 查询系统用户
        SysUser user = userMapper.selectByUsername(loginDTO.getUsername());

        //校验是否能够登录
        validateLogin(loginDTO, user);

        // 执行登录
        StpUtil.login(user.getId());
        String tokenValue = StpUtil.getTokenValue();

        // 返回用户信息
        LoginUserInfo loginUserInfo = BeanCopyUtil.copyObj(user, LoginUserInfo.class);
        loginUserInfo.setToken(tokenValue);

        StpUtil.getSession().set(Constants.CURRENT_USER, loginUserInfo);
        return loginUserInfo;
    }

    private static void validateLogin(LoginDTO loginDTO, SysUser user) {
        if (user == null) {
            throw new ServiceException("登录用户不存在");
        }

        // 验证密码
        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            throw new ServiceException("用户名或密码错误");
        }

        // 验证状态
        if (user.getStatus() != 1) {
            throw new ServiceException("账号已被禁用");
        }

        if (user.getUsername().equals(Constants.TEST) && "PC".equalsIgnoreCase(loginDTO.getSource())) {
            throw new ServiceException("演示用户不允许门户登录！");
        }
    }

    @Override
    public LoginUserInfo getLoginUserInfo(String source) {
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        LoginUserInfo loginUserInfo = BeanCopyUtil.copyObj(user, LoginUserInfo.class);

        //获取菜单权限列表
        if ("admin".equalsIgnoreCase(source)) {
            List<String> permissions;
            List<String> roles = roleMapper.selectRolesCodeByUserId(userId);
            if (roles.contains(Constants.ADMIN)) {
                permissions = menuMapper.getPermissionList(MenuTypeEnum.BUTTON.getCode());
            } else {
                permissions = menuMapper.getPermissionListByUserId(userId, MenuTypeEnum.BUTTON.getCode());
            }
            loginUserInfo.setRoles(roles);
            loginUserInfo.setPermissions(permissions);
        }

        return loginUserInfo;
    }

    @Override
    public Captcha getCaptcha() {
        Captcha captcha = new Captcha();
        CaptchaUtil.getCaptcha(captcha);
        return captcha;
    }
}
