--// create vw_taobao_order_quota
-- Migration SQL that makes the change goes here.

CREATE
    VIEW vw_taobao_order_quota 
    AS
(SELECT
	SUM(i.discount_fee) discount_fee,
	o.payment payment,
	o.post_fee post_fee,
	o.receiver_district receiver_district,
	o.tid tid,
	o.trade_from trade_from,
	SUM(i.num) product_amount,
	COUNT(1) product_count,
	GROUP_CONCAT(i.num_iid) num_iids
FROM tb_tc_taobao_order o,tb_tc_taobao_order_item i WHERE o.tid=i.tid
GROUP BY tid);

--//@UNDO
-- SQL to undo the change goes here.

DROP VIEW vw_taobao_order_quota;
