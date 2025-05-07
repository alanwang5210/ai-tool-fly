package com.aitool.task;

import com.aitool.service.VipMembershipService;
import com.aitool.service.UserPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.aitool.entity.VipMembership;

@Component
public class VipDailyPointsTask {
    
    @Autowired
    private VipMembershipService vipMembershipService;
    
    @Autowired
    private UserPointService userPointService;
    
    @Scheduled(cron = "0 0 0 * * ?") // 每天凌晨执行
    public void grantDailyPoints() {
        // 获取所有有效的会员
        List<VipMembership> activeVips = vipMembershipService.list(
            new QueryWrapper<VipMembership>()
                .gt("end_time", LocalDateTime.now())
        );
        
        // 为每个会员发放每日赠送点数
        for (VipMembership vip : activeVips) {
            Integer dailyPoints = vip.getDailyPoints();
            if (dailyPoints > 0) {
                userPointService.grantDailyPoints(vip.getUserId(), dailyPoints);
            }
        }
    }
} 