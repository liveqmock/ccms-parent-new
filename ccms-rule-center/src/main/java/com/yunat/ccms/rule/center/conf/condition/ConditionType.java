package com.yunat.ccms.rule.center.conf.condition;

import java.util.HashMap;
import java.util.Map;

import com.yunat.ccms.rule.center.runtime.fact.Customer;
import com.yunat.ccms.rule.center.runtime.fact.Order;

/**
 * 条件类型(指标类型)
 * 
 * @author wenjian.liang
 */
public enum ConditionType {

	CUSTOMER(10, "基于顾客", Customer.class), //
	ORDER(11, "基于订单", Order.class), //
	;

	/*** id of tm_db_table */
	public final long tableId;

	private final String typeId;
	private final String typeName;
	private final Class<?> typeClass;

	public static ConditionType valueOfIgnoreCase(final String typeId) {
		for (final ConditionType t : values()) {
			if (t.typeId.equalsIgnoreCase(typeId)) {
				return t;
			}
		}
		return null;
	}

	private ConditionType(final long tableId, final String typeName, final Class<?> typeClass) {
		typeId = name().toLowerCase();
		this.tableId = tableId;
		this.typeName = typeName;
		this.typeClass = typeClass;
	}

	public String getTypeId() {
		return typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public long getTableId() {
		return tableId;
	}

	public Class<?> getTypeClass() {
		return typeClass;
	}

	public Map<String, Object> toMap() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("typeId", typeId);
		map.put("typeName", typeName);
		return map;
	}

}
