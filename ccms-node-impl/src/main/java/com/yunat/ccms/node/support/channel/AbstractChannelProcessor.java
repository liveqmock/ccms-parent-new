package com.yunat.ccms.node.support.channel;

import java.util.Date;

import org.jooq.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.base.enums.app.PlatEnum;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.channel.external.scs.handler.ChannelSendHandler;
import com.yunat.ccms.channel.support.ChannelEntity;
import com.yunat.ccms.channel.support.domain.ChannelLogger;
import com.yunat.ccms.channel.support.service.ChannelLoggerService;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.external.ChannelParamBuilder;
import com.yunat.ccms.core.support.jooq.MySQLFactorySingleton;
import com.yunat.ccms.jooq.tables.TwfNodeRetry;
import com.yunat.ccms.node.biz.AbstractNodeProcessor;
import com.yunat.ccms.node.spi.NodeOutput;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.spi.NodeProcessingException;
import com.yunat.ccms.node.support.io.DefaultNodeOutput;
import com.yunat.ccms.node.support.io.Table;
import com.yunat.ccms.node.support.tmpdata.TemporaryTableDecorator;

@Component
public abstract class AbstractChannelProcessor<T extends ChannelEntity> extends AbstractNodeProcessor<T> implements
		AbstractChannelBusiness<T> {

	private static Logger logger = LoggerFactory.getLogger(AbstractChannelProcessor.class);
	protected MySQLFactorySingleton ccmsFactory = MySQLFactorySingleton.getInstance();

	@Autowired
	protected AppPropertiesQuery appPropertiesQuery;

	@Autowired
	private TemporaryTableDecorator temporaryTableDecorator;

	@Autowired
	private ChannelLoggerService channelLoggerService;

	@Autowired
	private ChannelSendHandler channelSendHandler;

	protected abstract String buildOutputMessage(T nodeConfig, NodeProcessingContext context, String schemaName);

	protected abstract String rebuildOutputSchemaName(T nodeConfig, NodeProcessingContext context, String schemaName);

	@Override
	@Transactional
	public NodeOutput process(T nodeConfig, NodeProcessingContext context) throws NodeProcessingException {

		addChannelLogger(nodeConfig, context);

		String selectSQL = builderSQL(nodeConfig, context);
		String schemaName = temporaryTableDecorator
				.generate(context.getJobId(), context.getSubjobId(), null, selectSQL);

		executeDeliveryBussiness(nodeConfig, context, schemaName);

		schemaName = rebuildOutputSchemaName(nodeConfig, context, schemaName);

		return new DefaultNodeOutput(context.getNodeId(), new Table(schemaName), buildOutputMessage(nodeConfig,
				context, schemaName));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canRetry(NodeProcessingContext context) {
		boolean retryable = true;
		if (context.isTestExecute()) {
			return retryable;
		}

		String nodeRetryTimesLimit = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_NODE_RETRY_TIMES);
		Long jobId = context.getJobId();
		Long nodeId = context.getNodeId();
		TwfNodeRetry nodeRetry = TwfNodeRetry.TWF_NODE_RETRY;
		Select select = ccmsFactory.selectCount().from(nodeRetry).where(nodeRetry.JOB_ID.equal(jobId))
				.and(nodeRetry.NODE_ID.equal(nodeId)).and(nodeRetry.IS_TEST_EXECUTE.equal(Byte.valueOf("0")));

		int currentRetryTimes = sqlExecutor.queryForInt(select.getSQL(true));
		retryable = Integer.parseInt(nodeRetryTimesLimit) > currentRetryTimes;
		logger.info("normal execution canRetry job_id : {}, node_id : {}, is retry ? {}", new Object[] { jobId, nodeId,
				retryable });
		return retryable;
	}

	private void executeDeliveryBussiness(T nodeConfig, NodeProcessingContext context, String schemaName) {
		if (!context.isTestExecute()) {
			generateChannelUserRecord(nodeConfig, context, schemaName);
		}
		executeDelivery(nodeConfig, context, schemaName);
		refreshExecutionRecord(nodeConfig, context, schemaName);
	}

	private void addChannelLogger(T nodeConfig, NodeProcessingContext context) {
		ChannelLogger channelLogger = new ChannelLogger();
		channelLogger.setCampaignId(context.getCampId());
		channelLogger.setChannelId(nodeConfig.getChannelId().longValue());
		channelLogger.setChannelType(nodeConfig.getChannelType());
		channelLogger.setCreateTime(new Date());
		channelLogger.setNodeId(context.getNodeId());
		channelLogger.setTaskId(buildTaskId(context.getJobId(), context.getNodeId()));
		channelLogger.setTestExecute(context.isTestExecute());
		channelLogger.setSubjobId(context.getSubjobId());
		channelLogger.setPlanStartTime(new Date());
		channelLogger.setPlanEndTime(new Date());
		channelLoggerService.saveOrUpdate(channelLogger);
	}

	private String buildTaskId(Long jobId, Long nodeId) {
		String tenantId = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID);
		return ChannelParamBuilder.taskId(tenantId, jobId, nodeId);
	}

	/**
	 * 判断当前task_id 是否已经提交到了渠道
	 * 
	 * @param taskId
	 *            发送任务执行ID
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	private boolean isChannelTaskId(String taskId) {
		String tenantId = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID);
		String tenantPassword = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_PASSWORD);
		BaseResponse info = channelSendHandler.sendTaskDate(taskId, tenantId, tenantPassword, PlatEnum.taobao);
		logger.info("{}" + info);
		if (info.isSuccess() && null == info.getRtnData()) {
			return false;
		}
		return true;
	}

}