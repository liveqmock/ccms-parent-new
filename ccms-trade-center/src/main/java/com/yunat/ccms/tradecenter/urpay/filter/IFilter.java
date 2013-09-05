package com.yunat.ccms.tradecenter.urpay.filter;

import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;

/**
 * 催付、关怀通用的过滤器接口
 *
 * @author shaohui.li
 * @version $Id: IFilter.java, v 0.1 2013-5-30 下午04:36:59 shaohui.li Exp $
 */
public interface IFilter {

    /**
     * 执行过滤订单操作
     *
     * @param order：订单对象
     * @return
     *    true：被过滤 false:不过滤
     */
    public FilterResult doFiler(OrderDomain order,BaseConfigDomain config);


    /** 获取过滤器名 **/
    public String getFilterName();


}
