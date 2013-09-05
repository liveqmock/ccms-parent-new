package com.yunat.ccms.tradecenter.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taobao.api.response.TradeFullinfoGetResponse;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.controller.vo.AffairsVO;
import com.yunat.ccms.tradecenter.controller.vo.TradeMemoAddInterRequest;
import com.yunat.ccms.tradecenter.domain.AffairDomain;
import com.yunat.ccms.tradecenter.domain.AffairsHandleDomain;
import com.yunat.ccms.tradecenter.domain.CustomerDomain;
import com.yunat.ccms.tradecenter.service.AffairService;
import com.yunat.ccms.tradecenter.service.AffairsHandleService;
import com.yunat.ccms.tradecenter.service.CustomerService;
import com.yunat.ccms.tradecenter.service.queryobject.AffairsQuery;
import com.yunat.ccms.tradecenter.support.cons.AffairCons;
import com.yunat.ccms.tradecenter.support.cons.OrderStatus;
import com.yunat.ccms.tradecenter.support.cons.RefundStatus;
import com.yunat.ccms.tradecenter.support.cons.ShippingStatus;
import com.yunat.ccms.tradecenter.support.taobaoapi.TaobaoTradeMemoManager;
import com.yunat.ccms.tradecenter.util.BeanUtil;

@Controller
@RequestMapping(value = "/affair/*")
public class AffairController implements AffairCons{

	private static Logger logger = LoggerFactory.getLogger(AffairController.class);

	@Autowired
	private AffairService affairService;

    @Autowired
    private AffairsHandleService affairsHandleService;

    @Autowired
	private CustomerService customerService;

    @Autowired
    private TaobaoTradeMemoManager taobaoTradeMemoManager;

	/**
	 * 新增事务
	 * @param affairDomain
	 * @return
	 * @throws Exception
	 * http://wiki.yunat.com/pages/viewpage.action?pageId=18779672
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public ControlerResult addAffair(@RequestBody AffairDomain affairDomain) throws Exception {
		try {
			affairService.addAffair(affairDomain);
		} catch (Exception e) {
			logger.error("新增事务失败： ", e);
			return ControlerResult.newError("", e.getMessage());
		}

		return ControlerResult.newSuccess();
	}

	/**
	 * 修改事务
	 * @param affairDomain
	 * @return
	 * @throws Exception
	 * http://wiki.yunat.com/pages/viewpage.action?pageId=18779674
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public ControlerResult modifyAffair(@RequestBody AffairDomain affairDomain) throws Exception {
		try {
			affairService.modifyAffair(affairDomain);
		} catch (Exception e) {
			logger.error("修改事务失败： ", e);
			return ControlerResult.newError("", e.getMessage());
		}

		return ControlerResult.newSuccess();
	}

    /**
     * 跟进事务
     * @param status
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/handleAffairs", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ControlerResult handleAffairs(@RequestBody AffairsHandleDomain affairsHandleDomain, int status) {

        affairsHandleService.handleAffairs(affairsHandleDomain.getNote(), affairsHandleDomain.getNextHandler(), status, affairsHandleDomain.getAffairsId());

        return ControlerResult.newSuccess();
    }

    /**
     * 事务列表查询
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findAffairs", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ControlerResult findAffairs(@RequestBody AffairsQuery affairsQuery) {
        List<AffairsVO> affairsVOList = affairService.findAffairs(affairsQuery);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", affairsQuery.getTotalItem());
        resultMap.put("page", affairsQuery.getCurrentPage());
        resultMap.put("totalPage", affairsQuery.getTotalPage());
        resultMap.put("pageSize", affairsQuery.getPageSize());
        resultMap.put("data", affairsVOList);
        return ControlerResult.newSuccess(resultMap);
    }

    /**
     * 事务明细查询
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findAffairDetail", method = RequestMethod.GET)
    public ControlerResult findAffairDetail(Long affair_id) {

    	AffairDomain affairDomain = affairService.findAffair(affair_id);
    	Map<String, Object> affairInfo = genAffairInfo(affairDomain);

    	CustomerDomain cd = customerService.findByCustomerno(affairDomain.getCustomerno());
    	Map<String, Object> customerInfo = genCustomerInfo(cd);

    	Map<String, Object> order = affairService.findOrderInfoByTid(affairDomain.getTid());
    	Map<String, Object> tidInfo = genTidInfo(order);
    	List<Map<String, Object>> orderItemsInfo = affairService.findOrderItemsInfoByTid(affairDomain.getTid());
    	List<Map<String, Object>> oidInfo = genOidInfo(orderItemsInfo, affairDomain);

    	List<AffairsHandleDomain> affairsHandleList = affairsHandleService.findAffairHandles(affair_id);
    	List<Map<String, Object>> affairsHandleListInfo = genAffairsHandleListInfo(affairsHandleList);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("affairDetail", affairInfo);
        resultMap.put("userInfo", customerInfo);
        resultMap.put("tidInfo", tidInfo);
		resultMap.put("oidInfo", oidInfo );
        resultMap.put("affairHandles", affairsHandleListInfo);

        return ControlerResult.newSuccess(resultMap);
    }

	private List<Map<String, Object>> genAffairsHandleListInfo(List<AffairsHandleDomain> affairsHandleList) {
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
		if(affairsHandleList == null){
		}else{
			for(AffairsHandleDomain affairsHandle : affairsHandleList){
				Map<String, Object> rtnMap = new HashMap<String, Object>();
				rtnMap = BeanUtil.beanToMap(affairsHandle);
				rtnMap.put("created", DateUtils.getStringDate(affairsHandle.getCreated()));
				rtnList.add(rtnMap);
			}
		}
		return rtnList;
	}

	/**
	 * 向原子订单属性集里，添加链接
	 * @param orderItemsInfo
	 * @param affairDomain
	 * @return
	 */
	private List<Map<String, Object>> genOidInfo(List<Map<String, Object>> orderItemsInfo, AffairDomain affairDomain) {
		List<Map<String, Object>> rtnList = genOidInfo(orderItemsInfo);
		for(Map<String, Object> rtnMap : rtnList){
			int sourceId = affairDomain.getSourceId().intValue();
			switch (sourceId) {
			case 2://依次为未付款、发货、物流、评价
			case 3:
			case 4:
			case 6:
				rtnMap.put("titleUrl", "http://detail.tmall.com/item.htm?id="+ rtnMap.get("num_iid"));
				rtnMap.put("picUrl", "http://detail.tmall.com/item.htm?id="+ rtnMap.get("num_iid"));
				break;
			case 5: //退款
				rtnMap.put("titleUrl", "http://tradearchive.taobao.com/trade/detail/trade_item_detail.htm?biz_order_id="+ rtnMap.get("tid"));
				rtnMap.put("picUrl", "http://detail.tmall.com/item.htm?id="+ rtnMap.get("num_iid"));
				break;
			default:
				break;
			}


		}

		return rtnList;
	}

