package com.yunat.ccms.tradecenter.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.yunat.ccms.auth.login.taobao.TaobaoShop;
import com.yunat.ccms.auth.login.taobao.TaobaoShopService;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.controller.vo.UrpayOrderRequest;
import com.yunat.ccms.tradecenter.controller.vo.UrpaySendLogVo;
import com.yunat.ccms.tradecenter.controller.vo.UrpaySummaryListRequest;
import com.yunat.ccms.tradecenter.domain.CareSummaryDomain;
import com.yunat.ccms.tradecenter.domain.UrpaySummaryDomain;
import com.yunat.ccms.tradecenter.repository.CountOrderRepository;
import com.yunat.ccms.tradecenter.service.UrpaySummaryService;
import com.yunat.ccms.tradecenter.support.cons.SysType;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.ccms.tradecenter.util.FileDownLoadUtil;
import com.yunat.ccms.tradecenter.util.SortUtil;

/**
 * 催付统计展示
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-5-30 下午04:48:26
 */

@Controller
@RequestMapping(value = "/urpay/urpaySummary/*")
public class UrpaySummaryController {

	private static Logger logger = LoggerFactory.getLogger(UrpaySummaryController.class);

	@Autowired
	private UrpaySummaryService urpaySummaryService;

	@Autowired
	private CountOrderRepository countOrderRepository;

	@Autowired
	private TaobaoShopService taobaoShopService;


	/**
	 * 发送记录列表展示
	 * @param urpay
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/urpayLogList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ControlerResult urpayLogList(@ModelAttribute final UrpayOrderRequest urpay){
		urpay.setSysType(SysType.ORDERCENTER);// 设置查询订单中心的记录
		PageRequest page = new PageRequest(urpay.getCurrPage() - 1, urpay.getPageSize());
		Page<Map<String, Object>> listPage = countOrderRepository.getSendLogByType(page, urpay);
		UrpaySendLogVo voData = new UrpaySendLogVo();
		voData.setContent(listPage.getContent());
		voData.setCurrPage(urpay.getCurrPage());
		voData.setPageSize(urpay.getPageSize());
		voData.setTotalPages(listPage.getTotalPages());
		voData.setTotalElements(listPage.getTotalElements());
		return ControlerResult.newSuccess(voData);
	}


	/**
	 * 发送记录
	 * @param urpay
	 * @param resp
	 */
	@RequestMapping(value = "/downloadUrpayLog", method = RequestMethod.GET)
	public void downloadUrpayLog(@ModelAttribute final UrpayOrderRequest urpay, HttpServletResponse resp){
		try {
			urpay.setSysType(SysType.ORDERCENTER);// 设置查询订单中心的记录
			String filename = getFileName(urpay.getType(), urpay.getDpId());
			FileDownLoadUtil.setHeader(resp, filename);
			countOrderRepository.getSendLogByTypeAlls(urpay, resp.getWriter());
		} catch (IOException e) {
			logger.info("导出发送记录异常 : [{}]", e);
		}
	}


