package com.yunat.ccms.rule.center.engine.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.rule.center.RuleCenterBusinessException;
import com.yunat.ccms.rule.center.engine.Fact;
import com.yunat.ccms.rule.center.engine.FactResolver;
import com.yunat.ccms.rule.center.engine.FactResult;
import com.yunat.ccms.rule.center.engine.RuleEngineService;
import com.yunat.ccms.rule.center.engine.RuleEngineSession;
import com.yunat.ccms.rule.center.engine.RuleEngineSessionRepository;

@Component
@SuppressWarnings("rawtypes")
public class DefaultRuleEngineService implements RuleEngineService {

	private final static Logger logger = LoggerFactory.getLogger(DefaultRuleEngineService.class);

	@Autowired
	private RuleEngineSessionRepository sessionRepository;

	@Autowired
	private List<FactResolver> allFactResolver;

	private Map<Class, FactResolver> classResolverMap = new HashMap<Class, FactResolver>();

	@Override
	public FactResult execute(String planGroupId, Serializable factId, Class factType)
			throws RuleCenterBusinessException {
		FactResolver resolver = getFactResolver(factType);
		Fact fact = resolver.resolve(factId);
		RuleEngineSession session = sessionRepository.getSession(planGroupId);
		return session.execute(fact);
	}

	private FactResolver getFactResolver(Class factType) throws RuleCenterBusinessException {
		FactResolver resolver = classResolverMap.get(factType);
		if (resolver != null) {
			return resolver;
		}
		for (FactResolver fr : allFactResolver) {
			if (fr.support(factType)) {
				classResolverMap.put(factType, fr);
				return fr;
			}
		}
		throw new RuleCenterBusinessException("无法找到与" + factType.getName() + "匹配的FactResolver！");
	}
}
