package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.MemberDomain;
import com.yunat.ccms.tradecenter.domain.MemberDomainPK;

/**
 * 会员等级数据接口
 *
 * @author shaohui.li
 * @version $Id: MemberGradeRepository.java, v 0.1 2013-6-3 下午04:18:05 shaohui.li Exp $
 */
public interface MemberGradeRepository extends JpaRepository<MemberDomain, MemberDomainPK>{

    /**
     *
     *根据店铺和买家昵称查询会员等级
     * @param dpId:店铺Id
     * @param customerNo:买家昵称
     * @return
     */
	@Query("from MemberDomain where dp_id = :dpId and customerno = :customerNo")
	MemberDomain getMemberByDpIdAndCustomerNo(@Param("dpId") String dpId,@Param("customerNo") String customerNo);

	/**
	 * 根据会员名查询会员店铺等级
	 * @param dpId 店铺Id
	 * @param customernos 买家昵称列表
	 * @return
	 */
	@Query("from MemberDomain where id.dpId = :dpId and customerno in (:customernos)")
	List<MemberDomain> getMembersByCustomers(@Param("dpId")String dpId, @Param("customernos")List<String> customernos);
}
