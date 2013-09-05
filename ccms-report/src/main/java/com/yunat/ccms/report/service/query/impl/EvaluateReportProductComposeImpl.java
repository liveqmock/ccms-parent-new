package com.yunat.ccms.report.service.query.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yunat.ccms.report.service.query.EvaluateReportCompose;

/**
 * 评估报表-商品明细-构成
 * 
 * @author yin
 * 
 */
@Component("evaluateReportProductComposeImpl")
public class EvaluateReportProductComposeImpl implements EvaluateReportCompose {

	@Override
	public List<String> findColumnHeaders() {
		List<String> columnHeaders = Lists.newArrayList();
		columnHeaders.add("商品编号");
		columnHeaders.add("商品名称");
		columnHeaders.add("商品外部编码");
		columnHeaders.add("付款客人数");
		columnHeaders.add("付款订单数");
		columnHeaders.add("付款商品数量");
		columnHeaders.add("付款商品金额");
		return columnHeaders;
	}

	@Override
	public StringBuilder builderSQLExpression(String previousNodeOutput) {

		StringBuilder sb = new StringBuilder();
		sb.append(" select  ");
		sb.append(" p.num_iid, ");
		sb.append(" p.title, ");
		sb.append(" p.outer_id, ");

		// 成交客人数
		sb.append(" count(distinct i.customerno), ");
		// 成交订单数
		sb.append(" count(distinct i.tid), ");
		// 成交数量
		sb.append(" sum(i.num), ");
		// 成交金额
		sb.append(" sum(i.payment) ");

		sb.append(" from  " + previousNodeOutput
				+ "  n, plt_taobao_order o, plt_taobao_order_item i ,plt_taobao_product  p ");

		sb.append(" where n.uni_id = CONCAT('taobao|', o.customerno) ");
		sb.append(" and   o.tid  = i.tid ");
		sb.append(" and   i.num_iid  = p.num_iid ");
		sb.append(" and   o.created >=  DATE_FORMAT(:startDate,'%Y-%m-%d %H')  "
				+ " and   o.created <  DATE_FORMAT(:endDate,'%Y-%m-%d %H') ");
		sb.append(" and   o.dp_id IN (:shopId) ");
		sb.append(" and   o.ccms_order_status  in  (21,22,23) ");

		sb.append(" group by date_format(o.created,'%Y-%m-%d'),p.num_iid, p.title,p.outer_id ");
		sb.append(" order by date_format(o.created,'%y-%m-%d') ");

		return sb;
	}

}
