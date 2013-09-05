package com.yunat.ccms.tradecenter.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yunat.ccms.channel.support.vo.GatewayInfoResponse;
import com.yunat.ccms.core.support.json.JsonDateDeserialize;
import com.yunat.ccms.core.support.json.JsonDateSerializer;

/**
 *
 *  所有配置的基类
 *
 * @author shaohui.li
 * @version $Id: BaseConfigDomain.java, v 0.1 2013-6-7 下午12:11:51 shaohui.li Exp $
 */
@MappedSuperclass
public class BaseConfigDomain extends BaseDomain{

    /**  */
    private static final long serialVersionUID = -8774130646634525954L;

    /**主键 **/
    @Id
    @GeneratedValue
    @Column(name = "pkid")
    private Long pkid;

    /**数据创建时间 **/
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserialize.class)
    @Column(name = "created")
    private Date created;

    /**数据更新时间 **/
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserialize.class)
    @Column(name = "updated")
    private Date updated;


    /**订单范围开始时间 **/
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserialize.class)
    @Column(name = "start_date")
    private Date startDate;

    /**订单范围结束时间 **/
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserialize.class)
    @Column(name = "end_date")
    private Date endDate;

    /** 订单范围类型(日期类型：1 自定义类型，0非自定义类型) **/
    @Column(name = "date_type")
    private Integer dateType;

    /** 如果非自定义类型，根据改字段反推算 1：开启一天,0：持续开启(从现在开启到无穷大), 7:今天后头推七天，填充开始结束时间 **/
    @Column(name = "date_number")
    private Integer dateNumber;

    /** 催付选项 **/
    @Column(name = "notify_option")
    private Integer notifyOption;

    /** 催付订单最小金额**/
    @Column(name = "order_minamount")
    private Double orderMinAcount;

    /** 催付订单最大金额 **/
    @Column(name = "order_maxamount")
    private Double orderMaxAcount;

    /** 过滤条件**/
    @Column(name = "filter_condition")
    private String filterCondition;

    /** 过滤条件列表 **/
    @Transient
    private List<DictDomain> filterConditionList;

    /**会员等级 **/
    @Column(name = "member_grade")
    private String memberGrade;

    /** 会员等级列表 **/
    @Transient
    private List<DictDomain> memberGradeList;

    /** 商品 **/
    @Column(name = "goods")
    private String goods;

    /** 排除商品标识 **/
    @Column(name = "exclude_goods")
    private Integer excludeGoods;

    /** 是否包含聚划算(1:是，0：否) **/
    @Column(name = "include_cheap")
    private Integer includeCheap;

    /** 短信内容 **/
    @Column(name = "sms_content")
    private String smsContent;

    /** 通道id **/
    @Column(name = "gateway_id")
    private Integer gatewayId;

    /** 通道数据列表 */
    @Transient
    private List<GatewayInfoResponse> gatewayList;

    /** 是否打开催付功能(1:开,0:关) **/
    @Column(name = "is_open")
    private Integer isOpen;

    /** 总开关，是否关闭(1:开，0:关闭)**/
    @Column(name = "is_switch")
    private Integer isSwitch;

    /** CCMS用户 **/
    @Column(name = "user_name")
    private String userName;

    /** 平台名称 **/
    @Column(name = "plat_name")
    private String platName;

    /** 操作人**/
    @Column(name = "op_user")
    private String opUser;

    /** 店铺id **/
    @Column(name = "dp_id")
    private String dpId;

    @Transient
    private ConfigLogDomain log;

    public ConfigLogDomain getLog() {
        return log;
    }

    public void setLog(ConfigLogDomain log) {
        this.log = log;
    }

    public List<GatewayInfoResponse> getGatewayList() {
		return gatewayList;
	}

	public void setGatewayList(List<GatewayInfoResponse> gatewayList) {
		this.gatewayList = gatewayList;
	}

	public Long getPkid() {
        return pkid;
    }

    public void setPkid(Long pkid) {
        this.pkid = pkid;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getDateType() {
        return dateType;
    }

    public void setDateType(Integer dateType) {
        this.dateType = dateType;
    }

    public Integer getDateNumber() {
        return dateNumber;
    }

    public void setDateNumber(Integer dateNumber) {
        this.dateNumber = dateNumber;
    }

    public Integer getNotifyOption() {
        return notifyOption;
    }

    public void setNotifyOption(Integer notifyOption) {
        this.notifyOption = notifyOption;
    }

    public Double getOrderMinAcount() {
        return orderMinAcount;
    }

    public void setOrderMinAcount(Double orderMinAcount) {
        this.orderMinAcount = orderMinAcount;
    }

    public Double getOrderMaxAcount() {
        return orderMaxAcount;
    }

    public void setOrderMaxAcount(Double orderMaxAcount) {
        this.orderMaxAcount = orderMaxAcount;
    }

    public String getFilterCondition() {
        return filterCondition;
    }

    public void setFilterCondition(String filterCondition) {
        this.filterCondition = filterCondition;
    }

    public List<DictDomain> getFilterConditionList() {
        return filterConditionList;
    }

    public void setFilterConditionList(List<DictDomain> filterConditionList) {
        this.filterConditionList = filterConditionList;
    }

    public String getMemberGrade() {
        return memberGrade;
    }

    public void setMemberGrade(String memberGrade) {
        this.memberGrade = memberGrade;
    }

    public List<DictDomain> getMemberGradeList() {
        return memberGradeList;
    }

    public void setMemberGradeList(List<DictDomain> memberGradeList) {
        this.memberGradeList = memberGradeList;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public Integer getExcludeGoods() {
        return excludeGoods;
    }

    public void setExcludeGoods(Integer excludeGoods) {
        this.excludeGoods = excludeGoods;
    }

    public Integer getIncludeCheap() {
        return includeCheap;
    }

    public void setIncludeCheap(Integer includeCheap) {
        this.includeCheap = includeCheap;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public Integer getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(Integer gateWayId) {
        this.gatewayId = gateWayId;
    }

    public Integer getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }

    public Integer getIsSwitch() {
        return isSwitch;
    }

    public void setIsSwitch(Integer isSwitch) {
        this.isSwitch = isSwitch;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPlatName() {
        return platName;
    }

    public void setPlatName(String platName) {
        this.platName = platName;
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
}
