package com.aitool.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("vip_membership")
public class VipMembership {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String level; // MONTHLY, YEARLY
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private Integer dailyPoints;
    
    private Integer freeUsageLimit;
    
    private Double discount;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 