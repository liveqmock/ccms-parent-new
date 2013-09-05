package com.yunat.ccms.tradecenter.repository.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.tradecenter.domain.UrpayStatusDomain;
import com.yunat.ccms.tradecenter.repository.UrpayStatusRepository;

/**
 *
 * 催付状态数据接口
 *
 * @author shaohui.li
 * @version $Id: UrpayStatusResitoryImpl.java, v 0.1 2013-6-4 下午05:44:18 shaohui.li Exp $
 */
@Repository
public class UrpayStatusResitoryImpl implements UrpayStatusRepository{

    @PersistenceContext
    private EntityManager em;

    /**
     * 1、支持事务
     * 2、每次只有一个线程能执行，防止多个线程插入同一个tid数据
     * @see
     */
    @Override
    @Transactional
    public synchronized void  insertUrpayStatusBatch(List<UrpayStatusDomain> list,String type) {
        String field = "";
        String updateSql = "";
        if(StringUtils.equals(type, "1")){

            field = "auto_urpay_status,auto_urpay_thread";
            updateSql = "auto_urpay_status = VALUES(auto_urpay_status),auto_urpay_thread=VALUES(auto_urpay_thread)";

        }else if(StringUtils.equals(type, "2")){

            field = "close_urpay_status,close_urpay_thread";
            updateSql = "close_urpay_status = VALUES(close_urpay_status),close_urpay_thread=VALUES(close_urpay_thread)";

        }else if(StringUtils.equals(type, "3")){

            field = "cheap_urpay_status,cheap_urpay_thread";
            updateSql = "cheap_urpay_status = VALUES(cheap_urpay_status),cheap_urpay_thread=VALUES(cheap_urpay_thread)";
        }
        String startSql = "insert into tb_tc_urpay_status (tid," + field + ",created,updated) values";

        String endSql = " on duplicate key update tid = VALUES(tid)," + updateSql + ",updated=now()";

        String allSql = "";
        for (int i = 0; i < list.size(); i++) {
            UrpayStatusDomain d = list.get(i);
            String tempSql = "('" + d.getTid() + "',";
            if(StringUtils.equals(type, "1")){
                tempSql = tempSql + d.getAutoUrpayStatus() + ",'" + d.getAutoUrpayThread() + "',now(),now())";
            }else if(StringUtils.equals(type, "2")){
                tempSql = tempSql + d.getCloseUrpayStatus() + ",'" + d.getCloseUrpayThread() + "',now(),now())";
            }else if(StringUtils.equals(type, "3")){
                tempSql = tempSql + d.getCheapUrpayStatus() + ",'" + d.getCheapUrpayThread() + "',now(),now())";
            }
            allSql = allSql + tempSql + ",";
            if ((i+1) % 200 == 0) {
                if(allSql.length() > 1){
                    allSql = allSql.substring(0, allSql.length() - 1);
                }
                em.createNativeQuery(startSql + allSql + endSql).executeUpdate();
                allSql = "";
            }
        }
        //最后一次处理
        if(allSql.length() > 1){
            allSql = allSql.substring(0, allSql.length() - 1);
            em.createNativeQuery(startSql + allSql + endSql).executeUpdate();
        }
    }

	@Override
	@Transactional
	public synchronized void  saveOrUpdateManualStatus(int manualUrpayStatus, String tid) {

		//查询制定主键是否存在
		UrpayStatusDomain urpayStatusDomain = em.find(UrpayStatusDomain.class, tid);

		if (urpayStatusDomain != null) {
			em.createQuery("update UrpayStatusDomain set manualUrpayStatus = :manualUrpayStatus where tid = :tid")
			  .setParameter("manualUrpayStatus", manualUrpayStatus).setParameter("tid", tid)
			  .executeUpdate();
		} else {
			urpayStatusDomain = new UrpayStatusDomain();
			urpayStatusDomain.setTid(tid);
			urpayStatusDomain.setManualUrpayStatus(manualUrpayStatus);
			urpayStatusDomain.setCreated(new Date());
			urpayStatusDomain.setUpdated(new Date());
			em.persist(urpayStatusDomain);
		}

	}
}
