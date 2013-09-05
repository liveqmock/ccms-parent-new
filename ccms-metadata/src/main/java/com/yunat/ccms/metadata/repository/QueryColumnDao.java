package com.yunat.ccms.metadata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.metadata.pojo.QueryColumn;

@Repository
public interface QueryColumnDao extends JpaRepository<QueryColumn, Long> {

}
