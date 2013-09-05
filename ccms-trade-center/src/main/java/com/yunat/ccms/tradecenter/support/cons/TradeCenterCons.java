package com.yunat.ccms.tradecenter.support.cons;

public interface TradeCenterCons {

    /**
     * 统计间隔
     * 一分钟
     */
    int statisInterval = 60;

    /**
     * 淘宝商品前缀
     */
    String ITEM_PREFIX = "http://detail.taobao.com/item.htm?id=";

    /**
     *
     */
    String FILTER_CONDITION_SEPARATOR = ",";

    /**
     * 物流关怀最大间隔时间（小时）
     */
    int LOGIS_CARE_INTERVAL_HOUR = 12;
}
