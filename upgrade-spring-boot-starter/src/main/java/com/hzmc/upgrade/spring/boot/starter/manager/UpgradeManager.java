package com.hzmc.upgrade.spring.boot.starter.manager;

import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;
import com.hzmc.upgrade.spring.boot.starter.backup.BackupEnvironment;
import com.hzmc.upgrade.spring.boot.starter.init.InitEnvironment;
import com.hzmc.upgrade.spring.boot.starter.rollback.RollbackEnvironment;
import com.hzmc.upgrade.spring.boot.starter.upgrade.UpgradeEnvironment;
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
	private RollbackEnvironment[] rollbackEnvironments;
	private InitEnvironment[] initEnvironments;
	private UpgradeEnvironment[] upgradeEnvironments;

	public UpgradeManager(ObjectProvider<BackupEnvironment[]> backupEnvironmentProvider,
						  ObjectProvider<RollbackEnvironment[]> rollbackEnvironmentProvider,
						  ObjectProvider<InitEnvironment[]> initEnvironmentProvider,
						  ObjectProvider<UpgradeEnvironment[]> upgradeEnvironmentProvider) {
		this.backupEnvironments = backupEnvironmentProvider.getIfAvailable();
		this.rollbackEnvironments = rollbackEnvironmentProvider.getIfAvailable();
		this.initEnvironments = initEnvironmentProvider.getIfAvailable();
		this.upgradeEnvironments = upgradeEnvironmentProvider.getIfAvailable();
	}

	@Override
	public void doUpgrade(ComponentUpgradeConfig config) {
		//还原
		rollback(config);
		//备份
		backup(config);
		//初始化
		init(config);
		//升级
		upgrade(config);
	}

	public void rollback(ComponentUpgradeConfig config) {
		if(Objects.nonNull(rollbackEnvironments) && rollbackEnvironments.length > 0){
			logger.info("start rollback");
			for (RollbackEnvironment rollbackEnvironment : rollbackEnvironments) {
				rollbackEnvironment.rollback(config);
			}
			logger.info("end rollback");
		}
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
		if(Objects.nonNull(upgradeEnvironments) && upgradeEnvironments.length > 0){
			logger.info("start upgrade");
			for (UpgradeEnvironment upgradeEnvironment : upgradeEnvironments) {
				upgradeEnvironment.upgrade(config);
			}
			logger.info("end upgrade");
		}
	}

}
