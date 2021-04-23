package com.hzmc.upgrade.spring.boot.starter.processor;

import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;
import com.hzmc.upgrade.spring.boot.starter.domain.ComponentVersion;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * upgrade-spring-boot
 * 2021/4/21 15:45
 * 版本信息委托类
 *
 * @author lanhaifeng
 * @since
 **/
@Component
public class ComponentVersionDelegate {

	private static Logger logger = LoggerFactory.getLogger(ComponentVersionDelegate.class);
	private ComponentVersionProcessor componentVersionProcessor;

	public ComponentVersionDelegate(ObjectProvider<ComponentVersionProcessor> componentVersionProcessorProvider) {
		componentVersionProcessor = componentVersionProcessorProvider.getIfAvailable();
	}

	public Boolean componentVersionExist(String componentName){
		boolean tableExist = componentVersionProcessor.tableExist(componentVersionProcessor.getVersionTableName());
		if(!tableExist || StringUtils.isBlank(componentName)){
			return false;
		}
		ComponentVersion componentVersion = componentVersionProcessor.getComponentVersion(componentName);
		return Objects.nonNull(componentVersion) && componentVersion.notEmpty();
	}

	public Boolean componentUpgrade(ComponentUpgradeConfig config){
		ComponentVersion componentVersion = componentVersionProcessor.getComponentVersion(config.getComponentName());

		return Objects.nonNull(componentVersion) &&
				componentVersion.getVersion().compareTo(config.getCurrentVersion()) < 0;
	}

	public String currentComponentVersion(String componentName){
		ComponentVersion componentVersion = componentVersionProcessor.getComponentVersion(componentName);
		return Objects.nonNull(componentVersion) ? componentVersion.getVersion() : null;
	}

	public void initVersionTable(){
		if(!componentVersionProcessor.tableExist(componentVersionProcessor.getVersionTableName())){
			componentVersionProcessor.executeSql(componentVersionProcessor.getVersionInitTableSql());
		}
	}

	public void addComponentVersion(ComponentVersion componentVersion){
		componentVersionProcessor.addComponentVersion(componentVersion);
	}

	public void updateComponentVersion(ComponentVersion componentVersion){
		componentVersionProcessor.updateComponentVersion(componentVersion);
	}

	public void executeSql(@NotEmpty String sql){
		if(StringUtils.isNotBlank(sql)){
			componentVersionProcessor.executeSql(sql);
		}
	}

	public void executeSqlFile(@NotEmpty String sqlFileContent){
		if(StringUtils.isNotBlank(sqlFileContent)){
			componentVersionProcessor.executeSqlFile(sqlFileContent);
		}
	}

	public void backupTables(ComponentUpgradeConfig config){
		if(Objects.isNull(config) || Objects.isNull(config.getBackupTables())){
			return;
		}
		config.getBackupTables().forEach(tableName->{
			String backupTable = tableName + config.getBackupTableSuffix();
			if(componentVersionProcessor.tableExist(backupTable)){
				logger.info("备份表{}已存在，删除备份表{}", backupTable, backupTable);
				componentVersionProcessor.dropTable(backupTable);
			}
			logger.info("开始备份表{}到{}", tableName, backupTable);
			componentVersionProcessor.backupTable(tableName, backupTable);
		});
	}
}
