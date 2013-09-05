package com.yunat.ccms.core.support.vo;

import java.io.Serializable;

/**
 * 用于在某些场合传输对象的最简单摘要:只有id和名字
 * 
 * @author wenjian.liang
 * 
 */
public class IdName implements Serializable {

	private static final long serialVersionUID = -7271900037682569835L;

	protected Long id;
	protected String name;

	public IdName() {
		super();
	}

	public IdName(final Long id, final String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "IdName [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final IdName other = (IdName) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
