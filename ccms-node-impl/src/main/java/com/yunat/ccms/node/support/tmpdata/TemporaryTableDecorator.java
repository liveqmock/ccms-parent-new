package com.yunat.ccms.node.support.tmpdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.annotation.ResultType;
import com.yunat.ccms.node.spi.NodeTemporaryDataHandler;
import com.yunat.ccms.node.spi.NodeTemporaryDataRegistry;

/**
 * role: ConcreteDecorator
 * 
 * @author yinwei
 * 
 */
@Component
public class TemporaryTableDecorator extends TemporaryDataDecorator {

	@Autowired
	@Qualifier("temporaryTable")
	private NodeTemporaryDataHandler handler;

	@Autowired
	private NodeTemporaryDataRegistry registry;

	@Override
	public String generate(Long jobId, Long subjobId, String suffix, String sql) {
		String tempTableName = handler.generate(jobId, subjobId, suffix, sql);
		registry.register(jobId, tempTableName, ResultType.TABLE.toString());
		return tempTableName;
	}

	@Override
	public void destroy(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String convert(Long subjobId, String suffix, String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
}