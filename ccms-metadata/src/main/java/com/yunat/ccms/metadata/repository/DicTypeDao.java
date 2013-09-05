package com.yunat.ccms.metadata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.metadata.pojo.DicType;

@Repository
public interface DicTypeDao extends JpaRepository<DicType, Long> {

	@Query("select t from DicType t where dicTypeId in (?1)")
	public List<DicType> findByIds(List<Long> ids);
}
