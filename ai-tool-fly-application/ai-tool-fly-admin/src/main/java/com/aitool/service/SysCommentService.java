package com.aitool.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aitool.entity.SysComment;
import com.aitool.vo.comment.SysCommentVO;

/**
 * @author quequnlong
 * @date 2025/1/2
 * @description
 */
public interface SysCommentService extends IService<SysComment> {

    /**
     * 获取评论列表
     * @return
     */
    Page<SysCommentVO> selectList();



}
