package com.yunat.ccms.channel.external;

public interface RetryPolicy {
	public boolean needRetry(Exception e);
	
	public Long getDelay(Long lastDelay);
	
	public void doWhenFail(Exception e);
	
	public void doWhenFinallyFail(Exception e);
}