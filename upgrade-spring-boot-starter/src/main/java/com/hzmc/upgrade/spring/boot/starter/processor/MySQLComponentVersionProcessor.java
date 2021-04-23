package com.hzmc.upgrade.spring.boot.starter.processor;

import com.hzmc.upgrade.spring.boot.autoconfigure.constants.UpgradeConstant;
import com.hzmc.upgrade.spring.boot.starter.domain.ComponentVersion;
import com.hzmc.upgrade.spring.boot.starter.mybatis.ComponentVersionMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * upgrade-spring-boot
 * 2021/4/21 15:41
 * mysql 返回版本表信息
 *
 * @author lanhaifeng
 * @since
 **/
@ConditionalOnProperty(prefix = "upgrade.db", name = "dialect", havingValue = "MYSQL", matchIfMissing = true)
@Component
public class MySQLComponentVersionProcessor implements ComponentVersionProcessor {

	private static final String VERSION_TABLE_SQL = "CREATE TABLE `" + UpgradeConstant.VERSION_TABLE_NAME + "`\n" +
			"(\n" +
			"  `component_name`    VARCHAR(200)  NOT NULL PRIMARY KEY,\n" +
			"  `version`  		VARCHAR(300) NOT NULL,\n" +
			"  `create_time` BIGINT             NOT NULL,\n" +
			"  `update_time`    BIGINT          NOT NULL\n" +
			") ENGINE = InnoDB DEFAULT CHARSET=utf8;";

	private static final String DROP_TABLE_SQL_TEMPLATE = "DROP TABLE %s";

	private static final String BACKUP_TABLE_SQL_TEMPLATE = "CREATE TABLE %s AS SELECT * FROM %s";

	private static final String TABLE_EXIST_SQL = "show tables like '%s'";

	private JdbcTemplate jdbcTemplate;
	private ComponentVersionMapper componentVersionMapper;

	public MySQLComponentVersionProcessor(ObjectProvider<JdbcTemplate> jdbcTemplateProvider,
										  ObjectProvider<ComponentVersionMapper> componentVersionMapperProvider) {
		this.jdbcTemplate = jdbcTemplateProvider.getIfAvailable();
		this.componentVersionMapper = componentVersionMapperProvider.getIfAvailable();
	}

	@Override
	public String getVersionTableName() {
		return UpgradeConstant.VERSION_TABLE_NAME;
	}

	@Override
	public String getVersionInitTableSql() {
		return VERSION_TABLE_SQL;
	}

	@Override
	public String getVersionCheckTableSql(String tableName) {
		return String.format(TABLE_EXIST_SQL, tableName);
	}

	@Override
	public ComponentVersion getComponentVersion(String componentName) {
		return componentVersionMapper.getComponentVersion(componentName);
	}

	@Override
	public Boolean tableExist(String tableName) {
		List<String> targetTables = jdbcTemplate.query(getVersionCheckTableSql(tableName),
				(resultSet, i) -> resultSet.next() ? resultSet.getString(0) : "");

		return Objects.nonNull(targetTables) && targetTables.size() == 1 &&
				targetTables.get(0).equalsIgnoreCase(tableName);
	}

	@Override
	public void executeSql(String sql) {
		jdbcTemplate.execute(sql);
	}

	@Override
	public void executeSqls(String[] sqls) {
		jdbcTemplate.batchUpdate(Arrays.stream(sqls).filter(sql->
				StringUtils.isNotBlank(sql.trim())).toArray(String[]::new));
	}

	@Override
	public void executeSqlFile(String sqlFileContent) {
		sqlFileContent = sqlFileContent.replaceAll("\n", "");
		String[] sqls = sqlFileContent.split(";");
		executeSqls(sqls);
	}

	@Override
	public void addComponentVersion(ComponentVersion componentVersion) {
		componentVersionMapper.insertComponentVersion(componentVersion);
	}

	@Override
	public void updateComponentVersion(ComponentVersion componentVersion) {
		componentVersionMapper.updateComponentVersion(componentVersion);
	}

	@Override
	public void dropTable(String tableName) {
		jdbcTemplate.execute(String.format(DROP_TABLE_SQL_TEMPLATE, tableName));
	}

	@Override
	public void backupTable(String sourceTableName, String targetTableName) {
		jdbcTemplate.execute(String.format(BACKUP_TABLE_SQL_TEMPLATE, targetTableName, sourceTableName));
	}

}
