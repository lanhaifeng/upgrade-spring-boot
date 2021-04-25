package com.hzmc.upgrade.spring.boot.starter.processor;

import com.hzmc.upgrade.spring.boot.starter.domain.ComponentVersion;

/**
 * upgrade-spring-boot
 * 2021/4/21 15:38
 * 组件版本处理器
 *
 * @author lanhaifeng
 * @since
 **/
public interface ComponentVersionProcessor extends UpgradeProcessor {

	/**
	 * 2021/4/21 15:40
	 * 获取版本表名
	 *
	 * @param
	 * @author lanhaifeng
	 * @return java.lang.String
	 */
	String getVersionTableName();

	/**
	 * 2021/4/21 15:40
	 * 获取版本表初始化SQL
	 *
	 * @param
	 * @author lanhaifeng
	 * @return java.lang.String
	 */
	String getVersionInitTableSql();

	/**
	 * 2021/4/21 16:15
	 * 检测表是否存在
	 *
	 * @param tableName
	 * @author lanhaifeng
	 * @return java.lang.Boolean
	 */
	Boolean tableExist(String tableName);

	/**
	 * 2021/4/21 18:51
	 * 获取组件版本信息
	 *
	 * @param  componentName
	 * @author lanhaifeng
	 * @return com.hzmc.upgrade.spring.boot.starter.domain.ComponentVersion
	 */
	ComponentVersion getComponentVersion(String componentName);

	/**
	 * 2021/4/22 19:18
	 * 添加组件版本信息
	 *
	 * @param componentVersion
	 * @author lanhaifeng
	 * @return void
	 */
	void addComponentVersion(ComponentVersion componentVersion);

	/**
	 * 2021/4/23 14:23
	 * 跟新组件版本信息
	 *
	 * @param componentVersion
	 * @author lanhaifeng
	 * @return void
	 */
	void updateComponentVersion(ComponentVersion componentVersion);
}
