package com.yunat.ccms.tradecenter.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 评价表实体
 * 
 * @author tim.yin
 * 
 */
@Entity
@Table(name = "plt_taobao_traderate")
public class TraderateDomain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TraderateDomainPK id;

	@Column(name = "dp_id")
	private String dpId;

	@Column(name = "valid_score")
	private Boolean validScore;

	@Column(name = "role")
	private String role;

	@Column(name = "nick")
	private String nick;

	@Column(name = "result")
	private String result;

	@Column(name = "created")
	private Date created;

	@Column(name = "rated_nick")
	private String ratedNick;

	@Column(name = "item_title")
	private String itemTitle;

	@Column(name = "item_price")
	private BigDecimal itemPrice;

	@Column(name = "content")
	private String content;

	@Column(name = "reply")
	private String reply;

	public TraderateDomain() {

	}

	public TraderateDomain(TraderateDomainPK id, String dpId, Boolean validScore, String role, String nick,
			String result, Date created, String ratedNick, String itemTitle, BigDecimal itemPrice, String content,
			String reply) {
		super();
		this.id = id;
		this.dpId = dpId;
		this.validScore = validScore;
		this.role = role;
		this.nick = nick;
		this.result = result;
		this.created = created;
		this.ratedNick = ratedNick;
		this.itemTitle = itemTitle;
		this.itemPrice = itemPrice;
		this.content = content;
		this.reply = reply;
	}

	public TraderateDomainPK getId() {
		return id;
	}

	public void setId(TraderateDomainPK id) {
		this.id = id;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	public Boolean getValidScore() {
		return validScore;
	}

	public void setValidScore(Boolean validScore) {
		this.validScore = validScore;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getRatedNick() {
		return ratedNick;
	}

	public void setRatedNick(String ratedNick) {
		this.ratedNick = ratedNick;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

}
