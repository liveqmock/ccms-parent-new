package com.yunat.ccms.biz.service.query.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.biz.domain.ProgramCategory;
import com.yunat.ccms.biz.repository.ProgramCategoryRepository;
import com.yunat.ccms.biz.service.query.ProgramCategoryQuery;

@Service
public class ProgramCategoryQueryImpl implements ProgramCategoryQuery {

	@Autowired
	private ProgramCategoryRepository repository;
	
	@Override
	public ProgramCategory findById(Long id) {
		return repository.findOne(id);
	}

}