package com.yunat.ccms.module.auth;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.yunat.ccms.auth.AuthContext;
import com.yunat.ccms.core.support.auth.SupportOp;
import com.yunat.ccms.module.Module;
import com.yunat.ccms.module.ModuleService;
import com.yunat.ccms.module.ModuleType;
import com.yunat.ccms.module.auth.ModuleEntryFieldValidator.ValidateResult;

@Service
public class ModuleAuthServiceImpl implements ModuleAuthService, ApplicationContextAware {

	@Autowired
	private ModuleEntryService moduleEntryService;
	@Autowired
	private ModuleService moduleService;

	private List<ModuleEntryFieldValidator> moduleEntryFieldValidators;

	@Override
	public Module authorizeModule(final AuthContext authContext, final Object rootModuleId) {
		// 获得本次请求的根模块
		final Module rootModule;
		if (rootModuleId instanceof String) {
			rootModule = getModuleService().getModuleByFullName((String) rootModuleId);
		} else if (rootModuleId instanceof Long) {
			rootModule = getModuleService().getModuleById((Long) rootModuleId);
		} else {
			throw new RuntimeException("没有此模块:" + rootModuleId);
		}
		if (rootModule == null) {
			throw new RuntimeException("没有此模块:" + rootModuleId);
		}

		final Map<Module, ModuleAndAuthority> moduleAuthorityMap = new LinkedHashMap<Module, ModuleAndAuthority>();
		// 以所请求的模块为根模块
		authModuleTree(authContext, rootModule, moduleAuthorityMap);
		return moduleAuthorityMap.get(rootModule);
	}

	/**
	 * @param authContext
	 * @param rootModule
	 * @param moduleAuthorityMap
	 */
	protected void authModuleTree(final AuthContext authContext, final Module rootModule,
			final Map<Module, ModuleAndAuthority> moduleAuthorityMap) {
		final int productEdition = authContext.getProductEdition().id;
		final Queue<Module> queue = new LinkedList<Module>();
		queue.add(rootModule);
		while (!queue.isEmpty()) {// 广度遍历模块树
			final Module module = queue.poll();
			// 检查版本要求.
			final int moduleLowestEditionRequired = module.getLowestEditionRequired();
			if (moduleLowestEditionRequired > productEdition) {
				continue;
			}
			// 容器模块(父模块)的权限
			final ModuleAndAuthority containerModuleAndAuthority = moduleAuthorityMap.get(module.getContainerModule());

			final ModuleAndAuthority moduleAndAuthority = validateModule(authContext, module,
					containerModuleAndAuthority);
			if (moduleAndAuthority == null) {// 无权,直接舍弃,不再检查子模块
				continue;
			}

			moduleAuthorityMap.put(module, moduleAndAuthority);
			final ModuleAndAuthority parentModuleAndAuthority = moduleAuthorityMap.get(module.getContainerModule());
			if (parentModuleAndAuthority != null) {
				parentModuleAndAuthority.addChild(moduleAndAuthority);
			}
			// 将包含的模块(子模块)放到队列继续检查
			queue.addAll(module.getContainingModules());
		}
	}

	protected ModuleAndAuthority validateModule(final AuthContext authContext, final Module module,
			final ModuleAndAuthority containerModuleAndAuthority) {
		final Long moduleId = module.getId();
		// 获取该模块的所有准入规则.
		final Collection<ModuleEntry> moduleEntries = getModuleEntryService().getEntriesOfModule(moduleId);

		if (moduleEntries == null || moduleEntries.isEmpty()) {
			// 无指定,继承containerModule的
			return inherit(containerModuleAndAuthority, module);
		}

		final ModuleEntry mostMatch = findMostMatch(authContext, module, moduleEntries);
		if (mostMatch == null) {
			// 无指定,继承containerModule的
			return inherit(containerModuleAndAuthority, module);
		}

		final Collection<SupportOp> supportOps = mostMatch.getSupportOps();
		if (supportOps == null || supportOps.isEmpty()) {// ModuleEntry规定此Module对此User为无权
			return null;
		}
		return new ModuleAndAuthority(module, supportOps);
	}

