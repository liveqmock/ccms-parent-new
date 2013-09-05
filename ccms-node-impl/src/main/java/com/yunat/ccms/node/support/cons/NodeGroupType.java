package com.yunat.ccms.node.support.cons;

/**
 * 节点分组
 */
public enum NodeGroupType {
	/** 流程组  */
	FLOW("tflow"),
	/** 筛选组  */
	FILTER("tfilter"),
	/** 客户组 */
	CUSTOMER("tcustomer"),
	/** 定向优惠组  */
	FIXED("tfixed"), // TODO 优惠券、UMP 需要调整节点类型
	/** 沟通组 */
	COMMUNICATE("tcommunicate"),
	/** 渠道反馈组 */
	CHANNEL_RESPONSE("tchannelresponse"),
	/** 客户分析组  */
	ANALYSIS("tanalysis"),
	/** 会员管理组  */
	MEMBER("tmm"),
	/** 指标构造  */
	DATA("tdata"),
	;// end of define enum

	private String name;
	private NodeGroupType(String name_) {
		this.name = name_;
	}

	public String getName() {
		return name;
	}
}