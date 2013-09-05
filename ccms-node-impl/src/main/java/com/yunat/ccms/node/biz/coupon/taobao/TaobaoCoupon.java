package com.yunat.ccms.node.biz.coupon.taobao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yunat.ccms.auth.login.taobao.TaobaoShop;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.core.support.json.JsonDateSerializer;

@Entity
@Table(name = "plt_taobao_coupon")
public class TaobaoCoupon implements Serializable {

	/***  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "coupon_id", nullable = false)
	private Long couponId;

	@Column(name = "coupon_name", nullable = false, length = 50)
	private String couponName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "shop_id", referencedColumnName = "shop_id", nullable = false)
	private TaobaoShop shop;

	/*** 优惠券条件(元),为null代表没有限制 */
	@Column(name = "threshold", nullable = true)
	private Long threshold;

	/*** 优惠券面额 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "denomination_value", referencedColumnName = "denomination_value", nullable = false)
	private TaobaoCouponDenomination denomination;

	/*** 生效日期 */
	@JsonSerialize(using = JsonDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time", nullable = true)
	private Date startTime;

	/*** 截止日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonDateSerializer.class)
	@Column(name = "end_time", nullable = true)
	private Date endTime;

	/*** 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonDateSerializer.class)
	@Column(name = "created_time", nullable = true)
	private Date createTime;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "creator", referencedColumnName = "id", nullable = true)
	private User creator;

	/*** 在CCMS优惠券是否启用 */
	@Column(name = "enable", columnDefinition = "TINYINT(1)")
	private boolean enable;

	/*** 备注 */
	@Column(name = "remark", nullable = false, length = 512)
	private String remark;

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public TaobaoShop getShop() {
		return shop;
	}

	public void setShop(TaobaoShop shop) {
		this.shop = shop;
	}

	public Long getThreshold() {
		return threshold;
	}

	public void setThreshold(Long threshold) {
		this.threshold = threshold;
	}

	public TaobaoCouponDenomination getDenomination() {
		return denomination;
	}

	public void setDenomination(TaobaoCouponDenomination denomination) {
		this.denomination = denomination;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createdTime) {
		this.createTime = createdTime;
	}

	/*** 在淘宝平台该优惠券是否有效 */
	public boolean isAvailable() {
		Date now = new Date();
		return now.before(this.endTime);
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@Override
	public String toString() {
		return "TaobaoCoupon [couponId=" + couponId + ", couponName=" + couponName + ", shop=" + shop + ", threshold="
				+ threshold + ", denomination=" + denomination + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", createTime=" + createTime + "]";
	}

}
