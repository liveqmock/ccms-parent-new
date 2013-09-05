package com.yunat.ccms.biz.service.query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.biz.domain.Program;

public interface ProgramQuery {
	List<Program> findAll();

	List<Program> findByProgName(String progName);

	List<Program> findByProgNameAndNotProgId(String progName, Long progId);

	Program findById(Long progId);

	Page<Program> findByFilterName(User user, String filterName,
			Pageable pageable);
}