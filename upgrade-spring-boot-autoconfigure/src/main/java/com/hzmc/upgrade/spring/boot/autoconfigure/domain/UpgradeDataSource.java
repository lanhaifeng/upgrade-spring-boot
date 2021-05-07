package com.hzmc.upgrade.spring.boot.autoconfigure.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;

/**
 * upgrade-spring-boot
 * 2021/4/29 11:04
 * 升级数据源属性
 *
 * @author lanhaifeng
 * @since
 **/
public class UpgradeDataSource {
	private String username;
	private String password;
	private Class<? extends DataSource> type;
	private String driverClassName;
	private String url;

	private String ref;

	@JsonIgnore
	public Boolean canUse(){
		return StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)
				&& StringUtils.isNotBlank(url) || StringUtils.isNotBlank(getRef());
	}

	public DataSource buildDataSource(){
		DataSourceProperties dataSourceProperties = new DataSourceProperties();
		dataSourceProperties.setUsername(getUsername());
		dataSourceProperties.setPassword(getPassword());
		dataSourceProperties.setType(getType());
		dataSourceProperties.setDriverClassName(getDriverClassName());
		dataSourceProperties.setUrl(getUrl());

		return dataSourceProperties.initializeDataSourceBuilder().build();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Class<? extends DataSource> getType() {
		return type;
	}

	public void setType(Class<? extends DataSource> type) {
		this.type = type;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
}
