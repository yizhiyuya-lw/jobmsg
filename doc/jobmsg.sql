/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50717 (5.7.17-log)
 Source Host           : localhost:3306
 Source Schema         : jobmsg

 Target Server Type    : MySQL
 Target Server Version : 50717 (5.7.17-log)
 File Encoding         : 65001

 Date: 01/11/2022 20:49:00
*/
/* !40100 DEFAULT CHARACTER SET utf8 */
CREATE DATABASE `jobmsg`;
USE `jobmsg`;


-- ----------------------------
-- Table structure for job_info
-- ----------------------------
DROP TABLE IF EXISTS `job_info`;
CREATE TABLE `job_info`  (
  `l_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `vc_job_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `d_add_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  `d_update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `vc_userid` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '添加用户id',
  `vc_alarm_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务执行失败，通知类型：EMAIL邮件，DINGD：钉钉，SMS：手机短信',
  `vc_alarm_conf` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行失败通知配置，根据通知类型确定，数据格式为json',
  `vc_schedule_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度类型，CRON:cron表达式',
  `vc_schedule_conf` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度配置，如类型为CRON，则需配置cron具体表达式',
  `vc_expired_policy` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '过期策略，DO_NOTHING：跳过不执行，EXEC_ONCE_NOW:立即执行一次',
  `vc_executor_handler` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务执行类名',
  `vc_executor_param` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务执行参数，json格式',
  `l_executor_timeout` int(11) NOT NULL DEFAULT -1 COMMENT '任务执行超时时间，单位：秒，-1表示不用处理',
  `l_executor_retry_count` int(11) NOT NULL DEFAULT -1 COMMENT '任务失败重试次数，-1表示不需处理',
  `c_trigger_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度状态：0 停止  1 运行',
  `l_trigger_last_time` bigint(20) NOT NULL COMMENT '上次调度时间',
  `l_trigger_next_time` bigint(20) NOT NULL COMMENT '下次调度时间',
  PRIMARY KEY (`l_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for job_log
-- ----------------------------
DROP TABLE IF EXISTS `job_log`;
CREATE TABLE `job_log`  (
  `l_id` int(11) NOT NULL COMMENT '主键id',
  `l_job_id` int(11) NOT NULL COMMENT '任务id',
  `vc_executor_type` int(11) NOT NULL COMMENT '任务类型',
  `vc_executor_param` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务执行参数',
  `d_trigger_time` datetime NOT NULL COMMENT '任务触发时间',
  `l_trigger_code` int(11) NOT NULL COMMENT '触发结果code',
  `vc_trigger_msg` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '触发结果msg',
  `d_handle_time` datetime NOT NULL COMMENT '任务开始执行时间',
  `l_handle_code` int(11) NOT NULL COMMENT '任务执行结果code',
  `vc_handle_msg` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务执行结果msg',
  `l_alarm_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '任务执行失败通知结果：0默认，1无需通知，2通知成功，3通知失败',
  `l_retry_count` int(11) NOT NULL DEFAULT 0 COMMENT '重试次数：0默认',
  PRIMARY KEY (`l_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
