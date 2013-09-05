package com.yunat.ccms.node.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.spi.NodeEntity;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.spi.NodeProcessor;
import com.yunat.ccms.node.spi.NodeRetryAble;
import com.yunat.ccms.node.support.NodeSQLExecutor;

/**
 * 所有NodeProcessor实现类的基类，可注入一些公用的属性，如对临时表的操作等
 * 
 * @author xiaojing.qu
 * @param <T>
 * 
 */
@Component
public abstract class AbstractNodeProcessor<T extends NodeEntity> implements NodeProcessor<T>, NodeRetryAble {

	@Autowired
	protected NodeSQLExecutor sqlExecutor;

	@Autowired
	private NodeProcessorHelper nodeProcessorHelper;
	
	@Override
	public boolean canRetry(NodeProcessingContext context) {
		return false;
	}

	protected String getOutputMessage(String name) {
		return nodeProcessorHelper.buildOutputMessage(name);
	}

}