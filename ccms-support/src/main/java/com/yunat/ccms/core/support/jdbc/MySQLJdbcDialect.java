package com.yunat.ccms.core.support.jdbc;

public class MySQLJdbcDialect implements JdbcDialect {

	@Override
	public String getPaginationCountSql(CharSequence sql) {
		return new StringBuffer("select count(*) from (").append(sql).append(") t").toString();
	}

	@Override
	public String getPaginationQuerySql(CharSequence sql, int startRow, int pageSize) {
		StringBuffer fetchSql = sql instanceof StringBuffer ? ((StringBuffer) sql).insert(0, "select * from (").append(
				") t limit " + startRow + ", " + pageSize) : new StringBuffer("select * from (").append(sql).append(
				") t limit " + startRow + ", " + pageSize);
		return fetchSql.toString();

	}

}