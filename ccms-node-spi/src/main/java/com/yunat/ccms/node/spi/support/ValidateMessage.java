package com.yunat.ccms.node.spi.support;

public abstract class ValidateMessage {

	/**
	 * 验证消息的类型
	 * 
	 * @return
	 */
	public abstract Type getType();

	/**
	 * 获得验证的消息
	 * 
	 * @return
	 */
	public abstract String getMessage();

	/**
	 * 获得消息所属的节点ID
	 * 
	 * @return
	 */
	public abstract Long getNodeId();

	public static enum Type {
		WARN, ERROR
	}

	/**
	 * 流程配置相关错误信息
	 * 
	 * @param message
	 * @return
	 */
	public static ValidateMessage forError(String message) {
		return new Message(Type.ERROR, message);
	}

	/**
	 * 节点配置相关错误信息
	 * 
	 * @param message
	 * @return
	 */
	public static ValidateMessage forNodeError(String message, Long nodeId) {
		return new Message(Type.ERROR, message, nodeId);
	}

	/**
	 * 流程配置相关警告信息
	 * 
	 * @param message
	 * @return
	 */
	public static ValidateMessage forWarn(String message) {
		return new Message(Type.WARN, message);
	}

	/**
	 * 节点配置相关警告信息
	 * 
	 * @param message
	 * @return
	 */
	public static ValidateMessage forNodeWarn(String message, Long nodeId) {
		return new Message(Type.WARN, message, nodeId);
	}

	/**
	 * 内部使用
	 * 
	 * @author xiaojing.qu
	 * 
	 */
	public static class Message extends ValidateMessage {

		final Type type;
		final String message;
		final Long nodeId;

		Message(Type type, String message) {
			this.type = type;
			this.message = message;
			this.nodeId = null;
		}

		Message(Type type, String message, Long nodeId) {
			this.type = type;
			this.message = message;
			this.nodeId = nodeId;
		}

		@Override
		public Type getType() {
			return type;
		}

		@Override
		public String getMessage() {
			return message;
		}

		@Override
		public Long getNodeId() {
			return nodeId;
		}

		@Override
		public String toString() {
			return "Message [type=" + type + ", message=" + message + ", nodeId=" + nodeId + "]";
		}
	}

}
