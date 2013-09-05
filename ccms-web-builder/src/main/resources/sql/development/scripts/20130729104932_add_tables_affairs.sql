--// add_tables_affairs
-- Migration SQL that makes the change goes here.
 CREATE TABLE tb_tc_affairs (
  pkid bigint(20) NOT NULL AUTO_INCREMENT,
  title varchar(50) DEFAULT NULL,
  important tinyint(4) DEFAULT NULL COMMENT '1-低，2-中，3-高',
  status tinyint(4) DEFAULT NULL COMMENT '1-未处理，2-处理中，3-处理完成，4-已关闭',
  founder varchar(30) DEFAULT NULL COMMENT '创建人',
  current_handler varchar(30) DEFAULT NULL COMMENT '当前处理人',
  expiration_time datetime DEFAULT NULL,
  note varchar(100) DEFAULT NULL COMMENT '事务备注',
  source varchar(10) DEFAULT NULL COMMENT '来源',
  source_type varchar(10) DEFAULT NULL COMMENT '来源备注',
  created datetime DEFAULT NULL,
  updated datetime DEFAULT NULL,
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE tb_tc_affairs_handle (
  pkid bigint(20) NOT NULL AUTO_INCREMENT,
  note varchar(100) DEFAULT NULL COMMENT '处理备注',
  founder varchar(30) DEFAULT NULL COMMENT '备注创建者',
  next_handler varchar(30) DEFAULT NULL COMMENT '下一位处理人',
  created datetime DEFAULT NULL,
  updated datetime DEFAULT NULL,
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE tb_tc_category (
  pkid bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(20) DEFAULT NULL,
  value varchar(20) DEFAULT NULL,
  description varchar(50) DEFAULT NULL,
  parent_id bigint(20) DEFAULT NULL,
  out_type varchar(20) DEFAULT NULL COMMENT '分类集合所属类型',
  out_id varchar(30) DEFAULT NULL COMMENT '分类集合所属id',
  created datetime DEFAULT NULL,
  updated datetime DEFAULT NULL,
  is_delete tinyint(4) DEFAULT NULL COMMENT '逻辑删除',
  PRIMARY KEY (pkid)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--//@UNDO
-- SQL to undo the change goes here.
drop table tb_tc_affairs;
drop table tb_tc_affairs_handle;
drop table tb_tc_category;


