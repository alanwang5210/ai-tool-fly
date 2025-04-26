package com.aitool.service;

import com.aitool.entity.SysMoment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 说说 服务接口
 */
public interface SysMomentService extends IService<SysMoment> {
    /**
     * 查询说说分页列表
     */
    IPage<SysMoment> selectPage(SysMoment sysMoment);

    Object add(SysMoment sysMoment);

}
