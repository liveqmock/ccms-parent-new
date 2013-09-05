--// modify receiver state and view
-- Migration SQL that makes the change goes here.

update tm_db_column set db_name = 'receiver_state' where column_id = 605 and table_id = 11 ;

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
	o.receiver_state receiver_state,
	o.tid tid,
	o.trade_from trade_from,
	SUM(i.num) product_amount,
	COUNT(1) product_count,
	GROUP_CONCAT(i.num_iid) num_iids
FROM plt_taobao_order_tc o,plt_taobao_order_item_tc i WHERE o.tid=i.tid
GROUP BY tid);

--//@UNDO
-- SQL to undo the change goes here.

update tm_db_column set db_name = 'receiver_district' where column_id = 605 and table_id = 11 ;

DROP VIEW vw_taobao_order_quota;
