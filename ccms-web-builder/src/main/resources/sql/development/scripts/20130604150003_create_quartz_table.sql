--// create quartz table
-- Migration SQL that makes the change goes here.

CREATE  TABLE IF NOT EXISTS tb_tc_quartz_job_details (
  sched_name VARCHAR(120) NOT NULL ,
  job_name VARCHAR(200) NOT NULL ,
  job_group VARCHAR(200) NOT NULL ,
  description VARCHAR(250) NULL ,
  job_class_name VARCHAR(250) NOT NULL ,
  is_durable TINYINT(1) NOT NULL ,
  is_nonconcurrent TINYINT(1) NOT NULL ,
  is_update_data TINYINT(1) NOT NULL ,
  requests_recovery TINYINT(1) NOT NULL ,
  job_data BLOB NULL ,
  PRIMARY KEY (sched_name, job_name, job_group) )
ENGINE = InnoDB;

CREATE INDEX idx_tb_tc_quartz_j_grp ON tb_tc_quartz_job_details (sched_name ASC, job_group ASC) ;

CREATE INDEX idx_tb_tc_quartz_j_req_recovery ON tb_tc_quartz_job_details (sched_name ASC, requests_recovery ASC) ;

CREATE  TABLE IF NOT EXISTS tb_tc_quartz_triggers (
  sched_name VARCHAR(120) NOT NULL ,
  trigger_name VARCHAR(200) NOT NULL ,
  trigger_group VARCHAR(200) NOT NULL ,
  job_name VARCHAR(200) NOT NULL ,
  job_group VARCHAR(200) NOT NULL ,
  description VARCHAR(250) NULL ,
  next_fire_time BIGINT NULL ,
  prev_fire_time BIGINT NULL ,
  priority INT NULL ,
  trigger_state VARCHAR(16) NOT NULL ,
  trigger_type VARCHAR(8) NOT NULL ,
  start_time BIGINT NOT NULL ,
  end_time BIGINT NULL ,
  calendar_name VARCHAR(200) NULL ,
  misfire_instr SMALLINT NULL ,
  job_data BLOB NULL ,
  PRIMARY KEY (sched_name, trigger_name, trigger_group) ,
  CONSTRAINT tb_tc_quartz_triggers_sched_name_fkey
    FOREIGN KEY (sched_name , job_name , job_group )
    REFERENCES tb_tc_quartz_job_details (sched_name , job_name , job_group )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX idx_tb_tc_quartz_t_c ON tb_tc_quartz_triggers (sched_name ASC, calendar_name ASC) ;

CREATE INDEX idx_tb_tc_quartz_t_g ON tb_tc_quartz_triggers (sched_name ASC, trigger_group ASC) ;

CREATE INDEX idx_tb_tc_quartz_t_j ON tb_tc_quartz_triggers (sched_name ASC, job_name ASC, job_group ASC) ;

CREATE INDEX idx_tb_tc_quartz_t_jg ON tb_tc_quartz_triggers (sched_name ASC, job_group ASC) ;

CREATE INDEX idx_tb_tc_quartz_t_n_g_state ON tb_tc_quartz_triggers (sched_name ASC, trigger_group ASC, trigger_state ASC) ;

CREATE INDEX idx_tb_tc_quartz_t_n_state ON tb_tc_quartz_triggers (sched_name ASC, trigger_name ASC, trigger_group ASC, trigger_state ASC) ;

CREATE INDEX idx_tb_tc_quartz_t_next_fire_time ON tb_tc_quartz_triggers (sched_name ASC, next_fire_time ASC) ;

CREATE INDEX idx_tb_tc_quartz_t_nft_misfire ON tb_tc_quartz_triggers (sched_name ASC, misfire_instr ASC, next_fire_time ASC) ;

CREATE INDEX idx_tb_tc_quartz_t_nft_st ON tb_tc_quartz_triggers (sched_name ASC, trigger_state ASC, next_fire_time ASC) ;

CREATE INDEX idx_tb_tc_quartz_t_nft_st_misfire ON tb_tc_quartz_triggers (sched_name ASC, misfire_instr ASC, next_fire_time ASC, trigger_state ASC) ;

CREATE INDEX idx_tb_tc_quartz_t_nft_st_misfire_grp ON tb_tc_quartz_triggers (sched_name ASC, misfire_instr ASC, next_fire_time ASC, trigger_group ASC, trigger_state ASC) ;

CREATE INDEX idx_tb_tc_quartz_t_state ON tb_tc_quartz_triggers (sched_name ASC, trigger_state ASC) ;

CREATE  TABLE IF NOT EXISTS tb_tc_quartz_blob_triggers (
  sched_name VARCHAR(120) NOT NULL ,
  trigger_name VARCHAR(200) NOT NULL ,
  trigger_group VARCHAR(200) NOT NULL ,
  blob_data BLOB NULL ,
  PRIMARY KEY (sched_name, trigger_name, trigger_group) ,
  CONSTRAINT tb_tc_quartz_blob_triggers_sched_name_fkey
    FOREIGN KEY (sched_name , trigger_name , trigger_group )
    REFERENCES tb_tc_quartz_triggers (sched_name , trigger_name , trigger_group )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE  TABLE IF NOT EXISTS tb_tc_quartz_calendars (
  sched_name VARCHAR(120) NOT NULL ,
  calendar_name VARCHAR(200) NOT NULL ,
  calendar BLOB NOT NULL ,
  PRIMARY KEY (sched_name, calendar_name) )
ENGINE = InnoDB;

CREATE  TABLE IF NOT EXISTS tb_tc_quartz_cron_triggers (
  sched_name VARCHAR(120) NOT NULL ,
  trigger_name VARCHAR(200) NOT NULL ,
  trigger_group VARCHAR(200) NOT NULL ,
  cron_expression VARCHAR(120) NOT NULL ,
  time_zone_id VARCHAR(80) NULL ,
  PRIMARY KEY (sched_name, trigger_name, trigger_group) ,
  CONSTRAINT tb_tc_quartz_cron_triggers_sched_name_fkey
    FOREIGN KEY (sched_name , trigger_name , trigger_group )
    REFERENCES tb_tc_quartz_triggers (sched_name , trigger_name , trigger_group )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE  TABLE IF NOT EXISTS tb_tc_quartz_fired_triggers (
  sched_name VARCHAR(120) NOT NULL ,
  entry_id VARCHAR(95) NOT NULL ,
  trigger_name VARCHAR(200) NOT NULL ,
  trigger_group VARCHAR(200) NOT NULL ,
  instance_name VARCHAR(200) NOT NULL ,
  fired_time BIGINT NOT NULL ,
  priority INT NOT NULL ,
  state VARCHAR(16) NOT NULL ,
  job_name VARCHAR(200) NULL ,
  job_group VARCHAR(200) NULL ,
  is_nonconcurrent TINYINT(1) NULL ,
  requests_recovery TINYINT(1) NULL ,
  PRIMARY KEY (sched_name, entry_id) )
ENGINE = InnoDB;

CREATE INDEX idx_tb_tc_quartz_ft_inst_job_req_rcvry ON tb_tc_quartz_fired_triggers (sched_name ASC, instance_name ASC, requests_recovery ASC) ;

CREATE INDEX idx_tb_tc_quartz_ft_j_g ON tb_tc_quartz_fired_triggers (sched_name ASC, job_name ASC, job_group ASC) ;

CREATE INDEX idx_tb_tc_quartz_ft_jg ON tb_tc_quartz_fired_triggers (sched_name ASC, job_group ASC) ;

CREATE INDEX idx_tb_tc_quartz_ft_t_g ON tb_tc_quartz_fired_triggers (sched_name ASC, trigger_name ASC, trigger_group ASC) ;

CREATE INDEX idx_tb_tc_quartz_ft_tg ON tb_tc_quartz_fired_triggers (sched_name ASC, trigger_group ASC) ;

CREATE INDEX idx_tb_tc_quartz_ft_trig_inst_name ON tb_tc_quartz_fired_triggers (sched_name ASC, instance_name ASC) ;

CREATE  TABLE IF NOT EXISTS tb_tc_quartz_locks (
  sched_name VARCHAR(120) NOT NULL ,
  lock_name VARCHAR(40) NOT NULL ,
  PRIMARY KEY (sched_name, lock_name) )
ENGINE = InnoDB;

CREATE  TABLE IF NOT EXISTS tb_tc_quartz_paused_trigger_grps (
  sched_name VARCHAR(120) NOT NULL ,
  trigger_group VARCHAR(200) NOT NULL ,
  PRIMARY KEY (sched_name, trigger_group) )
ENGINE = InnoDB;

CREATE  TABLE IF NOT EXISTS tb_tc_quartz_scheduler_state (
  sched_name VARCHAR(120) NOT NULL ,
  instance_name VARCHAR(200) NOT NULL ,
  last_checkin_time BIGINT NOT NULL ,
  checkin_interval BIGINT NOT NULL ,
  PRIMARY KEY (sched_name, instance_name) )
ENGINE = InnoDB;

CREATE  TABLE IF NOT EXISTS tb_tc_quartz_simple_triggers (
  sched_name VARCHAR(120) NOT NULL ,
  trigger_name VARCHAR(200) NOT NULL ,
  trigger_group VARCHAR(200) NOT NULL ,
  repeat_count BIGINT NOT NULL ,
  repeat_interval BIGINT NOT NULL ,
  times_triggered BIGINT NOT NULL ,
  PRIMARY KEY (sched_name, trigger_name, trigger_group) ,
  CONSTRAINT tb_tc_quartz_simple_triggers_sched_name_fkey
    FOREIGN KEY (sched_name , trigger_name , trigger_group )
    REFERENCES tb_tc_quartz_triggers (sched_name , trigger_name , trigger_group )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE  TABLE IF NOT EXISTS tb_tc_quartz_simprop_triggers (
  sched_name VARCHAR(120) NOT NULL ,
  trigger_name VARCHAR(200) NOT NULL ,
  trigger_group VARCHAR(200) NOT NULL ,
  str_prop_1 VARCHAR(512) NULL ,
  str_prop_2 VARCHAR(512) NULL ,
  str_prop_3 VARCHAR(512) NULL ,
  int_prop_1 INT NULL ,
  int_prop_2 INT NULL ,
  long_prop_1 BIGINT NULL ,
  long_prop_2 BIGINT NULL ,
  dec_prop_1 DECIMAL(13,4) NULL ,
  dec_prop_2 DECIMAL(13,4) NULL ,
  bool_prop_1 TINYINT(1) NULL ,
  bool_prop_2 TINYINT(1) NULL ,
  PRIMARY KEY (sched_name, trigger_name, trigger_group) ,
  CONSTRAINT tb_tc_quartz_simprop_triggers_sched_name_fkey
    FOREIGN KEY (sched_name , trigger_name , trigger_group )
    REFERENCES tb_tc_quartz_triggers (sched_name , trigger_name , trigger_group )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE tb_tc_quartz_task_init (
  pkid bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  job_name varchar(200) DEFAULT NULL COMMENT '任务名',
  job_group varchar(200) DEFAULT NULL COMMENT '任务组名',
  job_class_name varchar(250) DEFAULT NULL COMMENT '任务类全名',
  cron_expression varchar(120) DEFAULT NULL COMMENT '任务调度的表达式',
  is_valid int(11) DEFAULT NULL,
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE IF EXISTS tb_tc_quartz_cron_triggers ;
DROP TABLE IF EXISTS tb_tc_quartz_blob_triggers ;
DROP TABLE IF EXISTS tb_tc_quartz_calendars ;
DROP TABLE IF EXISTS tb_tc_quartz_fired_triggers ;
DROP TABLE IF EXISTS tb_tc_quartz_locks ;
DROP TABLE IF EXISTS tb_tc_quartz_paused_trigger_grps ;
DROP TABLE IF EXISTS tb_tc_quartz_scheduler_state ;
DROP TABLE IF EXISTS tb_tc_quartz_simple_triggers ;
DROP TABLE IF EXISTS tb_tc_quartz_simprop_triggers ;
DROP TABLE IF EXISTS tb_tc_quartz_triggers ;
DROP TABLE IF EXISTS tb_tc_quartz_job_details ;
DROP TABLE IF EXISTS tb_tc_quartz_task_init ;

