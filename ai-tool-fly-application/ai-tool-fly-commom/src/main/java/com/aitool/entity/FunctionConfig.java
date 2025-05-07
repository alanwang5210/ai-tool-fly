package com.aitool.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("function_config")
public class FunctionConfig {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String functionCode;
    
    private String functionName;
    
    private Integer pointCost;
    
    private Boolean isVipFree;
    
    private Integer vipFreeLimit;
    
    private String description;
    
    private Boolean enabled;
    
    private String requiredRole;
    
    private Integer dailyLimit;
    
    private Integer totalLimit;
    
    private String apiKey;
    
    private String apiEndpoint;
    
    private String version;
    
    private String category;
    
    private Integer priority;
    
    private Integer sort;
    
    private String apiPath;
    
    private String method;
    
    private String permission;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 