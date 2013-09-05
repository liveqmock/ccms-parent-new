package com.yunat.ccms.node.process;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.node.biz.target.NodeTarget;
import com.yunat.ccms.node.spi.NodeOutput;
import com.yunat.ccms.node.spi.NodeProcessor;
import com.yunat.ccms.schedule.core.impl.DefaultNodeProcessingContext;

/**
 * 基础版-目标组 测试用例
 * 
 * @author yinwei
 * 
 */
public class BaseVersionTargetNodeProcessTest extends AbstractJunit4SpringContextBaseTests {

	private String preTableName = "tmp_log_node_2_property";

	@Autowired
	@Qualifier("baseVersionTargetNodeProcess")
	NodeProcessor<NodeTarget> targetProcess;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception {

		String preTempTableDropScript = "DROP table   IF EXISTS  tmp_log_node_2_property";

		String preTempTableCreateScript = " Create table "
				+ preTableName
				+ "(uni_id varchar(64) COLLATE utf8_bin NOT NULL COMMENT '主键。客户全局统一ID，由客户统一ID生成规则生成',"
				+ " control_group_type int(2) NOT NULL DEFAULT '0') ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin";

		String preTempTableInitScript = " insert into " + preTableName
				+ " select uni_id ,  -1  from uni_customer  limit 2 ";

		executeScript(preTempTableDropScript);
		executeScript(preTempTableCreateScript);
		executeScript(preTempTableInitScript);
	}

	private void executeScript(String sqlScript) {
		logger.info(sqlScript);
		jdbcTemplate.execute(sqlScript);
	}

	/**
	 * 正常执行
	 */

	@Test
	public final void testProcessNormal() throws Exception {

		Long subjobId = 1000L;
		Long jobId = 100L;
		Long nodeId = 10L;
		Long campaingId = 700L;
		boolean isTest = false;

		DefaultNodeProcessingContext context = this.generatorData2IsNotPreExecuteAndIsNotRecover(preTableName,
				subjobId, jobId, nodeId, campaingId, isTest);

		NodeTarget nodeTarget = new NodeTarget();

		NodeOutput output = targetProcess.process(nodeTarget, context);

		Assert.assertNotNull(output);
	}

}
