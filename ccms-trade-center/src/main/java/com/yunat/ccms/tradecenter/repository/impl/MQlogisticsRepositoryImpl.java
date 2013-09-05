/**
 *
 */
package com.yunat.ccms.tradecenter.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.controller.vo.LogisticsVO;
import com.yunat.ccms.tradecenter.repository.MQlogisticsRepository;
import com.yunat.ccms.tradecenter.service.queryobject.LogisticsQuery;
import com.yunat.ccms.tradecenter.support.cons.AbnormalStatus;
import com.yunat.ccms.tradecenter.support.cons.OrderStatus;
import com.yunat.ccms.tradecenter.support.cons.ShippingStatus;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-17 下午01:50:07
 */
@Repository
public class MQlogisticsRepositoryImpl implements MQlogisticsRepository {

	private static Logger logger = LoggerFactory.getLogger(MQlogisticsRepositoryImpl.class);

	@Autowired
	private JdbcPaginationHelper helper;

	@Override
	public List<Map<String, Object>> queryShopId() {
		String sql = "select shop_id from plt_taobao_shop";
		return helper.queryForList(sql, new HashMap<String, Object>(0));
	}

	@Override
	public List<Map<String, Object>> queryOrderMQ() {
		String sql = "select o.dp_id as dpId,o.tid as tid from plt_taobao_order_tc o left JOIN plt_taobao_transitstepinfo_tc l on o.tid = l.tid  "
				+ "where o.order_status =3 and o.consign_time is not null and ( l.shipping_status !=3 or l.shipping_status is null ) ";
		return helper.queryForList(sql, new HashMap<String, Object>(0));
	}

	@Override
	public List<Map<String, Object>> queryStepinfoTmp(List<String> tidList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (tidList != null && tidList.size() > 0) {
			String tidWhere = "";
			for (String tid : tidList) {
				tidWhere = tidWhere + "'" + tid + "',";
			}
			tidWhere = tidWhere.substring(0, tidWhere.length() - 1);
			String sql = "select * from plt_taobao_transitstepinfo where tid in (" + tidWhere + ")";
			list = helper.queryForList(sql, new HashMap<String, Object>(0));
		}
		return list;

	}

	@Override
	public void deleteStepinfoTmp(List<String> tidList) {
		if (tidList != null && tidList.size() > 0) {
			String tidWhere = "";
			for (String tid : tidList) {
				tidWhere = tidWhere + "'" + tid + "',";
			}
			tidWhere = tidWhere.substring(0, tidWhere.length() - 1);
			String sql = "delete from plt_taobao_transitstepinfo where tid in (" + tidWhere + ")";
			helper.update(sql, new HashMap<String, Object>(0));
		}
	}

	@Override
	public List<Map<String, Object>> queryOrderTC(List<String> tidList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (tidList != null && tidList.size() > 0) {
			String tidWhere = "";
			for (String tid : tidList) {
				tidWhere = tidWhere + "'" + tid + "',";
			}
			tidWhere = tidWhere.substring(0, tidWhere.length() - 1);
			String sql = "select * from plt_taobao_order_tc where tid in (" + tidWhere + ")";
			list = helper.queryForList(sql, new HashMap<String, Object>(0));
		}
		return list;

	}

	@Override
	public List<Map<String, Object>> queryCareStatus(List<String> tidList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (tidList != null && tidList.size() > 0) {
			String tidWhere = "";
			for (String tid : tidList) {
				tidWhere = tidWhere + "'" + tid + "',";
			}
			tidWhere = tidWhere.substring(0, tidWhere.length() - 1);
			String sql = "select * from tb_tc_care_status where tid in (" + tidWhere + ")";
			list = helper.queryForList(sql, new HashMap<String, Object>(0));
		}
		return list;

	}

