package com.aitool.controller;

import com.aitool.service.UserPointService;
import com.aitool.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/point")
public class UserPointController {
    
    @Autowired
    private UserPointService userPointService;
    
    @GetMapping("/balance")
    public Result<Integer> getPointBalance(@RequestParam Long userId) {
        return Result.success(userPointService.getPointBalance(userId));
    }
    
    @PostMapping("/signin")
    public Result<Void> signIn(@RequestParam Long userId) {
        userPointService.signIn(userId);
        return Result.success();
    }
    
    @PostMapping("/consume")
    public Result<Boolean> consumePoints(
            @RequestParam Long userId,
            @RequestParam String functionCode,
            @RequestParam Integer points) {
        boolean success = userPointService.consumePoints(userId, functionCode, points);
        return Result.success(success);
    }
    
    @PostMapping("/recharge")
    public Result<Void> rechargePoints(
            @RequestParam Long userId,
            @RequestParam Integer points,
            @RequestParam String orderNo) {
        userPointService.rechargePoints(userId, points, orderNo);
        return Result.success();
    }
} 