--// update_module_type_name_tradecenter
-- Migration SQL that makes the change goes here.
UPDATE module_type SET id='67', key_name='myFollow', name='我的事务', name_plus='二级菜单', url='javascript:void(0);', data_url=NULL, tip=NULL, lowest_edition_required='0', support_ops_mask='17', memo=NULL WHERE id='67';
UPDATE module_type SET id='68', key_name='followList', name='事务列表', name_plus='', url='#/affairs/myaffair', data_url=NULL, tip=NULL, lowest_edition_required='0', support_ops_mask='17', memo=NULL WHERE id='68';



--//@UNDO
-- SQL to undo the change goes here.
UPDATE module_type SET id='67', key_name='myFollow', name='我的事物', name_plus='二级菜单', url='javascript:void(0);', data_url=NULL, tip=NULL, lowest_edition_required='0', support_ops_mask='17', memo=NULL WHERE id='67';
UPDATE module_type SET id='68', key_name='followList', name='事物列表', name_plus='', url='#/affairs/myaffair', data_url=NULL, tip=NULL, lowest_edition_required='0', support_ops_mask='17', memo=NULL WHERE id='68';



