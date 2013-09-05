--// update tb template
-- Migration SQL that makes the change goes here.

-- 邮件营销模板
update tb_template set disabled = '1' where template_id = 2 ;

--//@UNDO
-- SQL to undo the change goes here.

update tb_template set disabled = '0' where template_id = 2 ;

