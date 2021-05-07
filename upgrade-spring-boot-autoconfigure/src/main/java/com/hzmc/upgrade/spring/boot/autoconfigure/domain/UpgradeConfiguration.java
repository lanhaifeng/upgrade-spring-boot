package com.hzmc.upgrade.spring.boot.autoconfigure.domain;

import org.springframework.core.env.ConfigurableEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * upgrade-spring-boot
 * 2021/4/29 16:46
 * 组件升级配置类
 *
 * @author lanhaifeng
 * @since
 **/
public class UpgradeConfiguration {

	protected ConfigurableEnvironment environment;

	protected List<ComponentUpgradeConfig> componentUpgradeConfigs = new ArrayList<>();

	public ConfigurableEnvironment getEnvironment() {
		return environment;
	}

	public void setEnvironment(ConfigurableEnvironment environment) {
		this.environment = environment;
	}

	public List<ComponentUpgradeConfig> getComponentUpgradeConfigs() {
		return componentUpgradeConfigs;
	}

	public void setComponentUpgradeConfigs(List<ComponentUpgradeConfig> componentUpgradeConfigs) {
		this.componentUpgradeConfigs = componentUpgradeConfigs;
	}
}
