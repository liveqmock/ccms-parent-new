package com.yunat.ccms.channel.repository;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;

public class BlackListRepositoryManagerTest extends AbstractJunit4SpringContextBaseTests {

	@Autowired
	JdbcPaginationHelper jdbcPaginationHelper;

	@Test
	public void testGetBlackListRepository() {
		String FetchRowSql = "select email as contact from tw_email_blacklist where email like :contact";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("contact", "%yunat.net%");
		Page<String> page = jdbcPaginationHelper.queryForList(FetchRowSql, map, new PageRequest(0,
				15), String.class);

		System.out.println(page.getContent().size());
	}
}
