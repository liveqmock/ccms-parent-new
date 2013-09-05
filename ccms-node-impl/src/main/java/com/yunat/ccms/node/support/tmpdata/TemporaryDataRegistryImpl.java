package com.yunat.ccms.node.support.tmpdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.cons.TableConstant;
import com.yunat.ccms.node.spi.NodeTemporaryDataRegistry;
import com.yunat.ccms.node.support.NodeSQLExecutor;

/**
 * role: ConcreteDecorator
 * @author yinwei
 */
@Component
public class TemporaryDataRegistryImpl implements NodeTemporaryDataRegistry {

	@Autowired
	private NodeSQLExecutor nodeSQLExecutor;

	@Override
	public void register(Long jobId, String name, String type) {
		StringBuffer sql = new StringBuffer();
		sql.append("insert into " + TableConstant.TWF_LOG_NODE_MIDS
				+ " (job_id,table_view_name,table_view_type,created_time) values (" + jobId + ",'" + name
				+ "','" + type + "'" + ",now())");
		
		nodeSQLExecutor.execute(sql.toString());
	}

	@Override
	public void destory(String name) {
		StringBuffer sql = new StringBuffer();
		sql.append("drop table if exists ").append(name);
		
		nodeSQLExecutor.execute(sql.toString());
	}
	
	public void destoryNodeMidsLogger(Long jobId, String schemaName) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("delete from ").append(TableConstant.TWF_LOG_NODE_MIDS)
			.append(" where job_id = ? and table_view_name = ?");
		
		nodeSQLExecutor.execute(sqlBuffer.toString(), jobId, schemaName);
	}


}