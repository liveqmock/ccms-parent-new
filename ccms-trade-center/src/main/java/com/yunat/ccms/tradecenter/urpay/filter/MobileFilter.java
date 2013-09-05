package com.yunat.ccms.tradecenter.urpay.filter;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;

/**
 *
 *手机号码过滤器
 *
 *只有符合规则的手机号码，才能发送短信
 *
 * @author shaohui.li
 * @version $Id: MobileFilter.java, v 0.1 2013-5-31 上午10:52:03 shaohui.li Exp $
 */
@Component("mobileFilter")
public class MobileFilter implements IFilter{

    /** 日志对象 **/
    private Logger logger = LoggerFactory.getLogger(getClass());

    /** 手机号码正则表达式 **/
    private static final Pattern PHONE = Pattern.compile("^(13[0-9]|14[0-9]|15[0-9]|18[0-9]){1}\\d{8}$");

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {
        FilterResult result = new FilterResult();
        String mobile = order.getReceiverMobile();
        if(StringUtils.isBlank(mobile)){
            logger.info("订单:[" + order.getTid() + "] 手机号码为空");
            result.setFilter(true);
            return result;
        }
        if (!PHONE.matcher(mobile.trim()).find()) {
            logger.info("订单:[" + order.getTid() + "] 手机号码非法");
            result.setFilter(true);
            return result;
        }
        return result;
    }

    @Override
    public String getFilterName() {
        return getClass().getSimpleName();
    }
}
