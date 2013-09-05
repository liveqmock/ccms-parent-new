package com.yunat.ccms.schedule.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.yunat.ccms.schedule.service.RecoverService;

/**
 * 监听Root ApplicationContext的完成事件，然后进行业务处理
 * 
 * @author xiaojing.qu
 * 
 */
@Component
@Scope("singleton")
public class ScheduleContextListener implements ApplicationListener<ApplicationEvent> {

	@Autowired
	private RecoverService recoverService;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			boolean isRoot = ((ContextRefreshedEvent) event).getApplicationContext().getParent() == null;
			if (isRoot) {
				recoverService.recoverScheduelContext();
			}
		}

	}

}
