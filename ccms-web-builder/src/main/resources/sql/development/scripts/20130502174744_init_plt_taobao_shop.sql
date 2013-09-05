--// create a new table twf_node_evaluate_product_detail
-- Migration SQL that makes the change goes here.

insert ignore into plt_taobao_shop(shop_id,shop_name)
values('65927470','qiushi shop');

--//@UNDO
-- SQL to undo the change goes here.

delete from plt_taobao_shop where shop_id='65927470';