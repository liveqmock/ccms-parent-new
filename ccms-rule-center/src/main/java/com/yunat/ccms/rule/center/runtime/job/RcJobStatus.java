package com.yunat.ccms.rule.center.runtime.job;

public enum RcJobStatus {
	INIT, // 初始化状态
	ACQUIRED, // 已经被任务处理job获取
	PROCESSING, // 规则引擎正在处理中
	SUCCESS, // 规则引擎处理完成
	ERROR; // 规则引擎处理出现错误
}
