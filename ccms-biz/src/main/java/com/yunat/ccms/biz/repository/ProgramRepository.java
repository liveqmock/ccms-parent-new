package com.yunat.ccms.biz.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.biz.domain.Program;

public interface ProgramRepository {

	List<Program> findByProgName(String progName);

	List<Program> findByProgNameAndNotProgId(String progName, Long progId);

	Page<Program> findByFilterName(User user, String filterName,
			Pageable pageable);

	void save(Program program);

	void saveAndFlush(Program program);

	void delete(Program program);

	List<Program> findAll();

	Program findById(Long progId);

}
