package com.aitool.controller.monitor;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aitool.common.Result;
import com.aitool.service.CacheService;
import com.aitool.vo.cache.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author 10100
 */
@Api(tags = "缓存监控")
@RestController
@RequestMapping("/monitor/cache")
@RequiredArgsConstructor
public class CacheController {

    private final CacheService cacheService;

    @ApiOperation(value = "获取缓存信息")
    @GetMapping("/info")
    public Result<CacheInfoVo> getCacheInfo() {
        return Result.success(cacheService.getCacheInfo());
    }

    @ApiOperation(value = "获取内存信息")
    @GetMapping("/memory")
    public Result<CacheMemoryVo> getMemoryInfo() {
        return Result.success(cacheService.getMemoryInfo());
    }

    @ApiOperation(value = "获取缓存键列表")
    @GetMapping("/keys")
    public Result<IPage<CacheKeyVo>> getKeyList(CacheKeyQuery query) {
        return Result.success(cacheService.getKeyList(query));
    }

    @ApiOperation(value = "清空缓存")
    @DeleteMapping
    public Result<Void> clearCache() {
        cacheService.clearCache();
        return Result.success();
    }
}