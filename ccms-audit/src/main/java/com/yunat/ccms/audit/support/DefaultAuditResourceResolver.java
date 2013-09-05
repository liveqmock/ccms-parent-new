package com.yunat.ccms.audit.support;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.aspectj.lang.JoinPoint;

import com.github.inspektr.audit.spi.AuditResourceResolver;
import com.google.common.collect.Lists;

public class DefaultAuditResourceResolver implements AuditResourceResolver {

	@Override
	public String[] resolveFrom(JoinPoint target, Object returnValue) {

		List<String> resources = Lists.newArrayList();

		resources.add(target.getTarget().toString());

		String[] parameters = parameterAsString(target.getArgs());
		Collections.addAll(resources, parameters);

		resources.add(ToStringBuilder.reflectionToString(returnValue));

		return resources.toArray(new String[resources.size()]);
	}

	@Override
	public String[] resolveFrom(JoinPoint target, Exception exception) {
		List<String> resources = Lists.newArrayList();

		resources.add(target.getTarget().toString());

		String[] parameters = parameterAsString(target.getArgs());
		Collections.addAll(resources, parameters);

		resources.add("__EXCEPTION: [" + exception.getMessage() + "]");

		return resources.toArray(new String[resources.size()]);
	}

	private String[] parameterAsString(Object[] parameters) {

		List<String> stringArgs = Lists.newArrayListWithExpectedSize(parameters.length);

		for (Object obj : parameters) {
			stringArgs.add(ToStringBuilder.reflectionToString(obj));
		}

		return stringArgs.toArray(new String[parameters.length]);

	}

}
