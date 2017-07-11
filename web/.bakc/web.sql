/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50612
Source Host           : 127.0.0.1:3306
Source Database       : web

Target Server Type    : MYSQL
Target Server Version : 50612
File Encoding         : 65001

Date: 2014-11-20 10:54:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `news_templete`
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
-- Records of news_templete
-- ----------------------------
INSERT INTO `news_templete` VALUES ('1', '2014-11-10 10:07:33', '0', '<p style=\"line-height: 16px;\"><img src=\"/web/upload/ueditor/jsp/upload/image/20141110/1415585249969055988.jpg\" title=\"1415585249969055988.jpg\" alt=\"20140917181801.jpg\"/><img style=\"vertical-align: middle; margin-right: 2px;\" src=\"/web/ueditor/dialogs/attachment/fileTypeImages/icon_rar.gif\"/><a style=\"font-size:12px; color:#0066cc;\" href=\"/web/upload/ueditor/jsp/upload/file/20141106/1415240191989090305.rar\" title=\"51CTO下载-exe4j_win_v420 - 副本.rar\">51CTO下载-exe4j_win_v420 - 副本.rar</a></p><p class=\"picIntro\" style=\"text-align: left;\"><img src=\"http://img.baidu.com/hi/jx2/j_0002.gif\"/>台当局控制的太平岛码头兴建工程已启动，图为完工后示意图。 图片来源：台湾《中时电子报》<br/></p><p style=\"text-align: left;\">原标题：大陆南海7岛填海造陆 台担心严重冲击太平岛防务</p><p>【环\r\n球网综合报道】台湾《中国时报》报道称，中国大陆从今年开始，在南海多个岛礁进行大规模填海造陆，对太平岛形成“包围之势”，台“国安”单位相当重视。据\r\n早前报道，台“国安局长”李翔宙曾在台“立法院”坦言“非常担心”，称大陆目前在南海7个岛礁所进行的填海造陆作业，有5个是由最高领导人核定的，目标是\r\n“小岛堡垒化”和“大岛阵地化”。</p><p>李翔宙表示，以“华阳礁”为例，去年3月还只有1000平方米，到今年7月已经有14万平方米，到9月底已达18万平方米。</p><p>李翔宙说，解放军海军司令在9月下旬，史无前例地用一星期时间，逐岛视察这些岛礁的填海造陆工程，还在永暑礁视察三军联合作战想定的操演，借此宣示中国大陆在南海已经有全盘的战略规划。</p><p>报道称，长期关心南海局势的国民党“立委”林郁方指出，包括赤瓜礁、东门礁、南熏礁和华阳礁，对太平岛已呈现“包围态势”。有外国官员还认为，在华阳礁、赤瓜礁和南熏礁完工后，大陆就会宣布设立“南海防空识别区”。</p><p>林\r\n郁方表示，赤瓜礁、东门礁和南熏礁不仅已经对太平岛形成“包围”，与太平岛的距离更是“一个比一个近”，赤瓜礁在太平岛南方70公里、东门礁在东南方57\r\n公里，南熏礁更是在太平岛西南方只有30公里。不论中国大陆最后填海的目的是大型雷达站、码头或跑道，对太平岛的防务势将造成“严重的冲击”。<span class=\"ifengLogo\"><a href=\"http://www.ifeng.com/\" target=\"_blank\"></a></span></p><p>&nbsp;&nbsp;<video class=\"edui-upload-video  vjs-default-skin           video-js\" controls=\"\" preload=\"none\" width=\"420\" height=\"280\" src=\"/web/upload/ueditor/jsp/upload/video/20141029/1414577541133041267.mp4\" data-setup=\"{}\"><source src=\"/web/upload/ueditor/jsp/upload/video/20141029/1414577541133041267.mp4\" type=\"video/mp4\"/></video></p>', '台当局 太平岛码头兴建工程', '南海7岛 台湾 ');

