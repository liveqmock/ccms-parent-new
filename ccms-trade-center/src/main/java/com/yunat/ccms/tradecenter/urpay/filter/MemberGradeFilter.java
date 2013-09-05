package com.yunat.ccms.tradecenter.urpay.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.MemberDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.service.MemberGradeService;

/**
 * 会员等级过滤器
 * 
 * 从会员登记表查询
 * 
 * 不 限(-1)： 新 客 户(0): 没查到数据 或 (grade = 0 and trade_count = 0) 未 分 级 (99): grade
 * = 0 and trade_count > 0 普通会员(1): grade = 1 高级会员(2): grade = 2 VIP (3)： grade
 * = 3 至尊VIP(4)： grade = 4
 * 
 * @author shaohui.li
 * @version $Id: MemberGradeFilter.java, v 0.1 2013-5-31 上午10:43:11 shaohui.li
 *          Exp $
 */
@Component("memberGradeFilter")
public class MemberGradeFilter implements IFilter {
	/** 会员等级 服务 **/
	@Autowired
	private MemberGradeService memberGradeService;

	/** 新用户 **/
	public static final String NEW_CUSTOMER = "0";

	/** 未分级 **/
	public static final String NO_CLASS = "99";

	@Override
	public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {
		FilterResult result = new FilterResult();
		MemberDomain member = memberGradeService.getMemberByDpIdAndCustomerNo(order.getDpId(), order.getCustomerno());
		// 用户配置会员等级
		String configMember = config.getMemberGrade();
		// 用户实际等级
		String customerGrade = "";
		if (member != null) {
			// 新用户
			if (member.getGrade().equals(NEW_CUSTOMER) && member.getTradeCount() == 0) {
				customerGrade = NEW_CUSTOMER;
			} else if (member.getGrade().equals(NEW_CUSTOMER) && member.getTradeCount() > 0) {// 未分级
				customerGrade = NO_CLASS;
			} else {
				customerGrade = member.getGrade();
			}
		} else {
			customerGrade = NEW_CUSTOMER;
		}
		// 如果用户设置包括用户等级，则不用过滤
		if (StringUtils.contains(configMember, customerGrade)) {
			return result;
		} else {
			result.setFilter(true);
			return result;
		}
	}

	@Override
	public String getFilterName() {
		return getClass().getSimpleName();
	}
}