	protected ModuleAndAuthority inherit(final ModuleAndAuthority containerModuleAndAuthority, final Module module) {
		return containerModuleAndAuthority == null ? null//
				: new ModuleAndAuthority(module, containerModuleAndAuthority.getSupportOps());
	}

	protected ValidateResult[] validatorsValidateEntry(final AuthContext authContext, final Module module,
			final ModuleEntry moduleEntry) {
		final int validatorCount = moduleEntryFieldValidators.size();
		int index = 0;
		final ValidateResult[] curResults = new ValidateResult[validatorCount];
		for (final ModuleEntryFieldValidator validator : moduleEntryFieldValidators) {
			final ValidateResult validateResult = validator.accpept(authContext, module, moduleEntry);
			if (validateResult == ValidateResult.NOT_CONTAINING) {
				// 不符合条件,直接跳过本条规则.
				return null;
			}
			curResults[index] = validateResult;
			++index;
		}
		return curResults;
	}

	protected ModuleEntry findMostMatch(final AuthContext authContext, final Module module,
			final Collection<ModuleEntry> moduleEntries) {
		final List<ModuleEntryFieldValidator> moduleEntryFieldValidators = getModuleEntryFieldValidators();
		final int validatorCount = moduleEntryFieldValidators.size();
		ValidateResult[] validateResults = null;
		final List<ModuleEntry> mostMatches = new LinkedList<ModuleEntry>();
		for (final ModuleEntry moduleEntry : moduleEntries) {
			final ValidateResult[] curResults = validatorsValidateEntry(authContext, module, moduleEntry);
			if (curResults == null) {// 不匹配
				continue;
			}
			// ModuleEntry中其他的限定条件还没用上.

			// 比较当前规则和之前最匹配规则哪个更好
			if (validateResults == null) {// 没有"当前最匹配",则将现在的作为第一个"当前最匹配"
				validateResults = curResults;
				mostMatches.add(moduleEntry);
			} else {
				for (int i = 0; i < validatorCount; ++i) {
					final ValidateResult curResult = curResults[i];
					ValidateResult validateResult = validateResults[i];
					if (curResult == validateResult) {
						if (i == validatorCount - 1) {// 比较到最后一位还相等,则是并列最匹配
							mostMatches.add(moduleEntry);
						}
						continue;
					}
					if (curResult.value < validateResult.value) {
						// 当前的匹配所使用的规则 比 之前最匹配所使用的规则细致,则取代之前的最匹配
						mostMatches.clear();
						mostMatches.add(moduleEntry);
						validateResult = curResult;
					} else {// curResult.value>validateResult.value
							// 当前的匹配所使用的规则 不如 之前最匹配所使用的规则细致,则还保留之前最匹配
					}
					break;
				}
			}
		}

		if (mostMatches.isEmpty()) {
			return null;
		}
		if (mostMatches.size() == 1) {
			return mostMatches.get(0);
		}
		// 多个符合mostMatch的
		return moreThanOneMostMatch(authContext, module, mostMatches);
	}

	protected ModuleEntry moreThanOneMostMatch(final AuthContext authContext, final Module module,
			final List<ModuleEntry> mostMatches) {
		final Iterator<ModuleEntry> iterator = mostMatches.iterator();
		final ModuleEntry first = iterator.next();
		final int authMask = first.getSupportOpsMask();
		while (iterator.hasNext()) {
			final ModuleEntry notFirst = iterator.next();
			if (authMask != notFirst.getSupportOpsMask()) {
				throw new RuntimeException("模块" + module.getId() + "对" + authContext.getUser().getLoginName()//
						+ "的授权" + first.getId() + "和" + notFirst.getId() + "有冲突。");
			}
		}
		return first;
	}

