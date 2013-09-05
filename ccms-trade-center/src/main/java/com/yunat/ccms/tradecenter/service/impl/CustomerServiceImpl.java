package com.yunat.ccms.tradecenter.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.tradecenter.domain.CustomerDomain;
import com.yunat.ccms.tradecenter.repository.CustomerRepository;
import com.yunat.ccms.tradecenter.service.BaseService;
import com.yunat.ccms.tradecenter.service.CustomerService;

@Service
public class CustomerServiceImpl extends BaseService implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public CustomerDomain findByCustomerno(String customerno) {
		return customerRepository.findOne(customerno);
	}

}
