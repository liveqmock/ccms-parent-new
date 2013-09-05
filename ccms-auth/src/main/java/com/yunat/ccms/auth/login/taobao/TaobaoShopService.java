package com.yunat.ccms.auth.login.taobao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaobaoShopService {

	@Autowired
	private TaobaoShopRepository taobaoShopRepository;

	@Transactional(readOnly = true)
	public List<TaobaoShop> list() {
		return taobaoShopRepository.findAll();
	}

	@Transactional(readOnly = true)
	public TaobaoShop get(String shopId) {
		return taobaoShopRepository.findOne(shopId);
	}
	
	/**
	 * 返回所有淘宝店铺
	 * @return
	 */
	public List<TaobaoShop> taoBaoList() {
		List<TaobaoShop> shops = taobaoShopRepository.findAll();
		List<TaobaoShop> newShops = new ArrayList<TaobaoShop>();
		if(shops.size()>0){
			for (TaobaoShop taobaoShop : shops) {
				if(!taobaoShop.getShopId().contains("|")){
					newShops.add(taobaoShop);
				}
			}
		}
		return taobaoShopRepository.findAll();
	}
}
