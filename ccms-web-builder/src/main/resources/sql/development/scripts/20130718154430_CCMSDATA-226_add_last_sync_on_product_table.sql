--// CCMSDATA-226 add last_sync on product table
-- Migration SQL that makes the change goes here.
RENAME TABLE top_item TO top_item_v_5_0;
CREATE TABLE top_item like top_item_v_5_0;

ALTER TABLE plt_taobao_product add column last_sync datetime DEFAULT NULL;
ALTER TABLE top_item_job_buffer add column last_sync datetime DEFAULT NULL;
ALTER TABLE top_jobmid_item add column last_sync datetime DEFAULT NULL;
ALTER TABLE top_item add column last_sync datetime DEFAULT NULL;

-- etl product 增加字段---------------------------------------------------------
DROP PROCEDURE IF EXISTS top_item_transform;
CREATE PROCEDURE top_item_transform(IN p_job_id bigint(20))
BEGIN

    -- 1. 合并商品
    DELETE FROM top_jobmid_item;
    INSERT IGNORE INTO top_jobmid_item
                (dp_id,num_iid,nick,price,detail_url,title,pic_url,
                list_time,type,props_name,created,promoted_service,cid,
                seller_cids,props,input_pids,input_str,num,valid_thru,
                delist_time,stuff_status,loc_zip,loc_address,loc_city,
                loc_state,loc_country,loc_district,post_fee,express_fee,
                ems_fee,has_discount,freight_payer,has_invoice,has_warranty,
                has_showcase,modified,increment,approve_status,postage_id,
                product_id,auction_point,property_alias,outer_id,is_virtual,
                is_taobao,is_ex,is_timing,is_3d,score,volume,one_station,
                second_kill,auto_fill,violation,is_prepay,ww_status,
                wap_detail_url,after_sale_id,cod_postage_id,sell_promise,last_sync)
    SELECT dp_id,num_iid,nick,price,detail_url,title,pic_url,
           list_time,type,props_name,created,promoted_service,cid,
           seller_cids,props,input_pids,input_str,num,valid_thru,
           delist_time,stuff_status,loc_zip,loc_address,loc_city,
           loc_state,loc_country,loc_district,post_fee,express_fee,
           ems_fee,has_discount,freight_payer,has_invoice,has_warranty,
           has_showcase,modified,increment,approve_status,postage_id,
           product_id,auction_point,property_alias,outer_id,is_virtual,
           is_taobao,is_ex,is_timing,is_3d,score,volume,one_station,
           second_kill,auto_fill,violation,is_prepay,ww_status,
           wap_detail_url,after_sale_id,cod_postage_id,sell_promise,last_sync
    FROM top_item_job_buffer WHERE job_status = 'DOING' ORDER BY modified DESC; -- 新先入则旧不入

    -- 2. 正式导入商品表 先INSERT IGNORE
    REPLACE INTO plt_taobao_product
                (num_iid,dp_id,detail_url,title,created,cid,pic_url,
                list_time,delist_time,price,modified,approve_status,outer_id,
                props_name,props,seller_cids,input_pids,input_str,last_sync)
    SELECT num_iid,i1.dp_id,i1.detail_url,i1.title,i1.created,i1.cid,i1.pic_url,
          i1.list_time,i1.delist_time,i1.price,i1.modified,i1.approve_status,i1.outer_id,
          i1.props_name,i1.props,i1.seller_cids,i1.input_pids,i1.input_str,i1.last_sync
    FROM top_jobmid_item i1;

    -- 4. 从子订单表中补充 商品表中的缺失信息
    INSERT IGNORE INTO plt_taobao_product(num_iid,dp_id,title,price,cid,last_sync)
    SELECT a.num_iid AS num_iid,
           a.dp_id AS dp_id,
           a.title AS title,
           a.price AS price,
           a.cid AS cid,
           b.created AS last_sync
    FROM top_jobmid_trade_order a, top_jobmid_trade b
    WHERE a.tid = b.tid;

    UPDATE plt_taobao_product p,
        (SELECT o.dp_id AS dp_id, o.title AS title, o.price AS price, o.cid AS cid, t.created AS created, o.num_iid as num_iid
            FROM top_jobmid_trade_order o left join top_jobmid_trade t on o.tid=t.tid) a
    SET p.dp_id = a.dp_id,
        p.title = a.title,
        p.price = a.price,
        p.cid = a.cid,
        p.last_sync = a.created
    WHERE (p.last_sync IS NULL OR p.last_sync < a.created) and p.num_iid = a.num_iid;

    -- 6. 正式导入卖家商品属性表
    REPLACE INTO plt_taobao_product_prop( num_iid,pid,prop_name,vid,name,name_alias,is_input_prop )
    SELECT p1.num_iid,p1.pid,p1.prop_name,p1.vid,p1.name,p1.name_alias,p1.is_input_prop
    FROM top_product_prop_job_buffer p1
         INNER JOIN (SELECT MAX(row_id) AS row_id
                    FROM top_product_prop_job_buffer WHERE job_status = 'DOING'
                    GROUP BY num_iid,pid) p2
         ON p1.row_id = p2.row_id
    WHERE p1.job_status = 'DOING';

    INSERT INTO top_load_log(table_name,updated,etl_job_id) VALUES ('plt_taobao_product',now(), p_job_id );
