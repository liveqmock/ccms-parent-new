/**
 *
 */
package com.yunat.ccms.tradecenter.thread;

import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.ThreadPool;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-18 下午01:53:02
 */
public class ThreadApp {

	/** 物流线程池*/
	private static ThreadPool simpleThreadPool = null;

	static{
		// 初始化线程池
		simpleThreadPool = new SimpleThreadPool(60, 9);
		try {
			simpleThreadPool.initialize();
		} catch (Exception e) {
			throw new RuntimeException("ThreadPool init faild!");
		}
	}

	public static ThreadPool getSimpleThreadPool() {
		return simpleThreadPool;
	}

}
