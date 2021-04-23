package com.hzmc.upgrade.spring.boot.starter.init;

import com.baseframework.utils.util.FileUtil;
import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;
import com.hzmc.upgrade.spring.boot.starter.domain.ComponentVersion;
import com.hzmc.upgrade.spring.boot.starter.processor.ComponentVersionDelegate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * upgrade-spring-boot
 * 2021/4/21 14:22
 * 初始化版本表
 *
 * @author lanhaifeng
 * @since
 **/
@Component
public class InitComponentVersion implements InitEnvironment {

	private ComponentVersionDelegate componentVersionDelegate;

	public InitComponentVersion(ObjectProvider<ComponentVersionDelegate> componentVersionDelegateProvider) {
		this.componentVersionDelegate = componentVersionDelegateProvider.getIfAvailable();
	}

	@Override
	public void init(ComponentUpgradeConfig config) {
		if(componentVersionDelegate.componentVersionExist(config.getComponentName())
				|| StringUtils.isNotBlank(componentVersionDelegate.currentComponentVersion(config.getComponentName()))){
			return;
		}
		//初始化版本表
		componentVersionDelegate.initVersionTable();
		//执行初始化操作
		try{
			initVersion(config);
		}catch(Exception e){
			logger.error("执行初始化失败，错误:" + ExceptionUtils.getFullStackTrace(e));
		}
	}


	private void initVersion(ComponentUpgradeConfig config) throws IOException {
		List<Resource> resources = config.getUpgradeResources();
		StringBuffer stringBuffer = new StringBuffer();
		for (Resource resource : resources) {
			FileUtil.readToBuffer(stringBuffer, resource.getInputStream());
			componentVersionDelegate.executeSqlFile(stringBuffer.toString());
			stringBuffer.setLength(0);
			if(!config.isNeedBackup()){
				config.setNeedBackup(true);
			}
		}

		String version = config.getCurrentVersion();
		if(resources.size() > 0){
			String fileName = FileUtil.getFileName(resources.get(resources.size() - 1).getFilename());
			String fileVersion = fileName.substring(0, fileName.lastIndexOf(config.getUpgradeFileSuffix()));
			if(fileVersion.compareTo(version) > 0){
				version = fileVersion;
			}
		}

		ComponentVersion componentVersion = new ComponentVersion(
				config.getComponentName(), version, System.currentTimeMillis(), System.currentTimeMillis());
		componentVersionDelegate.addComponentVersion(componentVersion);
	}

}
