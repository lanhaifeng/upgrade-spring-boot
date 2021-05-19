package com.hzmc.upgrade.spring.boot.starter.start;

import com.hzmc.upgrade.spring.boot.autoconfigure.domain.UpgradeConfiguration;
import com.hzmc.upgrade.spring.boot.starter.manager.ComponentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * upgrade-spring-boot
 * 2021/4/21 18:53
 * starter配置类
 *
 * @author lanhaifeng
 * @since
 **/
@Component
public class UpgradeStarter implements InitializingBean, BeanPostProcessor {

	private static Logger logger = LoggerFactory.getLogger(UpgradeStarter.class);
	private ComponentManager componentManager;
	private UpgradeConfiguration configuration;

	public UpgradeStarter(ObjectProvider<ComponentManager> componentManagerProvider,
						  ObjectProvider<UpgradeConfiguration> configurationProvider) {
		this.componentManager = componentManagerProvider.getIfAvailable();
		this.configuration = configurationProvider.getIfAvailable();
	}

	@PostConstruct
	public void start(){
		logger.info("upgrade execute start");
		configuration.getComponentUpgradeConfigs().forEach(conf -> {
			logger.info("{} start upgrade", conf.getComponentName());
			componentManager.doUpgrade(conf);
			logger.info("{} end upgrade", conf.getComponentName());
		});

		logger.info("upgrade execute end");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Objects.requireNonNull(componentManager, "未指定组件升级管理器ComponentManager");
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
