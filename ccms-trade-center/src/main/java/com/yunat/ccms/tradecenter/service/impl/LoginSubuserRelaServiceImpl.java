package com.yunat.ccms.tradecenter.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.tradecenter.domain.LoginSubuserRelaDomain;
import com.yunat.ccms.tradecenter.domain.LoginSubuserRelaLogDomain;
import com.yunat.ccms.tradecenter.repository.LoginSubuserRelaLogRepository;
import com.yunat.ccms.tradecenter.repository.LoginSubuserRelaRepository;
import com.yunat.ccms.tradecenter.service.BaseService;
import com.yunat.ccms.tradecenter.service.LoginSubuserRelaService;

@Service
public class LoginSubuserRelaServiceImpl  extends BaseService implements LoginSubuserRelaService{

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private LoginSubuserRelaRepository loginSubuserRelaRepository;

	@Autowired
	private LoginSubuserRelaLogRepository loginSubuserRelaLogRepository;

	@Override
	public LoginSubuserRelaDomain findloginSubuserRela(String dpId) {
		return loginSubuserRelaRepository.findOneByloginName(getLoginName(), dpId);
	}

	@Override
	@Transactional
	public boolean save(LoginSubuserRelaDomain loginSubuserRelaDomain) {
		try {
			LoginSubuserRelaLogDomain lsrld = new LoginSubuserRelaLogDomain();
			lsrld.setCreated(new Date());
			lsrld.setLoginName(getLoginName());
			lsrld.setNextTaobaoSubuser(loginSubuserRelaDomain.getTaobaoSubuser());

			LoginSubuserRelaDomain lsrd = loginSubuserRelaRepository.findOneByloginName(getLoginName(), loginSubuserRelaDomain.getDpId());
			if(lsrd == null){
				loginSubuserRelaDomain.setCreated(new Date());
				loginSubuserRelaDomain.setLoginName(getLoginName());
				loginSubuserRelaRepository.save(loginSubuserRelaDomain);
			}else{
				lsrld.setLastTaobaoSubuser(lsrd.getTaobaoSubuser());
				lsrd.setTaobaoSubuser(loginSubuserRelaDomain.getTaobaoSubuser());
				lsrd.setUpdated(new Date());
				loginSubuserRelaRepository.save(lsrd);
			}
			loginSubuserRelaLogRepository.saveAndFlush(lsrld);
		} catch (Exception e) {
			logger.error("LoginSubuserRelaServiceImpl.save 异常：", e);
			return false;
		}
		return true;
	}

}
