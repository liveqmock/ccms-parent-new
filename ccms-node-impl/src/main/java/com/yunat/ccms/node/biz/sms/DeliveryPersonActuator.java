package com.yunat.ccms.node.biz.sms;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.JoinType;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.yunat.ccms.core.support.cons.ColumnConstant;
import com.yunat.ccms.core.support.cons.DBFunctionConstant;
import com.yunat.ccms.jooq.tables.TwMobileBlacklist;
import com.yunat.ccms.jooq.tables.TwfLogChannelUser;
import com.yunat.ccms.jooq.tables.UniCustomer;
import com.yunat.ccms.jooq.tables.VwTaobaoCustomer;
import com.yunat.ccms.metadata.pojo.DatabaseColumn;
import com.yunat.ccms.metadata.pojo.DatabaseTable;
import com.yunat.ccms.metadata.pojo.DicType;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.support.cons.ContactValidType;
import com.yunat.ccms.node.support.cons.ContactValidatorRegex;
import com.yunat.ccms.node.support.cons.ControlGroupDataType;
import com.yunat.ccms.node.support.io.NodeInputUtil;
import com.yunat.ccms.node.support.tmpdata.jooq.TmpSmsSend;

@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
@Component
public class DeliveryPersonActuator extends AbstractNodeSMSActuator {

	private static Logger logger = LoggerFactory.getLogger(DeliveryPersonActuator.class);

	@Override
	public String builderSQL(NodeSMS nodeSMS, NodeProcessingContext context) {
		String code = NodeInputUtil.getUniqueInputTable(context);

		Table table = ccmsFactory.tableByName(code).as("_input");
		Field userId = ccmsFactory.fieldByName(table.getName(), ColumnConstant.UNI_ID);
		Field controlGroupType = ccmsFactory.fieldByName(table.getName(), ColumnConstant.CONTROL_GROUP_TYPE);

		VwTaobaoCustomer vtc = VwTaobaoCustomer.VW_TAOBAO_CUSTOMER;
		Select select = ccmsFactory.select(userId, controlGroupType).from(table).leftOuterJoin(vtc)
				.on(userId.equal(vtc.UNI_ID))
				.where(controlGroupType.equal(ccmsFactory.inline(ControlGroupDataType.SENDING_GROUP.getCode())))
				.and(vtc.IS_MOBILE_VALID.equal(ccmsFactory.inline(ContactValidType.VALID.getCode().toString())))
				.or(controlGroupType.equal(ccmsFactory.inline(ControlGroupDataType.CONTROL_GROUP.getCode())));

		logger.info(select.getSQL(true));
		return select.getSQL(true);
	}

	@Override
	public void generateChannelUserRecord(NodeSMS nodeConfig, NodeProcessingContext context, String schemaName) {
		Table table = ccmsFactory.tableByName(schemaName).as("_schema");
		Field userId = ccmsFactory.fieldByName(table.getName(), ColumnConstant.UNI_ID);
		Field controlGroupType = ccmsFactory.fieldByName(table.getName(), ColumnConstant.CONTROL_GROUP_TYPE);
		UniCustomer uniCustomer = UniCustomer.UNI_CUSTOMER.as("_uni");
		Select select = ccmsFactory
				.select(ccmsFactory.inline(context.getSubjobId()), userId, ccmsFactory.field("now()"),
						ccmsFactory.inline(context.getNodeId()), uniCustomer.MOBILE)
				.from(table)
				.leftOuterJoin(uniCustomer)
				.on(userId.equal(uniCustomer.UNI_ID))
				.where(controlGroupType.equal(ControlGroupDataType.SENDING_GROUP.getCode()))
				.and(ccmsFactory.condition(uniCustomer.getName() + "." + uniCustomer.MOBILE.getName() + " regexp "
						+ ContactValidatorRegex.MYSQL_REGEX_MOBILE.getRegexExpression()));

		generateChannelUserRecordPrefix(select);
	}

