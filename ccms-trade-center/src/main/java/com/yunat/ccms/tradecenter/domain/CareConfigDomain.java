package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yunat.ccms.core.support.json.JsonDateDeserializeParrten;
import com.yunat.ccms.core.support.json.JsonDateSerializerParrten;

/**
 * 关怀配置表
 *
 * @author teng.zeng date 2013-6-13 下午04:36:23
 */
@Entity
@Table(name = "tb_tc_care_config")
public class CareConfigDomain extends BaseConfigDomain {
	/**
	 *
	 */
	private static final long serialVersionUID = 5043147712034857744L;

	/** 关怀类型4:下单通知，5：发货通知，6：同城通知，7：派件通知，8：签收通知，9：退款关怀，10：确认收货关怀，11：评价关怀 ，12：中差评告警 ，13：退款告警 */
	@Column(name = "care_type")
	private Integer careType;

	/** 催付时间开始时间 **/
	@JsonSerialize(using = JsonDateSerializerParrten.class)
	@JsonDeserialize(using = JsonDateDeserializeParrten.class)
	@Column(name = "care_start_time")
	private Date careStartTime;

	/** 催付时间结束时间 **/
	@JsonSerialize(using = JsonDateSerializerParrten.class)
	@JsonDeserialize(using = JsonDateDeserializeParrten.class)
	@Column(name = "care_end_time")
	private Date careEndTime;

	/** 关怀状态 */
	@Column(name = "care_status")
	private Integer careStatus;

	/** 评价关键字（仅评价使用） */
	@Column(name = "assess_key")
	private String assessKey;

	/** 评价选项（仅评价使用） */
	@Column(name = "assess_option")
	private Integer assessOption;

	/** 关怀时间（仅下单关怀使用）：
	 * 0:下单后关怀
	 * 1:付款后关怀 */
    @Column(name = "care_moment")
    private Integer careMoment;

	public Integer getCareMoment() {
        return careMoment;
    }

    public void setCareMoment(Integer careMoment) {
        this.careMoment = careMoment;
    }

    public Integer getCareType() {
		return careType;
	}

	public void setCareType(Integer careType) {
		this.careType = careType;
	}

	public Date getCareStartTime() {
		return careStartTime;
	}

	public void setCareStartTime(Date careStartTime) {
		this.careStartTime = careStartTime;
	}

	public Date getCareEndTime() {
		return careEndTime;
	}

	public void setCareEndTime(Date careEndTime) {
		this.careEndTime = careEndTime;
	}

	public Integer getCareStatus() {
		return careStatus;
	}

	public void setCareStatus(Integer careStatus) {
		this.careStatus = careStatus;
	}

	public String getAssessKey() {
		return assessKey;
	}

	public void setAssessKey(String assessKey) {
		this.assessKey = assessKey;
	}

	public Integer getAssessOption() {
		return assessOption;
	}

	public void setAssessOption(Integer assessOption) {
		this.assessOption = assessOption;
	}

}
