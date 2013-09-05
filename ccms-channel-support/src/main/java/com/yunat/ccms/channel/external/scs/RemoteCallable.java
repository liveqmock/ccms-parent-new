package com.yunat.ccms.channel.external.scs;

public interface RemoteCallable<T> {
	public T call() throws Exception;
	public String getTaskId() throws Exception;
}