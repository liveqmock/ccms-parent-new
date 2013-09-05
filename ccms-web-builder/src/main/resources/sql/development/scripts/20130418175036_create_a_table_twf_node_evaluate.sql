--// create a table twf_node_evaluate
-- Migration SQL that makes the change goes here.


 CREATE TABLE twf_node_evaluate (
  node_id bigint(20) NOT NULL COMMENT '评估节点ID',
  node_name varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '评估节点名称',
  evaluate_cycle int(11) NOT NULL COMMENT '评估周期（1天~7天）',
  shop_id varchar(200) COLLATE utf8_bin NOT NULL COMMENT '评估的店铺',
  created datetime NOT NULL,
  PRIMARY KEY (node_id)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



--//@UNDO
-- SQL to undo the change goes here.


 drop table twf_node_evaluate;