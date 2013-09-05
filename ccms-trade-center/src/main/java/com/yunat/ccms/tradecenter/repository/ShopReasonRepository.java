package com.yunat.ccms.tradecenter.repository;

import com.yunat.ccms.tradecenter.domain.PropertiesConfigDomain;
import com.yunat.ccms.tradecenter.domain.ShopReasonDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * User: weilin.li
 * Date: 13-8-8
 * Time: 下午5:44
 */
public interface ShopReasonRepository extends JpaRepository<ShopReasonDomain, Long> {

    public List<ShopReasonDomain> findByShopId(String shopId);

    public List<ShopReasonDomain> findByShopType(String shopType);

    public List<ShopReasonDomain> findByShopIdIn(List<String> shopIds);

}
