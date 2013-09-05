package com.yunat.ccms.report.service.query.impl;

import static org.jooq.impl.Factory.decode;
import static org.jooq.impl.Factory.replace;
import static org.jooq.impl.Factory.round;
import static org.jooq.impl.Factory.val;

import java.util.List;

import org.jooq.Field;
import org.jooq.Select;
import org.jooq.impl.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yunat.ccms.core.support.jooq.MySQLFactorySingleton;
import com.yunat.ccms.jooq.tables.PltTaobaoCustomer;
import com.yunat.ccms.jooq.tables.TmDicValue;
import com.yunat.ccms.jooq.tables.TwfLogGroupUser;
import com.yunat.ccms.report.service.query.ReportCompose;

/**
 * 客户组-构成
 * 
 * @author yin
 * 
 */

@Component("customerReportComposeImpl")
public class CustomerReportComposeImpl implements ReportCompose {
	private static Logger logger = LoggerFactory.getLogger(CustomerReportComposeImpl.class);

	private static final Long buyerCreditLevDicId = 42L;

	Factory create = MySQLFactorySingleton.getInstance();

	@Override
	public List<String> findColumnHeaders(String platCode) {
		// TODO need find by platCode from db
		List<String> columnHeaders = Lists.newArrayList();
		columnHeaders.add("姓名");
		columnHeaders.add("性别");
		columnHeaders.add("生日");
		columnHeaders.add("省份");
		columnHeaders.add("城市");
		columnHeaders.add("客户全站等级");
		columnHeaders.add("淘宝昵称");
		columnHeaders.add("手机号");
		columnHeaders.add("email");
		columnHeaders.add("买家信用等级");
		columnHeaders.add("买家好评率");
		columnHeaders.add("地址");
		return columnHeaders;
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
	@Override
	public String builderSQLExpression(Long subjobId, String platCode) {
		TwfLogGroupUser groupUser = TwfLogGroupUser.TWF_LOG_GROUP_USER.as("_groupUser");
		TmDicValue dicValue = TmDicValue.TM_DIC_VALUE.as("_dicValue");
		PltTaobaoCustomer taobaoCustomer = PltTaobaoCustomer.PLT_TAOBAO_CUSTOMER.as("_taobaoCustomer");

		Field buyer_good_ratio = round(
				decode().when(taobaoCustomer.BUYER_CREDIT_TOTAL_NUM.gt(0),
						taobaoCustomer.BUYER_CREDIT_GOOD_NUM.mul(100).div(taobaoCustomer.BUYER_CREDIT_TOTAL_NUM))
						.otherwise(0), 1).as("buyer_good_ratio");

		Field<String> sexField = decode().when(taobaoCustomer.SEX.eq("f"), val("女"))
				.when(taobaoCustomer.SEX.eq("m"), val("男")).otherwise(val("未知")).as("sex");

		Field<String> vipInfoField = decode().when(taobaoCustomer.VIP_INFO.eq("c"), val("普通会员"))
				.when(taobaoCustomer.VIP_INFO.eq("asso_vip"), val("荣誉会员"))
				.otherwise(create.upper(taobaoCustomer.VIP_INFO)).as("vip_info");

		Select select = create
				.select(taobaoCustomer.FULL_NAME, sexField, taobaoCustomer.BIRTHDAY, taobaoCustomer.STATE,
						taobaoCustomer.CITY, vipInfoField, taobaoCustomer.CUSTOMERNO, taobaoCustomer.MOBILE,
						taobaoCustomer.EMAIL, dicValue.SHOW_NAME.as("buyer_credit_lev"), buyer_good_ratio,
						taobaoCustomer.ADDRESS).from(groupUser).leftOuterJoin(taobaoCustomer)
				.on(replace(groupUser.UNI_ID, "taobao|", "").equal(taobaoCustomer.CUSTOMERNO)).leftOuterJoin(dicValue)
				.on(dicValue.DIC_VALUE.equal(taobaoCustomer.BUYER_CREDIT_LEV.cast(String.class)))
				.and(dicValue.DIC_ID.equal(buyerCreditLevDicId)).where(groupUser.SUBJOB_ID.equal(subjobId));

		logger.info("builder sql expression : [{}]", select.getSQL(true));
		return select.getSQL(true);
	}

}