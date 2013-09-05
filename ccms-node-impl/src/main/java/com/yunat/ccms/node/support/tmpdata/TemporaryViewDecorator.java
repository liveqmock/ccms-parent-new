package com.yunat.ccms.node.support.tmpdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.annotation.ResultType;
import com.yunat.ccms.node.spi.NodeTemporaryDataHandler;
import com.yunat.ccms.node.spi.NodeTemporaryDataRegistry;

/**
 * role: ConcreteDecorator
 * @author yinwei
 */
@Component
public class TemporaryViewDecorator extends TemporaryDataDecorator {

	@Autowired
	@Qualifier("temporaryView")
	private NodeTemporaryDataHandler handler;

	@Autowired
	private NodeTemporaryDataRegistry registry;

	@Override
	public String generate(Long jobId, Long subjobId, String suffix, String sql) {
		String tempViewName = handler.generate(jobId, subjobId, suffix, sql);
		registry.register(jobId, tempViewName, ResultType.VIEW.toString());
		return tempViewName;
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