package com.yunat.ccms.node.biz;

import org.jooq.Select;
import org.jooq.impl.Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.jooq.MySQLFactorySingleton;
import com.yunat.ccms.node.support.NodeSQLExecutor;

@Component
public class NodeProcessorHelper {
	private Factory ccmsFactory = MySQLFactorySingleton.getInstance();

	@Autowired
	private NodeSQLExecutor sqlExecutor;

	@SuppressWarnings("rawtypes")
	public String buildOutputMessage(String name) {
		Select select = ccmsFactory.selectCount().from(Factory.tableByName(name));
		int count = sqlExecutor.queryForInt(select.getSQL(true));
		return count + "人";
	}

}