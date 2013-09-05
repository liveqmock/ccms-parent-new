package com.yunat.ccms.auth.bizdata.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.UnloadedSidException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import com.yunat.ccms.auth.PermissionTranslate;
import com.yunat.ccms.auth.bizdata.web.AuthedJacksonMessageConverter;
import com.yunat.ccms.auth.bizdata.web.AuthedJacksonMessageConverter.Mapping;
import com.yunat.ccms.auth.role.Role;
import com.yunat.ccms.auth.user.User;

public class AuthedJacksonViewTest {

	private final Random r = new Random();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddSupportedOps() throws IOException {
		System.out.println("@@@@@@AuthedJacksonView.main():start");
		final Role role1 = new Role();
		role1.setId(1L);
		role1.setName("admin");

		final Role role2 = new Role();
		role2.setId(2L);
		role2.setName("admin");

		final User user1 = new User();
		user1.setId(1L);
		user1.setLoginName("user1");
		user1.setRoles(new HashSet<Role>(Arrays.asList(role1, role2)));

		final User user2 = new User();
		user2.setId(2L);
		user2.setLoginName("user2");
		user2.setRoles(new HashSet<Role>(Arrays.asList(role2)));

		final Act act1 = new Act();
		act1.setId(1);
		act1.setName("act1");
		act1.setCreater(user1);

		final Act act2 = new Act();
		act2.setId(2);
		act2.setName("act2");
		act2.setCreater(user2);

		final Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("user", user1);
		map.put("acts", Arrays.asList(act1, act2));

		//这些值都没有关系...
		final SecurityContext context = new SecurityContextImpl();
		final Authentication authentication = new UsernamePasswordAuthenticationToken("lwj", "123456");
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);

		final AuthedJacksonMessageConverter view = new AuthedJacksonMessageConverter();
		view.setSidRetrievalStrategy(new SidRetrievalStrategyImpl());
		view.setAclService(new AclService() {

			@Override
			public Map<ObjectIdentity, Acl> readAclsById(final List<ObjectIdentity> objects, final List<Sid> sids)
					throws NotFoundException {
				return null;
			}

			@Override
			public Map<ObjectIdentity, Acl> readAclsById(final List<ObjectIdentity> objects) throws NotFoundException {
				return null;
			}

			@Override
			public Acl readAclById(final ObjectIdentity object, final List<Sid> sids) throws NotFoundException {
				final List<AccessControlEntry> aces = new ArrayList<AccessControlEntry>();
				@SuppressWarnings("serial")
				final Acl acl = new Acl() {
					@Override
					public boolean isSidLoaded(final List<Sid> sids) {
						return true;
					}

					@Override
					public boolean isGranted(final List<Permission> permission, final List<Sid> sids,
							final boolean administrativeMode) throws NotFoundException, UnloadedSidException {
						return r.nextBoolean();//随机授权
					}

					@Override
					public boolean isEntriesInheriting() {
						return true;
					}

					@Override
					public Acl getParentAcl() {
						return null;
					}

					@Override
					public Sid getOwner() {
						return null;
					}

					@Override
					public ObjectIdentity getObjectIdentity() {
						return object;
					}

					@Override
					public List<AccessControlEntry> getEntries() {
						return aces;
					}
				};
				for (final Sid sid : sids) {
					for (final PermissionTranslate pt : PermissionTranslate.values()) {
						aces.add(new AccessControlEntryImpl(object.getIdentifier(), acl,//
								sid, pt.getPermissions().get(0), true, false, false));
					}
				}
				return acl;
			}

			@Override
			public Acl readAclById(final ObjectIdentity object) throws NotFoundException {
				return null;
			}

			@Override
			public List<ObjectIdentity> findChildren(final ObjectIdentity parentIdentity) {
				return null;
			}
		});
		final ObjectMapper objectMapper = new ObjectMapper();
		final JsonGenerator generator = objectMapper.getJsonFactory()
				.createJsonGenerator(System.out, JsonEncoding.UTF8);

		final Set<Mapping> mappings = new HashSet<Mapping>();
		view.modelToMappings(map, mappings, new LinkedList<Object>());
		if (mappings.isEmpty()) {
			objectMapper.writeValue(generator, map);
		} else {
			view.addSupportOps(map, generator, mappings);
		}
		System.out.println();
		System.out.println("@@@@@@AuthedJacksonView.main():end");
	}

	/**
	 * 一个简单的类,假设是活动类
	 * 
	 * @author MaGiCalL
	 */
	public static class Act {
		User creater;
		int id;
		String name;

		public User getCreater() {
			return creater;
		}

		public void setCreater(final User creater) {
			this.creater = creater;
		}

		public int getId() {
			return id;
		}

		public void setId(final int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}
	}
}
