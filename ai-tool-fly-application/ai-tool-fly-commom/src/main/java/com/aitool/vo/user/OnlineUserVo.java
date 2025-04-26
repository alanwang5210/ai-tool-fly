package com.aitool.vo.user;

import com.aitool.entity.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author quequnlong
 * @date 2025/1/3
 * @description
 */
@Data
public class OnlineUserVo extends SysUser {

    @ApiModelProperty(value = "token")
    private String tokenValue;

}
