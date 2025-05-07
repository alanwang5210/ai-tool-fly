package com.aitool.service;

import com.aitool.entity.VipMembership;

public interface VipMembershipService {
    // 订阅会员
    void subscribeVip(Long userId, String level);
    
    // 获取会员状态
    VipMembership getVipStatus(Long userId);
    
    // 获取功能价格（考虑会员折扣）
    Integer getFunctionPrice(Long userId, String functionCode);
    
    // 检查会员是否过期
    boolean isVipExpired(Long userId);
    
    // 获取会员每日赠送点数
    Integer getDailyPoints(Long userId);
} 