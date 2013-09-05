package com.yunat.ccms.tradecenter.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.constant.ConstantTC;
import com.yunat.ccms.tradecenter.controller.vo.SendGoodsQueryRequest;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.repository.BaseRepository;
import com.yunat.ccms.tradecenter.repository.IOrderRepository;
import com.yunat.ccms.tradecenter.service.PropertiesConfigManager;
import com.yunat.ccms.tradecenter.service.impl.BuyerStatisticServiceImpl;
import com.yunat.ccms.tradecenter.service.queryobject.OrderQuery;
import com.yunat.ccms.tradecenter.support.cons.OrderStatus;
import com.yunat.ccms.tradecenter.support.cons.PropertiesNameEnum;
import com.yunat.ccms.tradecenter.support.cons.UrpayQueryType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class OrderRepositoryImpl extends BaseRepository implements IOrderRepository {

	private static Logger logger = LoggerFactory.getLogger(BuyerStatisticServiceImpl.class);

	@Autowired
	private JdbcPaginationHelper helper;

	@Autowired
	private PropertiesConfigManager propertiesConfigManager;

    @PersistenceContext
    private EntityManager em;

	@Override
	public List<OrderDomain> findOrders(final OrderQuery orderQuery) {

		String sql = getSql(orderQuery, 1);
		logger.info("sql:{}", sql);

		RowMapper<OrderDomain> tidMapper = new RowMapper<OrderDomain>() {
			@Override
			public OrderDomain mapRow(ResultSet rs, int paramInt) throws SQLException {
				OrderDomain orderDomain = new OrderDomain();
				orderDomain.setTid(rs.getString("tid"));
				orderDomain.setDpId(rs.getString("dp_id"));
				orderDomain.setCustomerno(rs.getString("customerno"));
				orderDomain.setCreated(rs.getTimestamp("created"));
				orderDomain.setTradeFrom(rs.getString("trade_from"));
				orderDomain.setPayment(rs.getDouble("payment"));
				orderDomain.setPostFee(rs.getDouble("post_fee"));
				orderDomain.setSellerFlag(rs.getInt("seller_flag"));
				orderDomain.setBuyerMessage(rs.getString("buyer_message"));
				orderDomain.setReceiverAddress(rs.getString("receiver_address"));
				orderDomain.setReceiverCity(rs.getString("receiver_city"));
				orderDomain.setReceiverDistrict(rs.getString("receiver_district"));
				orderDomain.setReceiverMobile(rs.getString("receiver_mobile"));
				orderDomain.setReceiverName(rs.getString("receiver_name"));
				orderDomain.setReceiverPhone(rs.getString("receiver_phone"));
				orderDomain.setReceiverState(rs.getString("receiver_state"));
				orderDomain.setReceiverZip(rs.getString("receiver_zip"));
				orderDomain.setTradeFrom(rs.getString("trade_from"));
				orderDomain.setStatus(rs.getString("status"));
				orderDomain.setBuyerMessage(rs.getString("buyer_message"));

				return orderDomain;
			}
		};

		List<OrderDomain> orderDomains = helper.query(sql, new HashMap<String, Object>(0), tidMapper);

		return orderDomains;
	}

	@Override
	public void findOrderCount(OrderQuery orderQuery) {

		String sqlCount = getSql(orderQuery, 2);
		logger.info("count_sql:{}", sqlCount);

		int count = helper.queryForInt(sqlCount, new HashMap<String, Object>(0));
		orderQuery.setTotalItem(count);
	}

	/**
	 * 拼接sql
	 *
	 * @param orderQuery
	 * @param type
	 *            1--表示获取值， 2--表示获取总记录数
	 * @return
	 */
	private String getSql(OrderQuery orderQuery, int type) {

		Integer urpayQueryType = orderQuery.getUrpayStatus();

		String from = "select o.* from plt_taobao_order_tc o force index(idx_created_dpid_status_customerno)";
		String fromCount = "select count(DISTINCT(o.tid)) as num from plt_taobao_order_tc o force index(idx_created_dpid_status_customerno)";

		// 连接
		String join = "";
		if (!StringUtils.isEmpty(orderQuery.getTitle()) || !StringUtils.isEmpty(orderQuery.getOuterIid())) {
			join += " inner join plt_taobao_order_item_tc oi on oi.tid = o.tid";
		}

		if (!StringUtils.isEmpty(orderQuery.getServiceStaffName()) || orderQuery.getIsHide() != null) {
			join += " inner join tb_tc_customer_orders_ship os on os.tid = o.tid";
		}

		if (urpayQueryType != null) {

			// 已催付
			if (urpayQueryType.equals(UrpayQueryType.HAS_URPAY.getType())) {
				join += " inner join tb_tc_urpay_status us on us.tid = o.tid and (us.auto_urpay_status = 1 or us.close_urpay_status=1 or us.cheap_urpay_status=1 or us.manual_urpay_status=1)";
			} else if (urpayQueryType.equals(UrpayQueryType.NO_URPAY.getType())) {
				join += " inner join tb_tc_urpay_status us on us.tid = o.tid";
			} else {
				join += " inner join tb_tc_urpay_status us on us.tid = o.tid inner join tb_tc_buyer_interaction_statistic ins on ins.dp_id=o.dp_id and ins.customerno = o.customerno";
			}

		}

		// where条件
		String conditions = " where o.dp_id = " + orderQuery.getDpId();
		conditions += " and o.status = '" + OrderStatus.WAIT_BUYER_PAY.getStatus() + "'";

		// 如果商品名不为空
		if (!StringUtils.isEmpty(orderQuery.getTitle())) {
			String[] titles = orderQuery.getTitle().split("\\s");
			for (String title : titles) {
				conditions += " and oi.title like '%" + title + "%'";
			}
		}

		// 如果商品外部编码不会空
		if (!StringUtils.isEmpty(orderQuery.getOuterIid())) {
			conditions += " and oi.outer_iid like '%" + orderQuery.getOuterIid() + "%'";
		}

		// 如果引导客服不为空
		if (!StringUtils.isEmpty(orderQuery.getServiceStaffName())) {
			conditions += " and os.service_staff_id = '" + orderQuery.getServiceStaffName() + "'";
		}

		// 是否隐藏
		if (orderQuery.getIsHide() != null) {
			if (orderQuery.getIsHide()) {
				conditions += " and os.is_hide = " + orderQuery.getIsHide();
			} else {
				conditions += " and (os.is_hide is null || os.is_hide = " + orderQuery.getIsHide() + ")";
			}

		}

		// 客户名不为空
		if (!StringUtils.isEmpty(orderQuery.getCustomerno())) {
			conditions += " and o.customerno = '" + orderQuery.getCustomerno() + "'";
		}

		// 客户手机不为空
		if (!StringUtils.isEmpty(orderQuery.getMobile())) {
			conditions += " and o.receiver_mobile = " + orderQuery.getMobile();
		}

		// 如果开始时间不为空
		if (!StringUtils.isEmpty(orderQuery.getCreatedStartTime())) {
			conditions += " and o.created >= '" + orderQuery.getCreatedStartTime() + "'";
		}

		// 如果结束时间不为空
		if (!StringUtils.isEmpty(orderQuery.getCreatedEndTime())) {
			conditions += " and o.created <= '" + orderQuery.getCreatedEndTime() + "'";
		}

		if (urpayQueryType != null) {

			// 未催付
			if (urpayQueryType.equals(UrpayQueryType.NO_URPAY.getType())) {
				conditions += " and us.auto_urpay_status != 1 and us.close_urpay_status!=1 and us.cheap_urpay_status!=1 and us.manual_urpay_status!=1";
			}
			// 建议催付
			else if (urpayQueryType.equals(UrpayQueryType.ADVICE_URPAY.getType())) {
				conditions += "  and us.auto_urpay_status != 1 and us.close_urpay_status!=1 and us.cheap_urpay_status!=1 and us.manual_urpay_status!=1 and ins.trade_payed_count=0 and ins.urpay_count=0";
			}
			// 不建议催付
			else if (urpayQueryType.equals(UrpayQueryType.DEPRECATED_URPAY.getType())) {
				conditions += " and us.auto_urpay_status != 1 and us.close_urpay_status!=1 and us.cheap_urpay_status!=1 and us.manual_urpay_status!=1 and (ins.trade_payed_count>0 || ins.urpay_count>0)";
			}

		}

		// 分组
		String group = "";
		if (!StringUtils.isEmpty(orderQuery.getTitle()) || !StringUtils.isEmpty(orderQuery.getOuterIid())) {
			group = " group by o.tid";
		}

		// 排序
		String order = "";
		if (orderQuery.getFirstOrder() != null && orderQuery.getFirstOrderSort() != null) {
			order = " order by o." + orderQuery.getFirstOrder() + " " + orderQuery.getFirstOrderSort();
		}

		// 限制
		String limit = " limit " + orderQuery.getStartRow() + "," + orderQuery.getPageSize();

		String sqlCount = fromCount + join + conditions;

		String sql = from + join + conditions + group + order + limit;

		if (type == 1) {
			return sql;
		} else {
			return sqlCount;
		}
	}

	@Override
	public List<OrderDomain> findWaitSendGoodsOrders(SendGoodsQueryRequest request) {
		String sql = getSendGoodsQuerySql(request, 0);
		logger.info("获取发货订单sql:[" + sql + "]");

		RowMapper<OrderDomain> tidMapper = new RowMapper<OrderDomain>() {
			@Override
			public OrderDomain mapRow(ResultSet rs, int paramInt) throws SQLException {
				OrderDomain orderDomain = new OrderDomain();
				orderDomain.setTid(rs.getString("tid"));
				orderDomain.setDpId(rs.getString("dp_id"));
				orderDomain.setCustomerno(rs.getString("customerno"));
				orderDomain.setPayTime(rs.getTimestamp("pay_time"));
				orderDomain.setTradeFrom(rs.getString("trade_from"));
				orderDomain.setPayment(rs.getDouble("payment"));
				orderDomain.setPostFee(rs.getDouble("post_fee"));
				orderDomain.setReceiverAddress(rs.getString("receiver_address"));
				orderDomain.setReceiverCity(rs.getString("receiver_city"));
				orderDomain.setReceiverDistrict(rs.getString("receiver_district"));
				orderDomain.setReceiverMobile(rs.getString("receiver_mobile"));
				orderDomain.setReceiverName(rs.getString("receiver_name"));
				orderDomain.setReceiverPhone(rs.getString("receiver_phone"));
				orderDomain.setReceiverState(rs.getString("receiver_state"));
				orderDomain.setReceiverZip(rs.getString("receiver_zip"));
				orderDomain.setStatus(rs.getString("status"));
				return orderDomain;
			}
		};

		List<OrderDomain> orderDomains = helper.query(sql, new HashMap<String, Object>(0), tidMapper);
		return orderDomains;
	}

	/**
	 * 获取查询发货的sql语句
	 *
	 * @param request
	 * @return
	 */
	private String getSendGoodsQuerySql(SendGoodsQueryRequest request, int type) {
		String sql = "select o.tid,o.dp_id,o.pay_time,o.customerno,o.trade_from,o.payment,o.post_fee,o.receiver_state,o.receiver_city,"
				+ "o.receiver_district,o.receiver_address,o.receiver_mobile,o.receiver_name,o.receiver_phone,o.receiver_zip,o.status from plt_taobao_order_tc o force index(idx_status_dpid)";

		String sqlCount = "select count(DISTINCT(o.tid)) as num from plt_taobao_order_tc o force index(idx_status_dpid)";

		String join = "";
		if (request.getCareStatus() != -1 || request.getIsHide() != -1) {
			join += " inner join tb_tc_customer_orders_ship os on os.tid = o.tid";
		}
		if (StringUtils.isNotBlank(request.getTitle())) {
			join += " inner join plt_taobao_order_item_tc oi on oi.tid = o.tid";
		}

		// 默认条件
		// String condition = " where o.dp_id = '" + request.getDpId() +
		// "' and o.status = '" + OrderStatus.WAIT_SELLER_SEND_GOODS.getStatus()
		// + "'";
		// 增加部分发货功能
		String condition = " where o.dp_id = '" + request.getDpId() + "' and o.status in ('"
				+ OrderStatus.WAIT_SELLER_SEND_GOODS.getStatus() + "','"
				+ OrderStatus.SELLER_CONSIGNED_PART.getStatus() + "')";
		// 发货等待天数
		if (request.getWaitDay() != null) {
			// 当前时间
			Date curTime = new Date();
			Date waitTime = null;
			if (request.getWaitDay() == 0) {
				// 24小时前
				// Date fefore24Hour = DateUtils.addHour(curTime, -24);
				// condition = condition + " and o.pay_time >='" +
				// DateUtils.getStringDate(fefore24Hour) + "'";
				waitTime = curTime;
			} else {
				waitTime = DateUtils.addDay(curTime, -request.getWaitDay());
			}
			condition = condition + " and o.pay_time <='" + DateUtils.getStringDate(waitTime) + "'";
		}

		// 订单编号
		if (StringUtils.isNotBlank(request.getTid())) {
			condition = condition + " and o.tid ='" + request.getTid() + "'";
		}
		// 客户昵称
		if (StringUtils.isNotBlank(request.getCustomerno())) {
			condition = condition + " and o.customerno ='" + request.getCustomerno() + "'";
		}
		// 付款开始时间
		if (StringUtils.isNotBlank(request.getPayedStartTime())) {
			condition = condition + " and o.pay_time >='" + request.getPayedStartTime() + "'";
		}
		// 付款结束时间
		if (StringUtils.isNotBlank(request.getPayedEndTime())) {
			condition = condition + " and o.pay_time <='" + request.getPayedEndTime() + "'";
		}

		// 收货省份
		if (StringUtils.isNotBlank(request.getReceiverState())
				&& !StringUtils.equalsIgnoreCase("-1", request.getReceiverState())) {
			condition = condition + " and o.receiver_state ='" + request.getReceiverState() + "'";
		}

		// 关怀状态
		if (request.getCareStatus() != -1) {
			condition = condition + getCareStatusSql(request.getCareStatus(), request.getDpId());
		}
		// 是否隐藏
		if (request.getIsHide() != -1) {
			if (request.getIsHide() == 0) {// 不隐藏
				condition = condition + " and os.sendgoods_hide = 0";
			} else if (request.getIsHide() == 1) {// 隐藏
				condition = condition + " and os.sendgoods_hide = 1";
			}
		}
		// 商品名称
		if (StringUtils.isNotBlank(request.getTitle())) {
			String[] titles = request.getTitle().split("\\s");
			for (String title : titles) {
				condition = condition + " and oi.title like '%" + title + "%'";
			}
		}

		// 分组
		String group = "";
		if (StringUtils.isNotBlank(request.getTitle())) {
			group = " group by o.tid";
		}
		// 排序
		String sort = "";
		if (StringUtils.isNotBlank(request.getOrderSort())) {
			sort = sortSql(request.getOrderSort());
		}
		// 分页
		String limit = " limit " + request.getStartRow() + "," + request.getPageSize();

		String returnSql = "";
		if (type == 0) {
			returnSql = sql + join + condition + group + sort + limit;
		} else {
			returnSql = sqlCount + join + condition;
		}
		return returnSql;
	}

	private String getCareStatusSql(Integer careStatus, String dpId) {
		// 严重延迟
		int seriousDelay = ConstantTC.SERIOUS_DELAY;

		Integer seriousDelayObj = propertiesConfigManager.getInt(dpId, PropertiesNameEnum.SERIOUS_DELAY.getName());
		if (seriousDelayObj != null) {
			seriousDelay = seriousDelayObj;
		}
		// 一般延迟
		int commonDelay = ConstantTC.COMMON_DELAY;
		Integer commonDelayObj = propertiesConfigManager.getInt(dpId, PropertiesNameEnum.AVERAGE_DELAY.getName());
		if (commonDelayObj != null) {
			commonDelay = commonDelayObj;
		}

		// 当前时间
		Date curTime = new Date();

		Date seriousDelayTime = DateUtils.addHour(curTime, -seriousDelay);

		Date commonDelayTime = DateUtils.addHour(curTime, -commonDelay);

		String sql = "";
		// 未催付
		if (careStatus == 0) {
			sql = " and os.sendgoods_care_status = 0";
		} else if (careStatus == 1) {// 已催付
			sql = " and os.sendgoods_care_status = 1";
		} else if (careStatus == 2) {// 严重延迟
			sql = " and os.sendgoods_care_status = 0 and o.pay_time <='"
					+ DateUtils.getStringDate(seriousDelayTime) + "'";
		} else if (careStatus == 3) {// 轻微延迟
			sql = " and os.sendgoods_care_status = 0 and o.pay_time <='"
					+ DateUtils.getStringDate(commonDelayTime) + "' and o.pay_time >'"
					+ DateUtils.getStringDate(seriousDelayTime) + "'";
		} else if (careStatus == 4) {// 正常未发货
			sql = " and os.sendgoods_care_status = 0 and o.pay_time <'"
					+ DateUtils.getStringDate(curTime) + "' and o.pay_time >'"
					+ DateUtils.getStringDate(commonDelayTime) + "'";
		}
		return sql;
	}

	private String sortSql(String sort) {
		String sortSql = "";
		if (StringUtils.equals("payTime_asc", sort)) {
			sortSql = " order by o.pay_time asc";
		}
		if (StringUtils.equals("payTime_desc", sort)) {
			sortSql = " order by o.pay_time desc";
		}
		if (StringUtils.equals("payment_asc", sort)) {
			sortSql = " order by o.payment asc";
		}
		if (StringUtils.equals("payment_desc", sort)) {
			sortSql = " order by o.payment desc";
		}
		return sortSql;
	}

	@Override
	public long findWaitSendGoodsOrdersCount(SendGoodsQueryRequest request) {
		String sqlCount = getSendGoodsQuerySql(request, 2);
		logger.info("count_sql:{}", sqlCount);
		int count = helper.queryForInt(sqlCount, new HashMap<String, Object>(0));
		return count;
	}

	@Override
	public Integer countOrderCreateTime(String dpId, String created) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1)");
		sql.append(" from plt_taobao_order_tc o");
		sql.append(" where o.dp_id = :dpId and o.created >= :created");

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("dpId", dpId);
		paramMap.put("created", created);

		Integer count = helper.queryForInt(sql.toString(), paramMap);
		return count;
	}

	@Override
	public Integer countOrderPayTime(String dpId, String created) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1)");
		sql.append(" from plt_taobao_order_tc o");
		sql.append(" where o.dp_id = :dpId and o.created >= :created and o.pay_time >= :payTime");

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("dpId", dpId);
		paramMap.put("created", created);
		paramMap.put("payTime", created);

		Integer count = helper.queryForInt(sql.toString(), paramMap);
		return count;
	}

	@Override
	public Integer countOrderConsignTime(String dpId, String created) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1)");
		sql.append(" from plt_taobao_order_tc o");
		sql.append(" where o.dp_id = :dpId and o.created >= :created and o.consign_time >= :consignTime");

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("dpId", dpId);
		paramMap.put("created", created);
		paramMap.put("consignTime", created);

		Integer count = helper.queryForInt(sql.toString(), paramMap);
		return count;
	}

	@Override
	public Integer countOrderFinished(String dpId, String created, String status) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1)");
		sql.append(" from plt_taobao_order_tc o");
		sql.append(" where o.dp_id = :dpId and o.created >= :created and status = :status");

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("dpId", dpId);
		paramMap.put("created", created);
		paramMap.put("endTime", created);
		paramMap.put("status", status);

		Integer count = helper.queryForInt(sql.toString(), paramMap);
		return count;
	}

	@Override
	public Integer countOrderAndTransitstepinfo(String dpId, String created, String status, Integer shippingStatus) {
		// 统计 订单中已签收物流信息的订单总数
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1)");
		sql.append(" from plt_taobao_order_tc o, plt_taobao_transitstepinfo_tc ts");
		sql.append(" where o.tid = ts.tid and o.dp_id = :dpId and o.created >= :created");
		sql.append(" and ts.shipping_status = :shippingStatus");

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("dpId", dpId);
		paramMap.put("created", created);
		paramMap.put("shippingStatus", shippingStatus);
		Integer count = helper.queryForInt(sql.toString(), paramMap);
		
		// 统计 订单已确认 但物流信息未签收的订单总数
		StringBuffer notSql = new StringBuffer();
		notSql.append("select count(1)");
		notSql.append(" from plt_taobao_order_tc o");
		notSql.append(" where not exists ");
		notSql.append(" (select 1 from plt_taobao_transitstepinfo_tc ts where o.tid = ts.tid and ts.shipping_status = :shippingStatus)");
		notSql.append(" and o.dp_id = :dpId and o.created >= :created and o.status = :status");

		Map<String, Object> notParamMap = Maps.newHashMap();
		notParamMap.put("dpId", dpId);
		notParamMap.put("created", created);
		notParamMap.put("status", status);
		notParamMap.put("shippingStatus", shippingStatus);

		Integer notcount = helper.queryForInt(notSql.toString(), notParamMap);
		
		return count + notcount;
	}

	@Override
	public Integer countOrderAndTraderate(String dpId, String created) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1)");
		sql.append(" from plt_taobao_order_tc o,");
		sql.append(" (select tid, role from plt_taobao_traderate group by tid) as tr");
		sql.append(" where o.tid = tr.tid and o.dp_id = :dpId and o.created >= :created");
		sql.append(" and tr.role = 'buyer'");

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("dpId", dpId);
		paramMap.put("created", created);
		Integer count = helper.queryForInt(sql.toString(), paramMap);
		return count;
	}

	@Override
	public Double countOrderPaymentIntervalTime(String dpId, String created) {
		StringBuffer sql = new StringBuffer();
		sql.append("select avg(TIMESTAMPDIFF(MINUTE, o.created, o.pay_time) / 60)").append(" as avg");
		sql.append(" from plt_taobao_order_tc o");
		sql.append(" where o.dp_id = :dpId and o.created >= :created");

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("dpId", dpId);
		paramMap.put("created", created);
		Map<String, Object> map = helper.queryForMap(sql.toString(), paramMap);
		if (null != map.get("avg")) {
			return Double.parseDouble(map.get("avg").toString());
		} else {
			return 0.0;
		}
	}

	@Override
	public Double countOrderSendGoodsIntervalTime(String dpId, String created) {
		StringBuffer sql = new StringBuffer();
		sql.append("select avg(TIMESTAMPDIFF(MINUTE, o.pay_time, o.consign_time) / 60)").append(" as avg");
		sql.append(" from plt_taobao_order_tc o");
		sql.append(" where o.dp_id = :dpId and o.created >= :created");

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("dpId", dpId);
		paramMap.put("created", created);

		Map<String, Object> map = helper.queryForMap(sql.toString(), paramMap);
		if (null != map.get("avg")) {
			return Double.parseDouble(map.get("avg").toString());
		} else {
			return 0.0;
		}
	}

	@Override
	public Double countOrderSignedIntervalTime(String dpId, String created, Integer shippingStatus) {
		StringBuffer sql = new StringBuffer();
		sql.append("select avg(TIMESTAMPDIFF(MINUTE, o.consign_time, ts.signed_time) / 60)").append(" as avg");
		sql.append(" from plt_taobao_order_tc o, plt_taobao_transitstepinfo_tc ts ");
		sql.append(" where o.tid = ts.tid and o.dp_id = :dpId");
		sql.append(" and o.created >= :created and ts.shipping_status = :shippingStatus ");

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("dpId", dpId);
		paramMap.put("created", created);
		paramMap.put("shippingStatus", shippingStatus);
		Map<String, Object> map = helper.queryForMap(sql.toString(), paramMap);
		if (null != map.get("avg")) {
			return Double.parseDouble(map.get("avg").toString());
		} else {
			return 0.0;
		}
	}

	@Override
	public Double countOrderFinishedIntervalTime(String dpId, String created, String status, Integer shippingStatus) {
		StringBuffer sql = new StringBuffer();
		sql.append("select avg(TIMESTAMPDIFF(MINUTE, ts.signed_time, o.endtime) / 60)").append(" as avg");
		sql.append(" from plt_taobao_order_tc o, plt_taobao_transitstepinfo_tc ts ");
		sql.append(" where o.tid = ts.tid and o.dp_id = :dpId");
		sql.append(" and o.created >= :created and o.status = :status and ts.shipping_status = :shippingStatus ");

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("dpId", dpId);
		paramMap.put("created", created);
		paramMap.put("status", status);
		paramMap.put("shippingStatus", shippingStatus);
		Map<String, Object> map = helper.queryForMap(sql.toString(), paramMap);
		if (null != map.get("avg")) {
			return Double.parseDouble(map.get("avg").toString());
		} else {
			return 0.0;
		}
	}

	@Override
	public Double countOrderTraderateIntervalTime(String dpId, String created) {
		StringBuffer sql = new StringBuffer();
		sql.append("select avg(TIMESTAMPDIFF(MINUTE, o.endtime, tr.created) / 60) ").append(" as avg");
		sql.append(" from plt_taobao_order_tc o,");
		sql.append(" (select tid, role, min(created) as created from plt_taobao_traderate group by tid) as tr");
		sql.append(" where o.tid = tr.tid and o.dp_id = :dpId");
		sql.append(" and o.created >= :created and tr.role = 'buyer'");

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("dpId", dpId);
		paramMap.put("created", created);
		Map<String, Object> map = helper.queryForMap(sql.toString(), paramMap);
		if (null != map.get("avg")) {
			return Double.parseDouble(map.get("avg").toString());
		} else {
			return 0.0;
		}
	}

    @Override
    public List<OrderDomain> findLogisticsCareOpenOrders(Map<String, Integer> shopToLastConfigMap) {
/*        String bak = "select o.dp_id as dpId,o.tid as tid from plt_taobao_order_tc o left JOIN plt_taobao_transitstepinfo_tc l on o.tid = l.tid  "
                + "where o.order_status =3 and o.consign_time is not null and ( l.shipping_status !=3 or l.shipping_status is null ) "
                + "and ( l.logistics_status is null or l.logistics_status !=9"*/


        String select = "select o.*";
        String from = " from plt_taobao_order_tc o" ;
        String join = " left JOIN plt_taobao_transitstepinfo_tc l on o.tid = l.tid";
        String condition = " where o.order_status =3 and o.consign_time is not null";
        condition += " and (";
        int i = 0;
        for (Map.Entry<String, Integer> shopLastConfigType : shopToLastConfigMap.entrySet()) {

            String dpId = shopLastConfigType.getKey();
            int careType = shopLastConfigType.getValue();

            if (UserInteractionType.SHIPMENT_CARE.getType().equals(careType)) {
                condition += " ( o.dp_id = "+dpId+" and (l.shipping_status is null)) ";
            } else if (UserInteractionType.ARRIVED_CARE.getType().equals(careType)){
                condition += " ( o.dp_id = "+dpId+" and (l.shipping_status is null or l.shipping_status in (4))) ";
            } else if (UserInteractionType.DELIVERY_CARE.getType().equals(careType)){
                condition += " ( o.dp_id = "+dpId+" and (l.shipping_status is null or l.shipping_status in (4, 1))) ";
            } else {
                condition += " ( o.dp_id = "+dpId+" and (l.shipping_status is null or l.shipping_status in (4,1,2))) ";
            }

            if (i < shopToLastConfigMap.entrySet().size() - 1) {
                condition += " or";
            }

            i++;
        }
        condition += " )";

        String sql = select + from + join + condition;

        logger.info("sql: {}", sql);
        List<OrderDomain> orderDomains = em.createNativeQuery(sql, OrderDomain.class).getResultList();
        return orderDomains;
    }
}
