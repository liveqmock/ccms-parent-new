package com.yunat.ccms.node.biz.coupon;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.jooq.Delete;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.biz.service.query.CampaignQuery;
import com.yunat.ccms.channel.external.scs.handler.RetrievalGatewayHandler;
import com.yunat.ccms.channel.external.scs.handler.TaobaoCouponHandler;
import com.yunat.ccms.channel.support.cons.ChannelErrorCode;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.cons.ColumnConstant;
import com.yunat.ccms.core.support.exception.CcmsBusinessException;
import com.yunat.ccms.core.support.external.ChannelParamBuilder;
import com.yunat.ccms.core.support.jooq.MySQLFactorySingleton;
import com.yunat.ccms.jooq.tables.TwfLogChannelUser;
import com.yunat.ccms.jooq.tables.UniCustomerPlat;
import com.yunat.ccms.jooq.tables.VwTaobaoCustomer;
import com.yunat.ccms.node.biz.NodeProcessorHelper;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.spi.NodeTemporaryDataRegistry;
import com.yunat.ccms.node.support.NodeSQLExecutor;
import com.yunat.ccms.node.support.cons.ControlGroupDataType;
import com.yunat.ccms.node.support.cons.ControlOutputControlType;
import com.yunat.ccms.node.support.io.NodeInputUtil;
import com.yunat.ccms.node.support.tmpdata.jooq.GenerateTableByJooqTemplate;
import com.yunat.ccms.node.support.tmpdata.jooq.TmpCouponReturn;
import com.yunat.ccms.node.support.tmpdata.jooq.TmpCouponSend;
import com.yunat.channel.business.taobao.TaoBaoZipUtil;

@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
@Component
public class DeliveryNodeCouponActuator implements NodeCouponActuator {

	private static Logger logger = LoggerFactory.getLogger(DeliveryNodeCouponActuator.class);

	@Autowired
	protected NodeSQLExecutor sqlExecutor;

	@Autowired
	private GenerateTableByJooqTemplate generateTableByJooqTemplate;

	@Autowired
	private AppPropertiesQuery appPropertiesQuery;

	@Autowired
	private RetrievalGatewayHandler retrievalGatewayHandler;

	@Autowired
	private TaobaoCouponHandler taobaoCouponHandler;

	@Autowired
	private CampaignQuery campaignQuery;

	@Autowired
	protected NodeProcessorHelper nodeProcessorHelper;

	@Autowired
	private NodeTemporaryDataRegistry nodeTemporaryDataRegistry;

	protected MySQLFactorySingleton ccmsFactory = MySQLFactorySingleton.getInstance();

	private VwTaobaoCustomer vtc = VwTaobaoCustomer.VW_TAOBAO_CUSTOMER;

	private TwfLogChannelUser logChannelUser = TwfLogChannelUser.TWF_LOG_CHANNEL_USER;

	private UniCustomerPlat uniCustomerPlat = UniCustomerPlat.UNI_CUSTOMER_PLAT.as("_uni_plat");

	@Override
	public void generateChannelUserRecord(NodeCoupon nodeConfig, NodeProcessingContext context, String schemaName) {
		Table table = ccmsFactory.tableByName(schemaName).as("_schema");
		Field userId = ccmsFactory.fieldByName(table.getName(), ColumnConstant.UNI_ID);
		Field controlGroupType = ccmsFactory.fieldByName(table.getName(), ColumnConstant.CONTROL_GROUP_TYPE);

		Select select = ccmsFactory
				.select(ccmsFactory.inline(context.getSubjobId()), userId, ccmsFactory.field("now()"),
						ccmsFactory.inline(context.getNodeId()), uniCustomerPlat.CUSTOMERNO).from(table)
				.leftOuterJoin(uniCustomerPlat).on(userId.equal(uniCustomerPlat.UNI_ID))
				.where(controlGroupType.equal(ControlGroupDataType.SENDING_GROUP.getCode()))
				.and(uniCustomerPlat.PLAT_CODE.equal(ccmsFactory.inline(PlatEnum.taobao.toString())));

		generateChannelUserRecordPrefix(select);
	}

