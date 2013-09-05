package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模糊聊天记录表
 * @author 李卫林
 *
 */
@Entity
@Table(name = "plt_taobao_chat_log")
public class ChatLogDomain extends BaseDomain {


	/**
	 *
	 */
	private static final long serialVersionUID = 4266425442485218829L;

	/**
	 * 主健
	 */
	private Long pkid;

	/**
	 * 店铺id
	 */
	private String dpId;

	/**
	 * 聊天客服id
	 */
	private String serviceStaffId;

	/**
	 * 买家nick
	 */
	private String buyerNick;

	/**
	 * 消息方向
	 * 0:买家->卖家 1:卖家->买家
	 */
	private Integer direction;

	/**
	 * 消息类型
	 * 1 ：自动回复 2: 陌生人消息（等于0是好友消息） 4: 广播消息  8: 最近联系人陌生人消息   16: 离线消息    32: 子账号转发系统消息
	 */
	private Integer chatType;

	/**
	 * 完整消息内容
	 * 当direction=0有效
	 */
	private String content;

	/**
	 * url列表
	 * direction=1有效
	 */
	private String urlLists;

	/**
	 * 商品id列表
	 * 从url列表中解析出来的
	 * direction=1有效
	 */
	private String numIids;

	/**
	 * （关键词，数量）列表
	 *  direction=1有效
	 */
	private String wordLists;

	/**
	 * 消息长度
	 * direction=1有效
	 */
	private Integer length;

	/**
	 * 消息时间（yyyy-MM-dd HH:mm:ss）
	 */
	private Date chatTime;

	@Id
	@GeneratedValue
	public Long getPkid() {
		return pkid;
	}
	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	@Column(name = "dp_id")
	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	@Column(name = "service_staff_id")
	public String getServiceStaffId() {
		return serviceStaffId;
	}
	public void setServiceStaffId(String serviceStaffId) {
		this.serviceStaffId = serviceStaffId;
	}

	@Column(name = "buyer_nick")
	public String getBuyerNick() {
		return buyerNick;
	}
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public Integer getDirection() {
		return direction;
	}
	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	@Column(name = "chat_type")
	public Integer getChatType() {
		return chatType;
	}
	public void setChatType(Integer chatType) {
		this.chatType = chatType;
	}

	@Column(name = "chat_time")
	public Date getChatTime() {
		return chatTime;
	}
	public void setChatTime(Date chatTime) {
		this.chatTime = chatTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "url_lists")
	public String getUrlLists() {
		return urlLists;
	}
	public void setUrlLists(String urlLists) {
		this.urlLists = urlLists;
	}

	@Column(name = "num_iids")
	public String getNumIids() {
		return numIids;
	}
	public void setNumIids(String numIids) {
		this.numIids = numIids;
	}

	@Column(name = "word_lists")
	public String getWordLists() {
		return wordLists;
	}
	public void setWordLists(String wordLists) {
		this.wordLists = wordLists;
	}

	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
}
