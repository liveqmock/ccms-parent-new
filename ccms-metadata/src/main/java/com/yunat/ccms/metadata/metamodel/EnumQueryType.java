package com.yunat.ccms.metadata.metamodel;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.google.common.collect.Lists;
import com.yunat.ccms.metadata.face.FaceProduct;
import com.yunat.ccms.metadata.face.FaceProductKeyword;

/**
 * 元数据：数据类型定义的枚举类型
 * 
 * @author kevin.jiang 2013-3-13
 */
public enum EnumQueryType {

	STRING(1, String.class, "varchar", "'?'", "字符串") {

		public List<MetaOperator> getMappedOperators() {

			return Lists.newArrayList(new MetaOperator[] { MetaOperator.EQ, MetaOperator.NE, MetaOperator.LIKE,
					MetaOperator.NOTLIKE });
		}

		public String parseJsonValue(String json) {

			return json;
		}
	},
	NUMBER(2, Number.class, "number", "?", "数字") {

		public List<MetaOperator> getMappedOperators() {

			return Lists.newArrayList(new MetaOperator[] { MetaOperator.EQ, MetaOperator.NE, MetaOperator.LT,
					MetaOperator.LE, MetaOperator.GT, MetaOperator.GE });
		}

		public String parseJsonValue(String json) {

			return json;
		}
	},
	DATE(3, Date.class, "date", "str_to_date('?', '%Y-%m-%d')", "日期") {

		public List<MetaOperator> getMappedOperators() {

			return Lists.newArrayList(new MetaOperator[] { MetaOperator.EQ, MetaOperator.NE, MetaOperator.LT,
					MetaOperator.LE, MetaOperator.GT, MetaOperator.GE });
		}

		@SuppressWarnings("unchecked")
		public String parseJsonValue(String json) {

			Map<String, Object> aMap = (Map<String, Object>) JSONObject.fromObject(json);
			String sType = (String) aMap.get("type");
			String value = (String) aMap.get("value");

			if (sType == null || "".equals(sType) || value == null || "".equals(value)) {

				return null;
			}

			if (EnumTimeType.RELTIME.name().equals(sType)) {

				return DateTimeHelper.convertRelDate(value);

			} else {

				return (String) aMap.get("value");
			}

		}
	},
	DATETIME(4, Date.class, "timestamp", "str_to_date('?', '%Y-%m-%d %H:%i:%s')", "日期时间") {

		public List<MetaOperator> getMappedOperators() {

			return Lists.newArrayList(new MetaOperator[] { MetaOperator.EQ, MetaOperator.NE, MetaOperator.LT,
					MetaOperator.LE, MetaOperator.GT, MetaOperator.GE });
		}

		@SuppressWarnings("unchecked")
		public String parseJsonValue(String json) {

			Map<String, Object> aMap = (Map<String, Object>) JSONObject.fromObject(json);
			String sType = (String) aMap.get("type");
			String value = (String) aMap.get("value");

			if (sType == null || "".equals(sType) || value == null || "".equals(value)) {

				return null;
			}

			if (EnumTimeType.RELTIME.name().equals(sType)) {

				String[] array = value.split(" ");
				String date = array[0];
				String hour = "";
				if (array.length > 1) {
					hour = array[1];
				}
				return DateTimeHelper.convertRelDateTime(date, hour);

			} else {

				return (String) aMap.get("value");
			}

		}
	},
	DIC(5, String.class, "varchar", "'?'", "字典") {

		public List<MetaOperator> getMappedOperators() {

			return Lists.newArrayList(new MetaOperator[] { MetaOperator.EQ, MetaOperator.NE });
		}

		public String parseJsonValue(String json) {

			return json;
		}
	},
	ORDERED_DIC(6, String.class, "varchar", "'?'", "有序字典") {

		public List<MetaOperator> getMappedOperators() {

			return Lists.newArrayList(new MetaOperator[] { MetaOperator.EQ, MetaOperator.NE, MetaOperator.LT,
					MetaOperator.LE, MetaOperator.GT, MetaOperator.GE });
		}

		public String parseJsonValue(String json) {

			return json;
		}
	},
	REFER(7, String.class, "varchar", "?", "引用") {

		public List<MetaOperator> getMappedOperators() {

			return Lists.newArrayList(new MetaOperator[] { MetaOperator.EQ, MetaOperator.NE });
		}

		public String parseJsonValue(String json) {

			return json;
		}
	},
	BIRTHDAY(11, Date.class, "date", "", "生日") {

		public List<MetaOperator> getMappedOperators() {

			return Lists.newArrayList(new MetaOperator[] { MetaOperator.EQ, MetaOperator.LT, MetaOperator.GT });
		}

		/**
		 * <option selected="" value="1">绝对日期</option><br>
		 * <option value="2">当天的前几天</option><br>
		 * <option value="3">当天的后几天</option>
		 */
		@SuppressWarnings("unchecked")
		public String parseJsonValue(String json) {

			Map<String, Object> aMap = (Map<String, Object>) JSONObject.fromObject(json);
			String sType = (String) aMap.get("type");
			String value = (String) aMap.get("value");

			int iType = Integer.parseInt(sType);
			if (iType != 1) {

				return DateTimeHelper.convertRelMonthDay(Integer.parseInt(value), iType);

			} else {

				return (String) aMap.get("value");
			}

		}
	},
	QUOTA(12, String.class, "varchar", "", "指标") {

		public List<MetaOperator> getMappedOperators() {

			return Lists.newArrayList(new MetaOperator[] { MetaOperator.EQ, MetaOperator.LT, MetaOperator.GT });
		}

		public String parseJsonValue(String json) {

			return json;
		}
	},
	CUSTOMER_LABEL(12, String.class, "varchar", "", "客户标签") {

		public List<MetaOperator> getMappedOperators() {

			return Lists.newArrayList(new MetaOperator[] { MetaOperator.EQ, MetaOperator.NE });
		}

		public String parseJsonValue(String json) {

			return json;
		}
	},
	TDS_PRODUCT(12, String.class, "varchar", "", "商品") {

		public List<MetaOperator> getMappedOperators() {

			return Lists.newArrayList(new MetaOperator[] { MetaOperator.EXISTS, MetaOperator.NOTEXISTS });
		}

		/**
		 * 格式样例：
		 * {"type":"R","value":"F:45L:61559109P:10347207133P:10347916849"}
		 **/
		@SuppressWarnings("unchecked")
		public String parseJsonValue(String json) {

			Map<String, Object> aMap = (Map<String, Object>) JSONObject.fromObject(json);
			String sType = (String) aMap.get("type");
			String sValue = (String) aMap.get("value");

			StringBuilder builder = new StringBuilder();
			if ("F".equals(sType)) {
				builder.append(" exists (select 1 from plt_taobao_product tt where 1=2 ");
			} else if ("R".equals(sType)) {

				builder.append(" not exists (select 1 from plt_taobao_product tt where 1=2 ");
			} else {
				return null;
			}

			FaceProduct face = ProductHelper.parseUIData(sValue);

			for (String id : face.getIds()) {

				builder.append(" or tt.num_iid = " + id);
			}

			for (FaceProductKeyword fpk : face.getKeys()) {

				builder.append(" or (tt.dp_id = " + fpk.getDpId());

				if (fpk.getKeywords() != null && fpk.getKeywords().size() > 0) {
					for (String keyword : fpk.getKeywords()) {

						if (fpk.getKeywords().indexOf(keyword) == 1) {

							builder.append(" (tt.title like '%" + keyword + "%'");
						}
						builder.append(" and title like '%" + keyword + "%'");
					}
					builder.append(")");
					builder.append(" and tt.num_iid = ?.num_iid");
				}
				builder.append(")");
			}

			return builder.toString();
		}
	};

	private int key;
	private Class<?> javaClass;
	private String sqlType;
	private String sqlExpr;
	private String chineseName;

	private EnumQueryType(int key, Class<?> javaClass, String sqlType, String sqlExpr, String chineseName) {
		this.key = key;
		this.javaClass = javaClass;
		this.sqlType = sqlType;
		this.sqlExpr = sqlExpr;
		this.chineseName = chineseName;
	}

	public abstract List<MetaOperator> getMappedOperators();

	public abstract String parseJsonValue(String json);

	public int getKey() {
		return key;
	}

	public Class<?> getJavaClass() {
		return javaClass;
	}

	public String getSqlType() {
		return sqlType;
	}

	public String getSqlExpr() {
		return sqlExpr;
	}

	public String getChineseName() {
		return chineseName;
	}
}
