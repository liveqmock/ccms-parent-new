package com.yunat.ccms.auth.login.taobao.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.yunat.ccms.auth.login.taobao.TaobaoUser;
import com.yunat.ccms.auth.login.taobao.TaobaoUserService;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.auth.user.UserCommand;
import com.yunat.ccms.core.support.vo.PagedResultVo;

@Controller
public class TaobaoUserController {
	@Autowired
	private UserCommand userCommand;
	
	@Autowired
	private TaobaoUserService taobaoUserService;

	@ResponseBody
	@RequestMapping(value = "/user/taobao/{id}", method = RequestMethod.PUT)
	public void updateTaobaoUserStatus(@PathVariable final Long id, @RequestParam final boolean disabled)
			throws Exception {
		try {
			final TaobaoUser taobaoUser = taobaoUserService.findUserByOne(id);
			User user = taobaoUser.getUser();
			user.setDisabled(disabled);
			userCommand.saveOrUpdate(user);
		} catch (Exception e) {
			throw new Exception("更新旺旺子账号状态失败！");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/user/taobao/list", method = RequestMethod.GET)
	public PagedResultVo<TaobaoUserViewResult> paginationTaobaoUserList(@RequestParam int page,
			@RequestParam int rp, @RequestParam String query, @RequestParam String sortname,
			@RequestParam String sortorder) throws Exception {
		try {
			Sort sort = null;
			Pageable pageable = new PageRequest(page - 1, rp);
			if (StringUtils.isNotBlank(sortname) && StringUtils.isNotBlank(sortorder)) {
				sort = new Sort(Direction.ASC.toString().equalsIgnoreCase(sortorder) ? Direction.ASC : Direction.DESC,
						sortname);
				pageable = new PageRequest(page - 1, rp, sort);
			}

			PagedResultVo<TaobaoUserViewResult> vo = new PagedResultVo<TaobaoUserViewResult>();
			Page<TaobaoUser> rltPage = taobaoUserService.findAll(query, pageable);
			List<TaobaoUserViewResult> respList = Lists.newArrayList();
			for (TaobaoUser taobaoUser : rltPage.getContent()) {
				TaobaoUserViewResult resp = new TaobaoUserViewResult();
				resp.setId(taobaoUser.getId());
				resp.setPlatUserId(taobaoUser.getPlatUserId());
				resp.setPlatUserName(taobaoUser.getPlatUserName());
				resp.setShopName(taobaoUser.getPlatShop().getShopName());
				resp.setSubuser(taobaoUser.getIsSubuser());
				resp.setDisabled(taobaoUser.getUser().getDisabled());
				respList.add(resp);
			}
			vo.setData(respList);
			vo.setPage(page);
			vo.setTotal(rltPage.getTotalElements());
			return vo;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("得到旺旺子账号列表数据失败！");
		}
	}

}
