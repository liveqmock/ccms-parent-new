package com.yunat.ccms.core.support.jdbc;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class JdbcPaginationHelper extends NamedParameterJdbcTemplate {

	private JdbcDialect jdbcDialect;

	public JdbcPaginationHelper(DataSource dataSource) {
		super(dataSource);
		this.jdbcDialect = new MySQLJdbcDialect();
	}

	public Page<Map<String, Object>> queryForMap(final CharSequence sql, final Map<String, ?> paramMap,
			final Pageable pageable) {


		int total = queryForInt(jdbcDialect.getPaginationCountSql(sql), paramMap);
		final int startRow = pageable.getOffset();
		final int pageSize = pageable.getPageSize();

		StringBuffer sqlBuf = new StringBuffer(sql);
		Sort sort = pageable.getSort();
		if (null != sort) {
			sqlBuf.append(" order by ");
			for (Order order : sort) {
				sqlBuf.append(order.getProperty()).append(" ").append(order.getDirection().toString()).append(",");
			}
			sqlBuf.setLength(sqlBuf.length() - 1);
		}

		List<Map<String, Object>> content = queryForList(jdbcDialect.getPaginationQuerySql(sqlBuf, startRow, pageSize), paramMap);
		return new PageImpl<Map<String, Object>>(content, pageable, total);
	}

	public <T> Page<T> queryForList(final CharSequence sql, final Map<String, ?> paramMap, final Pageable pageable,
			final Class<T> elementType) {
		int total = queryForInt(jdbcDialect.getPaginationCountSql(sql), paramMap);
		final int startRow = pageable.getOffset();
		final int pageSize = pageable.getPageSize();

		final String sqlFetchRows = jdbcDialect.getPaginationQuerySql(sql, startRow, pageSize);
		List<T> content = queryForList(sqlFetchRows, paramMap, elementType);
		return new PageImpl<T>(content, pageable, total);
	}
}
