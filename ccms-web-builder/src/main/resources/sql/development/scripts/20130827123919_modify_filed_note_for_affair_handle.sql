--// modify_filed_note_for_affair_handle
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_affairs_handle
MODIFY COLUMN note  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理备注' AFTER affairs_id;

ALTER TABLE tb_tc_affairs
MODIFY COLUMN title  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER customerno,
MODIFY COLUMN note  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '事务备注' AFTER expiration_time;



--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_affairs_handle
MODIFY COLUMN note  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理备注' AFTER affairs_id;

ALTER TABLE tb_tc_affairs
MODIFY COLUMN title  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER customerno,
MODIFY COLUMN note  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '事务备注' AFTER expiration_time;
