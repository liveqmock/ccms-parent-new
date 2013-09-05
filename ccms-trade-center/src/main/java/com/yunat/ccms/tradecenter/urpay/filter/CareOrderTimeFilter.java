package com.yunat.ccms.tradecenter.urpay.filter;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.support.cons.FilterResultType;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.ccms.tradecenter.support.util.DateUtil;

/**
 * 关怀订单时间过滤器
 * @author 李卫林
 *
 */
@Component("careOrderTimeFilter")
public class CareOrderTimeFilter implements IFilter{

    /** 日志对象 **/
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {
		FilterResult retn = new FilterResult();

		//如果类型不对，过滤掉
		if (!(config instanceof CareConfigDomain)) {
			retn.setFilter(true);
			return retn;
		}

		CareConfigDomain careConfigDomain  = (CareConfigDomain)config;

    	   //开始时间
        Date startDate = careConfigDomain.getStartDate();

        //结束时间
        Date endDate = null;
        Integer dateNumber = careConfigDomain.getDateNumber();
        if(dateNumber == null){
            endDate = careConfigDomain.getEndDate();
        }else{
            //总是开始，表示结束时间为100年后
            if(dateNumber == 0){
                endDate = DateUtils.addDay(startDate, 365 * 100);
            }else{
                endDate = careConfigDomain.getEndDate();
            }
        }

        // 下单关怀情况下，如果用户选择的是[下单后发送],需要判断订单状态为未付款
        if (UserInteractionType.ORDER_CARE.getType().equals(careConfigDomain.getCareType())) {
            if(careConfigDomain.getCareMoment() == 0){
                if(order.getOrderStatus() != 1){
                    logger.info("用户选择[下单后发送]，此时状态为非未付款，订单被过滤！");
                    retn.setFilter(true);
                    return retn;
                }
            }
            if(careConfigDomain.getCareMoment() == 1){
                if(order.getOrderStatus() != 2){
                    logger.info("用户选择[付款后发送]，此时状态为非付款，订单被过滤！");
                    retn.setFilter(true);
                    retn.setFilteredStatus(FilterResultType.NOT_SEND.getType());
                    return retn;
                }
            }
        }

        //获取当前关怀类型对应的状态时间
        Date careStatusTime = null;

        switch (UserInteractionType.get(careConfigDomain.getCareType())) {
            //下单关怀
        	case ORDER_CARE :
        		careStatusTime = (careConfigDomain.getCareMoment() == 1 ? order.getPayTime() : order.getCreated());
        		break;
        	//发货关怀
        	case SHIPMENT_CARE :
        		careStatusTime = order.getConsignTime();
        		break;
        	//确认收货关怀
        	case CONFIRM_CARE :
        		careStatusTime = order.getEndtime();
        		break;
        }

        if (careStatusTime != null) {

        	/*    比较，如果小于结束时间或小于开始时间，都被过滤掉   */

        	if (getInterval(careStatusTime, endDate) > 0) {
        		retn.setFilter(true);
        	}

        	if (getInterval(careStatusTime, startDate) < 0) {
        		retn.setFilter(true);
        	}

            /*   最大时间过滤  */

            //如果为确认收货时间，不超过12小时
            if (UserInteractionType.CONFIRM_CARE.getType().equals(careConfigDomain.getCareType())) {
            	if (DateUtil.getSeconds(new Date(), careStatusTime) > 12 * 3600) {
            		retn.setFilter(true);
            	}
            }
        }



        return retn;
    }

    @Override
    public String getFilterName() {
        return getClass().getSimpleName();
    }

    /**
     * 返回两个日期相差的毫秒数
     * date1 - date2
     * @param date1
     * @return
     */
    private long getInterval(Date date1, Date date2) {
    	return date1.getTime() - date2.getTime();
    }

}
