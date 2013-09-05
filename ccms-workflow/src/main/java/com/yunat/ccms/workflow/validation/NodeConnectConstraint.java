package com.yunat.ccms.workflow.validation;

import java.util.Set;

/**
 * 对节点间连接的约束
 * 
 * @author tao.yang
 * 
 */
public class NodeConnectConstraint {

	private String nodeType;
	private int inCount;
	private int outCount;

	private Set<ConnectionCondition> outIncludeSet;
	private Set<ConnectionCondition> outExcludeSet;
	private Set<ConnectionCondition> inIncludeSet;
	private Set<ConnectionCondition> inExcludeSet;

	public ConnectionCondition crateConnectionCondition() {
		return new ConnectionCondition();
	}

	public class ConnectionCondition {
		private boolean isGroup;
		private String targetnodeType;

		public boolean isGroup() {
			return isGroup;
		}

		public void setGroup(boolean isGroup) {
			this.isGroup = isGroup;
		}

		public String getTargetnodeType() {
			return targetnodeType;
		}

		public void setTargetnodeType(String targetnodeType) {
			this.targetnodeType = targetnodeType;
		}
	}

	public int getInCount() {
		return inCount;
	}

	public void setInCount(int inCount) {
		this.inCount = inCount;
	}

	public int getOutCount() {
		return outCount;
	}

	public void setOutCount(int outCount) {
		this.outCount = outCount;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public Set<ConnectionCondition> getOutIncludeSet() {
		return outIncludeSet;
	}

	public void setOutIncludeSet(Set<ConnectionCondition> outIncludeSet) {
		this.outIncludeSet = outIncludeSet;
	}

	public Set<ConnectionCondition> getOutExcludeSet() {
		return outExcludeSet;
	}

	public void setOutExcludeSet(Set<ConnectionCondition> outExcludeSet) {
		this.outExcludeSet = outExcludeSet;
	}

	public Set<ConnectionCondition> getInIncludeSet() {
		return inIncludeSet;
	}

	public void setInIncludeSet(Set<ConnectionCondition> inIncludeSet) {
		this.inIncludeSet = inIncludeSet;
	}

	public Set<ConnectionCondition> getInExcludeSet() {
		return inExcludeSet;
	}

	public void setInExcludeSet(Set<ConnectionCondition> inExcludeSet) {
		this.inExcludeSet = inExcludeSet;
	}
}