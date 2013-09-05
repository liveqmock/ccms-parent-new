--// refund_proof_table
-- Migration SQL that makes the change goes here.

CREATE TABLE tb_tc_refund_proof_file (
  pkid int(11) NOT NULL AUTO_INCREMENT,
  file_name varchar(100) COLLATE utf8_bin NOT NULL COMMENT '文件名',
  path varchar(500) COLLATE utf8_bin NOT NULL COMMENT '凭证文件路径',
  created timestamp NULL DEFAULT NULL,
  updated timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='退款事务凭证表';


CREATE TABLE tb_tc_refund_top_content (
  pkid int(11) NOT NULL AUTO_INCREMENT,
  content varchar(100) COLLATE utf8_bin NOT NULL COMMENT '常用话术内容',
  created timestamp NULL DEFAULT NULL,
  updated timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='退款事务常用话术表';


--//@UNDO
-- SQL to undo the change goes here.
drop table if exists tb_tc_refund_proof_file;
drop table if exists tb_tc_refund_top_content;