	@Override
	public void refreshExecutionRecord(NodeCoupon nodeConfig, NodeProcessingContext context, String schemaName) {

	}

	@Override
	public String builderSQL(NodeCoupon nodeConfig, NodeProcessingContext context) {
		String code = NodeInputUtil.getUniqueInputTable(context);

		Table table = ccmsFactory.tableByName(code).as("_input");
		Field uni_id = ccmsFactory.fieldByName(table.getName(), ColumnConstant.UNI_ID);
		Field controlGroupType = ccmsFactory.fieldByName(table.getName(), ColumnConstant.CONTROL_GROUP_TYPE);

		Select select = ccmsFactory.select(uni_id, controlGroupType).from(table).leftOuterJoin(vtc)
				.on(uni_id.eq(vtc.UNI_ID))
				.where(controlGroupType.equal(ccmsFactory.inline(ControlGroupDataType.SENDING_GROUP.getCode())))
				.or(controlGroupType.equal(ccmsFactory.inline(ControlGroupDataType.CONTROL_GROUP.getCode())));

		logger.info(select.getSQL(true));
		return select.getSQL(true);
	}

	@Override
	public void executeDelivery(NodeCoupon nodeConfig, NodeProcessingContext context, String schemaName) {
		Long campaignId = context.getCampId();
		Campaign campaign = campaignQuery.findByCampId(campaignId);
		String creater = campaign.getCreater().getLoginName();

		if (!nodeConfig.getCoupon().isEnable()) {
			logger.info("当前节点发送的优惠卷已经过期！");
			throw new CcmsBusinessException("当前节点发送的优惠卷已经过期！");
		}
		List<String> testExecuteNumber = convertTestNickList(nodeConfig.getPreviewCustomers());
		// 创建优惠劵节点内使用临时表
		String deliveryTableName = generateTableByJooqTemplate.create(TmpCouponSend.class, context.getJobId(),
				context.getSubjobId(), null);
		// 正式执行插入发送对象List
		if (!context.isTestExecute()) {
			buildDeliveryDetailList(nodeConfig, context, deliveryTableName, schemaName);
		}
		String taskId = ChannelParamBuilder.taskId(
				appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID), context.getJobId(),
				context.getNodeId());
		// 发送优惠劵
		deliveryCounponTask(nodeConfig, taskId, deliveryTableName, testExecuteNumber, context, creater);

