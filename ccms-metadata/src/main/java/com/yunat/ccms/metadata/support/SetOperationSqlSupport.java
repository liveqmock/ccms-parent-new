package com.yunat.ccms.metadata.support;

import java.util.List;

import com.google.common.collect.Lists;
import com.yunat.ccms.metadata.metamodel.EnumQueryType;
import com.yunat.ccms.metadata.metamodel.EnumTableJoin;
import com.yunat.ccms.metadata.metamodel.MetaAttribute;
import com.yunat.ccms.metadata.metamodel.MetaCriteria;
import com.yunat.ccms.metadata.metamodel.MetaEntity;
import com.yunat.ccms.metadata.metamodel.MetaOperator;
import com.yunat.ccms.metadata.metamodel.MetaQuery;

public class SetOperationSqlSupport {

	public static String UNI_ID = "uni_id";
	public static String CONTROL_GROUP_TYPE = "control_group_type";

	/**
	 * 生成两个查询结果的并集的SQL（或者的关系） <br>
	 * UNION关键字的SQL <br>
	 * 去重
	 * 
	 * @param primaryTable
	 * @param relativeTable
	 * @return
	 */
	public static String genUnionSetSql(String primaryTable, String relativeTable) {

		StringBuilder sql = new StringBuilder();
		List<String> tables = Lists.newArrayList(new String[] { primaryTable, relativeTable });
		for (String table : tables) {

			MetaQuery meta_q = new MetaQuery();
			MetaEntity entity = new MetaEntity(table);
			MetaAttribute attr_a = new MetaAttribute(entity, UNI_ID);
			MetaAttribute attr_b = new MetaAttribute("-1", CONTROL_GROUP_TYPE);
			meta_q.addAttribute(attr_a);
			meta_q.addAttribute(attr_b);
			meta_q.setMasterEntity(entity);
			sql.append(meta_q.toSql(true));

			if (tables.indexOf(table) < (tables.size() - 1)) {
				sql.append(" union ");
			}
		}
		return sql.toString();

	}

	/**
	 * 生成两个查询结果的交集的SQL（并且的关系）<br>
	 * JOIN关键字的SQL<br>
	 * 去重
	 * 
	 * @param primaryTable
	 * @param relativeTable
	 * @return
	 */
	public static String genIntersectionSetSql(String primaryTable, String relativeTable) {

		MetaQuery meta_q = new MetaQuery(true);

		try {

			MetaEntity leftEntity = new MetaEntity(primaryTable);
			MetaEntity rightEntity = new MetaEntity(relativeTable);

			MetaAttribute attr_a = new MetaAttribute(leftEntity, UNI_ID);
			MetaAttribute attr_b = new MetaAttribute("-1", CONTROL_GROUP_TYPE);
			meta_q.addAttribute(attr_a);
			meta_q.addAttribute(attr_b);

			meta_q.setMasterEntity(leftEntity);
			MetaAttribute leftAttr = new MetaAttribute(leftEntity, UNI_ID);
			MetaAttribute rightAttr = new MetaAttribute(rightEntity, UNI_ID);
			meta_q.manualJoinEntity(leftAttr, rightEntity, rightAttr, EnumTableJoin.INNER);
			return meta_q.toSql(true);

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 生成两个查询结果的补集的SQL（排除的关系）<br>
	 * NOT EXISTS 或者 Left Join关键字的SQL<br>
	 * 去重
	 * 
	 * @param primaryTable
	 * @param relativeTable
	 * @return
	 */
	public static String genComplementarySetSql(String primaryTable, String relativeTable) {

		MetaQuery meta_q = new MetaQuery(true);

		try {

			MetaEntity leftEntity = new MetaEntity(primaryTable);
			MetaEntity rightEntity = new MetaEntity(relativeTable);

			MetaAttribute attr_a = new MetaAttribute(leftEntity, UNI_ID);
			MetaAttribute attr_b = new MetaAttribute("-1", CONTROL_GROUP_TYPE);
			meta_q.addAttribute(attr_a);
			meta_q.addAttribute(attr_b);

			meta_q.setMasterEntity(leftEntity);
			MetaAttribute leftAttr = new MetaAttribute(leftEntity, UNI_ID);
			MetaAttribute rightAttr = new MetaAttribute(rightEntity, UNI_ID);
			meta_q.manualJoinEntity(leftAttr, rightEntity, rightAttr, EnumTableJoin.LEFT);

			MetaCriteria cri = new MetaCriteria(rightAttr, MetaOperator.ISNULL, "");
			cri.setQueryType(EnumQueryType.STRING);
			meta_q.getCriterias().add(cri);
			return meta_q.toSql(true);

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}
}
