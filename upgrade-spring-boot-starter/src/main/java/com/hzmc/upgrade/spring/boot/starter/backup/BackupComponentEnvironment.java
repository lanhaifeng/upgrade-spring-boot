package com.hzmc.upgrade.spring.boot.starter.backup;

import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;
import com.hzmc.upgrade.spring.boot.starter.processor.ComponentVersionDelegate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

/**
 * upgrade-spring-boot
 * 2021/4/23 9:49
 * 备份环境
 *
 * @author lanhaifeng
 * @since
 **/
@Component
public class BackupComponentEnvironment implements BackupEnvironment {

	private ComponentVersionDelegate componentVersionDelegate;

	public BackupComponentEnvironment(ObjectProvider<ComponentVersionDelegate> componentVersionDelegateProvider) {
		this.componentVersionDelegate = componentVersionDelegateProvider.getIfAvailable();
	}

	@Override
	public void backup(ComponentUpgradeConfig config) {
		if(componentVersionDelegate.componentBackup(config)){
			componentVersionDelegate.backupTables(config);
		}
	}
}
