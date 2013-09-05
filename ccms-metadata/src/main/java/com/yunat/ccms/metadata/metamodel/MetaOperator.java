package com.yunat.ccms.metadata.metamodel;

/**
 * 元数据：查询运算符
 * 
 * @author kevin.jiang 2013-3-13
 */
public enum MetaOperator {

	GT(1, "gt", " > ", "大于") {

	},
	LT(2, "lt", " < ", "小于") {

	},
	GE(3, "ge", " >= ", "大于等于") {

	},
	LE(4, "le", " <= ", "小于等于") {

	},
	EQ(5, "eq", " = ", "等于") {

	},
	LIKE(6, "like", " like ", "包含") {

	},
	NE(7, "ne", " != ", "不等于") {

	},
	NOTLIKE(8, "notlike", " not like ", "不包含") {

	},
	ISNULL(9, "isnull", " is null ", "为空") {

	},
	NOTNULL(10, "is not null", " is not null ", "不为空") {

	},
	EXISTS(11, "exists", " exists ", "存在") {

	},
	NOTEXISTS(12, "not exists", " not exists ", "不存在") {

	};
	private MetaOperator(int key, String opName, String sql, String chineseName) {

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
