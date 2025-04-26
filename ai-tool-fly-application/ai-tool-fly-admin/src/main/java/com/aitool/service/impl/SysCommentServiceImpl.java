package com.aitool.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aitool.entity.SysComment;
import com.aitool.mapper.SysCommentMapper;
import com.aitool.service.SysCommentService;
import com.aitool.utils.PageUtil;
import com.aitool.vo.comment.SysCommentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author quequnlong
 * @date 2025/1/2
 * @description
 */
@Service
@RequiredArgsConstructor
public class SysCommentServiceImpl extends ServiceImpl<SysCommentMapper,SysComment> implements SysCommentService {

    @Override
    public Page<SysCommentVO> selectList() {
        return baseMapper.selectPage(PageUtil.getPage());
    }
}
