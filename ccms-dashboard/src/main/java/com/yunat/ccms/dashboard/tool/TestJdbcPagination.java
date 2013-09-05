package com.yunat.ccms.dashboard.tool;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.yunat.ccms.dashboard.model.Notice;

@Component
public class TestJdbcPagination {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public Page<Notice> getNotices(final int pageNo, final int pageSize, String account) throws SQLException {
		JdbcPaginationExtra<Notice> ph = new JdbcPaginationExtra<Notice>();
        return ph.fetchPage(
                jdbcTemplate,
                "SELECT count(*) FROM tb_user_notice WHERE  account = ? ",
                "SELECT id, title FROM tb_user_notice WHERE account = ? ",
                new Object[]{account},
                pageNo,
                pageSize,
                new ParameterizedRowMapper<Notice>() {
                    @Override
                	public Notice mapRow(ResultSet rs, int i) throws SQLException {
                        Notice notice  = new Notice();
                        notice.setId(rs.getLong(1));
                        notice.setTitle(rs.getString(2));
                        return notice;
                    }
                }
        );
  }

}
