#-------------------------------------------------------------------------------------------------------------------------------
#  建存储过程脚本
#------------------------------------------------------------------------------------------------------------------------------

delimiter //
CREATE PROCEDURE proc_dedup_customer(IN p_plat_code char(8), IN p_parent_plat char(8), IN p_plat_priority smallint, IN p_customerno varchar(50), IN p_full_name varchar(50), IN p_sex char(1), IN p_job varchar(50), IN p_birth_year smallint, IN p_birthday date, IN p_email varchar(100), IN p_mobile varchar(20), IN p_phone varchar(50), IN p_zip varchar(20), IN p_country varchar(50), IN p_state varchar(50), IN p_city varchar(50), IN p_district varchar(100), IN p_address varchar(255), IN p_changed datetime, OUT p_isdup boolean)
    COMMENT '统一客户信息时执行的客户去重过程，需遵循 Dedup Rules，返回当前客户是否重复的判断结果'
BEGIN
  DECLARE v_uni_id varchar(64); #统一客户ID
  Set p_isdup = false; #是否重复客户

    #客户去重规则 --------------------------------------------------------------------------------------
    # DedupRule 1：如果当前客户的平台昵称存与本平台或者其父平台的一个客户一致，即可认为两个客户是相同的一个人
    # DedupRule 2：(TODO: 后继需加入更多dedup规则)
    #--------------------------------------------------------------------------------------------------

    #1.使用Dedup规则，判断当前客户是否为重复客户
    select uni_id into v_uni_id from uni_customer_plat where plat_code = p_plat_code and customerno = p_customerno limit 1; #检查当前客户与本平台关系
    IF (v_uni_id IS NOT NULL) THEN # 当前客户与本平台关系存在
        update uni_customer_plat set  changed = p_changed where plat_code = p_plat_code and customerno = p_customerno; #更新客户属性变更时间
        Set p_isdup = true; #客户被判定为重复
    ELSE
       #若当前客户与本平台关系不存在
       select uni_id into v_uni_id from uni_customer_plat where plat_code = p_parent_plat and customerno = p_customerno limit 1; #检查当前客户与父平台关系
       IF (v_uni_id IS NOT NULL) THEN #若有父平台客户与当前客户一致
           Set p_isdup = true; #客户被判定为重复
           #取父平台的统一客户ID作为当前客户的ID，并记录当前客户-当前平台关系
           insert into uni_customer_plat (uni_id, plat_code, customerno, plat_priority, changed)
                              values (v_uni_id, p_plat_code, p_customerno, p_plat_priority, p_changed);
       END IF;
    END IF;

    #2.若该客户是新的客户（非重复客户），则新增统一客户信息并记录客户平台关系表
    IF (!p_isdup) THEN
        begin
            #记录新统一客户信息
            Set v_uni_id = CONCAT(p_plat_code, '|', p_customerno); #以"平台代码 +竖线 + 平台内的客户编码"作为规则生成生成统一客户ID
            insert into uni_customer (uni_id, full_name, sex, job, birth_year, birthday, email, mobile, phone,
                                  zip, country, state, city, district, address)
                          values (v_uni_id, p_full_name, p_sex, p_job, p_birth_year, p_birthday, p_email, p_mobile, p_phone,
                                  p_zip, p_country, p_state, p_city, p_district, p_address);
            #记录当前客户-当前平台关系
            insert into uni_customer_plat (uni_id, plat_code, customerno, plat_priority, changed)
                               values (v_uni_id, p_plat_code, p_customerno, p_plat_priority, p_changed);
        end;
    END IF;

    #RETURN v_isdup;
END
//
delimiter ;


delimiter //
CREATE PROCEDURE proc_delete_unify_customer(IN p_plat_code char(8), IN p_customerno varchar(50))
    COMMENT '删除统一客户信息'
BEGIN
  Delete from uni_customer where uni_id =
      (select uni_id from uni_customer_plat where plat_code = p_plat_code and customerno = p_customerno );
END
//
delimiter ;


