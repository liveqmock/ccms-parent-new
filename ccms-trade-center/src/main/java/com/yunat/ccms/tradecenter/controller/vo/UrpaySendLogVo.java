package com.yunat.ccms.tradecenter.controller.vo;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

/**
 * 催付订单成功记录
 *
 * @author ming.peng
 * @date 2013-6-9
 * @since 4.2.0
 */
public class UrpaySendLogVo extends Pagination<Map<String, Object>> {

	public void setContent(List<Map<String, Object>> content) {
		if (CollectionUtils.isNotEmpty(content)){
			Map<Integer, String> types = UserInteractionType.getTypeMsgMap();
			for (Map<String, Object> item : content) {
				item.put("type", types.get(item.get("type")));
			}
		}
		super.setContent(content);
	}

}
