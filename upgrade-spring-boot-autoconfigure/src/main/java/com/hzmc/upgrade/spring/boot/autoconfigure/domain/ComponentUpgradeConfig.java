package com.hzmc.upgrade.spring.boot.autoconfigure.domain;

import com.baseframework.utils.validate.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hzmc.upgrade.spring.boot.autoconfigure.constants.UpgradeConstant;
import com.hzmc.upgrade.spring.boot.autoconfigure.enums.UpgradeDialect;
import com.hzmc.upgrade.spring.boot.autoconfigure.provider.ResourceProvider;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.core.io.Resource;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * upgrade-spring-boot
 * 2021/4/22 18:39
 * 组件升级的配置信息
 *
 * @author lanhaifeng
 * @since
 **/
public class ComponentUpgradeConfig implements Serializable {

	//组件名
	@NotEmpty(message = "组件名为空")
	@Pattern(regexp = "[a-zA-Z]", message = "组件名非法")
	private String componentName;
	//升级文件后缀
	@NotEmpty(message = "文件后缀为空")
	@Pattern(regexp = ".[a-zA-Z]", message = "文件后缀非法")
	private String upgradeFileSuffix;
	//升级文件路径
	@NotEmpty(message = "文件路径为空")
	@Pattern(regexp = "[a-zA-Z/\\\\.]", message = "文件路径非法")
	private String upgradeFilePath;
	//方言，即持久化类型
	@Dict(source = UpgradeDialect.class, message = "方言非法")
	private String dialect;
	//当前版本
	@NotEmpty(message = "文件路径为空")
	@Pattern(regexp = "[0-9.]", message = "文件路径非法")
	private String currentVersion;
	//备份表
	@Pattern(regexp = "[a-zA-Z0-9.$_]", message = "备份表非法")
	private String backupTable;

	//备份表
	@NotEmpty(message = "备份表后缀")
	@Pattern(regexp = "[a-zA-Z_]", message = "备份表后缀")
	private String backupTableSuffix;

	@JsonIgnore
	private List<Resource> upgradeResources = new ArrayList<>();

	@JsonIgnore
	private List<String> backupTables = new ArrayList<>();

	private void initUpgradeResources(ResourceProvider[] resourceProviders) {
		if(Objects.nonNull(resourceProviders) && resourceProviders.length > 0){
			List<Resource> targets;
			upgradeResources.clear();
			for (ResourceProvider resourceProvider : resourceProviders) {
				targets = resourceProvider.scanResources(
						getUpgradeFilePath(), getUpgradeFileSuffix());
				if(Objects.nonNull(targets)){
					upgradeResources.addAll(targets);
				}
			}
		}
	}

	public List<Resource> getUpgradeResources() {
		return upgradeResources;
	}

	public List<String> getBackupTables() {
		return backupTables;
	}

	public static ComponentUpgradeConfig load(Properties properties, ResourceProvider[] resourceProviders){
		ComponentUpgradeConfig config = new ComponentUpgradeConfig();

		config.setComponentName(properties.getProperty(UpgradeConstant.COMPONENT_NAME_KEY));

		String upgradeFileSuffix = UpgradeConstant.DEFAULT_FILE_SUFFIX;
		if(properties.containsKey(UpgradeConstant.FILE_SUFFIX_KEY)){
			upgradeFileSuffix = properties.getProperty(UpgradeConstant.FILE_SUFFIX_KEY);
		}
		config.setUpgradeFileSuffix(upgradeFileSuffix);

		String upgradeFilePath = String.format(UpgradeConstant.DEFAULT_FILE_PATH_TEMPLATE, config.getComponentName());
		if(properties.containsKey(UpgradeConstant.FILE_PATH_KEY)){
			upgradeFilePath = properties.getProperty(UpgradeConstant.FILE_PATH_KEY);
		}
		config.setUpgradeFilePath(upgradeFilePath);

		String dialect = UpgradeConstant.DEFAULT_DB_DIALECT;
		if(properties.containsKey(UpgradeConstant.DB_DIALECT_KEY)){
			dialect = properties.getProperty(UpgradeConstant.DB_DIALECT_KEY);
		}
		config.setDialect(dialect);

		String currentVersion = UpgradeConstant.DEFAULT_CURRENT_VERSION;
		if(properties.containsKey(UpgradeConstant.CURRENT_VERSION_KEY)){
			currentVersion = properties.getProperty(UpgradeConstant.CURRENT_VERSION_KEY);
		}
		config.setCurrentVersion(currentVersion);

		if(properties.containsKey(UpgradeConstant.BACKUP_TABLE_KEY)){
			config.setBackupTable(properties.getProperty(UpgradeConstant.BACKUP_TABLE_KEY));
		}

		String backupTableSuffix = UpgradeConstant.DEFAULT_BACKUP_TABLE_SUFFIX;
		if(properties.containsKey(UpgradeConstant.BACKUP_TABLE_SUFFIX_KEY)){
			backupTableSuffix = properties.getProperty(UpgradeConstant.BACKUP_TABLE_SUFFIX_KEY);
			if(!backupTableSuffix.startsWith(UpgradeConstant.BACKUP_TABLE_SUFFIX_SEPARATOR)){
				backupTableSuffix = UpgradeConstant.BACKUP_TABLE_SUFFIX_SEPARATOR + backupTableSuffix;
			}
		}
		config.setBackupTableSuffix(backupTableSuffix);

		config.initUpgradeResources(resourceProviders);

		return config;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getUpgradeFileSuffix() {
		return upgradeFileSuffix;
	}

	public void setUpgradeFileSuffix(String upgradeFileSuffix) {
		this.upgradeFileSuffix = upgradeFileSuffix;
	}

	public String getUpgradeFilePath() {
		return upgradeFilePath;
	}

	public void setUpgradeFilePath(String upgradeFilePath) {
		this.upgradeFilePath = upgradeFilePath;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public String getBackupTable() {
		return backupTable;
	}

	public void setBackupTable(String backupTable) {
		this.backupTable = backupTable;
		if(StringUtils.isNotBlank(backupTable)){
			String[] tables = backupTable.trim().split(UpgradeConstant.BACKUP_TABLE_STRING_SEPARATOR);
			backupTables.addAll(
					Arrays.stream(tables).filter(
							t-> StringUtils.isNotBlank(t.trim())).collect(Collectors.toList()));
		}
	}

	public String getBackupTableSuffix() {
		return backupTableSuffix;
	}

	public void setBackupTableSuffix(String backupTableSuffix) {
		this.backupTableSuffix = backupTableSuffix;
	}

}
