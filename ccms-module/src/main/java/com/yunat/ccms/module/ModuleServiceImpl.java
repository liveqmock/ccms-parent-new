package com.yunat.ccms.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Maps;

@Service
public class ModuleServiceImpl implements ModuleService {

	protected static class CachedObject {
		final Map<Long, ? extends Module> moduleIdMap;
		final Map<String, ? extends Module> moduleFullNameMap;
		final Map<Long, ? extends ModuleType> moduleTypeMap;

		public CachedObject(final Map<Long, ? extends Module> moduleIdMap,//
				final Map<String, ? extends Module> moduleFullNameMap,//
				final Map<Long, ? extends ModuleType> moduleTypeMap) {
			super();
			this.moduleIdMap = moduleIdMap;
			this.moduleFullNameMap = moduleFullNameMap;
			this.moduleTypeMap = moduleTypeMap;
		}

		@Override
		public String toString() {
			return "CachedObject [moduleIdMap=" + moduleIdMap + ", moduleFullNameMap=" + moduleFullNameMap
					+ ", moduleTypeMap=" + moduleTypeMap + "]";
		}
	}

	protected final Collection<CacheReloadListener> removalListenerChain = new ArrayList<CacheReloadListener>();

	protected final LoadingCache<Object, CachedObject> cache = CacheBuilder.newBuilder()//
			.expireAfterWrite(30, TimeUnit.MINUTES)//
			.removalListener(new RemovalListener<Object, CachedObject>() {

				@Override
				public void onRemoval(final RemovalNotification<Object, CachedObject> notification) {
					for (final CacheReloadListener l : removalListenerChain) {
						l.onCacheReload(notification.getCause());
					}
				}
			})//
			.build(new CacheLoader<Object, CachedObject>() {
				@Override
				public CachedObject load(final Object key) throws Exception {
					return ModuleServiceImpl.this.buildCache();
				}
			});

	@Autowired
	private ModuleRepository moduleRepository;
	@Autowired
	private ModuleTypeRepository moduleTypeRepository;

	@Override
	public void regCacheRemovalListener(final CacheReloadListener listener) {
		removalListenerChain.add(listener);
	}

	protected Collection<ModuleFromDB> getModulesFromDB() {
		return moduleRepository.findAll();
	}

	protected Map<Long, ModuleType> getModuleTypeFromDB() {
		final Collection<ModuleType> moduleTypes = moduleTypeRepository.findAll();
		final Map<Long, ModuleType> map = Maps.newHashMap();
		for (final ModuleType moduleType : moduleTypes) {
			map.put(moduleType.getId(), moduleType);
		}
		return map;
	}

	protected CachedObject buildCache() {
		final Collection<ModuleFromDB> modules = getModulesFromDB();
		final Map<Long, Module> moduleIdMap = Maps.newHashMap();
		final Map<String, Module> moduleKeyMap = Maps.newHashMap();
		final Map<Long, ModuleType> moduleTypeMap = getModuleTypeFromDB();

		//放到id-map里.注:要先构建好整个moduleIdMap,因为后面要用.
		for (final ModuleFromDB module : modules) {
			moduleIdMap.put(module.getId(), module);
		}

		for (final ModuleFromDB module : modules) {
			//模块类型
			final Long moduleTypeId = module.getModuleTypeId();
			final ModuleType moduleType = moduleTypeMap.get(moduleTypeId);
			if (moduleType == null) {
				//不会到达
				throw new RuntimeException("模块" + module.getId() + "不属于任何一种模块类型");
			}
			module.setModuleType(moduleType);
			//模块包含关系
			final Long containerModuleId = module.getContainerModuleId();
			if (containerModuleId != null) {
				final Module container = moduleIdMap.get(containerModuleId);
				if (container == null) {
					//不会到达
					throw new RuntimeException("模块关系有误:" + module.getId() + "<" + containerModuleId);
				}
				module.setContainerModule(container);
				container.getContainingModules().add(module);
			}
			//全路径-模块
			moduleKeyMap.put(ModuleUtil.keyPath(module), module);
		}
		return new CachedObject(moduleIdMap, moduleKeyMap, moduleTypeMap);
	}

	protected CachedObject getCachedObject() {
		try {
			return cache.get(Boolean.TRUE);
		} catch (final ExecutionException e) {
			throw new RuntimeException("获取模块出错", e);
		}
	}

	@Override
	public ModuleType getModuleTypeById(final Long id) {
		return getCachedObject().moduleTypeMap.get(id);
	}

	@Override
	public Module getModuleById(final Long id) {
		return getCachedObject().moduleIdMap.get(id);
	}

	@Override
	public Module getModuleByFullName(final String name) {
		return getCachedObject().moduleFullNameMap.get(name);
	}

	@Override
	public Module save(final ModuleFromDB module) {
		return moduleRepository.saveAndFlush(module);
	}
}
