package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * 店铺统计配置表
 * @author 李卫林
 */

@Entity
@Table(name = "tb_tc_shop_statis_config")
public class ShopStatisConfigDomain extends BaseDomain{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String dpId;
    private Date recentlyOrderStatisTime;
    private Date recentlyUrpayStatisTime;
    private Integer statisInterval;
    private Date created;
    private Date updated;

    @Id
    @Column(name="dp_id")
    public String getDpId() {
        return dpId;
    }
    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    @Column(name="recently_order_statis_time")
    public Date getRecentlyOrderStatisTime() {
        return recentlyOrderStatisTime;
    }
    public void setRecentlyOrderStatisTime(Date recentlyOrderStatisTime) {
        this.recentlyOrderStatisTime = recentlyOrderStatisTime;
    }

    @Column(name="recently_urpay_statis_time")
    public Date getRecentlyUrpayStatisTime() {
        return recentlyUrpayStatisTime;
    }
    public void setRecentlyUrpayStatisTime(Date recentlyUrpayStatisTime) {
        this.recentlyUrpayStatisTime = recentlyUrpayStatisTime;
    }

    @Column(name="statis_interval")
    public Integer getStatisInterval() {
        return statisInterval;
    }
    public void setStatisInterval(Integer statisInterval) {
        this.statisInterval = statisInterval;
    }
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
