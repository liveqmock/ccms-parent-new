package com.yunat.ccms.core.support.utils.exception;

public interface ThrowableHandler<T extends Throwable> {

	void handle(T e) throws RuntimeException;
}
