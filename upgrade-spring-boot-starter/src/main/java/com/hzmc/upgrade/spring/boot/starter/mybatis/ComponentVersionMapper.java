package com.hzmc.upgrade.spring.boot.starter.mybatis;

import com.hzmc.upgrade.spring.boot.autoconfigure.constants.UpgradeConstant;
import com.hzmc.upgrade.spring.boot.starter.domain.ComponentVersion;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * upgrade-spring-boot
 * 2021/4/21 14:32
 * 组件版本mapper
 *
 * @author lanhaifeng
 * @since
 **/
@Mapper
public interface ComponentVersionMapper {

	@Insert("insert into " +  UpgradeConstant.VERSION_TABLE_NAME +
			"(component_name,version,create_time,update_time) " +
			" values(#{componentName},#{version}, #{createTime}, #{updateTime})")
	void insertComponentVersion(ComponentVersion componentVersion);

	@Delete("delete from " +  UpgradeConstant.VERSION_TABLE_NAME + " where component_name=#{componentName}")
	void deleteComponentVersion(String componentName);

	@Update({"<script>",
			" update ",
			UpgradeConstant.VERSION_TABLE_NAME,
			" set version = #{version}, update_time = #{updateTime}",
			" where component_name=#{componentName}",
			"</script>"})
	void updateComponentVersion(ComponentVersion componentVersion);

	@Delete("delete from " +  UpgradeConstant.VERSION_TABLE_NAME)
	void clearComponentVersions();

	@Select("select component_name,version,create_time,update_time " +
			" from " +  UpgradeConstant.VERSION_TABLE_NAME + " order by create_time desc")
	@Results(id = "ComponentVersionMap", value = {
			@Result(column = "component_name", property = "componentName", javaType = String.class),
			@Result(column = "version", property = "version", javaType = String.class),
			@Result(column = "create_time", property = "createTime", javaType = Long.class),
			@Result(column = "update_time", property = "updateTime", javaType = Long.class)
	})
	List<ComponentVersion> getComponentVersions();

	@Select("select component_name,version,create_time,update_time " +
			" from " +  UpgradeConstant.VERSION_TABLE_NAME +
			" where component_name = #{componentName}")
	@ResultMap("ComponentVersionMap")
	ComponentVersion getComponentVersion(String componentName);
}
