package com.hzmc.upgrade.spring.boot.starter.manager;

import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;

/**
 * upgrade-spring-boot
 * 2021/4/23 10:43
 * 组件管理器
 *
 * @author lanhaifeng
 * @since
 **/
public interface ComponentManager {

	/**
	 * 2021/4/23 11:01
	 * 执行升级
	 *
	 * @param config
	 * @author lanhaifeng
	 * @return void
	 */
	void doUpgrade(ComponentUpgradeConfig config);
}
