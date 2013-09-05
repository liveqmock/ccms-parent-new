package com.yunat.ccms.module.auth;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yunat.ccms.auth.AuthContextImpl;
import com.yunat.ccms.auth.exceptions.NoAuthorizationException;
import com.yunat.ccms.auth.login.LoginInfo;
import com.yunat.ccms.auth.login.LoginInfoHolder;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.core.support.auth.SupportOp;
import com.yunat.ccms.core.support.cons.ProductEdition;
import com.yunat.ccms.module.Module;
import com.yunat.ccms.module.RequestToModuleIdMapping;

/**
 * 如果请求是来请求模块的,放过;
 * 否则(是请求数据的),校验用户对所请求的数据所属的模块是否有权限
 * 
 * @author MaGiCalL
 */
public class ModuleInterceptor extends HandlerInterceptorAdapter implements Ordered {

	@Autowired
	private RequestToModuleIdMapping requestToModuleIdMapping;
	@Autowired
	private ModuleAuthService moduleAuthService;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception {
		if (requestToModuleIdMapping.isForModule(request)) {
			return true;
		}
		final HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		final Map<String, Module> dataUrlModuleMap = (Map<String, Module>) session.getAttribute("dataUrlModuleMap");
		if (dataUrlModuleMap == null) {
			return true;
		}
		final String url = request.getRequestURL().toString();
		Module module = null;
		for (final Entry<String, Module> e : dataUrlModuleMap.entrySet()) {
			final String dataUrl = e.getKey();
			if (url.contains(dataUrl)) {
				module = e.getValue();
				break;
			}
		}
		final LoginInfo loginInfo = LoginInfoHolder.getLoginInfo();
		final User user = loginInfo.getUser();
		final ProductEdition productEdition = loginInfo.getProductEdition();
		final SupportOp[] moduleSupportOp = moduleAuthService.getSupportOps(new AuthContextImpl(user,
				request, productEdition), module);
		if (moduleSupportOp == null || moduleSupportOp.length == 0) {
			throw new NoAuthorizationException();
		}
		return true;
	}

	@Override
	public int getOrder() {
		return 10000;
	}

}
