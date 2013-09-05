package com.yunat.ccms.configuration.variable;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.cons.AppCons;

@Component
class SystemVariableHolder implements SystemVariable, ApplicationContextAware, ServletContextAware,
		InitializingBean {

	private static Logger logger = LoggerFactory.getLogger(SystemVariableHolder.class);

	private ServletContext servletContext;
	private ApplicationContext applicationContext;
	private String webRootFullPath;

	@Autowired
	private AppPropertiesQuery appPropertiesQuery;

	private void setVersion() {
		String version = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_VERSION);
		String announcementURL = appPropertiesQuery
				.retrieveConfigValue(CCMSPropertiesEnum.CHANNEL_REPORT_ANNOUNCEMENT_URL);
		logger.info("put version into ServletContext : {}", version);
		servletContext.setAttribute(AppCons.VERSION_VAR, version);
		logger.info("put announcementURL into ServletContext : {}", announcementURL);
		servletContext.setAttribute(AppCons.CHANNEL_ANNOUNCEMENT_URL, announcementURL);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (servletContext != null) {
			setVersion();
		}
	}

	@Override
	public void setServletContext(ServletContext servletContext_) {
		this.servletContext = servletContext_;
		webRootFullPath = this.servletContext.getRealPath("/");
		logger.info("WEB ROOT Real Path : {}", webRootFullPath);
		if (appPropertiesQuery != null) {
			setVersion();
		}
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext_) throws BeansException {
		applicationContext = applicationContext_;
	}

	@Override
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public String getWebRootFullPath() {
		return this.webRootFullPath;
	}

}