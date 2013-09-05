package com.yunat.ccms.tradecenter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.yunat.base.util.JackSonMapper;
import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.auth.login.taobao.TaobaoShop;
import com.yunat.ccms.auth.login.taobao.TaobaoShopService;
import com.yunat.ccms.tradecenter.controller.vo.UrPayOrdersLogVo;
import com.yunat.ccms.tradecenter.controller.vo.UrpayOrderRequest;
import com.yunat.ccms.tradecenter.domain.CustomerOrdersShipDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.repository.CountOrderRepository;
import com.yunat.ccms.tradecenter.repository.CustomerOrdersShipRepository;
import com.yunat.ccms.tradecenter.repository.UrpayOrderRespository;
import com.yunat.ccms.tradecenter.service.CustomerOrdersService;
import com.yunat.ccms.tradecenter.service.LogisticsService;
import com.yunat.ccms.tradecenter.service.OrderService;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

/**
 *
 *
 * @author ming.peng
 * @date 2013-6-3
 * @since 4.1.0
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
//		"classpath:config/spring/applicationContext.xml"
//})
//@TransactionConfiguration(defaultRollback = false)
public class CustomerOrdersShipServiceTest
 extends AbstractJunit4SpringContextBaseTests
{

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CustomerOrdersService orders;

	@Autowired
	private CustomerOrdersShipRepository shipRepository;

	@Autowired
	private OrderService orderService;


	@Autowired
	UrpayOrderRespository re;


	@Autowired
	private CountOrderRepository countOrderRepository;

	@Autowired
	private TaobaoShopService taobaoShopService;

	@Autowired
	LogisticsService logisticsService;

	@Test
	public void testUpdateTimeoutActionTimeByTids(){
		String count = getFileName(null, "100571094");
		System.out.println(count);

		int counts = logisticsService.updateTimeoutByTid(3l, "1000000000000001", "1000000000000002");
		System.out.println(counts);
	}


	/**
	 * 拼接文件名
	 * @param urpayType
	 * @return "shop_nick"_"page_type_name"__"YYYYMMDD"
	 */
	private String getFileName(Integer urpayType,String dpId){
		StringBuilder fileName = new StringBuilder();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		String orderDate = format.format(new Date());
		TaobaoShop taobaoShop = taobaoShopService.get(dpId);
		fileName.append(taobaoShop.getShopName()).append("_");
		fileName.append(UserInteractionType.getMessage(urpayType)).append("_");
		fileName.append(orderDate);
		return fileName.toString();
	}


	private Object[] construct(Map<String, String> columnNameMap, Map<String, Object> paramMap, StringBuffer sql, UrpayOrderRequest request){
		// 列名对应的中文名称
		columnNameMap.put("created", "发送时间");
		columnNameMap.put("mobile", "手机号码");
		columnNameMap.put("type", "发送类型");
		columnNameMap.put("buyerNick", "淘宝昵称");
		columnNameMap.put("tid", "订单编号");
		columnNameMap.put("smsContent", "短信内容");
		// 查询 sql
		sql.append("select from tb_tc_send_log ").append("where dp_id = :dpId ");
		// 查询参数
		paramMap.put("dpId", request.getDpId());
		if (ObjectUtils.notEqual(request.getType(), null)){
			sql.append("and type = :type ");
			paramMap.put("type", request.getType());
		}
		if (ObjectUtils.notEqual(request.getStartCreated(), null) && ObjectUtils.notEqual(request.getEndCreated(), null)){
			sql.append("and (created between :startCreated and :endCreated) ");
			paramMap.put("startCreated", new Date(request.getStartCreated()));
			paramMap.put("endCreated", new Date(request.getEndCreated()));
		}
		if (StringUtils.isNotEmpty(request.getMobileOrNickOrTid())){
			sql.append("and (mobile = :fields or buyer_nick = :fields or tid = :fields) ");
			paramMap.put("fields", request.getMobileOrNickOrTid());
		}
		String columns = " date_format(created, ''%Y-%m-%d %H:%i:%s'') as {0}, mobile as {1}, type as {2}, buyer_nick as {3}, tid as {4}, sms_content as {5}";
		Object[] columnName = columnNameMap.keySet().toArray(new Object[0]);
		columns = MessageFormat.format(columns, columnName);
		sql.insert(6, columns);
		return columnName;
	}

	@Test
	public void test() throws ParseException, FileNotFoundException{
		UrpayOrderRequest urpay = new UrpayOrderRequest();
		urpay.setDpId("65927470");
//		urpay.setStartCreated(DateUtils.parseDate("2013-06-23", "yyyy-MM-dd").getTime());
//		urpay.setEndCreated(DateUtils.parseDate("2013-06-24", "yyyy-MM-dd").getTime());

		StringBuffer sql = new StringBuffer();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		final Map<String, String> columnNameMap = new LinkedHashMap<String, String>(6);
		final Object[] columnName = construct(columnNameMap, paramMap, sql, urpay);
		System.out.println(columnName);
		System.out.println(sql);
	}

	@Test
	public void testSendLogAll() throws ParseException, FileNotFoundException{
		UrpayOrderRequest urpay = new UrpayOrderRequest();
		urpay.setDpId("65927470");
//		urpay.setStartCreated(DateUtils.parseDate("2013-06-23", "yyyy-MM-dd").getTime());
//		urpay.setEndCreated(DateUtils.parseDate("2013-06-24", "yyyy-MM-dd").getTime());
//		System.out.println(JackSonMapper.toJsonString(urpay));
		PrintWriter pw = new PrintWriter("f:/a.txt");
		countOrderRepository.getSendLogByTypeAlls(urpay, pw);
//
//		for (SendLogDomain sendLogDomain : listPage) {
//			System.out.println(JackSonMapper.toJsonString(sendLogDomain));
//		}
	}

	@Test
	public void testUpdateCusOrdsShipHide(){
		List<String> list = new ArrayList<String>();
		list.add("100000000000001");
		list.add("100000000000002");
		int [] a = orders.updateCusOrdsShipHide(false, "is_hide", list.toArray(new String[0]));
		System.out.println(a);
	}

	@Test
	public void testShippingNotice(){
		List<OrderDomain> list = null; // re.getOrders("68790050");

		System.out.println(JackSonMapper.toJsonString(list));

		Map<String, List<OrderDomain>> map = new HashMap<String, List<OrderDomain>>();
		map.put("68790050", list);
		orderService.shippingNotice(map);


	}

	@Test
	public void testUrPayOrdersLogList(){
		PageRequest pr = new PageRequest(0, 20);
		UrPayOrdersLogVo  ship = orders.urPayOrdersLogList(pr, "65927470");
		System.out.println(JackSonMapper.toJsonString(ship));

//		ship = orders.urPayOrdersLogList(pr, "b");
//		System.out.println(JackSonMapper.toJsonString(ship));
	}

	@Test
	public void testGetWwListByDpId(){
//		List<Map<String, Object>>  ship = shipRepository.getWwListByDpId("a");
//		List<String>  ship = shipRepository.getWwListByDpId("a");
		List<Map<String, Object>>  ship = orders.getOrderReceptionWwListByDpId("a");
		System.out.println(JackSonMapper.toJsonString(ship));
	}

	@Test
	public void testFindCusOrdsByTid(){
//		shipRepository.updateCusOrdsByTid("a");
//		CustomerOrdersShipDomain ship = shipRepository.findCusOrdsByTid("b");
//		System.out.println(JackSonMapper.toJsonString(ship));
//
//		int[] updas = orders.updateCusOrdsShipHide(true, "b");
//		ship = shipRepository.findCusOrdsByTid("b");
//		System.out.println(JackSonMapper.toJsonString(ship));
//
//
//		int upda = orders.updateCusOrdsShipHide("b");
//		System.out.println(JackSonMapper.toJsonString(upda));
////		shipRepository.updateCusOrdsByTid("a");
//
//		ship = shipRepository.findCusOrdsByTid("b");
//		System.out.println(JackSonMapper.toJsonString(ship));
		List<String> list = new ArrayList<String>();
		list.add("100000000000001");
		list.add("100000000000002");

		List<CustomerOrdersShipDomain> l = shipRepository.getByTidIn(list);
		System.out.println(JackSonMapper.toJsonString(l));

	}

	@Test
	public void testSaveCustomerOrdersShipData(){
		orders.saveCustomerOrdersShipData();
	}

	@Test
	public void testGetCustomerOrdersShipData(){
		List<Map<String, Object>> objs = orders.getCustomerOrdersShipData();
		for (Map<String, Object> objects : objs) {
			logger.info(JackSonMapper.toJsonString(objects));
		}
	}


}
