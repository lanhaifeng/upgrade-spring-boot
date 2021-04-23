package com.hzmc.upgrade.spring.boot.starter.manager;

import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;
import com.hzmc.upgrade.spring.boot.starter.backup.BackupEnvironment;
import com.hzmc.upgrade.spring.boot.starter.init.InitEnvironment;
import com.hzmc.upgrade.spring.boot.starter.upgrade.UpgradeComponentEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * upgrade-spring-boot
 * 2021/4/23 10:46
 * jdbc管理器
 *
 * @author lanhaifeng
 * @since
 **/
@Component
public class UpgradeManager implements ComponentManager {

	private static Logger logger = LoggerFactory.getLogger(UpgradeManager.class);

	private BackupEnvironment[] backupEnvironments;
	private InitEnvironment[] initEnvironments;
	private UpgradeComponentEnvironment[] upgradeComponentEnvironments;

	public UpgradeManager(ObjectProvider<BackupEnvironment[]> backupEnvironmentProvider,
						  ObjectProvider<InitEnvironment[]> initEnvironmentProvider,
						  ObjectProvider<UpgradeComponentEnvironment[]> upgradeComponentEnvironmentProvider) {
		this.backupEnvironments = backupEnvironmentProvider.getIfAvailable();
		this.initEnvironments = initEnvironmentProvider.getIfAvailable();
		this.upgradeComponentEnvironments = upgradeComponentEnvironmentProvider.getIfAvailable();
	}

	@Override
	public void doUpgrade(ComponentUpgradeConfig config) {
		//备份
		backup(config);
		//初始化
		init(config);
		//升级
		upgrade(config);
	}

	public void backup(ComponentUpgradeConfig config) {
		if(Objects.nonNull(backupEnvironments) && backupEnvironments.length > 0){
			logger.info("start backup");
			for (BackupEnvironment backupEnvironment : backupEnvironments) {
				backupEnvironment.backup(config);
			}
			logger.info("end backup");
		}
	}

	public void init(ComponentUpgradeConfig config) {
		if(Objects.nonNull(initEnvironments) && initEnvironments.length > 0){
			logger.info("start init");
			for (InitEnvironment initEnvironment : initEnvironments) {
				initEnvironment.init(config);
			}
			logger.info("end init");
		}
	}

	public void upgrade(ComponentUpgradeConfig config) {
		if(Objects.nonNull(upgradeComponentEnvironments) && upgradeComponentEnvironments.length > 0){
			logger.info("start upgrade");
			for (UpgradeComponentEnvironment upgradeComponentEnvironment : upgradeComponentEnvironments) {
				upgradeComponentEnvironment.upgrade(config);
			}
			logger.info("end upgrade");
		}
	}

}
