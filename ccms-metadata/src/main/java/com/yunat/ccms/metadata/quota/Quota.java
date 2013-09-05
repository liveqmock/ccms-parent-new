package com.yunat.ccms.metadata.quota;

import java.util.List;

import com.yunat.ccms.metadata.metamodel.EnumQueryType;

public interface Quota {

	List<String> getOrderedTableNameList();

	String toSql();

	String getChineseName();

	EnumQueryType getType();

	String getGroup();
}
