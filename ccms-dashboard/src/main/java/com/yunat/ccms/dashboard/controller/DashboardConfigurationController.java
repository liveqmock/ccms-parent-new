package com.yunat.ccms.dashboard.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yunat.ccms.dashboard.service.DashboardConfigurationService;


/**
* ©Copyright：yunat
* Project：ccms-dashboard
* Module ID：首页模块
* Comments：模块管理-模拟portlet管理
*           用户与首页模块  many-to-many
*
* Table :   tb_sysuser
*           dashboard_module_config
*           dashboard_module
*
*
* notice ：  init script
*
*           insert into  dashboard_module_config
			select   ts.id,  dm.dashboard_module_id,  CASE WHEN  dm.dashboard_default_module =1  THEN   0   ELSE  1  END
			from tb_sysuser  ts  ,   dashboard_module  dm
*
*
* JDK version used：<JDK1.6>
*
* Author：yinwei
* Create Date： 2013-1-31
* Version:1.0
*
* Modified By：
* Modified Date：
* Why & What is modified：
* Version：
*/


@Controller
@RequestMapping(value="/dashboard/configuration/manage/*")
public class DashboardConfigurationController {

	@Autowired
	DashboardConfigurationService dashboardConfigurationService;


	/**
	 * 增加用户发起维护(用户与首页模块)配置表
	 */

	@RequestMapping(value="/addUser", method=RequestMethod.GET)
	public void  maintainConfigOfUserLaunch(Long userId){
		dashboardConfigurationService.maintainConfigOfUserLaunch(userId);
	}


	/**
	 * 增加首页模块发起维护(用户与首页模块)配置表
	 * @param  moduleId:  添加的模块id
	 * @param  defaultModuleFlag: 添加的模块是否为首页默认的显示模块
	 */


	@RequestMapping(value="/addModule", method=RequestMethod.GET)
	public void maintainConfigOfDashboardModuleLaunch(Long moduleId,Long defaultModuleFlag){
		dashboardConfigurationService.maintainConfigOfDashboardModuleLaunch(moduleId,defaultModuleFlag);
	}


	/**
	 * 减少用户发起维护(用户与首页模块)配置表
	 */
	//TODO



	/**
	 * 减少首页模块发起维护(用户与首页模块)配置表
	 */
	//TODO




    



	/*
	 * 根据登入用户显示首页个性化展现的模块
	 * @param  session :  获取用户ID
	 * @return :  返回 模块pojo信息-》模块id  模块名称  模块访问url(供前台解析)
	 *//*


	@RequestMapping(value="/module/show", method=RequestMethod.GET,produces = "application/json; charset=utf-8")
	@ResponseBody
	public  String showModule(HttpSession  session){
		//Long userId  = (Long) session.getAttribute("userId");
		Long userId  = 4L;
		return jsonpDataFormatWrap(dashboardConfigurationService.showModule(userId));
	}

	*//**
	 * 根据登入用户显示首页个性化隐藏模块
	 * @param  session :  获取用户ID
	 * @return :  返回 模块pojo信息-》模块id  模块名称  模块访问url(供前台解析)
	 *//*

	@RequestMapping(value="/module/disable", method=RequestMethod.GET ,produces = "application/json; charset=utf-8")
	@ResponseBody
	public  List<DashboardModule>  disableModule(HttpSession  session){
		//Long userId  = (Long) session.getAttribute("userId");
		Long userId  = 4L;
		return dashboardConfigurationService.disableModule(userId);
	}*/




	/**
	 * @param session:  获取用户ID
	 * @param moduleId: 首页模块ID
	 * @param switchSign:  模块开关标记
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/switch", method=RequestMethod.GET)
	public void switchModule(@RequestParam Long moduleId , @RequestParam Boolean switchSign, HttpSession session) throws Exception{
		//Long userId  = (Long) session.getAttribute("userId");
		Long userId  = 4L;
		dashboardConfigurationService.switchModule(userId,moduleId,switchSign);
	}

}
