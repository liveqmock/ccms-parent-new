package com.yunat.ccms.tradecenter.domain;

import javax.persistence.*;

/**
 * User: weilin.li
 * Date: 13-8-8
 * Time: 下午5:41
 */
@Entity
@Table(name="tb_tc_shop_reason")
public class ShopReasonDomain {
    private Long pkid;

    private String shopId;

    private String shopType;

    private String reason;

    @Id
    @GeneratedValue
    public Long getPkid() {
        return pkid;
    }

    public void setPkid(Long pkid) {
        this.pkid = pkid;
    }

    @Column(name="shop_id")
    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @Column(name="shop_type")
    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
