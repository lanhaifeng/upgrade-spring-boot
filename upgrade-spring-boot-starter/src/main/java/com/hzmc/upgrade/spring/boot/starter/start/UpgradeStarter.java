package com.hzmc.upgrade.spring.boot.starter.start;

import com.hzmc.upgrade.spring.boot.autoconfigure.UpgradeAutoConfigure;
import com.hzmc.upgrade.spring.boot.starter.manager.ComponentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
public class UpgradeStarter implements InitializingBean {

	private static Logger logger = LoggerFactory.getLogger(UpgradeStarter.class);
	private UpgradeAutoConfigure upgradeAutoConfigure;
	private ComponentManager componentManager;

	public UpgradeStarter(UpgradeAutoConfigure upgradeAutoConfigure,
						  ObjectProvider<ComponentManager> componentManagerProvider) {
		this.upgradeAutoConfigure = upgradeAutoConfigure;
		this.componentManager = componentManagerProvider.getIfAvailable();
	}

	@Transactional
	@PostConstruct
	public void start(){
		logger.info("upgrade execute start");
		upgradeAutoConfigure.getConfigs().values().forEach(conf -> {
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
}
