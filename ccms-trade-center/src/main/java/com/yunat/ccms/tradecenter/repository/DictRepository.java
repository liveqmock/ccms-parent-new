package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yunat.ccms.tradecenter.domain.DictDomain;

/**
 * 字典表
 *
 * @author teng.zeng
 * date 2013-5-31 下午04:53:04
 */
public interface DictRepository extends JpaRepository<DictDomain, Long> {

	DictDomain getByTypeAndCode(Integer type, String code);

	List<DictDomain> getByType(Integer type);
}
