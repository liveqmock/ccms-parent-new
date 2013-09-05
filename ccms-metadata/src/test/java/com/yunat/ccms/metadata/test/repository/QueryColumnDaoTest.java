package com.yunat.ccms.metadata.test.repository;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.metadata.pojo.DatabaseColumn;
import com.yunat.ccms.metadata.pojo.DatabaseTable;
import com.yunat.ccms.metadata.pojo.DicType;
import com.yunat.ccms.metadata.pojo.Query;
import com.yunat.ccms.metadata.pojo.QueryTable;
import com.yunat.ccms.metadata.pojo.ReferType;
import com.yunat.ccms.metadata.repository.DatabaseColumnDao;
import com.yunat.ccms.metadata.repository.DatabaseTableDao;
import com.yunat.ccms.metadata.repository.DicTypeDao;
import com.yunat.ccms.metadata.repository.QueryColumnDao;
import com.yunat.ccms.metadata.repository.QueryDao;
import com.yunat.ccms.metadata.repository.QueryTableDao;
import com.yunat.ccms.metadata.repository.ReferTypeDao;
import com.yunat.ccms.metadata.test.base.MetaDataContextBaseTest;

public class QueryColumnDaoTest extends MetaDataContextBaseTest {

	@Autowired
	QueryColumnDao queryColumnDao;

	@Autowired
	QueryTableDao queryTableDao;

	@Autowired
	private DatabaseTableDao databaseTableDao;

	@Autowired
	QueryDao queryDao;

	@Autowired
	DatabaseColumnDao databaseColumnDao;

	@Autowired
	DicTypeDao dicTypeDao;

	@Autowired
	ReferTypeDao referTypeDao;

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
		column = databaseColumnDao.save(column);

		Query query = new Query();
		query.setPhyView("view_order");
		query.setPlatCode("taobao");
		query.setShowName("template_1");
		query = queryDao.save(query);

		QueryTable qt = new QueryTable();
		qt.setDbTable(table);
		qt.setQuery(query);
		qt = queryTableDao.save(qt);

		DicType dt = new DicType();
		dt.setPlatCode("taobao");
		dt.setShowName("province");
		dt = dicTypeDao.save(dt);

		ReferType rt = new ReferType();
		rt.setPlatCode("taobao");
		rt.setReferCriteriaSql("where 1=1");
		rt.setReferName("field1");
		rt.setReferTable("plt_taobao_order");
		rt.setReferKey("field2");
		rt.setRemark("remark");
		rt = referTypeDao.save(rt);

	}
}