	/**
	 *对退款状态和评价做处理
	 * @param orderItemsInfo
	 * @return
	 */
	private List<Map<String, Object>> genOidInfo(List<Map<String, Object>> orderItemsInfo) {
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
		if(orderItemsInfo != null && !orderItemsInfo.isEmpty()){
			for(Map<String, Object> orderItemInfo : orderItemsInfo){
				Map<String, Object> rtnMap = new HashMap<String, Object>();
				rtnMap = new HashMap<String, Object>(orderItemInfo);
				if(orderItemInfo.get("status") == null){
					rtnMap.put("status", "");
				}else{
					rtnMap.put("status", OrderStatus.getMessage(orderItemInfo.get("status").toString()));
				}
				if(orderItemInfo.get("refund_status") == null){
					rtnMap.put("refundStatus", "");
				}else{
					rtnMap.put("refundStatus", RefundStatus.getMessage(orderItemInfo.get("refund_status").toString()));
				}
				if(orderItemInfo.get("result") == null){
					rtnMap.put("result", "");
				}else{
					rtnMap.put("result", orderItemInfo.get("result").toString());
				}
				rtnMap.put("picPath", orderItemInfo.get("pic_path"));
				rtnList.add(rtnMap);
			}
		}
		return rtnList;
	}

	/**
	 * 对订单状态和物流详细状态做处理
	 * @param order
	 * @return
	 */
	private Map<String, Object> genTidInfo(Map<String, Object> order) {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		if(order == null){
			return rtnMap;
		}else{
			rtnMap = new HashMap<String, Object>(order);

			TradeFullinfoGetResponse tradeRes = taobaoTradeMemoManager.getTradeMemo(Long.parseLong(order.get("tid").toString()), order.get("dp_id").toString());
			if(tradeRes != null && tradeRes.isSuccess()){
				rtnMap.put("buyerMemo", tradeRes.getTrade().getBuyerMemo());
				rtnMap.put("sellerMemo", tradeRes.getTrade().getSellerMemo());
				rtnMap.put("sellerFlag", tradeRes.getTrade().getSellerFlag());
	    	}else{
	    		rtnMap.put("buyerMemo", "淘宝接口获取信息失败，请重试");
				rtnMap.put("sellerMemo", "淘宝接口获取信息失败，请重试");
	    	}
			rtnMap.put("ostatus", OrderStatus.getMessage(order.get("ostatus").toString()));
			if(order.get("shipping_status") == null){
				//TODO 如果没物流信息，默认为未同城
				rtnMap.put("shippingStatus", ShippingStatus.getMessage(4)+"(默认)");
			}else{
				rtnMap.put("shippingStatus", ShippingStatus.getMessage(new Integer(order.get("shipping_status").toString())));
			}
			if(order.get("created") != null){
				rtnMap.put("pay_time", order.get("created").toString());
			}
			return rtnMap;
		}
	}

