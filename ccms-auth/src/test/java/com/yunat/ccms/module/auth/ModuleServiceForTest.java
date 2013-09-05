package com.yunat.ccms.module.auth;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;

import com.yunat.ccms.core.support.auth.SupportOp;
import com.yunat.ccms.module.ModuleFromDB;
import com.yunat.ccms.module.ModuleServiceImpl;
import com.yunat.ccms.module.ModuleType;

public class ModuleServiceForTest extends ModuleServiceImpl {

	@Override
	protected Collection<ModuleFromDB> getModulesFromDB() {
		final ModuleFromDB module = new ModuleFromDB();
		module.setId(ModuleServiceTest.ID_1);
		module.setModuleTypeId(ModuleServiceTest.ID_1);

		return Arrays.asList(module);
	}

	@Override
	protected Map<Long, ModuleType> getModuleTypeFromDB() {
		final ModuleType type = new ModuleType();
		type.setId(ModuleServiceTest.ID_1);
		type.setLowestEditionRequired(0);
		type.setName(ModuleServiceTest.MODULE_NAME);
		type.setNamePlus("首页页面");
		type.setMemo("测试");
		type.setSupportOpsMask(SupportOp.toMask(EnumSet.of(SupportOp.VIEW, SupportOp.CLICK)));
		type.setTip("可点击");

		return Collections.singletonMap(type.getId(), type);
	}

	void printCache() {
		System.out.println("@@@@@@ModuleServiceTest.service.new ModuleServiceImpl() {...}.printCache():"
				+ super.getCachedObject());
	}
}