END;


--//@UNDO
-- SQL to undo the change goes here.
alter table plt_taobao_product drop column last_sync;
alter table top_item_job_buffer drop column last_sync;
alter table top_jobmid_item drop column last_sync;
alter table top_item drop column last_sync;

-- etl product 增加字段---------------------------------------------------------
DROP PROCEDURE IF EXISTS top_item_transform;
CREATE PROCEDURE top_item_transform(IN p_job_id bigint(20))
BEGIN

    -- 1. 合并商品
    DELETE FROM top_jobmid_item;
    INSERT IGNORE INTO top_jobmid_item
                (dp_id,num_iid,nick,price,detail_url,title,pic_url,
                list_time,type,props_name,created,promoted_service,cid,
                seller_cids,props,input_pids,input_str,num,valid_thru,
                delist_time,stuff_status,loc_zip,loc_address,loc_city,
                loc_state,loc_country,loc_district,post_fee,express_fee,
                ems_fee,has_discount,freight_payer,has_invoice,has_warranty,
                has_showcase,modified,increment,approve_status,postage_id,
                product_id,auction_point,property_alias,outer_id,is_virtual,
                is_taobao,is_ex,is_timing,is_3d,score,volume,one_station,
                second_kill,auto_fill,violation,is_prepay,ww_status,
                wap_detail_url,after_sale_id,cod_postage_id,sell_promise)
    SELECT dp_id,num_iid,nick,price,detail_url,title,pic_url,
           list_time,type,props_name,created,promoted_service,cid,
           seller_cids,props,input_pids,input_str,num,valid_thru,
           delist_time,stuff_status,loc_zip,loc_address,loc_city,
           loc_state,loc_country,loc_district,post_fee,express_fee,
           ems_fee,has_discount,freight_payer,has_invoice,has_warranty,
           has_showcase,modified,increment,approve_status,postage_id,
           product_id,auction_point,property_alias,outer_id,is_virtual,
           is_taobao,is_ex,is_timing,is_3d,score,volume,one_station,
           second_kill,auto_fill,violation,is_prepay,ww_status,
           wap_detail_url,after_sale_id,cod_postage_id,sell_promise
    FROM top_item_job_buffer WHERE job_status = 'DOING' ORDER BY modified DESC; -- 新先入则旧不入

    -- 2. 正式导入商品表 先INSERT IGNORE
    REPLACE INTO plt_taobao_product
                (num_iid,dp_id,detail_url,title,created,cid,pic_url,
                list_time,delist_time,price,modified,approve_status,outer_id,
                props_name,props,seller_cids,input_pids,input_str)
    SELECT num_iid,i1.dp_id,i1.detail_url,i1.title,i1.created,i1.cid,i1.pic_url,
          i1.list_time,i1.delist_time,i1.price,i1.modified,i1.approve_status,i1.outer_id,
          i1.props_name,i1.props,i1.seller_cids,i1.input_pids,i1.input_str
    FROM top_jobmid_item i1;

    -- 4. 从子订单表中补充 商品表中的缺失信息
    INSERT IGNORE INTO plt_taobao_product(num_iid,dp_id,title,price,cid)
    SELECT a.num_iid as num_iid,
           a.dp_id as dp_id,
           a.title as title,
           a.price as price,
           a.cid as cid
    FROM top_jobmid_trade_order a;

    -- 6. 正式导入卖家商品属性表
    REPLACE INTO plt_taobao_product_prop( num_iid,pid,prop_name,vid,name,name_alias,is_input_prop )
    SELECT p1.num_iid,p1.pid,p1.prop_name,p1.vid,p1.name,p1.name_alias,p1.is_input_prop
    FROM top_product_prop_job_buffer p1
         INNER JOIN (SELECT MAX(row_id) AS row_id
                    FROM top_product_prop_job_buffer WHERE job_status = 'DOING'
                    GROUP BY num_iid,pid) p2
         ON p1.row_id = p2.row_id
    WHERE p1.job_status = 'DOING';

    INSERT INTO top_load_log(table_name,updated,etl_job_id) VALUES ('plt_taobao_product',now(), p_job_id );
END;

