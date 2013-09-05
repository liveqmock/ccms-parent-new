package com.yunat.ccms.rule.center.runtime.job;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RcJobRepository extends JpaRepository<RcJob, Long> {

	List<RcJob> findByStatusOrderBySubmitTimeAsc(String status);

}
