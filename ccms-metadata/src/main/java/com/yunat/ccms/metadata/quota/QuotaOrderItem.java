package com.yunat.ccms.metadata.quota;

import java.util.List;

import com.google.common.collect.Lists;
import com.yunat.ccms.metadata.metamodel.EnumQueryType;

public enum QuotaOrderItem implements Quota {

	BUY_FEE(1, EnumQueryType.NUMBER, "?0.customerno", "购买金额") {

		public String toSql() {
			return "sum(?0.total_fee)";
		}

		public List<String> getOrderedTableNameList() {
			return Lists.newArrayList(new String[] { "plt_taobao_order_item" });
		}
	},
	BUY_NUM(2, EnumQueryType.NUMBER, "?0.customerno", "购买件数") {

		public String toSql() {
			return "sum(?.num)";
		}

		public List<String> getOrderedTableNameList() {
			return Lists.newArrayList(new String[] { "plt_taobao_order_item" });
		}
	},
	BUY_FREQ(3, EnumQueryType.NUMBER, "?0.customerno", "购买次数(人-店-天)") {

		public String toSql() {
			return "count(distinct cast(?0.created as date ), ?0.dp_id)";
		}

		public List<String> getOrderedTableNameList() {
			return Lists.newArrayList(new String[] { "plt_taobao_order_item" });
		}
	},

	LAST_INTERVAL(4, EnumQueryType.NUMBER, "?0.customerno", "未光顾天数") {

		public String toSql() {
			return "timestampdiff(day, max(cast(?0.created as date )), cast(now() as date))";
		}

		public List<String> getOrderedTableNameList() {
			return Lists.newArrayList(new String[] { "plt_taobao_order_item" });
		}
	},

	FIRST_INTERVAL(5, EnumQueryType.NUMBER, "?0.customerno", "第一次购买间隔（天）") {

		public String toSql() {
			return "timestampdiff(day, min(cast(?0.created as date)), cast(now() as date))";
		}

		public List<String> getOrderedTableNameList() {
			return Lists.newArrayList(new String[] { "plt_taobao_order_item" });
		}
	},

	LAST_BUY_TIME(6, EnumQueryType.DATETIME, "?0.customerno", "最后一次购买时间") {

		public String toSql() {
			return "max(cast(?0.created as date ))";
		}

		public List<String> getOrderedTableNameList() {
			return Lists.newArrayList(new String[] { "plt_taobao_order_item" });
		}
	},

	FIRST_BUY_TIME(7, EnumQueryType.DATETIME, "?0.customerno", "第一次购买时间") {

		public String toSql() {
			return "min(cast(?0.created as date ))";
		}

		public List<String> getOrderedTableNameList() {
			return Lists.newArrayList(new String[] { "plt_taobao_order_item" });
		}
	},
	AVG_BUY_FEE(8, EnumQueryType.NUMBER, "?0.customerno", "平均客单价（人-店-天）") {

		public String toSql() {
			return QuotaHelper.ensureNoDivisionByZero("sum(?0.total_fee)",
					"count(distinct cast(?0.created as date), ?0.dp_id) ");
		}

		public List<String> getOrderedTableNameList() {
			return Lists.newArrayList(new String[] { "plt_taobao_order_item" });
		}
	},

	AVG_BUY_FREQ(9, EnumQueryType.NUMBER, "?0.customerno", "平均购买周期（人-店-天）") {

		public String toSql() {
			StringBuilder sql = new StringBuilder();
			sql.append("count(distinct (cast(?0.created as date )) ) > 1 "); // 购买次数要大于1
			String expr = QuotaHelper.ensureNoDivisionByZero(
					"(to_days(max(cast(?0.created as date ))) - to_days(min(cast(?0.created as date ))))",
					"(count(distinct (cast(?0.created as date ))) -1)");
			sql.append(" and ").append(expr);// 最后一次购买时间-第一次购买时间）天/(购买次数-1)
			return sql.toString();
		}

		public List<String> getOrderedTableNameList() {
			return Lists.newArrayList(new String[] { "plt_taobao_order_item" });
		}

	},
	REFUND_NUM(10, EnumQueryType.NUMBER, "?0.customerno", "退款订单数") {

		public String toSql() {
			return "count(distinct(case when i.refund_fee > 0 and i.refund_status='SUCCESS' then i.tid else null end ))";
		}

		public List<String> getOrderedTableNameList() {
			return Lists.newArrayList(new String[] { "plt_taobao_order_item" });
		}
	},

	REFUND_FEE(11, EnumQueryType.NUMBER, "?0.customerno", "退款金额") {

		public String toSql() {
			return "sum( case when ?0.refund_status='SUCCESS' then ?0.refund_fee else 0 end )";
		}

		public List<String> getOrderedTableNameList() {
			return Lists.newArrayList(new String[] { "plt_taobao_order_item" });
		}
	};

	private int key;
	private EnumQueryType type;
	private String group;
	private String chineseName;

	private QuotaOrderItem(int key, EnumQueryType type, String group, String chineseName) {
		this.key = key;
		this.type = type;
		this.group = group;
		this.chineseName = chineseName;
	}

	public abstract String toSql();

	public abstract List<String> getOrderedTableNameList();

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public EnumQueryType getType() {
		return type;
	}

	public void setType(EnumQueryType type) {
		this.type = type;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
}
