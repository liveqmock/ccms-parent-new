package com.yunat.ccms.biz.repository.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.biz.domain.Program;
import com.yunat.ccms.biz.repository.ProgramRepository;
import com.yunat.ccms.biz.repository.specification.ProgramSpecifications;

@Repository
public class ProgramRepositoryImpl implements ProgramRepository {

	@PersistenceContext
	private EntityManager entityManager;

	private SimpleJpaRepository<Program, Long> programRepository;

	@Override
	public List<Program> findByProgName(String progName) {
		return programRepository.findAll(ProgramSpecifications.progName(progName));
	}

	@Override
	public List<Program> findByProgNameAndNotProgId(String progName, Long progId) {
		return programRepository.findAll(ProgramSpecifications.progNameAndNotProgId(progName, progId));
	}

	@Override
	public Page<Program> findByFilterName(User user, String filterName,
			Pageable pageable) {
		return programRepository.findAll(ProgramSpecifications.filterName(user.getId(), filterName), pageable);
	}

	@Override
	public List<Program> findAll() {
		return programRepository.findAll();
	}

	@Override
	public Program findById(Long progId) {
		return programRepository.findOne(progId);
	}

	@Override
	public void save(Program program) {
		programRepository.save(program);
	}

	@Override
	public void saveAndFlush(Program program) {
		programRepository.saveAndFlush(program);
	}

	@Override
	public void delete(Program program) {
		programRepository.delete(program);
	}

	public void setProgramRepository(
			SimpleJpaRepository<Program, Long> programRepository) {
		this.programRepository = programRepository;
	}

	/**
	 * An initialization method which is run after the bean has been
	 * constructed. This ensures that the entity manager is injected before we
	 * try to use it.
	 */
	@PostConstruct
	public void init() {
		JpaEntityInformation<Program, Long> programEntityInfo = new JpaMetamodelEntityInformation<Program, Long>(
				Program.class, entityManager.getMetamodel());
		programRepository = new SimpleJpaRepository<Program, Long>(
				programEntityInfo, entityManager);
	}
}