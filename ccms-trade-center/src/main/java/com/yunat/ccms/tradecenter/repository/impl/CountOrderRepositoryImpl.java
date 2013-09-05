package com.yunat.ccms.tradecenter.repository.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;
import com.yunat.ccms.tradecenter.constant.ConstantTC;
import com.yunat.ccms.tradecenter.controller.vo.UrpayOrderRequest;
import com.yunat.ccms.tradecenter.repository.CountOrderRepository;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.ccms.tradecenter.util.FileDownLoadUtil;

/**
 *订单统计实现类
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-5 下午03:39:33
 */
@Repository
public class CountOrderRepositoryImpl implements CountOrderRepository{

	private static Logger logger = LoggerFactory.getLogger(CountOrderRepositoryImpl.class);

	@Autowired
    private JdbcPaginationHelper helper;


	private String[] construct(Map<String, String> columnNameMap, Map<String, Object> paramMap, StringBuffer sql, UrpayOrderRequest req){
		// 列名对应的中文名称
		columnNameMap.put("tradeCreated", "下单时间");
		columnNameMap.put("created", "发送时间");
		columnNameMap.put("mobile", "手机号码");
		columnNameMap.put("type", "发送类型");
		columnNameMap.put("buyerNick", "淘宝昵称");
		columnNameMap.put("tid", "订单编号");
		columnNameMap.put("smsContent", "短信内容");
		// 查询 sql
		sql.append("select from tb_tc_send_log ").append("where dp_id = :dpId and send_status = 1 ");
		// 查询参数
		paramMap.put("dpId", req.getDpId());
		boolean typeFlag = true;
		if (ObjectUtils.notEqual(req.getType(), null)){
			typeFlag = false;
			sql.append("and type = :type ");
			paramMap.put("type", req.getType());
		}
		if (typeFlag){ // 在无类型的情况下需要区分是否是这个系统的记录
			sql.append("and type in (:types) ");
			paramMap.put("types", UserInteractionType.getTypeListBySysTypeShow(req.getSysType(), true));
		}
		boolean dateFlag = true;
		if (ObjectUtils.notEqual(req.getStartCreated(), null) && ObjectUtils.notEqual(req.getEndCreated(), null)){
			dateFlag = false;
			sql.append("and (trade_created between :startCreated and :endCreated) ");
			paramMap.put("startCreated", new Date(req.getStartCreated()));
			paramMap.put("endCreated", new Date(req.getEndCreated()));
		}
		if (ObjectUtils.notEqual(req.getSendStartCreated(), null) && ObjectUtils.notEqual(req.getSendEndCreated(), null)){
			dateFlag = false;
			sql.append("and (created between :sendStartCreated and :sendEndCreated) ");
			paramMap.put("sendStartCreated", new Date(req.getSendStartCreated()));
			paramMap.put("sendEndCreated", new Date(req.getSendEndCreated()));
		}
		if (dateFlag){ // 默认无时间的情况下
			sql.append("and trade_created >= adddate(current_date(), interval -29 day) ");
		}
		if (StringUtils.isNotEmpty(req.getMobileOrNickOrTid())){
			sql.append("and (mobile = :fields or buyer_nick = :fields or tid = :fields) ");
			paramMap.put("fields", req.getMobileOrNickOrTid());
		}
		sql.append("order by created desc");
		String columns = " date_format(trade_created, ''%Y-%m-%d %H:%i:%s'') as {0}, date_format(created, ''%Y-%m-%d %H:%i:%s'') as {1}, mobile as {2}, type as {3}, buyer_nick as {4}, tid as {5}, sms_content as {6}";
		String[] columnName = columnNameMap.keySet().toArray(new String[0]);
		columns = MessageFormat.format(columns, (Object[])columnName);
		sql.insert(6, columns);
		return columnName;
	}

