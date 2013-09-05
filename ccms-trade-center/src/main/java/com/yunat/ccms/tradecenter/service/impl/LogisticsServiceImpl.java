package com.yunat.ccms.tradecenter.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.controller.vo.TradeMemoVO;
import com.yunat.ccms.tradecenter.domain.AffairDomain;
import com.yunat.ccms.tradecenter.repository.AffairRepository;
import com.yunat.ccms.tradecenter.service.TaobaoMemoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.api.request.TradeReceivetimeDelayRequest;
import com.taobao.api.response.TradeReceivetimeDelayResponse;
import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.controller.vo.LogisticsRequest;
import com.yunat.ccms.tradecenter.controller.vo.LogisticsVO;
import com.yunat.ccms.tradecenter.controller.vo.SendLogVO;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.SendLogDomain;
import com.yunat.ccms.tradecenter.domain.TransitstepinfoDomain;
import com.yunat.ccms.tradecenter.repository.MQlogisticsRepository;
import com.yunat.ccms.tradecenter.repository.SendLogRepository;
import com.yunat.ccms.tradecenter.repository.TransitstepinfoRepository;
import com.yunat.ccms.tradecenter.service.LogisticsService;
import com.yunat.ccms.tradecenter.service.PropertiesConfigManager;
import com.yunat.ccms.tradecenter.service.TaobaoService;
import com.yunat.ccms.tradecenter.service.queryobject.LogisticsQuery;
import com.yunat.ccms.tradecenter.support.cons.AbnormalStatus;
import com.yunat.ccms.tradecenter.support.cons.PropertiesGroupEnum;
import com.yunat.ccms.tradecenter.support.cons.PropertiesNameEnum;
import com.yunat.ccms.tradecenter.support.cons.ShippingStatus;
import com.yunat.ccms.tradecenter.support.cons.ShippingType;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionWay;
import com.yunat.ccms.tradecenter.support.util.ListUtil;
import com.yunat.ccms.tradecenter.support.util.StringUtil;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-17 下午08:29:26
 */
