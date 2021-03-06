package com.hzmc.upgrade.spring.boot.starter.init;

import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * upgrade-spring-boot
 * 2021/4/20 16:48
 * 初始化所需环境
 *
 * @author lanhaifeng
 * @since
 **/
public interface InitEnvironment {

	static Logger logger = LoggerFactory.getLogger(InitEnvironment.class);

	/**
	 * 2021/4/21 14:21
	 * 初始化
	 *
	 * @param config
	 * @author lanhaifeng
	 * @return void
	 */
	void init(ComponentUpgradeConfig config);
}
