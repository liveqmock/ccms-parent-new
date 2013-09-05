--// modify receiver state rule
-- Migration SQL that makes the change goes here.

update tm_db_column set db_name = 'receiver_location' where column_id = 605 and table_id = 11 ;

DROP VIEW if exists vw_taobao_order_quota;

CREATE
    VIEW vw_taobao_order_quota
    AS
(SELECT
	o.customerno customerno,
	o.dp_id dp_id,
	SUM(i.discount_fee) discount_fee,
	o.payment payment,
	o.post_fee post_fee,
	CONCAT(o.receiver_state, ',', o.receiver_city) receiver_location,
	o.tid tid,
	o.trade_from trade_from,
	SUM(i.num) product_amount,
	COUNT(1) product_count,
	GROUP_CONCAT(i.num_iid) num_iids
FROM plt_taobao_order_tc o,plt_taobao_order_item_tc i WHERE o.tid=i.tid
GROUP BY tid);

--//@UNDO
-- SQL to undo the change goes here.

update tm_db_column set db_name = 'receiver_state' where column_id = 605 and table_id = 11 ;

DROP VIEW vw_taobao_order_quota;