	@SuppressWarnings({ "unused" })
	@Override
	public void refreshExecutionRecord(NodeSMS nodeConfig, NodeProcessingContext context, String schemaName) {
		String code = NodeInputUtil.getUniqueInputTable(context);

		if (StringUtils.isNotEmpty(code)) {
			Table lastTable = ccmsFactory.tableByName(code);
			Field userId = ccmsFactory.fieldByName(lastTable.getName(), ColumnConstant.UNI_ID);
			Field controlGroupType = ccmsFactory.fieldByName(lastTable.getName(), ColumnConstant.CONTROL_GROUP_TYPE);

			Select lastOutputCountSelect = ccmsFactory.selectCount().from(lastTable);
			int targetGroupCount = sqlExecutor.queryForInt(lastOutputCountSelect.getSQL(true));
			logger.info("target group persons: {}", targetGroupCount);

			Select lastNonControlSelect = ccmsFactory.selectCount().from(lastTable)
					.where(controlGroupType.equal(ControlGroupDataType.SENDING_GROUP.getCode()));
			int senderGroupCount = sqlExecutor.queryForInt(lastNonControlSelect.getSQL(true));
			logger.info("sender group count : {}", senderGroupCount);

			Table deliveryTable = ccmsFactory.tableByName(schemaName).as("_a");
			Field _userId = ccmsFactory.fieldByName(deliveryTable.getName(), ColumnConstant.UNI_ID);
			Field _controlGroupType = ccmsFactory.fieldByName(deliveryTable.getName(),
					ColumnConstant.CONTROL_GROUP_TYPE);

			UniCustomer uc = UniCustomer.UNI_CUSTOMER.as("_b");
			Select select = ccmsFactory
					.selectCount()
					.from(deliveryTable)
					.leftOuterJoin(uc)
					.on(_userId.equal(uc.UNI_ID))
					.where(_controlGroupType.equal(ControlGroupDataType.SENDING_GROUP.getCode()))
					.and(ccmsFactory.condition(uc.getName() + "." + uc.MOBILE.getName() + " regexp "
							+ ContactValidatorRegex.MYSQL_REGEX_MOBILE.getRegexExpression()));
			int senderGroupValidCount = sqlExecutor.queryForInt(select.getSQL(true));
			logger.info("sender group valid count : {}", senderGroupValidCount);

			// update execute record
			ExecutionRecord executionRecord = nodeSMSQuery.findByNodeIdAndSubjobId(context.getNodeId(), context.getSubjobId());
			if (null == executionRecord) {
				executionRecord = new ExecutionRecord();
				executionRecord.setNodeId(context.getNodeId());
				executionRecord.setSubjobId(context.getSubjobId());
				executionRecord.setCreatedTime(new Date());
			}
			executionRecord.setTargetGroupCustomers(new Long(targetGroupCount));
			executionRecord.setControlGroupCustomers(new Long(targetGroupCount - senderGroupCount));
			executionRecord.setValidPhoneAmount(new Long(senderGroupValidCount));
			executionRecord.setInvalidPhoneAmount(new Long(senderGroupCount - senderGroupValidCount));
			nodeSMSCommand.saveExecutionRecord(executionRecord);
		}
	}

	@Override
	public String buildOutputMessage(String schemaName) {
		return nodeProcessorHelper.buildOutputMessage(schemaName);
	}

	@Override
	public String rebuildOutputSchemaName(NodeProcessingContext context, String schemaName) {
		return schemaName;
	}

