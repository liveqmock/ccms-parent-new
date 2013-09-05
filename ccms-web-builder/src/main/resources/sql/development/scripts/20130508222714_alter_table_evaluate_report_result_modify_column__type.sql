--// alter table evaluate_report_result modify column  type
-- Migration SQL that makes the change goes here.

ALTER TABLE evaluate_report_result  MODIFY COLUMN  buy_order_count BIGINT(12);
ALTER TABLE evaluate_report_result  MODIFY COLUMN  buy_customer_count BIGINT(12);
ALTER TABLE evaluate_report_result  MODIFY COLUMN  pay_order_count BIGINT(12);
ALTER TABLE evaluate_report_result  MODIFY COLUMN  pay_customer_count BIGINT(12);
ALTER TABLE evaluate_report_result  MODIFY COLUMN  product_count BIGINT(12);

--//@UNDO
-- SQL to undo the change goes here.


ALTER TABLE evaluate_report_result  MODIFY COLUMN  buy_order_count INTEGER(11);
ALTER TABLE evaluate_report_result  MODIFY COLUMN  buy_customer_count INTEGER(11);
ALTER TABLE evaluate_report_result  MODIFY COLUMN  pay_order_count INTEGER(11);
ALTER TABLE evaluate_report_result  MODIFY COLUMN  pay_customer_count INTEGER(11);
ALTER TABLE evaluate_report_result  MODIFY COLUMN  product_count INTEGER(11);