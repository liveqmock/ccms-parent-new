package com.yunat.ccms.report.service.query.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yunat.ccms.report.service.query.EvaluateReportCompose;

/**
 * 评估报表-客户明细-构成
 * 
 * @author yin
 * 
 */
@Component("evaluateReportCustomerComposeImpl")
public class EvaluateReportCustomerComposeImpl implements EvaluateReportCompose {

	@Override
	public List<String> findColumnHeaders() {
		List<String> columnHeaders = Lists.newArrayList();
		columnHeaders.add("用户ID");
		columnHeaders.add("姓名");
		columnHeaders.add("性别");
		columnHeaders.add("生日");
		columnHeaders.add("省份");
		columnHeaders.add("城市");
		columnHeaders.add("区县");
		columnHeaders.add("客户全站等级");
		columnHeaders.add("手机号");
		columnHeaders.add("email");
		columnHeaders.add("买家信用等级");
		columnHeaders.add("买家好评率");
		columnHeaders.add("地址");
		columnHeaders.add("邮编");
		columnHeaders.add("职业");
		return columnHeaders;
	}

	@Override
	public StringBuilder builderSQLExpression(String previousNodeOutput) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ");
		sb.append(" c.customerno, ");
		sb.append(" c.full_name, ");
		sb.append(" CASE WHEN  c.sex = 'm' THEN '男'   WHEN c.sex = 'f' THEN '女'  ELSE '未知' END as sex, ");
		sb.append(" c.birthday, ");
		sb.append(" c.state, ");
		sb.append(" c.city, ");
		sb.append(" c.district, ");
		// 客户全站等级
		sb.append(" CASE WHEN c.vip_info ='c'  THEN '普通会员'   ");
		sb.append(" WHEN c.vip_info = 'asso_vip' THEN '荣誉会员' ");
		sb.append(" ELSE c.vip_info  END  AS  vip_info, ");
		sb.append(" c.mobile, ");
		sb.append(" c.email, ");
		// 买家信用等级
		sb.append(" d.show_name, ");
		// 买家好评率
		sb.append(" ifnull(CASE WHEN  c.buyer_credit_total_num >0  then c.buyer_credit_good_num/c.buyer_credit_total_num*100  ELSE NULL END ,0),");
		sb.append(" c.address, ");
		sb.append(" c.zip, ");
		sb.append(" c.job ");

		sb.append(" from  "
				+ previousNodeOutput
				+ "  p, plt_taobao_order o, plt_taobao_customer  c LEFT OUTER JOIN    tm_dic_value  d  ON   c.buyer_credit_lev  = d.dic_value  and d.dic_id = 42 ");

		sb.append(" where p.uni_id = CONCAT('taobao|', o.customerno) ");
		sb.append(" and   o.customerno  = c.customerno ");
		sb.append(" and o.created >=  DATE_FORMAT(:startDate,'%Y-%m-%d %H')  "
				+ " and o.created <  DATE_FORMAT(:endDate,'%Y-%m-%d %H') ");
		sb.append(" and o.dp_id IN (:shopId) ");
		sb.append(" and o.ccms_order_status  in  (21,22,23) ");

		sb.append(" group by  date_format(o.created,'%Y-%m-%d'),c.customerno ");
		sb.append(" order by date_format(o.created,'%y-%m-%d') ");

		return sb;

	}
}