-- ----------------------------
-- Table structure for `portfolio_templete`
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
-- Records of portfolio_templete
-- ----------------------------
INSERT INTO `portfolio_templete` VALUES ('1', '2014-11-06 10:17:00', '0', '<p><img alt=\"20140917181801.jpg\" src=\"/web/upload/ueditor/jsp/upload/image/20141106/1415240218974085997.jpg\" title=\"1415240218974085997.jpg\"/></p>', 'first', '2');
INSERT INTO `portfolio_templete` VALUES ('2', '2014-11-06 10:17:07', '0', '<p><br/><img alt=\"20140917181801.jpg\" src=\"/web/upload/ueditor/jsp/upload/image/20141106/1415240226491046097.jpg\" title=\"1415240226491046097.jpg\"/></p>', 'zzzz', '1');

-- ----------------------------
-- Table structure for `portfolio_type`
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
-- Records of portfolio_type
-- ----------------------------
INSERT INTO `portfolio_type` VALUES ('1', '2014-10-22 16:25:28', '0', '全部东西', 'ALL');
INSERT INTO `portfolio_type` VALUES ('2', '2014-10-22 16:25:00', '0', '第一个分类', 'UIPART');
INSERT INTO `portfolio_type` VALUES ('3', '2014-10-22 16:25:16', '0', '第二个分类', 'WEBPART');
INSERT INTO `portfolio_type` VALUES ('4', '2014-11-20 10:53:52', '0', '2', '2');
INSERT INTO `portfolio_type` VALUES ('5', '2014-11-20 10:53:56', '0', '3', '3');
INSERT INTO `portfolio_type` VALUES ('6', '2014-11-20 10:53:58', '0', '4', '4');
INSERT INTO `portfolio_type` VALUES ('7', '2014-11-20 10:54:01', '0', '5', '5');
INSERT INTO `portfolio_type` VALUES ('8', '2014-11-20 10:54:04', '0', '6', '6');
INSERT INTO `portfolio_type` VALUES ('9', '2014-11-20 10:54:08', '0', '7', '7');
INSERT INTO `portfolio_type` VALUES ('10', '2014-11-20 10:54:11', '0', '8', '8');
INSERT INTO `portfolio_type` VALUES ('11', '2014-11-20 10:54:15', '0', '9', '9');

