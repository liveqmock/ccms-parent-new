package com.yunat.ccms.tradecenter.urpay.task;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.auth.login.taobao.TaobaoShopService;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.RefundDomain;
import com.yunat.ccms.tradecenter.domain.WarnConfigDomain;
import com.yunat.ccms.tradecenter.service.RefundWarnService;
import com.yunat.ccms.tradecenter.service.WarnService;
import com.yunat.ccms.tradecenter.support.cons.RefundStatus;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

/**
 * 任务启动由quartz调度，内部所以简化逻辑，不记录前次执行的任何信息
 * @author Administrator
 *
 */
public class RefundWarnTask extends BaseJob{

	/** 日志对象 **/
    protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    protected WarnService warnService;

	@Autowired
    protected RefundWarnService refundWarnService;

	@Autowired
	protected TaobaoShopService taobaoShopService;

	/** 当前执行线程 **/
    private String thread ;

	@Override
	public void handle(JobExecutionContext context) {
		logger.info("----------------------退款警告任务启动了------------------------");
		thread = DateUtils.getStringDate(new Date());
		List<WarnConfigDomain> configList = getWarnConfig(UserInteractionType.REFUND_WARN.getType().toString());
		if(configList != null){
			//短信通知里的开始时间,也是最后一次退款警告的有效执行时间
			//待改进
//			Date lastDealTime = refundWarnService.getLastDealRefundTime();
			Date[] stAndEn = getLastDealRefundTime(new Date());
			Date lastDealTime = stAndEn[0];
			Date nowTime = stAndEn[1];
			if(lastDealTime == null){
				lastDealTime = org.apache.commons.lang.time.DateUtils.addDays(new Date(), -1);
				logger.info("lastDealTime为null按这个时间段查询退款订单： "+ lastDealTime+ "---"+ nowTime);
			}else{
				logger.info("lastDealTime按这个时间段查询退款订单： "+ lastDealTime+ "---"+ nowTime);
			}

			for(WarnConfigDomain config : configList){
				String dpNick = taobaoShopService.get(config.getDpId()).getShopName();
				String dpId = config.getDpId();
				List<RefundDomain> rds = refundWarnService.getRefundListByDpId(dpId, lastDealTime, nowTime);
				if(rds.size() != 0){
					List<String> oids = new ArrayList<String>();

					int refundUnclocedNum = 0;
					int refundTotalNum = rds.size();

					String timeDomain = DateUtils.getStringDate(lastDealTime) + "-"+ thread;

					BigDecimal total = BigDecimal.ZERO;
					for(RefundDomain rd : rds){
						if(isUnclosedRefund(rd)){
							refundUnclocedNum ++;
							total = total.add(BigDecimal.valueOf(rd.getRefundFee()));
							oids.add(rd.getOid());
						}
					}
					logger.info("退款单数(包括完成的和关闭的): "+ refundTotalNum);
					logger.info("退款单数(不包括完成的和关闭的): "+ refundUnclocedNum);
//					String smsContent = config.getContent();
					//"您的店铺:XXXXXX旗舰店,18号X点-19号X点收到X笔退款。其中X笔仍未完成，合计XX元【数据赢家】"
					String rtn = "您的店铺【店铺昵称】，【时间段】累计收到【退款笔数】笔退款。其中【未完成退款笔数】笔仍未完成，合计 【总金额】 元【数据赢家】";
					String smsContent = rtn.replaceAll("【店铺昵称】", dpNick).replaceAll("【时间段】", timeDomain).replaceAll("【退款笔数】", refundTotalNum+"")
					.replaceAll("【未完成退款笔数】", refundUnclocedNum+"").replaceAll(" 【总金额】", total+"");

					//TODO gatewayId问题 1L 应该为null
					BaseResponse<String> br = refundWarnService.sendRefundWarnSms(config.getWarnMobiles() , null, dpId, smsContent, dpNick);

					if(br.isSuccess()){
						refundWarnService.record(oids, thread);
						logger.info("店铺 "+ dpId+ " 退款告警成功 : [内容] "+ smsContent);
						logger.info("[接受者]:"+ config.getWarnMobiles());
					}else{
						logger.error("店铺 "+ dpId+ " 退款告警失败： [ErrCode]-"+ br.getErrCode()+ "-[ErrDesc]-"+ br.getErrDesc()+ "-[ErrMsg]-"+ br.getErrMsg());
					}
				}else{
					logger.info("店铺 "+ dpId+ " 退款告警成功  无退款订单....");
				}
			}
		}
		logger.info("----------------------退款警告任务结束了------------------------");
	}

	/**
	 * 获取起始时间点,[0]开始时间,[1]结束时间
	 * @param now
	 *
	 * @return
	 * @throws ParseException
	 */
	private static Date[] getLastDealRefundTime(Date now){
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		int minutes = 0;;
		int seconds = 0;
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.SECOND, seconds);

		Date[] ds = new Date[2];
		ds[1] = calendar.getTime();

		int nowhour = calendar.get(Calendar.HOUR_OF_DAY);
		if(nowhour == 8){
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			calendar.set(Calendar.HOUR_OF_DAY, 20);
		}else if(nowhour == 12){
			calendar.set(Calendar.HOUR_OF_DAY, 8);
		}else if(nowhour == 16){
			calendar.set(Calendar.HOUR_OF_DAY, 12);
		}else if(nowhour == 20){
			calendar.set(Calendar.HOUR_OF_DAY, 16);
		}else{
			return ds;
		}
		ds[0] = calendar.getTime();

		return ds;
	}

	private boolean isUnclosedRefund(RefundDomain rd) {
		String type = rd.getStatus();
		//TODO
		logger.info("子订单： "+ rd.getOid() +" 退款状态： "+ rd.getStatus()+ " 返回为："+ (!(RefundStatus.CLOSED.getStatus().equals(type) || RefundStatus.SUCCESS.getStatus().equals(type))));
		return !(RefundStatus.CLOSED.getStatus().equals(type) || RefundStatus.SUCCESS.getStatus().equals(type));
	}

	/**
	 * 根据告警类型，获取警告配置表的有效信息
	 * @return
	 */
	private List<WarnConfigDomain> getWarnConfig(String warnType) {
		return warnService.getRefundWarnConfigListAtPresent(warnType);
	}

	public static void main(String[] args) {
		Date now = DateUtils.getDateTime("2013-02-05 12:00:01");
		System.out.println(getLastDealRefundTime(now));

		String type = "CLOSED";
		System.out.println(!(RefundStatus.CLOSED.getStatus().equals(type) || RefundStatus.SUCCESS.getStatus().equals(type)));
	}
}