delimiter //
CREATE PROCEDURE proc_merge_customer(IN p_plat_code char(8), IN p_parent_plat char(8), IN p_plat_table varchar(50), IN p_plat_priority smallint, IN p_customerno varchar(50), IN p_full_name varchar(50), IN p_sex char(1), IN p_job varchar(50), IN p_birth_year smallint, IN p_birthday date, IN p_email varchar(100), IN p_mobile varchar(20), IN p_phone varchar(50), IN p_zip varchar(20), IN p_country varchar(50), IN p_state varchar(50), IN p_city varchar(50), IN p_district varchar(100), IN p_address varchar(255), IN p_changed datetime)
    COMMENT '统一客户信息时执行的客户信息合并过程，需遵循Merge Rules'
BEGIN
  #DECLARE rec_customer_plat record; #客户和平台的关系信息
  #DECLARE rec_customer record; #平台相关的客户信息

  DECLARE v_plat_code char(8);
  DECLARE v_customerno varchar(50);
  DECLARE v_plat_table varchar(50);
  DECLARE v_uni_id varchar(64); #当前客户的统一ID

  DECLARE v_full_name varchar(50);
  DECLARE v_sex char(1);
  DECLARE v_job varchar(50);
  DECLARE v_birth_year smallint;
  DECLARE v_birthday date;
  DECLARE v_email varchar(100);
  DECLARE v_mobile varchar(20);
  DECLARE v_phone varchar(50);
  DECLARE v_zip varchar(20);
  DECLARE v_country varchar(50);
  DECLARE v_state varchar(50);
  DECLARE v_city varchar(50);
  DECLARE v_district varchar(100);
  DECLARE v_address varchar(255);

  DECLARE stmt varchar(1024);
  DECLARE v_isLastrow boolean DEFAULT FALSE; #define the flag for loop judgement
  DECLARE rowCursor Cursor For #define the cursor
          Select plat_code, customerno from uni_customer_plat where uni_id = v_uni_id order by plat_priority desc, IFNULL(changed, '0001-01-01'); #平台优先级第一序，变更时间第二序
  DECLARE Continue Handler for NOT FOUND set v_isLastrow = true; #define the continue handler for not found flag

    #客户合并规则 -------------------------------------------------------------------------------------
    # MergeRule 1：根据平台客户信息的平台优先级叠加和覆盖统一客户信息，优先级高的平台覆盖优先级低的平台(plat_proority)
    # MergeRule 2：根据平台客户信息更新时间的先后叠加和覆盖统一客户信息，新的变更覆盖旧的变更(changed)
    # MergeRule 3：(TODO: 将来可能会加入更多merge规则)
    #--------------------------------------------------------------------------------------------------

  select uni_id into v_uni_id from uni_customer_plat where plat_code = p_plat_code and customerno = p_customerno;

  #1.客户信息合并
  Open rowCursor; #开始打开游标
      rowLoop:Loop
          Fetch rowCursor Into v_plat_code, v_customerno;
          IF v_isLastrow THEN
             Leave rowLoop;
          ELSE
              Begin
                  Select plat_table into v_plat_table from uni_plat where plat_code = v_plat_code; #取当前平台表名
                  IF (p_plat_code = v_plat_code) THEN #若合并的来源平台与当前平台相同，则直接取触发器相应的客户记录
                      Set @full_name  = p_full_name;
                      Set @sex        = p_sex;
                      Set @job        = p_job;
                      Set @birth_year = p_birth_year;
                      Set @birthday   = p_birthday;
                      Set @email      = p_email;
                      Set @mobile     = p_mobile;
                      Set @phone      = p_phone;
                      Set @zip        = p_zip;
                      Set @country    = p_country;
                      Set @state      = p_state;
                      Set @city       = p_city;
                      Set @district   = p_district;
                      Set @address    = p_address;
                  ELSE # 否则到具体的平台表中取客户信息记录
                      /* Mysql触发器中动态SQL不可用，以静态SQL实现其功能
                      Set stmt = CONCAT('select full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address '
                                       ,' Into @full_name,@sex,@job,@birth_year,@birthday,@email,@mobile,@phone,@zip,@country,@state,@city,@district,@address '
                                       ,' From ', v_plat_table, ' where customerno = ''' , v_customerno, '''');
                      EXECUTE stmt;
                      */
                      BEGIN
                          CASE v_plat_code
                              WHEN 'taobao'   THEN Select full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address
                                                   Into   @full_name, @sex, @job, @birth_year, @birthday, @email, @mobile, @phone, @zip, @country, @state, @city, @district, @address
                                                   From plt_taobao_customer where customerno = v_customerno;
                              WHEN 'wdzx'     THEN Select full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address
                                                   Into   @full_name, @sex, @job, @birth_year, @birthday, @email, @mobile, @phone, @zip, @country, @state, @city, @district, @address
                                                   From plt_wdzx_customer where customerno = v_customerno;
                              WHEN 'kfgzt'    THEN Select full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address
                                                   Into   @full_name, @sex, @job, @birth_year, @birthday, @email, @mobile, @phone, @zip, @country, @state, @city, @district, @address
                                                   From plt_kfgzt_customer where customerno = v_customerno;
                              WHEN 'ext'      THEN Select full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address
                                                   Into   @full_name, @sex, @job, @birth_year, @birthday, @email, @mobile, @phone, @zip, @country, @state, @city, @district, @address
                                                   From plt_ext_customer where customerno = v_customerno;
                              WHEN 'extaobao' THEN Select full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address
                                                   Into   @full_name, @sex, @job, @birth_year, @birthday, @email, @mobile, @phone, @zip, @country, @state, @city, @district, @address
                                                   From plt_extaobao_customer where customerno = v_customerno;
                              WHEN 'modify'   THEN Select full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address
                                                   Into   @full_name, @sex, @job, @birth_year, @birthday, @email, @mobile, @phone, @zip, @country, @state, @city, @district, @address
                                                   From plt_modify_customer where customerno = v_customerno;
                          END CASE;
                      END;
                  END IF;

                  Set v_full_name = IFNULL(@full_name, v_full_name); #null字段不能覆盖有值字段！
                  Set v_sex = IFNULL(@sex, v_sex);
                  Set v_job = IFNULL(@job, v_job);
                  Set v_birth_year = IFNULL(@birth_year, v_birth_year);
                  Set v_birthday = IFNULL(@birthday, v_birthday);
                  Set v_email = IFNULL(@email, v_email);
                  Set v_mobile = IFNULL(@mobile, v_mobile);
                  Set v_phone = IFNULL(@phone, v_phone);
                  Set v_zip = IFNULL(@zip, v_zip);
                  Set v_country = IFNULL(@country, v_country);
                  Set v_state = IFNULL(@state, v_state);
                  Set v_city = IFNULL(@city, v_city);
                  Set v_district = IFNULL(@district, v_district);
                  Set v_address = IFNULL(@address, v_address);

              End;
          END IF;
      End Loop;
  Close rowCursor; #游标遍历结束

  #2.记录合并结果
  update uni_customer set full_name = v_full_name,
                          sex = v_sex,
                          job = v_job,
                          birth_year = v_birth_year,
                          birthday = v_birthday,
                          email = v_email,
                          mobile = v_mobile,
                          phone = v_phone,
                          zip = v_zip,
                          country = v_country,
                          state = v_state,
                          city = v_city,
                          district = v_district,
                          address = v_address
  where uni_id = v_uni_id;

