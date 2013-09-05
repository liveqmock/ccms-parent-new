package com.yunat.ccms.tradecenter.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.controller.vo.CaringRequest;
import com.yunat.ccms.tradecenter.controller.vo.Pagination;
import com.yunat.ccms.tradecenter.controller.vo.TraderateAutoSetRequest;
import com.yunat.ccms.tradecenter.controller.vo.TraderateVO;
import com.yunat.ccms.tradecenter.domain.SendLogDomain;
import com.yunat.ccms.tradecenter.domain.TraderateDomainPK;
import com.yunat.ccms.tradecenter.service.SendLogService;
import com.yunat.ccms.tradecenter.service.TraderateService;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

/**
 *
 * 评价事务-controller
 *
 * @author tim.yin
 *
 */
@Controller
@RequestMapping(value = "/traderate/*")
public class TraderateController {

	private static Logger logger = LoggerFactory.getLogger(TraderateController.class);

	@Autowired
	private TraderateService traderateService;

	@Autowired
	private SendLogService sendLogService;

	/**
	 * 评价查询
	 *
	 * @param traderateVO
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/query", method = RequestMethod.POST)
	@ResponseBody
	public ControlerResult query(@RequestBody TraderateVO traderateVO) throws Exception {
		// 得到分页数据
		PageRequest page = new PageRequest(traderateVO.getCurrPage() - 1, traderateVO.getPageSize());

		Page<Map<String, Object>> pageList = traderateService.findTraderateByCondition(traderateVO, page);

		Pagination<Map<String, Object>> data = new Pagination<Map<String, Object>>();

		data.setContent(pageList.getContent());
		data.setCurrPage(page.getPageNumber() + 1);
		data.setPageSize(traderateVO.getPageSize());
		data.setTotalPages(pageList.getTotalPages());
		data.setTotalElements(pageList.getTotalElements());
		return ControlerResult.newSuccess(data);
	}

	/**
	 * 评价解释
	 *
	 * @param traderateDomain
	 * @throws Exception
	 */
	@RequestMapping(value = "/{tid}/{oid}/explain", method = RequestMethod.GET)
	@ResponseBody
	public ControlerResult explain(@PathVariable String tid, @PathVariable String oid, @RequestParam String reply,
			@RequestParam String shopId) throws Exception {
		try {
			traderateService.traderateCustomerExplain(new TraderateDomainPK(tid, oid), reply, shopId);
		} catch (Exception e) {
			logger.info("评价解释更新失败 : [exception:]", e);
			return ControlerResult.newError("", e.getMessage());
		}

		return ControlerResult.newSuccess();
	}

	/**
	 * 批量评价解释
	 *
	 */
	@RequestMapping(value = "/explain", method = RequestMethod.POST)
	@ResponseBody
	public ControlerResult batchExplain(@RequestBody List<LinkedHashMap<String, Object>> values) throws Exception {

		try {
			traderateService.traderateBatchCustomerExplain(values);
		} catch (Exception e) {
			logger.info("批量评价解释更新失败 : [exception:]", e);
			return ControlerResult.newError("", e.getMessage());
		}

		return ControlerResult.newSuccess();

	}

	/**
	 * 评价关怀
	 *
	 * @param traderateDomain
	 * @throws Exception
	 */
	@RequestMapping(value = "/regard", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public ControlerResult regard(@RequestBody CaringRequest caringRequest) throws Exception {

		// 短信关怀
		Integer type = Integer.parseInt(caringRequest.getCaringType());
		if ((UserInteractionType.MANUAL_TRADERATE_SMS_CARE.getType() == type)
				|| (UserInteractionType.MANUAL_TRADERATE_WANGWANG_CARE.getType() == type)) {
			if (StringUtils.isEmpty(caringRequest.getContent())) {
				return ControlerResult.newError("关怀内容不能为空!");
			}
			if (caringRequest.getOids() != null) {
				BaseResponse<String> response = traderateService.traderateCustomerRegard(caringRequest);

				if (!response.isSuccess()) {
					return ControlerResult.newError(response.getErrMsg());
				}
			}
		}
		return ControlerResult.newSuccess();

	}

	/**
	 * 批量评价关怀
	 *
	 * @param traderateDomains
	 * @throws Exception
	 */
	@RequestMapping(value = "/batch/regard", method = RequestMethod.POST)
	@ResponseBody
	public ControlerResult batchRegard(@RequestBody CaringRequest caringRequest) throws Exception {
		if(UserInteractionType.MANUAL_TRADERATE_SMS_CARE.getType().toString().equals(caringRequest.getCaringType())){
			if (StringUtils.isEmpty(caringRequest.getContent())) {
				return ControlerResult.newError("关怀不能为空");
			}
		}

		if (caringRequest.getOids() != null) {

			BaseResponse<String> response = traderateService.traderateBatchCustomerRegard(caringRequest);

			if (!response.isSuccess()) {
				return ControlerResult.newError(response.getErrMsg());
			}
		}

		return ControlerResult.newSuccess();

	}

	/**
	 * 关怀详细记录
	 *
	 */
	@RequestMapping(value = "/regard/record", method = RequestMethod.GET)
	@ResponseBody
	public ControlerResult regardRecord(@RequestParam String tid, @RequestParam String oid) throws Exception {

		List<SendLogDomain> resultList = new ArrayList<SendLogDomain>();

		try {
			resultList = sendLogService.getTraderateRegardHistorySendRecord(tid, oid);
		} catch (Exception e) {
			logger.info("获取关怀详细记录失败 : [exception:]", e);
			return ControlerResult.newError("", "获取关怀详细记录失败!");
		}

		return ControlerResult.newSuccess(resultList);

	}

	/**
	 * 自动评价设置
	 *
	 * @param traderateAutoSetRequest
	 * @return
	 */
	@RequestMapping(value = "/autoset", method = RequestMethod.POST)
	@ResponseBody
	public ControlerResult autoSet(@RequestBody TraderateAutoSetRequest traderateAutoSetRequest) {
		try {
			traderateService.traderateAutoSet(traderateAutoSetRequest);
		} catch (Exception e) {
			logger.info("评价事务-自动评价设置保存失败 : [exception:]", e);
			return ControlerResult.newError("", "自动评价设置保存失败!");
		}
		return ControlerResult.newSuccess();
	}

	/**
	 * 获取自动评价设置
	 *
	 * @param dpId
	 *            店铺ID
	 * @return
	 */
	@RequestMapping(value = "/getautoset", method = RequestMethod.GET)
	@ResponseBody
	public ControlerResult getAutoSet(@RequestParam String dpId) {
		try {
			return ControlerResult.newSuccess(traderateService.getTraderateAutoSet(dpId));
		} catch (Exception e) {
			logger.info("评价事务-获取自动评价设置失败 : [exception:]", e);
			return ControlerResult.newError("", "获取自动评价设置失败!");
		}
	}

}
