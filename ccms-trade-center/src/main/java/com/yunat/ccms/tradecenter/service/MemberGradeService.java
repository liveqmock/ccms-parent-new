package com.yunat.ccms.tradecenter.service;

import com.yunat.ccms.tradecenter.domain.MemberDomain;

/**
 *
 * 会员等级服务接口
 *
 * @author shaohui.li
 * @version $Id: MemberGradeService.java, v 0.1 2013-6-3 下午04:28:06 shaohui.li Exp $
 */
public interface MemberGradeService {

    /**
    *
    *根据店铺和买家昵称查询会员等级
    * @param dpId:店铺Id
    * @param customerNo:买家昵称
    * @return
    */
    MemberDomain getMemberByDpIdAndCustomerNo(String dpId,String customerNo);
}
