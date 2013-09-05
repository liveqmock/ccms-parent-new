package com.yunat.ccms.metadata.test.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.metadata.pojo.DicType;
import com.yunat.ccms.metadata.pojo.DicTypeValue;
import com.yunat.ccms.metadata.repository.DicTypeDao;
import com.yunat.ccms.metadata.repository.DicTypeValueDao;
import com.yunat.ccms.metadata.test.base.MetaDataContextBaseTest;

@Component
public class DicTypeValueDaoTest extends MetaDataContextBaseTest {

	@Autowired
	DicTypeValueDao dicTypeValueDao;

	@Autowired
	DicTypeDao dicTypeDao;

	@Test
	public void testSave() {

		DicType dt = new DicType();
		dt.setPlatCode("taobao");
		dt.setShowName("province");
		dt = dicTypeDao.save(dt);

		DicTypeValue dtv = new DicTypeValue();
		dtv.setDicType(dt);
		dtv.setRemark("remark");
		dtv.setShowName("上海");
		dtv.setTypeValue("上海市");
		dicTypeValueDao.save(dtv);
	}
}
