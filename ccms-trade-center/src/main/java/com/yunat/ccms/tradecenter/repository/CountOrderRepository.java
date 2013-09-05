/**
 *
 */
package com.yunat.ccms.tradecenter.repository;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yunat.ccms.tradecenter.controller.vo.UrpayOrderRequest;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-5 下午03:42:38
 */
public interface CountOrderRepository {

	/**
	 * 发送记录导出
	 * @param page
	 * @param request
	 * @return
	 */
	void getSendLogByTypeAlls(UrpayOrderRequest request, PrintWriter writer);

	/**
	 * 发送记录查询
	 * @param page
	 * @param request
	 * @return
	 */
	Page<Map<String, Object>> getSendLogByType(Pageable page, UrpayOrderRequest request);

	/**
     * 催付统计
     *
     * @param urpayType
     * @return
     */
	List<Map<String, Object>> countUrpayOrderNum(Integer urpayType);

	/**
     * 统计短信量
     *
     * @param urpayType
     * @return
     */
	Integer countUrpaySmsNum(Integer urpayType, String dpId, String urpayDate);

	/**
     * 统计30天短信量
     *
     * @param
     * @return
     */
	List<Map<String, Object>> countAllUrpaySmsNum();

	/**
	 * @param type
	 * @return
	 */
	List<Map<String, Object>> countAllUrpaySmsNum(Integer type,String dpId);



}
