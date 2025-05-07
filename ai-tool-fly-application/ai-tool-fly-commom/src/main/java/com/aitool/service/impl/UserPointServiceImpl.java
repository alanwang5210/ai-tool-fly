package com.aitool.service.impl;

import com.aitool.entity.UserPoint;
import com.aitool.entity.PointLog;
import com.aitool.entity.FunctionConfig;
import com.aitool.entity.VipMembership;
import com.aitool.service.UserPointService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class UserPointServiceImpl extends ServiceImpl<UserPointMapper, UserPoint> implements UserPointService {
    
    @Autowired
    private PointLogMapper pointLogMapper;
    
    @Autowired
    private FunctionConfigMapper functionConfigMapper;
    
    @Autowired
    private VipMembershipMapper vipMembershipMapper;
    
    @Override
    public Integer getPointBalance(Long userId) {
        UserPoint userPoint = getOne(new QueryWrapper<UserPoint>().eq("user_id", userId));
        return userPoint != null ? userPoint.getPointBalance() : 0;
    }
    
    @Override
    @Transactional
    public void signIn(Long userId) {
        // 检查今日是否已签到
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        PointLog lastSignIn = pointLogMapper.selectOne(
            new QueryWrapper<PointLog>()
                .eq("user_id", userId)
                .eq("type", "SIGN_IN")
                .ge("create_time", today)
        );
        
        if (lastSignIn != null) {
            throw new BusinessException("今日已签到");
        }
        
        // 签到赠送20点
        addPoints(userId, 20, "SIGN_IN", "每日签到奖励");
    }
    
    @Override
    @Transactional
    public boolean consumePoints(Long userId, String functionCode, Integer points) {
        // 检查用户是否为会员
        VipMembership vip = vipMembershipMapper.selectOne(
            new QueryWrapper<VipMembership>()
                .eq("user_id", userId)
                .gt("end_time", LocalDateTime.now())
        );
        
        // 获取功能配置
        FunctionConfig function = functionConfigMapper.selectOne(
            new QueryWrapper<FunctionConfig>().eq("function_code", functionCode)
        );
        
        if (function == null) {
            throw new BusinessException("功能不存在");
        }
        
        // 计算实际消耗点数
        Integer actualPoints = calculateActualPoints(function, vip);
        
        // 检查点数是否足够
        UserPoint userPoint = getOne(new QueryWrapper<UserPoint>().eq("user_id", userId));
        if (userPoint == null || userPoint.getPointBalance() < actualPoints) {
            return false;
        }
        
        // 扣除点数
        userPoint.setPointBalance(userPoint.getPointBalance() - actualPoints);
        updateById(userPoint);
        
        // 记录日志
        logPointChange(userId, -actualPoints, "CONSUME", 
            String.format("使用功能[%s]消耗%d点", function.getFunctionName(), actualPoints));
        
        return true;
    }
    
    @Override
    @Transactional
    public void rechargePoints(Long userId, Integer points, String orderNo) {
        addPoints(userId, points, "RECHARGE", "充值功能点，订单号：" + orderNo);
    }
    
    @Override
    @Transactional
    public void grantDailyPoints(Long userId, Integer points) {
        addPoints(userId, points, "VIP_GRANT", "会员每日赠送");
    }
    
    @Override
    public void logPointChange(Long userId, Integer points, String type, String description) {
        PointLog log = new PointLog();
        log.setUserId(userId);
        log.setPoints(points);
        log.setType(type);
        log.setDescription(description);
        log.setCreateTime(LocalDateTime.now());
        pointLogMapper.insert(log);
    }
    
    private void addPoints(Long userId, Integer points, String type, String description) {
        UserPoint userPoint = getOne(new QueryWrapper<UserPoint>().eq("user_id", userId));
        if (userPoint == null) {
            userPoint = new UserPoint();
            userPoint.setUserId(userId);
            userPoint.setPointBalance(points);
            userPoint.setCreateTime(LocalDateTime.now());
            save(userPoint);
        } else {
            userPoint.setPointBalance(userPoint.getPointBalance() + points);
            userPoint.setUpdateTime(LocalDateTime.now());
            updateById(userPoint);
        }
        
        logPointChange(userId, points, type, description);
    }
    
    private Integer calculateActualPoints(FunctionConfig function, VipMembership vip) {
        if (vip != null) {
            // 检查是否在免费次数内
            if (function.getIsVipFree() && function.getVipFreeLimit() > 0) {
                // TODO: 检查今日使用次数
                return 0;
            }
            // 应用会员折扣
            return (int) (function.getPointCost() * vip.getDiscount());
        }
        return function.getPointCost();
    }
} 