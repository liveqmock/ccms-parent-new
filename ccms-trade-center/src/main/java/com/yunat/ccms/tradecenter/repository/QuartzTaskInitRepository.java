/**
 *
 */
package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.QuartzTaskInitDomain;

/**
 *数据持久化
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-4 下午04:35:03
 */
public interface QuartzTaskInitRepository extends JpaRepository<QuartzTaskInitDomain, Long>{

	@Query("from QuartzTaskInitDomain where is_valid = 1")
	List<QuartzTaskInitDomain> queryQuartzTaskList();

	@Query("from QuartzTaskInitDomain where job_name = :job_name and job_group = :job_group")
	QuartzTaskInitDomain getQuartzTask(@Param("job_name")String job_name,@Param("job_group") String job_group);

}
