package com.aitool.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aitool.vo.comment.CommentListVo;
import com.aitool.entity.SysComment;

public interface CommentService {

    /**
     * 获取评论列表
     * @param articleId
     * @return
     */

    IPage<CommentListVo> getComments(Integer articleId,String sortType);

    /**
     * 新增评论
     * @param sysComment
     * @return
     */
    void add(SysComment sysComment);
}
