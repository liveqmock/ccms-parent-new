package com.yunat.ccms.tradecenter.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.springframework.transaction.annotation.Transactional;

import com.yunat.base.util.ParamBuilder;
import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.controller.vo.RefundVO;
import com.yunat.ccms.tradecenter.domain.RefundDomain;
import com.yunat.ccms.tradecenter.domain.RefundOrderDomain;
import com.yunat.ccms.tradecenter.repository.RefundRepository;
import com.yunat.ccms.tradecenter.service.queryobject.RefundQuery;
import com.yunat.ccms.tradecenter.support.cons.OrderStatus;
import com.yunat.ccms.tradecenter.support.cons.RefundStatus;

/**
 * 退款订单查询
 *
 * @author shaohui.li
 * @version $Id: RefundRepositoryImpl.java, v 0.1 2013-7-15 下午04:47:40 shaohui.li Exp $
 */
@Repository
public class RefundRepositoryImpl implements RefundRepository{

    /** 日志对象 **/
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JdbcPaginationHelper helper;

	public List<Map<String, Object>> findProofDetail(String dpId) {
		StringBuilder sql = new StringBuilder("select pkid as id, file_name as fileName, path from tb_tc_refund_proof_file where dp_id = :dpId");
		return helper.queryForList(sql.toString(), ParamBuilder.start("dpId", dpId));
	}

	public List<Map<String, Object>> findTopContentList(String dpId) {
		StringBuilder sql = new StringBuilder("select pkid as id, content from tb_tc_refund_top_content where dp_id = :dpId");
		return helper.queryForList(sql.toString(), ParamBuilder.start("dpId", dpId));
	}

	@Transactional
    public int[] refundOrdersCare(Map<String, ?>[] array) {
    	StringBuilder sql = new StringBuilder("insert into tb_tc_refund_status(tid, oid, dp_id, refund_care, created) ");
    	sql.append("values (:tid,:oid,:dpId,:care,now()) ");
    	sql.append("on duplicate key update refund_care = :care, updated = now()");
		return helper.batchUpdate(sql.toString(), array);
	}

	private RowMapper<RefundOrderDomain> mapper = new RowMapper<RefundOrderDomain>(){
        @Override
        public RefundOrderDomain mapRow(ResultSet rs, int paramInt) throws SQLException {
            RefundOrderDomain d = new RefundOrderDomain();
            d.setTid(rs.getString("tid"));
            d.setOid(rs.getString("oid"));
            d.setDpId(rs.getString("dp_id"));
            d.setRefundFee(rs.getDouble("refund_fee"));
            d.setCustomerno(rs.getString("customerno"));
            d.setCreated(rs.getTimestamp("tradeCreated"));
            d.setRefundTime(rs.getTimestamp("modified"));
            d.setSuccessTime(rs.getTimestamp("modified"));
            d.setTradeFrom(rs.getString("trade_from"));
            d.setReceiverName(rs.getString("receiver_name"));
            d.setReceiverMobile(rs.getString("receiver_mobile"));
            return d;
        }
    };

    @Override
    public List<RefundOrderDomain> getNotCaredRefundOrders(String dpId) {
        StringBuffer sb = new StringBuffer("select a.tid,a.dp_id,a.oid,a.created,a.modified,a.refund_fee,b.created as tradeCreated,b.customerno,b.receiver_name,b.receiver_mobile,b.trade_from from plt_taobao_refund a" +
                " inner join plt_taobao_order_tc b on a.tid = b.tid");
        sb.append(" left join tb_tc_refund_status c on a.oid = c.oid");
        sb.append(" where a.status = 'SUCCESS' and a.dp_id = '" + dpId + "'");

        //当前时间
        Date curTime = new Date();
        Date startTime = DateUtils.getDateTime(DateUtils.getString(curTime) + " 00:00:00");
        Date endTime = DateUtils.dateEnd(curTime);
        //当日需要关怀的
        sb.append(" and ((a.modified >= '" + DateUtils.getStringDate(startTime) + "' and a.modified <= '" + DateUtils.getStringDate(endTime) + "' and (c.auto_refund_care = 0 or auto_refund_care is null))");

        //前一日需要次日催付的订单
        Date preDay = DateUtils.addDay(curTime, -1);
        String preDate = DateUtils.getStringYMD(preDay);
        sb.append(" or (nextday_senddate = '" + preDate + "' and auto_refund_care = 2))");

        logger.info("退款关怀 sql=[" + sb.toString() + "]");
        return helper.query(sb.toString(), new HashMap<String, Object>(0), mapper);
    }

    @Override
    public void updateRefundCareState(RefundOrderDomain refundOrder,int status,String nextSendDate) {
        String sql = "INSERT INTO tb_tc_refund_status(tid, oid, dp_id, auto_refund_care,nextday_senddate, created,updated) " +
        		"VALUES ('" + refundOrder.getTid() + "','" + refundOrder.getOid() + "','" + refundOrder.getDpId() + "'," + status + ",'" + nextSendDate + "', now(),now()) " +
        				"ON DUPLICATE KEY UPDATE auto_refund_care = " + status + ",nextday_senddate='" + nextSendDate + "', updated = now()";
        helper.update(sql, new HashMap<String, Object>(0));
    }

