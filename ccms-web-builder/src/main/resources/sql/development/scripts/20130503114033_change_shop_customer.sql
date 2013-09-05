--// create a new table twf_node_evaluate_product_detail
-- Migration SQL that makes the change goes here.

delete from plt_taobao_shop;

insert into plt_taobao_shop(shop_id,shop_name)
values('100571094','大狗子19890202');

insert into tb_app_properties(prop_id,prop_group,prop_name,prop_value)
values(13,'CCMS','ccms_username','sherrytest')
on duplicate key update 
	prop_group=values(prop_group),
	prop_name=values(prop_name),
	prop_value=values(prop_value);

--//@UNDO
-- SQL to undo the change goes here.

delete from plt_taobao_shop where shop_id='100571094';