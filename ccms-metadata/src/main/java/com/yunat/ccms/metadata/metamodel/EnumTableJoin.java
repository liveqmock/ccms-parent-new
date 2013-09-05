package com.yunat.ccms.metadata.metamodel;
/**
 * 元数据：表连接枚举类型
 * @author kevin.jiang
 * 2013-3-14
 */
public enum EnumTableJoin {

	INNER(1, "inner join", " inner join ", "内连接"), LEFT(2, "left join", " left join ", "左外连"), RIGHT(3, "right join",
			" right join ", "右外连");

	private EnumTableJoin(int key, String joinName, String sql, String chineseName) {
		this.key = key;
		this.joinName = joinName;
		this.sql = sql;
		this.chineseName = chineseName;
	}

	private int key;
	private String joinName;
	private String sql;
	private String chineseName;

	public int getKey() {
		return key;
	}

	public String getJoinName() {
		return joinName;
	}

	public String toSql() {
		return sql;
	}

	public String getChineseName() {
		return chineseName;
	}

}
