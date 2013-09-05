package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yunat.ccms.core.support.json.JsonDateDeserialize;
import com.yunat.ccms.core.support.json.JsonDateSerializer;

/**
 * 关怀表
 *
 * @author xin.chen date 2013-7-10
 */
@Entity
@Table(name = "tb_tc_caring_detail")
public class CaringDetailDomain extends BaseDomain {

	/**
	 *
	 */
	private static final long serialVersionUID = -508520370032694488L;

	/**主键 **/
    @Id
    @GeneratedValue
    @Column(name = "pkid")
    private Long pkid;

	/** 订单号 */
	@Column(name = "tid")
	private String tid;

	/** 子订单号 */
	@Column(name = "oid")
	private String oid;

	/** 店铺id */
	@Column(name = "dp_id")
	private String dpId;

	@Column(name = "customerno")
	private String customerno;

	@Column(name = "caring_type")
	private Integer caringType;

	@Column(name = "gateway_id")
	private Long gatewayId;

	@Column(name = "content")
	private String content;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserialize.class)
	@Column(name = "created")
	private Date created;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserialize.class)
	@Column(name = "updated")
	private Date updated;

	@Column(name = "caringperson")
	private String caringperson;

	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	public Integer getCaringType() {
		return caringType;
	}

	public void setCaringType(Integer caringType) {
		this.caringType = caringType;
	}


	public Long getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(Long gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getCaringperson() {
		return caringperson;
	}

	public void setCaringperson(String caringperson) {
		this.caringperson = caringperson;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
