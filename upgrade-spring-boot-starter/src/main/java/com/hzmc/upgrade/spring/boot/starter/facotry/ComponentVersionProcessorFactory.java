package com.hzmc.upgrade.spring.boot.starter.facotry;

import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;
import com.hzmc.upgrade.spring.boot.autoconfigure.enums.UpgradeDialect;
import com.hzmc.upgrade.spring.boot.starter.mybatis.ComponentVersionMapper;
import com.hzmc.upgrade.spring.boot.starter.processor.ComponentVersionProcessor;
import com.hzmc.upgrade.spring.boot.starter.processor.MySQLComponentVersionProcessor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * upgrade-spring-boot
 * 2021/4/28 11:07
 * 组件工厂
 *
 * @author lanhaifeng
 * @since
 **/
public class ComponentVersionProcessorFactory implements FactoryBean<ComponentVersionProcessor> {

	private ComponentVersionProcessor componentVersionProcessor;

	private SqlSessionFactory sqlSessionFactory;
	private DataSourceTransactionManager dataSourceTransactionManager;

	public ComponentVersionProcessorFactory(ComponentUpgradeConfig config) {
		init(config);
	}

	private void init(ComponentUpgradeConfig config){
		sqlSessionFactory = buildSqlSessionFactory(config);
		dataSourceTransactionManager = new DataSourceTransactionManager(config.getDataSource());
		if(!sqlSessionFactory.getConfiguration().hasMapper(ComponentVersionMapper.class)){
			sqlSessionFactory.getConfiguration().addMapper(ComponentVersionMapper.class);
		}
		switch (UpgradeDialect.get(config.getDialect())){
			case MYSQL:
				componentVersionProcessor = new MySQLComponentVersionProcessor(new JdbcTemplate(config.getDataSource()),
						sqlSessionFactory.getConfiguration().getMapper(ComponentVersionMapper.class, sqlSessionFactory.openSession()));
		}
	}

	private SqlSessionFactory buildSqlSessionFactory(ComponentUpgradeConfig config) {
		try {
			SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
			sqlSessionFactory.setDataSource(config.getDataSource());
			return sqlSessionFactory.getObject();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public ComponentVersionProcessor getObject() throws Exception {
		return componentVersionProcessor;
	}

	@Override
	public Class<? extends ComponentVersionProcessor> getObjectType() {
		return componentVersionProcessor == null ? ComponentVersionProcessor.class : componentVersionProcessor.getClass();
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