	/**
	 * 对信用等级和好评率做了处理
	 * @param cd
	 * @return
	 */
	private Map<String, Object> genCustomerInfo(CustomerDomain cd) {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		if(cd != null){
			rtnMap = BeanUtil.beanToMap(cd);
			if(cd.getBirthYear() != null){
				Calendar cal = Calendar.getInstance();
				rtnMap.put("age", cal.get(Calendar.YEAR)-cd.getBirthYear());
			}
			rtnMap.put("created", DateUtils.getStringDate(cd.getCreated()));
			if(cd.getBuyerCreditLev() != null){
				rtnMap.put("buyerCreditLevel", CREDIT_LEVEL[cd.getBuyerCreditLev()]);
			}
			if(cd.getBuyerCreditGoodNum() != null && cd.getBuyerCreditTotalNum() != null){
				double goodRate = divide(cd.getBuyerCreditGoodNum(), cd.getBuyerCreditTotalNum(), 4)*100;
				rtnMap.put("buyerGoodRate", goodRate+"%");
			}
			rtnMap.put("birthday", cd.getBirthday() == null ? "" : DateUtils.getString(cd.getBirthday()));
			rtnMap.put("sex", StringUtils.isEmpty(cd.getSex()) ? "" : (cd.getSex().equals("m") ? "男" : "女"));
		}
		return rtnMap;
	}

	private Double divide(Number src,Number des,int length){
        if(des.doubleValue() == 0){
            return 0.00;
        }
        BigDecimal result = new BigDecimal(src.doubleValue()).divide(new BigDecimal(des.doubleValue()),length,BigDecimal.ROUND_HALF_UP);
        return result.doubleValue();
    }

	/**
	 * 转化了事务重要性、状态、剩余时间
	 * @param affairDomain
	 * @return
	 */
	private Map<String, Object> genAffairInfo(AffairDomain affairDomain) {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		if(affairDomain != null){
			rtnMap = BeanUtil.beanToMap(affairDomain);
			//由于beantomap后，变为了long型
			rtnMap.put("expiration_time", DateUtils.getStringDate(affairDomain.getExpirationTime()));
			rtnMap.put("created", DateUtils.getStringDate(affairDomain.getCreated()));
			rtnMap.put("updated", DateUtils.getStringDate(affairDomain.getUpdated()));
//			rtnMap.put("important", AFFAIR_IMPORTANT[affairDomain.getImportant()]);
			rtnMap.put("important", affairDomain.getImportant());
			rtnMap.put("status", affairDomain.getStatus());
			if(3 == affairDomain.getStatus() || 4 == affairDomain.getStatus()){
				rtnMap.put("remainTime", "");
			}else{
				if(affairDomain.getExpirationTime().before(new Date())){
					rtnMap.put("remainTime", "已超期");
				}else{
					rtnMap.put("remainTime", "剩余"+ DateUtils.compareDate(affairDomain.getExpirationTime(), new Date()));
				}
			}
		}
		return rtnMap;
	}

	/**
     * 给订单添加备注
	 * @param shopId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/tradememo/add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ControlerResult tradeMemoAdd(@RequestBody TradeMemoAddInterRequest tRequest) {
        boolean ifsuc = taobaoTradeMemoManager.addOrUpdateTradeMemo(tRequest.getTid(), tRequest.getMemo(), tRequest.getFlag(), tRequest.getShopId());
        if(ifsuc){
        	return ControlerResult.newSuccess("订单备注添加成功！");
        }else{
        	return ControlerResult.newError("订单备注添加失败,请重试！");
        }
    }

}
