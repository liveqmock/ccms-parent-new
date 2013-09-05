package com.yunat.ccms.tradecenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yunat.ccms.tradecenter.domain.ConfigLogDomain;

/**
 * 任务配置日志记录操作
 *
 * @author teng.zeng
 * date 2013-6-14 下午02:42:12
 */
public interface ConfigLogRepository extends JpaRepository<ConfigLogDomain, Long>{

}
