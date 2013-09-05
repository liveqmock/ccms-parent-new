package com.yunat.ccms.rule.center.memo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * 
 * @author tao.yang
 *
 */
public interface RcJobTaobaoMemoRepository extends JpaRepository<RcJobTaobaoMemo, String> {
	List<RcJobTaobaoMemo> findByStatusOrderBySubmitTimeAsc(String status);
}
