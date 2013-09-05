package com.yunat.ccms.core.support.utils.io;

import java.io.Closeable;
import java.io.IOException;

/**
 * 处理Closeable的回调接口
 * 
 * @author wenjian.liang
 * 
 * @param <C>
 */
public interface CloseableIOCallback<C extends Closeable> {
	/**
	 * 处理Closeable,只需要关心业务,不需要关系关闭流什么的
	 * 
	 * @param closeable
	 * @throws IOException
	 */
	void callback(C closeable) throws IOException;
}
