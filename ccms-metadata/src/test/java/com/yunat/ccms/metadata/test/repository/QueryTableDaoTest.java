package com.yunat.ccms.metadata.test.repository;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.metadata.pojo.DatabaseTable;
import com.yunat.ccms.metadata.pojo.Query;
import com.yunat.ccms.metadata.pojo.QueryTable;
import com.yunat.ccms.metadata.repository.DatabaseTableDao;
import com.yunat.ccms.metadata.repository.QueryDao;
import com.yunat.ccms.metadata.repository.QueryTableDao;
import com.yunat.ccms.metadata.test.base.MetaDataContextBaseTest;

@Component
public class QueryTableDaoTest extends MetaDataContextBaseTest {

	@Autowired
	QueryTableDao queryTableDao;

	@Autowired
	private DatabaseTableDao databaseTableDao;

	@Autowired
	QueryDao queryDao;

	@Test
	public void testSave() {

		DatabaseTable table = new DatabaseTable();
		table.setCreated(new Date());
		table.setDbName("order");
		table.setPkColumn("tid");
		table.setPlatCode("taobao");
		table.setRemark("remark");
		table.setShowName("订单");
		table.setUpdated(new Date());
		table = databaseTableDao.save(table);

		Query query = new Query();
		query.setPhyView("view_order");
		query.setPlatCode("taobao");
		query.setShowName("template_1");
		query = queryDao.save(query);

		QueryTable qt = new QueryTable();
		qt.setDbTable(table);
		qt.setQuery(query);
		queryTableDao.save(qt);
	}
}
