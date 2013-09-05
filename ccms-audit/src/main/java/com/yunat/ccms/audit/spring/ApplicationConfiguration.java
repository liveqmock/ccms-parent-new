package com.yunat.ccms.audit.spring;

import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.inspektr.audit.AuditTrailManagementAspect;
import com.github.inspektr.audit.AuditTrailManager;
import com.github.inspektr.audit.spi.AuditActionResolver;
import com.github.inspektr.audit.spi.AuditResourceResolver;
import com.github.inspektr.audit.spi.support.DefaultAuditActionResolver;
import com.github.inspektr.audit.spi.support.SpringSecurityAuditablePrincipalResolver;
import com.github.inspektr.audit.support.JdbcAuditTrailManager;
import com.github.inspektr.audit.support.Slf4jLoggingAuditTrailManager;
import com.github.inspektr.common.spi.ClientInfoResolver;
import com.github.inspektr.common.spi.PrincipalResolver;
import com.github.inspektr.common.web.ClientInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunat.ccms.audit.support.DefaultAuditResourceResolver;

/**
 * 
 * @author hanzheng
 * @see <a
 *      href="https://github.com/dima767/inspektr/wiki/Inspektr-Auditing">Inspektr-Auditing</a>
 */
@Configuration
public class ApplicationConfiguration {

	/**
	 * @return
	 */
	@Bean
	public PrincipalResolver principalResolver() {
		return new SpringSecurityAuditablePrincipalResolver();
	}

	/**
	 * Resolves the system resource being targeted by an audit action i.e. the
	 * WHAT at audit points
	 * 
	 * @return
	 */
	@Bean
	public Map<String, AuditResourceResolver> auditResourceResolver() {

		Map<String, AuditResourceResolver> auditResourceResolvers = Maps.newConcurrentMap();

		auditResourceResolvers.put("DEFAULT", new DefaultAuditResourceResolver());

		return auditResourceResolvers;
	}

	/**
	 * Resolves audited actions i.e. ACTION at audit points
	 * 
	 * @return
	 */
	@Bean
	public Map<String, AuditActionResolver> auditActionResolver() {

		Map<String, AuditActionResolver> auditActionResolvers = Maps.newConcurrentMap();

		auditActionResolvers.put("DEFAULT", new DefaultAuditActionResolver());

		return auditActionResolvers;
	}

	@Bean
	public ClientInfoResolver ClientInfoResolver() {

		return new ClientInfoResolver() {

			@Override
			public ClientInfo resolveFrom(JoinPoint joinPoint, Object retVal) {
				// CCMS的请求都是转发的，所以这块基本没用
				return ClientInfo.EMPTY_CLIENT_INFO;
			}
		};
	}

	/**
	 * @return
	 */
	@Bean
	public List<AuditTrailManager> auditTrailManagers() {
		List<AuditTrailManager> auditTrailManagers = Lists.newArrayListWithExpectedSize(5);

		auditTrailManagers.add(new Slf4jLoggingAuditTrailManager());
		// auditTrailManagers.add(new JdbcAuditTrailManager(null));

		return auditTrailManagers;
	}

	/**
	 * @return
	 */
	@Bean
	public AuditTrailManagementAspect auditTrailManagementAspect() {

		PrincipalResolver auditablePrincipalResolver = principalResolver();
		List<AuditTrailManager> auditTrailManagers = auditTrailManagers();
		Map<String, AuditActionResolver> auditActionResolverMap = auditActionResolver();
		Map<String, AuditResourceResolver> auditResourceResolverMap = auditResourceResolver();

		return new AuditTrailManagementAspect("CCMS", auditablePrincipalResolver, auditTrailManagers,
				auditActionResolverMap, auditResourceResolverMap);

	}
}
