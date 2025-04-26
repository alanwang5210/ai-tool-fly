package com.aitool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aitool.entity.SysWebConfig;

public interface SysWebConfigService extends IService<SysWebConfig> {

    void update(SysWebConfig sysWebConfig);
}