-- ----------------------------
-- Table structure for `sysman_resource`
-- ----------------------------
DROP TABLE IF EXISTS `sysman_resource`;
CREATE TABLE `sysman_resource` (
  `pid` int(11) NOT NULL,
  `createTime` datetime DEFAULT NULL,
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
-- Records of sysman_resource
-- ----------------------------
INSERT INTO `sysman_resource` VALUES ('1', '2014-08-27 16:05:40', '0', '系统管理', '/', '系统管理', '1', '1', null);
INSERT INTO `sysman_resource` VALUES ('2', '2014-11-06 16:07:13', '0', '角色管理', '/admin/sysmanRole/list', '角色管理', '2', '1', '5');
INSERT INTO `sysman_resource` VALUES ('3', '2014-11-06 16:07:08', '0', '用户管理', '/admin/sysmanUser/list', '用户管理', '1', '1', '5');
INSERT INTO `sysman_resource` VALUES ('4', '2014-10-13 15:41:02', '0', '资源管理', '/admin/sysmanResource/list', '资源管理', '3', '1', '5');
INSERT INTO `sysman_resource` VALUES ('5', '2014-11-06 16:07:04', '0', '账号管理', '/', '账号管理', '1', '1', '1');
INSERT INTO `sysman_resource` VALUES ('6', '2014-10-16 17:50:32', '1', '首页管理', '/admin/page/home', '首页管理', '1', '1', '1');
INSERT INTO `sysman_resource` VALUES ('7', '2014-10-16 18:18:46', '0', '模板管理', '/', '模板管理', '2', '1', '1');
INSERT INTO `sysman_resource` VALUES ('8', '2014-10-16 18:23:19', '0', '新闻页模板管理', '/admin/page/templete/newsTemplete/list', '新闻页模板管理', '1', '1', '7');
INSERT INTO `sysman_resource` VALUES ('9', '2014-10-22 16:11:46', '0', '代表作品集-类型', '/admin/page/templete/portfolioType/list', '代表作品集-类型', '2', '1', '7');
INSERT INTO `sysman_resource` VALUES ('10', '2014-10-22 16:49:26', '0', '代表作品集', '/admin/page/templete/portfolioTemplete/list', '代表作品集', '3', '1', '7');

-- ----------------------------
-- Table structure for `sysman_role`
-- ----------------------------
DROP TABLE IF EXISTS `sysman_role`;
CREATE TABLE `sysman_role` (
  `pid` int(11) NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `creater_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`pid`),
  KEY `FK7D89020860C1C1FC` (`creater_id`),
  CONSTRAINT `FK7D89020860C1C1FC` FOREIGN KEY (`creater_id`) REFERENCES `sysman_user` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sysman_role
-- ----------------------------
INSERT INTO `sysman_role` VALUES ('1', '2014-10-29 18:16:47', '0', '系统管理员1', '系统管理员', null);

-- ----------------------------
-- Table structure for `sysman_role_resource`
-- ----------------------------
DROP TABLE IF EXISTS `sysman_role_resource`;
CREATE TABLE `sysman_role_resource` (
  `role_id` int(11) NOT NULL,
  `resource_id` int(11) NOT NULL,
  KEY `FK17BAC656127E527` (`role_id`),
  KEY `FK17BAC653B9CBFA7` (`resource_id`),
  CONSTRAINT `FK17BAC653B9CBFA7` FOREIGN KEY (`resource_id`) REFERENCES `sysman_resource` (`pid`),
  CONSTRAINT `FK17BAC656127E527` FOREIGN KEY (`role_id`) REFERENCES `sysman_role` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sysman_role_resource
-- ----------------------------
INSERT INTO `sysman_role_resource` VALUES ('1', '1');
INSERT INTO `sysman_role_resource` VALUES ('1', '5');
INSERT INTO `sysman_role_resource` VALUES ('1', '3');
INSERT INTO `sysman_role_resource` VALUES ('1', '2');
INSERT INTO `sysman_role_resource` VALUES ('1', '4');
INSERT INTO `sysman_role_resource` VALUES ('1', '7');
INSERT INTO `sysman_role_resource` VALUES ('1', '8');
INSERT INTO `sysman_role_resource` VALUES ('1', '9');
INSERT INTO `sysman_role_resource` VALUES ('1', '10');

-- ----------------------------
-- Table structure for `sysman_user`
-- ----------------------------
DROP TABLE IF EXISTS `sysman_user`;
CREATE TABLE `sysman_user` (
  `pid` int(11) NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(32) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `position` varchar(20) DEFAULT NULL,
  `position_desc` varchar(100) DEFAULT NULL,
  `real_name` varchar(10) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `userName` varchar(50) NOT NULL,
  `creater_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`pid`),
  KEY `FK7D8A6D5D60C1C1FC` (`creater_id`),
  CONSTRAINT `FK7D8A6D5D60C1C1FC` FOREIGN KEY (`creater_id`) REFERENCES `sysman_user` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sysman_user
-- ----------------------------
INSERT INTO `sysman_user` VALUES ('1', '2014-11-06 15:57:08', '0', '11111111', '21232f297a57a5a743894a0e4a801fc3', '23123', 'a', 'aaaaa', 'admim', '1', 'admin', null);

-- ----------------------------
-- Table structure for `sysman_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sysman_user_role`;
CREATE TABLE `sysman_user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  KEY `FKD0596186127E527` (`role_id`),
  KEY `FKD059618652A907` (`user_id`),
  CONSTRAINT `FKD0596186127E527` FOREIGN KEY (`role_id`) REFERENCES `sysman_role` (`pid`),
  CONSTRAINT `FKD059618652A907` FOREIGN KEY (`user_id`) REFERENCES `sysman_user` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sysman_user_role
-- ----------------------------
INSERT INTO `sysman_user_role` VALUES ('1', '1');

-- ----------------------------
-- Table structure for `test_entity`
-- ----------------------------
DROP TABLE IF EXISTS `test_entity`;
CREATE TABLE `test_entity` (
  `pid` int(11) NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of test_entity
-- ----------------------------
