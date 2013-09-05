package com.yunat.ccms.rule.center.drl.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.metadata.pojo.DatabaseColumn;
import com.yunat.ccms.metadata.repository.DatabaseColumnDao;

@Component
public class PropertyIdConverter {

	@Autowired
	private DatabaseColumnDao databaseColumnDao;

	public String propertyId(final Long columnId, final Long tableId) {
		final DatabaseColumn databaseColumn = databaseColumnDao.findByTableTableIdAndColumnId(tableId, columnId);
		return FieldPropertyConverter.columnNameToPropertyName(databaseColumn.getColumnName());
	}
}