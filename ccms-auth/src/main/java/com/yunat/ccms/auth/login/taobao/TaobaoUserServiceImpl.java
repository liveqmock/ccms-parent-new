package com.yunat.ccms.auth.login.taobao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TaobaoUserServiceImpl implements TaobaoUserService {
	
	@Autowired
	private TaobaoUserRepository taobaoUserRepository;
	
	public TaobaoUser findUserByOne(final Long id) throws Exception {
		try {
			return taobaoUserRepository.findOne(id);
		} catch (Exception e) {
			throw new Exception("获取用户失败！");
		}
	}

	@Override
	public void saveOrUpdate(TaobaoUser taobaoUser) {
		taobaoUserRepository.saveAndFlush(taobaoUser);
	}

	@Override
	public Page<TaobaoUser> findAll(String taobaoUsername, Pageable pageable) {
		if (StringUtils.isEmpty(taobaoUsername)) {
			return taobaoUserRepository.findAll(pageable);
		}
		return taobaoUserRepository.findAll(queryUsername(taobaoUsername), pageable);
	}

	public static Specification<TaobaoUser> queryUsername(final String username) {
		return new Specification<TaobaoUser>() {

			@Override
			public Predicate toPredicate(Root<TaobaoUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("platUserName"), username); 
			}
		};
	}

}