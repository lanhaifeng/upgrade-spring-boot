package com.hzmc.upgrade.spring.boot.autoconfigure.domain;

import com.baseframework.utils.validate.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hzmc.upgrade.spring.boot.autoconfigure.constants.UpgradeConstant;
import com.hzmc.upgrade.spring.boot.autoconfigure.enums.UpgradeDialect;
import com.hzmc.upgrade.spring.boot.autoconfigure.provider.ResourceProvider;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
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
	@NotEmpty(message = "当前版本为空")
	@Pattern(regexp = "[0-9.]", message = "文件路径非法")
	private String currentVersion;
	//备份表
	@Pattern(regexp = "[a-zA-Z0-9.$_]", message = "备份表非法")
	private String backupTable;

	//备份表
	@NotEmpty(message = "备份表后缀")
	@Pattern(regexp = "[a-zA-Z_]", message = "备份表后缀")
	private String backupTableSuffix;

	//数据源信息
	private UpgradeDataSource upgradeDataSource;

	@JsonIgnore
	@NotNull(message = "数据源为空")
	private DataSource dataSource;

	@JsonIgnore
	private List<Resource> upgradeResources = new ArrayList<>();

	@JsonIgnore
	private List<String> backupTables = new ArrayList<>();

	@JsonIgnore
	public Boolean canUse(){
		return Objects.nonNull(getUpgradeDataSource()) && getUpgradeDataSource().canUse();
	}

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

	public void initDefault(){
		if(StringUtils.isBlank(getUpgradeFileSuffix())){
			setUpgradeFileSuffix(UpgradeConstant.DEFAULT_FILE_SUFFIX);
		}
		if(StringUtils.isBlank(getDialect())){
			setDialect(UpgradeConstant.DEFAULT_DB_DIALECT);
		}
		if(StringUtils.isBlank(getCurrentVersion())){
			setCurrentVersion(UpgradeConstant.DEFAULT_CURRENT_VERSION);
		}
		if(StringUtils.isBlank(getBackupTableSuffix())){
			setBackupTableSuffix(UpgradeConstant.DEFAULT_BACKUP_TABLE_SUFFIX);
		}
		if (!getBackupTableSuffix().startsWith(UpgradeConstant.BACKUP_TABLE_SUFFIX_SEPARATOR)) {
			setBackupTableSuffix(UpgradeConstant.BACKUP_TABLE_SUFFIX_SEPARATOR + getBackupTableSuffix());
		}
		if (StringUtils.isBlank(getUpgradeFilePath())) {
			setUpgradeFilePath(String.format(UpgradeConstant.DEFAULT_FILE_PATH_TEMPLATE, getComponentName()));
		}
	}

	public static ComponentUpgradeConfig loadBySpring(ApplicationContext applicationContext,
													  Properties properties, ResourceProvider[] resourceProviders
			, DataSource dataSource){
		ComponentUpgradeConfig config = new ComponentUpgradeConfig();
		try {
			config.setUpgradeFileSuffix(UpgradeConstant.DEFAULT_FILE_SUFFIX);
			config.setDialect(UpgradeConstant.DEFAULT_DB_DIALECT);
			config.setCurrentVersion(UpgradeConstant.DEFAULT_CURRENT_VERSION);
			config.setBackupTableSuffix(UpgradeConstant.DEFAULT_BACKUP_TABLE_SUFFIX);
			config.setDataSource(dataSource);

			RelaxedDataBinder binder = new RelaxedDataBinder(config, UpgradeConstant.PROPERTIES_KEY_PRE);
			PropertyValues pvs = new MutablePropertyValues(properties);
			binder.bind(pvs);

			if (!config.getBackupTableSuffix().startsWith(UpgradeConstant.BACKUP_TABLE_SUFFIX_SEPARATOR)) {
				config.setBackupTableSuffix(UpgradeConstant.BACKUP_TABLE_SUFFIX_SEPARATOR + config.getBackupTableSuffix());
			}
			if (StringUtils.isBlank(config.getUpgradeFilePath())) {
				config.setUpgradeFilePath(String.format(UpgradeConstant.DEFAULT_FILE_PATH_TEMPLATE, config.getComponentName()));
			}
			if(config.canUse()){
				if(StringUtils.isNotBlank(config.getUpgradeDataSource().getRef())
						&& applicationContext.containsBean(config.getUpgradeDataSource().getRef())){
					config.setDataSource(applicationContext.getBean(config.getUpgradeDataSource().getRef(),
							DataSource.class));
				} else {
					config.setDataSource(config.getUpgradeDataSource().buildDataSource());
				}
			}

			config.initUpgradeResources(resourceProviders);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

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

	public UpgradeDataSource getUpgradeDataSource() {
		return upgradeDataSource;
	}

	public void setUpgradeDataSource(UpgradeDataSource upgradeDataSource) {
		this.upgradeDataSource = upgradeDataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
