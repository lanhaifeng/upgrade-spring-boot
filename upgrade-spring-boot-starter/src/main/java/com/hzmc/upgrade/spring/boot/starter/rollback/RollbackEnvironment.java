package com.hzmc.upgrade.spring.boot.starter.rollback;

import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * upgrade-spring-boot
 * 2021/4/23 9:47
 * 回滚环境
 *
 * @author lanhaifeng
 * @since
 **/
public interface RollbackEnvironment {

	static Logger logger = LoggerFactory.getLogger(RollbackEnvironment.class);

	/**
	 * 2021/4/21 14:21
	 * 回滚环境
	 *
	 * @param config
	 * @author lanhaifeng
	 * @return void
	 */
	void rollback(ComponentUpgradeConfig config);
}
