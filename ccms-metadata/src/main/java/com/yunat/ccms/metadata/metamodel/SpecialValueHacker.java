package com.yunat.ccms.metadata.metamodel;

import com.yunat.ccms.metadata.pojo.DicType;

public class SpecialValueHacker {

	/**
	 * 生成特殊处理的SQL（无法用设计定义包含的，暂时归类在这里） <BR>
	 * 由于特殊处理的特殊性，处理顺序是重要的，最先匹配，最先返回，有短路效果<BR>
	 * 越特殊的处理，就越应该放在前面
	 * 
	 * @param key
	 *            元数据查询字段ID tm_query_criteria表的query_criteria_id
	 * @param queryType
	 *            查询类型
	 * @param columnWithAlias
	 *            已经替换过表别名的字段名
	 * @param operator
	 *            操作符
	 * @param value
	 *            目标值
	 * @return
	 */
	public static String genSpecialValueSql(Long key, EnumQueryType queryType, DicType dic, String columnWithAlias,
			MetaOperator operator, String value) {

		// 处理生日
		if (EnumQueryType.BIRTHDAY.equals(queryType)) {
			// 由于只比较月和日，所以取买家已有的生日字段的年来做比较
			return columnWithAlias + operator.toSql() + " str_to_date(concat(year(" + columnWithAlias + ") , '-', "
					+ "'" + EnumQueryType.BIRTHDAY.parseJsonValue(value) + "') ,'%Y-%m-%d')";
		}

		// 订单交易状态，有效交易，界面只传递等于操作符EQ
		if (key != null && key == 63L && "20".equals(value)) {

			return (new StringBuilder()).append(columnWithAlias).append(" in (21, 22, 23)").toString();
		}

		// 省份和城市处理：因为这两个字典的查询值一般都是短名，如石家庄，而数据中往往是长名，如石家庄市，所以要把等于改为包含
		if (dic != null && (dic.getDicTypeId() == 22L || dic.getDicTypeId() == 23L)) {

			if (operator == MetaOperator.EQ) {
				return (new StringBuilder()).append(columnWithAlias).append(" like '" + value + "%' ").toString();
			}
			if (operator == MetaOperator.NE) {
				return (new StringBuilder()).append(columnWithAlias).append(" not like '" + value + "%'").toString();
			}
		}

		// 字典类型数据: 用none标识未知的情形（包含null和''两种数据）
		if ((queryType == EnumQueryType.DIC || queryType == EnumQueryType.ORDERED_DIC) && "none".equals(value)) {

			if (operator == MetaOperator.EQ) {
				return (new StringBuilder()).append("(").append(columnWithAlias).append(" is null or ")
						.append(columnWithAlias).append(" = '') ").toString();
			}
			if (operator == MetaOperator.NE) {

				return (new StringBuilder()).append("(").append(columnWithAlias).append(" is not null and ")
						.append(columnWithAlias).append(" != '') ").toString();
			}
		}

		return null;
	}
}
