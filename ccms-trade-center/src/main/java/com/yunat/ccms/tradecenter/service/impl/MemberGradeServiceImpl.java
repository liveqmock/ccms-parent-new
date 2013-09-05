package com.yunat.ccms.tradecenter.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.tradecenter.domain.MemberDomain;
import com.yunat.ccms.tradecenter.repository.MemberGradeRepository;
import com.yunat.ccms.tradecenter.service.MemberGradeService;

/**
 *
 * 会员等级服务实现类
 * @author shaohui.li
 * @version $Id: MemberGradeServiceImpl.java, v 0.1 2013-6-3 下午04:31:03 shaohui.li Exp $
 */
@Service("memberGradeService")
public class MemberGradeServiceImpl implements MemberGradeService{

    @Autowired
    MemberGradeRepository memberGradeRepository;

    @Override
    public MemberDomain getMemberByDpIdAndCustomerNo(String dpId, String customerNo) {
        return memberGradeRepository.getMemberByDpIdAndCustomerNo(dpId, customerNo);
    }

}