	@Override
	protected void buildDeliveryDetailList(NodeSMS nodeSMS, NodeProcessingContext context, String deliveryTableName,
			List<Long> markIdAttributeList, String messageSubstitute, String testDomainMessageSubstitute) {
		TmpSmsSend tmpSmsSend = TmpSmsSend.TMP_SMS_SEND.as(deliveryTableName);

		Set<DatabaseTable> tableSet = Sets.newLinkedHashSet();
		Field sqlReplace = replaceMarkAttribute(tableSet, markIdAttributeList, messageSubstitute);

		SelectQuery select = ccmsFactory.selectQuery();
		VwTaobaoCustomer vtc = VwTaobaoCustomer.VW_TAOBAO_CUSTOMER;
		select.addSelect(vtc.UNI_ID.as(ColumnConstant.UNI_ID));
		TwfLogChannelUser logChannelUser = TwfLogChannelUser.TWF_LOG_CHANNEL_USER;
		select.addSelect(logChannelUser.USER_CHANNEL_INFO.as(ColumnConstant.MOBILE));
		select.addSelect(sqlReplace.as("message"));

		select.addFrom(logChannelUser);
		for (DatabaseTable databaseTable : tableSet) {
			if (!vtc.getName().equals(databaseTable.getDbName())) {
				Table tableName = ccmsFactory.tableByName(databaseTable.getDbName()).as(databaseTable.getDbName());
				Field fieldName = ccmsFactory.fieldByName(tableName.getName(), databaseTable.getPkColumn());
				select.addJoin(tableName, JoinType.LEFT_OUTER_JOIN, logChannelUser.UNI_ID.equal(fieldName));
			}
		}
		select.addFrom(vtc);

		select.addConditions(logChannelUser.UNI_ID.equal(vtc.UNI_ID));
		select.addConditions(vtc.IS_MOBILE_VALID.equal(ContactValidType.VALID.getCode().toString()));

		if (nodeSMS.getBlacklistDisabled()) {
			TwMobileBlacklist blackListTable = TwMobileBlacklist.TW_MOBILE_BLACKLIST;
			Select selectBlackList = ccmsFactory.select(blackListTable.MOBILE).from(blackListTable)
					.where(blackListTable.MOBILE.equal(logChannelUser.USER_CHANNEL_INFO));
			select.addConditions(ccmsFactory.notExists(selectBlackList));
		}

		select.addConditions(logChannelUser.SUBJOB_ID.equal(context.getSubjobId()));
		select.addGroupBy(logChannelUser.USER_CHANNEL_INFO);

		Insert insert = ccmsFactory.insertInto(tmpSmsSend, tmpSmsSend.UNI_ID, tmpSmsSend.MOBILE, tmpSmsSend.MESSAGE)
				.select(select);

		logger.info("{}", insert.getSQL(true));
		sqlExecutor.execute(insert.getSQL(true));
	}

	private Field replaceMarkAttribute(Set<DatabaseTable> tableSet, List<Long> markIdAttributeList,
			String messageSubstitute) {
		Field messageTag = ccmsFactory.inline(messageSubstitute);

		for (Long markId : markIdAttributeList) {
			DatabaseColumn column = metaBaseConfigService.findDatabaseColumnById(markId);
			tableSet.add(column.getTable());
			DicType dicType = column.getDicType();

			Field columnNameCoalesce = null;
			Field columnName = ccmsFactory.fieldByName(column.getTable().getDbName(), column.getColumnName());
			Field columnNameCast = columnName.cast(String.class);
			if (dicType == null) {
				columnNameCoalesce = ccmsFactory.coalesce(columnNameCast, ccmsFactory.inline(""));
			} else {
				Field fnGetDicViewName = ccmsFactory.function(DBFunctionConstant.FN_GET_DIC_VIEWNAME, String.class,
						ccmsFactory.inline(dicType.getDicTypeId()), columnNameCast);
				Field columnNameCastExt = ccmsFactory.field("cast({0} as char)", fnGetDicViewName);
				columnNameCoalesce = ccmsFactory.coalesce(columnNameCastExt, ccmsFactory.inline(""));
			}

			messageTag = ccmsFactory.replace(messageTag, ccmsFactory.inline("@@" + column.getColumnId() + "@@"),
					columnNameCoalesce);
		}

		return messageTag;
	}

}