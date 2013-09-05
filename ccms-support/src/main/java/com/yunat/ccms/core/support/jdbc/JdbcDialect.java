package com.yunat.ccms.core.support.jdbc;

public interface JdbcDialect {
	public String getPaginationCountSql(CharSequence sql);

	public String getPaginationQuerySql(CharSequence sql, int startRow, int pageSize);

}