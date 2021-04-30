一、快速使用
1.引入依赖
```properties
<dependency>
    <groupId>com.hzmc</groupId>
    <artifactId>upgrade-spring-boot-starter</artifactId>
    <version>1.0.0.RELEASE</version>
</dependency>
```

2.添加模块配置文件
{upgrade.component-name}-upgrade.properties
初始化配置
```properties
# 模块名，必须配置
upgrade.component-name=ds
```
升级配置
```properties
# 当前版本
upgrade.current-version=0.0.1
```

3.添加sql文件
sql文件：sql-script/{upgrade.component-name}/{upgrade.current-version}.sql

二、配置文件详解
```properties
# 模块名，必须配置
upgrade.component-name=ds
# 当前版本，初始版本
upgrade.current-version=0.0.1
# 备份表
upgrade.backup-table=assert_ds_base,assert_ds_rac,assert_ds_conn,assert_ds_kerberos_auth,\
  assert_ds_ldap_auth,assert_ds_group,source_classification_job

# 自定义数据源配置
upgrade.upgrade-data-source.username=${ds.datasource.username}
upgrade.upgrade-data-source.password=${ds.datasource.password}
upgrade.upgrade-data-source.type=${ds.datasource.type}
upgrade.upgrade-data-source.driver-class-name=${ds.datasource.driver-class-name}
upgrade.upgrade-data-source.url=${ds.datasource.url}

# 数据源别名
upgrade.upgrade-datasource.ref=${ds.datasource.ref}
```

数据源配置有三种情况：
优先级：别名>自定义>默认
1.使用系统默认的数据源(即spring容器中的数据源)，无需额外配置数据源

2.使用别名方式(别名为spring容器中的数据源名字)，用于多数据源情况
```properties
upgrade.upgrade-datasource.ref=dsDataSource
```

3.自定义数据源
```properties
upgrade.upgrade-data-source.username=test
upgrade.upgrade-data-source.password=test
upgrade.upgrade-data-source.type=com.alibaba.druid.pool.DruidDataSource
upgrade.upgrade-data-source.driver-class-name=com.mysql.cj.jdbc.Driver
upgrade.upgrade-data-source.url=jdbc:mysql://127.0.0.1:13306/soc
```