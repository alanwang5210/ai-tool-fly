package com.aitool.enums;

import lombok.Getter;

/**
 * 登录来源枚举
 *
 * @author alan
 * @date 2024/06/25
 */
@Getter
public enum UserTypeEnum {

    SYSTEM("system", "系统用户登录"),

    APP("app", "APP用户登录");

    private final String source;
    
    private final String desc;

    UserTypeEnum(String source, String desc) {
        this.source = source;
        this.desc = desc;
    }

    /**
     * 根据source获取枚举
     *
     * @param source 来源标识
     * @return 对应的枚举值，如果没有匹配项则返回null
     */
    public static UserTypeEnum getBySource(String source) {
        if (source == null) {
            return null;
        }

        for (UserTypeEnum type : values()) {
            if (type.getSource().equalsIgnoreCase(source)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 判断是否为APP用户登录
     *
     * @param source 来源标识
     * @return 是否为APP用户登录
     */
    public static boolean isAppUser(String source) {
        return APP.getSource().equalsIgnoreCase(source);
    }
}