package com.aitool.model;

import lombok.Data;

@Data
public class FunctionUsageStats {
    private String functionCode;
    
    private Long totalUsageCount;
    
    private Long uniqueUserCount;
    
    private Long totalPointsConsumed;
    
    private Long successCount;
    
    private Long failureCount;
    
    private Double averagePointsPerUsage;
    
    private Double successRate;
} 