package com.yunat.ccms.node.biz.sms;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Matcher;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.biz.service.query.CampaignQuery;
import com.yunat.ccms.channel.external.scs.handler.RetrievalGatewayHandler;
import com.yunat.ccms.channel.external.scs.handler.SenderSMSHandler;
import com.yunat.ccms.channel.support.MarkVariableResolve;
import com.yunat.ccms.channel.support.cons.ChannelErrorCode;
import com.yunat.ccms.channel.support.service.RemoteLoggerService;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.cons.ColumnConstant;
import com.yunat.ccms.core.support.exception.CcmsBusinessException;
import com.yunat.ccms.core.support.external.ChannelParamBuilder;
import com.yunat.ccms.core.support.jooq.MySQLFactorySingleton;
import com.yunat.ccms.jooq.tables.TwMobileBlacklist;
import com.yunat.ccms.jooq.tables.TwMobileRedlist;
import com.yunat.ccms.jooq.tables.TwfLogChannelUser;
import com.yunat.ccms.metadata.service.MetaBaseConfigService;
import com.yunat.ccms.node.biz.NodeProcessorHelper;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.spi.NodeTemporaryDataRegistry;
import com.yunat.ccms.node.support.NodeSQLExecutor;
import com.yunat.ccms.node.support.cons.ContactValidatorRegex;
import com.yunat.ccms.node.support.tmpdata.jooq.GenerateTableByJooqTemplate;
import com.yunat.ccms.node.support.tmpdata.jooq.TmpSmsSend;
import com.yunat.channel.business.sms.SMSDynamicZipUtil;
import com.yunat.channel.business.sms.SMSField;
import com.yunat.channel.business.sms.SMSZipUtil;
import com.yunat.channel.util.ChannelZipUtil;

@Component
public abstract class AbstractNodeSMSActuator implements NodeSMSActuator {

	private static Logger logger = LoggerFactory.getLogger(AbstractNodeSMSActuator.class);
	protected MySQLFactorySingleton ccmsFactory = MySQLFactorySingleton.getInstance();

	@Autowired
	protected NodeSQLExecutor sqlExecutor;

	@Autowired
	protected NodeProcessorHelper nodeProcessorHelper;

	@Autowired
	protected NodeSMSCommand nodeSMSCommand;

	@Autowired
	protected NodeSMSQuery nodeSMSQuery;

	@Autowired
	protected MetaBaseConfigService metaBaseConfigService;

	@Autowired
	private CampaignQuery campaignQuery;

	@Autowired
	private MarkVariableResolve markVariableResolve;

	@Autowired
	private GenerateTableByJooqTemplate generateTableByJooqTemplate;

	@Autowired
	private AppPropertiesQuery appPropertiesQuery;

	@Autowired
	private RetrievalGatewayHandler retrievalGatewayHandler;

	@Autowired
	private SenderSMSHandler senderSMSHandler;

	@Autowired
	private RemoteLoggerService remoteLoggerService;

	@Autowired
	private NodeTemporaryDataRegistry nodeTemporaryDataRegistry;

	protected abstract void buildDeliveryDetailList(NodeSMS nodeSMS, NodeProcessingContext context,
			String deliveryTableName, List<Long> markIdAttributeList, String messageSubstitute,
			String testMessageSubstitute);

	@SuppressWarnings("rawtypes")
	protected void generateChannelUserRecordPrefix(Select select) {
		TwfLogChannelUser logChannelUser = TwfLogChannelUser.TWF_LOG_CHANNEL_USER;
		Field subjobId = logChannelUser.SUBJOB_ID;
		Field uniId = logChannelUser.UNI_ID;
		Field createTime = logChannelUser.CREATETIME;
		Field nodeId = logChannelUser.NODE_ID;
		Field userChannelInfo = logChannelUser.USER_CHANNEL_INFO;
		Insert insert = ccmsFactory.insertInto(logChannelUser, subjobId, uniId, createTime, nodeId, userChannelInfo)
				.select(select);

		logger.info("{}", insert.getSQL(true));
		sqlExecutor.execute(insert.getSQL(true));

	}

