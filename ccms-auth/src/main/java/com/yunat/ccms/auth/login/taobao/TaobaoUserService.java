package com.yunat.ccms.auth.login.taobao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaobaoUserService {
	TaobaoUser findUserByOne(final Long id) throws Exception;

	void saveOrUpdate(TaobaoUser taobaoUser);

	Page<TaobaoUser> findAll(String query, Pageable pageable);
}
