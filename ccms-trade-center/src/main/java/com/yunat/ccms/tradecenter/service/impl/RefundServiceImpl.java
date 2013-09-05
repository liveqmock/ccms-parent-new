package com.yunat.ccms.tradecenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.controller.vo.TradeMemoVO;
import com.yunat.ccms.tradecenter.service.TaobaoMemoService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.auth.login.taobao.TaobaoShop;
import com.yunat.ccms.auth.login.taobao.TaobaoShopRepository;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.controller.vo.RefundRequest;
import com.yunat.ccms.tradecenter.controller.vo.RefundVO;
import com.yunat.ccms.tradecenter.controller.vo.SendLogVO;
import com.yunat.ccms.tradecenter.domain.AffairDomain;
import com.yunat.ccms.tradecenter.domain.MemberDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.OrderItemDomain;
import com.yunat.ccms.tradecenter.domain.RefundCareDomain;
import com.yunat.ccms.tradecenter.domain.RefundProofFileDomain;
import com.yunat.ccms.tradecenter.domain.RefundTopContentDomain;
import com.yunat.ccms.tradecenter.domain.SendLogDomain;
import com.yunat.ccms.tradecenter.domain.ShopReasonDomain;
import com.yunat.ccms.tradecenter.domain.TransitstepinfoDomain;
import com.yunat.ccms.tradecenter.repository.AffairRepository;
import com.yunat.ccms.tradecenter.repository.MemberGradeRepository;
import com.yunat.ccms.tradecenter.repository.OrderItemRepository;
import com.yunat.ccms.tradecenter.repository.OrderRepository;
import com.yunat.ccms.tradecenter.repository.RefundCareRepository;
import com.yunat.ccms.tradecenter.repository.RefundProofFileRepository;
import com.yunat.ccms.tradecenter.repository.RefundRepository;
import com.yunat.ccms.tradecenter.repository.RefundTopContentRepository;
import com.yunat.ccms.tradecenter.repository.SendLogRepository;
import com.yunat.ccms.tradecenter.repository.ShopReasonRepository;
import com.yunat.ccms.tradecenter.repository.TransitstepinfoRepository;
import com.yunat.ccms.tradecenter.service.PropertiesConfigManager;
import com.yunat.ccms.tradecenter.service.RefundService;
import com.yunat.ccms.tradecenter.service.queryobject.RefundQuery;
import com.yunat.ccms.tradecenter.support.cons.AbnormalStatus;
import com.yunat.ccms.tradecenter.support.cons.OrderStatus;
import com.yunat.ccms.tradecenter.support.cons.PropertiesGroupEnum;
import com.yunat.ccms.tradecenter.support.cons.PropertiesNameEnum;
import com.yunat.ccms.tradecenter.support.cons.RefundStatus;
import com.yunat.ccms.tradecenter.support.cons.ShippingStatus;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionWay;
import com.yunat.ccms.tradecenter.support.util.ListUtil;
import com.yunat.ccms.tradecenter.support.util.StringUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-17
 * Time: 上午11:18
 * To change this template use File | Settings | File Templates.
 */
@Component("refundService")
public class RefundServiceImpl implements RefundService {

	private static Logger logger = LoggerFactory.getLogger(RefundServiceImpl.class);

    @Autowired
    private RefundRepository refundRepository;

    @Autowired
    private TransitstepinfoRepository transitstepinfoRepository;

    @Autowired
    private MemberGradeRepository memberGradeRepository;

    @Autowired
    private RefundCareRepository refundCareRepository;

    @Autowired
    private RefundTopContentRepository contentRepository;

    @Autowired
    private RefundProofFileRepository fileRepository;

    @Autowired
    private PropertiesConfigManager propertiesConfigManager;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private SendLogRepository sendLogRepository;

    @Autowired
    private TaobaoShopRepository taobaoShopRepository;

    @Autowired
    private ShopReasonRepository shopReasonRepository;

    @Autowired
    AffairRepository affairRepository;

    @Autowired
    private TaobaoMemoService taobaoMemoService;

