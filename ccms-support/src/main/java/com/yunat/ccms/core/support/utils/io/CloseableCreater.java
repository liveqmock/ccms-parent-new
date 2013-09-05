package com.yunat.ccms.core.support.utils.io;

import java.io.Closeable;
import java.io.IOException;

/**
 * Closeable实例的创造器.
 * 
 * @author wenjian.liang
 * 
 * @param <C>
 */
public interface CloseableCreater<C extends Closeable> {

	C create() throws IOException;
}
