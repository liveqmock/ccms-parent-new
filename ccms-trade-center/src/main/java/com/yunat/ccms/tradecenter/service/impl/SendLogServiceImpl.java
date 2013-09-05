package com.yunat.ccms.tradecenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.base.constants.Constant;
import com.yunat.base.enums.app.AppEnum;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.SendLogDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.repository.SendLogRepository;
import com.yunat.ccms.tradecenter.service.SendLogService;
import com.yunat.ccms.tradecenter.util.TaskUtil;
import com.yunat.channel.business.sms.SMSDynamicZipUtil;
import com.yunat.channel.business.sms.SMSField;
import com.yunat.channel.business.sms.SMSPreviewUtil;
import com.yunat.channel.client.ChannelClient;
import com.yunat.channel.request.info.GateWayInfoRequest;
import com.yunat.channel.request.service.sp.SMSDynamicRequest;

/**
 *
 * SendLogService 接口实现类
 *
 * @author shaohui.li
 * @version $Id: SendLogServiceImpl.java, v 0.1 2013-6-4 下午01:51:34 shaohui.li
 *          Exp $
 */
@Service("sendLogService")
public class SendLogServiceImpl implements SendLogService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	SendLogRepository sendLogRepository;

	@Override
	public List<SendLogDomain> getSmsLogByBuyer(String dpId, Date sendDate, String buyer, int urpayOrCareType) {
		Date startDate = DateUtils.dateStart(sendDate);
		Date endDate = DateUtils.dateEnd(startDate);
		return sendLogRepository.getSmsLogByBuyer(dpId, startDate, endDate, buyer, urpayOrCareType);
	}

	@Override
	public List<SendLogDomain> getSmsLogByMobile(String dpId, Date sendDate, String mobile, int urpayOrCareType) {
		Date startDate = DateUtils.dateStart(sendDate);
		Date endDate = DateUtils.dateEnd(startDate);
		return sendLogRepository.getSmsLogByMobile(dpId, startDate, endDate, mobile, urpayOrCareType);
	}

	@Override
	public List<SendLogDomain> getSmsLogByMobileOrBuyer(String dpId, Date sendDate, String mobile, String buyerNick,
			int urpayOrCareType) {
		// 获取指定日期的最大最小值
		Date startDate = DateUtils.dateStart(sendDate);
		Date endDate = DateUtils.dateEnd(startDate);
		return sendLogRepository.getSmsLogByMobileOrBuyer(dpId, startDate, endDate, mobile, buyerNick, urpayOrCareType);
	}

	@Override
	public void saveSendLogDomain(SendLogDomain domain) {
		sendLogRepository.save(domain);
	}

	@Override
	public BaseResponse<String> sendSMS(List<SmsQueueDomain> ListT, Long gatewayId, String ccmsUser, String passWord) {
		BaseResponse<String> resT = new BaseResponse<String>();
		if (ListT.size() > 0) {
			logger.info("订单中心发送短信------用户：" + ccmsUser + " 获取通道【" + gatewayId + "】的信息！！");
			BaseResponse<Map<String, Object>> res = getWayInfo(ccmsUser, passWord, gatewayId);
			List<SmsQueueDomain> sendList = new ArrayList<SmsQueueDomain>();
			if (res.isSuccess()) {
				Map<String, Object> gateWayMap = res.getRtnData();
				SMSDynamicZipUtil smsUtil = null;
				// 通道打包数据
				int pkgAmount = (Integer) getValue(gateWayMap, "maxnum_dynamic");
				int wordsLimit = (Integer) getValue(gateWayMap, "words_limit");
				int gatewayType = (Integer) getValue(gateWayMap, "gateway_type");
				int markLength = (Integer) getValue(gateWayMap, "mark_length");
				String backorderinfo = getValue(gateWayMap, "backorderinfo").toString();
				String sign = getValue(gateWayMap, "sign").toString();
				String taskId = TaskUtil.getTaskId(ccmsUser);
				logger.info("订单中心发送短信------用户：" + ccmsUser + " 获取通道【" + gatewayId + "】的信息成功，发送" + ListT.size()
						+ "条短信！！");
				try {
					SMSField smsFie = new SMSField(markLength, wordsLimit, gatewayType, 0, backorderinfo, sign);
					smsUtil = new SMSDynamicZipUtil(pkgAmount, taskId, smsFie);
					for (SmsQueueDomain order : ListT) {
						String sms = order.getSms_content();
						sms = sms.replaceAll("&nbsp;", " ");
						String mobile = order.getMobile();
						Matcher me = Constant.NEWLINE_REGEX.matcher(mobile);
						if (me.find()) {
						    mobile = me.replaceAll("");
						}
						smsUtil.put(order.getBuyer_nick(), mobile, sms);
						SMSPreviewUtil pu = new SMSPreviewUtil(smsFie);
						String[] st = pu.smsPreview(sms);
						Integer smsNum = 0;
						if (st != null) {
							smsNum = st.length - 1;
						}
						order.setSmsNum(smsNum);
						sendList.add(order);
					}
				} catch (Exception ex) {
					logger.error("短信组装计算异常", ex);
				}

				SMSDynamicRequest req = new SMSDynamicRequest();
				req.setContent(smsUtil.getZipBytes());
				req.setTaskId(taskId);
				req.setRequestLen(smsUtil.getTotal());// 请求总数
				req.setActualLen(smsUtil.getTotalSum());// 总发送数
				req.setSmsContent("模板内容省略");
				req.setGatewayId(gatewayId);
				req.setUserName(ccmsUser);
				req.setPassWord(passWord);
				req.setCampId("8888");
				req.setNodeId("8888");
				// 发送短信
				logger.info("订单中心发送短信------用户：" + ccmsUser + " task_id：" + taskId + " 请求总数：" + smsUtil.getTotal()
						+ " 总发送数：" + smsUtil.getTotalSum() + " 通道ID：" + gatewayId);
				BaseResponse<String> response = ChannelClient.getInstance(AppEnum.CCMS, null).excute(req);
				if (response.isSuccess()) {
					logger.info("订单中心发送短信------用户：" + ccmsUser + " task_id：" + taskId + " 发送成功！！");
					sendFinishT(sendList, gatewayId, 1, taskId);
					return response;
				} else {
					logger.info("订单中心短信发送失败:" + response);
					sendFinishT(sendList, gatewayId, 0, taskId);
					return response;
				}
			} else {
				logger.info("获取通道信息失败，短信发送失败，" + "错误码：" + res.getErrCode() + ",错误信息：" + res.getErrMsg());
				sendFinishT(ListT, gatewayId, 0, null);
				resT.setSuccess(false);
				resT.setErrMsg("获取通道信息失败");
				return resT;
			}
		} else {
			logger.info("订单中心发送短信为0------");
			resT.setSuccess(false);
			resT.setErrMsg("您将要发送的消息为空");
			return resT;
		}
	}

	private void sendFinishT(List<SmsQueueDomain> sendList, Long gatewayId, Integer sendStatus, String taskId) {
		for (SmsQueueDomain sms : sendList) {
			SendLogDomain log = new SendLogDomain();
			log.setDpId(sms.getDpId());
			log.setGatewayId(gatewayId);
			log.setSendStatus(sendStatus);
			log.setBuyerNick(sms.getBuyer_nick());
			log.setSendUser(sms.getSend_user());
			log.setTid(sms.getTid());
			log.setTradeCreated(sms.getTrade_created());
			log.setMobile(sms.getMobile());
			log.setType(sms.getType());
			log.setSmsContent(sms.getSms_content());
			log.setSmsNum(sms.getSmsNum());
			log.setTask_id(taskId);
			log.setCreated(new Date());
			log.setUpdated(new Date());
			log.setOid(sms.getOid());
			saveSendLogDomain(log);
		}
	}

	/**
	 * 获取通道信息
	 *
	 * @param userName
	 *            ：用户名
	 * @param password
	 *            ：密码
	 * @param gateWayId
	 *            ：通道Id
	 * @return
	 */
	private BaseResponse<Map<String, Object>> getWayInfo(String userName, String password, Long gateWayId) {
		GateWayInfoRequest req = new GateWayInfoRequest();
		req.setUserName(userName);
		req.setPassWord(password);
		req.setGatewayId(gateWayId);
		BaseResponse<Map<String, Object>> response = null;
		int i = 0;
		while (i < 3) {
			i++;
			response = ChannelClient.getInstance(AppEnum.CHANNEL, PlatEnum.taobao).excute(req);
			if (response.isSuccess()) {
				return response;
			}
		}
		return response;
	}

	/**
	 * 从Map里面获取value
	 *
	 * @param map
	 * @param key
	 * @return
	 */
	private Object getValue(Map<String, Object> map, String key) {
		Object returnValue = "";
		if (map == null || map.isEmpty()) {
			return returnValue;
		}
		if (map.containsKey(key) == false) {
			return returnValue;
		} else {
			returnValue = map.get(key);
			if (returnValue == null) {
				returnValue = "";
			}
		}
		return returnValue;
	}

	@Override
	public List<SendLogDomain> getTraderateRegardHistorySendRecord(String tid, String oid) {
		return sendLogRepository.findByTidAndOid(tid, oid);
	}


	public static void main(String[] args) {
	    String mobile = "13456595646\nsadfji";
	    System.out.println(mobile);
	    Matcher me = Constant.NEWLINE_REGEX.matcher(mobile);
        if (me.find()) {
            mobile = me.replaceAll("");
        }
        System.out.println(mobile);
    }
}
