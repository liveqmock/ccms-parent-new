package com.yunat.ccms.auth.bizdata.web;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;

import com.yunat.ccms.auth.AuthUtil;
import com.yunat.ccms.auth.PermissionTranslate;
import com.yunat.ccms.auth.login.LoginInfoHolder;
import com.yunat.ccms.auth.role.Role;
import com.yunat.ccms.auth.springsecurity.acl.AclTypeCache;
import com.yunat.ccms.auth.springsecurity.acl.AclUtil;
import com.yunat.ccms.auth.springsecurity.acl.CcmsObjectIdentityRetrievalStrategy;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.core.support.auth.AuthCons;
import com.yunat.ccms.core.support.auth.SupportOp;

/**
 * 在渲染返回的json时加入对数据的权限.
 * 
 * @author wenjian.liang
 */
public class AuthedJacksonMessageConverter extends MappingJacksonHttpMessageConverter implements
		ApplicationContextAware, InitializingBean {

	/**
	 * 内部用来临时记录返回的model的信息的类
	 * 
	 * @author MaGiCalL
	 */
	protected static class Mapping {
		ObjectIdentity oid;
		/**
		 * 在ModelMap中的路径.
		 */
		List<Object> path;

		public Mapping(final ObjectIdentity oid, final List<Object> list) {
			super();
			this.oid = oid;
			path = list;
		}
	}

	protected static final MethodFilter GETTER_FILTER = new MethodFilter() {

		@Override
		public boolean matches(final Method method) {
			return isGetter(method);
		}
	};

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected AclTypeCache aclTypeCache;
	//父类不开放的字段
	protected boolean prefixJson = false;
	protected JsonEncoding encoding = JsonEncoding.UTF8;

	protected AclService aclService;
	protected SidRetrievalStrategy sidRetrievalStrategy;

	protected ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy;

	@Override
	protected void writeInternal(final Object object, final HttpOutputMessage outputMessage)//
			throws IOException, HttpMessageNotWritableException {

		final ObjectMapper objectMapper = getObjectMapper();
		final JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
		final JsonGenerator jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(outputMessage.getBody(),
				encoding);
		try {
			if (prefixJson) {
				jsonGenerator.writeRaw("{} && ");
			}

			final Set<Mapping> mappings = new HashSet<Mapping>();
			modelToMappings(object, mappings, new LinkedList<Object>());
			if (mappings.isEmpty()) {
				//这里是父类默认算法
				objectMapper.writeValue(jsonGenerator, object);
			} else {
				//这里是我们加上去的要搞权限注入的.
				addSupportOps(object, jsonGenerator, mappings);
			}
		} catch (final JsonProcessingException ex) {
			throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}

	/**
	 * @param value
	 * @param generator
	 * @param mappings
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	protected void addSupportOps(final Object value, final JsonGenerator generator, final Set<Mapping> mappings)
			throws IOException, JsonProcessingException {
		final ObjectMapper objectMapper = getObjectMapper();
		//生成json对象树
		final JsonNode rootNode = objectMapper.valueToTree(value);
		final List<Sid> sids = getSidOfCurrentUser();

		for (final Mapping mapping : mappings) {
			final ObjectIdentity oid = mapping.oid;
			try {
				final Acl acl = aclService.readAclById(oid, sids);

				final List<Object> path = mapping.path;
				final JsonNode node = findNodeInTree(rootNode, path);
				assert node instanceof ObjectNode;

				final Collection<SupportOp> supportedOpCodes = checkSupportedOps(sids, acl);
				if (supportedOpCodes != null) {
					final String supportOpsStr = supportOpsToString(supportedOpCodes);
					//在json树上这个对象里加一个字段
					((ObjectNode) node).put(AuthCons.SUPPORT_OP_JSON_FIELD_NAME, supportOpsStr);
				} else {
					((ObjectNode) node).put(AuthCons.SUPPORT_OP_JSON_FIELD_NAME,
							supportOpsToString(Arrays.asList(SupportOp.values())));
				}
			} catch (final NotFoundException e) {
				//没有acl就认为有全部权限(即未限制)
			}
		}
		objectMapper.writeTree(generator, rootNode);
	}

	protected List<Sid> getSidOfCurrentUser() {
		//sid表示"身份",有两种:
		//"私人身份"对应PrincipalSid类,在spring security的sid表中principal字段是true
		//"角色身份"对应GrantedAuthoritySid类,在spring security的sid表中principal字段是false
		final User user = LoginInfoHolder.getCurrentUser();
		final Collection<Role> roles = user.getRoles();
		final List<Sid> sids = new ArrayList<Sid>(roles.size() + 1);
		//私人身份排在前面
		sids.add(new PrincipalSid(String.valueOf(user.getId())));
		//角色身份排在后面
		for (final Role role : roles) {
			sids.add(new GrantedAuthoritySid(String.valueOf(role.getId())));
		}
		return sids;
	}

	protected Collection<SupportOp> checkSupportedOps(final List<Sid> sids, final Acl acl) {
		final PermissionTranslate[] permissionTranslates = PermissionTranslate.values();
		final int allSupportOpsCount = permissionTranslates.length;
		final Collection<SupportOp> supportedOpCodes = new ArrayList<SupportOp>(allSupportOpsCount);

		final Permission p = AclUtil.mergePermissions(acl, sids);
		if (p == null) {
			return defaultPermission();
		}
		for (final PermissionTranslate pt : permissionTranslates) {
			final Permission p2 = pt.getPermissions().get(0);
			if ((p.getMask() & p2.getMask()) != 0) {
				supportedOpCodes.add(pt.getSupportOp());
			}
		}
		return supportedOpCodes.size() == allSupportOpsCount ? null//支持全部操作的,就不返回了,当做默认.
				: supportedOpCodes;
	}

	protected String supportOpsToString(final Collection<SupportOp> supportOps) {
		if (supportOps == null) {
			return null;
		}
		final char[] cs = new char[supportOps.size()];
		int i = 0;
		for (final SupportOp o : supportOps) {
			cs[i++] = o.getCode();
		}
		return new String(cs);
	}

	private JsonNode findNodeInTree(final JsonNode rootNode, final List<Object> path) {
		JsonNode node = rootNode;
		for (final Object p : path) {
			if (p instanceof Integer) {
				node = node.get((Integer) p);
			} else if (p instanceof String) {
				node = node.get((String) p);
			} else {
				return null;
			}
		}
		return node;
	}

	protected void modelToMappings(final Object model, final Set<Mapping> mappings, final List<Object> path) {
		if (model == null) {
			return;
		}
		if (model instanceof Map<?, ?>) {
			final Map<?, ?> map = (Map<?, ?>) model;
			for (final Entry<?, ?> e : map.entrySet()) {
				final List<Object> p = new LinkedList<Object>(path);
				p.add(e.getKey());
				modelToMappings(e.getValue(), mappings, p);
			}
		} else if (model instanceof Iterable<?>) {
			int i = 0;
			for (final Object o : (Iterable<?>) model) {
				final List<Object> p = new LinkedList<Object>(path);
				p.add(i);
				modelToMappings(o, mappings, p);
				++i;
			}
		} else if (model.getClass().isArray()) {
			final int len = Array.getLength(model);
			for (int i = 0; i < len; ++i) {
				final List<Object> p = new LinkedList<Object>(path);
				p.add(i);
				modelToMappings(Array.get(model, i), mappings, p);
			}
		} else {
			final Class<?> modelClass = model.getClass();
			if (maybeModel(modelClass)) {
				if (aclTypeCache.hasAcl(modelClass)) {
					final ObjectIdentity oid = createObjectIdentity(model);
					if (oid != null) {
						mappings.add(new Mapping(oid, path));
					}
				}

				ReflectionUtils.doWithMethods(modelClass, new MethodCallback() {
					@Override
					public void doWith(final Method method) throws IllegalArgumentException, IllegalAccessException {
						if (!method.isAccessible()) {
							method.setAccessible(true);
						}
						final Object v = ReflectionUtils.invokeMethod(method, model);
						if (v == null || BeanUtils.isSimpleProperty(v.getClass())) {
							return;
						}
						final List<Object> p = new LinkedList<Object>(path);
						final String getterName = method.getName();
						p.add(getterNameToPropertyName(getterName));
						modelToMappings(v, mappings, p);
					}

					/**
					 * @param getterName
					 * @return
					 */
					protected String getterNameToPropertyName(final String getterName) {
						return Character.toLowerCase(getterName.charAt("get".length()))
								+ getterName.substring("get".length() + 1);
					}
				}, GETTER_FILTER);
			}
		}
	}

	public static boolean isGetter(final Method method) {
		{//name check
			final String methodName = method.getName();
			if (methodName.length() <= "get".length()) {
				return false;
			}
			if (!methodName.startsWith("get")) {
				return false;
			}
			//get后的首字母不能是小写.
			//注:在无大小写区别的语言中(比如汉语),isLowerCase和isUpperCase都返回false,
			//因此只能检查该字符是否小写字符,若是,则认为是一个以"get"开头的单词的一部分,而非一个"get xxx"
			if (Character.isLowerCase(methodName.charAt("get".length()))) {
				return false;
			}
		}
		{//param check
			final Class<?>[] paramClasses = method.getParameterTypes();
			if (paramClasses != null && paramClasses.length > 0) {
				return false;
			}
		}
		{//return type check
			if (method.getReturnType() == Void.TYPE) {
				return false;
			}
		}
		return true;
	}

	protected boolean maybeModel(final Class<?> clazz) {
		return !BeanUtils.isSimpleProperty(clazz);
	}

	protected ObjectIdentity createObjectIdentity(final Object domainObject) {
		return objectIdentityRetrievalStrategy.getObjectIdentity(domainObject);
	}

	/**
	 * 表示"拥有所有权限"
	 * 
	 * @return
	 */
	protected Collection<SupportOp> permitAllPermission() {
		return AuthUtil.permitAllSupportOps();//用null表示默认,即有全部权限
	}

	protected Collection<SupportOp> defaultPermission() {
		return permitAllPermission();
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		if (aclService == null) {
			aclService = applicationContext.getBean(AclService.class);
		}
		if (sidRetrievalStrategy == null) {
			try {
				sidRetrievalStrategy = applicationContext.getBean(SidRetrievalStrategy.class);
			} catch (final Exception e) {
				sidRetrievalStrategy = new SidRetrievalStrategyImpl();
			}
		}
		if (objectIdentityRetrievalStrategy == null) {
			try {
				objectIdentityRetrievalStrategy = applicationContext.getBean(ObjectIdentityRetrievalStrategy.class);
			} catch (final Exception e) {
				objectIdentityRetrievalStrategy = new CcmsObjectIdentityRetrievalStrategy();
			}
		}
		if (aclTypeCache == null) {
			aclTypeCache = applicationContext.getBean(AclTypeCache.class);
		}
	}

	@Override
	protected Object readInternal(final Class<? extends Object> clazz, final HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		return super.readInternal(clazz, inputMessage);
	}

	public AclService getAclService() {
		return aclService;
	}

	public void setAclService(final AclService aclService) {
		this.aclService = aclService;
	}

	public SidRetrievalStrategy getSidRetrievalStrategy() {
		return sidRetrievalStrategy;
	}

	public void setSidRetrievalStrategy(final SidRetrievalStrategy sidRetrievalStrategy) {
		this.sidRetrievalStrategy = sidRetrievalStrategy;
	}

	public boolean getPrefixJson() {
		return prefixJson;
	}

	public JsonEncoding getEncoding() {
		return encoding;
	}

	public ObjectIdentityRetrievalStrategy getObjectIdentityRetrievalStrategy() {
		return objectIdentityRetrievalStrategy;
	}

	public void setObjectIdentityRetrievalStrategy(final ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy) {
		this.objectIdentityRetrievalStrategy = objectIdentityRetrievalStrategy;
	}

	public void setEncoding(final JsonEncoding encoding) {
		this.encoding = encoding;
	}

	public AclTypeCache getAclTypeCache() {
		return aclTypeCache;
	}

	public void setAclTypeCache(final AclTypeCache aclTypeCache) {
		this.aclTypeCache = aclTypeCache;
	}

	@Override
	public void setPrefixJson(final boolean prefixJson) {
		this.prefixJson = prefixJson;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//增加一个设置:返回json时把null值换成空字符串
		final ObjectMapper objectMapper = getObjectMapper();
		objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(final Object value, final JsonGenerator jgen, final SerializerProvider provider)
					throws IOException, JsonProcessingException {
				jgen.writeString("");
			}
		});
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//下面这句 返回的Date类型字段统一格式化为"yyyy-MM-dd HH:mm:ss"
//		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}
}
