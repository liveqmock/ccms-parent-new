package com.yunat.ccms.dashboard.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.BeforeClass;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;
/**
 * springMvc controller层单元测试基类  
 * @author yinwei
 * @version 1.0
 */
public class JUnitControlBase {
	private static HandlerMapping handlerMapping;  
    private static HandlerAdapter handlerAdapter;  
	
    /** 
     * 读取spring3 MVC配置文件 
     */  
    @BeforeClass  
    public static void setUp() {  
        if (handlerMapping == null) {  
            
        	String[] configs = { 
			        			"file:src/main/webapp/WEB-INF/spring-servlet.xml",
			        			"classpath:config/database/jdbc.properties",
			        			"classpath:config/database/persistence.xml",
			        			"classpath:config/spring/jdbcTemplateContext.xml",
			        			"classpath:config/spring/applicationContext.xml"
            		     		}; 
            
            XmlWebApplicationContext context = new XmlWebApplicationContext();  
            context.setConfigLocations(configs);  
            MockServletContext msc = new MockServletContext();  
            context.setServletContext(msc);         
            context.refresh();  
            msc.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context); 
            handlerMapping = (HandlerMapping) context.getBean(DefaultAnnotationHandlerMapping.class);  
            handlerAdapter = (HandlerAdapter) context.getBean(context.getBeanNamesForType(AnnotationMethodHandlerAdapter.class)[0]);     
        }  
    }  
  
    /** 
     * 执行request对象请求的action 
     *  
     * @param request 
     * @param response 
     * @return 
     * @throws Exception 
     */  
    public ModelAndView excuteController(HttpServletRequest request, HttpServletResponse response)  throws Exception {  
        HandlerExecutionChain chain = handlerMapping.getHandler(request);  
		
        final ModelAndView model = handlerAdapter.handle(request, response,  
                chain.getHandler());  
        
        return model;  
    }  
}

