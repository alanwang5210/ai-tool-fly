package com.aitool.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aitool.entity.SysResource;
import com.aitool.vo.resource.SysResourceVo;

/**
 * @author quequnlong
 * @date 2025/3/12
 * @description
 */
public interface ResourceService {

    Page<SysResourceVo> getResourceList(SysResource sysResource);

    void add(SysResource sysResource);

    SysResource verify(String code,Long id);
}
