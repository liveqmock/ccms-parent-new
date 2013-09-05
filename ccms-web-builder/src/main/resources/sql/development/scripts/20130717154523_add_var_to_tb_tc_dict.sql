--// add var to tb_tc_dict
-- Migration SQL that makes the change goes here.

INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#REFUND_FEE#', 'refund_fee', 1, 15, '退款金额');

INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#REFUND_SUCCESS_TIME#', 'success_time', 1, 16, '退款成功时间');

--//@UNDO
-- SQL to undo the change goes here.

DELETE FROM tb_tc_dict where type = 4 and code = '#REFUND_FEE#';

DELETE FROM tb_tc_dict where type = 4 and code = '#REFUND_SUCCESS_TIME#';
