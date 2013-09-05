package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 客服交互信息.
 *
 * @author ming.peng
 * @date 2013-6-3
 * @since 4.1.0
 */
@Entity
@Table(name = "tb_tc_customer_orders_ship")
public class CustomerOrdersShipDomain  extends BaseDomain {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3832701826431074805L;

	/** 主键. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pkid")
	private Long pkid;

	/** 客服昵称. */
	@Column(name = "service_staff_id")
	private String serviceStaffName;

	/** 店铺ID. */
	@Column(name = "dp_id")
	private String dpId;

	/** 订单ID. */
	@Column(name = "tid")
	private String tid;

	/** 处理日期. */
	@Column(name = "order_created")
	private Date orderCreated;

	/** The created. */
	@Column(name = "created")
	private Date created;

	/** The updated. */
	@Column(name = "updated")
	private Date updated;

	@Column(name = "is_hide")
	private Boolean isHide;

	/** 发货事务隐藏. */
	@Column(name = "sendgoods_hide")
	private Boolean sendgoodsHide;

	/** 物流事务隐藏. */
	@Column(name = "logistics_hide")
	private Boolean logisticsHide;

	/** 发货事务 关怀状态. */
	@Column(name = "sendgoods_care_status")
	private Integer sendgoodsCareStatus;

	/** 物流事务 关怀状态. */
	@Column(name = "logistics_care_status")
	private Integer logisticsCareStatus;

    @Column(name = "order_followup")
    private Boolean orderFollowup;

	   /** 发货事务隐藏. */
    @Column(name = "sendgoods_followup")
    private Boolean sendgoodsFollowUp;

    @Column(name = "logistics_followup")
    private Boolean logisticsFollowup;

	public Boolean getSendgoodsFollowUp() {
        return sendgoodsFollowUp;
    }

    public void setSendgoodsFollowUp(Boolean sendgoodsFollowUp) {
        this.sendgoodsFollowUp = sendgoodsFollowUp;
    }

    public Boolean getIsHide() {
		return isHide;
	}

	public void setIsHide(Boolean isHide) {
		this.isHide = isHide;
	}

	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public String getServiceStaffName() {
		return serviceStaffName;
	}

	public void setServiceStaffName(String serviceStaffName) {
		this.serviceStaffName = serviceStaffName;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public Boolean getSendgoodsHide() {
		return sendgoodsHide;
	}

	public void setSendgoodsHide(Boolean sendgoodsHide) {
		this.sendgoodsHide = sendgoodsHide;
	}

	public Boolean getLogisticsHide() {
		return logisticsHide;
	}

	public void setLogisticsHide(Boolean logisticsHide) {
		this.logisticsHide = logisticsHide;
	}

	public Integer getSendgoodsCareStatus() {
		return sendgoodsCareStatus;
	}

	public void setSendgoodsCareStatus(Integer sendgoodsCareStatus) {
		this.sendgoodsCareStatus = sendgoodsCareStatus;
	}

	public Integer getLogisticsCareStatus() {
		return logisticsCareStatus;
	}

	public void setLogisticsCareStatus(Integer logisticsCareStatus) {
		this.logisticsCareStatus = logisticsCareStatus;
	}

	public Date getOrderCreated() {
		return orderCreated;
	}

	public void setOrderCreated(Date orderCreated) {
		this.orderCreated = orderCreated;
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

    public Boolean getOrderFollowup() {
        return orderFollowup;
    }

    public void setOrderFollowup(Boolean orderFollowup) {
        this.orderFollowup = orderFollowup;
    }

    public Boolean getLogisticsFollowup() {
        return logisticsFollowup;
    }

    public void setLogisticsFollowup(Boolean logisticsFollowup) {
        this.logisticsFollowup = logisticsFollowup;
    }
}