	@Override
	public List<LogisticsVO> findWorkLogisticsList(LogisticsQuery logisticsQuery, Integer abnormalDays) {

		String selectCount = "select count(*)";
        String select = "select o.*, t.*, os.*";
        String from = " from plt_taobao_order_tc o FORCE INDEX (idx_consigntime_dpid_status)";
        String join = " inner join plt_taobao_transitstepinfo_tc t on t.tid = o.tid";
        join += " inner join tb_tc_customer_orders_ship os on os.tid = o.tid";

        if (AbnormalStatus.DIFFICULT_THING.getStatus().equals(logisticsQuery.getAbnormalStatus())){
            join += " left join tb_tc_properties_config p on p.dp_id = o.dp_id and p.name = o.receiver_state";
        }

        String conditions = " where o.dp_id = " + logisticsQuery.getDpId();
        conditions += " and o.status = '" + OrderStatus.WAIT_BUYER_CONFIRM_GOODS.getStatus() + "'";

        //公司名
        if (!StringUtils.isEmpty(logisticsQuery.getCompanyName())) {
            conditions += " and t.company_name = '" +  logisticsQuery.getCompanyName() + "'";
        }

        //发货状态
        if (!StringUtils.isEmpty(logisticsQuery.getShippingStatus())) {
        	conditions += " and t.shipping_status = " +  logisticsQuery.getShippingStatus();
        }

        //收货人省份
        if (!StringUtils.isEmpty(logisticsQuery.getReceiverState())) {
        	 conditions += " and o.receiver_state = '" +  logisticsQuery.getReceiverState() + "'";
        }

        //收货人省份
        if (logisticsQuery.getReceiverStates() != null && logisticsQuery.getReceiverStates().length > 0) {
            conditions += " and o.receiver_state in (";
            for (int i = 0; i < logisticsQuery.getReceiverStates().length; i++) {
                conditions += "'" + logisticsQuery.getReceiverStates()[i] + "'";
                if (i < logisticsQuery.getReceiverStates().length - 1) {
                    conditions += ",";
                }
            }

            conditions += ")";
        }

        //发货开始时间
        if (!StringUtils.isEmpty(logisticsQuery.getConsignStartTime())) {
            conditions += " and o.consign_time > '" + logisticsQuery.getConsignStartTime() + "'";
        }

        //发货结束时间
        if (!StringUtils.isEmpty(logisticsQuery.getConsignEndTime())) {
            conditions += " and o.consign_time < '" + logisticsQuery.getConsignEndTime() + "'";
        }

        //用户名
        if (!StringUtils.isEmpty(logisticsQuery.getCustomerno())) {
            conditions += " and o.customerno = '" + logisticsQuery.getCustomerno() + "'";
        }

        //关键字
        if (!StringUtils.isEmpty(logisticsQuery.getKeyWord())) {
            conditions += " and t.transit_step_info like '%" + logisticsQuery.getKeyWord() + "%'";
        }

        // 在途时长
        int minInTransitDuration = 0;
        if (logisticsQuery.getMinInTransitDuration() != null) {
            minInTransitDuration =  logisticsQuery.getMinInTransitDuration();
        }
         Date cutDate1 = DateUtils.addDay(new Date(), -minInTransitDuration);
         conditions += " and o.consign_time < '" + DateUtils.getStringDate(cutDate1) + "'";

        // 运单号
        if (!StringUtils.isEmpty(logisticsQuery.getOutSid())) {
            conditions += " and t.out_sid = '" + logisticsQuery.getOutSid() + "'";
        }

        //用户手机号
        if (!StringUtils.isEmpty(logisticsQuery.getReceiverMobile())) {
            conditions += " and o.receiver_mobile = '" + logisticsQuery.getReceiverMobile() + "'";
        }

        //订单号
        if (!StringUtils.isEmpty(logisticsQuery.getTid())) {
            conditions += " and o.tid = '" + logisticsQuery.getTid() + "'";
        }

        //是否关怀
        if (logisticsQuery.getIsCare() != null) {
            if (logisticsQuery.getIsCare()) {
                conditions += " and os.logistics_care_status = 1";
            } else {
                conditions += " and os.logistics_care_status = 0";
            }
        }

        if (logisticsQuery.getIsHide() != null) {
        	if (logisticsQuery.getIsHide()) {
        		conditions += " and os.logistics_hide = 1";
        	} else {
        		conditions += " and os.logistics_hide = 0";
        	}
        }

        // 异常状态查询
        if (!StringUtils.isEmpty(logisticsQuery.getAbnormalStatus())) {
            if (AbnormalStatus.DIFFICULT_THING.getStatus().equals(logisticsQuery.getAbnormalStatus())) {
            	Date cutDate = DateUtils.addDay(new Date(), -50);
            	if (abnormalDays != null) {
            		cutDate = DateUtils.addDay(new Date(), -abnormalDays);
            	}
                conditions += " and (t.abnormal_status like '%疑难件%' or t.recently_time < '" + DateUtils.getStringDate(cutDate)  + "' or o.consign_time < DATE_SUB(NOW(), INTERVAL p.VALUE DAY)) and t.shipping_status !=3";
            } else if ("超区件".equals(logisticsQuery.getAbnormalStatus()))  {
                conditions += " and t.abnormal_status like '%超区件%'  and t.shipping_status !=3";
            } else if (AbnormalStatus.REFUSE_SIGN.getStatus().equals(logisticsQuery.getAbnormalStatus())) {
                conditions += " and t.abnormal_status like '拒收' and t.shipping_status !=3";
            } else if (AbnormalStatus.NO_FLOW.getStatus().equals(logisticsQuery.getAbnormalStatus())) {
                conditions += " and t.abnormal_status like '%无流转%'  and t.shipping_status !=3";
            } else if (AbnormalStatus.LOGISTICS_NOUPDATE_2DAY.getStatus().equals(logisticsQuery.getAbnormalStatus())) {
                Date cutDate = DateUtils.addDay(new Date(), -2);
                conditions += " and t.shipping_status != 3 and t.recently_time < '" + DateUtils.getStringDate(cutDate) +"'";
            } else if (AbnormalStatus.LOGISTICS_NOUPDATE_5DAY.getStatus().equals(logisticsQuery.getAbnormalStatus())) {
                Date cutDate = DateUtils.addDay(new Date(), -5);
                conditions += " and t.shipping_status != 3 and t.recently_time < '" + DateUtils.getStringDate(cutDate) +"'";
            }
        }

        String order = " order by o.consign_time desc";

        String limit = " limit " + logisticsQuery.getStartRow() + "," + logisticsQuery.getPageSize();

        String sqlCount = selectCount + from + join + conditions;
        int count = helper.queryForInt(sqlCount, new HashMap<String, Object>(0));
        logisticsQuery.setTotalItem(count);

        String sql = select + from + join + conditions + order + limit;
        logger.info("sql:{}", sql);

        RowMapper<LogisticsVO> mapper = new RowMapper<LogisticsVO>(){
            @Override
            public LogisticsVO mapRow(ResultSet rs, int paramInt) throws SQLException {
                LogisticsVO logisticsVO = new LogisticsVO();
                logisticsVO.setTid(rs.getString("tid"));
                logisticsVO.setBuyerMessage(rs.getString("buyer_message"));
                logisticsVO.setIsCare(rs.getBoolean("logistics_care_status"));
                logisticsVO.setCompanyName(rs.getString("company_name"));
                logisticsVO.setConsignTime1(rs.getTimestamp("consign_time"));
                logisticsVO.setCustomerno(rs.getString("customerno"));
                logisticsVO.setIsHide(rs.getBoolean("logistics_hide"));
                logisticsVO.setShippingStatus(ShippingStatus.getMessage(rs.getInt("shipping_status")));
                logisticsVO.setOutSid(rs.getString("out_sid"));
                logisticsVO.setPayment(rs.getDouble("payment"));
                logisticsVO.setReceiverAddress(rs.getString("receiver_address"));
                logisticsVO.setReceiverCity(rs.getString("receiver_city"));
                logisticsVO.setReceiverDistrict(rs.getString("receiver_district"));
                logisticsVO.setReceiverName(rs.getString("receiver_name"));
                logisticsVO.setReceiverMobile(rs.getString("receiver_mobile"));
                logisticsVO.setReceiverState(rs.getString("receiver_state"));
                logisticsVO.setReceiverZip(rs.getString("receiver_zip"));
                logisticsVO.setReceiverAddress(rs.getString("receiver_address"));
                logisticsVO.setSellerFlag(rs.getInt("seller_flag"));
                logisticsVO.setShippingType(rs.getString("shipping_type"));
                logisticsVO.setAbnormalStatus(rs.getString("abnormal_status"));
                logisticsVO.setRecentlyTime1(rs.getTimestamp("recently_time"));
                logisticsVO.setEndTime(DateUtils.getStringDate(rs.getTimestamp("timeout_action_time")));
                logisticsVO.setLogisticsFollowup(rs.getBoolean("logistics_followup"));

                return logisticsVO;
            }
        };

        List<LogisticsVO> logisticsVOs = helper.query(sql, new HashMap<String, Object>(0), mapper);
        return logisticsVOs;

	}

	/**
	 * 查询订单的物流信息
	 *
	 * @param tid
	 * @return
	 */
	public Map<String, Object> queryStansitStepInfo(String tid) {

		String sql = "select transit_step_info, shipping_status, out_sid, company_name from plt_taobao_transitstepinfo_tc where tid = " + tid;
		List<Map<String, Object>> list = helper.queryForList(sql, new HashMap<String, Object>(0));

		if (list == null || list.size() <= 0) {
			return null;
		}

		return list.get(0);
	}
}
