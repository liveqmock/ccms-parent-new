package com.yunat.ccms.channel.support.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yunat.ccms.core.support.json.JsonDateSerializer;

@Entity
@Table(name = "tw_mobile_blacklist")
public class MobileBlackList implements BlackList {

	private String contact; // 联系方式
	private String source; // 名单来源
	private Date created;

	public MobileBlackList() {

	}

	public MobileBlackList(String contact, String source, Date created) {
		super();
		this.contact = contact;
		this.source = source;
		this.created = created;
	}

	@Id
	@Column(name = "mobile", unique = true, nullable = false)
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Column(name = "get_from")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	@Column(name = "created")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
