package com.yunat.ccms.tradecenter.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.tradecenter.controller.vo.UrPayOrdersLogVo;
import com.yunat.ccms.tradecenter.repository.CustomerOrdersRepository;
import com.yunat.ccms.tradecenter.service.CustomerOrdersService;

/**
 * 客服订单关系业务处理实现
 *
 * @author ming.peng
 * @date 2013-6-4
 * @since 4.1.0
 */
@Service("customerOrdersService")
public class CustomerOrdersServiceImpl implements CustomerOrdersService{

	@Autowired
	private CustomerOrdersRepository cusOrdersRep;

	public UrPayOrdersLogVo urPayOrdersLogList(Pageable page, String dpId) {
		// 计算总的挽回销售额
		double totalAmount = cusOrdersRep.sumTotalAmount(dpId);
		// 分页数据
		Page<Map<String, Object>> listPage = cusOrdersRep.urPayOrdersLogList(page, dpId);
		UrPayOrdersLogVo data = new UrPayOrdersLogVo();
		data.setTotalAmount(totalAmount);
		data.setPageSize(page.getPageSize());
		data.setContent(listPage.getContent());
		data.setCurrPage(page.getPageNumber() + 1);
		data.setTotalPages(listPage.getTotalPages());
		data.setTotalElements(listPage.getTotalElements());
		return data;
	}

	public List<Map<String, Object>> getOrderReceptionWwListByDpId(String dpId) {
		return cusOrdersRep.getOrderReceptionWwListByDpId(dpId);
	}

	@Transactional
	public int updateCusOrdsShipHide(String tid, String hideColumnName) {
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("tid", tid);
		return cusOrdersRep.updateCustomerOrdersShipData(map, hideColumnName);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public int[] updateCusOrdsShipHide(boolean isHide, String hideColumnName, String... tids) {
		Map<String, ?>[] array = new Map[tids.length];
		for (int i = 0; i < tids.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>(2);
			map.put("tid", tids[i]);
			map.put("isHide", isHide);
			array[i] = map;
		}
		return cusOrdersRep.updateCustomerOrdersShipData(array, hideColumnName);
	}

	public List<Map<String, Object>> getCustomerOrdersShipData(){
		return cusOrdersRep.getCustomerOrdersShipData();
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public int[] saveCustomerOrdersShipData(){
		List<Map<String, Object>> list = getCustomerOrdersShipData();
		if (CollectionUtils.isNotEmpty(list)){
			Map<String, ?>[] array = list.toArray(new Map[0]);
			return cusOrdersRep.saveCustomerOrdersShipData(array);
		}
		return new int[0];
	}


}
