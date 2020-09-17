package com.tzcpa.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


/**
 * 配置文件读取,直接注入bean使用
 */
@Configuration
@ConfigurationProperties(prefix = "config")
@PropertySource("classpath:conf/conf.properties")
@Data
public class ConfigUtil {
//	private String arkUrl;
//	private String swaggerHost;
//	private String	bucketName;
//
//	private String domain;
//	private String accessKey;
//	private String secretKey;
//
//	private String smsURL;


}
