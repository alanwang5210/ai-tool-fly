package com.aitool.service.impl;

import com.aitool.entity.FunctionUsageLog;
import com.aitool.model.FunctionUsageStats;
import com.aitool.service.FunctionUsageLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FunctionUsageLogServiceImpl extends ServiceImpl<FunctionUsageLogMapper, FunctionUsageLog> implements FunctionUsageLogService {
    
    @Override
    @Transactional
    public void logUsage(FunctionUsageLog log) {
        log.setCreateTime(LocalDateTime.now());
        save(log);
    }
    
    @Override
    public Integer getTodayUsageCount(Long userId, String functionCode) {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        return count(new QueryWrapper<FunctionUsageLog>()
            .eq("user_id", userId)
            .eq("function_code", functionCode)
            .ge("create_time", today));
    }
    
    @Override
    public IPage<FunctionUsageLog> pageLogs(Long userId, String functionCode,
            LocalDateTime startTime, LocalDateTime endTime,
            Integer pageNum, Integer pageSize) {
        QueryWrapper<FunctionUsageLog> wrapper = new QueryWrapper<>();
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }
        if (functionCode != null) {
            wrapper.eq("function_code", functionCode);
        }
        if (startTime != null) {
            wrapper.ge("create_time", startTime);
        }
        if (endTime != null) {
            wrapper.le("create_time", endTime);
        }
        wrapper.orderByDesc("create_time");
        return page(new Page<>(pageNum, pageSize), wrapper);
    }
    
    @Override
    public FunctionUsageStats getUsageStats(String functionCode,
            LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper<FunctionUsageLog> wrapper = new QueryWrapper<FunctionUsageLog>()
            .eq("function_code", functionCode)
            .ge("create_time", startTime)
            .le("create_time", endTime);
        
        List<FunctionUsageLog> logs = list(wrapper);
        
        FunctionUsageStats stats = new FunctionUsageStats();
        stats.setFunctionCode(functionCode);
        stats.setTotalUsageCount((long) logs.size());
        stats.setUniqueUserCount(logs.stream().map(FunctionUsageLog::getUserId).distinct().count());
        stats.setTotalPointsConsumed(logs.stream().mapToLong(FunctionUsageLog::getPointsConsumed).sum());
        stats.setSuccessCount(logs.stream().filter(FunctionUsageLog::getSuccess).count());
        stats.setFailureCount(logs.stream().filter(log -> !log.getSuccess()).count());
        
        if (stats.getTotalUsageCount() > 0) {
            stats.setAveragePointsPerUsage(
                (double) stats.getTotalPointsConsumed() / stats.getTotalUsageCount());
            stats.setSuccessRate(
                (double) stats.getSuccessCount() / stats.getTotalUsageCount());
        }
        
        return stats;
    }
    
    @Override
    @Transactional
    public void cleanExpiredLogs(int days) {
        LocalDateTime expireTime = LocalDateTime.now().minusDays(days);
        remove(new QueryWrapper<FunctionUsageLog>().lt("create_time", expireTime));
    }
} 