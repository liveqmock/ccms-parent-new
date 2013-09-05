package com.yunat.ccms.rule.center.drl;

public interface DRLFragmentBuilder<T> {
	public DRLFragment toDRL(T obj);
}
