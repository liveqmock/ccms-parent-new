package com.yunat.ccms.module.auth.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.yunat.ccms.auth.AuthContext;
import com.yunat.ccms.auth.AuthContextImpl;
import com.yunat.ccms.auth.login.LoginInfoHolder;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.core.support.auth.SupportOp;
import com.yunat.ccms.core.support.cons.ProductEdition;
import com.yunat.ccms.module.Module;
import com.yunat.ccms.module.ModuleUtil;
import com.yunat.ccms.module.RequestToModuleIdMapping;
import com.yunat.ccms.module.auth.ModuleAuthService;

/**
 * 分析出用户本次请求的是哪个模块,校验相应的权限
 * 
 * @author wenjian.liang
 */
@Controller("moduleController2")
@RequestMapping(value = "/module/*", method = RequestMethod.GET)
public class ModuleController {

	@Autowired
	private ModuleAuthService moduleAuthService;
	@Autowired
	private RequestToModuleIdMapping requestToModuleIdMapping;

	@RequestMapping
	@ResponseBody
	public ModuleJson exe(final HttpServletRequest request) {
		// 获得所请求的根模块
		final Object rootModuleId = requestToModuleIdMapping.analyzeModuleId(request);
		// 获得用户、购买的ccms版本
		final User user = LoginInfoHolder.getCurrentUser();
		final ProductEdition editionVersion = LoginInfoHolder.getLoginInfo().getProductEdition();
		final AuthContext authContext = new AuthContextImpl(user, request, editionVersion);
		// 验证用户对哪些模块有权限
		final Module authorizedModules = moduleAuthService.authorizeModule(authContext, rootModuleId);
		return new ModuleJson(authorizedModules);
	}

	public static class ModuleJson {
		private final Module module;

		public ModuleJson(final Module module) {
			super();
			this.module = module;
		}

		public char[] getSupportOps() {
			final Collection<SupportOp> ops = module.getSupportOps();
			final char[] cs = new char[ops.size()];
			int index = 0;
			for (final SupportOp o : ops) {
				cs[index] = o.getCode();
				index++;
			}
			return cs;
		}

		public Collection<ModuleJson> getChildren() {
			final Collection<Module> children = module.getContainingModules();
			final Collection<ModuleJson> rt = Lists.newArrayListWithExpectedSize(children.size());
			for (final Module m : children) {
				rt.add(new ModuleJson(m));
			}
			return rt;
		}

		public String getKey() {
			return ModuleUtil.keyPath(module);
		}

		public Long getId() {
			return module.getId();
		}

		public String getTip() {
			return module.getTip();
		}

		public String getUrl() {
			return module.getUrl();
		}

		public String getDataUrl() {
			return module.getDataUrl();
		}

		public String getName() {
			return module.getName();
		}
	}
}
