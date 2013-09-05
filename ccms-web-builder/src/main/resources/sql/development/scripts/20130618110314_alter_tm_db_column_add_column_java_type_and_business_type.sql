--// alter tm_db_column add column java_type and business_type
-- Migration SQL that makes the change goes here.

ALTER TABLE `tm_db_column` 
	ADD COLUMN `java_type` VARCHAR(255) NULL COMMENT 'java类全名.' AFTER `db_type`,
	ADD COLUMN `business_type` VARCHAR(30) NULL COMMENT '业务类型' AFTER `java_type`;

update tm_db_column set business_type=db_type where table_id in(10,11);
update tm_db_column set business_type='product' where table_id=11 and db_name='has_products';
update tm_db_column set db_type='string' where table_id=11 and db_name='receiver_district';
	
--//@UNDO
-- SQL to undo the change goes here.

alter table tm_db_column
	drop column java_type,
	drop column business_type;