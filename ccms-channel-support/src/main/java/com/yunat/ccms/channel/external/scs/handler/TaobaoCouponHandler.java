package com.yunat.ccms.channel.external.scs.handler;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.base.enums.app.AppEnum;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.channel.external.InvokerHandler;
import com.yunat.ccms.channel.external.InvokerLoggerWhenFailPolicy;
import com.yunat.ccms.channel.external.scs.RemoteCallable;
import com.yunat.ccms.channel.external.scs.RemoteHttp;
import com.yunat.ccms.channel.support.service.RemoteLoggerService;
import com.yunat.channel.business.taobao.TaoBaoZipUtil;
import com.yunat.channel.client.ChannelClient;
import com.yunat.channel.request.service.sp.CouponRequest;
import com.yunat.channel.request.service.taobao.CouponSendReportRequest;
import com.yunat.channel.request.service.taobao.CreateCouponRequest;

/**
 * 调用渠道操作taobao优惠券 </br><li>创建优惠券 </br><li>发送优惠券 </br><li>获取优惠券发送报告
 * 
 * @author xiaojing.qu
 * 
 */
@Component
public class TaobaoCouponHandler implements InvokerHandler {

	private static Logger logger = LoggerFactory.getLogger(TaobaoCouponHandler.class);

	@Autowired
	private RemoteLoggerService httpLogService;

	private static final String CREAT_COUPON = "createCoupon";
	private static final String SEND_TASK_COUPON = "sendTaskCoupon";
	private static final String GET_SEND_COUPON_REPORT = "get_send_coupon_report";

	/**
	 * @param tenantId
	 * @param tenantPassword
	 * @param shop_key
	 * @param condition
	 * @param denominationValue
	 *            优惠券面额
	 * @param effctiveTime
	 * @param endTime
	 * @param platEnum
	 * @return
	 */
	public BaseResponse<String> createCoupon(final String tenantId, final String tenantPassword, final String shop_key,
			final Long condition, final Long denominationValue, final String effctiveTime, final String endTime,
			final PlatEnum platEnum) {
		return new RemoteHttp(CHANNEL_REMOTE_SEND_MTD_INITIAL_RETRY_INTERVAL).call(
				new RemoteCallable<BaseResponse<String>>() {
					@Override
					public BaseResponse<String> call() throws Exception {
						CreateCouponRequest sr = new CreateCouponRequest();
						sr.setShopKey(shop_key);
						sr.setDenominations(denominationValue);
						sr.setEffctiveTime(effctiveTime);
						sr.setEndTime(endTime);
						sr.setCondition(condition);
						sr.setUserName(tenantId);
						sr.setPassWord(tenantPassword);
						logger.info("CreateCouponRequest:{}", ToStringBuilder.reflectionToString(sr));
						BaseResponse<String> response = ChannelClient.getInstance(AppEnum.CCMS, platEnum).excute(sr);
						return response;
					}

					@Override
					public String getTaskId() {
						return null;
					}
				}, new InvokerLoggerWhenFailPolicy(CREAT_COUPON, httpLogService), platEnum);
	}

	/**
	 * 优惠劵发送接口
	 * 
	 * @param gatewayId
	 *            通道ID
	 * @param shop_key
	 *            店铺Key
	 * @param tenantId
	 *            系统用户名
	 * @param tenantPassword
	 *            系统密码
	 * @param ecPack
	 *            发送NICK打包
	 * @param couponId
	 *            优惠劵ID
	 * @param sendTime
	 *            发送时间
	 * @param taskId
	 *            发送任务ID
	 * @param campId
	 *            活动ID
	 * @param nodeId
	 *            节点ID
	 * @param fullName
	 *            操作人名称
	 * @param platEnum
	 *            平台类型
	 * @return
	 */
	public BaseResponse<String> sendTaskCoupon(final Long gatewayId, final String shop_key, final String tenantId,
			final String tenantPassword, final TaoBaoZipUtil ecPack, final Long couponId, final String sendTime,
			final String taskId, final String campId, final String nodeId, final String fullName,
			final PlatEnum platEnum) {
		return new RemoteHttp(CHANNEL_REMOTE_SEND_MTD_INITIAL_RETRY_INTERVAL).call(
				new RemoteCallable<BaseResponse<String>>() {
					@Override
					public BaseResponse<String> call() throws Exception {

						CouponRequest cr = new CouponRequest();
						cr.setShopKey(shop_key);
						cr.setCouponId(couponId);
						cr.setContent(ecPack.getZipBytes());
						cr.setTaskId(taskId);
						cr.setRequestLen(ecPack.getTotal());// 请求总数

						cr.setSendTime(sendTime);
						cr.setGatewayId(gatewayId);
						cr.setUserName(tenantId);
						cr.setPassWord(tenantPassword);
						cr.setCampId(campId);
						cr.setNodeId(nodeId);
						cr.setFullName(fullName);
						BaseResponse<String> response = ChannelClient.getInstance(AppEnum.CCMS, platEnum).excute(cr);
						return response;
					}

					@Override
					public String getTaskId() {
						return taskId;
					}
				}, new InvokerLoggerWhenFailPolicy(SEND_TASK_COUPON, httpLogService), platEnum);
	}

	/**
	 * 返回优惠劵发送情况
	 * 
	 * @param tenantId
	 *            系统用户名
	 * @param tenantPassword
	 *            系统密码
	 * @param taskId
	 *            发送任务ID
	 * @param platEnum
	 *            平台类型
	 * @return
	 */
	public BaseResponse<Map<String, Object>> getSendCouponReport(final String tenantId, final String tenantPassword,
			final String taskId, final PlatEnum platEnum) {
		return new RemoteHttp(CHANNEL_REMOTE_SEND_MTD_INITIAL_RETRY_INTERVAL).call(
				new RemoteCallable<BaseResponse<Map<String, Object>>>() {
					@Override
					public BaseResponse<Map<String, Object>> call() throws Exception {
						CouponSendReportRequest req = new CouponSendReportRequest();
						req.setUserName(tenantId);
						req.setPassWord(tenantPassword);
						req.setTaskId(taskId);
						BaseResponse<Map<String, Object>> response = ChannelClient.getInstance(AppEnum.CCMS, platEnum)
								.excute(req);
						return response;
					}

					@Override
					public String getTaskId() {
						return taskId;
					}
				}, new InvokerLoggerWhenFailPolicy(GET_SEND_COUPON_REPORT, httpLogService), platEnum);
	}

}
