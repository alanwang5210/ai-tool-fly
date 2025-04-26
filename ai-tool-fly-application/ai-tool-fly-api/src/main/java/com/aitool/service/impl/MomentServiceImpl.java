package com.aitool.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aitool.mapper.SysMomentMapper;
import com.aitool.service.MomentService;
import com.aitool.utils.PageUtil;
import com.aitool.vo.moment.MomentPageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author quequnlong
 * @date 2025/2/5
 * @description
 */
@Service
@RequiredArgsConstructor
public class MomentServiceImpl implements MomentService {

    private final SysMomentMapper baseMapper;

    @Override
    public IPage<MomentPageVo> getMomentList() {
        return baseMapper.selectPage(PageUtil.getPage());
    }
}
