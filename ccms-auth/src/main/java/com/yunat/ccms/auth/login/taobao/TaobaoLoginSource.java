package com.yunat.ccms.auth.login.taobao;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.yunat.ccms.auth.exceptions.DisabledUserException;
import com.yunat.ccms.auth.exceptions.IllegalLoginParamException;
import com.yunat.ccms.auth.login.LoginSource;
import com.yunat.ccms.auth.role.Role;
import com.yunat.ccms.auth.role.RoleRepository;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.auth.user.UserService;
import com.yunat.ccms.auth.user.UserType;
import com.yunat.ccms.core.support.cons.ProductEdition;

@Component
public class TaobaoLoginSource implements LoginSource {
	// 后面这串字符串是从ccms3抄来的,不知道怎么来的...
	static final String SECRET = "4283e0d7a760229ab34bca67cc87fcf2";

	private static final String SHOP_ID_SUFFIX = "taobao_";
	private static final int SHOP_ID_SUFFIX_LEN = SHOP_ID_SUFFIX.length();

	private static final Map<ProductEdition, String> TAOBAO_LOGIN_URL = Maps.newHashMap();
	static {
		TAOBAO_LOGIN_URL.put(ProductEdition.FREE,
				"http://fuwu.taobao.com/using/serv_using.htm?service_code=ts-11631&item_code=ts-11631-v17");

		// @何建伟 :未登录用户访问L1版本的页面,在订购中心即已经进行登录跳转,不会来到ccms
		// 因此L1版本的事实上不会用到.
		TAOBAO_LOGIN_URL.put(ProductEdition.BASIC_L1,
				"http://fuwu.taobao.com/using/serv_using.htm?service_code=ts-11631&item_code=ts-11631-v18");

		TAOBAO_LOGIN_URL.put(ProductEdition.BASIC_L2,
				"http://fuwu.taobao.com/using/serv_using.htm?service_code=ts-11631&item_code=ts-11631-v19");
		TAOBAO_LOGIN_URL.put(ProductEdition.BASIC_L3,
				"http://fuwu.taobao.com/using/serv_using.htm?service_code=ts-11631&item_code=ts-11631-v16xx");
		// 标准版是显示登录框,进行本站登录.而非跳转到淘宝进行登录.故为空字符串,与前端约定,空字符串表示本站登录
		TAOBAO_LOGIN_URL.put(ProductEdition.STANDARD, "");
	}

	private static final long ADMIN_ROLE_ID = 100000L;
	private static final long USER_ROLE_ID = 100001L;

	static Logger logger = LoggerFactory.getLogger(TaobaoLoginSource.class);

	@Autowired
	private TaobaoUserRepository taobaoUserRepository;
	@Autowired
	private TaobaoShopRepository taobaoShopRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserService userService;

	@Override
	public String getPlatformName() {
		return "淘宝";
	}

	@Override
	public String loginUrl(final ProductEdition productEdition) {
		return TAOBAO_LOGIN_URL.get(productEdition);
	}

	@Override
	public boolean support(final HttpServletRequest request, final ProductEdition productEdition) {
		return request.getParameter("top_appkey") != null;
	}

	@Override
	public User authentication(final HttpServletRequest request) throws IllegalLoginParamException {
		final TopParams params = parseParams(request);
		return authenticationFromParams(params);
	}

	/**
	 * @param params
	 * @return
	 */
	protected User authenticationFromParams(final TopParams params) {
		final User user = checkUser(params);
		return user;// 什么情况下会是null?
	}

	/**
	 * @param params
	 * @param taobaoId
	 * @param shop
	 * @return
	 */
	protected User checkUser(final TopParams params) {
		final TaobaoShop shop = checkShop(params);
		final String taobaoId = params.isSubuser() ? params.getSub_taobao_user_id() : params.getVisitor_id();

		TaobaoUser taobaoUser = taobaoUserRepository.getByPlatUserId(taobaoId);
		if (taobaoUser == null) {
			// if first time to login, save user to database
			taobaoUser = createNewUser(params, taobaoId, shop);
		} else {
			final User user = taobaoUser.getUser();
			if (Boolean.TRUE.equals(user.getDisabled())) {
				logger.error(taobaoUser.getPlatUserName() + "在系统中已经被停用");
				throw new DisabledUserException(user);
			}
		}
		return taobaoUser.getUser();
	}

	protected String getParam(final HttpServletRequest request, final String paramName)
			throws IllegalLoginParamException {
		final String value = request.getParameter(paramName);
		if (value == null) {
			throw IllegalLoginParamException.fromLoginSource(this);
		}
		return value;
	}

	protected String getParam(final Map<String, String> map, final String paramName) throws IllegalLoginParamException {
		final String value = map.get(paramName);
		if (value == null) {
			throw IllegalLoginParamException.fromLoginSource(this);
		}
		return value;
	}

