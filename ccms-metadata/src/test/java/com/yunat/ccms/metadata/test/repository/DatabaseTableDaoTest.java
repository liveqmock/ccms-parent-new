package com.yunat.ccms.metadata.test.repository;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.metadata.pojo.DatabaseTable;
import com.yunat.ccms.metadata.repository.DatabaseTableDao;
import com.yunat.ccms.metadata.test.base.MetaDataContextBaseTest;

@Component
public class DatabaseTableDaoTest extends MetaDataContextBaseTest {

	@Autowired
	private DatabaseTableDao databaseTableDao;

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
		databaseTableDao.save(table);
	}
}
