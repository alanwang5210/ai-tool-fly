package com.aitool.controller;

import com.aitool.service.VipMembershipService;
import com.aitool.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vip")
public class VipController {
    
    @Autowired
    private VipMembershipService vipMembershipService;
    
    @PostMapping("/subscribe")
    public Result<Void> subscribeVip(
            @RequestParam Long userId,
            @RequestParam String level) {
        vipMembershipService.subscribeVip(userId, level);
        return Result.success();
    }
    
    @GetMapping("/status")
    public Result<VipMembership> getVipStatus(@RequestParam Long userId) {
        return Result.success(vipMembershipService.getVipStatus(userId));
    }
    
    @GetMapping("/function/price")
    public Result<Integer> getFunctionPrice(
            @RequestParam Long userId,
            @RequestParam String functionCode) {
        return Result.success(vipMembershipService.getFunctionPrice(userId, functionCode));
    }
} 