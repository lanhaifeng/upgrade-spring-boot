package com.hzmc.upgrade.spring.boot.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

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

    public static final String UPGRADE_PREFIX = "upgrade";

    @Value("classpath*:**-upgrade.properties")
    private Resource[] configResources;

    public Resource[] getConfigResources() {
        return configResources;
    }
}
