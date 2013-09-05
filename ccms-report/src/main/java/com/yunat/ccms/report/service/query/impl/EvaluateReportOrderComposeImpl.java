package com.yunat.ccms.report.service.query.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yunat.ccms.report.service.query.EvaluateReportCompose;

/**
 * 评估报表-订单-构成
 * 
 * @author yin
 * 
 */
@Component("evaluateReportOrderComposeImpl")
public class EvaluateReportOrderComposeImpl implements EvaluateReportCompose {

	@Override
	public List<String> findColumnHeaders() {
		List<String> columnHeaders = Lists.newArrayList();
		columnHeaders.add("订单编号");
		columnHeaders.add("客户ID");
		columnHeaders.add("下单时间");
		columnHeaders.add("付款时间");
		columnHeaders.add("发货时间");
		columnHeaders.add("应付金额");
		columnHeaders.add("实付金额");
		columnHeaders.add("订单状态");
		return columnHeaders;
	}

	@Override
	public StringBuilder builderSQLExpression(String previousNodeOutput) {

		StringBuilder sb = new StringBuilder();
		sb.append(" select ");

		sb.append(" o.tid, ");
		sb.append(" o.customerno, ");
		sb.append(" o.created, ");
		sb.append(" o.pay_time, ");
		sb.append(" o.consign_time, ");
		sb.append(" o.total_fee, ");
		sb.append(" o.payment, ");
		sb.append(" CASE WHEN o.ccms_order_status = 10 THEN '已下单未付款'    ");
		sb.append(" WHEN o.ccms_order_status = 21 THEN '已付款未发货'  ");
		sb.append(" WHEN o.ccms_order_status = 22 THEN '已发货待确认'  ");
		sb.append(" WHEN o.ccms_order_status = 23 THEN '交易成功'  ");
		sb.append(" ELSE '交易失败' END  ");

		sb.append(" from  " + previousNodeOutput + "  n, plt_taobao_order o  ");

		sb.append(" where n.uni_id = CONCAT('taobao|', o.customerno) ");

		sb.append(" and   o.created >=  DATE_FORMAT(:startDate,'%Y-%m-%d %H')  "
				+ " and   o.created <  DATE_FORMAT(:endDate,'%Y-%m-%d %H') ");

		sb.append(" and   o.dp_id IN (:shopId) ");

		sb.append(" order by date_format(o.created,'%y-%m-%d') ");

		return sb;
	}

}
