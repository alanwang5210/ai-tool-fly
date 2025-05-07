package com.aitool.controller;

import com.aitool.common.Result;
import com.aitool.entity.FunctionConfig;
import com.aitool.service.FunctionConfigService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/function")
public class FunctionConfigController {
    
    @Autowired
    private FunctionConfigService functionConfigService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> createFunction(@RequestBody FunctionConfig function) {
        functionConfigService.createFunction(function);
        return Result.success();
    }
    
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateFunction(@RequestBody FunctionConfig function) {
        functionConfigService.updateFunction(function);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteFunction(@PathVariable Long id) {
        functionConfigService.deleteFunction(id);
        return Result.success();
    }
    
    @GetMapping("/{functionCode}")
    public Result<FunctionConfig> getFunction(@PathVariable String functionCode) {
        return Result.success(functionConfigService.getFunction(functionCode));
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<IPage<FunctionConfig>> pageFunctions(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(functionConfigService.pageFunctions(pageNum, pageSize, keyword));
    }
    
    @GetMapping("/enabled")
    public Result<List<FunctionConfig>> listEnabledFunctions() {
        return Result.success(functionConfigService.listEnabledFunctions());
    }
    
    @GetMapping("/check/{functionCode}")
    public Result<Boolean> checkPermission(
            @RequestParam Long userId,
            @PathVariable String functionCode) {
        return Result.success(functionConfigService.hasPermission(userId, functionCode));
    }
    
    @GetMapping("/categories")
    public Result<List<String>> listCategories() {
        return Result.success(functionConfigService.listCategories());
    }
    
    @GetMapping("/category/{category}")
    public Result<List<FunctionConfig>> listFunctionsByCategory(@PathVariable String category) {
        return Result.success(functionConfigService.listFunctionsByCategory(category));
    }
} 