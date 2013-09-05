package com.yunat.ccms.rule.center.conf.planGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanGroupRepository extends JpaRepository<PlanGroup, String> {

}
