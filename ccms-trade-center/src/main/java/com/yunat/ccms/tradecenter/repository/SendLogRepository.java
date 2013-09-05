package com.yunat.ccms.tradecenter.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.SendLogDomain;

/**
 * 买家交互统计数据库接口
 * 
 * @author 李卫林
 * 
 */
public interface SendLogRepository extends JpaRepository<SendLogDomain, Long> {

	@Query("from SendLogDomain where dpId = :dpId and created between :startTime and :endTime and type in (:types)")
	List<SendLogDomain> getByDpId(@Param("dpId") String dpId, @Param("startTime") Date startTime,
			@Param("endTime") Date endTime, @Param("types") List<Integer> types);

	@Query("from SendLogDomain where dpId = :dpId and created between :startTime and :endTime and type in (:types) and buyerNick in (:buyerNicks)")
	List<SendLogDomain> getByDpIdAndCus(@Param("dpId") String dpId, @Param("startTime") Date startTime,
			@Param("endTime") Date endTime, @Param("types") List<Integer> types,
			@Param("buyerNicks") List<String> buyerNicks);

	/**
	 * 根据买家获取短信发送历史
	 * 
	 * @param dpId
	 *            :店铺Id
	 * @param sendDate
	 *            :发送日期
	 * @param buyer
	 *            ：买家昵称
	 * @return
	 */
	@Query("from SendLogDomain where dpId = :dpId and buyer_nick = :buyer and created >= :startDate and created <= :endDate and type = :urpayOrCareType")
	List<SendLogDomain> getSmsLogByBuyer(@Param("dpId") String dpId, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate, @Param("buyer") String buyer, @Param("urpayOrCareType") int urpayOrCareType);

	/**
	 * 根据手机号码获取短信发送历史
	 * 
	 * @param dpId
	 *            :店铺Id
	 * @param sendDate
	 *            :发送日期
	 * @param mobile
	 *            ：手机号码
	 * @return
	 */
	@Query("from SendLogDomain where dpId = :dpId and mobile = :mobile and created >= :startDate and created <= :endDate and type = :urpayOrCareType")
	List<SendLogDomain> getSmsLogByMobile(@Param("dpId") String dpId, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate, @Param("mobile") String mobile,
			@Param("urpayOrCareType") int urpayOrCareType);

	/**
	 * 根据手机号码获取短信发送历史
	 * 
	 * @param dpId
	 *            :店铺Id
	 * @param sendDate
	 *            :发送日期
	 * @param mobile
	 *            ：手机号码
	 * @return
	 */
	@Query("from SendLogDomain where dpId = :dpId and (mobile = :mobile or buyerNick = :buyerNick) and created between :startTime and :endTime and type = :urpayOrCareType")
	List<SendLogDomain> getSmsLogByMobileOrBuyer(@Param("dpId") String dpId, @Param("startTime") Date startTime,
			@Param("endTime") Date endTime, @Param("mobile") String mobile, @Param("buyerNick") String buyerNick,
			@Param("urpayOrCareType") int urpayOrCareType);

	/**
	 * 获取下发记录更加订单id列表
	 * 
	 * @param tids
	 *            订单id列表
	 * @param types
	 *            TODO
	 * @param dpId
	 *            店铺id
	 * @return
	 */
	@Query("from SendLogDomain where tid in (:tids) and type in (:types) and sendStatus = 1")
	List<SendLogDomain> getSendLogByTids(@Param("tids") List<String> tids, @Param("types") List<Integer> types);

	/**
	 * 获取评价事务关怀记录集
	 */
	List<SendLogDomain> findByTidAndOid(String tid, String oid);

}
