package com.yunat.ccms.auth.login.taobao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaobaoUserRepository extends JpaRepository<TaobaoUser, Long>, JpaSpecificationExecutor<TaobaoUser> {

	@Query
	TaobaoUser getByPlatUserId(String platUserId);
}
