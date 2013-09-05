/**
 *
 */
package com.yunat.ccms.tradecenter.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.service.BuyerStatisticService;

/**
 * 催付统计展示
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-5-30 下午04:48:26
 */

@Controller
@RequestMapping(value = "/customer/*")
public class BuyerStatisticController {

	private static Logger logger = LoggerFactory.getLogger(BuyerStatisticController.class);

	@Autowired
	private BuyerStatisticService buyerStatisticService;

	@ResponseBody
	@RequestMapping(value = "/autoBuyerStatistic", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ControlerResult urpaySummaryList(String dp_id, String end_date, Integer day_num) {
		final Map<String, Object> result = Maps.newHashMap();
		try {
			Date staticEndDate = new Date();
			if (end_date != null) {
				staticEndDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(end_date);
			}
			int days = 3;
			if (day_num != null) {
				days = day_num;
			}
			buyerStatisticService.staticBuyerInteraction(dp_id, days, staticEndDate);
			return ControlerResult.newSuccess(result);
		} catch (final Exception e) {
			logger.info("买家交互信息统计失败 : [{}]", e);
		}
		result.clear();

		result.put("errordesc", "买家交互信息统计成功!");
		return ControlerResult.newSuccess(result);
	}

}