	/**
	 * XXX:据@李鹏 说,订购中心已经对param进行过一次解析,在这里解析是一种重复且没有必要的事情.
	 * 由于现在要兼容3.x,先不改.考虑在以后的版本中约定订购中心更改接口.
	 * 
	 * @param paramMap
	 * @return
	 */
	protected TopParams parseParams(final HttpServletRequest request) throws IllegalLoginParamException {
		final String top_appkey = getParam(request, "top_appkey");
		final String top_parameters = getParam(request, "top_parameters");
		final String top_session = getParam(request, "top_session");
		final String top_sign = getParam(request, "top_sign");

		final String sign = request.getParameter("sign");
		final String timestamp = request.getParameter("timestamp");
		final String[] itemCode = request.getParameterValues("itemCode");

		final TopParams params = new TopParams();
		params.setTop_appkey(top_appkey);
		params.setTop_parameters(top_parameters);
		params.setTop_sign(top_sign);
		params.setTop_session(top_session);
		params.setTimestamp(timestamp);
		params.setSign(sign);
		params.setItemCode(itemCode);

		final Map<String, String> map = TaobaoLoginUtils.convertBase64StringtoMap(top_parameters);
		if (map == null) {
			throw IllegalLoginParamException.fromLoginSource(this);
		}

		final String visitor_id = getParam(map, "visitor_id");
		final String visitor_nick = getParam(map, "visitor_nick");
		//注:shop_id这一段是跟ccms3不同的!具体原因如下:
		//订购中心跳转去ccms3时,会有一个额外的参数shop_id,其值是不带前缀的纯数字的shop_id.
		//@施俊 说不要用这种方式,应该从base64那个字符串中自己解析出shop_id.于是订购中心跳转去ccms4时没有带有shop_id参数
		//而从base64字符串中解析出来的shop_id参数是带有taobao_这个前缀的.
		final String shopIdWithSuffix = getParam(map, "shop_id");
		final String shop_id = shopIdWithSuffix.substring(SHOP_ID_SUFFIX_LEN);

		final String sub_taobao_user_id = map.get("sub_taobao_user_id");
		final String sub_taobao_user_nick = map.get("sub_taobao_user_nick");
		params.setVisitor_id(visitor_id);
		params.setVisitor_nick(visitor_nick);
		params.setShop_id(shop_id);
		params.setSub_taobao_user_id(sub_taobao_user_id);
		params.setSub_taobao_user_nick(sub_taobao_user_nick);
		params.setSubuser(sub_taobao_user_id != null);

		if (!TaobaoLoginUtils.validate(params)) {
			throw IllegalLoginParamException.fromLoginSource(this);
		}
		return params;
	}

	/**
	 * @param params
	 * @return
	 */
	protected TaobaoShop checkShop(final TopParams params) {
		final String shopId = params.getShop_id();
		final TaobaoShop shop = taobaoShopRepository.findOne(shopId);
		if (shop == null) {
			logger.info("当前登录的店铺数据没有完成对接或您不是店铺人员！shopId=" + shopId);
			throw new PreAuthenticatedCredentialsNotFoundException("当前登录的店铺数据没有完成对接或您不是店铺人员！");
		}
		return shop;
	}

	/**
	 * @param params
	 * @param taobaoName
	 * @param taobaoId
	 * @param shop
	 * @return
	 */
	protected TaobaoUser createNewUser(final TopParams params, final String taobaoId, final TaobaoShop shop) {
		final String taobaoName = params.isSubuser() ? params.getSub_taobao_user_nick() : params.getVisitor_nick();

		final User user = new User();
		user.setLoginName(taobaoName);
		user.setRealName(taobaoName);
		user.setPassword("");
		user.setDisabled(false);
		user.setUserType(UserType.TAOBAO.getName());
		final Set<Role> roles = new HashSet<Role>();
		final Role basicRole = roleRepository.findOne(params.isSubuser() ? USER_ROLE_ID : ADMIN_ROLE_ID);
		roles.add(basicRole);
		user.setRoles(roles);

		final TaobaoUser taobaoUser = new TaobaoUser();
		taobaoUser.setUser(user);
		taobaoUser.setIsSubuser(params.isSubuser());
		taobaoUser.setPlatUserId(taobaoId);
		taobaoUser.setPlatUserName(taobaoName);
		taobaoUser.setPlatShop(shop);

		taobaoUserRepository.save(taobaoUser);
		logger.info(taobaoName + "用户保存成功");
		return taobaoUser;
	}

	public TaobaoUserRepository getTaobaoUserRepository() {
		return taobaoUserRepository;
	}

	public void setTaobaoUserRepository(final TaobaoUserRepository taobaoUserRepository) {
		this.taobaoUserRepository = taobaoUserRepository;
	}

	public TaobaoShopRepository getTaobaoShopRepository() {
		return taobaoShopRepository;
	}

	public void setTaobaoShopRepository(final TaobaoShopRepository taobaoShopRepository) {
		this.taobaoShopRepository = taobaoShopRepository;
	}

	public RoleRepository getRoleRepository() {
		return roleRepository;
	}

	public void setRoleRepository(final RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

}
