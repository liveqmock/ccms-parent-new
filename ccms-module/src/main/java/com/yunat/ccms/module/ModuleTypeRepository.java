package com.yunat.ccms.module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleTypeRepository extends JpaRepository<ModuleType, Long> {

	@SuppressWarnings("unchecked")
	@Override
	ModuleType save(ModuleType moduleType);
}
