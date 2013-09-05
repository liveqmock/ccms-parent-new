package com.yunat.ccms.rule.center.drl.convert;

import java.lang.reflect.Field;

import com.yunat.ccms.core.support.utils.ReflectionUtils;
import com.yunat.ccms.rule.center.runtime.fact.Customer;
import com.yunat.ccms.rule.center.runtime.fact.Order;

public class PropertyValueConverter {
	public final static Customer customer = new Customer();
	public final static Order order = new Order();

	public static JFieldType convert(final Object object, final String fieldName) {
		final Field field = ReflectionUtils.findField(object.getClass(), fieldName);
		return JFieldType.valueOf(field.getType());
	}

}