package com.yunat.ccms.metadata.test.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.metadata.pojo.DicType;
import com.yunat.ccms.metadata.repository.DicTypeDao;
import com.yunat.ccms.metadata.test.base.MetaDataContextBaseTest;

@Component
public class DicTypeDaoTest extends MetaDataContextBaseTest {

	@Autowired
	DicTypeDao dicTypeDao;

	@Test
	public void testSave() {

		DicType dt = new DicType();
		dt.setPlatCode("taobao");
		dt.setShowName("province111");
		dicTypeDao.save(dt);
	}
}
