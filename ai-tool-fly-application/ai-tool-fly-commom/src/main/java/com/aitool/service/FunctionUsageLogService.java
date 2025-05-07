package com.aitool.service;

import com.aitool.entity.FunctionUsageLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.time.LocalDateTime;

public interface FunctionUsageLogService {
    // 记录功能使用
    void logUsage(FunctionUsageLog log);
    
    // 获取用户今日使用次数
    Integer getTodayUsageCount(Long userId, String functionCode);
    
    // 分页查询使用记录
    IPage<FunctionUsageLog> pageLogs(Long userId, String functionCode, 
            LocalDateTime startTime, LocalDateTime endTime,
            Integer pageNum, Integer pageSize);
    
    // 获取功能使用统计
    FunctionUsageStats getUsageStats(String functionCode, 
            LocalDateTime startTime, LocalDateTime endTime);
    
    // 清理过期日志
    void cleanExpiredLogs(int days);
} 