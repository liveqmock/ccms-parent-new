package com.yunat.ccms.metadata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.metadata.pojo.DicTypeValue;

@Repository
public interface DicTypeValueDao extends JpaRepository<DicTypeValue, Long> {

	List<DicTypeValue> findByDicTypeDicTypeId(long dicId);

	List<DicTypeValue> findByParentDicTypeValueId(long parentId);
}
