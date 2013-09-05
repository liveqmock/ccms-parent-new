package com.yunat.ccms.channel.support.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_channel")
public class Channel implements Serializable {

	private static final long serialVersionUID = -3001137277190224652L;

	private Long channelId;
	private String channelName;
	private Long channelType;
	private Double channelPrice;
	private String channelDesc;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "channel_id", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Column(name = "channel_name")
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	@Column(name = "channel_type")
	public Long getChannelType() {
		return channelType;
	}

	public void setChannelType(Long channelType) {
		this.channelType = channelType;
	}

	@Column(name = "channel_price")
	public Double getChannelPrice() {
		return channelPrice;
	}

	public void setChannelPrice(Double channelPrice) {
		this.channelPrice = channelPrice;
	}

	@Column(name = "channel_desc")
	public String getChannelDesc() {
		return channelDesc;
	}

	public void setChannelDesc(String channelDesc) {
		this.channelDesc = channelDesc;
	}

}