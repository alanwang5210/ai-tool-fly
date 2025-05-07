package com.aitool.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("point_log")
public class PointLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Integer points;
    
    private String type; // SIGN_IN, CONSUME, RECHARGE, VIP_GRANT
    
    private String description;
    
    private LocalDateTime createTime;
} 