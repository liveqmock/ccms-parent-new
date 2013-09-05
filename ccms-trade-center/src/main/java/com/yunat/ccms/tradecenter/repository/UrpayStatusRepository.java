package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import com.yunat.ccms.tradecenter.domain.UrpayStatusDomain;


/**
 * 催付状态数据访问接口
 *
 * @author shaohui.li
 * @version $Id: UrpayStatusRepository.java, v 0.1 2013-6-4 下午05:05:52 shaohui.li Exp $
 */
public interface UrpayStatusRepository {

    /**
     * 批量保存催付状态
     *
     * @param list
     */
    public void insertUrpayStatusBatch(List<UrpayStatusDomain> list,String type);

    /**
     * 根据tid更新手动催付状态
     * 如果不存在则插入
     * @param tid TODO
     * @param tid
     */
    void saveOrUpdateManualStatus(int manualUrpayStatus, String tid);

}
