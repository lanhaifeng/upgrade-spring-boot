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
public interface UpgradeProcessor {

	/**
	 * 2021/4/20 17:03
	 * 执行sql
	 *
	 * @param sql
	 * @author lanhaifeng
	 * @return java.lang.Boolean
	 */
	void executeSql(@NotEmpty String sql);

	/**
	 * 2021/4/20 17:03
	 * 执行sql
	 *
	 * @param sqls
	 * @author lanhaifeng
	 * @return java.lang.Boolean
	 */
	void executeSqls(@NotEmpty String[] sqls);

	/**
	 * 2021/4/22 16:11
	 * 执行sql文件
	 *
	 * @param sqlFileContent
	 * @author lanhaifeng
	 * @return void
	 */
	void executeSqlFile(@NotEmpty String sqlFileContent);

	/**
	 * 2021/4/23 14:24
	 * 删除表
	 *
	 * @param tableName
	 * @author lanhaifeng
	 * @return void
	 */
	void dropTable(@NotEmpty String tableName);

	/**
	 * 2021/4/23 14:24
	 * 备份表
	 *
	 * @param sourceTableName
	 * @param targetTableName
	 * @author lanhaifeng
	 * @return void
	 */
	void backupTable(@NotEmpty String sourceTableName, @NotEmpty String targetTableName);
}
