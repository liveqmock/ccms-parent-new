package com.yunat.ccms.tradecenter.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.repository.UrpayOrderRespository;

/**
 * 催付订单查询接口
 *
 * @author shaohui.li
 * @version $Id: UrpayOrderRespositoryImpl.java, v 0.1 2013-6-5 下午04:53:03 shaohui.li Exp $
 */
@Repository
public class UrpayOrderRespositoryImpl implements UrpayOrderRespository{

    /** 日志对象 **/
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private RowMapper<OrderDomain> mapper = new RowMapper<OrderDomain>(){
        @Override
        public OrderDomain mapRow(ResultSet rs, int paramInt) throws SQLException {
            OrderDomain d = new OrderDomain();
            d.setTid(rs.getString("tid"));
            d.setDpId(rs.getString("dp_id"));
            d.setCustomerno(rs.getString("customerno"));
            d.setCreated(rs.getTimestamp("created"));
            d.setTradeFrom(rs.getString("trade_from"));
            d.setPayment(rs.getDouble("payment"));
            d.setReceiverMobile(rs.getString("receiver_mobile"));
            d.setReceiverName(rs.getString("receiver_name"));
            d.setConsignTime(rs.getTimestamp("consign_time"));
            d.setPayTime(rs.getTimestamp("pay_time"));
            d.setReceiverCity(rs.getString("receiver_city"));
            d.setReceiverDistrict(rs.getString("receiver_district"));
            d.setAutoUrpayStatus(rs.getInt("auto_urpay_status"));
            return d;
        }
    };

    private StringBuffer commomSql(String dpId){
        StringBuffer sb = new StringBuffer("select a.tid,a.dp_id,a.customerno,a.created,a.trade_from,a.payment,a.receiver_mobile," +
        		"a.receiver_name,a.consign_time,a.pay_time,a.receiver_city,a.receiver_district,b.auto_urpay_status from plt_taobao_order_tc a");
        sb.append(" inner join  tb_tc_urpay_status b on a.tid = b.tid ");
        sb.append(" where a.pay_time is null and a.order_status = 1 and (a.type != 'step' and a.type != 'cod') and a.dp_id = '" + dpId + "'");
        return sb;
    }


    @Autowired
    private JdbcPaginationHelper helper;

    /**
     * 获取三日内未付款的订单且未催付的 + 前一天未付款且需次日发送的订单
     *
     * 催付状态：
     *
     * 0：未催付
     * 1：已催付
     * 2：隔日催付
     * 3：实际短信不需要发送(会作为发送统计数据进行统计)
     *
     *
     * @see com.yunat.ccms.tradecenter.repository.UrpayOrderRespository#getNotPayedAndNotUrpayedOrders()
     */

    @Override
    public List<OrderDomain> getNotPayedAndNotUrpayedOrders(String dpId) {
        StringBuffer sb = commomSql(dpId);
        //当前时间
        Date curTime = new Date();
        //当前时间的3天前
        Date startTime = DateUtils.addDay(curTime, -3);
        //结束时间
        Date endTime = curTime;
        //当日未付款，且未催付的订单
        sb.append(" and ((a.created >= '" + DateUtils.getStringDate(startTime) + "' and a.created <='" + DateUtils.getStringDate(endTime) + "' and b.auto_urpay_status = 0)");
        //前一日需要次日催付的订单
        Date preDay = DateUtils.addDay(curTime, -1);
        String preDate = DateUtils.getStringYMD(preDay);
        sb.append(" or (b.auto_urpay_status  = 2 and SUBSTR(b.auto_urpay_thread,1,8)='" + preDate + "'))");
        logger.info("自动催付 sql=[" + sb.toString() + "]");
        return helper.query(sb.toString(), new HashMap<String, Object>(0), mapper);
    }

    /**
     * 获取预关闭的订单
     *
     * 订单的范围：
     *
     * 下单时间在：以当前时间为结束点，向前推3天的时间为开始点，所有未付款，未催付的订单,且不包括聚划算订单
     *
     **/
    @Override
    public List<OrderDomain> getPreclosedOrders(String dpId) {
        StringBuffer sb = commomSql(dpId);
        //当前时间
        Date curTime = new Date();
        //当前时间的3天前
        Date startTime = DateUtils.addDay(curTime, -3);
        //结束时间
        Date endTime = curTime;
        sb.append(" and a.created >= '" + DateUtils.getStringDate(startTime) + "' and a.created <='" + DateUtils.getStringDate(endTime) + "' and b.close_urpay_status = 0");
        //不包括聚划算
        sb.append(" and a.trade_from != 'JHS'");
        return helper.query(sb.toString(), new HashMap<String, Object>(0), mapper);
    }

    /**
    *
    * 获取聚划算的订单数据
    *
    * 订单的范围：
    *   只选择当日订单，未付款且未未催付
    *   且订单来源为聚划算催付
    *
    * @return
    */
    @Override
    public List<OrderDomain> getCheapOrders(String dpId) {
        StringBuffer sb = commomSql(dpId);
        //当日下单，且未催付
        sb.append(" and date(a.created) = curdate() and b.cheap_urpay_status = 0");
        //必须是聚划算
        sb.append(" and a.trade_from = 'JHS'");
        return helper.query(sb.toString(), new HashMap<String, Object>(0), mapper);
    }
}
