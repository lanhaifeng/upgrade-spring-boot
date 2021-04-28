package com.hzmc.upgrade.spring.boot.starter.processor;

import com.hzmc.upgrade.spring.boot.autoconfigure.constants.UpgradeConstant;
import com.hzmc.upgrade.spring.boot.starter.domain.ComponentVersion;
import com.hzmc.upgrade.spring.boot.starter.mybatis.ComponentVersionMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
			") ENGINE = InnoDB DEFAULT CHARSET=utf8";

	private static final String DROP_TABLE_SQL_TEMPLATE = "DROP TABLE %s";

	private static final String BACKUP_TABLE_SQL_TEMPLATE = "CREATE TABLE %s AS SELECT * FROM %s";

	private SqlSessionTemplate sqlSessionTemplate;
	private ComponentVersionMapper componentVersionMapper;

	public MySQLComponentVersionProcessor(ObjectProvider<SqlSessionTemplate> sqlSessionTemplateProvider,
										  ObjectProvider<ComponentVersionMapper> componentVersionMapperProvider) {
		this.sqlSessionTemplate = sqlSessionTemplateProvider.getIfAvailable();
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
	public ComponentVersion getComponentVersion(String componentName) {
		return componentVersionMapper.getComponentVersion(componentName);
	}

	@Override
	public Boolean tableExist(String tableName) {
		List<String> targetTables = componentVersionMapper.showTables(tableName);
		return Objects.nonNull(targetTables) && targetTables.size() == 1 &&
				targetTables.get(0).equalsIgnoreCase(tableName);
	}

	@Override
	public void executeSql(String sql) {
		sqlSessionTemplate.update(sql);
	}

	@Override
	public void executeSqls(String[] sqls) {
		SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH,false);
		List<String> sqlList = Arrays.stream(sqls).filter(sql->
				StringUtils.isNotBlank(sql.trim())).collect(Collectors.toList());
		int batchSize = 1000;
		for (int i = 0; i < sqlList.size(); i++) {
			session.update(sqlList.get(i));
			if(i%batchSize == 0 || i == sqlList.size() - 1){
				session.commit();
				session.clearCache();
			}
		}
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
		sqlSessionTemplate.update(String.format(DROP_TABLE_SQL_TEMPLATE, tableName));
	}

	@Override
	public void backupTable(String sourceTableName, String targetTableName) {
		sqlSessionTemplate.update(String.format(BACKUP_TABLE_SQL_TEMPLATE, targetTableName, sourceTableName));
	}

}
