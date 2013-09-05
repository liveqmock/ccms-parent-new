package com.yunat.ccms.tradecenter.service;

import java.util.List;

import com.yunat.ccms.tradecenter.domain.AffairsHandleDomain;

/**
 * User: weilin.li
 * Date: 13-7-29
 * Time: 下午2:51
 */
public interface AffairsHandleService {
    /**
     * 事务跟进处理
     * @param note
     * @param nextHandler
     * @param status
     * @param affirsId
     */
    void handleAffairs(String note, String nextHandler, int status, long affirsId);

    /**
     * 根据事务id查询事务跟进详情
     * @param affair_id
     * @return
     */
	List<AffairsHandleDomain> findAffairHandles(Long affair_id);
}
