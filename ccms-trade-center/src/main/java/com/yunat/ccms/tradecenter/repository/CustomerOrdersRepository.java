package com.yunat.ccms.tradecenter.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * 客服订单关系数据处理
 *
 * @author ming.peng
 * @date 2013-6-3
 * @since 4.1.0
 */
public interface CustomerOrdersRepository {

	/**
	 * 计算总的挽回销售额[包括今天在内的三天时间]
	 * @param dpId
	 * @return
	 */
	public double sumTotalAmount(String dpId);

	/**
	 * 催付成功订单记录分页列表
	 * @param dpId
	 * @return
	 */
	public Page<Map<String, Object>> urPayOrdersLogList(Pageable page, String dpId);

	/**
	 * 得到当前店铺Id中客服旺旺列表
	 * @param dpId
	 * @return
	 */
	public List<Map<String, Object>> getOrderReceptionWwListByDpId(String dpId);


	/**
	 * 根据订单ID如果存在客服订单关系实体且状态为未隐藏则修改为隐藏，状态为隐藏改为未隐藏，否则插入数据且状态也为隐藏
	 * @param array 订单ID
	 * @return
	 */
	public int updateCustomerOrdersShipData(Map<String, ?> array, String hideColumnName);

	/**
	 * 根据订单ID如果存在客服订单关系实体则修改为隐藏，否则插入数据且状态也为隐藏
	 * @param tids 订单ID 列表
	 * @return
	 */
	public int[] updateCustomerOrdersShipData(Map<String, ?>[] array, String hideColumnName);

	/**
	 * 保存统计出与客服相关的订单关系数据
	 * @param array
	 * @return
	 */
	public int[] saveCustomerOrdersShipData(Map<String, ?>[] array);

	/**
	 * 统计出与客服相关的订单关系数据
	 * @return
	 */
	public List<Map<String, Object>> getCustomerOrdersShipData();


}
