package com.aitool.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aitool.entity.SysComment;
import com.aitool.entity.SysUser;
import com.aitool.mapper.*;
import com.aitool.service.UserService;
import com.aitool.utils.PageUtil;
import com.aitool.vo.comment.CommentListVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author quequnlong
 * @date 2025/1/11
 * @description
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SysUserMapper sysUserMapper;

    private final SysCommentMapper commentMapper;

    @Override
    public IPage<CommentListVo> selectMyComment() {
        return commentMapper.selectMyComment(PageUtil.getPage(), StpUtil.getLoginIdAsLong());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Void delMyComment(List<Long> ids) {
        commentMapper.deleteBatchIds(ids);
        commentMapper.delete(new LambdaQueryWrapper<SysComment>()
                .in(SysComment::getParentId, ids));
        return null;
    }

    @Override
    public IPage<CommentListVo> getMyReply() {
        return commentMapper.getMyReply(PageUtil.getPage(), StpUtil.getLoginIdAsLong());
    }

    @Override
    public void updateProfile(SysUser user) {
        sysUserMapper.updateById(user);
    }

}
