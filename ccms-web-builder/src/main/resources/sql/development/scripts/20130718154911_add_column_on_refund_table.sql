--// add column on refund table
-- Migration SQL that makes the change goes here.
RENAME TABLE top_refund TO top_refund_v_5_0;
CREATE TABLE top_refund like top_refund_v_5_0;

alter table top_refund_job_buffer add cs_status varchar(200) COLLATE utf8_bin DEFAULT NULL;
alter table top_refund add cs_status varchar(200) COLLATE utf8_bin DEFAULT NULL;

DROP PROCEDURE IF EXISTS top_refund_transform;
CREATE PROCEDURE top_refund_transform()
BEGIN

    INSERT IGNORE INTO plt_taobao_refund
                 (refund_id,tid,oid,dp_id,total_fee,buyer_nick,seller_nick,created,modified,
                 order_status,status,good_status,has_good_return,refund_fee,payment,
                 reason,refund_desc,title,num,company_name,sid,cs_status,price,good_return_time,
                 num_iid,remind_type,exist_timeout,timeout)
    SELECT refund_id,tid,oid,dp_id,total_fee,buyer_nick,seller_nick,created,modified,
           order_status,status,good_status,has_good_return,refund_fee,payment,
           reason,refund_desc,title,num,company_name,sid,cs_status,price,good_return_time,
           num_iid,rto_remind_type,rto_exists_timeout,rto_timeout
    FROM top_refund_job_buffer
    WHERE job_status = 'DOING' ORDER BY modified;

    REPLACE INTO plt_taobao_refund
                 (refund_id,tid,oid,dp_id,total_fee,buyer_nick,seller_nick,created,modified,
                 order_status,status,good_status,has_good_return,refund_fee,payment,
                 reason,refund_desc,title,num,company_name,sid,cs_status,price,good_return_time,
                 num_iid,remind_type,exist_timeout,timeout)
    SELECT o.refund_id,o.tid,o.oid,o.dp_id,o.total_fee,o.buyer_nick,o.seller_nick,o.created,o.modified,
           o.order_status,o.status,o.good_status,o.has_good_return,o.refund_fee,o.payment,
           o.reason,o.refund_desc,o.title,o.num,o.company_name,o.sid,o.cs_status,o.price,o.good_return_time,
           o.num_iid,o.rto_remind_type,o.rto_exists_timeout,o.rto_timeout
    FROM top_refund_job_buffer o, plt_taobao_refund p
    WHERE o.job_status = 'DOING' AND o.refund_id = p.refund_id AND o.modified > p.modified
    ORDER BY modified;

END;


--//@UNDO
-- SQL to undo the change goes here.
alter table top_refund_job_buffer drop cs_status varchar(200) COLLATE utf8_bin DEFAULT NULL;
alter table top_refund drop cs_status varchar(200) COLLATE utf8_bin DEFAULT NULL;

DROP PROCEDURE IF EXISTS top_refund_transform;
CREATE PROCEDURE top_refund_transform()
BEGIN

    INSERT IGNORE INTO plt_taobao_refund
                 (refund_id,tid,oid,dp_id,total_fee,buyer_nick,seller_nick,created,modified,
                 order_status,status,good_status,has_good_return,refund_fee,payment,
                 reason,refund_desc,title,num,company_name,sid)
    SELECT refund_id,tid,oid,dp_id,total_fee,buyer_nick,seller_nick,created,modified,
           order_status,status,good_status,has_good_return,refund_fee,payment,
           reason,refund_desc,title,num,company_name,sid
    FROM top_refund_job_buffer
    WHERE job_status = 'DOING' ORDER BY modified;

    REPLACE INTO plt_taobao_refund
                 (refund_id,tid,oid,dp_id,total_fee,buyer_nick,seller_nick,created,modified,
                 order_status,status,good_status,has_good_return,refund_fee,payment,
                 reason,refund_desc,title,num,company_name,sid)
    SELECT o.refund_id,o.tid,o.oid,o.dp_id,o.total_fee,o.buyer_nick,o.seller_nick,o.created,o.modified,
           o.order_status,o.status,o.good_status,o.has_good_return,o.refund_fee,o.payment,
           o.reason,o.refund_desc,o.title,o.num,o.company_name,o.sid
    FROM top_refund_job_buffer o, plt_taobao_refund p
    WHERE o.job_status = 'DOING' AND o.refund_id = p.refund_id AND o.modified > p.modified
    ORDER BY modified;

END;

