package com.yunat.ccms.tradecenter.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;
import com.yunat.ccms.tradecenter.repository.AffairJdbcRepository;

@Repository
public class AffairJdbcRespositoryImpl implements AffairJdbcRepository {

	@Autowired
    private JdbcPaginationHelper helper;


	@Override
	public List<Map<String, Object>> findOrderItemsInfoByTid(String tid) {
//		String sql = "select o.num_iid,o.tid,o.oid,o.payment,o.status,o.num,o.refund_status,o.title,o.pic_path,o.sku_properties_name,t.result" +
//				" from plt_taobao_order_item_tc o,plt_taobao_traderate t where o.tid = :tid and t.tid = :tid and t.oid = o.oid";
		String sql = "select o.num_iid,o.tid,o.oid,o.payment,o.status,o.num,o.refund_status,o.title,o.pic_path,o.sku_properties_name,t.result from plt_taobao_order_item_tc o " +
				" LEFT JOIN plt_taobao_traderate t ON t.oid = o.oid where o.tid = :tid";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tid", tid);
		return helper.queryForList(sql, paramMap);
	}

	@Override
	public Map<String, Object> findOrderInfoByTid(String tid) {
//		String jpql = "select o.pay_time,o.tid,o.payment,o.status as ostatus,t.out_sid,t.status as tstatus,t.logistics_status,t.company_name,t.shipping_status"
//				+ " from plt_taobao_order_tc o,plt_taobao_transitstepinfo_tc t where o.tid = :tid and t.tid = o.tid";
		String sql = "select o.dp_id,o.created,o.tid,o.payment,o.status as ostatus,t.out_sid,t.status as tstatus,t.logistics_status,t.company_name,t.shipping_status from plt_taobao_order_tc o"
			+ " LEFT JOIN plt_taobao_transitstepinfo_tc t ON t.tid = o.tid where o.tid = :tid";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tid", tid);
		List l = helper.queryForList(sql, paramMap);
		if(l == null || l.isEmpty()){
			return null;
		}else{
			return helper.queryForMap(sql, paramMap);
		}
	}

}
