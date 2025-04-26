package com.aitool.controller.friend;

import com.aitool.service.FriendService;
import com.aitool.common.Result;
import com.aitool.entity.SysFriend;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend")
@RequiredArgsConstructor
@Api(tags = "门户-友情链接管理")
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/list")
    @ApiOperation(value = "友情链接列表")
    public Result<List<SysFriend>> getFriendList() {
        return Result.success(friendService.getFriendList());
    }

    @PostMapping("/apply")
    @ApiOperation(value = "申请友链")
    public Result<Boolean> apply(@RequestBody SysFriend sysFriend) {
        return Result.success(friendService.apply(sysFriend));
    }
}
