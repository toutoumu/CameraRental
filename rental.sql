/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50630
 Source Host           : localhost
 Source Database       : rental

 Target Server Type    : MySQL
 Target Server Version : 50630
 File Encoding         : utf-8

 Date: 07/11/2017 11:08:22 AM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `SYSMAN_USER`
-- ----------------------------
DROP TABLE IF EXISTS `SYSMAN_USER`;
CREATE TABLE `SYSMAN_USER` (
  `pid` int(11) NOT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `backImage` varchar(200) DEFAULT NULL,
  `bankCard` varchar(255) DEFAULT NULL,
  `birthday` bigint(20) DEFAULT NULL,
  `category` int(11) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `frontImage` varchar(200) DEFAULT NULL,
  `idCard` varchar(30) DEFAULT NULL,
  `invitationCode` varchar(50) DEFAULT NULL,
  `locked` int(11) DEFAULT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `password` varchar(32) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `portrait` varchar(200) DEFAULT NULL,
  `position` varchar(20) DEFAULT NULL,
  `position_desc` varchar(100) DEFAULT NULL,
  `real_name` varchar(10) DEFAULT NULL,
  `reason` varchar(500) DEFAULT NULL,
  `sex` varchar(2) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `userName` varchar(50) NOT NULL,
  `verify` int(11) DEFAULT NULL,
  `creater_id` int(11) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `cityId` int(11) DEFAULT NULL,
  `device` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pid`),
  KEY `FK_46ink0fo06cp5om9wbd8n5kea` (`creater_id`),
  CONSTRAINT `FK_46ink0fo06cp5om9wbd8n5kea` FOREIGN KEY (`creater_id`) REFERENCES `sysman_user` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `SYSMAN_USER`
-- ----------------------------
BEGIN;
INSERT INTO `SYSMAN_USER` VALUES ('1', '1437902623198', '1', '0', null, null, '0', '1', 'admin@admin.com', null, null, null, '1', null, 'e10adc3949ba59abbe56e057f20f883e', '134555666789', 'http://localhost:8080/CameraRental/image/data/4/0/52eb98af-b4e4-4577-b14f-29f6a9b89080.small.jpg', '系统管理员', '系统管理员', 'admin', null, null, null, 'admin', '3', null, null, null, '3', null, null), ('2', '1437914365444', '1', '0', null, null, '0', '2', 'toutuomu@126.com', null, null, null, '1', null, '21232f297a57a5a743894a0e4a801fc3', '15989348952', null, 'Android工程师', 'Android工程师', '刘斌', null, null, null, 'liubin', '0', null, null, null, '3', null, null), ('3', '1437903642208', '1', '0', null, null, '0', '2', null, null, null, '20150726174042', '1', null, '96e79218965eb72c92a549dd5a330112', '15989348952', null, null, null, null, null, null, null, '15989348952', '3', null, null, null, '3', null, null);
COMMIT;

-- ----------------------------
--  Table structure for `news_templete`
-- ----------------------------
DROP TABLE IF EXISTS `news_templete`;
CREATE TABLE `news_templete` (
  `pid` int(11) NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  `content` varchar(10000) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `news_templete`
-- ----------------------------
BEGIN;
INSERT INTO `news_templete` VALUES ('1', '2014-11-10 10:07:33', '0', '<p style=\"line-height: 16px;\"><img src=\"/web/upload/ueditor/jsp/upload/image/20141110/1415585249969055988.jpg\" title=\"1415585249969055988.jpg\" alt=\"20140917181801.jpg\"/><img style=\"vertical-align: middle; margin-right: 2px;\" src=\"/web/ueditor/dialogs/attachment/fileTypeImages/icon_rar.gif\"/><a style=\"font-size:12px; color:#0066cc;\" href=\"/web/upload/ueditor/jsp/upload/file/20141106/1415240191989090305.rar\" title=\"51CTO下载-exe4j_win_v420 - 副本.rar\">51CTO下载-exe4j_win_v420 - 副本.rar</a></p><p class=\"picIntro\" style=\"text-align: left;\"><img src=\"http://img.baidu.com/hi/jx2/j_0002.gif\"/>台当局控制的太平岛码头兴建工程已启动，图为完工后示意图。 图片来源：台湾《中时电子报》<br/></p><p style=\"text-align: left;\">原标题：大陆南海7岛填海造陆 台担心严重冲击太平岛防务</p><p>【环\r\n球网综合报道】台湾《中国时报》报道称，中国大陆从今年开始，在南海多个岛礁进行大规模填海造陆，对太平岛形成“包围之势”，台“国安”单位相当重视。据\r\n早前报道，台“国安局长”李翔宙曾在台“立法院”坦言“非常担心”，称大陆目前在南海7个岛礁所进行的填海造陆作业，有5个是由最高领导人核定的，目标是\r\n“小岛堡垒化”和“大岛阵地化”。</p><p>李翔宙表示，以“华阳礁”为例，去年3月还只有1000平方米，到今年7月已经有14万平方米，到9月底已达18万平方米。</p><p>李翔宙说，解放军海军司令在9月下旬，史无前例地用一星期时间，逐岛视察这些岛礁的填海造陆工程，还在永暑礁视察三军联合作战想定的操演，借此宣示中国大陆在南海已经有全盘的战略规划。</p><p>报道称，长期关心南海局势的国民党“立委”林郁方指出，包括赤瓜礁、东门礁、南熏礁和华阳礁，对太平岛已呈现“包围态势”。有外国官员还认为，在华阳礁、赤瓜礁和南熏礁完工后，大陆就会宣布设立“南海防空识别区”。</p><p>林\r\n郁方表示，赤瓜礁、东门礁和南熏礁不仅已经对太平岛形成“包围”，与太平岛的距离更是“一个比一个近”，赤瓜礁在太平岛南方70公里、东门礁在东南方57\r\n公里，南熏礁更是在太平岛西南方只有30公里。不论中国大陆最后填海的目的是大型雷达站、码头或跑道，对太平岛的防务势将造成“严重的冲击”。<span class=\"ifengLogo\"><a href=\"http://www.ifeng.com/\" target=\"_blank\"></a></span></p><p>&nbsp;&nbsp;<video class=\"edui-upload-video  vjs-default-skin           video-js\" controls=\"\" preload=\"none\" width=\"420\" height=\"280\" src=\"/web/upload/ueditor/jsp/upload/video/20141029/1414577541133041267.mp4\" data-setup=\"{}\"><source src=\"/web/upload/ueditor/jsp/upload/video/20141029/1414577541133041267.mp4\" type=\"video/mp4\"/></video></p>', '台当局 太平岛码头兴建工程', '南海7岛 台湾 ');
COMMIT;

-- ----------------------------
--  Table structure for `portfolio_templete`
-- ----------------------------
DROP TABLE IF EXISTS `portfolio_templete`;
CREATE TABLE `portfolio_templete` (
  `pid` int(11) NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `portfolioType_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`pid`),
  KEY `FK71B234B5EAD8DBDA` (`portfolioType_id`),
  CONSTRAINT `FK71B234B5EAD8DBDA` FOREIGN KEY (`portfolioType_id`) REFERENCES `portfolio_type` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `portfolio_templete`
