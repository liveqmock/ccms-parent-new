package com.yunat.ccms.tradecenter.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author tim.yin
 * 
 */

@Embeddable
public class TraderateDomainPK implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "tid")
	private String tid;

	@Column(name = "oid")
	private String oid;

	public TraderateDomainPK() {
		super();
	}

	public TraderateDomainPK(String tid, String oid) {
		super();
		this.tid = tid;
		this.oid = oid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((tid == null) ? 0 : tid.hashCode());
		result = PRIME * result + ((oid == null) ? 0 : oid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TraderateDomainPK other = (TraderateDomainPK) obj;
		if (tid == null) {
			if (other.tid != null)
				return false;
		} else if (!tid.equals(other.tid))
			return false;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

}
