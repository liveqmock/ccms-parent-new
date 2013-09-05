package com.yunat.ccms.tradecenter.service;

import java.util.List;

import com.yunat.ccms.tradecenter.domain.UrpayStatusDomain;

/**
 *
 * 催付状态服务接口
 * @author shaohui.li
 * @version $Id: UrpayStatusService.java, v 0.1 2013-6-4 下午07:49:12 shaohui.li Exp $
 */
public interface UrpayStatusService {

    /**
     * 批量插入或者更新催付状态数据
     *
     * @param list
     * @param type
     */
    public void  insertUrpayStatusBatch(List<UrpayStatusDomain> list,String type);
}
