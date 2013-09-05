package com.yunat.ccms.node.support.tmpdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.spi.NodeTemporaryDataHandler;
import com.yunat.ccms.node.support.NodeSQLExecutor;

/**
 * role :ConcreteComponent
 * @author yinwei
 */
@Component(value = "temporaryView")
public class TemporaryView implements NodeTemporaryDataHandler {

	@Autowired
	private NodeSQLExecutor nodeSQLExecutor;

	@Autowired
	private TemporayDataNamingConvention namingConvention;

	@Override
	public String generate(Long jobId, Long subjobId, String suffix, String sql) {
		String viewName = namingConvention.getTemporaryViewName(subjobId, suffix);
		String sqlCreate = "create or replace view " + viewName + " as " + sql;
		nodeSQLExecutor.execute(sqlCreate);
		return viewName;
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
