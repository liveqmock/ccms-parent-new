package com.yunat.ccms.tradecenter.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.yunat.ccms.tradecenter.controller.vo.UrPayOrdersLogVo;

/**
 * 客服订单关系业务处理
 *
 * @author ming.peng
 * @date 2013-6-4
 * @since 4.1.0
 */
public interface CustomerOrdersService {

	/**
	 * 催付成功订单记录分页列表 及计算总的挽回销售额[包括今天在内的三天时间]
	 * @param page
	 * @param dpId
	 * @return
	 */
	public UrPayOrdersLogVo urPayOrdersLogList(Pageable page, String dpId);

	/**
	 * 得到当前店铺Id中客服旺旺列表
	 * @param dpId
	 * @return
	 */
	public List<Map<String, Object>> getOrderReceptionWwListByDpId(String dpId);

	/**
	 * 修改当前订单ID的状态为隐藏，如果该订单ID不存在则生成该订单关系数据，且状态为隐藏
	 * @param tid 订单ID 列表
	 */
	public int[] updateCusOrdsShipHide(boolean isHide, String hideColumnName, String... tids);

	/**
	 * 根据订单ID如果存在客服订单关系实体且状态为未隐藏则修改为隐藏，状态为隐藏改为未隐藏，否则插入数据且状态也为隐藏
	 * @param tid 订单ID 列表
	 */
	public int updateCusOrdsShipHide(String tid, String hideColumnName);

	/**
	 * 统计出与客服相关的订单关系数据
	 */
	public List<Map<String, Object>> getCustomerOrdersShipData();

	/**
	 * 对统计出与客服相关的订单关系数据做业务处理后保存
	 */
	public int[] saveCustomerOrdersShipData();

}
