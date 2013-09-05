package com.yunat.ccms.tradecenter.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MemberDomainPK implements java.io.Serializable{

    /**  */
    private static final long serialVersionUID = 5003370791491335483L;

    /** 店铺id **/
    @Column(name = "dp_id")
    private String dpId;

    /** 买家昵称 **/
    @Column(name = "customerno")
    private String customerNo;


    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((dpId == null) ? 0 : dpId.hashCode());
        result = PRIME * result
                + ((customerNo == null) ? 0 : customerNo.hashCode());
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

        final MemberDomainPK other = (MemberDomainPK) obj;
        if (dpId == null) {
            if (other.dpId != null)
                return false;
        } else if (!dpId.equals(other.dpId))
            return false;

        if (customerNo == null) {
            if (other.customerNo != null)
                return false;
        } else if (!customerNo.equals(other.customerNo))
            return false;
        return true;
    }
}
