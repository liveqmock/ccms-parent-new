package com.yunat.ccms.tradecenter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 *代码字典表
 * @author shaohui.li
 * @version $Id: DictDomain.java, v 0.1 2013-5-30 下午02:53:02 shaohui.li Exp $
 */

@Entity
@Table(name = "tb_tc_dict")
public class DictDomain extends BaseDomain{

    /**  */
    private static final long serialVersionUID = 6320052624541063181L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pkid")
    private Long pkid;

    /** 字典类型 **/
    @Column(name = "type")
    private Integer type;

    /** 字典代码 **/
    @Column(name = "code")
    private String code;

    /** 字典名称**/
    @Column(name = "name")
    private String name;

    /** 是否有效**/
    @Column(name = "is_valid")
    private String isValid;

	/** 排序**/
    @Column(name = "px")
    private Integer px;

    /** 备注 **/
    @Column(name = "remark")
    private String remark;

    public Long getPkid() {
        return pkid;
    }

    public void setPkid(Long pkid) {
        this.pkid = pkid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public Integer getPx() {
        return px;
    }

    public void setPx(Integer px) {
        this.px = px;
    }

    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
