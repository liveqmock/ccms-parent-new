package com.yunat.ccms.rule.center.memo;

public enum RcJobTaobaoMemoStatus {
	INIT, // 初始化状态
	ACQUIRED, // 已被获取，进入队列
	PROCESSING, // 备注进行中
	SUCCESS, // 成功
	ERROR; // 错误
}
