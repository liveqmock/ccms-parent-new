package com.yunat.ccms.auth.login.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.auth.login.LoginInfo;
import com.yunat.ccms.auth.login.LoginInfoHolder;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.repository.AppPropertiesRepository;
import com.yunat.ccms.core.support.auth.AuthCons;
import com.yunat.ccms.core.support.cons.ProductEdition;
import com.yunat.ccms.core.support.ucenter.ToUCenter;
import com.yunat.ccms.core.support.utils.EnumUtil;

public class SetThreadLocalFilter implements Filter {

	private static final String SESSION_ATTR_NAME = "p";

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AppPropertiesRepository appPropertiesRepository;
	@Autowired
	private ToUCenter toUCenter;

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpSession session = request.getSession();

		putProductEdition(request, session);

		putLoginInfo(request, session);

		chain.doFilter(request, response);
	}

	protected void putLoginInfo(final HttpServletRequest request, final HttpSession session) {
		final LoginInfo loginInfo = (LoginInfo) session.getAttribute(AuthCons.LOGIN_INFO_SESSION_ATTR_NAME);
		if (loginInfo != null) {
			_LoginInfoHolder._setLoginInfo(loginInfo);
		}
	}

	/**
	 * @param request
	 * @param session
	 */
	protected ProductEdition putProductEdition(final HttpServletRequest request, final HttpSession session) {
		Integer productEditionId = (Integer) session.getAttribute(SESSION_ATTR_NAME);
		final ProductEdition productEdition;
		if (productEditionId == null) {
			productEdition = getProductEdition(request);
			productEditionId = productEdition.id;
			session.setAttribute(SESSION_ATTR_NAME, productEditionId);
		} else {
			productEdition = EnumUtil.get(ProductEdition.class, productEditionId);
		}
		_LoginInfoHolder._setProductEdition(productEdition);
		return productEdition;
	}

	private static class _LoginInfoHolder extends LoginInfoHolder {
		static void _setProductEdition(final ProductEdition productEdition) {
			PRODUCT_EDITION_LOCAL.set(productEdition);
		}

		static void _setLoginInfo(final LoginInfo loginInfo) {
			setLoginInfo(loginInfo);
		}
	}

	/**
	 * @param request
	 * @return
	 */
	protected ProductEdition getProductEdition(final HttpServletRequest request) {
		final String custumerName = customerNameFromRequest(request);
		logger.debug("tenantId:" + custumerName);
		if (custumerName == null) {
			throw new RuntimeException("无法获取当前用户购买的CCMS版本.");
		}
		final ProductEdition productEdition = toUCenter.getProductEdition(custumerName);
		logger.debug(custumerName + " 's productEdition:" + productEdition);
		if (productEdition == null) {
			throw new RuntimeException("无法获取当前用户购买的CCMS版本");
		}
		return productEdition;
	}

	protected String customerNameFromRequest(final HttpServletRequest request) {
		final String customerName = appPropertiesRepository.retrieveConfiguration(
				CCMSPropertiesEnum.CCMS_TENANT_ID.getProp_group(), CCMSPropertiesEnum.CCMS_TENANT_ID.getProp_name());
		return customerName;
	}

	@Override
	public void init(final FilterConfig arg0) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
