package com.hzmc.upgrade.spring.boot.starter.upgrade;

import com.baseframework.utils.util.FileUtil;
import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;
import com.hzmc.upgrade.spring.boot.starter.domain.ComponentVersion;
import com.hzmc.upgrade.spring.boot.starter.processor.ComponentVersionDelegate;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * upgrade-spring-boot
 * 2021/4/21 18:47
 * 升级组件环境
 *
 * @author lanhaifeng
 * @since
 **/
@Component
public class UpgradeComponentEnvironment implements UpgradeEnvironment {

	private ComponentVersionDelegate componentVersionDelegate;

	public UpgradeComponentEnvironment(ObjectProvider<ComponentVersionDelegate> componentVersionDelegateProvider) {
		this.componentVersionDelegate = componentVersionDelegateProvider.getIfAvailable();
	}

	@Override
	@Transactional
	public void upgrade(ComponentUpgradeConfig config) {
		if(componentVersionDelegate.componentUpgrade(config)){
			try {
				upgradeEnvironment(config);
			} catch (IOException e) {
				logger.error("执行升级失败，错误:" + ExceptionUtils.getFullStackTrace(e));
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	private void upgradeEnvironment(ComponentUpgradeConfig config) throws IOException {
		List<Resource> resources = config.getUpgradeResources();
		String lastVersion = componentVersionDelegate.currentComponentVersion(config.getComponentName());
		String fileVersion;
		StringBuffer stringBuffer = new StringBuffer();
		for (Resource resource : resources) {
			fileVersion = FileUtil.getFileName(resources.get(resources.size() - 1).getFilename());
			fileVersion = fileVersion.substring(0, fileVersion.lastIndexOf(config.getUpgradeFileSuffix()));
			if(fileVersion.compareTo(lastVersion) > 0 && config.getCurrentVersion().compareTo(fileVersion) >= 0){
				FileUtil.readToBuffer(stringBuffer, resource.getInputStream());
				componentVersionDelegate.executeSqlFile(stringBuffer.toString());
				stringBuffer.setLength(0);
				if(!config.isNeedBackup()){
					config.setNeedBackup(true);
				}
			}
		}
		ComponentVersion componentVersion = new ComponentVersion(
				config.getComponentName(), config.getCurrentVersion(), System.currentTimeMillis());
		componentVersionDelegate.updateComponentVersion(componentVersion);
	}
}
