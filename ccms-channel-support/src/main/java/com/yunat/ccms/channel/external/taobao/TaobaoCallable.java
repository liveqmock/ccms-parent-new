package com.yunat.ccms.channel.external.taobao;


public interface TaobaoCallable<T> {
	public T call() throws Exception;
}
