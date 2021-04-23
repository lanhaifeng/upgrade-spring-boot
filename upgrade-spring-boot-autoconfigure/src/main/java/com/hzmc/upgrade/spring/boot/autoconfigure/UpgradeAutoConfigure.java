package com.hzmc.upgrade.spring.boot.autoconfigure;

import com.baseframework.utils.util.JacksonUtil;
import com.baseframework.utils.util.ValidateUtils;
import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;
import com.hzmc.upgrade.spring.boot.autoconfigure.provider.ResourceProvider;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
@MapperScan(basePackages = {"com.hzmc.upgrade.spring.boot.**.mybatis**"})
@Configuration
public class UpgradeAutoConfigure {

	private static Logger logger = LoggerFactory.getLogger(UpgradeAutoConfigure.class);

	private UpgradeProperties upgradeProperties;

	private final ResourceProvider[] resourceProviders;

	private Map<String, ComponentUpgradeConfig> configs = new ConcurrentHashMap<>();

	public UpgradeAutoConfigure(UpgradeProperties upgradeProperties, ObjectProvider<ResourceProvider[]> resourceProvider) {
		this.upgradeProperties = upgradeProperties;
		this.resourceProviders = resourceProvider.getIfAvailable();
		initConfigs(resourceProviders);
	}

	private void initConfigs(ResourceProvider[] resourceProviders) {
		try {
			Properties properties = new Properties();
			List<ComponentUpgradeConfig> targetConfigs = new ArrayList<>();

			if(Objects.nonNull(upgradeProperties.getConfigResources()) && upgradeProperties.getConfigResources().length > 0){
				for (Resource resource : upgradeProperties.getConfigResources()) {
					properties.load(resource.getInputStream());
					targetConfigs.add(ComponentUpgradeConfig.load(properties, resourceProviders));
					properties.clear();
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

			configs.putAll(targetConfigs.stream().collect(Collectors.toMap(ComponentUpgradeConfig::getComponentName,
					entry-> entry)));
		} catch (Exception e) {
			logger.error("加载配置文件失败" + ExceptionUtils.getFullStackTrace(e));
		}
	}

	public Map<String, ComponentUpgradeConfig> getConfigs() {
		return configs;
	}
}
