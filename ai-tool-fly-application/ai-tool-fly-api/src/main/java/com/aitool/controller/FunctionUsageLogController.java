package com.aitool.controller;

import com.aitool.common.Result;
import com.aitool.entity.FunctionUsageLog;
import com.aitool.model.FunctionUsageStats;
import com.aitool.service.FunctionUsageLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/function/usage")
public class FunctionUsageLogController {
    
    @Autowired
    private FunctionUsageLogService functionUsageLogService;
    
    @GetMapping("/today/{functionCode}")
    public Result<Integer> getTodayUsageCount(
            @RequestParam Long userId,
            @PathVariable String functionCode) {
        return Result.success(functionUsageLogService.getTodayUsageCount(userId, functionCode));
    }
    
    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<IPage<FunctionUsageLog>> pageLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String functionCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(functionUsageLogService.pageLogs(
            userId, functionCode, startTime, endTime, pageNum, pageSize));
    }
    
    @GetMapping("/stats/{functionCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<FunctionUsageStats> getUsageStats(
            @PathVariable String functionCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(functionUsageLogService.getUsageStats(
            functionCode, startTime, endTime));
    }
    
    @PostMapping("/clean")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> cleanExpiredLogs(@RequestParam(defaultValue = "30") int days) {
        functionUsageLogService.cleanExpiredLogs(days);
        return Result.success();
    }
} 