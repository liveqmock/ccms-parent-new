package com.yunat.ccms.tradecenter.service.impl;

import com.yunat.ccms.auth.login.taobao.TaobaoShop;
import com.yunat.ccms.auth.login.taobao.TaobaoShopRepository;
import com.yunat.ccms.tradecenter.domain.ShopReasonDomain;
import com.yunat.ccms.tradecenter.repository.ShopReasonRepository;
import com.yunat.ccms.tradecenter.service.ShopReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: weilin.li
 * Date: 13-8-9
 * Time: 上午11:09
 */
@Service("shopReasonService")
public class ShopReasonServiceImpl implements ShopReasonService {

    @Autowired
    private TaobaoShopRepository taobaoShopRepository;

    @Autowired
    private ShopReasonRepository shopReasonRepository;

    @Override
    public List<String> getRefundReasons(String shopId) {

        List<ShopReasonDomain> shopReasonDomainList = new ArrayList<ShopReasonDomain>();
        shopReasonDomainList = shopReasonRepository.findByShopId(shopId);

        List<String> reasons = new ArrayList<String>();

        for (int i = 0; i < shopReasonDomainList.size(); i++) {
            reasons.add(shopReasonDomainList.get(i).getReason());
        }

        return reasons;
    }
}
