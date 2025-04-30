package com.aitool.controller.article;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aitool.common.Result;
import com.aitool.entity.SysCategory;
import com.aitool.service.SysCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类表 控制器
 *
 * @author 10100
 */
@RestController
@Api(tags = "分类管理")
@RequiredArgsConstructor
@RequestMapping("/sys/category")
public class SysCategoryController {

    private final SysCategoryService sysCategoryService;

    @GetMapping("/list")
    @ApiOperation(value = "分类列表")
    public Result<IPage<SysCategory>> list(SysCategory sysCategory) {
        return Result.success(sysCategoryService.selectPage(sysCategory));
    }

    @GetMapping("/all")
    @ApiOperation(value = "获取全部分类")
    public Result<List<SysCategory>> selectAllList() {
        return Result.success(sysCategoryService.selectList(null));
    }

    @PostMapping
    @ApiOperation(value = "新增分类")
    @SaCheckPermission("sys:category:add")
    public Result<Object> add(@RequestBody SysCategory sysCategory) {
        return Result.success(sysCategoryService.insert(sysCategory));
    }

    @PutMapping
    @ApiOperation(value = "修改分类")
    @SaCheckPermission("sys:category:update")
    public Result<Object> edit(@RequestBody SysCategory sysCategory) {
        return Result.success(sysCategoryService.update(sysCategory));
    }

    @DeleteMapping("/delete/{ids}")
    @ApiOperation(value = "删除分类")
    @SaCheckPermission("sys:category:delete")
    public Result<Object> remove(@PathVariable List<Integer> ids) {
        return Result.success(sysCategoryService.deleteByIds(ids));
    }
}