@Service("LogisticsService")
public class LogisticsServiceImpl implements LogisticsService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	TransitstepinfoRepository transitstepinfoRepository;

	@Autowired
	JdbcPaginationHelper helper;

	@Autowired
	private TaobaoService taobaoService;

	@Autowired
	MQlogisticsRepository mQlogisticsRepository;

	@Autowired
	private PropertiesConfigManager propertiesConfigManager;

	@Autowired
	private SendLogRepository sendLogRepository;

    @Autowired
    private AffairRepository affairRepository;

    @Autowired
    private TaobaoMemoService taobaoMemoService;

	@Transactional
	public int updateTimeoutByTid(Long extensionDays, String... tids) {
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("tids", Arrays.asList(tids));
		paramMap.put("time", extensionDays);
		StringBuilder sql = new StringBuilder("update plt_taobao_order_tc set timeout_action_time = ")
				.append("if(timeout_action_time is null, adddate(consign_time, interval +if(shipping_type='post', 30, 10)+:time day), adddate(timeout_action_time, interval +:time day)) where tid in (:tids)");
		return helper.update(sql.toString(), paramMap);
	}

	@Transactional
	public int updateTimeoutActionTimes(LogisticsRequest req) {
		List<String> list = new LinkedList<String>();
		for (String tid : req.getTids()) {
			TradeReceivetimeDelayRequest reqs = new TradeReceivetimeDelayRequest();
			reqs.setTid(Long.parseLong(tid));
			reqs.setDays(req.getExtensionDays());
			TradeReceivetimeDelayResponse response = taobaoService.execTaobao(req.getDpId(), reqs);
			if (ObjectUtils.notEqual(response, null) && response.isSuccess()) {
				// 需要修改延迟时间
				list.add(tid);
			}
		}
		return CollectionUtils.isEmpty(list) ? 0 : updateTimeoutByTid(req.getExtensionDays(), list.toArray(new String[0])) > 0 ? list.size() : 0;
	}

	@Override
	public Map<String, TransitstepinfoDomain> analysisTransitstepinfoList(List<Map<String, Object>> infoList,
			Map<String, OrderDomain> orderMap, String SIGNED, String REJECT, String DELIVERY) {
		Map<String, TransitstepinfoDomain> stepinfoMap = new HashMap<String, TransitstepinfoDomain>();
		for (Map<String, Object> map : infoList) {
			String tid = null;
			String out_sid = null;
			String status = null;
			String company_name = null;
			String transit_step_info = null;
			Date created = (Date) map.get("created");
			if (map.get("tid") != null) {
				tid = map.get("tid").toString();
			}
			if (map.get("out_sid") != null) {
				out_sid = map.get("out_sid").toString();
			}
			if (map.get("status") != null) {
				status = map.get("status").toString();
			}
			if (map.get("company_name") != null) {
				company_name = map.get("company_name").toString();
			}
			if (map.get("transit_step_info") != null) {
				transit_step_info = map.get("transit_step_info").toString();
			}

			if (tid != null) {
				// 处理接续逻辑
				Integer logisticsStatus = null;
				int shipping_status = 4;// 已经拿到数据
				String signed_time = null;
				String arrived_time = null;
				String delivery_time = null;
				try {
					logisticsStatus = getLogisticsStatus(status);
					// 获取签收在流转信息出现的次数
					int signNum = getkeywordsPlace(transit_step_info, SIGNED);
					int rejectNum = getkeywordsPlace(transit_step_info, REJECT);
					int delivery = getkeywordsPlace(transit_step_info, DELIVERY);
					// 同时出现签收和拒绝签收字样
					if (signNum >= 1 && rejectNum >= 1) {
						int signMaxPos = getMaxPosition(SIGNED, transit_step_info);
						int rejectMaxPos = getMaxPosition(REJECT, transit_step_info);
						// "签收"字样出现的位置大于"拒绝签收"等字样的后面才认为是签收，否则不认为是签收
						if (signMaxPos > rejectMaxPos) {
							shipping_status = 3;
							signed_time = getShippingTime(transit_step_info, signMaxPos);
						}
						// 签收出现一次,且没有出现拒绝签收等字样
					} else if (signNum >= 1 && rejectNum == 0) {
						shipping_status = 3;
						int signMaxPos = getMaxPosition(SIGNED, transit_step_info);
						signed_time = getShippingTime(transit_step_info, signMaxPos);
					} else if (delivery >= 1) {
						shipping_status = 2;
						int deliveryMaxPos = getMaxPosition(DELIVERY, transit_step_info);
						delivery_time = getShippingTime(transit_step_info, deliveryMaxPos);
					} else {
						// 获取用户收获城市
						OrderDomain order = orderMap.get(tid);
						if (order != null) {
							String city = order.getReceiverCity();
							if (StringUtils.isNotBlank(city)) {
								int cityCount = getKeyCount(transit_step_info, city);
								if (cityCount >= 3) {
									shipping_status = 1;
									arrived_time = getLogisticsTime(transit_step_info, city, 3);
								} else {
									city = city.replaceAll("市", "");
									cityCount = getKeyCount(transit_step_info, city);
									if (cityCount >= 3) {
										shipping_status = 1;
										arrived_time = getLogisticsTime(transit_step_info, city, 3);
									}
								}
							}
						}
					}
				} catch (Exception ex) {
					logger.error("整理物流信息出错：tid =" + tid.toString(), ex);
				}

				// 获得物流最近更新时间
				Date recentlyTime = getRecentlyTime(transit_step_info);
				if (recentlyTime == null) {
                    OrderDomain order = orderMap.get(tid);
					recentlyTime = order.getConsignTime();
				}

				// 解析获得异常状态
				String abnormalStatus = null;
				if (!StringUtils.isEmpty(transit_step_info)) {
					if (transit_step_info.contains(AbnormalStatus.DIFFICULT_THING.getStatus())) {
						abnormalStatus = AbnormalStatus.DIFFICULT_THING.getStatus();
					} else if (transit_step_info.contains(AbnormalStatus.SUPER_AREA.getStatus())) {
						abnormalStatus = AbnormalStatus.SUPER_AREA.getStatus();
					} else if (transit_step_info.contains(AbnormalStatus.NO_FLOW.getStatus())) {
						abnormalStatus = AbnormalStatus.NO_FLOW.getStatus();
					} else if (transit_step_info.contains(AbnormalStatus.REFUSE_SIGN.getStatus())) {
						abnormalStatus = AbnormalStatus.REFUSE_SIGN.getStatus();
					}
                    //如果包含null，解析为无流转
                    else if (transit_step_info.toLowerCase().contains("null")) {
                        abnormalStatus = AbnormalStatus.NO_FLOW.getStatus();
                    }
				} else {
					abnormalStatus = AbnormalStatus.NO_FLOW.getStatus();
				}

				TransitstepinfoDomain domain = new TransitstepinfoDomain();
				domain.setTid(tid);
				domain.setOutSid(out_sid);
				domain.setCompanyName(company_name);
				domain.setStatus(status);
				domain.setLogisticsStatus(logisticsStatus);
				domain.setTransitStepInfo(transit_step_info);
				domain.setShippingStatus(shipping_status);
				if (delivery_time != null) {
					domain.setDeliveryTime(DateUtils.getDateTime(delivery_time));
				}
				if (signed_time != null) {
					domain.setSignedTime(DateUtils.getDateTime(signed_time));
				}
				if (arrived_time != null) {
					domain.setArrivedTime(DateUtils.getDateTime(arrived_time));
				}
				domain.setRecentlyTime(recentlyTime);
				domain.setAbnormalStatus(abnormalStatus);
				domain.setUpdated(new Date());
				domain.setCreated(created);
				saveTransitstepinfoDomain(domain);
				stepinfoMap.put(tid, domain);
			}
		}
		return stepinfoMap;

	}

	@Override
	public List<LogisticsVO> findWorkLogisticsList(LogisticsQuery logisticsQuery) {

        long t1 = System.currentTimeMillis();
        //获取物流结果
		Integer abnormalDays = propertiesConfigManager.getInt(logisticsQuery.getDpId(), PropertiesNameEnum.ABNORMAL_DAYS.getName());
        long t2 = System.currentTimeMillis();
        List<LogisticsVO> logisticsVOs = mQlogisticsRepository.findWorkLogisticsList(logisticsQuery, abnormalDays);
        long t3 = System.currentTimeMillis();

        //获取疑难件配置
        Map<String, String> logisticsNameValueMap = propertiesConfigManager.getNameValueMap(logisticsQuery.getDpId(), PropertiesGroupEnum.ABNORMAL_LOGISTICS.getName());
        long t4 = System.currentTimeMillis();

        List<String> tids = ListUtil.getPropertiesFromList(logisticsVOs, "tid");

        List<SendLogDomain> sendLogDomains = new ArrayList<SendLogDomain>();
        Map<String,TradeMemoVO> memoMap = new HashMap<String, TradeMemoVO>();
        if (tids.size() > 0) {
    		//获得关怀历史
    		List<Integer> types = new ArrayList<Integer>();
    		types.add(UserInteractionType.MANUAL_LOGISTICS_SMS_CARE.getType());
    		types.add(UserInteractionType.MANUAL_LOGISTICS_WW_CARE.getType());
    		types.add(UserInteractionType.MANUAL_LOGISTICS_MOBILE_CARE.getType());
    		sendLogDomains = sendLogRepository.getSendLogByTids(tids, types);

            memoMap = taobaoMemoService.getTaobaoMemo(tids, logisticsQuery.getDpId());
        }
        long t5 = System.currentTimeMillis();

        //解析物流状态
        for (LogisticsVO logisticsVO : logisticsVOs) {
        	//解析出关怀记录
        	List<SendLogVO> sendLogVOs = new ArrayList<SendLogVO>();
        	for (SendLogDomain sendLogDomain : sendLogDomains) {
        		if (sendLogDomain.getTid().equals(logisticsVO.getTid())) {
        			SendLogVO sendLogVO = new SendLogVO();
        			sendLogVO.setContent(sendLogDomain.getSmsContent());
        			sendLogVO.setDate(DateUtils.getStringDate(sendLogDomain.getCreated()));
        			int manualUrpayStatus = 0;
        			if (UserInteractionType.MANUAL_LOGISTICS_SMS_CARE.getType().equals(sendLogDomain.getType())) {
        				manualUrpayStatus = UserInteractionWay.SMS.getType();
        			} else if (UserInteractionType.MANUAL_LOGISTICS_WW_CARE.getType().equals(sendLogDomain.getType())) {
        				manualUrpayStatus = UserInteractionWay.WANGWANG.getType();
        			} else if (UserInteractionType.MANUAL_LOGISTICS_MOBILE_CARE.getType().equals(sendLogDomain.getType())) {
        				manualUrpayStatus = UserInteractionWay.PHONE.getType();
        			}
        			sendLogVO.setManualUrpayStatus(manualUrpayStatus);
        			sendLogVO.setServiceStaff(sendLogDomain.getSendUser());

        			sendLogVOs.add(sendLogVO);
        		}
        	}
        	logisticsVO.setCareLogs(sendLogVOs);

        	logisticsVO.setRecentlyTime(DateUtils.getStringDate(logisticsVO.getRecentlyTime1()));
        	logisticsVO.setConsignTime(DateUtils.getStringDate(logisticsVO.getConsignTime1()));
        	logisticsVO.setServerTime(DateUtils.getStringDate(new Date()));

        	//计算关闭时间
        	if (logisticsVO.getEndTime() == null) {
            	//如果是平邮，则为30天
            	Date endTime = null;
            	if (ShippingType.POST.getType().equals(logisticsVO.getShippingType())) {
            		endTime = DateUtils.addDay(logisticsVO.getConsignTime1(), 30);
            	}
            	//其他为10天
            	else {
            		endTime = DateUtils.addDay(logisticsVO.getConsignTime1(), 10);
            	}
            	logisticsVO.setEndTime(DateUtils.getStringDate(endTime));
        	}

        	if (!ShippingStatus.signed.getMessage().equals(logisticsVO.getShippingStatus())) {
        		String abnormalStatus = null;
                String abnormalReason = null;

                // 疑难件
                if (abnormalDays != null) {
                    Date larLogiUpTime = DateUtils.addDay(logisticsVO.getRecentlyTime1(), abnormalDays);
                    if (larLogiUpTime.before(new Date())) {
                        abnormalStatus = AbnormalStatus.DIFFICULT_THING.getStatus();
                        abnormalReason  =  "疑难件：物流无更新时间大于" + abnormalDays + "天";
                    }
                }

                // 疑难件
                Integer consignDays = StringUtil.getInt(logisticsNameValueMap.get(logisticsVO.getReceiverState()));
                if (consignDays != null) {
                    Date larConsignTime = DateUtils.addDay(logisticsVO.getConsignTime1(), consignDays);
                    if (larConsignTime.before(new Date())) {
                        abnormalStatus = AbnormalStatus.DIFFICULT_THING.getStatus();
                        abnormalReason  =  "疑难件：" + logisticsVO.getReceiverState() + "在途时长大于" + consignDays + "天";
                    }
                }

                //物流流转信息
                String abnormalStatus1 = logisticsVO.getAbnormalStatus();
                if (abnormalStatus1 != null) {
    /*                //包含无流转信息
                    if (abnormalStatus1.contains(AbnormalStatus.NO_FLOW.getStatus())) {
                        abnormalStatus = AbnormalStatus.NO_FLOW.getStatus();
                        abnormalReason = "无流转：无物流流转信息";
                    }
                    //超区件
                    else if (abnormalStatus1.contains(AbnormalStatus.SUPER_AREA.getStatus())) {
                        abnormalStatus = AbnormalStatus.SUPER_AREA.getStatus();
                        abnormalReason  =  "超区件：物流信息包含" + AbnormalStatus.SUPER_AREA.getStatus();
                    }*/
                    // 疑难件
                    if (abnormalStatus1.contains(AbnormalStatus.DIFFICULT_THING.getStatus())) {
                        abnormalStatus = AbnormalStatus.DIFFICULT_THING.getStatus();
                        abnormalReason  =  "疑难件：物流信息包含" + AbnormalStatus.DIFFICULT_THING.getStatus();
                    }
    /*                //拒收
                    else if (abnormalStatus1.contains(AbnormalStatus.REFUSE_SIGN.getStatus())) {
                    	//只要在未签收的情况下
                    	if (!ShippingStatus.signed.getMessage().equals(logisticsVO.getShippingStatus())) {
                    		abnormalStatus = AbnormalStatus.REFUSE_SIGN.getStatus();
                        	abnormalReason = "拒收：物流信息包含" +AbnormalStatus.REFUSE_SIGN.getStatus();
                    	}
                    }*/
                }

                //logisticsVO.setAbnormalStatus(abnormalStatus);
                logisticsVO.setAbnormalReason(abnormalReason);
        	}

            //跟进状态关联
            AffairDomain affairDomain = affairRepository.getAffairDomainByTid(logisticsVO.getTid(), 4l);

            if (affairDomain != null) {
                logisticsVO.setFollowupStatus(affairDomain.getStatus());
                logisticsVO.setFollowupId(affairDomain.getPkid());
            }

            logisticsVO.setMemoVo( memoMap.get(logisticsVO.getTid()));

            //解析黑名单
            //获取白名单黑名单
            Map<String, String> nameValueMap =  propertiesConfigManager.getNameValueMap(logisticsQuery.getDpId(), PropertiesGroupEnum.ADDRESS_BLACKLIST.getName());
            String addressBlacklist = nameValueMap.get(PropertiesNameEnum.ADDRESS_BLACKLIST.getName());
            String addressBlacklistExclusive = nameValueMap.get(PropertiesNameEnum.ADDRESS_BLACKLIST_EXCLUSIVE.getName());
            if (addressBlacklist != null) {
                String[] addressBlacklistArray = addressBlacklist.split(",");
                String[] addressExclusiveArray = null;
                if (addressBlacklistExclusive != null) {
                    addressExclusiveArray = nameValueMap.get(PropertiesNameEnum.ADDRESS_BLACKLIST_EXCLUSIVE.getName()).split(",");
                }

                String addressDetail = logisticsVO.getReceiverState() + logisticsVO.getReceiverCity() + logisticsVO.getReceiverDistrict() + logisticsVO.getReceiverAddress();

                //黑名单信息
                String blacklistMessage = null;

                //看看是否在黑名单中
                boolean isBlackList = false;
                for (String black : addressBlacklistArray) {
                	if (addressDetail.contains(black)) {
                		isBlackList = true;
                		blacklistMessage = "地区包含 " + black + " 字样";
                		break;
                	}
                }

                //看看是否在白名单中
                if (isBlackList && addressExclusiveArray != null) {
                	for (String exclusive : addressExclusiveArray) {
                		if (addressDetail.contains(exclusive)) {
                			isBlackList = false;
                			break;
                		}
                	}
                }

                //如果确认黑名单，则将信息加入，给前台显示
                if (isBlackList) {
                	logisticsVO.setBlacklistMessage(blacklistMessage);
                }
            }
        }
        long t6 = System.currentTimeMillis();

        System.out.println("**************************************");
        System.out.println("t2-t1:" + (t2-t1));
        System.out.println("t3-t2:" + (t3-t2));
        System.out.println("t4-t3:" + (t4-t3));
        System.out.println("t5-t4:" + (t5-t4));
        System.out.println("t6-t5:" + (t6-t5));
        System.out.println("**************************************");

        return logisticsVOs;
    }

	private Integer getLogisticsStatus(String status) {
		Integer logisticsStatus = -1;
		// 转化物流状态
		if (StringUtils.equals(status, "等候发送给物流公司")) {
			logisticsStatus = 0;
		} else if (StringUtils.equals(status, "已提交给物流公司,等待物流公司接单")) {
			logisticsStatus = 1;
		} else if (StringUtils.equals(status, "已经确认消息接收，等待物流公司接单")) {
			logisticsStatus = 2;
		} else if (StringUtils.equals(status, "物流公司已接单")) {
			logisticsStatus = 3;
		} else if (StringUtils.equals(status, "物流公司不接单")) {
			logisticsStatus = 4;
		} else if (StringUtils.equals(status, "物流公司揽收失败")) {
			logisticsStatus = 5;
		} else if (StringUtils.equals(status, "物流公司揽收成功")) {
			logisticsStatus = 6;
		} else if (StringUtils.equals(status, "签收失败")) {
			logisticsStatus = 7;
		} else if (StringUtils.equals(status, "对方已签收")) {
			logisticsStatus = 8;
		} else if (StringUtils.equals(status, "对方拒绝签收")) {
			logisticsStatus = 9;
		}
		return logisticsStatus;
	}

	/**
	 *
	 * 获取关键字次数
	 *
	 * @param shippingInfo
	 * @param keys
	 * @return
	 */
	private int getkeywordsPlace(String shippingInfo, String keys) {
		String[] allKey = keys.split("\\|");
		int sum = 0;
		for (int i = 0; i < allKey.length; i++) {
			int numCount = (shippingInfo.length() - (shippingInfo.replaceAll(allKey[i], "")).length())
					/ allKey[i].length();
			sum = sum + numCount;
		}
		return sum;
	}

	/**
	 *
	 * 从流转信息中获取key最后一次出现的位置
	 *
	 * @param keys
	 * @param shippingInfo
	 * @return
	 */
	private int getMaxPosition(String keys, String shippingInfo) {
		String[] allKey = keys.split("\\|");
		int maxPos = 0;
		for (int i = 0; i < allKey.length; i++) {
			int times = getKeyPos(shippingInfo, allKey[i]);
			maxPos = Math.max(times, maxPos);
		}
		return maxPos;
	}

	/**
	 * 获取关键字在流转信息的位置
	 *
	 * @param shippingInfo
	 * @param key
	 * @return
	 */
	private int getKeyPos(String shippingInfo, String key) {
		if (StringUtils.isBlank(shippingInfo)) {
			return 0;
		}
		String[] allStep = shippingInfo.split("\\|");
		int pos = 0;
		for (int i = 0; i < allStep.length; i++) {
			String step = allStep[i];
			String[] info = step.split("@");
			if (info.length == 2) {
				String stepDesc = info[1];
				if (StringUtils.contains(stepDesc, key)) {
					pos = i + 1;
				}
			}
		}
		return pos;
	}

	/**
	 * 根据关键字获取物流时间
	 *
	 * @param shippingInfo
	 *            ：流转信息
	 * @return
	 */
	private String getShippingTime(String shippingInfo, int pos) {
		if (StringUtils.isBlank(shippingInfo)) {
			return null;
		}
		String[] allStep = shippingInfo.split("\\|");
		if (allStep.length >= pos) {
			String step = allStep[pos - 1];
			String[] info = step.split("@");
			String stepTime = info[0];
			return stepTime.split("\\$")[1];
		}
		return null;
	}

	/**
	 * 根据关键字获取物流时间
	 *
	 * @param shippingInfo
	 *            ：流转信息
	 * @param key
	 *            ：关键字
	 * @param num
	 *            ：出现次数
	 * @return
	 */
	private String getLogisticsTime(String shippingInfo, String key, int num) {
		if (StringUtils.isBlank(shippingInfo)) {
			return null;
		}
		String[] allStep = shippingInfo.split("\\|");
		int times = 0;
		for (int i = 0; i < allStep.length; i++) {
			String step = allStep[i];
			String[] info = step.split("@");
			if (info.length == 2) {
				String stepTime = info[0];
				String stepDesc = info[1];
				if (StringUtils.contains(stepDesc, key)) {
					times++;
					if (times == num) {
						return stepTime.split("\\$")[1];
					}
				}
			}
		}
		return null;
	}

    private Date getRecentlyTime(String shippingInfo) {
        if (StringUtils.isBlank(shippingInfo)) {
            return null;
        }

        String[] allStep = shippingInfo.split("\\|");
        String lastStep = allStep[allStep.length - 1];

        String[] info = lastStep.split("\\@");
        String dataTime = info[0].split("\\$")[1];

        Date date = null;

        try {
            date =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTime);
        } catch (ParseException e) {
            logger.error("parse data error!", e);
        }

        return  date;
    }

	/**
	 * 获取关键字出现的次数
	 *
	 * @param shippingInfo
	 *            ：流转信息
	 * @param key
	 *            ：key
	 * @return
	 */
	private int getKeyCount(String shippingInfo, String key) {
		if (StringUtils.isBlank(shippingInfo)) {
			return 0;
		}
		String[] allStep = shippingInfo.split("\\|");
		int times = 0;
		for (int i = 0; i < allStep.length; i++) {
			String step = allStep[i];
			String[] info = step.split("@");
			if (info.length == 2) {
				String stepDesc = info[1];
				if (StringUtils.contains(stepDesc, key)) {
					times++;
				}
			}
		}
		return times;
	}

	@Override
	public void saveTransitstepinfoDomain(TransitstepinfoDomain domain) {
		transitstepinfoRepository.saveAndFlush(domain);
	}

	/**
	 * 获取订单的物流信息清单
	 *
	 * @param tid
	 * @return
	 */
	public Map<String, Object> getTransitStepInfo(String tid) {

		Map<String, Object> queryMap = mQlogisticsRepository.queryStansitStepInfo(tid);

		if (queryMap == null || queryMap.size() <= 0) {

			return null;
		}

		String transitStepInfo = (String)queryMap.get("transit_step_info");
		String shippingStatus = String.valueOf(queryMap.get("shipping_status"));
		String outSid = String.valueOf(queryMap.get("out_sid"));
		String companyName = String.valueOf(queryMap.get("company_name"));

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (!StringUtils.isEmpty(transitStepInfo)) {
            String[] infoArray = transitStepInfo.split("\\|");
            if (infoArray == null || infoArray.length <= 0) {

                return null;
            }

            for (String item : infoArray) {

                if (StringUtils.isEmpty(item)) {

                    continue;
                }

                String[] valueArray = item.trim().split("@");

                if (valueArray == null || valueArray.length <= 0) {

                    continue;
                }

                String timeValue = valueArray[0].trim();
                String infoValue = valueArray[1].trim();

                int timeIndex = timeValue.indexOf("$");
                if (timeIndex != -1) {

                    timeValue = timeValue.substring(timeIndex + 1);
                }

                int infoIndex = infoValue.indexOf("$");
                if (infoIndex != -1) {

                    infoValue = infoValue.substring(infoIndex + 1);
                }

                if (timeIndex == -1 && infoIndex == -1) {

                    continue;
                }

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("time", timeValue);
                map.put("info", infoValue);
                list.add(map);
            }
		}



		HashMap<String, Object> rMap = new HashMap<String, Object>();
		rMap.put("status", shippingStatus);
		rMap.put("company", companyName);
		rMap.put("companyUrl", propertiesConfigManager.getString(null, "orderQueryUrl_" + companyName));
		rMap.put("receiptNo", outSid);
		rMap.put("step", list);
		return rMap;
	}
}
