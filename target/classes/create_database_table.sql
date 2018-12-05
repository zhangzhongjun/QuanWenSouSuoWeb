/*
 *	在mysql命令行中，执行
 *	source 路径
 *  mysql> source F:\睿云实验室\王剑锋\QuanWenSouSuoWeb\src\main\sql\create_database_table.sql
 *  注意路径后面不加分号
 *
*/
DROP DATABASE IF EXISTS QWSS;

CREATE DATABASE QWSS
  DEFAULT CHARSET utf8
  COLLATE utf8_general_ci;

USE QWSS;
-- 输出用户表
DROP TABLE IF EXISTS users;
-- 用户信息表
CREATE TABLE users (
  id       INT(11)     NOT NULL AUTO_INCREMENT,
  username VARCHAR(20) NOT NULL,
  password VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- 上传的文件信息
DROP TABLE IF EXISTS file_info;
-- 文件信息表
CREATE TABLE file_info (
  -- 使用uuid作为主键
  id         CHAR(36)     NOT NULL,
  -- 文件名
  filename   VARCHAR(100) NOT NULL,
  -- 在sftp服务器中的存储路径
  filepath   VARCHAR(500) NOT NULL,
  -- 上传时间
  uploadtime TIMESTAMP    NOT NULL,
  -- 所属用户的用户名
  userid     int          NOT NULL,
  -- 是否被删除
  isdeleted  BOOLEAN      not null DEFAULT FALSE,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


INSERT INTO users (username, password)
VALUES ('root', 'root');
INSERT INTO users (username, password)
VALUES ('admin', 'admin');

INSERT INTO file_info (id, filename, filepath, uploadtime, userid)
VALUE (uuid(), "我的.pdf", "f://我的.pdf", now(), 1);
INSERT INTO file_info (id, filename, filepath, uploadtime, userid)
VALUE (uuid(), "阿斯顿.pdf", "f://阿斯顿.docs", now(), 1);
INSERT INTO file_info (id, filename, filepath, uploadtime, userid)
VALUE (uuid(), "爱仕达.pdf", "f://爱仕达.pdf", now(), 2);
INSERT INTO file_info (id, filename, filepath, uploadtime, userid)
VALUE (uuid(), "暗示法.pdf", "f://暗示法.pdf", now(), 2);