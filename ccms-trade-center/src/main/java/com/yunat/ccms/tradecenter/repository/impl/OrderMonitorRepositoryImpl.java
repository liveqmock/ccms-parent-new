package com.yunat.ccms.tradecenter.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.controller.vo.Today24HourOrderDataVO;
import com.yunat.ccms.tradecenter.repository.OrderMonitorRepository;

/**
 *
 * OrderMonitorRepository 接口实现类
 *
 * @author shaohui.li
 * @version $Id: OrderMonitorRepositoryImpl.java, v 0.1 2013-7-23 下午03:38:04 shaohui.li Exp $
 */
@Repository
public class OrderMonitorRepositoryImpl implements OrderMonitorRepository{

    @Autowired
    private JdbcPaginationHelper helper;

    private Logger logger = LoggerFactory.getLogger(OrderMonitorRepositoryImpl.class);

    /**
     * 获取今天付款的订单数以及金额
     *
     * @param dpId
     * @return
     */
    @Override
    public Map<String, Object> getPayTimeIsTodayOrderNumAndAmount(String dpId) {
        //当前时间
        Date curTime = new Date();
        String startTime = DateUtils.getString(curTime) + " 00:00:00";
        String endTime = DateUtils.getStringDate(DateUtils.dateEnd(curTime));
        String sql = "select count(*) as todayPayedOrderNum,sum(payment) as todayPayedOrderAmount from plt_taobao_order_tc where dp_id = '" + dpId +
                     "' and pay_time >= '" + startTime + "' and pay_time <= '" + endTime + "'";
        logger.info("[获取今天付款的订单数以及金额]sql:[" + sql + "]");
        return helper.queryForMap(sql, new HashMap<String, Object>(0));
    }

    /**
     * 获取今天付款的订单，其子订单的大于1的主订单数
     *
     * @param dpId
     * @return
     */
    @Override
    public Long getPayTimeIsTodayItemNumMoreOneOrderNum(String dpId) {
        //当前时间
        Date curTime = new Date();
        String startTime = DateUtils.getString(curTime) + " 00:00:00";
        String endTime = DateUtils.getStringDate(DateUtils.dateEnd(curTime));
        String sql = "select count(*) orderNum from (" +
        		                "SELECT count(*)AS todayPayedOrderNum  FROM plt_taobao_order_tc a INNER JOIN plt_taobao_order_item_tc b ON a.tid = b.tid where a.dp_id = '" + dpId +
        		                 "' and pay_time >= '" + startTime + "' and pay_time <= '" + endTime + "' GROUP BY a.tid  HAVING  todayPayedOrderNum > 1" +
                     		") r";
        logger.info("[获取今天付款的订单，其子订单的大于1的主订单数]sql:[" + sql + "]");
        return helper.queryForLong(sql, new HashMap<String, Object>(0));
    }

    /**
    *
    *  获取今天付款的订单的商品数
    * @param dpId
    * @return
    */
    @Override
    public Long getPayTimeIsTodayItemGoodsNums(String dpId) {
        //当前时间
        Date curTime = new Date();
        String startTime = DateUtils.getString(curTime) + " 00:00:00";
        String endTime = DateUtils.getStringDate(DateUtils.dateEnd(curTime));
        String sql = " SELECT sum(b.num) as goodsNum FROM plt_taobao_order_tc a INNER JOIN plt_taobao_order_item_tc b ON a.tid = b.tid  where a.dp_id = '" + dpId +
        "' and pay_time >= '" + startTime + "' and pay_time <= '" + endTime + "'";
        logger.info("[获取今天付款的订单的商品数]sql:[" + sql + "]");
        return helper.queryForLong(sql, new HashMap<String, Object>(0));
    }

    /**
     * 获取今天下单的订单数据
     *
     * @param dpId
     */
    @Override
    public Long getCreatedIsTodayOrderNum(String dpId) {
        //当前时间
        Date curTime = new Date();
        String startTime = DateUtils.getString(curTime) + " 00:00:00";
        String endTime = DateUtils.getStringDate(DateUtils.dateEnd(curTime));
        String sql = " select count(*) from plt_taobao_order_tc where dp_id = '" + dpId + "' and created >= '" + startTime + "' and created <= '" + endTime + "'";
        logger.info("[获取今天下单的订单数据]sql:[" + sql + "]");
        return helper.queryForLong(sql, new HashMap<String, Object>(0));
    }

    /**
    *
    * 获取今天下单的订单 且 付款(付款时间不为空)的订单数
    * @param dpId
    * @return
    */
    @Override
    public Long getCreatedIsTodayPayedOrderNum(String dpId) {
        //当前时间
        Date curTime = new Date();
        String startTime = DateUtils.getString(curTime) + " 00:00:00";
        String endTime = DateUtils.getStringDate(DateUtils.dateEnd(curTime));
        String sql = " select count(*) from plt_taobao_order_tc where dp_id = '" + dpId + "' and created >= '" + startTime + "' and created <= '" + endTime + "' and pay_time is not null";
        logger.info("[获取今天下单的订单 且 付款(付款时间不为空)的订单数]sql:[" + sql + "]");
        return helper.queryForLong(sql, new HashMap<String, Object>(0));
    }