	public RefundProofFileDomain saveRefundProofFileDetail(RefundRequest reqs, String path, String fileName) {
		RefundProofFileDomain domain = new RefundProofFileDomain();
		domain.setCreated(new Date());
		domain.setDpId(reqs.getDpId());
		domain.setFileName(fileName);
		domain.setPath(path);
		return fileRepository.save(domain);
	}

	public List<Map<String, Object>> findProofDetail(String dpId) {
		return refundRepository.findProofDetail(dpId);
	}

	@Transactional
    public String proofFileDel(Long proofId) {
    	RefundProofFileDomain domain = fileRepository.findOne(proofId);
    	try {
    		if (ObjectUtils.notEqual(domain, null)){
    			String path = domain.getPath();
    			fileRepository.delete(proofId);
    			return path;
    		}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据库删除凭证失败", e);
		}
		return null;
	}

	@Transactional
	public RefundTopContentDomain delTopContent(Long topId) {
		RefundTopContentDomain domain = contentRepository.findOne(topId);
		if (ObjectUtils.notEqual(domain, null)){
			contentRepository.delete(topId);
		}
		return domain;
	}

	@Transactional
	public RefundTopContentDomain updateTopContent(RefundRequest reqs) {
		RefundTopContentDomain domain = contentRepository.findOne(reqs.getTopId());
    	if (ObjectUtils.equals(domain, null)){
    		domain = new RefundTopContentDomain();
    	}
    	domain.setContent(reqs.getTopContent());
    	domain.setDpId(reqs.getDpId());
		return contentRepository.save(domain);
	}

	@Transactional
	public RefundTopContentDomain updateTopContent(Map<String , Object> req) {
		if(req.get("topId") != null && !StringUtils.isEmpty(req.get("topId").toString())){
			RefundTopContentDomain domain = contentRepository.findOne(Long.parseLong(req.get("topId").toString()));
			if (ObjectUtils.equals(domain, null)){
	    		domain = new RefundTopContentDomain();
	    		domain.setCreated(new Date());
			}else{
				domain.setUpdated(new Date());
			}
			domain.setContent(req.get("topContent").toString());
			domain.setDpId(req.get("dpId").toString());
			return contentRepository.save(domain);
		}else{
			RefundTopContentDomain domain = new RefundTopContentDomain();
			domain.setCreated(new Date());
			domain.setContent(req.get("topContent").toString());
			domain.setDpId(req.get("dpId").toString());
			return contentRepository.save(domain);
		}
	}

	public List<Map<String, Object>> findTopContentList(String dpId) {
		return refundRepository.findTopContentList(dpId);
	}

