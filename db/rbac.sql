/*
SQLyog Professional v12.08 (64 bit)
MySQL - 5.6.31 : Database - wnt_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`wnt_db` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `wnt_db`;

/*Table structure for table `rbac_action` */

DROP TABLE IF EXISTS `rbac_action`;

CREATE TABLE `rbac_action` (
  `actionid` int(10) NOT NULL COMMENT '操作id',
  `actionname` varchar(20) NOT NULL COMMENT '操作名',
  `icon` varchar(20) DEFAULT NULL COMMENT '图标',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `creatorId` varchar(20) NOT NULL COMMENT '创建者',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`actionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `rbac_action` */

insert  into `rbac_action`(`actionid`,`actionname`,`icon`,`status`,`remark`,`creatorId`,`createTime`) values (1,'switchUser',NULL,'1',NULL,'sysadmin','2016-07-22 15:23:06');

/*Table structure for table `rbac_group` */

DROP TABLE IF EXISTS `rbac_group`;

CREATE TABLE `rbac_group` (
  `groupid` int(10) NOT NULL COMMENT '用户组id',
  `groupname` varchar(20) NOT NULL COMMENT '用户组名',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `creatorId` varchar(20) NOT NULL COMMENT '创建者',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `rbac_group` */

/*Table structure for table `rbac_grouprole` */

DROP TABLE IF EXISTS `rbac_grouprole`;

CREATE TABLE `rbac_grouprole` (
  `groupid` int(10) NOT NULL COMMENT '用户组id',
  `roleid` int(10) NOT NULL COMMENT '角色id',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `creatorId` varchar(20) NOT NULL COMMENT '创建者',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`groupid`,`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `rbac_grouprole` */

/*Table structure for table `rbac_menu` */

DROP TABLE IF EXISTS `rbac_menu`;

CREATE TABLE `rbac_menu` (
  `menuid` int(10) NOT NULL COMMENT '菜单id',
  `parentid` int(10) NOT NULL COMMENT '父菜单',
  `menuname` varchar(20) NOT NULL COMMENT '菜单名称',
  `menuorder` int(10) NOT NULL COMMENT '菜单排序',
  `menutype` varchar(20) NOT NULL COMMENT '菜单类型',
  `url` varchar(256) DEFAULT NULL COMMENT '超链接',
  `icon` varchar(20) DEFAULT NULL COMMENT '菜单图标',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `creatorId` varchar(20) NOT NULL COMMENT '创建者',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`menuid`)
) ENGINE=InnoDB DEFAULT CHARSET=macce;

/*Data for the table `rbac_menu` */

/*Table structure for table `rbac_menuaction` */

DROP TABLE IF EXISTS `rbac_menuaction`;

CREATE TABLE `rbac_menuaction` (
  `actionid` int(10) NOT NULL COMMENT '操作id',
  `menuid` int(10) NOT NULL COMMENT '菜单id',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `creatorId` varchar(20) NOT NULL COMMENT '创建者',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`actionid`,`menuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `rbac_menuaction` */

/*Table structure for table `rbac_role` */

DROP TABLE IF EXISTS `rbac_role`;

CREATE TABLE `rbac_role` (
  `roleid` int(10) NOT NULL COMMENT '角色id',
  `rolename` varchar(20) NOT NULL COMMENT '角色名',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `creatorId` varchar(20) NOT NULL COMMENT '创建者',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `rbac_role` */

insert  into `rbac_role`(`roleid`,`rolename`,`status`,`remark`,`creatorId`,`createTime`) values (999,'超级管理员','1',NULL,'sysadmin','2016-07-22 15:03:32');

/*Table structure for table `rbac_roleaction` */

DROP TABLE IF EXISTS `rbac_roleaction`;

CREATE TABLE `rbac_roleaction` (
  `roleid` int(10) NOT NULL COMMENT '角色id',
  `actionid` int(10) NOT NULL COMMENT '操作id',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `creatorId` varchar(20) NOT NULL COMMENT '创建者',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`roleid`,`actionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `rbac_roleaction` */

insert  into `rbac_roleaction`(`roleid`,`actionid`,`status`,`remark`,`creatorId`,`createTime`) values (999,1,'1',NULL,'sysadmin','2016-07-22 15:27:01');

/*Table structure for table `rbac_rolemenu` */

DROP TABLE IF EXISTS `rbac_rolemenu`;

CREATE TABLE `rbac_rolemenu` (
  `roleid` int(10) NOT NULL COMMENT '角色id',
  `menuid` int(10) NOT NULL COMMENT '菜单id',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `creatorId` varchar(20) NOT NULL COMMENT '创建者',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`roleid`,`menuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `rbac_rolemenu` */

/*Table structure for table `rbac_user` */

DROP TABLE IF EXISTS `rbac_user`;

CREATE TABLE `rbac_user` (
  `userid` varchar(20) NOT NULL COMMENT '用户ID',
  `username` varchar(40) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `pswdsalt` varchar(16) NOT NULL COMMENT '密码盐',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `creatorId` varchar(20) NOT NULL COMMENT '创建者',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `rbac_user` */

insert  into `rbac_user`(`userid`,`username`,`password`,`pswdsalt`,`status`,`remark`,`creatorId`,`createTime`) values ('leslie','王南图','0FE81971364D8933F7B65B9ED8063EDB','52131083','1',NULL,'sysadmin','2016-07-20 14:07:08'),('sysadmin','系统管理员','906C44623A9F6D0719EEABA2D4279CCA','57693242','1',NULL,'sysadmin','2016-07-07 14:45:30');

/*Table structure for table `rbac_usergroup` */

DROP TABLE IF EXISTS `rbac_usergroup`;

CREATE TABLE `rbac_usergroup` (
  `userid` varchar(20) NOT NULL COMMENT '用户id',
  `groupid` int(10) NOT NULL COMMENT '用户组id',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `creatorId` varchar(20) NOT NULL COMMENT '创建者',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`userid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `rbac_usergroup` */

/*Table structure for table `rbac_userrole` */

DROP TABLE IF EXISTS `rbac_userrole`;

CREATE TABLE `rbac_userrole` (
  `userid` varchar(20) NOT NULL COMMENT '用户id',
  `roleid` int(10) NOT NULL COMMENT '角色id',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `creatorId` varchar(20) NOT NULL COMMENT '创建者',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`userid`,`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `rbac_userrole` */

insert  into `rbac_userrole`(`userid`,`roleid`,`status`,`remark`,`creatorId`,`createTime`) values ('sysadmin',999,'1',NULL,'sysadmin','2016-07-22 15:05:33');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
