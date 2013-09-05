package com.yunat.ccms.metadata.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 查询执行器
 *
 * @author kevin.jiang 2013-3-20
 */
@Component
public class JdbcExecutor {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public void execute(String sql) {

		jdbcTemplate.execute(sql);
	}
}
