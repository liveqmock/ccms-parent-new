package com.yunat.ccms.rule.center.engine;

import java.io.Serializable;

public interface FactResolver<ID extends Serializable> {

	Fact<ID> resolve(ID factId);

	boolean support(Class<?> clazz);
}
