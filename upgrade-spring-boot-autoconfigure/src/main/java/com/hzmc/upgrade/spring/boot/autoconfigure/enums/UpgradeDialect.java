package com.hzmc.upgrade.spring.boot.autoconfigure.enums;

import org.springframework.util.StringUtils;

/**
 * upgrade-spring-boot
 * 2021/4/21 15:37
 * 方言
 *
 * @author lanhaifeng
 * @since
 **/
public enum UpgradeDialect {
	MYSQL;

	public static UpgradeDialect get(String dialect){
		if(StringUtils.isEmpty(dialect)){
			return null;
		}
		for (UpgradeDialect upgradeDialect : values()) {
			if(upgradeDialect.name().equalsIgnoreCase(dialect)){
				return upgradeDialect;
			}
		}

		return null;
	}
}
