package com.hzmc.upgrade.spring.boot.starter.processor;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * upgrade-spring-boot
 * 2021/4/20 16:51
 * jdbc数据库处理器
 *
 * @author lanhaifeng
 * @since
 **/
public interface JdbcProcessor {

	/**
	 * 2021/4/20 17:01
	 * 验证表是否存在
	 *
	 * @param tableName
	 * @author lanhaifeng
	 * @return java.lang.Boolean
	 */
	Boolean tableExist(@NotEmpty String tableName);

	/**
	 * 2021/4/20 17:02
	 * 建表
	 *
	 * @param sql
	 * @author lanhaifeng
	 * @return java.lang.Boolean
	 */
	Boolean createTable(@NotEmpty String sql);

	/**
	 * 2021/4/20 17:03
	 * 执行sql
	 *
	 * @param sqlFile
	 * @author lanhaifeng
	 * @return java.lang.Boolean
	 */
	Boolean executeSql(@NotEmpty String sqlFile);
}
