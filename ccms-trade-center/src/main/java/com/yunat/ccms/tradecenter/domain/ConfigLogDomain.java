package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 配置日志表
 *
 * @author teng.zeng date 2013-6-6 下午02:19:55
 */
@Entity
@Table(name = "tb_tc_config_log")
public class ConfigLogDomain extends BaseDomain {

	/**
	 *
	 */
	private static final long serialVersionUID = 1333460586818419746L;

	/** 主键 **/
	@Id
	@GeneratedValue
	@Column(name = "pkid")
	private Long pkid;

	/**
	 * 操作类型： 催付类： 1：自动催付 2：预关闭催付 3：聚划算催付 关怀类：4：下单关怀 5：发货通知 6：同城通知 7：派件通知 8：签收通知
	 * 9 ：退款关怀 10：确认收货关怀： 11：评价关怀  告警12：中差评告警 13：退款告警
	 */
	@Column(name = "type")
	private Integer type;

	/** 操作人 */
	@Column(name = "op_user")
	private String opUser;

	/** 店铺id */
	@Column(name = "dp_id")
	private String dpId;

	/** 配置内容 */
	@Column(name = "content")
	private String content;

	/** 数据创建时间 */
	@Column(name = "created")
	private Date created;

	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getOpUser() {
		return opUser;
	}

	public void setOpUser(String opUser) {
		this.opUser = opUser;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
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

}
