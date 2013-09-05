/**
 *
 */
package com.yunat.ccms.tradecenter.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-6 下午07:12:44
 */
public interface SmsQueueRepository extends JpaRepository<SmsQueueDomain,Long>{

    @Query("from SmsQueueDomain where type = :urpayType and buyer_nick = :buyer and dp_id = :dpId")
    public List<SmsQueueDomain> querySmsQueueByBuyer(@Param("buyer") String buyer,@Param("urpayType") int urpayType,@Param("dpId") String dpId);

    @Query("from SmsQueueDomain where type = :urpayType and mobile = :mobile and dp_id = :dpId")
    public List<SmsQueueDomain> querySmsQueueByMobile(@Param("mobile") String mobile,@Param("urpayType") int urpayType,@Param("dpId") String dpId);

    @Query("from SmsQueueDomain where dpId = :dpId and created between :startTime and :endTime and type = :urpayType and (mobile = :mobile or  buyer_nick = :buyerNick)")
    public List<SmsQueueDomain> queryByMobileOrBuyer(@Param("mobile") String mobile,@Param("buyerNick") String buyerNick, @Param("urpayType") int urpayType,@Param("dpId") String dpId, @Param("startTime")Date startTime, @Param("endTime")Date endTime);

    @Query("from SmsQueueDomain where send_time is null or send_time <= :send_time")
    public List<SmsQueueDomain> querySmsQueueBySendTime(@Param("send_time") Date send_time);

    @Modifying
    @Transactional
    @Query("update SmsQueueDomain set thread =:thread where (send_time is null or send_time <= :send_time) and thread is null")
    public void updateSmsQueueByThread(@Param("thread") String thread,@Param("send_time") Date send_time);

    @Query("from SmsQueueDomain where thread =:thread")
    public List<SmsQueueDomain> querySmsQueueByThead(@Param("thread") String thread);

    @Modifying
    @Transactional
    @Query("delete from SmsQueueDomain where thread =:thread")
    public void deleteSmsQueueByThread(@Param("thread") String thread);

}
