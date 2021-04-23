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


	public static final String COMPONENT_NAME_KEY = "upgrade.component-name";
	public static final String FILE_SUFFIX_KEY = "upgrade.file-suffix";
	public static final String FILE_PATH_KEY = "upgrade.file-path";
	public static final String DB_DIALECT_KEY = "upgrade.db-dialect";
	public static final String CURRENT_VERSION_KEY = "upgrade.current-version";
	public static final String BACKUP_TABLE_KEY = "upgrade.backup-table";
	public static final String BACKUP_TABLE_SUFFIX_KEY = "upgrade.backup-table-suffix";

}
