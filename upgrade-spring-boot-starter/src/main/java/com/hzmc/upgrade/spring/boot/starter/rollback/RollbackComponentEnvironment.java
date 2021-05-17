package com.hzmc.upgrade.spring.boot.starter.rollback;

import com.hzmc.upgrade.spring.boot.autoconfigure.domain.ComponentUpgradeConfig;
import com.hzmc.upgrade.spring.boot.starter.processor.ComponentVersionDelegate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

/**
 * upgrade-spring-boot
 * 2021/5/17 15:48
 * 回滚
 *
 * @author lanhaifeng
 * @since
 **/
@Component
public class RollbackComponentEnvironment implements RollbackEnvironment {

	private ComponentVersionDelegate componentVersionDelegate;

	public RollbackComponentEnvironment(ObjectProvider<ComponentVersionDelegate> componentVersionDelegateProvider) {
		this.componentVersionDelegate = componentVersionDelegateProvider.getIfAvailable();
	}

	@Override
	public void rollback(ComponentUpgradeConfig config) {
		if(componentVersionDelegate.componentRollback(config)){
			componentVersionDelegate.rollback(config);
		}
	}

}