    public List<RefundVO> findWorkRefunds(RefundQuery refundQuery) {

        //获取物流表数据
        List<RefundVO> refundVOs = refundRepository.fidRefunds(refundQuery);

        List<String> tids = ListUtil.getPropertiesFromList(refundVOs, "tid");
        List<String> buyerNicks = ListUtil.getPropertiesFromList(refundVOs, "buyerNick");
        List<String> oids = ListUtil.getPropertiesFromList(refundVOs, "oid");

        if (tids.size() > 0) {
            Map<String,TradeMemoVO> memoMap = taobaoMemoService.getTaobaoMemo(tids, refundQuery.getDpId());

            //获取相关物流数据
            List<TransitstepinfoDomain> transitstepinfoDomains =    transitstepinfoRepository.findByTidIn(tids);

            //获取用户店铺等级
            List<MemberDomain> memberDomains = memberGradeRepository.getMembersByCustomers(refundQuery.getDpId(), buyerNicks);

            //获取关怀数据
            List<RefundCareDomain> refundCareDomains = refundCareRepository.findByOidIn(oids);

            List<OrderItemDomain> orderItemDomains = orderItemRepository.findOrderItems(tids);

            Map<String, TransitstepinfoDomain> tidToTranMap = ListUtil.toMapByProperty(transitstepinfoDomains, "tid");

            Map<String, OrderItemDomain> oidToOrderItemMap = ListUtil.toMapByProperty(orderItemDomains, "oid");

            //获得订单数据
            List<OrderDomain> orderDomains = orderRepository.findOrderByTids(tids);
            Map<String, OrderDomain> tidToOrderMap = ListUtil.toMapByProperty(orderDomains, "tid");

            //获得下发列表
            List<SendLogDomain> sendLogDomains = new ArrayList<SendLogDomain>();
            if (tids.size() > 0) {
                //获得关怀历史
                List<Integer> types = new ArrayList<Integer>();
                types.add(UserInteractionType.MANUAL_REFUND_MOBILE_CARE.getType());
                types.add(UserInteractionType.MANUAL_REFUND_SMS_CARE.getType());
                types.add(UserInteractionType.MANUAL_REFUND_WW_CARE.getType());
                sendLogDomains = sendLogRepository.getSendLogByTids(tids, types);
            }


            for (RefundVO refundVO : refundVOs) {
                TransitstepinfoDomain transitstepinfoDomain = tidToTranMap.get(refundVO.getTid());

                OrderDomain orderDomain = tidToOrderMap.get(refundVO.getTid());

                //收货手机
                refundVO.setReceiverMobile(orderDomain.getReceiverMobile());

                if (transitstepinfoDomain != null) {
                    refundVO.setCompanyName(transitstepinfoDomain.getCompanyName());
                    refundVO.setShippingStatus(ShippingStatus.getMessage(transitstepinfoDomain.getShippingStatus()));

                    String abnormalStatus = null;
                    String abnormalReason = null;

                    // 疑难件
                    Integer abnormalDays = propertiesConfigManager.getInt(refundQuery.getDpId(), PropertiesNameEnum.ABNORMAL_DAYS.getName());
                    //获取疑难件配置
                    Map<String, String> logisticsNameValueMap = propertiesConfigManager.getNameValueMap(refundQuery.getDpId(), PropertiesGroupEnum.ABNORMAL_LOGISTICS.getName());

                    if (abnormalDays != null) {
                        Date larLogiUpTime = DateUtils.addDay(transitstepinfoDomain.getRecentlyTime(), abnormalDays);
                        if (larLogiUpTime.before(new Date())) {
                            abnormalStatus = AbnormalStatus.DIFFICULT_THING.getStatus();
                            abnormalReason  =  "疑难件：物流无更新时间大于" + abnormalDays + "天";
                        }
                    }

                    // 疑难件
                    Integer consignDays = StringUtil.getInt(logisticsNameValueMap.get(orderDomain.getReceiverState()));
                    if (consignDays != null) {
                        Date larConsignTime = DateUtils.addDay(orderDomain.getConsignTime(), consignDays);
                        if (larConsignTime.before(new Date())) {
                            abnormalStatus = AbnormalStatus.DIFFICULT_THING.getStatus();
                            abnormalReason  =  "疑难件：" + orderDomain.getReceiverState() + "在途时长大于" + consignDays + "天";
                        }
                    }

                    //物流流转信息
                    String abnormalStatus1 = transitstepinfoDomain.getAbnormalStatus();
                    if (abnormalStatus1 != null) {
                        if (abnormalStatus1.contains(AbnormalStatus.DIFFICULT_THING.getStatus())) {
                            abnormalStatus = AbnormalStatus.DIFFICULT_THING.getStatus();
                            abnormalReason  =  "疑难件：物流信息包含" + AbnormalStatus.DIFFICULT_THING.getStatus();
                        }
                    }

                    refundVO.setAbnormalReason(abnormalReason);
                    refundVO.setAbnormalStatus(abnormalStatus);
                }



                String grade = "新用户";
                for (MemberDomain memberDomain : memberDomains)  {
                    if (memberDomain.getId().getCustomerNo().equals(refundVO.getBuyerNick()) && memberDomain.getId().getDpId().equals(refundQuery.getDpId()))  {
                        grade =    memberDomain.getGradeDes();
                        break;
                    }
                }
                refundVO.setGrade(grade);

                boolean isCare = false;
                boolean refundFollowup = false;
                for (RefundCareDomain refundCareDomain : refundCareDomains) {
                	if (refundCareDomain.getOid().equals(refundVO.getOid())) {
                		isCare = refundCareDomain.getRefundCare();
                        refundFollowup = refundCareDomain.getRefundFollowup();
                	}
                }
                refundVO.setIsCare(isCare);
                refundVO.setRefundFollowup(refundFollowup);

                //解析出关怀记录
                List<SendLogVO> sendLogVOs = new ArrayList<SendLogVO>();
                for (SendLogDomain sendLogDomain : sendLogDomains) {
                    if (sendLogDomain.getTid().equals(refundVO.getTid())) {
                        SendLogVO sendLogVO = new SendLogVO();
                        sendLogVO.setContent(sendLogDomain.getSmsContent());
                        sendLogVO.setDate(DateUtils.getStringDate(sendLogDomain.getCreated()));
                        int manualUrpayStatus = 0;
                        if (UserInteractionType.MANUAL_REFUND_SMS_CARE.getType().equals(sendLogDomain.getType())) {
                            manualUrpayStatus = UserInteractionWay.SMS.getType();
                        } else if (UserInteractionType.MANUAL_REFUND_WW_CARE.getType().equals(sendLogDomain.getType())) {
                            manualUrpayStatus = UserInteractionWay.WANGWANG.getType();
                        } else if (UserInteractionType.MANUAL_REFUND_MOBILE_CARE.getType().equals(sendLogDomain.getType())) {
                            manualUrpayStatus = UserInteractionWay.PHONE.getType();
                        }
                        sendLogVO.setManualUrpayStatus(manualUrpayStatus);
                        sendLogVO.setServiceStaff(sendLogDomain.getSendUser());

                        sendLogVOs.add(sendLogVO);
                    }
                }
                refundVO.setCareLogs(sendLogVOs);

                OrderItemDomain orderItemDomain = oidToOrderItemMap.get(refundVO.getOid());
                refundVO.setPicPath(orderItemDomain.getPicPath());
                refundVO.setSkuPropertiesName(orderItemDomain.getSkuPropertiesName());
                refundVO.setNumIid(orderItemDomain.getNumIid());

                //跟进状态关联
                AffairDomain affairDomain = affairRepository.getAffairDomainByOid(refundVO.getOid(), 5l);

                if (affairDomain != null) {
                    refundVO.setFollowupStatus(affairDomain.getStatus());
                    refundVO.setFollowupId(affairDomain.getPkid());
                }

                refundVO.setMemoVo(memoMap.get(refundVO.getTid()));
            }
        }

        return refundVOs;
    }

