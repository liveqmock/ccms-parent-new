package com.yunat.ccms.core.support.utils;

/**
 * 本类对象用来在finally中作为回调
 * 
 * @author MaGiCalL
 */
public interface FinallyCallback {

	/**
	 * 将在finally块中调用的方法.
	 */
	void finallyExecute();

	public static FinallyCallback DO_NOTHING = new FinallyCallback() {
		@Override
		public void finallyExecute() {
		}
	};
}
