package com.yunat.ccms.tradecenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.controller.vo.SendGoodsQueryRequest;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.UrpayStatusDomain;
import com.yunat.ccms.tradecenter.repository.BatchRepository;
import com.yunat.ccms.tradecenter.repository.OrderRepository;
import com.yunat.ccms.tradecenter.repository.UrpayOrderRespository;
import com.yunat.ccms.tradecenter.repository.UrpayStatusRepository;
import com.yunat.ccms.tradecenter.service.OrderService;

public class OrderRepositoryTest extends AbstractJunit4SpringContextBaseTests{

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UrpayOrderRespository urpayOrderRespository;

	@Autowired
	BatchRepository batchRepository;

	@Autowired
	UrpayStatusRepository urpayStatusRepository;

	@Autowired
	private OrderService orderService;

	@Test
	public void testGetByCreated() {
		Date startDate = new Date();
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2011-09-16 00:00:57");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date endDate = DateUtils.addDay(startDate, 376);

		String dpId = "123456";
		List<OrderDomain> orderDomains = orderRepository.findByModifiedBetween(startDate, endDate, dpId);

		System.out.println(orderDomains.size());
	}

	@Test
	public void findPayedOrder(){
	    //当前时间的前一天
        Date preDay = DateUtils.addDay(new Date(), -1);
        //转化为yyyy-MM-dd格式字符串
        String strDay = DateUtils.getString(preDay) + " 00:00:00";
        //转化为yyyy-MM-dd HH:mm:ss 格式日期
        Date startDate = DateUtils.getDateTime(strDay);
	    List<OrderDomain> list = orderRepository.findPayedOrder("123", "test",startDate,new Date());
	    if(list != null){
	        System.out.println(list.size());
	    }
	}

	@Test
	public void getNotPayedAndNotUrpayedOrders(){
	    List<OrderDomain> list = urpayOrderRespository.getNotPayedAndNotUrpayedOrders("dd");
	    if(list != null){
            System.out.println(list.size());
        }
	}

	@Test
	public void saveOrder(){
	    List<OrderDomain> list = new ArrayList<OrderDomain>();
	    for(int i=1200000;i<1200010;i++){
	        OrderDomain d = new OrderDomain();
	        d.setBuyerMessage("");
	        d.setConsignTime(new Date());
	        d.setCreated(DateUtils.getDateTime("2013-06-18 10:23:33"));
	        d.setCustomerno("c" + i);
	        d.setDpId("65927472");
	        d.setEndtime(null);
	        d.setModified(DateUtils.getDateTime("2013-06-18 10:23:33"));
	        d.setNum(1);
	        d.setOrderStatus(1);
	        d.setPayment(199.00);
	        d.setPayTime(null);
	        d.setPostFee(10.00);
	        d.setReceiverAddress("");
	        d.setReceiverCity("");
	        d.setReceiverDistrict("");
	        d.setReceiverMobile("");//手机号码
	        d.setReceiverName("test");
	        d.setReceiverPhone("");
	        d.setReceiverState("");
	        d.setReceiverZip("");
	        d.setSellerFlag(1);
	        d.setSellerMemo("");
	        d.setShippingType("");
	        d.setStatus("WAIT_BUYER_PAY");
	        d.setOrderStatus(1);
	        d.setTid(String.valueOf(i));
	        d.setTradeFrom("TAOBAO");
	        d.setOrderCreated(new Date());
	        d.setOrderUpdated(new Date());
	        list.add(d);
	        System.out.println(String.valueOf(i));
	    }
	    batchRepository.batchInsert(list);
	    //orderRepository.save(list);
	}


	@Test
    public void saveStatus(){
        List<UrpayStatusDomain> list = new ArrayList<UrpayStatusDomain>();
        for(int i=0;i<100000;i++){
            UrpayStatusDomain d = new UrpayStatusDomain();
            d.setAutoUrpayStatus(0);
            d.setAutoUrpayThread("20130620");
            d.setTid(String.valueOf(i));
            list.add(d);
            System.out.println(String.valueOf(i));
        }
        urpayStatusRepository.insertUrpayStatusBatch(list, "1");
        //orderRepository.save(list);
    }

	@Test
	public void querySendGoodsOrders(){
	    SendGoodsQueryRequest sendGoodsQuery = new SendGoodsQueryRequest();
	    sendGoodsQuery.setCareStatus(0);
	    sendGoodsQuery.setCurrentPage(1);
	    sendGoodsQuery.setDpId("65927470");
	    sendGoodsQuery.setOrderSort("payTime_asc");
	    //sendGoodsQuery.setCustomerno("miaomiaozhu0");
	    sendGoodsQuery.setIsHide(0);
	    sendGoodsQuery.setPageSize(10);
	    sendGoodsQuery.setReceiverState("上海市");
	    //sendGoodsQuery.setTid("12345678");
	    sendGoodsQuery.setTitle("favority");
	    sendGoodsQuery.setWaitDay(2);
	    orderService.querySendGoodsOrders(sendGoodsQuery);

	    //orderService.querySendGoodsOrdersCount(sendGoodsQuery);

	}

	@Test
	public void test(){
	    //付款时间
        Date payTime = DateUtils.getDateTime("2013-07-07 12:00:00");
        //当前时间
        Date curTime = new Date();
        //两个时间之差
        long time = Math.abs(curTime.getTime() - payTime.getTime());
        //转化为小时
        final int hour = (int) (time / (1000 * 3600));
        //分钟
        final int m = (int)((time - hour * 1000 * 3600) / (1000 * 60));
	}

}
