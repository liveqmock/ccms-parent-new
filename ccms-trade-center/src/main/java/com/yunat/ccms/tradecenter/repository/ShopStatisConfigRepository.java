package com.yunat.ccms.tradecenter.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.ShopStatisConfigDomain;

/**
 *  买家交互统计数据库接口
 *
 * @author 李卫林
 *
 */
public interface ShopStatisConfigRepository extends CrudRepository<ShopStatisConfigDomain, String>{

    /**
     * 通过店铺id获取
     * @param dpId
     * @return
     */
    ShopStatisConfigDomain getByDpId(String dpId);

    @Modifying
    @Query("update ShopStatisConfigDomain set  recentlyOrderStatisTime = :recentlyOrderStatisTime, updated=now() where dpId = :dpId")
    void updateOrderTime(@Param("dpId")String dpId, @Param("recentlyOrderStatisTime")Date recentlyOrderStatisTime);

    @Modifying
    @Query("update ShopStatisConfigDomain set recentlyUrpayStatisTime = :recentlyUrpayStatisTime, updated=now() where dpId = :dpId")
    void updateUrpayTime(@Param("dpId")String dpId, @Param("recentlyUrpayStatisTime")Date recentlyUrpayStatisTime);

    @Modifying
    @Query("update ShopStatisConfigDomain set recentlyOrderStatisTime = :recentlyOrderStatisTime, recentlyUrpayStatisTime = :recentlyUrpayStatisTime, updated=now() where dpId = :dpId")
    void updateTime(@Param("dpId")String dpId, @Param("recentlyOrderStatisTime")Date recentlyOrderStatisTime, @Param("recentlyUrpayStatisTime")Date recentlyUrpayStatisTime);
}
