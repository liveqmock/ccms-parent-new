package com.yunat.ccms.node.biz.sms;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.yunat.ccms.channel.support.ChannelEntity;
import com.yunat.ccms.channel.support.ChannelType;
import com.yunat.ccms.core.support.annotation.Descriptor;

@Entity
@Table(name = "twf_node_sms")
@Descriptor(type = NodeSMS.TYPE, 
		validatorClass = com.yunat.ccms.node.biz.sms.NodeSMSValidator.class,
		handlerClass = com.yunat.ccms.node.biz.sms.NodeSMSHandler.class,
		processorClass = com.yunat.ccms.node.biz.sms.NodeSMSProcessor.class)
public class NodeSMS implements Serializable, ChannelEntity {

	private static final long serialVersionUID = -6854407274261579861L;
	public static final String TYPE = "tcommunicateSMS";

	private Long nodeId;
	private String name;
	private Boolean unsubscribeEnabled; // 可否退订
	private Boolean blacklistDisabled; // 屏蔽黑名单
	private Boolean redlistEnabled; // 发送红名单
	private Integer deliveryChannelId; // 发送通道Id
	private String testPhoneString; // 测试执行号码串
	private String deliveryCategory; // 发送类别 @see #DeliveryCategory

	private String messageValue; // 信息内容

	private String phoneNumSource; // 手机号码来源
	private String outputControl; // 输出控制
	private String deliverySelection; // 发送方式选择
	private String assignDeliveryDate; // 指定发送日期
	private String assignDeliveryTime; // 指定发送的时分
	private Boolean overAssignDeliveryPeriod; // 是否超过预设时间
	private String remark; // 备注信息

	private Boolean samplingEnabled; // 可抽样
	private Integer samplingCopies; // 抽样份数
	private Date created; // 创建时间

	@Id
	@Column(name = "node_id")
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "unsubscribe_enabled", columnDefinition = "BOOLEAN")
	public Boolean getUnsubscribeEnabled() {
		return unsubscribeEnabled;
	}

	public void setUnsubscribeEnabled(Boolean unsubscribeEnabled) {
		this.unsubscribeEnabled = unsubscribeEnabled;
	}

	@Column(name = "blacklist_disabled", columnDefinition = "BOOLEAN")
	public Boolean getBlacklistDisabled() {
		return blacklistDisabled;
	}

	public void setBlacklistDisabled(Boolean blacklistDisabled) {
		this.blacklistDisabled = blacklistDisabled;
	}

	@Column(name = "redlist_enabled", columnDefinition = "BOOLEAN")
	public Boolean getRedlistEnabled() {
		return redlistEnabled;
	}

	public void setRedlistEnabled(Boolean redlistEnabled) {
		this.redlistEnabled = redlistEnabled;
	}

	@Column(name = "delivery_channel_id")
	public Integer getDeliveryChannelId() {
		return deliveryChannelId;
	}

	public void setDeliveryChannelId(Integer deliveryChannelId) {
		this.deliveryChannelId = deliveryChannelId;
	}

	@Column(name = "test_phone_string")
	public String getTestPhoneString() {
		return testPhoneString;
	}

	@Column(name = "delivery_category")
	public String getDeliveryCategory() {
		return deliveryCategory;
	}

	public void setDeliveryCategory(String deliveryCategory) {
		this.deliveryCategory = deliveryCategory;
	}

	public void setTestPhoneString(String testPhoneString) {
		this.testPhoneString = testPhoneString;
	}

	@Column(name = "message_value", columnDefinition = "TEXT")
	public String getMessageValue() {
		return messageValue;
	}

	public void setMessageValue(String messageValue) {
		this.messageValue = messageValue;
	}

	@Column(name = "phone_num_source")
	public String getPhoneNumSource() {
		return phoneNumSource;
	}

	public void setPhoneNumSource(String phoneNumSource) {
		this.phoneNumSource = phoneNumSource;
	}

	@Column(name = "output_control")
	public String getOutputControl() {
		return outputControl;
	}

	public void setOutputControl(String outputControl) {
		this.outputControl = outputControl;
	}

	@Column(name = "delivery_selection")
	public String getDeliverySelection() {
		return deliverySelection;
	}

	public void setDeliverySelection(String deliverySelection) {
		this.deliverySelection = deliverySelection;
	}

	@Column(name = "assign_delivery_date")
	public String getAssignDeliveryDate() {
		return assignDeliveryDate;
	}

	public void setAssignDeliveryDate(String assignDeliveryDate) {
		this.assignDeliveryDate = assignDeliveryDate;
	}

	@Column(name = "assign_delivery_time")
	public String getAssignDeliveryTime() {
		return assignDeliveryTime;
	}

	public void setAssignDeliveryTime(String assignDeliveryTime) {
		this.assignDeliveryTime = assignDeliveryTime;
	}

	@Column(name = "over_assign_delivery_period", columnDefinition = "BOOLEAN")
	public Boolean getOverAssignDeliveryPeriod() {
		return overAssignDeliveryPeriod;
	}

	public void setOverAssignDeliveryPeriod(Boolean overAssignDeliveryPeriod) {
		this.overAssignDeliveryPeriod = overAssignDeliveryPeriod;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "sampling_enabled", columnDefinition = "BOOLEAN")
	public Boolean getSamplingEnabled() {
		return samplingEnabled;
	}

	public void setSamplingEnabled(Boolean samplingEnabled) {
		this.samplingEnabled = samplingEnabled;
	}

	@Column(name = "sampling_copies")
	public Integer getSamplingCopies() {
		return samplingCopies;
	}

	public void setSamplingCopies(Integer samplingCopies) {
		this.samplingCopies = samplingCopies;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Transient
	@Override
	public Long getChannelId() {
		return this.deliveryChannelId.longValue();
	}

	@Transient
	@Override
	public Long getChannelType() {
		return ChannelType.SMS.getCode();
	}

}