package com.aitool.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.aitool.utils.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 第三方认证信息实体类
 *
 * @author 10100
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("app_user_third_auth")
@ApiModel(value = "第三方认证信息")
public class AppUserThirdAuth implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "第三方平台类型(google,wechat,github)")
    private String oauthType;

    @ApiModelProperty(value = "第三方平台用户ID")
    private String oauthId;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "访问令牌")
    private String accessToken;

    @ApiModelProperty(value = "刷新令牌")
    private String refreshToken;

    @ApiModelProperty(value = "令牌过期时间")
    private LocalDateTime expiresIn;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS, timezone = "GMT+8")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS, timezone = "GMT+8")
    private LocalDateTime updatedAt;
}