	public List<ModuleEntryFieldValidator> getModuleEntryFieldValidators() {
		return moduleEntryFieldValidators;
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		if (moduleEntryFieldValidators == null) {
			moduleEntryFieldValidators = defaultModuleEntryFieldValidators();
		}
	}

	@Override
	public SupportOp[] getSupportOps(final AuthContext authContext, final Module module) {
		final ModuleAndAuthority moduleAndAuthority = validateModule(authContext, module, null);
		// 若无精确匹配的,就用container的
		if (moduleAndAuthority == null) {
			return getSupportOps(authContext, module.getContainerModule());
		}
		final Collection<SupportOp> supportOps = moduleAndAuthority.getSupportOps();
		return supportOps.toArray(new SupportOp[supportOps.size()]);
	}

	protected List<ModuleEntryFieldValidator> defaultModuleEntryFieldValidators() {
		// 顺序很重要
		return Arrays.asList(//
				new ModuleIdValidator(),//
				new UserIdValidator(),//
				new RoleIdValidator(),//
				new PermissionIdValidator());
	}

	protected ModuleEntryService getModuleEntryService() {
		return moduleEntryService;
	}

	protected ModuleService getModuleService() {
		return moduleService;
	}

	protected void setModuleEntryService(final ModuleEntryService moduleEntryService) {
		this.moduleEntryService = moduleEntryService;
	}

	protected void setModuleService(final ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	/**
	 * 顺序敏感.排在前面的有优先决定权.
	 * 
	 * @param moduleEntryFieldValidators
	 */
	public void setModuleEntryFieldValidators(final List<ModuleEntryFieldValidator> moduleEntryFieldValidators) {
		this.moduleEntryFieldValidators = moduleEntryFieldValidators;
	}

	/**
	 * Module接口的一个包装型实现.
	 * 代理一个真正的Module,但有关权限不是Module原本的可用权限,而是根据上下文验证出来的用户对这个Module拥有的权限.
	 * 
	 * @author wenjian.liang
	 */
	private static class ModuleAndAuthority implements Module {
		private final Module module;
		private final Collection<SupportOp> supportOps;
		private final Collection<Module> children = new LinkedList<Module>();

		public ModuleAndAuthority(final Module module, final Collection<SupportOp> supportOps) {
			super();
			this.module = module;
			this.supportOps = supportOps;
		}

		private void addChild(final Module child) {
			children.add(child);
		}

		@Override
		public Collection<SupportOp> getSupportOps() {
			return supportOps;
		}

		@Override
		public Long getId() {
			return module.getId();
		}

		@Override
		public Long getModuleTypeId() {
			return module.getModuleTypeId();
		}

		@Override
		public Long getContainerModuleId() {
			return module.getContainerModuleId();
		}

		@Override
		public ModuleType getModuleType() {
			return module.getModuleType();
		}

		@Override
		public Module getContainerModule() {
			return module.getContainerModule();
		}

		@Override
		public String getName() {
			return module.getName();
		}

		@Override
		public String getTip() {
			return module.getTip();
		}

		@Override
		public String getMemo() {
			return module.getMemo();
		}

		@Override
		public Collection<Module> getContainingModules() {
			return children;
		}

		@Override
		public Integer getSupportOpsMask() {
			return module.getSupportOpsMask();
		}

		@Override
		public int getLowestEditionRequired() {
			return module.getLowestEditionRequired();
		}

		@Override
		public String toString() {
			return "ModuleAndAuthority [module=" + module + ", moduleSupportOps=" + supportOps + "]";
		}

		@Override
		public int getLayer() {
			return module.getLayer();
		}

		@Override
		public String getUrl() {
			return module.getUrl();
		}

		@Override
		public String getDataUrl() {
			return module.getDataUrl();
		}

		@Override
		public String getKeyName() {
			return module.getKeyName();
		}
	}
}
