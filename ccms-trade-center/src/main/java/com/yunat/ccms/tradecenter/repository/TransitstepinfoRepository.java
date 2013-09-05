/**
 *
 */
package com.yunat.ccms.tradecenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yunat.ccms.tradecenter.domain.TransitstepinfoDomain;

import java.util.List;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-18 上午11:56:36
 */
public interface TransitstepinfoRepository extends JpaRepository<TransitstepinfoDomain, String>{

    /**
     * 根据订单id列表获取对应的物流结果
     * @param tids
     * @return
     */
    List<TransitstepinfoDomain> findByTidIn(List<String> tids);
     
}
