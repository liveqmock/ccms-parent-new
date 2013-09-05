package com.yunat.ccms.node.support.io;

import java.util.List;

import com.yunat.ccms.node.spi.NodeData;
import com.yunat.ccms.node.spi.NodeInput;
import com.yunat.ccms.node.spi.NodeProcessingContext;

public class NodeInputUtil {

	/**
	 * 获取节点唯一输入的表名或者视图名
	 * 
	 * @param context
	 * @return
	 */
	public static final String getUniqueInputTable(NodeProcessingContext context) {
		NodeInput input = context.getNodeInput();
		List<Long> inputNodes = input.getInputNodes();
		NodeData data = input.getInputData(inputNodes.get(0));
		return data.getCode();
	}
}
