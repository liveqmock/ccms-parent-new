package com.yunat.ccms.metadata.metamodel;

/**
 * 元数据：关系运算符枚举类型
 *
 * @author kevin.jiang 2013-3-13
 */
public enum EnumRelation {

	AND(1, "and", " and ", "并且"), OR(2, "or", " or ", "或者");
	private EnumRelation(int key, String opName, String sql, String chineseName) {

		this.key = key;
		this.opName = opName;
		this.sql = sql;
		this.chineseName = chineseName;
	}

	private int key;
	private String opName;
	private String sql;
	private String chineseName;

	public int getKey() {
		return key;
	}

	public String getOpName() {
		return opName;
	}

	public String toSql() {
		return sql;
	}

	public String getChineseName() {
		return chineseName;
	}
}
