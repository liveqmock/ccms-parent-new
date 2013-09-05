package com.yunat.ccms.tradecenter.urpay.filter;

/**
 *
 * 过滤结果
 *
 * @author shaohui.li
 * @version $Id: FilterResult.java, v 0.1 2013-6-4 下午02:57:32 shaohui.li Exp $
 */
public class FilterResult {

    /** 是否过滤 **/
    private boolean isFilter = false;

    /** 被过滤掉的状态**/
    /**
     *
     * 1: 需要次日发送
     * 2：不需要次日发送，永远不发送
     * 3：去重订单
     * 0：默认值
     */
    private String filteredStatus = "0";

    public boolean isFilter() {
        return isFilter;
    }

    public void setFilter(boolean isFilter) {
        this.isFilter = isFilter;
    }

    public String getFilteredStatus() {
        return filteredStatus;
    }

    public void setFilteredStatus(String filteredStatus) {
        this.filteredStatus = filteredStatus;
    }

}
