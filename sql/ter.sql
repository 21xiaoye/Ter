/*
 Navicat Premium Data Transfer

 Source Server         : mysql8.0.12
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3307
 Source Schema         : ter

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 03/05/2024 16:07:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ter_barrage
-- ----------------------------
DROP TABLE IF EXISTS `ter_barrage`;
CREATE TABLE `ter_barrage`  (
  `barrageId` bigint(20) UNSIGNED NOT NULL COMMENT '弹幕唯一标识符',
  `barrageSend` int(10) UNSIGNED NOT NULL COMMENT '弹幕发送者',
  `videoId` int(10) UNSIGNED NOT NULL COMMENT '视频id',
  `barrageContent` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '弹幕内容',
  `sendTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `danmuTime` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '弹幕出现时间',
  `barrageColor` int(11) NULL DEFAULT NULL COMMENT '弹幕颜色',
  PRIMARY KEY (`barrageId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '弹幕表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_block_font
-- ----------------------------
DROP TABLE IF EXISTS `ter_block_font`;
CREATE TABLE `ter_block_font`  (
  `blockFontId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '敏感词标识符',
  `maskWord` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '敏感词内容',
  `category` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '敏感词类别',
  `level` enum('low','medium','high') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '铭感词等级',
  PRIMARY KEY (`blockFontId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '敏感词库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_column
-- ----------------------------
DROP TABLE IF EXISTS `ter_column`;
CREATE TABLE `ter_column`  (
  `columnId` bigint(20) UNSIGNED NOT NULL COMMENT '专栏唯一标识符',
  `columnCode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专栏编码',
  `columnIdContent` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专栏内容',
  `announcer` int(10) UNSIGNED NOT NULL COMMENT '发布者',
  `columnTitle` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专栏标题',
  `columnCover` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专栏封面',
  `columnStatus` int(11) NULL DEFAULT NULL COMMENT '专栏状态',
  `releaseTime` int(11) NULL DEFAULT NULL COMMENT '发布时间',
  `deleteTime` int(11) NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`columnId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '专栏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `ter_dict_data`;
CREATE TABLE `ter_dict_data`  (
  `dict_code` bigint(20) UNSIGNED NOT NULL COMMENT '字典编码',
  `dict_sort` int(11) NULL DEFAULT NULL COMMENT '字典排序',
  `dict_lobal` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典标签',
  `dict_value` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典键值',
  `dict_type` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典类型',
  `id_default` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否为默认值',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `ter_dict_type`;
CREATE TABLE `ter_dict_type`  (
  `dict_id` bigint(20) UNSIGNED NOT NULL COMMENT '字典主键',
  `dict_name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典名称',
  `dict_type` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典类型',
  PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_mail_origin
-- ----------------------------
DROP TABLE IF EXISTS `ter_mail_origin`;
CREATE TABLE `ter_mail_origin`  (
  `mailOriginId` bigint(20) UNSIGNED NOT NULL COMMENT '邮箱发送源id',
  `mailName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `mailPasswd` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码(授权码)',
  `mailHost` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务商',
  `protocol` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '协议',
  `port` int(11) NULL DEFAULT NULL COMMENT '端口',
  `encoding` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '编码格式',
  `createTime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`mailOriginId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '邮箱发送源' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_permission
-- ----------------------------
DROP TABLE IF EXISTS `ter_permission`;
CREATE TABLE `ter_permission`  (
  `permissionId` bigint(64) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限名',
  `url` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型为页面时，代表前端路由地址，类型为按钮时，代表后端接口地址',
  `type` int(2) NOT NULL COMMENT '权限类型，页面-1，按钮-2',
  `permission` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限表达式',
  `method` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后端接口访问方式',
  `sort` int(11) NOT NULL COMMENT '排序',
  `parentId` bigint(64) NOT NULL COMMENT '父级id',
  PRIMARY KEY (`permissionId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_role
-- ----------------------------
DROP TABLE IF EXISTS `ter_role`;
CREATE TABLE `ter_role`  (
  `roleId` bigint(64) UNSIGNED NOT NULL COMMENT '角色主键',
  `roleName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `description` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `createTime` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '创建时间',
  `updateTime` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`roleId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `ter_role_permission`;
CREATE TABLE `ter_role_permission`  (
  `roleId` bigint(20) UNSIGNED NOT NULL,
  `permissionId` bigint(20) UNSIGNED NOT NULL,
  PRIMARY KEY (`roleId`, `permissionId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_sys
-- ----------------------------
DROP TABLE IF EXISTS `ter_sys`;
CREATE TABLE `ter_sys`  (
  `sysId` bigint(20) UNSIGNED NOT NULL COMMENT '服务主键',
  `sysHost` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务ip',
  `sysPort` int(11) NULL DEFAULT NULL COMMENT '服务端口',
  `startTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '启动时间',
  `sysType` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务类型',
  PRIMARY KEY (`sysId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统服务信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_thumb_like
-- ----------------------------
DROP TABLE IF EXISTS `ter_thumb_like`;
CREATE TABLE `ter_thumb_like`  (
  `likeId` bigint(20) UNSIGNED NOT NULL COMMENT '点赞标识符',
  `infoId` bigint(20) UNSIGNED NOT NULL COMMENT '源头id',
  `likeUserId` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '点赞人id',
  `likeCreateTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '点赞创建时间',
  PRIMARY KEY (`likeId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '点赞记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_user
-- ----------------------------
DROP TABLE IF EXISTS `ter_user`;
CREATE TABLE `ter_user`  (
  `userId` bigint(20) UNSIGNED NOT NULL COMMENT '用户唯一标识符',
  `userName` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名称',
  `userAvatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `userEmail` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `userPasswd` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户密码',
  `userStatus` int(11) NULL DEFAULT 0 COMMENT '注销账号',
  `salt` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '盐值',
  `createTime` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '创建时间',
  `updateTime` bigint(20) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`userId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_user_collect
-- ----------------------------
DROP TABLE IF EXISTS `ter_user_collect`;
CREATE TABLE `ter_user_collect`  (
  `collectId` bigint(20) UNSIGNED NOT NULL COMMENT '收藏唯一标识符',
  `collectUser` bigint(20) UNSIGNED NOT NULL COMMENT '收藏用户',
  `collectOrigin` bigint(20) NULL DEFAULT NULL COMMENT '收藏源',
  `collectPath` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收藏路径',
  `collectFloder` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '收藏文件夹id',
  `collectType` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收藏类型',
  `collectTime` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`collectId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收藏信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_user_folder
-- ----------------------------
DROP TABLE IF EXISTS `ter_user_folder`;
CREATE TABLE `ter_user_folder`  (
  `folderId` bigint(20) UNSIGNED NOT NULL COMMENT '文件夹id',
  `floderUser` bigint(20) NULL DEFAULT NULL COMMENT '文件夹所属用户',
  `floderName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件夹名称',
  `floderPath` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件夹路径',
  `superId` bigint(20) NULL DEFAULT NULL COMMENT '上级id',
  PRIMARY KEY (`folderId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户文件夹' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_user_followers
-- ----------------------------
DROP TABLE IF EXISTS `ter_user_followers`;
CREATE TABLE `ter_user_followers`  (
  `userId` int(11) NOT NULL COMMENT '关注用户',
  `userFllower` int(11) NOT NULL COMMENT '被关注用户',
  `attentionTime` date NULL DEFAULT NULL COMMENT '关注时间',
  `unfollowTime` date NULL DEFAULT NULL COMMENT '取消关注时间',
  PRIMARY KEY (`userId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '关注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_user_role
-- ----------------------------
DROP TABLE IF EXISTS `ter_user_role`;
CREATE TABLE `ter_user_role`  (
  `userId` bigint(64) NOT NULL COMMENT '用户主键',
  `roleId` bigint(64) NOT NULL COMMENT '角色主键',
  PRIMARY KEY (`userId`, `roleId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_user_video_history
-- ----------------------------
DROP TABLE IF EXISTS `ter_user_video_history`;
CREATE TABLE `ter_user_video_history`  (
  `historyId` bigint(20) UNSIGNED NOT NULL COMMENT '观看唯一标识符',
  `userId` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '用户id',
  `videoId` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '视频id',
  `viewingTime` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '观看时间',
  `watchTime` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '观看时长',
  PRIMARY KEY (`historyId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '观看历史记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_video
-- ----------------------------
DROP TABLE IF EXISTS `ter_video`;
CREATE TABLE `ter_video`  (
  `videoId` bigint(20) UNSIGNED NOT NULL COMMENT '视频唯一标识符',
  `videoCode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '视频编码',
  `announcer` int(10) UNSIGNED NOT NULL COMMENT '发布者',
  `videoContent` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '视频URL',
  `videoTitle` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '视频标题',
  `videoCover` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '视频封面URL',
  `videoDist` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '视频分区',
  `videoStatus` int(11) NULL DEFAULT 0 COMMENT '视频状态',
  `releaseTime` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '发布时间',
  `auditTime` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '审核时间',
  `deleteTime` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`videoId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '视频表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_video_comment_one
-- ----------------------------
DROP TABLE IF EXISTS `ter_video_comment_one`;
CREATE TABLE `ter_video_comment_one`  (
  `commentId` bigint(20) UNSIGNED NOT NULL COMMENT '一级评论唯一标识符',
  `commentSend` int(11) NULL DEFAULT NULL COMMENT '评论者',
  `uuid` int(11) NULL DEFAULT NULL COMMENT '源头',
  `commentContent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评论内容',
  `commentLikeCount` int(11) NULL DEFAULT NULL COMMENT '评论点赞数',
  `isTop` int(11) NULL DEFAULT 0 COMMENT '评论是否被置顶',
  `isPopular` int(11) NULL DEFAULT 0 COMMENT '是否为热门评论',
  `isDelete` int(11) NULL DEFAULT 0 COMMENT '是否被删除',
  `commentTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `updateTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`commentId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '视频一级评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_video_comment_two
-- ----------------------------
DROP TABLE IF EXISTS `ter_video_comment_two`;
CREATE TABLE `ter_video_comment_two`  (
  `commentId` bigint(20) UNSIGNED NOT NULL COMMENT '二级评论唯一标识符',
  `fatherId` bigint(20) UNSIGNED NOT NULL COMMENT '父级评论id',
  `replySend` bigint(20) UNSIGNED NOT NULL COMMENT '发送者',
  `respondent` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '被回复者',
  `videoId` bigint(20) UNSIGNED NOT NULL COMMENT '视频源头',
  `replyContent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '回复内容',
  `replyLikeCount` int(11) NULL DEFAULT NULL COMMENT '回复点赞数',
  `isDelete` int(11) NULL DEFAULT 0 COMMENT '是否被删除',
  `replyTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '回复时间',
  `updateTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`commentId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '视频二级评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_video_detail
-- ----------------------------
DROP TABLE IF EXISTS `ter_video_detail`;
CREATE TABLE `ter_video_detail`  (
  `videoId` bigint(20) UNSIGNED NOT NULL COMMENT '视频唯一标识符',
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '视频简介',
  `videoTag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '视频标签',
  `videoDuration` int(11) NULL DEFAULT NULL COMMENT '视频时长',
  `videoSpace` int(11) NULL DEFAULT NULL COMMENT '视频大小(mb）',
  ` videoPlayCount` int(11) NULL DEFAULT NULL COMMENT '视频播放数',
  `videoBarrageCount` int(11) NULL DEFAULT NULL COMMENT '视频弹幕数',
  `videoLikeCount` int(11) NULL DEFAULT NULL COMMENT '视频点赞数',
  `videoCollectCount` int(11) NULL DEFAULT NULL COMMENT '视频收藏数',
  `videoShare` int(11) NULL DEFAULT NULL COMMENT '视频分享数',
  PRIMARY KEY (`videoId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '视频明细表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
