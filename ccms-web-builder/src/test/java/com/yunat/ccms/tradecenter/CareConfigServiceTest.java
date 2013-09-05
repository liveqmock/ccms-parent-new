package com.yunat.ccms.tradecenter;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.base.util.JackSonMapper;
import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.auth.login.LoginInfo;
import com.yunat.ccms.auth.login.LoginInfoHolder;
import com.yunat.ccms.auth.login.LoginInfoImpl;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.tradecenter.constant.ConstantTC;
import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.DictDomain;
import com.yunat.ccms.tradecenter.repository.DictRepository;
import com.yunat.ccms.tradecenter.service.CareConfigService;

/**
 * 任务配置测试类
 *
 * @author teng.zeng date 2013-6-13 下午05:37:03
 */
public class CareConfigServiceTest extends AbstractJunit4SpringContextBaseTests {

	@Autowired
	private CareConfigService careConfigService;

	@Autowired
	private DictRepository dictRepository;

	@Test
	public void queryCareConfigTest() {
		CareConfigDomain careConfigDomain = careConfigService.getByCareTypeAndDpId(2, "123");

		// 设置界面加载选项条件
		List<DictDomain> filterList = dictRepository.getByType(ConstantTC.CARE_FILTE_TYPE);
		careConfigDomain.setFilterConditionList(filterList);
		List<DictDomain> memberList = dictRepository.getByType(ConstantTC.MEMBER_TYPE);
		careConfigDomain.setMemberGradeList(memberList);
		careConfigDomain.setGatewayList(null);
		if (null != careConfigDomain) {
			System.out.println(JackSonMapper.toCJsonString(careConfigDomain));
		}
	}
//	static class _LoginInfoHolder extends LoginInfoHolder{
//		public static void setLoginInfo(final LoginInfo loginInfo) {
//			LOGIN_INFO_LOCAL.set(loginInfo);
//		}
//	};
	@Test
	public void saveCareConfigTest(){
//		User user=new User();
//		user.setLoginName("test");
//		_LoginInfoHolder.setLoginInfo(new LoginInfoImpl(null,user));
		CareConfigDomain careConfigDomain = new CareConfigDomain();
		careConfigDomain.setCareType(4);
		careConfigDomain.setDateType(0);
		careConfigDomain.setDateNumber(30);
		careConfigDomain.setCareStartTime(new Date());
		careConfigDomain.setCareEndTime(new Date());
		careConfigDomain.setCareStatus(1);
		careConfigDomain.setNotifyOption(1);
		careConfigDomain.setFilterCondition("1,2");
		careConfigDomain.setMemberGrade("1,2");
		careConfigDomain.setOrderMaxAcount(2.0);
		careConfigDomain.setOrderMinAcount(1.0);
		careConfigDomain.setSmsContent("test");
		careConfigDomain.setGatewayId(1);
		careConfigDomain.setIsOpen(1);
		careConfigDomain.setDpId("123456");
//		careConfigDomain.setPkid(4L);
		CareConfigDomain newCareConfigDomain = careConfigService.saveCareConfigDomain(careConfigDomain);
		if(null != newCareConfigDomain){
			System.out.println(JackSonMapper.toCJsonString(careConfigDomain));
		}
	}

}
