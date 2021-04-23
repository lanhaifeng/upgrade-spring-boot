package com.hzmc.upgrade.spring.boot.starter.domain;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * upgrade-spring-boot
 * 2021/4/21 14:38
 * 组件版本信息
 *
 * @author lanhaifeng
 * @since
 **/
public class ComponentVersion implements Serializable {

	//模块名
	@NotEmpty(message = "组件名不能为空")
	@Pattern(regexp = "[a-zA-Z-_]", message = "组件名应为英文字母、-、_组成")
	private String componentName;
	//版本信息
	@NotEmpty(message = "版本号不能为空")
	@Pattern(regexp = "[0-9.]", message = "模块名应为数字和.组成")
	private String version;
	//创建时间
	@NotNull(message = "创建时间不能为空")
	@Min(value = 1, message = "创建时间非法")
	private Long createTime;
	//修改时间
	@NotNull(message = "修改时间不能为空")
	@Min(value = 1, message = "修改时间非法")
	private Long updateTime;

	public ComponentVersion() {
	}

	public ComponentVersion(String componentName, String version, Long createTime, Long updateTime) {
		this.componentName = componentName;
		this.version = version;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public ComponentVersion(String componentName, String version, Long updateTime) {
		this.componentName = componentName;
		this.version = version;
		this.updateTime = updateTime;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Boolean notEmpty(){
		return StringUtils.isNotBlank(getComponentName()) && StringUtils.isNotBlank(getVersion());
	}
}
