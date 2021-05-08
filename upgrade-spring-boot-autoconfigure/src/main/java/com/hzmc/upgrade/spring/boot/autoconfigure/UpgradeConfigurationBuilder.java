package com.hzmc.upgrade.spring.boot.autoconfigure;

import com.baseframework.utils.util.JacksonUtil;
import com.baseframework.utils.util.ValidateUtils;
import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;
import com.hzmc.upgrade.spring.boot.autoconfigure.domain.UpgradeConfiguration;
import com.hzmc.upgrade.spring.boot.autoconfigure.provider.ResourceProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * upgrade-spring-boot
 * 2021/4/29 11:15
 * 初始化组件模块配置
 *
 * @author lanhaifeng
 * @since
 **/
public class UpgradeConfigurationBuilder {

	private static Logger logger = LoggerFactory.getLogger(UpgradeConfigurationBuilder.class);

	private ApplicationContext applicationContext;

	private ResourceProvider[] resourceProviders;

	private UpgradeProperties upgradeProperties;

	private ConfigurableEnvironment environment;

	private DataSource defaultDataSource;

	private static final String UPGRADE_PROPERTIES_NAME = "UPGRADE_PROPERTIES";

	public static UpgradeConfigurationBuilder create(UpgradeProperties upgradeProperties,
													 ApplicationContext applicationContext,
													 ResourceProvider[] resourceProviders,
													 ConfigurableEnvironment environment,
													 DataSource dataSource) {
		return new UpgradeConfigurationBuilder(upgradeProperties, applicationContext, resourceProviders, environment, dataSource);
	}


	public UpgradeConfigurationBuilder(UpgradeProperties upgradeProperties,
									   ApplicationContext applicationContext,
									   ResourceProvider[] resourceProviders,
									   ConfigurableEnvironment environment,
									   DataSource dataSource) {
		this.upgradeProperties = upgradeProperties;
		this.applicationContext = applicationContext;
		this.resourceProviders = resourceProviders;
		this.environment = environment;
		this.defaultDataSource = dataSource;
	}

	public UpgradeConfiguration build() {
		return initConfiguration(resourceProviders);
	}

	private UpgradeConfiguration initConfiguration(ResourceProvider[] resourceProviders) {
		UpgradeConfiguration configuration = new UpgradeConfiguration();
		try {

			Properties properties = new Properties();
			List<ComponentUpgradeConfig> targetConfigs = new ArrayList<>();

			MutablePropertySources pvs = new MutablePropertySources(environment.getPropertySources());
			Binder binder = new Binder(ConfigurationPropertySources.from(pvs));

			ComponentUpgradeConfig componentUpgradeConfig;
			if(Objects.nonNull(upgradeProperties.getConfigResources()) && upgradeProperties.getConfigResources().length > 0){
				for (Resource resource : upgradeProperties.getConfigResources()) {
					properties.load(resource.getInputStream());
					pvs.addLast(new PropertiesPropertySource(UPGRADE_PROPERTIES_NAME, properties));

					componentUpgradeConfig = binder.bind(UpgradeProperties.UPGRADE_PREFIX, ComponentUpgradeConfig.class).get();
					componentUpgradeConfig.initDefault();

					targetConfigs.add(componentUpgradeConfig);
					initConfigResources(componentUpgradeConfig, resourceProviders);
					initDataSource(componentUpgradeConfig);
					properties.clear();
					pvs.remove(UPGRADE_PROPERTIES_NAME);
				}
			}

			List<String> messages;
			for (ComponentUpgradeConfig targetConfig : targetConfigs) {
				logger.info("模块配置信息：", JacksonUtil.bean2Json(targetConfig));
				messages = ValidateUtils.validate(targetConfigs);
				if(Objects.nonNull(messages) && messages.size() > 0){
					logger.error(messages.toString());
					throw new RuntimeException("模块升级配置非法");
				}
			}

			configuration.getComponentUpgradeConfigs().addAll(targetConfigs);
			configuration.setEnvironment(environment);
		} catch (Exception e) {
			logger.error("加载配置文件失败" + ExceptionUtils.getFullStackTrace(e));
			throw new RuntimeException("初始化配置失败", e);
		}

		return configuration;
	}

	private void initConfigResources(ComponentUpgradeConfig config, ResourceProvider[] resourceProviders) {
		if(Objects.nonNull(resourceProviders) && resourceProviders.length > 0){
			List<Resource> targets;
			config.getUpgradeResources().clear();
			for (ResourceProvider resourceProvider : resourceProviders) {
				targets = resourceProvider.scanResources(
						config.getUpgradeFilePath(), config.getUpgradeFileSuffix());
				if(Objects.nonNull(targets)){
					config.getUpgradeResources().addAll(targets);
				}
			}
		}
	}

	private void initDataSource(ComponentUpgradeConfig config){
		if (config.canUse()) {
			String dataSourceRef = config.getUpgradeDataSource().getRef();
			if (StringUtils.isNotBlank(dataSourceRef) && applicationContext.containsBean(dataSourceRef)) {
				config.setDataSource(applicationContext.getBean(dataSourceRef, DataSource.class));
			}
			if (StringUtils.isBlank(dataSourceRef)) {
				config.setDataSource(config.getUpgradeDataSource().buildDataSource());
			}
		} else {
			config.setDataSource(defaultDataSource);
		}
	}

}
