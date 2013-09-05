package com.yunat.ccms.tradecenter;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.tradecenter.domain.MemberDomain;
import com.yunat.ccms.tradecenter.service.MemberGradeService;

public class MemberGradeServiceTest extends AbstractJunit4SpringContextBaseTests{

    @Autowired
    private MemberGradeService memberGradeService;

    @Test
    public void queryMemberGrade(){
        String dpId = "123";
        String customerNo = "lsh";
        MemberDomain d = memberGradeService.getMemberByDpIdAndCustomerNo(dpId, customerNo);
        if(d != null){
            System.out.println(d.getGrade());
        }
    }

}
