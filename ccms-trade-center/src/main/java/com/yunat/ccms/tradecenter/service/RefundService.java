package com.yunat.ccms.tradecenter.service;

import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.controller.vo.RefundRequest;
import com.yunat.ccms.tradecenter.controller.vo.RefundVO;
import com.yunat.ccms.tradecenter.domain.RefundProofFileDomain;
import com.yunat.ccms.tradecenter.domain.RefundTopContentDomain;
import com.yunat.ccms.tradecenter.service.queryobject.RefundQuery;

/**
 * Created with IntelliJ IDEA.
 * User: 李卫林
 * Date: 13-7-17
 * Time: 上午11:16
 * To change this template use File | Settings | File Templates.
 */
public interface RefundService {

	/**
	 * 存入凭证信息
	 * @param reqs
	 * @param path
	 * @param fileName
	 * @return
	 */
	RefundProofFileDomain saveRefundProofFileDetail(RefundRequest reqs, String path, String fileName);

	/**
	 * 得到凭证信息
	 * @return
	 */
	List<Map<String, Object>> findProofDetail(String dpId);

	/**
	 * 根据凭证Id删除凭证
	 * @param proofId
	 * @return
	 */
	String proofFileDel(Long proofId);

	/**
	 * 根据常用话术Id删除常用话术
	 * @param topId
	 * @return
	 */
	RefundTopContentDomain delTopContent(Long topId);

    /**
     * 得到所有的常用话术
     * @param dpId
     * @return
     */
	List<Map<String, Object>> findTopContentList(String dpId);

	/**
	 * 根据pkid  如果存在修改当前的话术，否则增加
	 * @param reqs
	 * @return
	 */
	RefundTopContentDomain updateTopContent(RefundRequest reqs);

	/**
	 * 根据pkid  如果存在修改当前的话术，否则增加
	 * @param reqs
	 * @return
	 */
	RefundTopContentDomain updateTopContent(Map<String , Object> req);

    /**
     * 退款事务
     *
     * @param refundQuery
     * @return
     */
    List<RefundVO> findWorkRefunds(RefundQuery refundQuery);

    /**
     * 统计未完成的状态数量
     * @param status
     * @return
     */
    Map<String, Long> statisticsDealWithStatus(String dpId);

    /**
     * 统计退款原因
     */
    void refundResonStaticsTask();

}
