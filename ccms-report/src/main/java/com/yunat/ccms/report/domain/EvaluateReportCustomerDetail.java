package com.yunat.ccms.report.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yunat.ccms.core.support.json.JsonDateSerializer;

/**
 * 评估节点-客户明细-实体
 * 
 * @author yin
 * 
 */
@Entity
@Table(name = "twf_node_evaluate_customer_detail")
public class EvaluateReportCustomerDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2451484740053161763L;

	// 非业务字段主键ID
	private Long evaluateCustomerId;

	private Long jobId;

	private Long nodeId;

	private String evaluateTime;

	private String uniId;

	private String fullName;

	private String sex;

	private Date birthday;

	private String state;

	private String city;

	private String district;

	private String vipInfo;

	private String mobile;

	private String email;

	private String buyerCreditLev;

	private String goodRate;

	private String address;

	private String zip;

	private String job;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evaluate_customer_id", unique = true, nullable = false, precision = 20, scale = 0)
	public Long getEvaluateCustomerId() {
		return evaluateCustomerId;
	}

	public void setEvaluateCustomerId(Long evaluateCustomerId) {
		this.evaluateCustomerId = evaluateCustomerId;
	}

	@Column(name = "job_id")
	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	@Column(name = "node_id")
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "evaluate_time")
	public String getEvaluateTime() {
		return evaluateTime;
	}

	public void setEvaluateTime(String evaluateTime) {
		this.evaluateTime = evaluateTime;
	}

	@Column(name = "uni_id")
	public String getUniId() {
		return uniId;
	}

	public void setUniId(String uniId) {
		this.uniId = uniId;
	}

	@Column(name = "full_name")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name = "sex")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	@Column(name = "birthday")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name = "state")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "district")
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Column(name = "vip_info")
	public String getVipInfo() {
		return vipInfo;
	}

	public void setVipInfo(String vipInfo) {
		this.vipInfo = vipInfo;
	}

	@Column(name = "mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "buyer_credit_lev")
	public String getBuyerCreditLev() {
		return buyerCreditLev;
	}

	public void setBuyerCreditLev(String buyerCreditLev) {
		this.buyerCreditLev = buyerCreditLev;
	}

	@Column(name = "good_rate")
	public String getGoodRate() {
		return goodRate;
	}

	public void setGoodRate(String goodRate) {
		this.goodRate = goodRate;
	}

	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "zip")
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Column(name = "job")
	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.evaluateCustomerId != null ? this.evaluateCustomerId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the
		// evaluateResultId fields are not set
		if (!(object instanceof EvaluateReportResult)) {
			return false;
		}
		EvaluateReportCustomerDetail other = (EvaluateReportCustomerDetail) object;
		if (this.evaluateCustomerId != other.evaluateCustomerId
				&& (this.evaluateCustomerId == null || !this.evaluateCustomerId.equals(other.evaluateCustomerId)))
			return false;
		return true;
	}

}