END
//
delimiter ;


delimiter //
CREATE PROCEDURE proc_sync_parent_plat(IN p_plat_code char(8), IN p_parent_plat char(8), IN p_customerno varchar(50), IN p_full_name varchar(50), IN p_sex char(1), IN p_job varchar(50), IN p_birth_year smallint, IN p_birthday date, IN p_email varchar(100), IN p_mobile varchar(20), IN p_phone varchar(50), IN p_zip varchar(20), IN p_country varchar(50), IN p_state varchar(50), IN p_city varchar(50), IN p_district varchar(100), IN p_address varchar(255), IN p_changed datetime)
    COMMENT '将当前平台的客户信息同步到其父平台的过程'
BEGIN
    #DECLARE v_parent_plat char(8); #父平台代码

    #Select parent_plat Into v_parent_plat From uni_plat Where plat_code = v_plat_code;#取父平台代码
    CASE p_parent_plat
        WHEN 'taobao' THEN #若当前客户的父平台是“淘宝”
          BEGIN

              #若父平台是否不存在该客户，则在父平台插入该客户
              Insert Into plt_taobao_customer (customerno, full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address, changed)
              Select p_customerno, p_full_name, p_sex, p_job, p_birth_year, p_birthday, p_email, p_mobile, p_phone, p_zip, p_country, p_state, p_city, p_district, p_address, p_changed
              From plt_taobao_customer
              Where NOT EXISTS (select 1 from  plt_taobao_customer where customerno = p_customerno) Limit 1;

          END;
    END CASE;

