package com.yunat.ccms.metadata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.metadata.pojo.DatabaseTable;

@Repository
public interface DatabaseTableDao extends JpaRepository<DatabaseTable, Long>{

	@Query("select t from DatabaseTable t where t.dbName =?1")
	public DatabaseTable findByTableName(String tableName);
}
