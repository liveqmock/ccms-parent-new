package com.yunat.ccms.dashboard.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.dashboard.channel.helper.ChannelGuide;
import com.yunat.ccms.dashboard.channel.helper.ChannelGuideFactory;

/**
 * ©Copyright：yunat Project：ccms-dashboard Module ID：首页模块 Comments：渠道URI JDK
 * version used：<JDK1.6>
 * 
 * Author：yinwei Create Date： 2013-1-19 Version:1.0
 * 
 * Modified By： Modified Date： Why & What is modified： Version：
 */

@Controller
@RequestMapping(value = "/dashboard/channel/*")
public class DashBoardChannelController {
	private Logger logger = LoggerFactory.getLogger(DashBoardChannelController.class);

	@Autowired
	private ChannelGuideFactory channelGuideFactory;

	// 通道余额
	@RequestMapping(value = "/passageway/account", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<Map<String, Object>> passagewayAccount(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		// return channelBalanceCommon();
		List<Map<String, Object>> test = new ArrayList<Map<String, Object>>();
		try {
			//test.get(1);
		} catch (Exception e) {
			//throw new Exception("自定义异常信息！");
		}

		return test;
	}

	// TODO
	List<Map<String, Object>> channelBalanceCommon() {
		// TODO 取渠道的通道信息
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		logger.info("******************通道余额*******************");
		return resultList;
	}

	/**
	 * 通道发送情况
	 * 
	 * @param channelType
	 * @param mode
	 * @return
	 */
	@RequestMapping(value = "/passageway/send", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<Map<String, Object>> passagewaySend(@RequestParam String channelType) {
		ChannelGuide channelGuidePage = getChannelGuideAbstract(channelType);
		return channelGuidePage.guideHandler();
	}

	private ChannelGuide getChannelGuideAbstract(String channelType) {
		return getPerformers().get(channelType);
	}

	/**
	 * @return the all concreteObject in the factory set map
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private Map<String, ChannelGuide> getPerformers() {
		Map<String, ChannelGuide> performers = new HashMap<String, ChannelGuide>();
		Method[] methods = channelGuideFactory.getClass().getDeclaredMethods();
		for (Method m : methods) {
			ChannelGuide channelGuide = null;
			try {
				channelGuide = (ChannelGuide) m.invoke(channelGuideFactory);
			} catch (IllegalArgumentException e) {
				logger.info("首页渠道发送页面invoke非法参数异常！");
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				logger.info("首页渠道发送页面invoke非法访问异常！");
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				logger.info("首页渠道发送页面调用异常！");
				e.printStackTrace();
			}
			performers.put(channelGuide.getChannelType(), channelGuide);
		}
		return performers;
	}

}
