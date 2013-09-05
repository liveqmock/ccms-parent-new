package com.yunat.ccms.metadata.test.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.metadata.pojo.Catalog;
import com.yunat.ccms.metadata.repository.ShowCatalogDao;
import com.yunat.ccms.metadata.test.base.MetaDataContextBaseTest;

public class ShowCatalogDaoTest extends MetaDataContextBaseTest {

	@Autowired
	ShowCatalogDao showCatalogDao;

	@Test
	public void testSave() {

		Catalog cata_p = new Catalog();
		cata_p.setCatalogType(1);
		cata_p.setShowName("客户信息");
		cata_p.setShowOrder(1);
		cata_p = showCatalogDao.save(cata_p);

		Catalog cata_c = new Catalog();
		cata_c.setParent(cata_p);
		cata_c.setCatalogType(1);
		cata_c.setShowName("客户信息");
		cata_c.setShowOrder(1);
		cata_c = showCatalogDao.save(cata_c);
	}
}
