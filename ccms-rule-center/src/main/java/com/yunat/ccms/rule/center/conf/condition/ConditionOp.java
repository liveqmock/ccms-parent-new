package com.yunat.ccms.rule.center.conf.condition;

/**
 * 指标比较符.
 * 参见:条件{@link Condition}
 * 
 * @author wenjian.liang
 */
public enum ConditionOp {

	/**
	 * 等于
	 */
	EQ("等于", "=="), //
	/**
	 * 大于
	 */
	GT("大于", ">"), //
	/**
	 * 大于等于
	 */
	GE("大于等于", ">="), //
	/**
	 * 小于
	 */
	LT("小于", "<"), //
	/**
	 * 小于等于
	 */
	LE("小于等于", "<="), //
	/**
	 * 不等于
	 */
	NOT_EQ("不等于", "!="), //
	/**
	 * 包含
	 */
	IN("包含", "contains"), //
	/**
	 * 字符串型的包含
	 */
	LIKE("包含", "matches"), //
	/**
	 * 包含任意一个.可用于商品
	 */
	CONTAINS_ANY("包含任意一个", "contains"), //
	;

	public final String label;
	public final String symbol;

	public static ConditionOp valueOfIgnoreCase(final String conditionOpName) {
		for (final ConditionOp t : values()) {
			if (t.name().equalsIgnoreCase(conditionOpName)) {
				return t;
			}
		}
		return null;
	}

	private ConditionOp(final String label, final String symbol) {
		this.label = label;
		this.symbol = symbol;
	}

	public String getName() {
		return name();
	}

	public String getLabel() {
		return label;
	}

	public String getSymbol() {
		return symbol;
	}

}
