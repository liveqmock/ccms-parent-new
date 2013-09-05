package com.yunat.ccms.rule.center.conf.condition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.rule.center.RuleCenterRuntimeException;

@Controller
public class ProductSearchController {

	@Autowired
	private ItemsInvokerService itemsInvokerService;

	/**
	 * 关键字搜索商品列表
	 *
	 * @param q
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/rulecenter/shop/{shopId}/products", method = RequestMethod.POST)
	@ResponseBody
	public Object searchProducts(@PathVariable final String shopId, @RequestBody final SearchProductParameters param) {
		try {
			final ItemsResponse itemResp = itemsInvokerService.searchProducts(shopId, param.getQ(), param.getPageNo(),
					param.getPageSize());
			return ControlerResult.newSuccess(itemResp);
		} catch (final RuleCenterRuntimeException e) {
			return ControlerResult.newError(e.getMessage());
		}
	}

	/**
	 * 显示所提供的商品Id的商品信息
	 *
	 * @param numiids
	 * @return
	 */
	@RequestMapping(value = "/rulecenter/shop/{shopId}/products/{numiids}", method = RequestMethod.GET)
	@ResponseBody
	public Object showProducts(@PathVariable final String shopId, @PathVariable final String numiids) {
		try {
			final ItemsResponse itemResp = itemsInvokerService.findItemsByCondition(shopId, numiids);
			return ControlerResult.newSuccess(itemResp);
		} catch (final RuleCenterRuntimeException e) {
			return ControlerResult.newError(e.getMessage());
		}
	}

}