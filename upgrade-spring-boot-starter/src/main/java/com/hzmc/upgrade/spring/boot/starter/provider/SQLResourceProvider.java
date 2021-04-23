package com.hzmc.upgrade.spring.boot.starter.provider;

import com.baseframework.utils.util.FileUtil;
import com.hzmc.upgrade.spring.boot.autoconfigure.provider.ResourceProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * upgrade-spring-boot
 * 2021/4/22 9:35
 * 扫描SQL文件
 *
 * @author lanhaifeng
 * @since
 **/
@Component
public class SQLResourceProvider implements ResourceProvider {

	private static Logger logger = LoggerFactory.getLogger(ResourceProvider.class);

	@Override
	public List<Resource> scanResources(String sqlLocation, String suffix) {
		List<Resource> resources = new ArrayList<>();
		if(StringUtils.isBlank(sqlLocation)){
			return resources;
		}
		if(Objects.isNull(suffix)){
			suffix = "";
		}
		ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		try {
			Resource[] mappers = resourceResolver.getResources(sqlLocation);
			resources.addAll(Arrays.asList(mappers));
		} catch (IOException e) {
			logger.error("解析xml文件失败，错误：", ExceptionUtils.getFullStackTrace(e));
		}

		Iterator<Resource> iterator = resources.iterator();
		Resource resource;
		while (iterator.hasNext()){
			resource = iterator.next();
			if(!resource.getFilename().endsWith(suffix)){
				iterator.remove();
			}
		}
		resources.sort(Comparator.comparing(o -> FileUtil.getFileName(o.getFilename())));

		return resources;
	}
}
