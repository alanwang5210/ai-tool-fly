package com.aitool.controller.site;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.aitool.common.Result;
import com.aitool.entity.SysWebConfig;
import com.aitool.service.SysWebConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author 10100
 */
@RestController
@Api(tags = "网站配置管理")
@RequestMapping("/sys/web")
@RequiredArgsConstructor
public class SysWebConfigController {

    private final SysWebConfigService sysWebConfigService;

    @GetMapping("/config")
    @ApiOperation(value = "获取网站配置")
    public Result<SysWebConfig> getWebConfig() {
        return Result.success(sysWebConfigService.getOne(new LambdaQueryWrapper<SysWebConfig>().last("limit 1")));
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改网站配置")
    @SaCheckPermission("sys:web:update")
    public Result<Void> update(@RequestBody SysWebConfig sysWebConfig) {
        sysWebConfigService.update(sysWebConfig);
        return Result.success();
    }
}