	@Override
	public Map<String, Long> statisticsDealWithStatus(String dpId) {
        Map<String, Long> statusToCountMap =  new HashMap<String, Long>();

		List<Map<String, Object>> statusToCountMapList =  refundRepository.statisticsDealWithStatus(dpId);

        long refundSize = 0;
        long refundNoSendGoodsSize = 0;
        long onlyRefundHasSendGoodsSize = 0;
        long returnGoodsWaitSellerSize = 0;
        long returnGoodsWaitBuyerSize = 0;
        long returnGoodsWaitSellerConfirmSize = 0;

        for (Map<String, Object> stringObjectMap : statusToCountMapList) {
            refundSize +=  (Long)stringObjectMap.get("size");

            if ((RefundStatus.WAIT_SELLER_AGREE.getStatus().equals(stringObjectMap.get("status"))
                    || RefundStatus.SELLER_REFUSE_BUYER.getStatus().equals(stringObjectMap.get("status"))) && OrderStatus.WAIT_SELLER_SEND_GOODS.getStatus().equals(stringObjectMap.get("order_status"))) {
                refundNoSendGoodsSize += (Long)stringObjectMap.get("size");
            }

            if (OrderStatus.WAIT_BUYER_CONFIRM_GOODS.getStatus().equals(stringObjectMap.get("order_status")) && !Boolean.valueOf((String)stringObjectMap.get("has_good_return"))) {
                onlyRefundHasSendGoodsSize += (Long)stringObjectMap.get("size");;
            }

            if ((RefundStatus.WAIT_SELLER_AGREE.getStatus().equals(stringObjectMap.get("status"))
                    || RefundStatus.SELLER_REFUSE_BUYER.getStatus().equals(stringObjectMap.get("status"))) && Boolean.valueOf((String)stringObjectMap.get("has_good_return"))) {
                returnGoodsWaitSellerSize += (Long)stringObjectMap.get("size");;
            }

            if (RefundStatus.WAIT_BUYER_RETURN_GOODS.getStatus().equals(stringObjectMap.get("status")) && Boolean.valueOf((String)stringObjectMap.get("has_good_return"))) {
                returnGoodsWaitBuyerSize += (Long)stringObjectMap.get("size");;
            }

            if (RefundStatus.WAIT_SELLER_CONFIRM_GOODS.getStatus().equals(stringObjectMap.get("status")) && Boolean.valueOf((String)stringObjectMap.get("has_good_return"))) {
                returnGoodsWaitSellerConfirmSize += (Long)stringObjectMap.get("size");;
            }

        }

		statusToCountMap.put("refundNoSendGoods", refundNoSendGoodsSize);
        statusToCountMap.put("onlyRefundHasSendGoods", onlyRefundHasSendGoodsSize);
        statusToCountMap.put("returnGoodsWaitSeller", returnGoodsWaitSellerSize);
        statusToCountMap.put("returnGoodsWaitBuyer", returnGoodsWaitBuyerSize);
        statusToCountMap.put("returnGoodsWaitSellerConfirm", returnGoodsWaitSellerConfirmSize);
        statusToCountMap.put("refund", refundSize);

		return statusToCountMap;
	}

