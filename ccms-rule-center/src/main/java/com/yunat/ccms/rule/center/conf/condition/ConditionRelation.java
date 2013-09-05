package com.yunat.ccms.rule.center.conf.condition;

public enum ConditionRelation {
	AND("&&"), OR("||");

	private String symbol;

	public static ConditionRelation valueOfIgnoreCase(final String relationValue) {
		for (final ConditionRelation t : values()) {
			if (t.name().equalsIgnoreCase(relationValue)) {
				return t;
			}
		}
		return null;
	}

	private ConditionRelation(String _symbol) {
		this.symbol = _symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

}
