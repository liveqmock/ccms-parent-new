package com.yunat.ccms.node.biz.query;

import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.metadata.metamodel.EnumQueryCode;
import com.yunat.ccms.metadata.metamodel.EnumQueryType;
import com.yunat.ccms.metadata.metamodel.EnumRelation;
import com.yunat.ccms.metadata.metamodel.EnumTableJoin;
import com.yunat.ccms.metadata.metamodel.MetaAttribute;
import com.yunat.ccms.metadata.metamodel.MetaCriteria;
import com.yunat.ccms.metadata.metamodel.MetaEntity;
import com.yunat.ccms.metadata.metamodel.MetaOperator;
import com.yunat.ccms.metadata.metamodel.MetaQuery;
import com.yunat.ccms.metadata.pojo.Query;
import com.yunat.ccms.metadata.service.MetaQueryConfigService;
import com.yunat.ccms.metadata.support.SetOperationSqlSupport;

/**
 * 查询节点：构建并查询执行
 * 
 * @author kevin.jiang 2013-3-18
 */
@Service
public class QueryNodeExecuteService {

	Logger logger = Logger.getLogger(this.getClass());

	public static String VIEW_TAOBAO_CUSTOMER = "vw_taobao_customer";

	public static String UNI_ID = "uni_id";

	public static String CUSTOMER_NO = "customerno";

	public static String CONTROL_GROUP_TYPE = "control_group_type";

	@Autowired
	NodeQuerySupport nodeQuerySupport;

	@Autowired
	QueryNodeConfigService queryNodeConfigService;

	@Autowired
	MetaQueryConfigService metaQueryConfigService;

	/**
	 * 客户属性查询执行
	 * 
	 * @param nodeId
	 * @param jobId
	 * @param subjobId
	 * @param inputTableName
	 *            输入临时表
	 * @param tempTableNameList
	 *            临时表名称列表，需要把生成的临时表注册到列表中，供最开始的调用者删除这些临时表
	 * @return
	 */
	public String executePropertiesQuery(long nodeId, long jobId, long subjobId, String inputTableName,
			List<String> tempTableNameList) {

		String selectSql = buildPropertiesQuery(nodeId, inputTableName);

		if (selectSql == null) {

			return inputTableName;
		}
		return nodeQuerySupport.createNodeTempTable(jobId, subjobId, "property", UNI_ID, selectSql, tempTableNameList);
	}

