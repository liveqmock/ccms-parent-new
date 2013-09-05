package com.yunat.ccms.module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<ModuleFromDB, Long> {

	@SuppressWarnings("unchecked")
	@Override
	@Secured({ "addAcl" })
	ModuleFromDB save(ModuleFromDB moduleFromDB);

	@Override
	@Secured({ "addAcl" })
	ModuleFromDB findOne(Long id);
}