	public void getSendLogByTypeAlls(UrpayOrderRequest request, final PrintWriter writer) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		final Map<String, String> columnNameMap = new LinkedHashMap<String, String>(6);
		final String[] columns = construct(columnNameMap, paramMap, sql, request);
		helper.query(sql.toString(), paramMap, new RowCallbackHandler() {

			private Map<Integer, String> msgs = UserInteractionType.getTypeMsgMap();

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				try {
					writerHeader();
					writerContent(rs);
					while (rs.next()) {
						writerContent(rs);
					}
					FileDownLoadUtil.flushClose(writer);
				} catch (IOException e) {
					logger.info("导出发送记录异常 : [{}]", e);
				}
			}

			/** 写出头 */
			private void writerHeader() throws IOException {
				for (int i = 0; i < columns.length; i++) {
					// 判断是否为最后一列，如果是则不加
					String columnSplit = i == (columns.length - 1) ? "" : ConstantTC.TABLE_COMMA;
					FileDownLoadUtil.writer(writer, columnNameMap.get(columns[i]) + columnSplit); // 写出列标题
				}
				FileDownLoadUtil.writer(writer, ConstantTC.NEWLINE);
			}

			/** 写出内容 */
			private void writerContent(ResultSet rs) throws SQLException, IOException {
				for (int i = 0; i < columns.length; i++) {
					Object value = columns[i].equals("type") ? msgs.get(rs.getObject(columns[i])) : rs.getObject(columns[i]);
					FileDownLoadUtil.writer(writer, value + (i == (columns.length - 1) ? "" : ConstantTC.TABLE_COMMA));
				}
				FileDownLoadUtil.writer(writer, ConstantTC.NEWLINE);
			}

		});
	}

	public Page<Map<String, Object>> getSendLogByType(Pageable page, UrpayOrderRequest request) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, String> columnNameMap = new LinkedHashMap<String, String>(6);
		construct(columnNameMap, paramMap, sql, request);
		return helper.queryForMap(sql, paramMap, page);
	}

	@Override
	public List<Map<String, Object>> countUrpayOrderNum(Integer urpayType) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(urpayType!=null){
			String statusStr = null;
			if(urpayType==1){
				statusStr = " s.auto_urpay_status=1 ";//自动催付
			}else if(urpayType==2){
				statusStr = " s.close_urpay_status=1 ";//预关闭催付
			}else if(urpayType==3){
				statusStr = " s.cheap_urpay_status=1 ";//聚划算催付
			}
			if(statusStr!=null){
				String sql = "select o.dp_id as dpId,date(o.created) as urpayDate,count(*) as orderNum," +
					"sum(case when o.pay_time is not null then 1 else 0 end ) as responseNum, " +
					"sum(case when o.pay_time is not null then o.payment else 0 end ) as responseAmount " +
					"from plt_taobao_order_tc o join tb_tc_urpay_status s on o.tid=s.tid  " +
					"where "+statusStr+" and o.created>curdate()- INTERVAL 29 DAY  GROUP BY dp_id,date(o.created)";
				list = helper.queryForList(sql, new HashMap<String, Object>(0));
			}
		}
		return list;
	}

	@Override
	public Integer countUrpaySmsNum(Integer urpayType, String dpId,String urpayDate) {
		String urpayTypeStr = "";
		String dpIdStr = "";
		String urpayDateStr = "";
		if(urpayType!=null){
			urpayTypeStr = " and  type="+urpayType;
		}
		if(dpId!=null){
			dpIdStr = " and dp_id='"+dpId+"'";
		}
		if(urpayDate!=null){
			urpayDateStr = " and date(trade_created)='"+urpayDate+"'";
		}
		String sql = "select sum(sms_num) as num from tb_tc_send_log where 1=1 "+urpayTypeStr+dpIdStr+urpayDateStr;
		return helper.queryForInt(sql, new HashMap<String, Object>(0));
	}

	@Override
	public List<Map<String, Object>> countAllUrpaySmsNum() {
		String sql ="select type,dp_id as dpId,date(trade_created) as urpayDate,sum(sms_num) as num from tb_tc_send_log where send_status = 1 and trade_created>curdate()- INTERVAL 29 DAY GROUP BY type,dp_id,date(trade_created)";
		return helper.queryForList(sql, new HashMap<String, Object>(0));
	}

	@Override
	public List<Map<String, Object>> countAllUrpaySmsNum(Integer type,String dpId) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(type!=null&&StringUtils.isNotEmpty(dpId)){
			String sql ="select date(trade_created) as urpayDate,sum(sms_num) as num from tb_tc_send_log where send_status = 1 and type="+type+" and dp_id='"+dpId+"' and trade_created>curdate()- INTERVAL 29 DAY GROUP BY date(trade_created)";
			list = helper.queryForList(sql, new HashMap<String, Object>(0));
		}
		return list;
	}



}
