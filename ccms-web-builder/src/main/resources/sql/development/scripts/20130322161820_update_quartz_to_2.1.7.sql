--// update quartz to 2.1.7
-- Migration SQL that makes the change goes here.
--

-- back up all tables

create table  twf_qrtz_timer_job_listeners_bak_quartz_185 select * from twf_qrtz_timer_job_listeners;
create table  twf_qrtz_timer_trigger_listeners_bak_quartz_185 select * from twf_qrtz_timer_trigger_listeners;
create table  twf_qrtz_timer_fired_triggers_bak_quartz_185 select * from twf_qrtz_timer_fired_triggers;
create table  twf_qrtz_timer_paused_trigger_grps_bak_quartz_185 select * from twf_qrtz_timer_paused_trigger_grps;
create table  twf_qrtz_timer_scheduler_state_bak_quartz_185 select * from twf_qrtz_timer_scheduler_state;
create table  twf_qrtz_timer_locks_bak_quartz_185 select * from twf_qrtz_timer_locks;
create table  twf_qrtz_timer_simple_triggers_bak_quartz_185 select * from twf_qrtz_timer_simple_triggers;
create table  twf_qrtz_timer_cron_triggers_bak_quartz_185 select * from twf_qrtz_timer_cron_triggers;
create table  twf_qrtz_timer_blob_triggers_bak_quartz_185 select * from twf_qrtz_timer_blob_triggers;
create table  twf_qrtz_timer_triggers_bak_quartz_185 select * from twf_qrtz_timer_triggers;
create table  twf_qrtz_timer_job_details_bak_quartz_185 select * from twf_qrtz_timer_job_details;
create table  twf_qrtz_timer_calendars_bak_quartz_185 select * from twf_qrtz_timer_calendars;


-- drop tables that are no longer used

drop table if exists twf_qrtz_timer_job_listeners;
drop table if exists twf_qrtz_timer_trigger_listeners;

-- drop columns that are no longer used

alter table twf_qrtz_timer_job_details drop column is_volatile;
alter table twf_qrtz_timer_triggers drop column is_volatile;
alter table twf_qrtz_timer_fired_triggers drop column is_volatile;

-- add new columns that replace the 'is_stateful' column

alter table twf_qrtz_timer_job_details add column is_nonconcurrent bool;
alter table twf_qrtz_timer_job_details add column is_update_data bool;
update twf_qrtz_timer_job_details set is_nonconcurrent = is_stateful;
update twf_qrtz_timer_job_details set is_update_data = is_stateful;
alter table twf_qrtz_timer_job_details drop column is_stateful;
alter table twf_qrtz_timer_fired_triggers add column is_nonconcurrent bool;
alter table twf_qrtz_timer_fired_triggers add column is_update_data bool;
update twf_qrtz_timer_fired_triggers set is_nonconcurrent = is_stateful;
update twf_qrtz_timer_fired_triggers set is_update_data = is_stateful;
alter table twf_qrtz_timer_fired_triggers drop column is_stateful;

-- add new 'sched_name' column to all tables

alter table twf_qrtz_timer_blob_triggers add column sched_name varchar(120) not null DEFAULT 'TestScheduler';
alter table twf_qrtz_timer_calendars add column sched_name varchar(120) not null DEFAULT 'TestScheduler';
alter table twf_qrtz_timer_cron_triggers add column sched_name varchar(120) not null DEFAULT 'TestScheduler';
alter table twf_qrtz_timer_fired_triggers add column sched_name varchar(120) not null DEFAULT 'TestScheduler';
alter table twf_qrtz_timer_job_details add column sched_name varchar(120) not null DEFAULT 'TestScheduler';
alter table twf_qrtz_timer_locks add column sched_name varchar(120) not null DEFAULT 'TestScheduler';
alter table twf_qrtz_timer_paused_trigger_grps add column sched_name varchar(120) not null DEFAULT 'TestScheduler';
alter table twf_qrtz_timer_scheduler_state add column sched_name varchar(120) not null DEFAULT 'TestScheduler';
alter table twf_qrtz_timer_simple_triggers add column sched_name varchar(120) not null DEFAULT 'TestScheduler';
alter table twf_qrtz_timer_triggers add column sched_name varchar(120) not null DEFAULT 'TestScheduler';

