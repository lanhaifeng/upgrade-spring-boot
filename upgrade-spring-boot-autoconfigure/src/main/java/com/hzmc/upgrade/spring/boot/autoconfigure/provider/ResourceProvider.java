package com.hzmc.upgrade.spring.boot.autoconfigure.provider;

import org.springframework.core.io.Resource;

import java.util.List;

/**
 * upgrade-spring-boot
 * 2021/4/22 9:32
 * 读取sql文件
 *
 * @author lanhaifeng
 * @since
 **/
public interface ResourceProvider {

	/**
	 * 2021/4/22 9:34
	 * 扫描sql文件
	 *
	 * @param path
	 * @param suffix
	 * @author lanhaifeng
	 * @return java.util.List<org.springframework.core.io.Resource>
	 */
	List<Resource> scanResources(String path, String suffix);
}