	@Override
	public List<RefundVO> fidRefunds(RefundQuery refundQuery) {
        String selectCount = "SELECT count(*)";
        String select = "SELECT r.*";
        String from = " FROM plt_taobao_refund r";
        String conditions = " WHERE dp_id = " + refundQuery.getDpId();
        if (refundQuery.getHasGoodReturn() != null) {
                if (refundQuery.getHasGoodReturn()) {
                    conditions += " AND has_good_return = 'true'";
                } else {
                    conditions += " AND has_good_return = 'false'";
                }
        }

        if (!StringUtils.isEmpty(refundQuery.getReason())) {
            conditions += " and reason = '" + refundQuery.getReason() + "'";
        }

        if (!StringUtils.isEmpty(refundQuery.getOrderStatus())) {
            conditions += " and order_status = '" + refundQuery.getOrderStatus() + "'";
        }

        if (!StringUtils.isEmpty(refundQuery.getBuyerNick())) {
            conditions += " and buyer_nick = '" + refundQuery.getBuyerNick() + "'";
        }

        if (!StringUtils.isEmpty(refundQuery.getTid())) {
            conditions += " and tid = '" + refundQuery.getTid() + "'";
        }

        if (!StringUtils.isEmpty(refundQuery.getRefundId())) {
            conditions += " and refund_id = '" + refundQuery.getRefundId() + "'";
        }

        if (!StringUtils.isEmpty(refundQuery.getCreatedStartTime())) {
            conditions += " and created > '" + refundQuery.getCreatedStartTime() + "'";
        }

        if (!StringUtils.isEmpty(refundQuery.getCreatedEndTime())) {
            conditions += " and created < '" + refundQuery.getCreatedEndTime() + "'";
        }

        if (refundQuery.getNeedCustomerService() != null) {
            //1表示肯定，即客服已介入
            if (refundQuery.getNeedCustomerService()) {
                conditions += " AND cs_status != 1";
            } else {
                conditions += " AND cs_status = 1";
            }
        }

        if (!StringUtils.isEmpty(refundQuery.getTitle())) {
            conditions += " and title like '%" + refundQuery.getTitle() + "%'";
        }

        if (!StringUtils.isEmpty(refundQuery.getNumIid())) {
            conditions += " and num_iid like '%" + refundQuery.getNumIid() + "%'";
        }

        if (!StringUtils.isEmpty(refundQuery.getStatus())) {
        	if ("refund".equals(refundQuery.getStatus())) {
        		conditions += " and status in ('" + RefundStatus.SELLER_REFUSE_BUYER.getStatus() + "','" + RefundStatus.WAIT_BUYER_RETURN_GOODS.getStatus() + "','" + RefundStatus.WAIT_SELLER_AGREE.getStatus() + "','" + RefundStatus.WAIT_SELLER_CONFIRM_GOODS.getStatus() + "')";
        	}
            else if ("refundNoSendGoods".equals(refundQuery.getStatus())){
                conditions += " and status in ('" + RefundStatus.SELLER_REFUSE_BUYER.getStatus() + "','" + RefundStatus.WAIT_SELLER_AGREE.getStatus() + "')";
                conditions += " and order_status = '" + OrderStatus.WAIT_SELLER_SEND_GOODS.getStatus() + "'";
            }
            else if ("onlyRefundHasSendGoods".equals(refundQuery.getStatus()    )){
                conditions += " and status in ('" + RefundStatus.SELLER_REFUSE_BUYER.getStatus() + "','" + RefundStatus.WAIT_BUYER_RETURN_GOODS.getStatus() + "','" + RefundStatus.WAIT_SELLER_AGREE.getStatus() + "','" + RefundStatus.WAIT_SELLER_CONFIRM_GOODS.getStatus() + "')";
                conditions += " and has_good_return='false' and order_status = '" + OrderStatus.WAIT_BUYER_CONFIRM_GOODS.getStatus() + "'";
            }
            else if ("returnGoodsWaitSeller".equals(refundQuery.getStatus()    )){
                conditions += " and status in ('" + RefundStatus.SELLER_REFUSE_BUYER.getStatus() + "','" + RefundStatus.WAIT_SELLER_AGREE.getStatus() + "')";
                conditions += " AND has_good_return='true'";
            }
            else if ("returnGoodsWaitBuyer".equals(refundQuery.getStatus()    )){
                conditions += " and status = '" + RefundStatus.WAIT_BUYER_RETURN_GOODS.getStatus() + "'";
                conditions += " AND has_good_return='true'";
            }
            else if ("returnGoodsWaitSellerConfirm".equals(refundQuery.getStatus()    )){
                conditions += " and status = '" + RefundStatus.WAIT_SELLER_CONFIRM_GOODS.getStatus() + "'";
                conditions += " AND has_good_return='true'";
            }
        }

        String limit = " LIMIT " + refundQuery.getStartRow() + "," + refundQuery.getPageSize();

        String sqlCount = selectCount + from + conditions;
        int count = helper.queryForInt(sqlCount, new HashMap<String, Object>(0));
        refundQuery.setTotalItem(count);

        String sql = select + from  + conditions + limit;
        logger.info("sql:{}", sql);

        RowMapper<RefundVO> mapper = new RowMapper<RefundVO>(){
            @Override
            public RefundVO mapRow(ResultSet rs, int paramInt) throws SQLException {
                RefundVO refundVO = new RefundVO();
                refundVO.setBuyerNick(rs.getString("buyer_nick"));
                refundVO.setCsStatus(rs.getInt("cs_status"));
                refundVO.setHasGoodReturn(rs.getBoolean("has_good_return"));
                refundVO.setNumIid(rs.getString("num_iid"));
                refundVO.setOrderStatus(OrderStatus.getMessage(rs.getString("order_status")));
                refundVO.setPayment(rs.getString("total_fee"));
                refundVO.setReason(rs.getString("reason"));
                refundVO.setRefundDesc(rs.getString("refund_desc"));
                refundVO.setRefundFee(rs.getDouble("refund_fee"));
                refundVO.setRefundId(rs.getString("refund_id"));
                refundVO.setStatus(RefundStatus.getMessage(rs.getString("status")));
                refundVO.setTid(rs.getString("tid"));
                refundVO.setOid(rs.getString("oid"));
                refundVO.setTitle(rs.getString("title"));
                refundVO.setTimeout(DateUtils.getStringDate(rs.getTimestamp("timeout")));
                refundVO.setCreated(DateUtils.getStringDate(rs.getTimestamp("created")));
                return refundVO;
            }
        };

        List<RefundVO> refundVOs = helper.query(sql, new HashMap<String, Object>(0), mapper);
        return refundVOs;
	}

