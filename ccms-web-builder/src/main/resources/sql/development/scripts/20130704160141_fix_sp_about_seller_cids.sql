--// fix sp about seller_cids
-- Migration SQL that makes the change goes here.
DROP PROCEDURE IF EXISTS top_item_seller_cat_transform;
CREATE PROCEDURE top_item_seller_cat_transform()
BEGIN
    declare num_iid_t varchar(50);
    declare seller_cids_t text;
    declare _done int default 0;

    -- 查询top_item_job_buffer表记录, 开始遍历
    declare _cur cursor for
      select num_iid, seller_cids from top_item_job_buffer;
    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET _done = 1;

    open _cur;
        repeat
            if not _done then
                fetch _cur into num_iid_t, seller_cids_t;
                call top_item_seller_cid_split(num_iid_t, seller_cids_t);
            end if;
        until _done end repeat;
    close _cur;

END;

DROP PROCEDURE IF EXISTS top_item_seller_cid_split;
CREATE PROCEDURE top_item_seller_cid_split(IN num_iid varchar(50), seller_cids text)
sp:BEGIN
    declare total_len int;
    declare start_pos int;
    declare end_pos int;
    declare str varchar(40);

    if (seller_cids is null or seller_cids='') then
        leave sp;
    end if;

    -- 替换标点
    set seller_cids = replace(seller_cids, '，', ',');
    set seller_cids = concat(seller_cids, ',');
    set total_len = length(seller_cids);

    set start_pos = 1;
    set end_pos = locate(',', seller_cids, start_pos);
    -- 两逗号','中间的是一个记录
    while end_pos <> 0 do
        set str = substring(seller_cids, start_pos, end_pos - start_pos);
        if(str is not null and str <> '') then
            insert ignore into plt_taobao_product_seller_cat(num_iid, seller_cid) values(num_iid, str);
        end if;
        set start_pos = end_pos + 1;
        set end_pos = locate(',', seller_cids, start_pos);
    end while;

END;


--//@UNDO
-- SQL to undo the change goes here.
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
                call top_item_seller_cid_split(num_iid_t, seller_cids_t);
            end if;
        until _done end repeat;
    close _cur;

END;

DROP PROCEDURE IF EXISTS top_item_seller_cid_split;
CREATE PROCEDURE top_item_seller_cid_split(IN num_iid varchar(50), seller_cids varchar(200))
sp:BEGIN
    declare total_len int;
    declare start_pos int;
    declare end_pos int;
    declare str varchar(40);

    if (seller_cids is null or seller_cids='') then
        leave sp;
    end if;

    -- 替换标点
    set seller_cids = replace(seller_cids, '，', ',');
    set seller_cids = concat(seller_cids, ',');
    set total_len = length(seller_cids);

    set start_pos = 1;
    set end_pos = locate(',', seller_cids, start_pos);
    -- 两逗号','中间的是一个记录
    while end_pos <> 0 do
        set str = substring(seller_cids, start_pos, end_pos - start_pos);
        if(str is not null and str <> '') then
            insert ignore into plt_taobao_product_seller_cat(num_iid, seller_cid) values(num_iid, str);
        end if;
        set start_pos = end_pos + 1;
        set end_pos = locate(',', seller_cids, start_pos);
    end while;

END;