	@Override
	public void executeDelivery(NodeSMS nodeConfig, NodeProcessingContext context, String schemaName) {
		Long campaignId = context.getCampId();
		Campaign campaign = campaignQuery.findByCampId(campaignId);
		String creater = campaign.getCreater().getLoginName();

		// 过滤短信内容的动态标签
		Pattern ImgPattern = markVariableResolve.messageImgMarkVariable();
		Pattern spanPattern = markVariableResolve.messageSpanMarkVariable();
		List<String> testExecuteNumber = convertTestMobileString2List(nodeConfig.getTestPhoneString());

		String message = standardizationMobileMessage(nodeConfig.getMessageValue());
		List<Long> markIds = parseMarkIdAttribute(ImgPattern, message);
		if (CollectionUtils.isEmpty(markIds)) {
			markIds = parseMarkIdAttribute(spanPattern, message);
		}

		// 正式发送的短信内容(经过替换后)
		String normalExecuteMessage = markVariableResolve.messageSubstitute(ImgPattern, message, "@@$1@@");
		normalExecuteMessage = markVariableResolve.messageSubstitute(spanPattern, normalExecuteMessage, "@@$1@@");

		// 测试执行的短信内容(经过替换后)
		String testExecuteMessage = markVariableResolve.messageSubstitute(ImgPattern, message, "$2");
		testExecuteMessage = markVariableResolve.messageSubstitute(spanPattern, testExecuteMessage, "$2");

		// 发送短信内容的临时存储表名称
		String deliveryTableName = generateTableByJooqTemplate.create(TmpSmsSend.class, context.getJobId(),
				context.getSubjobId(), null);

		// 生成发送详细列表并记录在临时存储表中
		buildDeliveryDetailList(nodeConfig, context, deliveryTableName, markIds, normalExecuteMessage,
				testExecuteMessage);

		// 删除测试执行号码在黑名单中存在的号码
		removeDuplicateTestExecuteNumber(testExecuteNumber, context.isTestExecute(), nodeConfig.getBlacklistDisabled());

		// 合并红名单与测试执行号码
		testExecuteNumber = mergeTestExecuteAndRedlistMobile(testExecuteNumber, context.isTestExecute(),
				nodeConfig.getBlacklistDisabled());

		String taskId = ChannelParamBuilder.taskId(
				appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID), context.getJobId(),
				context.getNodeId());
		// 普通短信发送
		if (CollectionUtils.isEmpty(markIds)) {
			deliverySMSTask(nodeConfig, context, deliveryTableName, taskId, testExecuteMessage, testExecuteNumber,
					creater, false);
		} else {
			// 动态短信发送
			deliverySMSTask(nodeConfig, context, deliveryTableName, taskId, testExecuteMessage, testExecuteNumber,
					creater, true);
		}

