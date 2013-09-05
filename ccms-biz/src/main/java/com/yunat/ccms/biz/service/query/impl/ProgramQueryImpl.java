package com.yunat.ccms.biz.service.query.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.biz.domain.Program;
import com.yunat.ccms.biz.repository.ProgramRepository;
import com.yunat.ccms.biz.service.query.ProgramQuery;

@Service
public class ProgramQueryImpl implements ProgramQuery {

	@Autowired
	private ProgramRepository programRepository;

	@Override
	public List<Program> findAll() {
		return programRepository.findAll();
	}

	@Override
	public List<Program> findByProgName(String progName) {
		return programRepository.findByProgName(progName);
	}

	@Override
	public List<Program> findByProgNameAndNotProgId(String progName, Long progId) {
		return programRepository.findByProgNameAndNotProgId(progName, progId);
	}

	@Override
	public Program findById(Long progId) {
		return programRepository.findById(progId);
	}

	@Override
	public Page<Program> findByFilterName(User user, String filterName,
			Pageable pageable) {
		return programRepository.findByFilterName(user, filterName, pageable);
	}
}