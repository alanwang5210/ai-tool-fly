package com.aitool.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aitool.vo.moment.MomentPageVo;

/**
 * @author quequnlong
 * @date 2025/2/5
 * @description
 */
public interface MomentService {
    IPage<MomentPageVo> getMomentList();

}
