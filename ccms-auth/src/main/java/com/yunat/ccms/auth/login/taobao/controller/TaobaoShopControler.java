package com.yunat.ccms.auth.login.taobao.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.yunat.ccms.auth.login.taobao.TaobaoShop;
import com.yunat.ccms.auth.login.taobao.TaobaoShopService;
import com.yunat.ccms.core.support.vo.ControlerResult;

@Controller
@RequestMapping(value = "/shop/taobao/*")
public class TaobaoShopControler {

	@Autowired
	private TaobaoShopService taobaoShopService;

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ControlerResult listShop() {
		List<TaobaoShop> shops = taobaoShopService.list();

		// 过滤京东、当当等非淘宝店铺
		List<TaobaoShop> newShops = new ArrayList<TaobaoShop>();
		if(shops.size()>0){
			for (TaobaoShop taobaoShop : shops) {
				if(!taobaoShop.getShopId().contains("|")){
					newShops.add(taobaoShop);
				}
			}
		}
		Map<String, Object> map = Maps.newHashMap();
		map.put("shops", newShops);
		return ControlerResult.newSuccess(map);
	}

	@ResponseBody
	@RequestMapping(value = "/{shopId}", method = RequestMethod.GET)
	public ControlerResult getShop(@PathVariable String shopId) {
		TaobaoShop shop = taobaoShopService.get(shopId);
		return ControlerResult.newSuccess(shop);
	}
}
