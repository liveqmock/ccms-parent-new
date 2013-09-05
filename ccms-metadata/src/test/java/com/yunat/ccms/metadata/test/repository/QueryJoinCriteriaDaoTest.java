package com.yunat.ccms.metadata.test.repository;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.metadata.pojo.DatabaseColumn;
import com.yunat.ccms.metadata.pojo.DatabaseTable;
import com.yunat.ccms.metadata.pojo.Query;
import com.yunat.ccms.metadata.pojo.QueryJoinCriteria;
import com.yunat.ccms.metadata.repository.DatabaseColumnDao;
import com.yunat.ccms.metadata.repository.DatabaseTableDao;
import com.yunat.ccms.metadata.repository.QueryDao;
import com.yunat.ccms.metadata.repository.QueryJoinCriteriaDao;
import com.yunat.ccms.metadata.test.base.MetaDataContextBaseTest;

@Component
public class QueryJoinCriteriaDaoTest extends MetaDataContextBaseTest {

	@Autowired
	QueryJoinCriteriaDao queryJoinCriteriaDao;

	@Autowired
	DatabaseColumnDao databaseColumnDao;

	@Autowired
	private DatabaseTableDao databaseTableDao;

	@Autowired
	QueryDao queryDao;

	@Test
	public void testSave() {

		Query query = new Query();
		query.setPhyView("view_order");
		query.setPlatCode("taobao");
		query.setShowName("template_1");

		query = queryDao.save(query);

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
		column = databaseColumnDao.save(column);

		DatabaseTable table_b = new DatabaseTable();
		table_b.setCreated(new Date());
		table_b.setDbName("buy_people");
		table_b.setPkColumn("pid");
		table_b.setPlatCode("taobao");
		table_b.setRemark("remark");
		table_b.setShowName("买家");
		table_b.setUpdated(new Date());
		table_b = databaseTableDao.save(table_b);

		DatabaseColumn column_b = new DatabaseColumn();
		column_b.setColumnName("tid");
		column_b.setColumnType("string");
		column_b.setCreated(new Date());
		column_b.setRemark("remark");
		column_b.setIsPK(false);
		column_b.setShowName("订单ID");
		column_b.setUpdated(new Date());
		column_b.setTable(table_b);
		column_b = databaseColumnDao.save(column_b);

		QueryJoinCriteria qjc = new QueryJoinCriteria();
		qjc.setJoinType("join");
		qjc.setLeftColumn(column);
		qjc.setRightColumn(column);
		qjc.setQuery(query);
		queryJoinCriteriaDao.save(qjc);
	}
}
