package com.hzmc.upgrade.spring.boot.autoconfigure;

import com.hzmc.upgrade.spring.boot.autoconfigure.domain.UpgradeConfiguration;
import com.hzmc.upgrade.spring.boot.autoconfigure.provider.ResourceProvider;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.sql.DataSource;

/**
 * upgrade-spring-boot
 * 2021/4/18 23:51
 * 描述一下类的用途
 *
 * @author lanhaifeng
 * @since
 **/
@EnableConfigurationProperties(UpgradeProperties.class)
@ComponentScan(basePackages = {"com.hzmc.upgrade.spring.boot"})
@ServletComponentScan(basePackages = {"com.hzmc.upgrade.spring.boot"})
@Configuration
public class UpgradeAutoConfigure {

	private static Logger logger = LoggerFactory.getLogger(UpgradeAutoConfigure.class);

	private UpgradeProperties upgradeProperties;

	private final ResourceProvider[] resourceProviders;

	private DataSource dataSource;

	private ApplicationContext applicationContext;

	private ConfigurableEnvironment environment;

	public UpgradeAutoConfigure(UpgradeProperties upgradeProperties,
								ApplicationContext applicationContext,
								ObjectProvider<ResourceProvider[]> resourceProvider,
								ObjectProvider<DataSource> dataSourceProvider,
								ObjectProvider<ConfigurableEnvironment> environmentProvider) {
		this.upgradeProperties = upgradeProperties;
		this.resourceProviders = resourceProvider.getIfAvailable();
		this.dataSource = dataSourceProvider.getIfAvailable();
		this.applicationContext = applicationContext;
		this.environment = environmentProvider.getIfAvailable();
	}

	@Bean
	public UpgradeConfiguration configuration(){
		return UpgradeConfigurationBuilder.create(
				upgradeProperties, applicationContext, resourceProviders, environment, dataSource).build();
	}
}
