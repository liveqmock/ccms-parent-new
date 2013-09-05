package com.yunat.ccms.metadata.test.repository;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.metadata.pojo.DatabaseColumn;
import com.yunat.ccms.metadata.pojo.DatabaseTable;
import com.yunat.ccms.metadata.repository.DatabaseColumnDao;
import com.yunat.ccms.metadata.repository.DatabaseTableDao;
import com.yunat.ccms.metadata.test.base.MetaDataContextBaseTest;

@Component
public class DatabaseColumnDaoTest extends MetaDataContextBaseTest {

	@Autowired
	DatabaseColumnDao databaseColumnDao;

	@Autowired
	DatabaseTableDao databaseTableDao;

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

		DatabaseColumn column = new DatabaseColumn();
		column.setColumnName("tid");
		column.setColumnType("string");
		column.setCreated(new Date());
		column.setIsPK(true);
		column.setRemark("remark");
		column.setShowName("订单ID");
		column.setUpdated(new Date());
		column.setTable(table);
		databaseColumnDao.save(column);
	}
}