    /**
    *
    * 获取今天下单的订单 且 未付款（订单状态：已下单未付款）的订单数
    * @param dpId
    * @return
    */
    @Override
    public Long getCreatedIsTodayNotPayedOrderNum(String dpId) {
        //当前时间
        Date curTime = new Date();
        String startTime = DateUtils.getString(curTime) + " 00:00:00";
        String endTime = DateUtils.getStringDate(DateUtils.dateEnd(curTime));
        String sql = " select count(*) from plt_taobao_order_tc where dp_id = '" + dpId + "' and created >= '" + startTime + "' and created <= '" + endTime + "' and order_status = 1";
        logger.info("[获取今天下单的订单 且 未付款（订单状态：已下单未付款）的订单数]sql:[" + sql + "]");
        return helper.queryForLong(sql, new HashMap<String, Object>(0));
    }

    /**
     *获取获取今天下单的订单，未付款且催付的订单数
     *
     * @param dpId
     * @return
     */
    @Override
    public Long getNotPayAndNotifyOrderNum(String dpId) {
        //当前时间
        Date curTime = new Date();
        String startTime = DateUtils.getString(curTime) + " 00:00:00";
        String endTime = DateUtils.getStringDate(DateUtils.dateEnd(curTime));
        String sql = "select count(a.tid) from plt_taobao_order_tc a inner join tb_tc_urpay_status b on a.tid = b.tid " +
        		     "where a.dp_id = " + dpId + " and a.pay_time is null and a.order_status = 1 and a.created >= '" + startTime + "' and a.created <= '" + endTime + "' " +
        			 "and (b.auto_urpay_status = 1 or b.close_urpay_status = 1 or b.cheap_urpay_status = 1 or b.manual_urpay_status = 1)";
        logger.info("[获取获取今天下单的订单，未付款且催付的订单数]sql:[" + sql + "]");
        return helper.queryForLong(sql, new HashMap<String, Object>(0));
    }

    @Override
    public List<Today24HourOrderDataVO> getToday24HourOrderData(String dpId, boolean limitPayedOrder) {
        //当前时间
        Date curTime = new Date();
        String startTime = DateUtils.getString(curTime) + " 00:00:00";
        String endTime = DateUtils.getStringDate(DateUtils.dateEnd(curTime));
        String sql = "select hour(created) as orderHour,sum(payment) as orderAmount from plt_taobao_order_tc where " +
        		"dp_id = '" + dpId + "' and created >= '" + startTime + "' and created <= '" + endTime + "'";
        if(limitPayedOrder){
            sql = sql +  " and pay_time is not null";
        }
        sql = sql + " group by hour(created)";
        logger.info("[获取今日下单24小时的订单金额]sql:[" + sql + "]");
        RowMapper<Today24HourOrderDataVO> mapper = new RowMapper<Today24HourOrderDataVO>(){
            @Override
            public Today24HourOrderDataVO mapRow(ResultSet rs, int paramInt) throws SQLException {
                Today24HourOrderDataVO vo = new Today24HourOrderDataVO();
                vo.setOrderHour(rs.getInt("orderHour"));
                vo.setOrderAmount(rs.getDouble("orderAmount"));
                return vo;
            }
        };
        return helper.query(sql, new HashMap<String, Object>(0),mapper);
    }

    @Override
    public Long getOrderNumByTableAndFieldName(String dpId, String tableName, String fieldName) {
        //当前时间
        Date curTime = new Date();
        String startTime = DateUtils.getString(curTime) + " 00:00:00";
        String endTime = DateUtils.getStringDate(DateUtils.dateEnd(curTime));
        String sql = " select count(*) from " + tableName + " where dp_id = '" + dpId + "' and " + fieldName + " >= '" + startTime + "' and " + fieldName + " <= '" + endTime + "'";
        logger.info("sql:[" + sql + "]");
        return helper.queryForLong(sql, new HashMap<String, Object>(0));
    }

    /**
     * 获取今日确认收货的订单数
     *
     * @param dpId
     * @return
     */
    @Override
    public Long getConfirmTodayOrderNum(String dpId) {
        //当前时间
        Date curTime = new Date();
        String startTime = DateUtils.getString(curTime) + " 00:00:00";
        String endTime = DateUtils.getStringDate(DateUtils.dateEnd(curTime));
        String sql = " select count(*) from plt_taobao_order_tc where dp_id = '" + dpId + "' and endtime >= '" + startTime + "' and endtime <= '" + endTime + "' and status = 'TRADE_FINISHED'";
        logger.info("[今日确认收货的订单数]sql:[" + sql + "]");
        return helper.queryForLong(sql, new HashMap<String, Object>(0));
    }

    @Override
    public Long getLogisNumByFieldName(String dpId,String fieldName) {
        //当前时间
        Date curTime = new Date();
        String startTime = DateUtils.getString(curTime) + " 00:00:00";
        String endTime = DateUtils.getStringDate(DateUtils.dateEnd(curTime));
        String sql = " select count(b.tid) from plt_taobao_transitstepinfo_tc a inner join plt_taobao_order_tc b on a.tid = b.tid where b.dp_id = '" + dpId + "' and a." + fieldName + " >= '" + startTime + "' and a." + fieldName + " <= '" + endTime + "'";
        logger.info("sql:[" + sql + "]");
        return helper.queryForLong(sql, new HashMap<String, Object>(0));
    }
}