	/**
	 * 发送类型接口
	 * @param urpay
	 * @param resp
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/urpayType", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ControlerResult urpayType(){
		return ControlerResult.newSuccess(UserInteractionType.getTypeMsgListBySysTypeShow(SysType.ORDERCENTER, true));
	}

	@ResponseBody
	@RequestMapping(value = "/urpaySummaryList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ControlerResult urpaySummaryList(@ModelAttribute final UrpaySummaryListRequest progRequest) {
		final Map<String, Object> result = Maps.newHashMap();
		try {
			if (progRequest.getUrpayType() != null && progRequest.getDpId() != null) {
				Map<String, UrpaySummaryDomain> map = new HashMap<String, UrpaySummaryDomain>();
				for (int i = 0; i > -30; i--) {
					Date date30 = DateUtils.addDay(new Date(), i);
					String key = DateUtils.getString(date30);
					UrpaySummaryDomain domain = new UrpaySummaryDomain();
					domain.setDpId(progRequest.getDpId());
					domain.setUrpayType(progRequest.getUrpayType());
					domain.setOrderNum(0);
					domain.setResponseNum(0);
					domain.setResponseAmount(0.0);
					domain.setSendNum(0);
					domain.setCreated(new Date());
					domain.setUpdated(new Date());
					domain.setUrpayDate(key);
					map.put(key, domain);
				}
				List<UrpaySummaryDomain> urpaySummaryListTmp = urpaySummaryService.queryUrpaySummaryList(
						progRequest.getUrpayType(), progRequest.getDpId());
				for (UrpaySummaryDomain summary : urpaySummaryListTmp) {
					map.put(summary.getUrpayDate().trim(), summary);
				}
				Collection<UrpaySummaryDomain> coll = map.values();
				List<UrpaySummaryDomain> urpaySummaryList = new ArrayList<UrpaySummaryDomain>();
				urpaySummaryList.addAll(coll);
				SortUtil.stockDataListSort(urpaySummaryList, "urpayDate", "desc");
				result.put("urpaySummaryList", urpaySummaryList);
			} else {
				return ControlerResult.newError("参数缺失");
			}
			return ControlerResult.newSuccess(result);
		} catch (final Exception e) {
			logger.info("显示催付统计异常 : [{}]", e);
		}
		result.clear();
		result.put("errordesc", "显示催付统计失败!");
		return ControlerResult.newSuccess(result);
	}

	@RequestMapping(value = "/downloadUrpaySummary", method = RequestMethod.GET)
	public void downloadUrpaySummary(@ModelAttribute final UrpaySummaryListRequest progRequest, HttpServletResponse resp) {
		try {
			if (progRequest.getUrpayType() != null && progRequest.getDpId() != null) {
				Map<String, UrpaySummaryDomain> map = new HashMap<String, UrpaySummaryDomain>();
				for (int i = 0; i > -30; i--) {
					Date date30 = DateUtils.addDay(new Date(), i);
					String key = DateUtils.getString(date30);
					UrpaySummaryDomain domain = new UrpaySummaryDomain();
					domain.setDpId(progRequest.getDpId());
					domain.setUrpayType(progRequest.getUrpayType());
					domain.setOrderNum(0);
					domain.setResponseNum(0);
					domain.setResponseAmount(0.0);
					domain.setSendNum(0);
					domain.setCreated(new Date());
					domain.setUpdated(new Date());
					domain.setUrpayDate(key);
					map.put(key, domain);
				}
				List<UrpaySummaryDomain> urpaySummaryListTmp = urpaySummaryService.queryUrpaySummaryList(
						progRequest.getUrpayType(), progRequest.getDpId());
				for (UrpaySummaryDomain summary : urpaySummaryListTmp) {
					map.put(summary.getUrpayDate().trim(), summary);
				}
				Collection<UrpaySummaryDomain> coll = map.values();
				List<UrpaySummaryDomain> urpaySummaryList = new ArrayList<UrpaySummaryDomain>();
				urpaySummaryList.addAll(coll);
				SortUtil.stockDataListSort(urpaySummaryList, "urpayDate", "desc");

				String filename = getFileName(progRequest.getUrpayType(),progRequest.getDpId());
				StringBuffer sb = new StringBuffer();
				if (urpaySummaryList != null && urpaySummaryList.size() > 0) {
					sb.append("日期,催付订单数,催付响应数,响应率,短信量,催付响应金额,ROI\n");
					for (UrpaySummaryDomain bean : urpaySummaryList) {
						Integer orderNum = 0;
						if (bean.getOrderNum() != null) {
							orderNum = bean.getOrderNum();
						}

						Integer responseNum = 0;
						if (bean.getResponseNum() != null) {
							responseNum = bean.getResponseNum();
						}

						Integer sendNum = 0;
						if (bean.getSendNum() != null) {
							sendNum = bean.getSendNum();
						}

						Double responseAmount = 0.0;
						if (bean.getResponseAmount() != null) {
							responseAmount = bean.getResponseAmount();
						}
						DecimalFormat df1 = new DecimalFormat("####.00");
						DecimalFormat df2 = new DecimalFormat("####");

						String ROI = "0";
						if (sendNum != 0) {
							double roi = (double) responseAmount.doubleValue() / (sendNum.doubleValue() * 0.05);
							ROI = df2.format(roi);
						}

						String responseT = "0";
						if (orderNum != 0) {
							double d = (100 * responseNum.doubleValue() / orderNum.doubleValue());
							if (d < 100) {
								responseT = df1.format(new Double("" + d));
							} else {
								responseT = "100";
							}

						}
						sb.append(bean.getUrpayDate() + "," + orderNum + "," + responseNum + "," + responseT + "%,"
								+ sendNum + "," + responseAmount + ",1:" + ROI + "\n");
					}
				}
				FileDownLoadUtil.writer(resp, filename, sb.toString());
			} else {
			}
		} catch (final Exception e) {
			logger.info("导出催付统计异常 : [{}]", e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/careSummaryList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ControlerResult careSummaryList(@ModelAttribute final UrpaySummaryListRequest progRequest) {
		final Map<String, Object> result = Maps.newHashMap();
		try {
			if (progRequest.getUrpayType() != null && progRequest.getDpId() != null) {
				Map<String, CareSummaryDomain> map = new HashMap<String, CareSummaryDomain>();
				for (int i = 0; i > -30; i--) {
					Date date30 = DateUtils.addDay(new Date(), i);
					String key = DateUtils.getString(date30);
					CareSummaryDomain domain = new CareSummaryDomain();
					domain.setDpId(progRequest.getDpId());
					domain.setUrpayType(progRequest.getUrpayType());
					domain.setSendNum(0);
					domain.setUrpayDate(key);
					map.put(key, domain);
				}
				List<Map<String, Object>> careSummaryListTmp = countOrderRepository.countAllUrpaySmsNum(
						progRequest.getUrpayType(), progRequest.getDpId());
				for (Map<String, Object> summary : careSummaryListTmp) {
					if (summary.get("urpayDate") != null && summary.get("num") != null) {
						map.get(summary.get("urpayDate").toString()).setSendNum(
								new Integer(summary.get("num").toString()));
					}
				}
				Collection<CareSummaryDomain> coll = map.values();
				List<CareSummaryDomain> careSummaryList = new ArrayList<CareSummaryDomain>();
				careSummaryList.addAll(coll);
				SortUtil.stockDataListSort(careSummaryList, "urpayDate", "desc");
				result.put("careSummaryList", careSummaryList);
			} else {
				return ControlerResult.newError("参数缺失");
			}
			return ControlerResult.newSuccess(result);
		} catch (final Exception e) {
			logger.info("显示关怀统计异常 : [{}]", e);
		}
		result.clear();
		result.put("errordesc", "显示关怀统计失败!");
		return ControlerResult.newSuccess(result);
	}

	@ResponseBody
	@RequestMapping(value = "/downloadCareSummary", method = RequestMethod.GET)
	public void downloadCareSummary(@ModelAttribute final UrpaySummaryListRequest progRequest, HttpServletResponse resp) {
		try {
			if (progRequest.getUrpayType() != null && progRequest.getDpId() != null) {
				Map<String, CareSummaryDomain> map = new HashMap<String, CareSummaryDomain>();
				for (int i = 0; i > -30; i--) {
					Date date30 = DateUtils.addDay(new Date(), i);
					String key = DateUtils.getString(date30);
					CareSummaryDomain domain = new CareSummaryDomain();
					domain.setDpId(progRequest.getDpId());
					domain.setUrpayType(progRequest.getUrpayType());
					domain.setSendNum(0);
					domain.setUrpayDate(key);
					map.put(key, domain);
				}
				List<Map<String, Object>> careSummaryListTmp = countOrderRepository.countAllUrpaySmsNum(
						progRequest.getUrpayType(), progRequest.getDpId());
				for (Map<String, Object> summary : careSummaryListTmp) {
					if (summary.get("urpayDate") != null && summary.get("num") != null) {
						map.get(summary.get("urpayDate").toString()).setSendNum(
								new Integer(summary.get("num").toString()));
					}
				}
				Collection<CareSummaryDomain> coll = map.values();
				List<CareSummaryDomain> careSummaryList = new ArrayList<CareSummaryDomain>();
				careSummaryList.addAll(coll);
				SortUtil.stockDataListSort(careSummaryList, "urpayDate", "desc");

				String filename = getFileName(progRequest.getUrpayType(),progRequest.getDpId());
				StringBuffer sb = new StringBuffer();
				if (careSummaryList != null && careSummaryList.size() > 0) {
					sb.append("日期,短信发送量\n");
					for (CareSummaryDomain bean : careSummaryList) {
						sb.append(bean.getUrpayDate() + "," + bean.getSendNum() + "\n");
					}
				}
				FileDownLoadUtil.writer(resp, filename, sb.toString());
			}
		} catch (final Exception e) {
			logger.info("导出关怀统计异常 : [{}]", e);
		}
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
}
