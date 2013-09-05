package com.yunat.ccms.module.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.yunat.ccms.auth.AuthContext;
import com.yunat.ccms.auth.AuthContextImpl;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.core.support.auth.SupportOp;
import com.yunat.ccms.core.support.cons.ProductEdition;
import com.yunat.ccms.module.Module;
import com.yunat.ccms.module.ModuleService;

public class ModuleAuthServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	ModuleEntryServiceImpl moduleEntryService = new ModuleEntryServiceImpl() {
		@Override
		protected Collection<ModuleEntry> getModuleEntriesFromDB() {
			long id = 1L;
			final Collection<ModuleEntry> rt = new ArrayList<ModuleEntry>();
			{
				final ModuleEntry entry = new ModuleEntry();
				entry.setId(id++);
				entry.setSupportOpsMask(SupportOp.toMask(EnumSet.noneOf(SupportOp.class)));
				rt.add(entry);
			}
			{
				final ModuleEntry entry = new ModuleEntry();
				entry.setId(id++);
				entry.setModuleId(ModuleServiceTest.ID_1);
				entry.setSupportOpsMask(SupportOp.toMask(EnumSet.of(SupportOp.VIEW)));
				rt.add(entry);
			}
			return rt;
		}
	};

	ModuleService moduleService = new ModuleServiceForTest();

	ModuleAuthServiceImpl service = new ModuleAuthServiceImpl();

	{
		try {
			moduleEntryService.setModuleService(moduleService);
			service.setModuleEntryService(moduleEntryService);
			service.setModuleService(moduleService);
			moduleEntryService.afterPropertiesSet();
			service.setApplicationContext(null);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAuthorizedModules() {
		final User user = new User();
		user.setId(1L);
		user.setRealName("真名");
		user.setLoginName("登录名");
		user.setUserType("built-in");

		final HttpServletRequest request = new Request("http://.../module/" + ModuleServiceTest.ID_1);
		final ProductEdition productEdition = ProductEdition.STANDARD;
		final AuthContext authContext = new AuthContextImpl(user, request, productEdition);

		final Module module = service.authorizeModule(authContext, ModuleServiceTest.ID_1);
		System.out.println("@@@@@@ModuleAuthServiceTest.testGetAuthorizedModules():" + module);
	}

}
