package com.yunat.ccms.auth.springsecurity.acl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.yunat.ccms.core.support.auth.AclManaged;

@Service
public class AclTypeCacheImpl implements AclTypeCache {

	private static final AclOidType EMPTY_VALUE = new AclOidType();

	@Autowired
	private AclTypeRepository aclTypeRepository;

	protected final LoadingCache<String, AclOidType> cache = CacheBuilder.newBuilder()//
			.expireAfterWrite(12, TimeUnit.HOURS)//
			.build(new CacheLoader<String, AclOidType>() {
				@Override
				public AclOidType load(final String key) throws Exception {
					final AclOidType aclOidType = aclTypeRepository.findByTypeName(key);
					return aclOidType == null ? EMPTY_VALUE : aclOidType;
				}
			});

	@Override
	public boolean hasAcl(final String typeName) {
		try {
			final AclOidType aclOidType = cache.get(typeName);
			return aclOidType != null && aclOidType != EMPTY_VALUE;
		} catch (final Exception e) {
			//都是"查不到数据","查数据时出了问题"的exception,就当没有了
			return false;
		}
	}

	@Override
	public boolean hasAcl(final Class<?> clazz) {
		final AclManaged aclManaged = AnnotationUtils.findAnnotation(clazz, AclManaged.class);
		if (aclManaged == null) {
			return hasAcl(clazz.getName());
		} else {
			return hasAcl(aclManaged.value());
		}
	}

}
