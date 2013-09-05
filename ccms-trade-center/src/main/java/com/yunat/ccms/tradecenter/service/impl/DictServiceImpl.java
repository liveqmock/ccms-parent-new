package com.yunat.ccms.tradecenter.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.tradecenter.domain.DictDomain;
import com.yunat.ccms.tradecenter.repository.DictRepository;
import com.yunat.ccms.tradecenter.service.DictService;

/**
 * 字典表
 *
 * @author teng.zeng
 * date 2013-5-31 下午07:41:51
 */
@Service("dictService")
public class DictServiceImpl implements DictService{

	@Autowired
	DictRepository dictRepository;

	@Override
	public DictDomain getByTypeAndCode(Integer type, String code) {
		return dictRepository.getByTypeAndCode(type, code);
	}

	@Override
	public List<DictDomain> getByType(Integer type) {
		return dictRepository.getByType(type);
	}

}
