package com.yunat.ccms.biz.support.cons;

public enum WorkflowEnum {
	STANDARD, // 标准流程
	RFM, // RFM
	CUSTOM_INDEX, // 自定义指标
	MEMBER_MANAGE, // 会员管理
	
	;//end of define enum
	
	
	public String getCode(){
		return this.toString();
	}
	
}
