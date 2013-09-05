package com.yunat.ccms.tradecenter.repository;

import org.springframework.data.repository.CrudRepository;

import com.yunat.ccms.tradecenter.domain.RefundProofFileDomain;

/**
 * 常用凭证操作
 *
 * @author ming.peng
 * @date 2013-7-18
 * @since 4.2.0
 */
public interface RefundProofFileRepository extends CrudRepository<RefundProofFileDomain, Long> {

}
