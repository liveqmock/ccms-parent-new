package com.yunat.ccms.tradecenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.tradecenter.domain.BuyerInteractionStatisticDomain;
import com.yunat.ccms.tradecenter.repository.BuyerInteractionStatisticRepository;
import com.yunat.ccms.tradecenter.service.BuyerStatisticService;

public class BuyerInteractionStatisticDomainTest  extends AbstractJunit4SpringContextBaseTests{

	@Autowired
	private BuyerInteractionStatisticRepository buyerInteractionStatisticRepository;

	@Autowired
	private BuyerStatisticService buyerStatisticService;

	/**
	 * jpa测试 测试保存
	 */
	@Test
	public void testSave() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		BuyerInteractionStatisticDomain buyerInteractionStatisticDomain = new BuyerInteractionStatisticDomain();
		buyerInteractionStatisticDomain.setCreated(calendar.getTime());
		buyerInteractionStatisticDomain.setCustomerno("liweilin456");
		buyerInteractionStatisticDomain.setDealDate(calendar.getTime());
		buyerInteractionStatisticDomain.setDpId("123456789");
		buyerInteractionStatisticDomain.setTradeCloseCount(30);
		buyerInteractionStatisticDomain.setTradeCount(50);
		buyerInteractionStatisticDomain.setTradeNoPayedCount(10);
		buyerInteractionStatisticDomain.setTradePayedCount(10);
		buyerInteractionStatisticDomain.setUpdated(calendar.getTime());
		buyerInteractionStatisticDomain.setUrpayCount(3);

		buyerInteractionStatisticRepository.save(buyerInteractionStatisticDomain);
	}

	/**
	 * 测试获取
	 */
	@Test
	public void testGet() {

		BuyerInteractionStatisticDomain buyerInteractionStatisticDomain = buyerInteractionStatisticRepository.findOne(1l);

		System.out.println(buyerInteractionStatisticDomain.getCustomerno());
	}

	@Test
	public void testFindByDealDateBetween() {
		Date today = new Date();
		Date startDate = DateUtils.addDays(today, -3);
		List<BuyerInteractionStatisticDomain> buyerInteractionStatisticDomains = buyerInteractionStatisticRepository.getByDealDateBetween(startDate, today);

		System.out.println(buyerInteractionStatisticDomains);

	}

	@Test
	public void testGetByDealDateAndDpId() {
		Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		Date startDate = DateUtils.addDays(today, -1);
		List<BuyerInteractionStatisticDomain> buyerInteractionStatisticDomains = buyerInteractionStatisticRepository.getByDealDateAndDpId(startDate, "123456789");

		System.out.println(buyerInteractionStatisticDomains);

	}

	@Test
	public void testStatic() {

		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2011-09-22 00:21:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		buyerStatisticService.staticBuyerInteraction("144939", 3, date);
	}
}