	@Override
	public List<Map<String, Object>> statisticsDealWithStatus(String dpId) {

		 String select = "SELECT status, has_good_return, order_status, count(*) AS size";
		 String from = " FROM plt_taobao_refund r";
		 String conditions = " WHERE dp_id = " + dpId;
		 conditions += " and status in ('" + RefundStatus.SELLER_REFUSE_BUYER.getStatus() + "','" + RefundStatus.WAIT_BUYER_RETURN_GOODS.getStatus() + "','" + RefundStatus.WAIT_SELLER_AGREE.getStatus() + "','" + RefundStatus.WAIT_SELLER_CONFIRM_GOODS.getStatus() + "')";
		 String group = " GROUP BY STATUS, has_good_return, order_status";
		 String sql = select + from + conditions + group;

		 logger.info("sql:{}", sql);

		 List<Map<String, Object>> mapList = (List<Map<String, Object>>)helper.queryForList(sql, new HashMap<String, Object>(0));
		return mapList;
	}

	@Override
	public List<RefundDomain> getRefundWarnListByDpId(String dpId, Date lastDealTime, Date nowTime) {
		String sql = "SELECT * FROM plt_taobao_refund WHERE dp_id = :dpId AND created > :lastDealTime AND created < :nowTime";
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("dpId", dpId);
		paramMap.put("lastDealTime", lastDealTime);
		paramMap.put("nowTime", nowTime);

		RowMapper<RefundDomain> mapper = new RowMapper<RefundDomain>(){
            @Override
            public RefundDomain mapRow(ResultSet rs, int paramInt) throws SQLException {
            	RefundDomain refundDomain = new RefundDomain();
            	refundDomain.setRefundFee(rs.getDouble("refund_fee"));
            	refundDomain.setOid(rs.getString("oid"));
            	refundDomain.setStatus(rs.getString("status"));
                return refundDomain;
            }
        };

        List<RefundDomain> refundDomains = helper.query(sql, paramMap, mapper);

//		return helper.queryForList(sql, paramMap, mapper);
        return refundDomains;
	}

    @Override
    public List<Map<String, Object>> refundResonStaticsTask(List<String> shopIdList, String recentStatisticsTime, String statisticsEndTime) {
        String select = "select dp_id, reason from plt_taobao_refund";
        String shopCon = "(";
        for (int i = 0; i < shopIdList.size(); i++) {
            String shopId = shopIdList.get(i);
            shopCon +=  "'"+shopId+"'";
            if (i < shopIdList.size()-1) {
                shopCon += ",";
            }
        }
        shopCon += ")";
        String conditions = " where dp_id in " +  shopCon;
        if (recentStatisticsTime != null) {
            conditions += " and created > '" + recentStatisticsTime + "'";
        }

        if (statisticsEndTime != null) {
            conditions += " and created < '" + statisticsEndTime + "'";
        }

        String group = " GROUP BY dp_id, reason";

        String sql = select + conditions + group;
        logger.info("sql:{}", sql);

        List<Map<String, Object>> mapList = (List<Map<String, Object>>)helper.queryForList(sql, new HashMap<String, Object>(0));

        return mapList;
    }
}
