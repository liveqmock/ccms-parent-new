package com.yunat.ccms.metadata.test.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.metadata.pojo.ReferType;
import com.yunat.ccms.metadata.repository.ReferTypeDao;
import com.yunat.ccms.metadata.test.base.MetaDataContextBaseTest;

@Component
public class ReferTypeDaoTest extends MetaDataContextBaseTest {

	@Autowired
	ReferTypeDao referTypeDao;

	@Test
	public void testSave() {

		ReferType rt = new ReferType();
		rt.setPlatCode("taobao");
		rt.setReferCriteriaSql("where 1=1");
		rt.setReferName("field1");
		rt.setReferTable("plt_taobao_order");
		rt.setReferKey("field2");
		rt.setRemark("remark");
		referTypeDao.save(rt);
	}
}
