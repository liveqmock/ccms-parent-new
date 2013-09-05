package com.yunat.ccms.rule.center.engine.impl;

import org.drools.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunat.ccms.rule.center.RuleCenterBusinessException;
import com.yunat.ccms.rule.center.engine.Fact;
import com.yunat.ccms.rule.center.engine.FactResult;
import com.yunat.ccms.rule.center.engine.RuleEngineSession;

public class DefaultRuleEngineSession implements RuleEngineSession {

	private static Logger logger = LoggerFactory.getLogger(DefaultRuleEngineSession.class);

	private final StatelessKnowledgeSession session;

	public DefaultRuleEngineSession(StatelessKnowledgeSession session) {
		this.session = session;
	}

	@Override
	public FactResult execute(Fact fact) throws RuleCenterBusinessException {
		try {
			session.execute(fact);
			return fact.getResult();
		} catch (Exception e) {
			logger.error("调用drools发生异常", e);
			throw new RuleCenterBusinessException("调用drools发生异常：" + e.getMessage(), e);
		}
	}

	/*
	 * private void execute(Collection<Fact> facts) throws
	 * RuleCenterBusinessException {
	 * try {
	 * session.execute(CommandFactory.newInsertElements(facts));
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * }
	 * }
	 */
}
