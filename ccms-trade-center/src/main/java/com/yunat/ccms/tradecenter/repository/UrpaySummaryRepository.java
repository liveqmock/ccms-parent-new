/**
 *
 */
package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.UrpaySummaryDomain;

/**
 *催付统计数据接口
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-5-31 下午08:41:18
 */
public interface UrpaySummaryRepository extends JpaRepository<UrpaySummaryDomain, Long>{


	/**
     * 根据催付类型获取所有开启此催付类型的催付配置
     *
     * @param urpayType dpId
     * @return
     */
	@Query("from UrpaySummaryDomain u where u.urpayType = :urpayType and u.dpId = :dpId and u.urpayDate>=:urpayDate  ORDER BY u.urpayDate DESC")
	List<UrpaySummaryDomain> queryUrpaySummaryList(@Param("urpayType") Integer urpayType,@Param("dpId") String dpId,@Param("urpayDate") String urpayDate);

	//List<UrpaySummaryDomain> getByUrpayTypeAndDpIdOrderByUrpayDateDesc(Integer urpayType,String dpId);

	/**
     * 取一个统计数据
     *
     * @param urpayType dpId  urpayDate
     * @return
     */
	@Query("from UrpaySummaryDomain where urpayType = :urpayType and dpId = :dpId and urpayDate = :urpayDate")
	List<UrpaySummaryDomain> queryUrpaySummaryDomain(@Param("urpayType") Integer urpayType,@Param("dpId") String dpId,@Param("urpayDate") String urpayDate);

}
