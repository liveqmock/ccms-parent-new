--// CCMS-XXXX_modify_affairs_add_ids_colum
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_affairs
ADD COLUMN source_id  bigint(20) NULL COMMENT '来源id（tb_tc_category表中pkid）' AFTER note,
ADD COLUMN source_type_id  bigint(20) NULL COMMENT '类型id（tb_tc_category表中pkid）' AFTER source;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_affairs
DROP COLUMN source_id,
DROP COLUMN source_type_id,
MODIFY COLUMN source  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源' AFTER note,
MODIFY COLUMN source_type  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源备注' AFTER source;