-- drop all primary and foreign key constraints, so that we can define new ones


-- add all primary and foreign key constraints, based on new columns

alter table twf_qrtz_timer_triggers add primary key (sched_name, trigger_name, trigger_group);
alter table twf_qrtz_timer_job_details add primary key (sched_name, job_name, job_group);
alter table twf_qrtz_timer_triggers add foreign key (sched_name, job_name, job_group) references twf_qrtz_timer_job_details(sched_name, job_name, job_group);
alter table twf_qrtz_timer_blob_triggers add primary key (sched_name, trigger_name, trigger_group);
alter table twf_qrtz_timer_blob_triggers add foreign key (sched_name, trigger_name, trigger_group) references twf_qrtz_timer_triggers(sched_name, trigger_name, trigger_group);
alter table twf_qrtz_timer_cron_triggers add primary key (sched_name, trigger_name, trigger_group);
alter table twf_qrtz_timer_cron_triggers add foreign key (sched_name, trigger_name, trigger_group) references twf_qrtz_timer_triggers(sched_name, trigger_name, trigger_group);
alter table twf_qrtz_timer_simple_triggers add primary key (sched_name, trigger_name, trigger_group);
alter table twf_qrtz_timer_simple_triggers add foreign key (sched_name, trigger_name, trigger_group) references twf_qrtz_timer_triggers(sched_name, trigger_name, trigger_group);
alter table twf_qrtz_timer_fired_triggers add primary key (sched_name, entry_id);
alter table twf_qrtz_timer_calendars add primary key (sched_name, calendar_name);
alter table twf_qrtz_timer_locks add primary key (sched_name, lock_name);
alter table twf_qrtz_timer_paused_trigger_grps add primary key (sched_name, trigger_group);
alter table twf_qrtz_timer_scheduler_state add primary key (sched_name, instance_name);

-- add new simprop_triggers table

