package com.yunat.ccms.module.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleEntryRepository extends JpaRepository<ModuleEntry, Long> {

}
