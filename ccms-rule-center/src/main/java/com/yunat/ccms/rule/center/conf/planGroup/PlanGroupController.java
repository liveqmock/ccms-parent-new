package com.yunat.ccms.rule.center.conf.planGroup;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.auth.login.taobao.TaobaoShopService;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.rule.center.RuleCenterRuntimeException;

@Controller
@RequestMapping("/rulecenter/planGroup/*")
public class PlanGroupController {

	@Autowired
	protected PlanGroupService planGroupService;
	@Autowired
	protected TaobaoShopService shopService;

	/**
	 * 获取某店铺的方案组
	 * 
	 * @param shopId
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object planGroupOfShop(@PathVariable("id") final String shopId) {
		try {
			final PlanGroup planGroup = planGroupService.planGroupOfShop(shopId);
			return ControlerResult.newSuccess(planGroup);
		} catch (final Exception e) {
			return ControlerResult.newError(e.getMessage());
		}
	}

	/**
	 * 获取第一个店铺(默认)的方案组,是 planGroupOfShop(String shopId)方法的便利版本
	 * 
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public Object planGroupOfShop() {
		final String shopId = shopService.list().get(0).getShopId();
		return planGroupOfShop(shopId);
	}

	/**
	 * 保存方案组的"默认备注签名"
	 * 
	 * @param sign
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public Object saveSign(@PathVariable("id") final String planGroupId,//
			@RequestBody final PlanGroup planGroup) {
		try {
			final boolean success = planGroupService.saveSign(planGroupId, planGroup.getSign());
			return success ? ControlerResult.newSuccess() : ControlerResult.newError();
		} catch (final RuleCenterRuntimeException e) {
			return ControlerResult.newError(e.getMessage());
		}
	}

	/**
	 * 预览方案的默认签名.
	 * 
	 * @param shopId
	 * @param sign 此参数已无效.过几天陶勇把它从请求中去掉之后就可以干掉了.现在先兼容着
	 * @return
	 */
	@RequestMapping(value = "/{shopId}/signContent", method = RequestMethod.GET)
	@ResponseBody
	public Object previewSign(@PathVariable("shopId") final String shopId,//
			@Deprecated @RequestParam(value = "sign", defaultValue = "") final String sign) {
		final String content = planGroupService.previewSign(shopId);
		return ControlerResult.newSuccess(Collections.singletonMap("content", content));
	}
}
