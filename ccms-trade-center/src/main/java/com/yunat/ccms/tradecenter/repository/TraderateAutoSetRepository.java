package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.TraderateAutoSetDomain;

/**
 * 
* @Description: TODO 评价事务-自动评价设置 Repository
* @author fanhong.meng
* @date 2013-7-12 下午5:05:27 
*
*/
public interface TraderateAutoSetRepository extends JpaRepository<TraderateAutoSetDomain, Long>{
	
	/**
	 * 获取 已开启或已关闭的自动评价设置
	 * @param status 开启或关闭
	 * @return
	 */
	@Query("from TraderateAutoSetDomain where status = :status")
	public List<TraderateAutoSetDomain> findTraderateAutoSet(@Param("status") Integer status);
	
	@Query("select u from TraderateAutoSetDomain u where u.dp_id = :dpId")
	public TraderateAutoSetDomain getTraderateAutoSet(@Param("dpId") String dpId);

}
