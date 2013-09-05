--// fix bugs about split props column
-- Migration SQL that makes the change goes here.
delete from plt_taobao_product_seller_cat;

ALTER TABLE plt_taobao_product_prop CHANGE COLUMN vid vid VARCHAR(150) NOT NULL COMMENT '属性值ID'  
, DROP PRIMARY KEY 
, ADD PRIMARY KEY (num_iid, pid, vid) ;

DROP PROCEDURE IF EXISTS top_item_props_transform;
CREATE PROCEDURE top_item_props_transform()
BEGIN
    declare num_iid_t varchar(50);
    declare props_t text;
    declare _done int default 0;

    -- 查找出top_item_job_buffer表中所有的props记录, 进行遍历
    declare _cur cursor for
      select num_iid, props_name from top_item_job_buffer;
    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET _done = 1;

    open _cur;
        repeat
            if not _done then
                fetch _cur into num_iid_t, props_t;
                delete from plt_taobao_product_prop where num_iid=num_iid_t;
                call top_item_props_split(num_iid_t, props_t);
            end if;
        until _done end repeat;
    close _cur;

END;

DROP PROCEDURE IF EXISTS top_item_seller_cat_transform;
CREATE PROCEDURE top_item_seller_cat_transform()
BEGIN
    declare num_iid_t varchar(50);
    declare seller_cids_t varchar(200);
    declare _done int default 0;

    -- 查询top_item_job_buffer表记录, 开始遍历
    declare _cur cursor for
      select num_iid, seller_cids from top_item_job_buffer;
    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET _done = 1;

    open _cur;
        repeat
            if not _done then
                fetch _cur into num_iid_t, seller_cids_t;
                delete from plt_taobao_product_seller_cat where num_iid=num_iid_t;
                call top_item_seller_cid_split(num_iid_t, seller_cids_t);
            end if;
        until _done end repeat;
    close _cur;

END;

DROP PROCEDURE IF EXISTS top_item_skus_prop_transform;
CREATE PROCEDURE top_item_skus_prop_transform()
BEGIN
    declare num_iid_t varchar(50);
    declare sku_id_t varchar(150);
    declare props_t varchar(1000);
    declare _done int default 0;

    declare _cur cursor for
      select num_iid, sku_id, properties_name from top_item_sku_job_buffer;
    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET _done = 1;

    open _cur;
        repeat
            if not _done then
                fetch _cur into num_iid_t, sku_id_t, props_t;
                delete from plt_taobao_product_skus_prop where num_iid=num_iid_t and sku_id=sku_id_t;
                call top_item_sku_split(num_iid_t, sku_id_t, props_t);
            end if;
        until _done end repeat;
    close _cur;

END;

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

--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE base_etl.plt_taobao_product_prop CHANGE COLUMN vid vid VARCHAR(150) NOT NULL COMMENT '属性值ID'  
, DROP PRIMARY KEY 
, ADD PRIMARY KEY (num_iid, pid) ;