    @Override
    @Transactional
    public void refundResonStaticsTask() {
        List<TaobaoShop> taobaoShopList = taobaoShopRepository.findAll();

        List<String> cShopIds = new ArrayList<String>();
        Map<String, String> shopToMap = new HashMap<String, String>();
        for (TaobaoShop taobaoShop : taobaoShopList) {
            cShopIds.add(taobaoShop.getShopId());
            shopToMap.put(taobaoShop.getShopId(), taobaoShop.getShopType());
        }

        String refundResonRecentStatisticsTime = propertiesConfigManager.getString(null, PropertiesNameEnum.REFUND_RESON_RECENT_STATISTICS_TIME.getName());

        String currentStaticsTime = DateUtils.getStringDate(new Date());
        //统计C店铺退款原因
        List<Map<String, Object>> mapList = refundRepository.refundResonStaticsTask(cShopIds, refundResonRecentStatisticsTime,currentStaticsTime);

        List<ShopReasonDomain> exitShopReasonDomainList = shopReasonRepository.findByShopIdIn(cShopIds);

        //跟新最近统计时间
        List<ShopReasonDomain> shopReasonDomains = new LinkedList<ShopReasonDomain>();

        for (Map<String, Object> map : mapList) {
            String dpId =  (String)map.get("dp_id");
            String reason =  (String)map.get("reason");

            ShopReasonDomain existShopReasonDomain = null;

            for (ShopReasonDomain shopReasonDomain : exitShopReasonDomainList) {
                if (dpId.equals(shopReasonDomain.getShopId()) && reason.equals(shopReasonDomain.getReason())) {
                    existShopReasonDomain =  shopReasonDomain;
                }
            }

            if (existShopReasonDomain == null) {
                ShopReasonDomain shopReasonDomain = new ShopReasonDomain();

                shopReasonDomain.setShopId(dpId);
                shopReasonDomain.setShopType(shopToMap.get(dpId));
                shopReasonDomain.setReason(reason);

                shopReasonDomains.add(shopReasonDomain);
            }
        }

        shopReasonRepository.save(shopReasonDomains);
        propertiesConfigManager.saveProperties(null,  PropertiesNameEnum.REFUND_RESON_RECENT_STATISTICS_TIME.getName(), currentStaticsTime);
    }
}
