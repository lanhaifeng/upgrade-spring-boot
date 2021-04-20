package com.hzmc.upgrade.spring.boot.starter.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * upgrade-spring-boot
 * 2021/4/20 17:31
 * spring jdbc实现数据库处理
 *
 * @author lanhaifeng
 * @since
 **/
@Component
public class SpringJdbcProcessor implements JdbcProcessor {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Boolean tableExist(String tableName) {
		return null;
	}

	@Override
	public Boolean createTable(String sql) {
		return null;
	}

	@Override
	public Boolean executeSql(String sqlFile) {
		return null;
	}
}
