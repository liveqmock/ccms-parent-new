/**
 *
 */
package com.yunat.ccms.tradecenter.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.controller.vo.PropertiesRequest;
import com.yunat.ccms.tradecenter.service.PropertiesConfigManager;
import com.yunat.ccms.tradecenter.support.bean.NameValueBean;


/**
 * 属性配置表
 * @author 李卫林
 *
 */
@Controller
@RequestMapping(value = "/properties/*")
public class PropertiesController {

	private static Logger logger = LoggerFactory.getLogger(PropertiesController.class);

	@Autowired
	private PropertiesConfigManager propertiesConfigManager;

	@ResponseBody
	@RequestMapping(value = "/addGroup", method = { RequestMethod.POST, RequestMethod.GET })
	public ControlerResult addGroup(@RequestBody PropertiesRequest propertiesRequest) {
		if (propertiesRequest.getDpId() == null) {
			logger.info("缺少参数：店铺Id");
			return ControlerResult.newError("缺少参数：店铺Id");
		}

		if (propertiesRequest.getName() == null) {
			return ControlerResult.newError("缺少参数：配置名");
		}

		if (propertiesRequest.getValue() == null) {
			return ControlerResult.newError("缺少参数：配置值");
		}

		if (propertiesRequest.getGroupName() == null) {
			return ControlerResult.newError("缺少参数：分组名");
		}

		propertiesConfigManager.saveProperties(propertiesRequest.getDpId(), propertiesRequest.getName(), propertiesRequest.getValue(), propertiesRequest.getGroupName());

		return ControlerResult.newSuccess();
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public ControlerResult add(@RequestBody PropertiesRequest propertiesRequest) {
		if (propertiesRequest.getDpId() == null) {
			return ControlerResult.newError("缺少参数：店铺Id");
		}

		if (propertiesRequest.getName() == null) {
			return ControlerResult.newError("缺少参数：配置名");
		}

		if (propertiesRequest.getValue() == null) {
			return ControlerResult.newError("缺少参数：配置值");
		}

		propertiesConfigManager.saveProperties(propertiesRequest.getDpId(), propertiesRequest.getName(), propertiesRequest.getValue());

		return ControlerResult.newSuccess();
	}

	@ResponseBody
	@RequestMapping(value = "/addArray", method = { RequestMethod.POST, RequestMethod.GET })
	public ControlerResult addArray(@RequestBody PropertiesRequest propertiesRequest) {
		if (propertiesRequest.getDpId() == null) {
			return ControlerResult.newError("缺少参数：店铺Id");
		}

		if (propertiesRequest.getName() == null) {
			return ControlerResult.newError("缺少参数：配置名");
		}

		if (propertiesRequest.getValues() == null) {
			return ControlerResult.newError("缺少参数：配置值");
		}

		propertiesConfigManager.saveProperties(propertiesRequest.getDpId(), propertiesRequest.getName(), propertiesRequest.getValues());

		return ControlerResult.newSuccess();
	}

	@ResponseBody
	@RequestMapping(value = "/addBatch", method = { RequestMethod.POST, RequestMethod.GET })
	public ControlerResult batchAdd(@RequestBody PropertiesRequest propertiesRequest) {
		if (propertiesRequest.getDpId() == null) {
			return ControlerResult.newError("缺少参数：店铺Id");
		}

		if (propertiesRequest.getNames() == null) {
			return ControlerResult.newError("缺少参数：配置名");
		}

		if (propertiesRequest.getValues() == null) {
			return ControlerResult.newError("缺少参数：配置值");
		}

		if (propertiesRequest.getGroupName() == null) {
			return ControlerResult.newError("缺少参数：分组名");
		}

		String[] names = propertiesRequest.getNames();
		String[] values = propertiesRequest.getValues();
		for (int i = 0; i < names.length; i++) {
			propertiesConfigManager.saveProperties(propertiesRequest.getDpId(), names[i], values[i], propertiesRequest.getGroupName());
		}

		return ControlerResult.newSuccess();
	}

	@ResponseBody
	@RequestMapping(value = "/replaceBatch", method = { RequestMethod.POST, RequestMethod.GET })
	public ControlerResult replaceBatch(@RequestBody PropertiesRequest propertiesRequest) {
		if (propertiesRequest.getDpId() == null) {
			return ControlerResult.newError("缺少参数：店铺Id");
		}

		if (propertiesRequest.getNames() == null) {
			return ControlerResult.newError("缺少参数：配置名");
		}

		if (propertiesRequest.getValues() == null) {
			return ControlerResult.newError("缺少参数：配置值");
		}

		if (propertiesRequest.getGroupName() == null) {
			return ControlerResult.newError("缺少参数：分组名");
		}

		String[] names = propertiesRequest.getNames();
		String[] values = propertiesRequest.getValues();
		propertiesConfigManager.batchReplace(propertiesRequest.getDpId(), names, values, propertiesRequest.getGroupName());

		return ControlerResult.newSuccess();
	}

	@ResponseBody
	@RequestMapping(value = "/get", method = { RequestMethod.POST, RequestMethod.GET })
	public ControlerResult get(@RequestBody PropertiesRequest propertiesRequest) {

		Object value = propertiesConfigManager.getString(propertiesRequest.getDpId(), propertiesRequest.getName());

		return ControlerResult.newSuccess(value);
	}


	@ResponseBody
	@RequestMapping(value = "/getArray", method = { RequestMethod.POST, RequestMethod.GET })
	public ControlerResult getIntArrayProperties(@RequestBody PropertiesRequest propertiesRequest) {
		String[] values = propertiesConfigManager.getStringArray(propertiesRequest.getDpId(), propertiesRequest.getName());

		return ControlerResult.newSuccess(values);
	}

	@ResponseBody
	@RequestMapping(value = "/getGroup", method = { RequestMethod.POST, RequestMethod.GET })
	public ControlerResult getGroup(@RequestBody PropertiesRequest propertiesRequest) {

		List<NameValueBean> nameValueList = propertiesConfigManager.findNameValueByGroup(propertiesRequest.getDpId(), propertiesRequest.getGroupName());

		return ControlerResult.newSuccess(nameValueList);
	}

	@ResponseBody
	@RequestMapping(value = "/getGroupMap", method = { RequestMethod.POST, RequestMethod.GET })
	public ControlerResult getGroupMap(@RequestBody PropertiesRequest propertiesRequest) {

		Map<String, String> nameValueMap = propertiesConfigManager.getNameValueMap(propertiesRequest.getDpId(), propertiesRequest.getGroupName());

		return ControlerResult.newSuccess(nameValueMap);
	}

	@ResponseBody
	@RequestMapping(value = "/deletePropertiesByName", method = { RequestMethod.POST, RequestMethod.GET })
	public ControlerResult deletePropertiesByName(@RequestBody PropertiesRequest propertiesRequest) {
		try {
			propertiesConfigManager.deleteByName(propertiesRequest.getName());
		} catch (Exception e) {
			return ControlerResult.newError();
		}


		return ControlerResult.newSuccess();
	}

}
