--// modify camp to campaign 
-- Migration SQL that makes the change goes here.

-- modify tds_camp_status to tds_campaign_status
ALTER  TABLE  tds_camp_status  RENAME  TO  tds_campaign_status;

-- modify tdu_camp_type to tdu_campaign_category
ALTER  TABLE  tdu_camp_type  RENAME  TO  tdu_campaign_category;
ALTER  TABLE  tdu_campaign_category  CHANGE  camp_type_id  category_id  bigint(20)  NOT NULL;
ALTER  TABLE  tdu_campaign_category  CHANGE  camp_type  category_value  varchar(15) COMMENT '活动类型名' ;

--//@UNDO
-- SQL to undo the change goes here.

ALTER  TABLE  tds_campaign_status  RENAME  TO  tds_camp_status;

ALTER  TABLE  tdu_campaign_category  RENAME  TO  tdu_camp_type;
ALTER  TABLE  tdu_camp_type  CHANGE  category_id camp_type_id  bigint(20)  NOT NULL;
ALTER  TABLE  tdu_camp_type  CHANGE  category_value camp_type  varchar(15) COMMENT '活动类型名' ;

