package com.yunat.ccms.metadata.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.metadata.pojo.DatabaseColumn;

@Repository
public interface DatabaseColumnDao extends JpaRepository<DatabaseColumn, Long> {

	public List<DatabaseColumn> findByColumnIdIn(Collection<Long> columnIds);

	public List<DatabaseColumn> findByTableTableId(long tableId);

	public DatabaseColumn findByTableTableIdAndColumnId(long tableId, long columnId);
}
