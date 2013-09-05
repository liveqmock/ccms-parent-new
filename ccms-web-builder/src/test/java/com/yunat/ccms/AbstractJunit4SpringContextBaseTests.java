package com.yunat.ccms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.yunat.ccms.node.spi.NodeData;
import com.yunat.ccms.node.spi.NodeInput;
import com.yunat.ccms.node.support.io.DefaultNodeInput;
import com.yunat.ccms.node.support.io.Table;
import com.yunat.ccms.schedule.core.impl.DefaultNodeProcessingContext;

@ContextConfiguration(locations = { "classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml",
		"classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = false)
public abstract class AbstractJunit4SpringContextBaseTests extends AbstractTransactionalJUnit4SpringContextTests {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 生成 正式执行并且正常执行的上下文
	 *
	 * @param node
	 * @param preTableName
	 * @param logJob
	 * @param logSubjob
	 * @return
	 */
	public DefaultNodeProcessingContext generatorData2IsNotPreExecuteAndIsNotRecover(String preTableName,
			Long subjobId, Long jobId, Long nodeId, Long campaingId, boolean isTest) {
		// 通过上一个表或者视图名称 得到nodeData Entity
		NodeData nodeData = new Table(preTableName);
		NodeInput input = new DefaultNodeInput(nodeId, 9L, nodeData);
		DefaultNodeProcessingContext context = new DefaultNodeProcessingContext(subjobId, jobId, nodeId, campaingId,
				isTest, input);
		return context;
	}
}
