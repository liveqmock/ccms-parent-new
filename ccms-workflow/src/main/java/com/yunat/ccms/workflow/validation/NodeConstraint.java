package com.yunat.ccms.workflow.validation;

/**
 * 对节点的约束
 * 
 * @author xiaojing.qu
 * 
 */
@SuppressWarnings("rawtypes")
public class NodeConstraint {
	private String nodeType;
	private boolean hasCountLog;
	private boolean cloneable;
	private boolean editableWhileJobExecuting;

	private Class entityClass;
	private Class handlerClass;
	private Class validatorClass;
	private Class processorClass;

	public NodeConstraint(String nodeType) {
		super();
		this.nodeType = nodeType;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public boolean hasCountLog() {
		return hasCountLog;
	}

	public void setHasCountLog(boolean hasCountLog) {
		this.hasCountLog = hasCountLog;
	}

	public boolean isCloneable() {
		return cloneable;
	}

	public void setCloneable(boolean cloneable) {
		this.cloneable = cloneable;
	}

	public boolean isEditableWhileJobExecuting() {
		return editableWhileJobExecuting;
	}

	public void setEditableWhileJobExecuting(boolean editableWhileJobExecuting) {
		this.editableWhileJobExecuting = editableWhileJobExecuting;
	}

	public Class getHandlerClass() {
		return handlerClass;
	}

	public void setHandlerClass(Class handlerClass) {
		this.handlerClass = handlerClass;
	}

	public Class getValidatorClass() {
		return validatorClass;
	}

	public void setValidatorClass(Class validatorClass) {
		this.validatorClass = validatorClass;
	}

	public Class getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class nodeEntityClass) {
		this.entityClass = nodeEntityClass;
	}

	public Class getProcessorClass() {
		return processorClass;
	}

	public void setProcessorClass(Class processorClass) {
		this.processorClass = processorClass;
	}

	public boolean isHasCountLog() {
		return hasCountLog;
	}

}