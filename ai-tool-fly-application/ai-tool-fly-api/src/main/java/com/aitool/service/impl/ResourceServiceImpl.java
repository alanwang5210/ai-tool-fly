package com.aitool.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aitool.common.RedisConstants;
import com.aitool.entity.SysResource;
import com.aitool.enums.ResourceStatusEnum;
import com.aitool.exception.ServiceException;
import com.aitool.mapper.SysResourceMapper;
import com.aitool.service.ResourceService;
import com.aitool.utils.PageUtil;
import com.aitool.utils.RedisUtil;
import com.aitool.vo.resource.SysResourceVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author quequnlong
 * @date 2025/3/12
 * @description
 */
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final SysResourceMapper baseMapper;

    private final RedisUtil redisUtil;

    @Override
    public Page<SysResourceVo> getResourceList(SysResource sysResource) {
        return baseMapper.getResourceList(PageUtil.getPage(),sysResource);
    }

    @Override
    public void add(SysResource sysResource) {
        sysResource.setUserId(StpUtil.getLoginIdAsLong());
        sysResource.setStatus(ResourceStatusEnum.AUDIT.getCode());
        baseMapper.insert(sysResource);
    }

    @Override
    public SysResource verify(String code,Long id) {
        String key = RedisConstants.CAPTCHA_CODE_KEY + code;
        if (!redisUtil.hasKey(key)) {
            throw new ServiceException("验证码错误");
        }
        redisUtil.delete(key);

        SysResource sysResource = baseMapper.selectById(id);

        sysResource.setDownloads(sysResource.getDownloads() + 1);
        baseMapper.updateById(sysResource);

        return sysResource;
    }
}
