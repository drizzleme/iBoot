DROP TABLE IF EXISTS `user_info`;  
CREATE TABLE `user_info` (  
  `user_id` int(11) NOT NULL AUTO_INCREMENT,  
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',  
  `user_pwd` varchar(50) DEFAULT NULL COMMENT '用户密码',  
  `user_phone` varchar(11) DEFAULT NULL COMMENT '用户手机号码',  
  `user_email` varchar(50) DEFAULT NULL COMMENT '用户邮箱',  
  `user_telephone` varchar(20) DEFAULT NULL COMMENT '固定电话 区号-号码',  
  `user_state` int(1) DEFAULT '0' COMMENT '用户状态 0禁用 1正常 2未激活',  
  `user_gender` enum('woman','man') DEFAULT 'man' COMMENT '用户性别',  
  `lasttime` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',  
  `createtime` timestamp NULL DEFAULT NULL COMMENT '用户创建时间',  
  `country` varchar(20) DEFAULT NULL,  
  `city` varchar(20) DEFAULT NULL,  
  `address` varchar(200) DEFAULT NULL,  
  `logoimg` varchar(200) DEFAULT NULL,  
  `level` int(3) DEFAULT '1' COMMENT '0管理 1房东 2财务 3房客 4个人',  
  `ali_pay` varchar(50) DEFAULT NULL COMMENT '支付宝账户',  
  `user_nicename` varchar(20) DEFAULT NULL COMMENT '用户昵称',  
  `role_id` int(11) DEFAULT '1' COMMENT '角色ID 当前角色ID',  
  `role_changetime` timestamp NULL DEFAULT NULL COMMENT '角色切换时间',  
  PRIMARY KEY (`user_id`)  
) ENGINE=InnoDB AUTO_INCREMENT=153 DEFAULT CHARSET=utf8;  

DROP TABLE IF EXISTS `user_role`;  
CREATE TABLE `user_role` (  
  `id` int(8) NOT NULL AUTO_INCREMENT COMMENT '角色id',  
  `role_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '角色名',  
  PRIMARY KEY (`id`)  
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_role_permission`;  
CREATE TABLE `user_role_permission` (  
  `id` int(8) NOT NULL AUTO_INCREMENT COMMENT '权限id',  
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',  
  `role_id` int(8) DEFAULT NULL COMMENT '角色id',  
  `permission_bit_operator` varchar(12) CHARACTER SET utf8 DEFAULT NULL COMMENT ' 权限位符 (下标 0:通断电 1：退开房 2：能否邀请 3：修改单价 4：修改组 5:修改设备昵称 6:修改电流 7:添加设备 8:删除设备 9:催租 10:分配管理员 11:能否充值)',  
  `parent_uid` int(11) DEFAULT NULL COMMENT '所属用户ID 上级用户ID',  
  PRIMARY KEY (`id`)  
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;  


DROP TABLE IF EXISTS `user_access_log`;  
CREATE TABLE `user_access_log` (  
  `id` int(8) NOT NULL AUTO_INCREMENT COMMENT 'log id',  
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',  
  `record_type` int(8) DEFAULT NULL COMMENT '记录类型',  
  `record_address` varchar(20) DEFAULT NULL COMMENT '记录的ip',  
  `device_model` varchar(20) DEFAULT NULL COMMENT '设备',  
  `device_version` varchar(20) DEFAULT NULL COMMENT '版本',  
  `record_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',  
  `system_version` varchar(20) NULL DEFAULT NULL COMMENT '系统版本',  
  PRIMARY KEY (`id`)  
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



INSERT INTO `user_info` VALUES ('1', '123456', '123456', '887', '455646@qq.com', 'aaa', '1', 'man', '2016-08-05 09:12:25', '2015-10-23 11:12:58', '中国', 'ui', '区', null, '0', null, null, '1', null);  
INSERT INTO `user_info` VALUES ('39', 'hus', '123456', '13818971563', '', '', '1', 'man', '2016-06-18 12:08:29', '2015-11-10 15:35:42', '', null, null, null, '1', null, null, '1', null);  
INSERT INTO `user_info` VALUES ('45', '8787', '123467', '56565665', null, null, '1', 'man', '2016-08-05 16:08:59', '2015-11-16 11:32:13', '', null, null, '/upload/45/img/090000987,0129151c_20160626173439332.jpg', '1', null, null, '1', null);
INSERT INTO `user_info` VALUES ('77', '好的', null, '11111111', null, null, '1', 'woman', '2016-08-03 17:41:09', '2016-03-29 15:28:08', '', null, null, '/upload/77/img/advert_20160630152458434.jpg', '1', null, null, '3', null);
INSERT INTO `user_info` VALUES ('116', '18895318484', '123456', '18895318484', null, null, '1', 'man', '2016-08-01 10:28:57', '2016-04-27 11:41:27', '中国', '上海', '宝山区', '/upload/116/img/headerImg_20160428114743452.png', '3', null, null, '3', null);


INSERT INTO `user_role` VALUES ('1', '房东');  
INSERT INTO `user_role` VALUES ('2', '管理员');  
INSERT INTO `user_role` VALUES ('3', '房客');

INSERT INTO `user_role_permission` VALUES ('1', '45', '1', '10101100001', null);  
INSERT INTO `user_role_permission` VALUES ('2', '77', '2', '10101100001', '45');  
INSERT INTO `user_role_permission` VALUES ('3', '116', '3', '10101100001', '77');  
INSERT INTO `user_role_permission` VALUES ('4', '1', '1', '10101100001', null); 

