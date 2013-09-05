package com.yunat.ccms.metadata.test.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.metadata.pojo.Query;
import com.yunat.ccms.metadata.repository.QueryDao;
import com.yunat.ccms.metadata.test.base.MetaDataContextBaseTest;

@Component
public class QueryDaoTest extends MetaDataContextBaseTest {

	@Autowired
	QueryDao queryDao;

	@Test
	public void testSave() {

		Query query = new Query();
		query.setPhyView("view_order");
		query.setPlatCode("taobao");
		query.setShowName("template_1");
		queryDao.save(query);
	}
}
