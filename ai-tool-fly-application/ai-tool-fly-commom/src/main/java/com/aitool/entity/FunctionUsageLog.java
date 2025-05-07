package com.aitool.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("function_usage_log")
public class FunctionUsageLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String functionCode;
    
    private Integer pointsConsumed;
    
    private String requestIp;
    
    private String userAgent;
    
    private String requestParams;
    
    private String responseResult;
    
    private Boolean success;
    
    private String errorMessage;
    
    private LocalDateTime createTime;
} 