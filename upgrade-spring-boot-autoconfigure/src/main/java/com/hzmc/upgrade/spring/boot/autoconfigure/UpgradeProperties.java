package com.hzmc.upgrade.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * upgrade-spring-boot
 * 2021/4/18 23:52
 * 描述一下类的用途
 *
 * @author lanhaifeng
 * @since
 **/
@ConfigurationProperties(prefix = UpgradeProperties.UPGRADE_PREFIX)
public class UpgradeProperties {

    public static final String UPGRADE_PREFIX = "UPGRADE";


}
