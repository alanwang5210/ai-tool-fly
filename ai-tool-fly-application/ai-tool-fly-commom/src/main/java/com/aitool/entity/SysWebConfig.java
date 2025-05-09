package com.aitool.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.aitool.utils.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * 网站配置表
 */
@Data
@TableName("sys_web_config")
@ApiModel(value = "网站配置表对象 gen_table")
public class SysWebConfig implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "logo(文件UID)")
    private String logo;

    @ApiModelProperty(value = "网站名称")
    private String name;

    @ApiModelProperty(value = "介绍")
    private String summary;

    @ApiModelProperty(value = "备案号")
    private String recordNum;

    @ApiModelProperty(value = "网站地址")
    private String webUrl;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "个性签名")
    private String authorInfo;

    @ApiModelProperty(value = "作者头像")
    private String authorAvatar;

    @ApiModelProperty(value = "支付宝收款码")
    private String aliPay;

    @ApiModelProperty(value = "微信收款码")
    private String weixinPay;

    @ApiModelProperty(value = "github地址")
    private String github;

    @ApiModelProperty(value = "gitee地址")
    private String gitee;

    @ApiModelProperty(value = "QQ号")
    private String qqNumber;

    @ApiModelProperty(value = "QQ群")
    private String qqGroup;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "微信")
    private String wechat;

    @ApiModelProperty(value = "显示的列表（用于控制邮箱、QQ、QQ群、Github、Gitee、微信是否显示在前端）")
    private String showList;

    @ApiModelProperty(value = "登录方式列表（用于控制前端登录方式，如账号密码,码云,Github,QQ,微信）")
    private String loginTypeList;

    @ApiModelProperty(value = "是否开启评论(0:否 1:是)")
    private Integer openComment;

    @ApiModelProperty(value = "是否开启赞赏(0:否， 1:是)")
    private Integer openAdmiration;

    @ApiModelProperty(value = "游客头像")
    private String touristAvatar;

    @ApiModelProperty(value = "公告")
    private String bulletin;

    @ApiModelProperty(value = "关于我")
    private String aboutMe;

    @ApiModelProperty(value = "是否开启灯笼")
    private Integer openLantern;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime updateTime;
}
