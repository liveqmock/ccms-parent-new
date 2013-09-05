package com.yunat.ccms.rule.center.conf.condition;

import java.util.Arrays;
import java.util.List;

import org.jooq.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.api.domain.Item;
import com.taobao.api.request.ItemsListGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.response.ItemsListGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.ccms.channel.external.taobao.handler.CommonInvokerHandler;
import com.yunat.ccms.rule.center.RuleCenterRuntimeException;
import com.yunat.ccms.ucenter.rest.service.AccessTokenService;
import com.yunat.ccms.ucenter.rest.vo.AccessToken;

@Component
public class ItemsInvokerServiceImpl implements ItemsInvokerService {

	private static final int TAOBAO_API_MAX_PAGE_SIZE = 20;

	@Autowired
	private AccessTokenService accessTokenService;

	@Autowired
	private CommonInvokerHandler invokerHandler;

	@Override
	public ItemsResponse searchProducts(final String shopId, final String q, final long pageNo, final long pageSize)
			throws RuleCenterRuntimeException {
		final ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();
		req.setFields("num_iid,title,pic_url,price");
		req.setPageNo(pageNo);
		req.setPageSize(pageSize);
		req.setQ(q);

		final AccessToken accessToken = accessTokenService.getAccessToken(PlatEnum.taobao, shopId);
		final ItemsOnsaleGetResponse resp = invokerHandler.execute(req, accessToken.getAccessToken());

		final ItemsResponse itemResp = new ItemsResponse();
		final List<Item> items = resp.getItems();
		final List<ItemWrapper> wrapperItems = Lists.newArrayList();
		if (items != null) {
			for (final Item item : items) {
				final ItemWrapper itemWrapper = new ItemWrapper();
				itemWrapper.setNumIid(item.getNumIid());
				itemWrapper.setPicUrl(item.getPicUrl());
				itemWrapper.setPrice(item.getPrice());
				itemWrapper.setTitle(item.getTitle());
				wrapperItems.add(itemWrapper);
			}
		}
		itemResp.setCount(resp.getTotalResults());
		itemResp.setList(wrapperItems);
		return itemResp;
	}

	@Override
	public ItemsResponse findItemsByCondition(final String shopId, final String numiids)
			throws RuleCenterRuntimeException {
		final ItemsListGetRequest req = new ItemsListGetRequest();
		req.setFields("num_iid,title,pic_url,price");
		final AccessToken accessToken = accessTokenService.getAccessToken(PlatEnum.taobao, shopId);
		final ItemsResponse itemResp = new ItemsResponse();
		final List<ItemWrapper> wrapperItems = Lists.newArrayList();

		final String[] numiidArr = numiids.split(",");
		for (int i = 0; i < numiidArr.length; i += TAOBAO_API_MAX_PAGE_SIZE) {
			String[] newArr = null;
			if (numiidArr.length - i < TAOBAO_API_MAX_PAGE_SIZE) {
				newArr = Arrays.copyOfRange(numiidArr, i, numiidArr.length);
			} else {
				newArr = Arrays.copyOfRange(numiidArr, i, i + TAOBAO_API_MAX_PAGE_SIZE);
			}
			final String newNumiids = StringUtils.join(newArr, ",");

			req.setNumIids(newNumiids);
			// TODO 20 times on request !
			final ItemsListGetResponse resp = invokerHandler.execute(req, accessToken.getAccessToken());
			final List<Item> items = resp.getItems();
			if (items != null) {
				for (final Item item : items) {
					final ItemWrapper itemWrapper = new ItemWrapper();
					itemWrapper.setNumIid(item.getNumIid());
					itemWrapper.setPicUrl(item.getPicUrl());
					itemWrapper.setPrice(item.getPrice());
					itemWrapper.setTitle(item.getTitle());
					wrapperItems.add(itemWrapper);
				}
			}
		}
		itemResp.setList(wrapperItems);

		return itemResp;
	}

	public static void main(final String[] args) {
		final String numiids = "1,2,3,4,5,6,7,8,9,1,2,3,4,5,6,7,8,9,1,2,3,1,2,3,4,5,6,7,8,9,1,2,3,4,5,6,7,8,9,1";
		final String[] numiidArr = numiids.split(",");
		for (int i = 0; i < numiidArr.length; i += 20) {
			String[] newArr = null;
			if (numiidArr.length - i < 20) {
				newArr = Arrays.copyOfRange(numiidArr, i, numiidArr.length);
			} else {
				newArr = Arrays.copyOfRange(numiidArr, i, i + 20);
			}
			final String newNumiids = StringUtils.join(newArr, ",");
			System.out.println(newNumiids);
		}
	}
}