END
//
delimiter ;


delimiter //
CREATE PROCEDURE proc_unify_customer(IN p_plat_code char(8), IN p_customerno varchar(50), IN p_full_name varchar(50), IN p_sex char(1), IN p_job varchar(50), IN p_birth_year smallint, IN p_birthday date, IN p_email varchar(100), IN p_mobile varchar(20), IN p_phone varchar(50), IN p_zip varchar(20), IN p_country varchar(50), IN p_state varchar(50), IN p_city varchar(50), IN p_district varchar(100), IN p_address varchar(255), IN p_changed datetime)
    COMMENT '统一客户信息的全过程'
BEGIN
  DECLARE v_plat_table varchar(50);
  DECLARE v_parent_plat char(8);
  DECLARE v_plat_priority smallint;

  #1.执行Dedup过程
  Select plat_table, parent_plat, plat_priority Into v_plat_table, v_parent_plat, v_plat_priority From uni_plat Where plat_code = p_plat_code Limit 1;
  CALL proc_dedup_customer(p_plat_code, v_parent_plat, v_plat_priority,
                           p_customerno, p_full_name, p_sex, p_job, p_birth_year, p_birthday, p_email, p_mobile, p_phone,
                           p_zip, p_country, p_state, p_city, p_district, p_address, p_changed, @is_dedup);#是否重复客户

  #2.若客户重复，则执行Merge过程
  IF (@is_dedup) THEN #若该客户是老客户（重复客户），则将当前客户与老客户合并成新的统一信息
      CALL proc_merge_customer(p_plat_code, v_parent_plat, v_plat_table, v_plat_priority,
                               p_customerno, p_full_name, p_sex, p_job, p_birth_year, p_birthday, p_email, p_mobile, p_phone,
                               p_zip, p_country, p_state, p_city, p_district, p_address, p_changed);
  END IF;
END
//
delimiter ;


delimiter //
CREATE PROCEDURE proc_unify_modify_customer(IN p_customerno varchar(64), IN p_full_name varchar(50), IN p_sex char(1), IN p_job varchar(50), IN p_birth_year smallint, IN p_birthday date, IN p_email varchar(100), IN p_mobile varchar(20), IN p_phone varchar(50), IN p_zip varchar(20), IN p_country varchar(50), IN p_state varchar(50), IN p_city varchar(50), IN p_district varchar(100), IN p_address varchar(255), IN p_changed datetime)
    COMMENT '统一客户信息被手工修改时过程'
BEGIN
  DECLARE v_uni_id varchar(64); #统一客户ID
  DECLARE v_plat_code char(8) DEFAULT 'modify';
  DECLARE v_plat_table varchar(50);
  DECLARE v_parent_plat char(8);
  DECLARE v_plat_priority smallint;

  #1.记录手工修改统一客户信息到平台关系表
  Select plat_table, parent_plat, plat_priority Into v_plat_table, v_parent_plat, v_plat_priority From uni_plat Where plat_code = v_plat_code Limit 1;
  Select uni_id into v_uni_id from uni_customer_plat where plat_code = v_plat_code and customerno = p_customerno limit 1;
  IF (v_uni_id IS NULL) THEN
      insert into uni_customer_plat (uni_id, plat_code, customerno, plat_priority, changed)
          values (p_customerno, v_plat_code, p_customerno, v_plat_priority, p_changed); #对于uni_id字段，此处手工修改平台客户ID与统一客户ID相同
  END IF;

  #2.执行Merge过程
  Call proc_merge_customer(v_plat_code, v_parent_plat, v_plat_table, v_plat_priority,
                           p_customerno, p_full_name, p_sex, p_job, p_birth_year, p_birthday, p_email, p_mobile, p_phone,
                           p_zip, p_country, p_state, p_city, p_district, p_address, p_changed);

END
//
delimiter ;


delimiter //
CREATE PROCEDURE proc_delete_customer(IN p_plat_code char(8), IN p_customerno varchar(50))
    COMMENT '删除客户相关平台信息'
BEGIN
  Delete from uni_customer_plat where plat_code = p_plat_code and customerno = p_customerno;
END 
//
delimiter ;