		nodeTemporaryDataRegistry.destory(deliveryTableName);
	}

	@SuppressWarnings("rawtypes")
	private void deliverySMSTask(NodeSMS nodeConfig, NodeProcessingContext context, String deliveryTableName,
			String taskId, String testExecuteMessage, List<String> testExecuteNumber, String creater, boolean isDynamic) {
		String username = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID);
		String password = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_PASSWORD);
		Long gatewayId = nodeConfig.getDeliveryChannelId().longValue();
		BaseResponse<Map<String, Object>> gatewayInfo = retrievalGatewayHandler.getGateWayInfo(username, password,
				gatewayId, PlatEnum.taobao);
		Integer packNumber = null;
		SMSField smsFile = null;
		Double sendingPrice = null;
		try {
			if (gatewayInfo.isSuccess()) {
				Map<String, Object> map = gatewayInfo.getRtnData();
				if (isDynamic) {
					packNumber = Integer.parseInt(map.get("maxnum_dynamic").toString());
				} else {
					packNumber = Integer.parseInt(map.get("maxnum").toString());
				}

				String sign = map.containsKey("sign") ? (String) map.get("sign") : "";
				int markLength = Integer.parseInt(map.get("mark_length").toString());
				int wordsLimit = Integer.parseInt(map.get("words_limit").toString());
				int gatewayType = Integer.parseInt(map.get("gateway_type").toString());
				sendingPrice = (Double) map.get("price") * 0.01;
				String unsubscribeMessage = map.get("backorderinfo").toString();
				Integer unsubscribeEnabledIntValue = nodeConfig.getUnsubscribeEnabled() ? 1 : 0;
				smsFile = new SMSField(markLength, wordsLimit, gatewayType, unsubscribeEnabledIntValue,
						unsubscribeMessage, sign);
			} else {
				logger.info("response gatewayInfo is not success. ");
				throw new CcmsBusinessException("response gatewayInfo is not success.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("read response gatewayInfo happend exception. ", e.getMessage());
			throw new CcmsBusinessException("read response gatewayInfo happend exception. " + e.getMessage());
		}

		int deliveryCount = countDelivery(deliveryTableName);
		if (deliveryCount <= 0 && CollectionUtils.isEmpty(testExecuteNumber)) {
			logger.info("delivery target is blank, will be not execute");
			// throw new
			// CcmsBusinessException("delivery target is blank, will be not execute");
		}

		// 执行打包发送
		int pageSize = Integer.valueOf(appPropertiesQuery
				.retrieveConfigValue(CCMSPropertiesEnum.CHANNEL_CLIENT_SEND_BATCH_SIZE));
		int sendingTotalNum = 0;
		BaseResponse info = null;
		if (isDynamic) {
			// 打包
			SMSDynamicZipUtil smsDynamicPack = null;
			try {
				smsDynamicPack = new SMSDynamicZipUtil(packNumber, taskId, smsFile);
				putPackPagination(deliveryTableName, deliveryCount, pageSize, smsDynamicPack, testExecuteMessage,
						testExecuteNumber);
				logger.info("info : {}", smsDynamicPack.getTotalSum());
				sendingTotalNum = smsDynamicPack.getTotalSum();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("delivery dynamic sms happened exception.", e.getMessage());
				throw new CcmsBusinessException("delivery dynamic sms happened exception.");
			}

			// 发送
			info = senderSMSHandler.sendTaskSMSBatch(gatewayId, username, password, smsDynamicPack, null, taskId,
					context.getCampId().toString(), context.getNodeId().toString(), creater, testExecuteMessage,
					PlatEnum.taobao);
			logger.info("dynamic sender result: {}", info);

		} else {
			// 打包
			SMSZipUtil smsPack = null;
			try {
				smsPack = new SMSZipUtil(packNumber, taskId, smsFile);
				smsPack.put(testExecuteMessage);
				putPackPagination(deliveryTableName, deliveryCount, pageSize, smsPack, testExecuteMessage,
						testExecuteNumber);
				logger.info("info : {}", smsPack.getTotalSum());
				sendingTotalNum = smsPack.getTotalSum();
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("delivery normal sms happened exception.", e.getMessage());
				throw new CcmsBusinessException("delivery normal sms happened exception.");
			}

			// 发送
			info = senderSMSHandler.sendTaskSMS(gatewayId, username, password, smsPack, testExecuteMessage, null,
					taskId, context.getCampId().toString(), context.getNodeId().toString(), creater, PlatEnum.taobao);
			logger.info("normal sender result: {}", info);

		}

		// 渠道反馈为null
		if (null == info) {
			nodeTemporaryDataRegistry.destoryNodeMidsLogger(context.getJobId(), deliveryTableName);
			throw new CcmsBusinessException("delivery sms happened exception. BaseResponse info is null.");
		}

		// 余额不足，节点抛出异常
		if (!info.isSuccess() && ChannelErrorCode.NotEnoughMoney.equals(info.getErrCode())) {
			nodeTemporaryDataRegistry.destoryNodeMidsLogger(context.getJobId(), deliveryTableName);
			throw new CcmsBusinessException("delivery sms happened exception. " + info.getErrMsg());
		}

		// save execute record
		ExecutionRecord executionRecord = new ExecutionRecord();
		executionRecord.setNodeId(context.getNodeId());
		executionRecord.setSubjobId(context.getSubjobId());
		executionRecord.setSendingTotalNum(Long.valueOf(sendingTotalNum));
		executionRecord.setSendingPrice(sendingPrice);
		executionRecord.setCreatedTime(new Date());
		nodeSMSCommand.saveExecutionRecord(executionRecord);

	}

	@SuppressWarnings("unchecked")
	private List<String> convertTestMobileString2List(String testMobileString) {
		if (StringUtils.isNotEmpty(testMobileString)) {
			String[] testExeNumbers = testMobileString.replaceAll(" ", "").replaceAll("，", ",").split(",");
			List<String> testExeNumLists = Arrays.asList(testExeNumbers);
			List<String> testExeNumList = Lists.newArrayList();
			for (String testNumber : testExeNumLists) {
				java.util.regex.Pattern p = java.util.regex.Pattern.compile(ContactValidatorRegex.JAVA_REGEX_MOBILE
						.getRegexExpression());
				java.util.regex.Matcher m = p.matcher(testNumber);
				if (m.matches() && !testExeNumList.contains(testNumber)) {
					testExeNumList.add(testNumber);
				}
			}
			return testExeNumList;
		}

		return Collections.EMPTY_LIST;
	}

	private String standardizationMobileMessage(String message) {
		return message.replaceAll("<br>", "");
	}

	private List<Long> parseMarkIdAttribute(Pattern pattern, String message) {
		List<Long> markIdAttributeList = new LinkedList<Long>();
		PatternMatcher matcher = new Perl5Matcher();
		MatchResult matchResult = matcher.getMatch();
		PatternMatcherInput matchInput = new PatternMatcherInput(message);
		while (matcher.contains(matchInput, pattern)) {
			matchResult = matcher.getMatch();
			markIdAttributeList.add(Long.parseLong(matchResult.group(1)));
		}
		return markIdAttributeList;
	}

	private List<String> mergeTestExecuteAndRedlistMobile(List<String> testExecuteMobile, boolean isTestExecute,
			boolean redlistEnabled) {
		if (CollectionUtils.isEmpty(testExecuteMobile)) {
			testExecuteMobile = Lists.newArrayList();
		}
		if (!isTestExecute && redlistEnabled) {
			List<Map<String, Object>> redlist = Lists.newArrayList();
			TwMobileRedlist table = TwMobileRedlist.TW_MOBILE_REDLIST;
			SelectQuery selectQuery = ccmsFactory.selectQuery();
			selectQuery.addSelect(table.MOBILE.as(ColumnConstant.MOBILE));
			selectQuery.addFrom(table);
			redlist = sqlExecutor.queryForList(selectQuery.getSQL(true));

			for (Map<String, Object> map : redlist) {
				String mobile = (String) map.get(ColumnConstant.MOBILE);
				if (!testExecuteMobile.contains(mobile)) {
					testExecuteMobile.add(mobile);
				}
			}
		}
		return testExecuteMobile;
	}

	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	private void removeDuplicateTestExecuteNumber(List<String> testExecuteNumber, boolean isTestExecute,
			boolean blackListDisabled) {
		if (!isTestExecute && blackListDisabled) {
			List<Map<String, Object>> maps = Lists.newArrayList();
			if (CollectionUtils.isNotEmpty(testExecuteNumber)) {
				TwMobileBlacklist blackListTable = TwMobileBlacklist.TW_MOBILE_BLACKLIST;
				Field mobileField = ccmsFactory.fieldByName(blackListTable.getName(), ColumnConstant.MOBILE);
				Select select = ccmsFactory.select(mobileField).from(blackListTable)
						.where(mobileField.in(testExecuteNumber));
				maps = sqlExecutor.queryForList(select.getSQL(true));

				for (Map<String, Object> map : maps) {
					String mobileValue = (String) map.get(ColumnConstant.MOBILE);
					if (testExecuteNumber.contains(mobileValue)) {
						testExecuteNumber.remove(mobileValue);
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private int countDelivery(String deliveryTableName) {
		Select select = ccmsFactory.selectCount().from(deliveryTableName);
		return sqlExecutor.queryForInt(select.getSQL(true));
	}

	private void putPackPagination(String table, int total, int pageSize, ChannelZipUtil channelPack,
			String testExecuteMessage, List<String> testExecuteNumber) {
		if (total > pageSize) {
			int times = total / pageSize + 1;
			int spare = total % pageSize;
			if (spare == 0) {
				times -= 1;
			}

			int position = 0;
			for (int i = 0; i < times; i++) {
				if ((i == times - 1) && (spare != 0)) {
					pageSize = spare;
				}

				List<Map<String, Object>> list = paginationQuery(table, position, pageSize);
				forLoop(list, channelPack, testExecuteNumber);
				position += pageSize;
			}
		} else {
			List<Map<String, Object>> list = query(table);
			forLoop(list, channelPack, testExecuteNumber);
		}

		if (CollectionUtils.isNotEmpty(testExecuteNumber)) {
			for (String mobile : testExecuteNumber) {
				Map<String, Object> map = Maps.newHashMap();
				map.put(ColumnConstant.UNI_ID, mobile); // unid 填入mobile值
				map.put(ColumnConstant.MOBILE, mobile);
				map.put(ColumnConstant.MESSAGE, testExecuteMessage);
				putPack(channelPack, map);
			}
		}

		logger.info("total size : " + channelPack.getTotal());
	}

	private void forLoop(List<Map<String, Object>> list, ChannelZipUtil channlPack, List<String> testExecuteNumber) {
		for (Map<String, Object> map : list) {
			String mobile = map.get(ColumnConstant.MOBILE).toString();
			if (testExecuteNumber.contains(mobile)) {
				testExecuteNumber.remove(mobile);
			}

			putPack(channlPack, map);
		}
	}

	private void putPack(ChannelZipUtil channelPack, Map<String, Object> map) {
		String uniId = map.get(ColumnConstant.UNI_ID).toString();
		String mobile = map.get(ColumnConstant.MOBILE).toString();
		String message = map.get(ColumnConstant.MESSAGE).toString();
		try {
			if (channelPack.getClass().equals(SMSZipUtil.class)) {
				((SMSZipUtil) channelPack).put(uniId, mobile);
			} else {
				((SMSDynamicZipUtil) channelPack).put(uniId, mobile, message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
	private List<Map<String, Object>> paginationQuery(String tableName, int position, int pageSize) {
		Table table = ccmsFactory.tableByName(tableName);
		Select select = ccmsFactory.select().from(table).limit(position, pageSize);
		return sqlExecutor.queryForList(select.getSQL(true));
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
	private List<Map<String, Object>> query(String tableName) {
		Table table = ccmsFactory.tableByName(tableName);
		Select select = ccmsFactory.select().from(table);
		return sqlExecutor.queryForList(select.getSQL(true));
	}
}