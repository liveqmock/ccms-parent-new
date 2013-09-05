package com.yunat.ccms.node.spi;


/**
 * 节点处理异常
 */
public class NodeProcessingException extends RuntimeException {


	private static final long serialVersionUID = 7862554352955149457L;

	public NodeProcessingException(String msg) {
		super(msg);
	}

	public NodeProcessingException(String msg, Throwable t) {
		super(msg,t);
	}
	

}