-- ----------------------------
BEGIN;
INSERT INTO `portfolio_templete` VALUES ('1', '2014-11-06 10:17:00', '0', '<p><img alt=\"20140917181801.jpg\" src=\"/web/upload/ueditor/jsp/upload/image/20141106/1415240218974085997.jpg\" title=\"1415240218974085997.jpg\"/></p>', 'first', '2'), ('2', '2014-11-06 10:17:07', '0', '<p><br/><img alt=\"20140917181801.jpg\" src=\"/web/upload/ueditor/jsp/upload/image/20141106/1415240226491046097.jpg\" title=\"1415240226491046097.jpg\"/></p>', 'zzzz', '1');
COMMIT;

-- ----------------------------
--  Table structure for `portfolio_type`
-- ----------------------------
DROP TABLE IF EXISTS `portfolio_type`;
CREATE TABLE `portfolio_type` (
  `pid` int(11) NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `portfolio_type`
-- ----------------------------
BEGIN;
INSERT INTO `portfolio_type` VALUES ('1', '2014-10-22 16:25:28', '0', '全部东西', 'ALL'), ('2', '2014-10-22 16:25:00', '0', '第一个分类', 'UIPART'), ('3', '2014-10-22 16:25:16', '0', '第二个分类', 'WEBPART'), ('4', '2014-11-20 10:53:52', '0', '2', '2'), ('5', '2014-11-20 10:53:56', '0', '3', '3'), ('6', '2014-11-20 10:53:58', '0', '4', '4'), ('7', '2014-11-20 10:54:01', '0', '5', '5'), ('8', '2014-11-20 10:54:04', '0', '6', '6'), ('9', '2014-11-20 10:54:08', '0', '7', '7'), ('10', '2014-11-20 10:54:11', '0', '8', '8'), ('11', '2014-11-20 10:54:15', '0', '9', '9');
COMMIT;

-- ----------------------------
--  Table structure for `sysman_resource`
-- ----------------------------
DROP TABLE IF EXISTS `sysman_resource`;
CREATE TABLE `sysman_resource` (
  `pid` int(11) NOT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `href` varchar(200) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `order_no` int(11) DEFAULT NULL,
  `resourceType` int(11) DEFAULT NULL,
  `p_menu_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`pid`),
  KEY `FKA95124A0952CD7E7` (`p_menu_id`),
  CONSTRAINT `FKA95124A0952CD7E7` FOREIGN KEY (`p_menu_id`) REFERENCES `sysman_resource` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sysman_resource`
-- ----------------------------
BEGIN;
INSERT INTO `sysman_resource` VALUES ('1', '20140827160540', '1', '系统管理', '/', '系统管理', '1', '1', null), ('2', '20141106160713', '1', '角色管理', '/admin/sysmanRole/list', '角色管理', '2', '1', '5'), ('3', '20141106160708', '1', '用户管理', '/admin/sysmanUser/list', '用户管理', '1', '1', '5'), ('4', '20141013154102', '1', '资源管理', '/admin/sysmanResource/list', '资源管理', '3', '1', '5'), ('5', '20141106160704', '1', '账号管理', '/', '账号管理', '1', '1', '1'), ('6', '20141016175032', '1', '首页管理', '/admin/page/home', '首页管理', '1', '1', '1'), ('7', '20150720144736', '1', '模板管理', '/', '模板管理', '3', '1', '1'), ('8', '20141016182319', '1', '新闻页模板管理', '/admin/page/templete/newsTemplete/list', '新闻页模板管理', '1', '1', '7'), ('9', '20141022161146', '1', '代表作品集-类型', '/admin/page/templete/portfolioType/list', '代表作品集-类型', '2', '1', '7'), ('10', '20141022164926', '1', '代表作品集', '/admin/page/templete/portfolioTemplete/list', '代表作品集', '3', '1', '7'), ('11', '20150720144745', '1', '基础数据维护', '/', '基础数据维护', '2', '1', '1'), ('12', '20150718233551', '1', '品牌维护', '/system/brand/brand_manage.jsp', '品牌维护', '1', '1', '11'), ('13', '20150720144345', '1', '类别维护', '/system/category/category_manage.jsp', '类别维护', '2', '1', '11'), ('14', '20150720144412', '1', '地区维护', '/system/region/region_manage.jsp', '地区维护', '3', '1', '11'), ('15', '20150720144433', '1', '相机维护', '/system/camera/camera_manage.jsp', '相机维护', '4', '1', '11'), ('16', '20150720144454', '1', '镜头维护', '/system/cameraLens/cameraLens_manage.jsp', '镜头维护', '5', '1', '11'), ('17', '20150720144753', '1', '系统管理', '/', '系统管理', '1', '1', '1'), ('18', '20150720144612', '1', '订单管理', '/system/order/order_manage.jsp', '订单管理', '1', '1', '17'), ('19', '20150720144659', '1', '用户管理', '/system/user/user_manage.jsp', '用户管理', '2', '1', '17'), ('20', '20150721214930', '1', '租赁', '/system/rental/rental_manage.jsp', '租赁信息管理', '3', '1', '17'), ('22', '1437916625735', '1', 'Banner维护', '/system/banner/banner_manage.jsp', 'Banner维护', '6', '1', '11');
COMMIT;

-- ----------------------------
--  Table structure for `sysman_role`
-- ----------------------------
DROP TABLE IF EXISTS `sysman_role`;
CREATE TABLE `sysman_role` (
  `pid` int(11) NOT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `creater_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`pid`),
  KEY `FK_6cdexd4wmbhstyrvladdjimql` (`creater_id`),
  CONSTRAINT `FK_6cdexd4wmbhstyrvladdjimql` FOREIGN KEY (`creater_id`) REFERENCES `sysman_user` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sysman_role`
-- ----------------------------
BEGIN;
INSERT INTO `sysman_role` VALUES ('1', '1437917697752', '1', '系统管理员1', '系统管理员', null), ('2', '1437903287451', '1', '操作员', '操作员', null);
COMMIT;

-- ----------------------------
--  Table structure for `sysman_role_resource`
-- ----------------------------
DROP TABLE IF EXISTS `sysman_role_resource`;
CREATE TABLE `sysman_role_resource` (
  `role_id` int(11) NOT NULL,
  `resource_id` int(11) NOT NULL,
  KEY `FK_195ytlqigsmlappoaknrh22c8` (`resource_id`),
  KEY `FK_b6mtodcxsgqmuegp5rqpvhqkj` (`role_id`),
  CONSTRAINT `FK_195ytlqigsmlappoaknrh22c8` FOREIGN KEY (`resource_id`) REFERENCES `sysman_resource` (`pid`),
  CONSTRAINT `FK_b6mtodcxsgqmuegp5rqpvhqkj` FOREIGN KEY (`role_id`) REFERENCES `sysman_role` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sysman_role_resource`
-- ----------------------------
BEGIN;
INSERT INTO `sysman_role_resource` VALUES ('2', '17'), ('2', '18'), ('2', '19'), ('2', '20'), ('2', '11'), ('2', '12'), ('2', '13'), ('2', '14'), ('2', '15'), ('2', '16'), ('1', '1'), ('1', '5'), ('1', '3'), ('1', '2'), ('1', '4'), ('1', '6'), ('1', '17'), ('1', '18'), ('1', '19'), ('1', '20'), ('1', '11'), ('1', '12'), ('1', '13'), ('1', '14'), ('1', '15'), ('1', '16'), ('1', '22'), ('1', '7'), ('1', '8'), ('1', '9'), ('1', '10');
COMMIT;

-- ----------------------------
--  Table structure for `sysman_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sysman_user_role`;
CREATE TABLE `sysman_user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  KEY `FK_1due6g347jkadpl3r4cmw54i8` (`role_id`),
  KEY `FK_j6xo65f2g9m69mx1xaoau803g` (`user_id`),
  CONSTRAINT `FK_1due6g347jkadpl3r4cmw54i8` FOREIGN KEY (`role_id`) REFERENCES `sysman_role` (`pid`),
  CONSTRAINT `FK_j6xo65f2g9m69mx1xaoau803g` FOREIGN KEY (`user_id`) REFERENCES `sysman_user` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sysman_user_role`
-- ----------------------------
BEGIN;
INSERT INTO `sysman_user_role` VALUES ('1', '1'), ('2', '2');
COMMIT;

-- ----------------------------
--  Table structure for `t_CashCoupon`
-- ----------------------------
DROP TABLE IF EXISTS `t_CashCoupon`;
CREATE TABLE `t_CashCoupon` (
  `cashId` int(11) NOT NULL AUTO_INCREMENT,
  `amount` int(11) DEFAULT NULL,
  `category` int(11) DEFAULT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `expireTime` bigint(20) NOT NULL,
  `invitationUserId` int(11) DEFAULT NULL,
  `orderNumber` varchar(255) DEFAULT NULL,
  `useTime` bigint(20) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `invitationCode` varchar(50) DEFAULT NULL,
  `mark` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`cashId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_Collected_RentalInfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_Collected_RentalInfo`;
CREATE TABLE `t_Collected_RentalInfo` (
  `rentalId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `collectTime` bigint(20) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `brandId` int(11) DEFAULT NULL,
  `cameraId` int(11) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `cityId` int(11) DEFAULT NULL,
  `cover` varchar(255) DEFAULT NULL,
  `lensId` int(11) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `rentalCount` int(11) DEFAULT NULL,
  `score` double DEFAULT NULL,
  PRIMARY KEY (`rentalId`,`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_Comment`
-- ----------------------------
DROP TABLE IF EXISTS `t_Comment`;
CREATE TABLE `t_Comment` (
  `commentId` int(11) NOT NULL AUTO_INCREMENT,
  `commentTime` bigint(20) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `grade` double DEFAULT NULL,
  `rentalId` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `orderNumber` varchar(255) DEFAULT NULL,
  `score` double DEFAULT NULL,
  PRIMARY KEY (`commentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_Message`
-- ----------------------------
DROP TABLE IF EXISTS `t_Message`;
CREATE TABLE `t_Message` (
  `messageId` int(11) NOT NULL AUTO_INCREMENT,
  `isFinish` bit(1) DEFAULT NULL,
  `messageType` int(11) DEFAULT NULL,
  `orderNumber` varchar(255) DEFAULT NULL,
  `parameters` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `templateId` varchar(255) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  `category` int(11) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `adminPhone` varchar(255) DEFAULT NULL,
  `guestPhone` varchar(255) DEFAULT NULL,
  `sendTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`messageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_RefundNotifyMessage`
-- ----------------------------
DROP TABLE IF EXISTS `t_RefundNotifyMessage`;
CREATE TABLE `t_RefundNotifyMessage` (
  `refundNotifyId` int(11) NOT NULL AUTO_INCREMENT,
  `Charset` varchar(255) DEFAULT NULL,
  `ErrorCode` varchar(255) DEFAULT NULL,
  `ErrorMsg` varchar(255) DEFAULT NULL,
  `Ext1` varchar(255) DEFAULT NULL,
  `Ext2` varchar(255) DEFAULT NULL,
  `OriginalOrderNo` varchar(255) DEFAULT NULL,
  `PartlyRefundFlag` varchar(255) DEFAULT NULL,
  `RefundAmount` varchar(255) DEFAULT NULL,
  `RefundOrderNo` varchar(255) DEFAULT NULL,
  `RefundTransNo` varchar(255) DEFAULT NULL,
  `SendTime` varchar(255) DEFAULT NULL,
  `SenderId` varchar(255) DEFAULT NULL,
  `ServiceCode` varchar(255) DEFAULT NULL,
  `SignMsg` varchar(255) DEFAULT NULL,
  `SignType` varchar(255) DEFAULT NULL,
  `Status` varchar(255) DEFAULT NULL,
  `TraceNo` varchar(255) DEFAULT NULL,
  `Version` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`refundNotifyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_RentalInfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_RentalInfo`;
CREATE TABLE `t_RentalInfo` (
  `rentalId` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `aircraft` int(11) NOT NULL,
  `autoAccept` int(11) NOT NULL,
  `battery` int(11) NOT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `brandId` int(11) DEFAULT NULL,
  `buckle` int(11) NOT NULL,
  `cameraBag` int(11) NOT NULL,
  `cameraId` int(11) DEFAULT NULL,
  `cardReader` int(11) NOT NULL,
  `charger` int(11) NOT NULL,
  `city` varchar(255) DEFAULT NULL,
  `cleaningTool` int(11) NOT NULL,
  `grayGradient` int(11) NOT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `lenBag` int(11) NOT NULL,
  `lensHood` int(11) NOT NULL,
  `locked` int(11) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `mark` varchar(255) DEFAULT NULL,
  `maxRental` int(11) DEFAULT NULL,
  `memory` varchar(255) DEFAULT NULL,
  `minRental` int(11) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `ndMirror` int(11) NOT NULL,
  `polarizer` int(11) NOT NULL,
  `price` int(11) DEFAULT NULL,
  `purchaseDate` bigint(20) DEFAULT NULL,
  `remoteControl` int(11) NOT NULL,
  `rentalCount` int(11) NOT NULL,
  `serialNumber` varchar(255) DEFAULT NULL,
  `snNumber` varchar(255) DEFAULT NULL,
  `strap` int(11) NOT NULL,
  `userId` int(11) DEFAULT NULL,
  `uv` int(11) NOT NULL,
  `verify` int(11) DEFAULT NULL,
  `waterproofShell` int(11) NOT NULL,
  `wirelessShutter` int(11) NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `deposit` int(11) DEFAULT NULL,
  `lensId` int(11) DEFAULT NULL,
  `cityId` int(11) DEFAULT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  `score` double DEFAULT NULL,
  `cover` varchar(255) DEFAULT NULL,
  `isDelete` int(11) DEFAULT NULL,
  `lensBrand` varchar(255) DEFAULT NULL,
  `lensModel` varchar(255) DEFAULT NULL,
  `responseTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`rentalId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_RentalInfo`
-- ----------------------------
BEGIN;
INSERT INTO `t_RentalInfo` VALUES ('1', '地址', '0', '2', '0', '品牌', '1', '0', '0', '0', '0', '0', '深圳', '0', '0', '', '0', '0', '1', '2.111', '', '0', '', '1', '型号', '姓名', '0', '0', '0', '0', '0', '0', '', '', '0', '1', '0', '2', '0', '0', 'asdf', '1235', '1', '1', '0', '0', null, null, null, null, null), ('2', '地址', '0', '2', '0', '品牌', '1', '0', '0', '0', '0', '0', '深圳', '0', '0', '', '0', '0', '1', '3.111', '', '0', '', '1', '型号', '姓名', '0', '0', '0', '0', '0', '0', '', '', '0', '1', '0', '2', '0', '0', 'asdf', '1235', '1', '1', '0', '0', null, null, null, null, null);
COMMIT;

-- ----------------------------
--  Table structure for `t_Withdrawals`
-- ----------------------------
DROP TABLE IF EXISTS `t_Withdrawals`;
CREATE TABLE `t_Withdrawals` (
  `WithdrawalsId` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `applyTime` bigint(20) DEFAULT NULL,
  `finishTime` bigint(20) DEFAULT NULL,
  `idCard` varchar(30) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `real_name` varchar(10) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `userName` varchar(50) NOT NULL,
  PRIMARY KEY (`WithdrawalsId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_banner`
-- ----------------------------
DROP TABLE IF EXISTS `t_banner`;
CREATE TABLE `t_banner` (
  `bannerId` int(11) NOT NULL AUTO_INCREMENT,
  `image` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `visible` int(11) NOT NULL,
  `content` varchar(10000) DEFAULT NULL,
  PRIMARY KEY (`bannerId`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_banner`
-- ----------------------------
BEGIN;
INSERT INTO `t_banner` VALUES ('8', 'http://localhost:8080/CameraRental/image/data/4/14/7e2dd2c0-306c-410d-9b6b-ce139928f48d.small.jpg', '阿斯蒂芬阿斯蒂芬', null, '1', null), ('10', 'http://localhost:8080/CameraRental/image/data/11/8/394d2624-62a7-4e73-b3e2-5dee7ed9dff1.small.jpg', '呃呃呃', null, '1', null), ('11', 'http://localhost:8080/CameraRental/image/data/15/12/d14e7d83-25bb-4696-9e43-e872c31dfec8.small.jpg', '阿斯蒂芬', null, '2', null);
COMMIT;

-- ----------------------------
--  Table structure for `t_brand`
-- ----------------------------
DROP TABLE IF EXISTS `t_brand`;
CREATE TABLE `t_brand` (
  `brandId` int(11) NOT NULL AUTO_INCREMENT,
  `mark` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`brandId`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_brand`
-- ----------------------------
BEGIN;
INSERT INTO `t_brand` VALUES ('3', null, '品牌一'), ('6', null, '品牌一'), ('7', null, '品牌一'), ('8', null, '品牌一'), ('9', '品牌啊', '品牌一'), ('10', null, '品牌一'), ('11', null, '品牌一'), ('12', 'asdf', 'sdf');
COMMIT;

-- ----------------------------
--  Table structure for `t_camera`
-- ----------------------------
DROP TABLE IF EXISTS `t_camera`;
CREATE TABLE `t_camera` (
  `cameraId` int(11) NOT NULL AUTO_INCREMENT,
  `antiShake` varchar(255) DEFAULT NULL,
  `batteryModel` varchar(255) DEFAULT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `brandId` int(11) DEFAULT NULL,
  `continuousShootingSpeed` varchar(255) DEFAULT NULL,
  `flashLamp` varchar(255) DEFAULT NULL,
  `focusCount` varchar(255) DEFAULT NULL,
  `frame` varchar(255) DEFAULT NULL,
  `gps` varchar(255) DEFAULT NULL,
  `mark` varchar(255) DEFAULT NULL,
  `memoryCardType` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `pictureResolution` varchar(255) DEFAULT NULL,
  `pixel` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `screenResolution` varchar(255) DEFAULT NULL,
  `screenSize` varchar(255) DEFAULT NULL,
  `sensitivity` varchar(255) DEFAULT NULL,
  `shootingNumber` varchar(255) DEFAULT NULL,
  `shutterSpeed` varchar(255) DEFAULT NULL,
  `size` varchar(255) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `videoResolution` varchar(255) DEFAULT NULL,
  `weight` varchar(255) DEFAULT NULL,
  `wifi` varchar(255) DEFAULT NULL,
  `deposit` int(11) DEFAULT NULL,
  PRIMARY KEY (`cameraId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_camera`
-- ----------------------------
BEGIN;
INSERT INTO `t_camera` VALUES ('2', '否', null, '尼康', '3', null, null, '12点', '全', null, '阿斯蒂芬', '雷克萨家分店', '7000', '12', '12000', '6000', null, null, '', null, null, null, '0', '啊啊', null, null, '123'), ('3', null, null, '品牌', '2', null, null, '2131', '54654', null, '5456', null, '型号', null, '54654', '65465', null, null, null, null, null, null, '0', null, null, null, '12'), ('4', '7676', '7878', '品牌一', '6', '787', '787', '76', '333', '787', '32', '8787', '333', '767', '234766', '333', '787', '7878', '787', '787', '8787', '7878', '0', 'u8787', '787', '7878', '33');
COMMIT;

-- ----------------------------
--  Table structure for `t_camera_category`
-- ----------------------------
DROP TABLE IF EXISTS `t_camera_category`;
CREATE TABLE `t_camera_category` (
  `cameraId` int(11) NOT NULL,
  `categoryId` int(11) NOT NULL,
  PRIMARY KEY (`cameraId`,`categoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_camera_category`
-- ----------------------------
BEGIN;
INSERT INTO `t_camera_category` VALUES ('2', '8');
COMMIT;

-- ----------------------------
--  Table structure for `t_cameralens`
-- ----------------------------
DROP TABLE IF EXISTS `t_cameralens`;
CREATE TABLE `t_cameralens` (
  `lensId` int(11) NOT NULL AUTO_INCREMENT,
  `antiShake` varchar(255) DEFAULT NULL,
  `apertureNumber` varchar(255) DEFAULT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `brandId` int(11) DEFAULT NULL,
  `focusRange` varchar(255) DEFAULT NULL,
  `kaKou` varchar(255) DEFAULT NULL,
  `lensAperture` varchar(255) DEFAULT NULL,
  `mark` varchar(255) DEFAULT NULL,
  `maxAperture` varchar(255) DEFAULT NULL,
  `maxMagnification` varchar(255) DEFAULT NULL,
  `minAperture` varchar(255) DEFAULT NULL,
  `minFocusDistance` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `size` varchar(255) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `weight` varchar(255) DEFAULT NULL,
  `deposit` int(11) DEFAULT NULL,
  PRIMARY KEY (`lensId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_cameralens`
-- ----------------------------
BEGIN;
INSERT INTO `t_cameralens` VALUES ('1', 'gdfgfdgf', 'hgfhgf', '品牌一', '3', 'gfdgfdgf', 'hgfhgfhg', 'hgfhgfhg', '阿斯蒂芬', 'gdgfdgfdg', 'gfhgfhgf', '5456FDSFDSgfd', 'hgfgfgd', '阿斯蒂芬', '阿斯蒂芬', 'fhgfhgf', '0', 'hgfhgf', '13');
COMMIT;

-- ----------------------------
--  Table structure for `t_captcha`
-- ----------------------------
DROP TABLE IF EXISTS `t_captcha`;
CREATE TABLE `t_captcha` (
  `captchaId` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`captchaId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_captcha`
-- ----------------------------
BEGIN;
INSERT INTO `t_captcha` VALUES ('1', '936033', '1437903574472', '验证码发送成功', '15989348952');
COMMIT;

-- ----------------------------
--  Table structure for `t_car`
-- ----------------------------
DROP TABLE IF EXISTS `t_car`;
CREATE TABLE `t_car` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `carAge` int(11) DEFAULT NULL,
  `carNumber` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_category`
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category` (
  `categoryId` int(11) NOT NULL AUTO_INCREMENT,
  `level` int(11) DEFAULT NULL,
  `mark` varchar(1000) DEFAULT NULL,
  `name` varchar(120) DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL,
  PRIMARY KEY (`categoryId`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_category`
-- ----------------------------
BEGIN;
INSERT INTO `t_category` VALUES ('3', '1', '很多', '很多', '0'), ('7', '2', '阿斯蒂芬', '暗室逢灯', '3'), ('8', '2', '阿斯蒂芬', '阿斯蒂芬', '3');
COMMIT;

-- ----------------------------
--  Table structure for `t_imageinfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_imageinfo`;
CREATE TABLE `t_imageinfo` (
  `imageId` int(11) NOT NULL AUTO_INCREMENT,
  `category` int(11) DEFAULT NULL,
  `fileName` varchar(255) DEFAULT NULL,
  `foreignKey` int(11) DEFAULT NULL,
  `fullPath` varchar(255) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `orgName` varchar(255) DEFAULT NULL,
  `saveDir` varchar(255) DEFAULT NULL,
  `size` int(11) NOT NULL,
  `uploadDate` bigint(20) DEFAULT NULL,
  `url` varchar(300) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  PRIMARY KEY (`imageId`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_imageinfo`
-- ----------------------------
BEGIN;
INSERT INTO `t_imageinfo` VALUES ('82', '1', '07b4f973-b43c-4434-9bc0-0f078553cdab.jpg', '4', '/Users/apple/Desktop/test/14/13/07b4f973-b43c-4434-9bc0-0f078553cdab.jpg', '1357', '营业执照.jpg', '/Users/apple/Desktop/test/14/13', '2', '1499741966051', 'http://localhost:8080/CameraRental/image/data/14/13/07b4f973-b43c-4434-9bc0-0f078553cdab.jpg', '1920'), ('83', '1', '07b4f973-b43c-4434-9bc0-0f078553cdab.small.jpg', '4', '/Users/apple/Desktop/test/14/13/07b4f973-b43c-4434-9bc0-0f078553cdab.small.jpg', '141', '营业执照.jpg', '/Users/apple/Desktop/test/14/13', '1', '1499741966051', 'http://localhost:8080/CameraRental/image/data/14/13/07b4f973-b43c-4434-9bc0-0f078553cdab.small.jpg', '200'), ('84', '1', '2001ea17-6ace-4e21-8e8e-7ed676a74bca.jpg', '2', '/Users/apple/Desktop/test/14/13/2001ea17-6ace-4e21-8e8e-7ed676a74bca.jpg', '1357', '营业执照.jpg', '/Users/apple/Desktop/test/14/13', '2', '1499742336983', 'http://localhost:8080/CameraRental/image/data/14/13/2001ea17-6ace-4e21-8e8e-7ed676a74bca.jpg', '1920'), ('85', '1', '2001ea17-6ace-4e21-8e8e-7ed676a74bca.small.jpg', '2', '/Users/apple/Desktop/test/14/13/2001ea17-6ace-4e21-8e8e-7ed676a74bca.small.jpg', '141', '营业执照.jpg', '/Users/apple/Desktop/test/14/13', '1', '1499742336983', 'http://localhost:8080/CameraRental/image/data/14/13/2001ea17-6ace-4e21-8e8e-7ed676a74bca.small.jpg', '200'), ('86', '1', 'ad7bc9b2-c285-4924-8990-448681e33530.jpg', '2', '/Users/apple/Desktop/test/14/4/ad7bc9b2-c285-4924-8990-448681e33530.jpg', '160', '寸照.jpg', '/Users/apple/Desktop/test/14/4', '2', '1499742346594', 'http://localhost:8080/CameraRental/image/data/14/4/ad7bc9b2-c285-4924-8990-448681e33530.jpg', '110'), ('88', '1', 'c506a126-fe6d-4896-a65f-f85563f206fd.jpg', '2', '/Users/apple/Desktop/test/12/2/c506a126-fe6d-4896-a65f-f85563f206fd.jpg', '1920', 'Android生命周期the complete android activity and fragment lifecy.jpg', '/Users/apple/Desktop/test/12/2', '2', '1499742359433', 'http://localhost:8080/CameraRental/image/data/12/2/c506a126-fe6d-4896-a65f-f85563f206fd.jpg', '899'), ('89', '1', 'c506a126-fe6d-4896-a65f-f85563f206fd.small.jpg', '2', '/Users/apple/Desktop/test/12/2/c506a126-fe6d-4896-a65f-f85563f206fd.small.jpg', '200', 'Android生命周期the complete android activity and fragment lifecy.jpg', '/Users/apple/Desktop/test/12/2', '1', '1499742359433', 'http://localhost:8080/CameraRental/image/data/12/2/c506a126-fe6d-4896-a65f-f85563f206fd.small.jpg', '93');
COMMIT;

-- ----------------------------
--  Table structure for `t_lens_category`
-- ----------------------------
DROP TABLE IF EXISTS `t_lens_category`;
CREATE TABLE `t_lens_category` (
  `categoryId` int(11) NOT NULL,
  `lensId` int(11) NOT NULL,
  PRIMARY KEY (`categoryId`,`lensId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_lens_category`
-- ----------------------------
BEGIN;
INSERT INTO `t_lens_category` VALUES ('3', '1'), ('4', '1');
COMMIT;

-- ----------------------------
--  Table structure for `t_notifymessage`
-- ----------------------------
DROP TABLE IF EXISTS `t_notifymessage`;
CREATE TABLE `t_notifymessage` (
  `notifyId` int(11) NOT NULL AUTO_INCREMENT,
  `charset` varchar(255) DEFAULT NULL,
  `errorCode` varchar(255) DEFAULT NULL,
  `errorMsg` varchar(255) DEFAULT NULL,
  `ext1` varchar(255) DEFAULT NULL,
  `ext2` varchar(255) DEFAULT NULL,
  `instCode` varchar(255) DEFAULT NULL,
  `merchantNo` varchar(255) DEFAULT NULL,
  `msgSender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `orderAmount` varchar(255) DEFAULT NULL,
  `orderNo` varchar(255) DEFAULT NULL,
  `sendTime` varchar(255) DEFAULT NULL,
  `signMsg` varchar(255) DEFAULT NULL,
  `signType` varchar(255) DEFAULT NULL,
  `traceNo` varchar(255) DEFAULT NULL,
  `transAmount` varchar(255) DEFAULT NULL,
  `transNo` varchar(255) DEFAULT NULL,
  `transStatus` varchar(255) DEFAULT NULL,
  `transTime` varchar(255) DEFAULT NULL,
  `transType` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`notifyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_order`
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `orderId` varchar(255) NOT NULL,
  `amount` int(11) DEFAULT NULL,
  `isDelete` int(11) DEFAULT NULL,
  `description` bigint(20) DEFAULT NULL,
  `rentalDays` int(11) DEFAULT NULL,
  `rentalId` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  `deposit` int(11) NOT NULL,
  `finishTime` bigint(20) DEFAULT NULL,
  `mark` varchar(400) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `nickName` varchar(255) DEFAULT NULL,
  `obtainTime` bigint(20) DEFAULT NULL,
  `refund` int(11) DEFAULT NULL,
  `returnTime` bigint(20) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `category` int(11) DEFAULT NULL,
  `invitationUserId` int(11) DEFAULT NULL,
  `useTime` bigint(20) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `rentalName` varchar(255) DEFAULT NULL,
  `rentalUserId` int(11) NOT NULL,
  `rentalNickName` varchar(255) DEFAULT NULL,
  `accetpTime` bigint(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `cashAmount` double DEFAULT NULL,
  `cashId` int(11) DEFAULT NULL,
  `cover` varchar(255) DEFAULT NULL,
  `deductibleInsurance` double DEFAULT NULL,
  `insurance` double DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `newAmount` double DEFAULT NULL,
  `newDeductibleInsurance` double DEFAULT NULL,
  `newInsurance` double DEFAULT NULL,
  `newObtainTime` bigint(20) DEFAULT NULL,
  `newRentalAmount` double DEFAULT NULL,
  `newRentalDays` int(11) DEFAULT NULL,
  `newReturnTime` bigint(20) DEFAULT NULL,
  `payAmount` double DEFAULT NULL,
  `price` double DEFAULT NULL,
  `refundRental` double DEFAULT NULL,
  `rentalAmount` double DEFAULT NULL,
  `share` int(11) DEFAULT NULL,
  PRIMARY KEY (`orderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_orderdetail`
-- ----------------------------
DROP TABLE IF EXISTS `t_orderdetail`;
CREATE TABLE `t_orderdetail` (
  `detailId` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `orderNumber` varchar(255) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`detailId`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_orderdetail`
-- ----------------------------
BEGIN;
INSERT INTO `t_orderdetail` VALUES ('34', '订单已经生成', null, '20150801200844708', '1438430924708', '3'), ('35', '机主接受订单', 'admin', '20150801200844708', '1438430924708', '1'), ('36', '支付完成,订单金额:0', null, '20150801200844708', '1438430924708', '3'), ('37', '后台修改,用户订单', null, '20150801200844708', '1438440509212', '0'), ('38', '订单已经生成', null, '2015080123084346', '1438441723046', '3'), ('39', '订单已经生成', null, '20150801231028379', '1438441828379', '3'), ('40', '订单已经生成', null, '20150801231236974', '1438441956975', '3'), ('41', '订单已经生成', null, '20150801231340614', '1438442020614', '3'), ('42', '订单已经生成', null, '20150801231534516', '1438442134516', '3'), ('43', '机主接受订单', 'admin', '20150801231534516', '1438442134516', '1'), ('44', '支付完成,订单金额:0', null, '20150801231534516', '1438442134516', '3');
COMMIT;

-- ----------------------------
--  Table structure for `t_parking`
-- ----------------------------
DROP TABLE IF EXISTS `t_parking`;
CREATE TABLE `t_parking` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `coordinate` varchar(255) DEFAULT NULL,
  `floorLevels` int(11) DEFAULT NULL,
  `imageUrl` varchar(255) DEFAULT NULL,
  `mamager` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parkingImage` varchar(255) DEFAULT NULL,
  `parkingType` int(11) DEFAULT NULL,
  `passState` int(11) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `regionId` int(11) DEFAULT NULL,
  `remain` int(11) DEFAULT NULL,
  `startPrice` double DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_parkingfloor`
-- ----------------------------
DROP TABLE IF EXISTS `t_parkingfloor`;
CREATE TABLE `t_parkingfloor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `parkingId` int(11) DEFAULT NULL,
  `remain` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_region`
-- ----------------------------
DROP TABLE IF EXISTS `t_region`;
CREATE TABLE `t_region` (
  `regionId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL,
  `regionType` int(11) DEFAULT NULL,
  PRIMARY KEY (`regionId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_region`
-- ----------------------------
BEGIN;
INSERT INTO `t_region` VALUES ('2', '湖南省', '0', '1'), ('3', '长沙市', '2', '2'), ('4', '广西', '0', '1'), ('5', '岳麓区', '3', '3'), ('6', '柳州', '4', '2'), ('7', '广东', '0', '1');
COMMIT;

-- ----------------------------
--  Table structure for `t_rejecttime`
-- ----------------------------
DROP TABLE IF EXISTS `t_rejecttime`;
CREATE TABLE `t_rejecttime` (
  `pid` int(11) NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  `rentalId` int(11) NOT NULL,
  `mTime` bigint(20) DEFAULT NULL,
  `timeStr` int(11) DEFAULT NULL,
  `userId` int(11) NOT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_traderecord`
-- ----------------------------
DROP TABLE IF EXISTS `t_traderecord`;
CREATE TABLE `t_traderecord` (
  `tradeId` int(11) NOT NULL AUTO_INCREMENT,
  `amount` int(11) DEFAULT NULL,
  `isDelete` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `orderId` varchar(255) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `mark` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `orderNumber` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`tradeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `test_entity`
-- ----------------------------
DROP TABLE IF EXISTS `test_entity`;
CREATE TABLE `test_entity` (
  `pid` int(11) NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
