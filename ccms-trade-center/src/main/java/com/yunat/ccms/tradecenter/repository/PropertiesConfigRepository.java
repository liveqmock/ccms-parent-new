package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.PropertiesConfigDomain;

public interface PropertiesConfigRepository extends JpaRepository<PropertiesConfigDomain, Long>{

	/**
	 * 根据group查找配置列表
	 * @param groupName
	 * @return
	 */
	List<PropertiesConfigDomain> findByDpIdAndGroupName(String dpId, String groupName);

    /**
     * 根据groupName查询
     * @param groupName
     * @return
     */
    List<PropertiesConfigDomain> findByGroupName(String groupName);

	/**
	 * 根据name和店铺id获得配置
	 * @param name
	 * @return
	 */
	PropertiesConfigDomain getByDpIdAndName(String dpId, String name);


	/**
	 * 根据name获得配置
	 * @param name
	 * @return
	 */
	PropertiesConfigDomain getByName(String name);

	@Modifying
	@Query("delete from PropertiesConfigDomain  where name = :name")
	void deleteByName(@Param("name")String name);

	@Modifying
	@Query("delete from PropertiesConfigDomain  where groupName = :groupName")
	void deleteByGroupName(@Param("groupName")String groupName);
}
