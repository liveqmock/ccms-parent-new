package com.yunat.ccms.dashboard.tool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class JdbcPaginationExtra<E> {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<E> fetchPage(
	            final JdbcTemplate jt,
	            //获取总行数的sql
	            final String sqlCountRows,
	            //获取记录集的sql
	            final String sqlFetchRows,
	            //参数
	            final Object args[],
	            //当前页
	            final int pageNo,
	            //单页条数
	            final int pageSize,
	            //spring提供参数化行转行的接口
	            final ParameterizedRowMapper<E> rowMapper) {

	        // 确定记录行数
	        final int rowCount = jt.queryForInt(sqlCountRows, args);

	        // 计算页的总数
	        int pageCount = rowCount / pageSize;
	        if (rowCount > pageSize * pageCount) {
	            pageCount++;
	        }

	        // 创建当前页对象
	        final Page<E> page = new Page<E>();
	        page.setPageNumber(pageNo);
	        page.setPagesAvailable(pageCount);

	        // 获取某一页上的结果集
	        final int startRow = (pageNo - 1) * pageSize;
	        jt.query(
	                sqlFetchRows,
	                args,
	                new ResultSetExtractor() {
	                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
	                        final List pageItems = page.getPageItems();
	                        int currentRow = 0;
	                        while (rs.next() && currentRow < startRow + pageSize) {
	                            if (currentRow >= startRow) {
	                                pageItems.add(rowMapper.mapRow(rs, currentRow));
	                            }
	                            currentRow++;
	                        }
	                        return page;
	                    }
	                });
	        return page;
	    }
}
