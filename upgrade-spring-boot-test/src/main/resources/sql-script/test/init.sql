drop table IF EXISTS `assert_ds_base`;
CREATE TABLE `assert_ds_base`
(
  `id`   BIGINT AUTO_INCREMENT PRIMARY KEY,
  `ds_name`    VARCHAR(200)       NULL,
  `ds_group_id`      BIGINT DEFAULT  NULL,
  `ds_type`  VARCHAR(100) NOT NULL,
  `ds_status` tinyint NOT NULL,
  `auth_type`  tinyint NOT NULL,
  `create_mode`  tinyint NOT NULL,
  `kerberos_switch`  tinyint(1) DEFAULT 0,
  `rac_switch`  tinyint(1) DEFAULT 0,
  `dynamic_port_switch`  tinyint(1) DEFAULT 0,

  `create_user` VARCHAR(100)    NOT NULL,
  `create_time` BIGINT             NOT NULL,
  `update_time`    BIGINT          NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

drop table IF EXISTS `assert_ds_rac`;
CREATE TABLE `assert_ds_rac` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `ds_id` BIGINT DEFAULT NULL,

  `rac_name` varchar(100) not NULL,
  `rac_ip` varchar(46) not NULL,
  `rac_ip_type`   tinyint DEFAULT 1,
  `rac_port` int(5) NOT NULL,
  `rac_sid` varchar(30) DEFAULT NULL,
  `rac_status` tinyint NOT NULL,

  `create_time` BIGINT NOT NULL,
  `update_time` BIGINT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table IF EXISTS `assert_ds_conn`;
CREATE TABLE `assert_ds_conn`
(
  `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
  `ds_id`       BIGINT       NOT NULL,

  `address`        VARCHAR(64)         NULL,
  `port`        INT(5)            NOT NULL,
  `dynamic_port`  INT(5),

  `user_name`         VARCHAR(200)  NULL,
  `password`         VARCHAR(200)  NULL,

  `zk_host`        VARCHAR(64)         NULL,
  `zk_host_name`        VARCHAR(64)         NULL,
  `zk_port`        INT(5)           NULL,

  `service_name` VARCHAR(100)       NULL,
  `instance_name` VARCHAR(100)       NULL,
  `character_set`  VARCHAR(100) ,

  `create_time` BIGINT             NOT NULL,
  `update_time`    BIGINT          NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

drop table IF EXISTS `assert_ds_kerberos_auth`;
CREATE TABLE `assert_ds_kerberos_auth`
(
  `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
  `ds_id`       BIGINT       NOT NULL,

  `config_file` VARCHAR(400)       NOT NULL,
  `client_key_tab_file` VARCHAR(400)       NOT NULL,
  `server_key_tab_file` VARCHAR(400)       NOT NULL,
  `config_file_name` VARCHAR(400)       NOT NULL,
  `client_key_tab_file_name` VARCHAR(400)       NOT NULL,
  `server_key_tab_file_name` VARCHAR(400)       NOT NULL,
  `principal`         VARCHAR(100)  NULL,
  `region_principal`         VARCHAR(100)  NULL,

  `create_time` BIGINT             NOT NULL,
  `update_time`    BIGINT          NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

drop table IF EXISTS `assert_ds_ldap_auth`;
CREATE TABLE `assert_ds_ldap_auth`
(
  `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
  `ds_id`       BIGINT       NOT NULL,

  `ldap_user_name` VARCHAR(200)  NULL,
  `ldap_password` VARCHAR(200)  NULL,

  `create_time` BIGINT             NOT NULL,
  `update_time`    BIGINT          NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

drop table IF EXISTS `assert_ds_group`;
CREATE TABLE `assert_ds_group`(
  `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,

  `ds_group_name`       VARCHAR(100)       NOT NULL,
  `ds_group_desc`        VARCHAR(1000)      NULL,
  `ds_group_status` tinyint NOT NULL,

  `create_user` VARCHAR(100)    NOT NULL,
  `create_time` BIGINT             NOT NULL,
  `update_time`    BIGINT          NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET=utf8;