package com.yunat.ccms.module.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalCause;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.yunat.ccms.module.ModuleService;
import com.yunat.ccms.module.ModuleService.CacheReloadListener;

@Service
public class ModuleEntryServiceImpl implements ModuleEntryService, InitializingBean {

	private static class CachedObject {
		Multimap<Long, ModuleEntry> moduleIdEntryMap;

		public CachedObject(final Multimap<Long, ModuleEntry> moduleIdEntryMap) {
			super();
			this.moduleIdEntryMap = moduleIdEntryMap;
		}
	}

	private final LoadingCache<Object, CachedObject> cache = CacheBuilder.newBuilder()//
			.expireAfterWrite(30, TimeUnit.MINUTES)//
			.build(new CacheLoader<Object, CachedObject>() {
				@Override
				public CachedObject load(final Object key) throws Exception {
					return buildCache();
				}
			});

	@Autowired
	private ModuleService moduleService;
	@Autowired
	private ModuleEntryRepository moduleEntryRepository;

	private CachedObject buildCache() {
		final Collection<ModuleEntry> moduleEntries = getModuleEntriesFromDB();
		final Multimap<Long, ModuleEntry> moduleIdEntryMap = ArrayListMultimap.create();
		for (final ModuleEntry e : moduleEntries) {
			final Long moduleId = e.getModuleId();
			if (moduleId != null) {
				e.setModule(moduleService.getModuleById(moduleId));
			}
			moduleIdEntryMap.put(moduleId, e);
		}
		return new CachedObject(moduleIdEntryMap);
	}

	protected Collection<ModuleEntry> getModuleEntriesFromDB() {
		return moduleEntryRepository.findAll();
	}

	@Override
	public Collection<ModuleEntry> getEntriesOfModule(final Long moduleId) {
		final Multimap<Long, ModuleEntry> moduleIdEntryMap = getCachedObject().moduleIdEntryMap;
		final Collection<ModuleEntry> rt = new ArrayList<ModuleEntry>();
		rt.addAll(moduleIdEntryMap.get(moduleId));
		//以null为key的规则,即不限定moduleId的规则
		rt.addAll(moduleIdEntryMap.get(null));
		return rt;
	}

	private CachedObject getCachedObject() {
		try {
			return cache.get(Boolean.TRUE);
		} catch (final ExecutionException e) {
			throw new RuntimeException("获取模块准入规则出错", e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		moduleService.regCacheRemovalListener(new CacheReloadListener() {
			@Override
			public void onCacheReload(final RemovalCause cause) {
				//暂时定为直接丢弃缓存
				cache.invalidateAll();
			}
		});
	}

	protected ModuleService getModuleService() {
		return moduleService;
	}

	protected void setModuleService(final ModuleService moduleService) {
		this.moduleService = moduleService;
	}
}
