package com.yunat.ccms.tradecenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yunat.ccms.tradecenter.domain.TraderateAutoLogDomain;

/**
 * 
* @Description: 评价事务-自动评价回复日志-Repository
* @author fanhong.meng
* @date 2013-7-15 下午3:44:59 
*
 */
public interface TraderateAutoLogRepository extends JpaRepository<TraderateAutoLogDomain, String>{
	
}
