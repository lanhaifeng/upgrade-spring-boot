package com.hzmc.upgrade.spring.boot.autoconfigure.constants;

/**
 * upgrade-spring-boot
 * 2021/4/21 11:22
 * 常量类
 *
 * @author lanhaifeng
 * @since
 **/
public class UpgradeConstant {

	//版本表名
	public static final String VERSION_TABLE_NAME = "COMPONENT_VERSION";

	//默认组件-升级文件后缀
	public static final String DEFAULT_FILE_SUFFIX = ".sql";
	//默认组件-升级文件路径
	public static final String DEFAULT_FILE_PATH_TEMPLATE = "classpath*:sql-script/%s/**.sql";
	//默认组件-持久化类型
	public static final String DEFAULT_DB_DIALECT = "MYSQL";
	//默认组件-版本
	public static final String DEFAULT_CURRENT_VERSION = "0.0.1";
	//默认组件-备份表后缀
	public static final String DEFAULT_BACKUP_TABLE_SUFFIX = "_backup";
	//备份表分隔符
	public static final String BACKUP_TABLE_SUFFIX_SEPARATOR = "_";

	//备份表字符串分隔符
	public static final String BACKUP_TABLE_STRING_SEPARATOR = ",";

	//properties文件中，key前缀
	public static final String PROPERTIES_KEY_PRE = "upgrade";
}
