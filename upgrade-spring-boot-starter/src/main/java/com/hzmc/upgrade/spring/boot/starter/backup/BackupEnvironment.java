package com.hzmc.upgrade.spring.boot.starter.backup;

import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * upgrade-spring-boot
 * 2021/4/23 9:47
 * 备份环境
 *
 * @author lanhaifeng
 * @since
 **/
public interface BackupEnvironment {

	static Logger logger = LoggerFactory.getLogger(BackupEnvironment.class);

	/**
	 * 2021/4/21 14:21
	 * 备份环境
	 *
	 * @param config
	 * @author lanhaifeng
	 * @return void
	 */
	void backup(ComponentUpgradeConfig config);
}
