package com.aitool.service;

import com.aitool.entity.UserPoint;
import com.aitool.entity.PointLog;

public interface UserPointService {
    // 查询用户功能点余额
    Integer getPointBalance(Long userId);
    
    // 签到获取功能点
    void signIn(Long userId);
    
    // 消费功能点
    boolean consumePoints(Long userId, String functionCode, Integer points);
    
    // 充值功能点
    void rechargePoints(Long userId, Integer points, String orderNo);
    
    // 会员每日赠送功能点
    void grantDailyPoints(Long userId, Integer points);
    
    // 记录功能点日志
    void logPointChange(Long userId, Integer points, String type, String description);
} 