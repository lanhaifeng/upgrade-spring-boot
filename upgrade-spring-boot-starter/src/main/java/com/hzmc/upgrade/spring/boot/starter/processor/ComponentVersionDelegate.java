package com.hzmc.upgrade.spring.boot.starter.processor;

import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;
import com.hzmc.upgrade.spring.boot.starter.domain.ComponentVersion;
import com.hzmc.upgrade.spring.boot.starter.facotry.ComponentVersionProcessorFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
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
	private Map<String, ComponentVersionProcessor> processorMap = new HashMap<>();

	public ComponentVersionProcessor getComponentVersionProcessor(ComponentUpgradeConfig config) {
		try{
			if(!processorMap.containsKey(config.getComponentName())){
				ComponentVersionProcessor processor = new ComponentVersionProcessorFactory(config).getObject();
				processorMap.put(config.getComponentName(), processor);
			}
			return processorMap.get(config.getComponentName());
		}catch(Exception e){
			logger.error("构造ComponentVersionProcessor错误：" + ExceptionUtils.getFullStackTrace(e));
			throw new RuntimeException(e.getMessage());
		}
	}

	public Boolean componentInit(ComponentUpgradeConfig config){
		ComponentVersionProcessor processor = getComponentVersionProcessor(config);
		boolean tableExist = processor.tableExist(processor.getVersionTableName());
		if(!tableExist){
			return true;
		}
		ComponentVersion componentVersion = processor.getComponentVersion(config.getComponentName());
		return Objects.isNull(componentVersion) || StringUtils.isBlank(componentVersion.getVersion());
	}

	public Boolean componentUpgrade(ComponentUpgradeConfig config){
		ComponentVersion componentVersion = getComponentVersionProcessor(config).getComponentVersion(config.getComponentName());

		return Objects.nonNull(componentVersion) &&
				componentVersion.getVersion().compareTo(config.getCurrentVersion()) < 0;
	}

	public Boolean componentBackup(ComponentUpgradeConfig config){
		if(config.getBackupTables().isEmpty()){
			return false;
		}

		return componentInit(config) || componentUpgrade(config);
	}

	public String currentComponentVersion(ComponentUpgradeConfig config){
		ComponentVersion componentVersion = getComponentVersionProcessor(config).getComponentVersion(config.getComponentName());
		return Objects.nonNull(componentVersion) ? componentVersion.getVersion() : null;
	}

	public void initVersionTable(ComponentUpgradeConfig config){
		ComponentVersionProcessor processor = getComponentVersionProcessor(config);
		if(!processor.tableExist(processor.getVersionTableName())){
			processor.executeSql(processor.getVersionInitTableSql());
		}
	}

	public void addComponentVersion(ComponentUpgradeConfig config, ComponentVersion componentVersion){
		getComponentVersionProcessor(config).addComponentVersion(componentVersion);
	}

	public void updateComponentVersion(ComponentUpgradeConfig config, ComponentVersion componentVersion){
		getComponentVersionProcessor(config).updateComponentVersion(componentVersion);
	}

	public void executeSql(ComponentUpgradeConfig config, @NotEmpty String sql){
		if(StringUtils.isNotBlank(sql)){
			getComponentVersionProcessor(config).executeSql(sql);
		}
	}

	public void executeSqlFile(ComponentUpgradeConfig config, @NotEmpty String sqlFileContent){
		if(StringUtils.isNotBlank(sqlFileContent)){
			getComponentVersionProcessor(config).executeSqlFile(sqlFileContent);
		}
	}

	public void backupTables(ComponentUpgradeConfig config){
		if(Objects.isNull(config) || Objects.isNull(config.getBackupTables())){
			return;
		}
		ComponentVersionProcessor processor = getComponentVersionProcessor(config);
		config.getBackupTables().forEach(tableName->{
			String backupTable = tableName + config.getBackupTableSuffix();
			if(!processor.tableExist(tableName)){
				logger.warn("待备份表{}不存在，不需备份", tableName);
				return;
			}
			if(processor.tableExist(backupTable)){
				logger.info("备份表{}已存在，删除备份表{}", backupTable, backupTable);
				processor.dropTable(backupTable);
			}
			logger.info("开始备份表{}到{}", tableName, backupTable);
			processor.backupTable(tableName, backupTable);
		});
	}
}