CREATE TABLE twf_qrtz_timer_simprop_triggers
 (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 BOOL NULL,
    BOOL_PROP_2 BOOL NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    REFERENCES twf_qrtz_timer_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

-- create indexes for faster queries

create index idx_twf_qrtz_timer_j_req_recovery on twf_qrtz_timer_job_details(SCHED_NAME,REQUESTS_RECOVERY);
create index idx_twf_qrtz_timer_j_grp on twf_qrtz_timer_job_details(SCHED_NAME,JOB_GROUP);
create index idx_twf_qrtz_timer_t_j on twf_qrtz_timer_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
create index idx_twf_qrtz_timer_t_jg on twf_qrtz_timer_triggers(SCHED_NAME,JOB_GROUP);
create index idx_twf_qrtz_timer_t_c on twf_qrtz_timer_triggers(SCHED_NAME,CALENDAR_NAME);
create index idx_twf_qrtz_timer_t_g on twf_qrtz_timer_triggers(SCHED_NAME,TRIGGER_GROUP);
create index idx_twf_qrtz_timer_t_state on twf_qrtz_timer_triggers(SCHED_NAME,TRIGGER_STATE);
create index idx_twf_qrtz_timer_t_n_state on twf_qrtz_timer_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
create index idx_twf_qrtz_timer_t_n_g_state on twf_qrtz_timer_triggers(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
create index idx_twf_qrtz_timer_t_next_fire_time on twf_qrtz_timer_triggers(SCHED_NAME,NEXT_FIRE_TIME);
create index idx_twf_qrtz_timer_t_nft_st on twf_qrtz_timer_triggers(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
create index idx_twf_qrtz_timer_t_nft_misfire on twf_qrtz_timer_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
create index idx_twf_qrtz_timer_t_nft_st_misfire on twf_qrtz_timer_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
create index idx_twf_qrtz_timer_t_nft_st_misfire_grp on twf_qrtz_timer_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);
create index idx_twf_qrtz_timer_ft_trig_inst_name on twf_qrtz_timer_fired_triggers(SCHED_NAME,INSTANCE_NAME);
create index idx_twf_qrtz_timer_ft_inst_job_req_rcvry on twf_qrtz_timer_fired_triggers(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
create index idx_twf_qrtz_timer_ft_j_g on twf_qrtz_timer_fired_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
create index idx_twf_qrtz_timer_ft_jg on twf_qrtz_timer_fired_triggers(SCHED_NAME,JOB_GROUP);
create index idx_twf_qrtz_timer_ft_t_g on twf_qrtz_timer_fired_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
create index idx_twf_qrtz_timer_ft_tg on twf_qrtz_timer_fired_triggers(SCHED_NAME,TRIGGER_GROUP);


--//@UNDO
-- SQL to undo the change goes here.


drop table if exists   twf_qrtz_timer_calendars;
drop table if exists   twf_qrtz_timer_job_listeners;
drop table if exists   twf_qrtz_timer_trigger_listeners;
drop table if exists   twf_qrtz_timer_fired_triggers;
drop table if exists   twf_qrtz_timer_paused_trigger_grps;
drop table if exists   twf_qrtz_timer_scheduler_state;
drop table if exists   twf_qrtz_timer_locks;
drop table if exists   twf_qrtz_timer_simple_triggers;
drop table if exists   twf_qrtz_timer_cron_triggers;
drop table if exists   twf_qrtz_timer_blob_triggers;
drop table if exists   twf_qrtz_timer_simprop_triggers;
drop table if exists   twf_qrtz_timer_triggers;
drop table if exists   twf_qrtz_timer_job_details;


alter table  twf_qrtz_timer_job_listeners_bak_quartz_185 rename to  twf_qrtz_timer_job_listeners;
alter table  twf_qrtz_timer_trigger_listeners_bak_quartz_185 rename to  twf_qrtz_timer_trigger_listeners;
alter table  twf_qrtz_timer_fired_triggers_bak_quartz_185 rename to  twf_qrtz_timer_fired_triggers;
alter table  twf_qrtz_timer_paused_trigger_grps_bak_quartz_185 rename to  twf_qrtz_timer_paused_trigger_grps;
alter table  twf_qrtz_timer_scheduler_state_bak_quartz_185 rename to  twf_qrtz_timer_scheduler_state;
alter table  twf_qrtz_timer_locks_bak_quartz_185 rename to  twf_qrtz_timer_locks;
alter table  twf_qrtz_timer_simple_triggers_bak_quartz_185 rename to  twf_qrtz_timer_simple_triggers;
alter table  twf_qrtz_timer_cron_triggers_bak_quartz_185 rename to  twf_qrtz_timer_cron_triggers;
alter table  twf_qrtz_timer_blob_triggers_bak_quartz_185 rename to  twf_qrtz_timer_blob_triggers;
alter table  twf_qrtz_timer_triggers_bak_quartz_185 rename to  twf_qrtz_timer_triggers;
alter table  twf_qrtz_timer_job_details_bak_quartz_185 rename to  twf_qrtz_timer_job_details;
alter table  twf_qrtz_timer_calendars_bak_quartz_185 rename to  twf_qrtz_timer_calendars;

-- drop table if exists   twf_qrtz_timer_job_listeners_bak_quartz_185;
-- drop table if exists   twf_qrtz_timer_trigger_listeners_bak_quartz_185;
-- drop table if exists   twf_qrtz_timer_fired_triggers_bak_quartz_185;
-- drop table if exists   twf_qrtz_timer_paused_trigger_grps_bak_quartz_185;
-- drop table if exists   twf_qrtz_timer_scheduler_state_bak_quartz_185;
-- drop table if exists   twf_qrtz_timer_locks_bak_quartz_185;
-- drop table if exists   twf_qrtz_timer_simple_triggers_bak_quartz_185;
-- drop table if exists   twf_qrtz_timer_cron_triggers_bak_quartz_185;
-- drop table if exists   twf_qrtz_timer_blob_triggers_bak_quartz_185;
-- drop table if exists   twf_qrtz_timer_triggers_bak_quartz_185;
-- drop table if exists   twf_qrtz_timer_job_details_bak_quartz_185;
-- drop table if exists   twf_qrtz_timer_calendars_bak_quartz_185;