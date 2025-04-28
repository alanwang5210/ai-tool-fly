package com.aitool.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Google登录配置属性
 *
 * @author quequnlong
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "google")
public class GoogleConfigProperties {

    /**
     * appId
     */
    private String appId;

    /**
     * appSecret
     */
    private String appSecret;

    /**
     * 回调域名
     */
    private String redirectUrl;

}