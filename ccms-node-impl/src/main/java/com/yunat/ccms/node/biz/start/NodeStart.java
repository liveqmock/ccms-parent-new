package com.yunat.ccms.node.biz.start;

import java.io.Serializable;

import com.yunat.ccms.core.support.annotation.Descriptor;
import com.yunat.ccms.node.spi.NodeEntity;

@Descriptor(type = NodeStart.TYPE, hasCountLog = false, cloneable = false,
		validatorClass = com.yunat.ccms.node.biz.start.NodeStartValidator.class,
		processorClass = com.yunat.ccms.node.biz.start.NodeStartProcessor.class,
		handlerClass = com.yunat.ccms.node.biz.start.NodeStartHandler.class)
public class NodeStart implements Serializable, NodeEntity {

	private static final long serialVersionUID = -8016190375768802340L;

	/**
	 * 开始节点只定义节点的类型
	 * 与其他节点有别的是开始节点不需要配置数据
	 */
	public static final String TYPE = "tflowstart";
}
