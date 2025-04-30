package com.aitool.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aitool.dto.user.SysUserAddAndUpdateDto;
import com.aitool.entity.SysUser;
import com.aitool.dto.user.UpdatePwdDTO;
import com.aitool.vo.user.OnlineUserVo;
import com.aitool.vo.user.SysUserVo;
import com.aitool.vo.user.SysUserProfileVo;

import java.util.List;

/**
 * @author 10100
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 分页查询用户
     */
    IPage<SysUserVo> listUsers(SysUser sysUser);

    /**
     * 新增用户
     */
    void add(SysUserAddAndUpdateDto user);

    /**
     * 更新用户
     */
    void update(SysUserAddAndUpdateDto user);

    /**
     * 删除用户
     */
    void delete(List<Integer> ids);


    /**
     * 修改密码
     *
     * @param updatePwdDTO 修改密码参数
     */
    void updatePwd(UpdatePwdDTO updatePwdDTO);

    /**
     * 获取个人信息
     *
     * @return com.aitool.vo.user.SysUserProfileVo
     * @author 10100
     * @date 2025-04-30 15:32
     **/
    SysUserProfileVo profile();

    /**
     * 修改个人信息
     *
     * @param user user
     */
    void updateProfile(SysUser user);

    /**
     * 锁屏界面验证密码
     *
     * @param password password
     * @return java.lang.Boolean
     */
    Boolean verifyPassword(String password);

    /**
     * 重置密码
     *
     * @param user user
     * @return java.lang.Boolean
     */
    Boolean resetPassword(SysUser user);

    /**
     * @param username username
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.aitool.vo.user.OnlineUserVo>
     * @author 10100
     * @date 2025-04-30 15:33
     **/
    IPage<OnlineUserVo> getOnlineUserList(String username);


}
