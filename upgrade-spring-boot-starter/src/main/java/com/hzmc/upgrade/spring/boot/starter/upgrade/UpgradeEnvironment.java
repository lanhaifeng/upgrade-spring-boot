package com.hzmc.upgrade.spring.boot.starter.upgrade;

import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * upgrade-spring-boot
 * 2021/4/20 16:48
 * 环境升级
 *
 * @author lanhaifeng
 * @since
 **/
public interface UpgradeEnvironment {

	static Logger logger = LoggerFactory.getLogger(UpgradeEnvironment.class);

	/**
	 * 2021/4/21 14:21
	 * 升级
	 *
	 * @param  config
	 * @author lanhaifeng
	 * @return void
	 */
	void upgrade(ComponentUpgradeConfig config);
}