		nodeTemporaryDataRegistry.destory(deliveryTableName);
	}

	@Override
	public String buildOutputMessage(String schemaName) {
		return nodeProcessorHelper.buildOutputMessage(schemaName);
	}

	@Override
	public String rebuildOutputSchemaName(NodeCoupon nodeConfig, NodeProcessingContext context, String schemaName) {
		if (context.isTestExecute()) {
			return schemaName;
		}
		if (1 == nodeConfig.getOutputControl()) {
			// 创建优惠劵节点 返回发送信息临时表
			String returnTableName = generateTableByJooqTemplate.create(TmpCouponReturn.class, context.getJobId(),
					context.getSubjobId(), null);
			String taskId = ChannelParamBuilder.taskId(
					appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID), context.getJobId(),
					context.getNodeId());

			boolean gotResult = false;
			do {
				gotResult = getSendCounponResult(taskId, nodeConfig, context, returnTableName);
			} while (!gotResult);

			Table table = ccmsFactory.tableByName(schemaName);
			Field userId = ccmsFactory.fieldByName(table.getName(), ColumnConstant.UNI_ID);
			Field controlGroupType = ccmsFactory.fieldByName(table.getName(), ColumnConstant.CONTROL_GROUP_TYPE);

			TmpCouponReturn tmpCouponReturn = TmpCouponReturn.TMP_COUPON_RETURN.as(returnTableName);
			Table couponReturnTable = ccmsFactory.tableByName(returnTableName);

			// 删除发送失败的nick
			Select select = ccmsFactory.selectOne().from(couponReturnTable).leftOuterJoin(uniCustomerPlat)
					.on(tmpCouponReturn.NICK.equal(uniCustomerPlat.CUSTOMERNO))
					.where(tmpCouponReturn.STATUS.equal(ControlOutputControlType.EC_SUCCESS_RESPONSE_TYPE.getCode()))
					.and(userId.equal(uniCustomerPlat.UNI_ID));
			Delete delete = ccmsFactory.delete(table).whereNotExists(select)
					.and(controlGroupType.equal(ControlGroupDataType.SENDING_GROUP.getCode()));

			logger.info("{}delete:" + delete.getSQL(true));
			sqlExecutor.execute(delete.getSQL(true));
			nodeTemporaryDataRegistry.destory(returnTableName);
		}
		return schemaName;
	}

	private void buildDeliveryDetailList(NodeCoupon nodeConfig, NodeProcessingContext context,
			String deliveryTableName, String schemaName) {
		TmpCouponSend tmpCouponSend = TmpCouponSend.TMP_COUPON_SEND.as(deliveryTableName);

		SelectQuery select = ccmsFactory.selectQuery();
		select.addSelect(logChannelUser.UNI_ID.as(ColumnConstant.UNI_ID));
		select.addSelect(logChannelUser.USER_CHANNEL_INFO.as(ColumnConstant.NICK));
		select.addFrom(logChannelUser);
		select.addConditions(logChannelUser.SUBJOB_ID.equal(ccmsFactory.inline(context.getSubjobId())));

		Insert insert = ccmsFactory.insertInto(tmpCouponSend, tmpCouponSend.UNI_ID, tmpCouponSend.NICK).select(select);

		logger.info("{}", insert.getSQL(true));
		sqlExecutor.execute(insert.getSQL(true));
	}

	/**
	 * 渠道节点记录发送列表
	 * 
	 * @param select
	 */
	private void generateChannelUserRecordPrefix(Select select) {
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

	/**
	 * 验证与排重测试买家nick
	 * 
	 * @param testNickString
	 * @return
	 */
	private List<String> convertTestNickList(String testNickString) {
		if (StringUtils.isNotEmpty(testNickString)) {
			String[] testExeNick = testNickString.replaceAll(" ", "").replaceAll("，", ",").split(",");
			List<String> testExeNickLists = Arrays.asList(testExeNick);
			List<String> testExeNickList = Lists.newArrayList();
			// 后期可加入调用淘宝接口校验买家nick验证
			for (String testNumber : testExeNickLists) {
				if (!testExeNickList.contains(testNumber)) {
					testExeNickList.add(testNumber);
				}
			}
			return testExeNickList;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * 发送优惠劵
	 * 
	 * @param nodeConfig
	 *            节点配置信息
	 * @param taskId
	 *            发送任务ID
	 * @param deliveryTableName
	 *            节点内临时表名
	 * @param testExecuteNumber
	 *            测试执行买家nick
	 * @param context
	 * @param creater
	 *            操作执行着名称
	 * @return
	 */
	@SuppressWarnings("unused")
	private void deliveryCounponTask(NodeCoupon nodeConfig, String taskId, String deliveryTableName,
			List<String> testExecuteNumber, NodeProcessingContext context, String creater) {
		String username = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID);
		String password = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_PASSWORD);
		Long gatewayId = nodeConfig.getChannelId().longValue();
		int deliveryCount = countDelivery(deliveryTableName);
		int pageSize = Integer.valueOf(appPropertiesQuery
				.retrieveConfigValue(CCMSPropertiesEnum.CHANNEL_CLIENT_SEND_BATCH_SIZE));
		int sendingTotalNum = 0;
		String shopKey = ChannelParamBuilder.shopKey(PlatEnum.taobao, nodeConfig.getShop().getShopId());
		boolean isNeedGetResponse = false;

		if (deliveryCount <= 0 || CollectionUtils.isEmpty(testExecuteNumber)) {
			logger.info("delivery target is blank, will be not execute");
		}

		BaseResponse<Map<String, Object>> gatewayInfo = retrievalGatewayHandler.getGateWayInfo(username, password,
				gatewayId, PlatEnum.taobao);
		logger.info("{}" + gatewayInfo.toString());

		TaoBaoZipUtil taobaoZipUtil = null;
		Integer packNumber = null;
		if (gatewayInfo.isSuccess()) {
			Map<String, Object> map = gatewayInfo.getRtnData();
			packNumber = Integer.parseInt(map.get("maxnum").toString());
			try {
				taobaoZipUtil = new TaoBaoZipUtil(packNumber, taskId);
				pagePutPack(deliveryTableName, deliveryCount, pageSize, taobaoZipUtil, testExecuteNumber);
				sendingTotalNum = taobaoZipUtil.getTotal();
				logger.info("info : {}", sendingTotalNum);
			} catch (Exception e) {
				logger.error("delivery normal coupon make pakeage failure.", e);
				throw new CcmsBusinessException("delivery normal coupon make pakeage failure.");
			}
		} else {
			throw new CcmsBusinessException("delivery normal coupon make pakeage failure.");
		}

		BaseResponse info = null;

		try {
			info = taobaoCouponHandler.sendTaskCoupon(gatewayId, shopKey, username, password, taobaoZipUtil, nodeConfig
					.getCoupon().getCouponId(), null, taskId, context.getCampId().toString(), context.getNodeId()
					.toString(), creater, PlatEnum.taobao);
			logger.info("dynamic sender result: {}", info);
		} catch (Exception e) {
			throw new CcmsBusinessException("delivery dynamic sms happened exception.", e);
		}
	}

	/**
	 * 返回优惠劵发送结果
	 * 
	 * @param taskId
	 *            发送任务ID
	 * @param nodeConfig
	 * @param context
	 * @param returnTableName
	 * @return 是否从渠道获得优惠券发送结果
	 */
	private boolean getSendCounponResult(String taskId, NodeCoupon nodeConfig, NodeProcessingContext context,
			String returnTableName) {
		String username = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID);
		String password = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_PASSWORD);

		try {
			logger.info("{} 等待："
					+ appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CHANNEL_CLIENT_GET_RESP_WAIT_TIME)
					+ "毫秒后去获取优惠券发送结果");
			Thread.sleep(Long.parseLong(appPropertiesQuery
					.retrieveConfigValue(CCMSPropertiesEnum.CHANNEL_CLIENT_GET_RESP_WAIT_TIME)));
		} catch (InterruptedException e) {
			logger.warn("{} 线程等待异常", e);
		}

		BaseResponse<Map<String, Object>> result = taobaoCouponHandler.getSendCouponReport(username, password, taskId,
				PlatEnum.taobao);

		if (!result.isSuccess()) {
			if (ChannelErrorCode.LATERAGAIN.equals(result.getErrCode())) {
				logger.info("{} 优惠券还在发送中，稍候再试:");
				return false;
			} else {
				logger.info("{} 获取优惠券发送结果失败" + result.getErrMsg());
				throw new CcmsBusinessException("获取优惠券发送结果失败.");
			}
		}
		JSONObject res = JSONObject.fromObject(result.getRtnData());

		// 获得预执行号码
		String previewCustomers = nodeConfig.getPreviewCustomers();
		// 发送成功，返回详细报告
		String buyerNickRevSuc = res.getString("buyerNickRevSuc");
		// 发送失败，接收人及原因
		String buyerNickRevFail = res.getString("buyerNickRevFailDetail");

		if (!"".equals(buyerNickRevSuc)) {
			batchUpdateNickReturn(buyerNickRevSuc, previewCustomers, returnTableName,
					ControlOutputControlType.EC_SUCCESS_RESPONSE_TYPE.getCode());
		}
		if (!"".equals(buyerNickRevFail)) {
			batchUpdateNickReturn(buyerNickRevFail, previewCustomers, returnTableName,
					ControlOutputControlType.EC_FAILURE_RESPONSE_TYPE.getCode());
		}
		return true;
	}

	private void pagePutPack(String tableName, int tableSize, int pageSize, TaoBaoZipUtil channelPack,
			List<String> preExeNumList) throws Exception {
		if (tableSize > pageSize) {
			int getTimes = tableSize / pageSize + 1;
			int spare = tableSize % pageSize;
			if (spare == 0) {
				getTimes -= 1;
			}
			int currentPosition = 0;
			for (int i = 0; i < getTimes; i++) {
				if (i == getTimes - 1 && spare != 0) {
					pageSize = spare;
				}
				List list = getPageSendList(tableName, currentPosition, pageSize);
				int listSize = list.size();
				for (int j = 0; j < listSize; j++) {
					Map map = (Map) list.get(j);

					String nick = map.get(ColumnConstant.NICK).toString();
					if (preExeNumList.contains(nick)) {
						preExeNumList.remove(nick);
					}
					putPack((TaoBaoZipUtil) channelPack, map);
				}
				currentPosition += pageSize;
			}
		} else {
			List list = getSendList(tableName);
			int listSize = list.size();
			for (int j = 0; j < listSize; j++) {
				Map map = (Map) list.get(j);
				String nick = map.get(ColumnConstant.NICK).toString();
				if (preExeNumList.contains(nick)) {
					preExeNumList.remove(nick);
				}
				putPack((TaoBaoZipUtil) channelPack, map);
			}
		}

		// 添加预执行
		if (!preExeNumList.isEmpty()) {
			for (String s : preExeNumList) {
				if (!"".equals(s)) {
					((TaoBaoZipUtil) channelPack).put(s, s);
				}
			}
		}
	}

	private List getPageSendList(String tableName, int currentPosition, int pageSize) {
		Table table = ccmsFactory.tableByName(tableName);
		Select select = ccmsFactory.select().from(table).limit(currentPosition, pageSize);
		String sql = select.getSQL(true);
		logger.info("{}打包分页取数据第" + currentPosition + "条到第" + (currentPosition + pageSize) + "条");
		return sqlExecutor.queryForList(sql);
	}

	private List getSendList(String tableName) {
		Table table = ccmsFactory.tableByName(tableName);
		Select select = ccmsFactory.select().from(table);
		String sql = select.getSQL(true);
		logger.info("{}打包取数据");
		return sqlExecutor.queryForList(sql);
	}

	private void putPack(TaoBaoZipUtil channelPack, Map map) throws Exception {
		String uniId = map.get(ColumnConstant.UNI_ID).toString();
		String nick = map.get(ColumnConstant.NICK).toString();
		channelPack.put(uniId, nick);
	}

	private int countDelivery(String deliveryTableName) {
		Select select = ccmsFactory.selectCount().from(deliveryTableName);
		return sqlExecutor.queryForInt(select.getSQL());
	}

	private static List<String> safeSplit(String str) {
		String[] sa = StringUtils.split(str, ',');
		List<String> list = new ArrayList<String>();
		if (sa != null) {
			Collections.addAll(list, sa);
		}
		return list;
	}

	/**
	 * 批量插入 优惠劵发送结果信息
	 * 
	 * @param buyerNick
	 *            返回Nick集合
	 * @param returnTableName
	 *            优惠劵返回Nick临时表名
	 */
	public void batchUpdateNickReturn(String buyerNick, String previewCustomers, String returnTableName,
			final Long couponResponseType) {
		List<String> buyerNickLists = safeSplit(buyerNick);
		List<String> previewCustomersList = safeSplit(previewCustomers);
		TmpCouponReturn tmpCouponReturn = TmpCouponReturn.TMP_COUPON_RETURN.as(returnTableName);
		buyerNickLists.removeAll(previewCustomersList);// 将测试执行Nick去除
		final List<String> finalSendbuyerNickLists = Lists.newArrayList(buyerNickLists);

		Insert insert = ccmsFactory.insertInto(tmpCouponReturn, tmpCouponReturn.NICK, tmpCouponReturn.STATUS).values(
				ccmsFactory.value("", tmpCouponReturn.NICK), ccmsFactory.value("", tmpCouponReturn.STATUS));

		int[] count = sqlExecutor.batchUpdate(insert.getSQL(), new BatchPreparedStatementSetter() {

			public int getBatchSize() {
				return finalSendbuyerNickLists.size();
			}

			public void setValues(PreparedStatement ps, int index) throws SQLException {
				String nick = finalSendbuyerNickLists.get(index);
				ps.setString(1, nick);
				ps.setLong(2, couponResponseType);
			}
		});
	}

}
