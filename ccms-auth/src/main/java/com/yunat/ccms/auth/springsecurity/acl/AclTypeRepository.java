package com.yunat.ccms.auth.springsecurity.acl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AclTypeRepository extends JpaRepository<AclOidType, Long> {

	@Query("select a from AclOidType a where a.typeName=:typeName")
	AclOidType findByTypeName(@Param("typeName") String typeName);
}
