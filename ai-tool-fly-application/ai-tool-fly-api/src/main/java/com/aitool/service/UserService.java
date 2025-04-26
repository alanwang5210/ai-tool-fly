package com.aitool.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aitool.entity.SysUser;
import com.aitool.vo.comment.CommentListVo;

import java.util.List;

/**
 * @author quequnlong
 * @date 2025/1/11
 * @description
 */
public interface UserService {

    /**
     * 查询我的评论
     *
     * @return
     */
    IPage<CommentListVo> selectMyComment();

    /**
     * 删除我的评论
     *
     * @param ids
     * @return
     */
    Void delMyComment(List<Long> ids);

    /**
     * 查询我的回复
     *
     * @return
     */
    IPage<CommentListVo> getMyReply();

    /**
     * 修改我的资料
     *
     * @param user
     */
    void updateProfile(SysUser user);

}
