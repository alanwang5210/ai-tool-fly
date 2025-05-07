package com.aitool.service.impl;

import com.aitool.entity.VipMembership;
import com.aitool.entity.FunctionConfig;
import com.aitool.service.VipMembershipService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class VipMembershipServiceImpl extends ServiceImpl<VipMembershipMapper, VipMembership> implements VipMembershipService {
    
    @Autowired
    private FunctionConfigMapper functionConfigMapper;
    
    @Override
    @Transactional
    public void subscribeVip(Long userId, String level) {
        // 检查是否已有会员
        VipMembership existingVip = getOne(new QueryWrapper<VipMembership>().eq("user_id", userId));
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime;
        Integer dailyPoints;
        Integer freeUsageLimit;
        Double discount;
        
        // 设置会员参数
        if ("MONTHLY".equals(level)) {
            endTime = now.plusMonths(1);
            dailyPoints = 200;
            freeUsageLimit = 5;
            discount = 0.8;
        } else if ("YEARLY".equals(level)) {
            endTime = now.plusYears(1);
            dailyPoints = 300;
            freeUsageLimit = -1; // 不限次数
            discount = 0.7;
        } else {
            throw new BusinessException("无效的会员等级");
        }
        
        if (existingVip != null) {
            // 更新现有会员
            existingVip.setLevel(level);
            existingVip.setStartTime(now);
            existingVip.setEndTime(endTime);
            existingVip.setDailyPoints(dailyPoints);
            existingVip.setFreeUsageLimit(freeUsageLimit);
            existingVip.setDiscount(discount);
            existingVip.setUpdateTime(now);
            updateById(existingVip);
        } else {
            // 创建新会员
            VipMembership vip = new VipMembership();
            vip.setUserId(userId);
            vip.setLevel(level);
            vip.setStartTime(now);
            vip.setEndTime(endTime);
            vip.setDailyPoints(dailyPoints);
            vip.setFreeUsageLimit(freeUsageLimit);
            vip.setDiscount(discount);
            vip.setCreateTime(now);
            save(vip);
        }
    }
    
    @Override
    public VipMembership getVipStatus(Long userId) {
        return getOne(new QueryWrapper<VipMembership>()
            .eq("user_id", userId)
            .gt("end_time", LocalDateTime.now()));
    }
    
    @Override
    public Integer getFunctionPrice(Long userId, String functionCode) {
        // 获取功能配置
        FunctionConfig function = functionConfigMapper.selectOne(
            new QueryWrapper<FunctionConfig>().eq("function_code", functionCode)
        );
        
        if (function == null) {
            throw new BusinessException("功能不存在");
        }
        
        // 获取会员状态
        VipMembership vip = getVipStatus(userId);
        
        if (vip != null) {
            // 检查是否在免费次数内
            if (function.getIsVipFree() && 
                (vip.getFreeUsageLimit() == -1 || vip.getFreeUsageLimit() > 0)) {
                return 0;
            }
            // 应用会员折扣
            return (int) (function.getPointCost() * vip.getDiscount());
        }
        
        return function.getPointCost();
    }
    
    @Override
    public boolean isVipExpired(Long userId) {
        VipMembership vip = getVipStatus(userId);
        return vip == null;
    }
    
    @Override
    public Integer getDailyPoints(Long userId) {
        VipMembership vip = getVipStatus(userId);
        return vip != null ? vip.getDailyPoints() : 0;
    }
} 