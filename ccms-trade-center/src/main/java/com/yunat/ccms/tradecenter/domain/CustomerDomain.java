package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yunat.ccms.core.support.json.JsonDateDeserialize;
import com.yunat.ccms.core.support.json.JsonDateSerializer;

@Entity
@Table(name="plt_taobao_customer")
public class CustomerDomain {

	@Id
	private String customerno;

	@Column(name = "full_name")
    private String fullName;

    private String sex;

    @Column(name = "buyer_credit_lev")
    private Integer buyerCreditLev;

    @Column(name = "buyer_credit_score")
    private Integer buyerCreditScore;

    @Column(name = "buyer_credit_good_num")
    private Integer buyerCreditGoodNum;

    @Column(name = "buyer_credit_total_num")
    private Integer buyerCreditTotalNum;

    private String zip;

    private String address;

    private String city;

    private String state;

    private String country;

    private String district;

    @JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserialize.class)
    private Date created;

    @JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserialize.class)
    @Column(name = "last_visit")
    private Date lastVisit;

    private Date birthday;

    @Column(name = "vip_info")
    private String vipInfo;

    private String email;

    private String mobile;

    private String phone;

    @Column(name = "last_sync")
    private Date lastSync;

    private String job;

    @Column(name = "birth_year")
    private Integer birthYear;

    private Date changed;

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getBuyerCreditLev() {
		return buyerCreditLev;
	}

	public void setBuyerCreditLev(Integer buyerCreditLev) {
		this.buyerCreditLev = buyerCreditLev;
	}

	public Integer getBuyerCreditScore() {
		return buyerCreditScore;
	}

	public void setBuyerCreditScore(Integer buyerCreditScore) {
		this.buyerCreditScore = buyerCreditScore;
	}

	public Integer getBuyerCreditGoodNum() {
		return buyerCreditGoodNum;
	}

	public void setBuyerCreditGoodNum(Integer buyerCreditGoodNum) {
		this.buyerCreditGoodNum = buyerCreditGoodNum;
	}

	public Integer getBuyerCreditTotalNum() {
		return buyerCreditTotalNum;
	}

	public void setBuyerCreditTotalNum(Integer buyerCreditTotalNum) {
		this.buyerCreditTotalNum = buyerCreditTotalNum;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(Date lastVisit) {
		this.lastVisit = lastVisit;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getVipInfo() {
		return vipInfo;
	}

	public void setVipInfo(String vipInfo) {
		this.vipInfo = vipInfo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getLastSync() {
		return lastSync;
	}

	public void setLastSync(Date lastSync) {
		this.lastSync = lastSync;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public Integer getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(Integer birthYear) {
		this.birthYear = birthYear;
	}

	public Date getChanged() {
		return changed;
	}

	public void setChanged(Date changed) {
		this.changed = changed;
	}



}