	/**
	 * 客户属性查询SQL构建
	 * 
	 * 基础版只支持客户信息查询，只使用客户信息查询一个查询模版，所有客户属性查询合并成一条查询语句
	 * 
	 * @param nodeId
	 * @param inputTableName
	 * @return
	 */
	private String buildPropertiesQuery(long nodeId, String inputTableName) {

		try {

			NodeQueryDefined propertiesDefinedQuery = null;

			NodeQuery node = queryNodeConfigService.loadQueryNodeByID(nodeId);
			Iterator<NodeQueryDefined> it = node.getQueryDefineds().iterator();
			while (it.hasNext()) {

				NodeQueryDefined defined = it.next();
				Query query = defined.getQuery();

				// 界面查询保存成一条查询语句，查询定义表中只有一条对应数据
				if (EnumQueryCode.CUSTOMER.name().equals(query.getCode())) {
					propertiesDefinedQuery = defined;
					break;
				}
			}

			if (propertiesDefinedQuery == null || propertiesDefinedQuery.getCriterias().size() <= 0) {

				return null;
			}
			MetaQuery meta_q = queryNodeConfigService.loadMetaQueryFromPojo(propertiesDefinedQuery);
			meta_q.setUseAlias(true);

			MetaEntity entity = new MetaEntity(inputTableName);

			MetaAttribute attr_a = new MetaAttribute(entity, UNI_ID);
			MetaAttribute attr_b = new MetaAttribute("-1", CONTROL_GROUP_TYPE);
			meta_q.addAttribute(attr_a);
			meta_q.addAttribute(attr_b);

			MetaEntity leftEntity = new MetaEntity(VIEW_TAOBAO_CUSTOMER);
			MetaAttribute leftAttr = new MetaAttribute(leftEntity, UNI_ID);
			meta_q.manualJoinEntity(leftAttr, entity, attr_a, EnumTableJoin.INNER);

			return meta_q.toSql(true);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 消费信息查询执行
	 * 
	 * @param nodeId
	 * @param jobId
	 * @param subjobId
	 * @param inputTableName
	 *            输入临时表
	 * @param tempTableNameList
	 *            临时表名称列表，需要把生成的临时表注册到列表中，供最开始的调用者删除这些临时表
	 * @return
	 */
	public String executeConsumeQuery(long nodeId, long jobId, long subjobId, String inputTableName,
			List<String> tempTableNameList) {

		String lastTable = null;
		String currTable = null;
		String subTable = null;

		try {
			NodeQuery node = queryNodeConfigService.loadQueryNodeByID(nodeId);

			int index = 0;
			Iterator<NodeQueryDefined> it = node.getQueryDefineds().iterator();

			int nodeQueryDefinedIndex = 0;
			while (it.hasNext()) {

				if (lastTable == null) {

					lastTable = inputTableName;
				}

				index++;
				NodeQueryDefined defined = it.next();
				Query query = defined.getQuery();
				if (EnumQueryCode.ORDER.name().equals(query.getCode())) {

					subTable = subQueryOrder(jobId, subjobId, defined, "plt_taobao_order", index, tempTableNameList);

				} else if (EnumQueryCode.ORDER_ITEM.name().equals(query.getCode())) {

					subTable = subQueryOrder(jobId, subjobId, defined, "plt_taobao_order_item", index,
							tempTableNameList);

				} else {

					continue;
				}

				// 查询单条消费信息
				currTable = singleQueryOrder(nodeId, jobId, subjobId, inputTableName, subTable, defined, index,
						tempTableNameList);

				// 查询结果和上一条查询结果合并。注意，每个查询节点的第一条，一定是和上一个节点的输出做IntersectionSet的，不论它自己的配置是AND还是OR。
				String sql = null;
				if (EnumRelation.OR.name().equals(defined.getRelation()) && nodeQueryDefinedIndex++ > 0) {

					sql = SetOperationSqlSupport.genUnionSetSql(lastTable, currTable);

				} else {

					sql = SetOperationSqlSupport.genIntersectionSetSql(lastTable, currTable);
				}
				logger.info(sql);

				String suffix = EnumQueryCode.ORDER.getCode() + "_" + index + "_output";
				if (EnumQueryCode.ORDER_ITEM.name().equals(query.getCode())) {
					suffix = EnumQueryCode.ORDER_ITEM.getCode() + "_" + index + "_output";
				}
				// 若不是最后一条，则本条的输出作为下条的输入
				lastTable = this.nodeQuerySupport.createNodeTempTable(jobId, subjobId, suffix, UNI_ID, sql,
						tempTableNameList);
			}

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

		return lastTable;
	}

	/**
	 * 消费信息查询SQL构建
	 * 
	 * 消费信息查询使用订单查询模版，每一行查询条件组成一条查询语句，可能有多条查询语句
	 * 
	 * @param nodeId
	 * @param inputTableName
	 * @return
	 */
	private String singleQueryOrder(Long nodeId, Long jobId, Long subjobId, String inputTableName, String subTableName,
			NodeQueryDefined defined, int index, List<String> tempTableNameList) throws Exception {

		MetaEntity leftEntity = new MetaEntity(inputTableName);
		MetaEntity rightEntity = new MetaEntity(subTableName);
		MetaAttribute attr_a = new MetaAttribute(leftEntity, UNI_ID);
		MetaAttribute attr_b = new MetaAttribute("-1", CONTROL_GROUP_TYPE);

		MetaQuery mq = new MetaQuery();
		mq.setMasterEntity(leftEntity);
		mq.addAttribute(attr_a);
		mq.addAttribute(attr_b);

		MetaAttribute leftAttr = new MetaAttribute(leftEntity, UNI_ID);
		MetaAttribute rightAttr = new MetaAttribute(rightEntity, UNI_ID);

		// JSON格式：{"buy":true} 或者 {"buy":false}
		boolean isBuy = false;
		if (defined.getExtCtrlInfo() != null) {

			JSONObject aJson = JSONObject.fromObject(defined.getExtCtrlInfo());
			isBuy = (Boolean) aJson.get("buy");
		}

		if (isBuy) {
			mq.manualJoinEntity(leftAttr, rightEntity, rightAttr, EnumTableJoin.INNER);
		} else {
			mq.manualJoinEntity(leftAttr, rightEntity, rightAttr, EnumTableJoin.LEFT);
			MetaCriteria cri = new MetaCriteria(rightAttr, MetaOperator.ISNULL, "[]");
			cri.setQueryType(EnumQueryType.STRING);
			mq.addCriteria(cri);
		}

		String suffix = EnumQueryCode.ORDER.getCode() + "_" + index + "_single";
		if (EnumQueryCode.ORDER_ITEM.name().equals(defined.getQuery().getCode())) {
			suffix = EnumQueryCode.ORDER_ITEM.getCode() + "_" + index + "_single";
		}
		logger.info(mq.toSql(true));
		return this.nodeQuerySupport.createNodeTempTable(jobId, subjobId, suffix, UNI_ID, mq.toSql(true),
				tempTableNameList);
	}

	/**
	 * 构建一个订单总表（或者订单字表）的子查询
	 * 
	 * @param defined
	 *            已定义查询对象
	 * @return 临时表名，临时表共包含两个字段，uni_id和control_group_type
	 * @throws Exception
	 */
	private String subQueryOrder(Long jobId, Long subjobId, NodeQueryDefined defined, String joinTableName, int index,
			List<String> tempTableNameList) throws Exception {

		MetaQuery meta_q = queryNodeConfigService.loadMetaQueryFromPojo(defined);
		meta_q.setUseAlias(true);

		MetaEntity entity = new MetaEntity("uni_customer_plat");
		MetaAttribute attr_a = new MetaAttribute(entity, UNI_ID);
		MetaAttribute attr_b = new MetaAttribute("-1", CONTROL_GROUP_TYPE);
		meta_q.addAttribute(attr_a);
		meta_q.addAttribute(attr_b);

		MetaEntity leftEntity = new MetaEntity(joinTableName);
		MetaAttribute leftAttr = new MetaAttribute(leftEntity, CUSTOMER_NO);
		MetaAttribute rightAttr = new MetaAttribute(entity, CUSTOMER_NO);
		meta_q.manualJoinEntity(leftAttr, entity, rightAttr, EnumTableJoin.INNER);

		MetaCriteria cri = new MetaCriteria(new MetaAttribute(entity, "plat_code"), MetaOperator.EQ, "taobao");
		cri.setQueryType(EnumQueryType.STRING);

		meta_q.addCriteria(cri);
		logger.info(meta_q.toSql(true));

		String suffix = EnumQueryCode.ORDER.getCode() + "_" + index + "_sub";
		if (EnumQueryCode.ORDER_ITEM.name().equals(defined.getQuery().getCode())) {
			suffix = EnumQueryCode.ORDER_ITEM.getCode() + "_" + index + "_sub";
		}
		return this.nodeQuerySupport.createNodeTempTable(jobId, subjobId, suffix, UNI_ID, meta_q.toSql(true),
				tempTableNameList);
	}
}
