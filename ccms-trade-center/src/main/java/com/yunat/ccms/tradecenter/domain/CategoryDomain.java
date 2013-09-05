package com.yunat.ccms.tradecenter.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-25
 * Time: 下午6:32
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="tb_tc_category")
public class CategoryDomain {
    /**
     * 主键
     */
    private Long pkid;

    /**
     * 类别名称
     */
    private String name;

    /**
     * 类别值
     */
    private String value;

    /**
     * 类别描述
     */
    private String description;

    /**
     * 父类别，如果没有则为0
     */
    private Long parentId;

    /**
     * 分类集合所属类型
     */
    private String outType;

    /**
     * 分类集合所属id
     */
    private String outId;

    private Date created;
    private Date updated;

    /**
     * 逻辑删除
     */
    private Integer isDelete;

    @Id
    @GeneratedValue
    public Long getPkid() {
        return pkid;
    }

    public void setPkid(Long pkid) {
        this.pkid = pkid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name="parent_id")
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Column(name="out_type")
    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    @Column(name="out_id")
    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Column(name="is_delete")
    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer delete) {
        isDelete = delete;
    }
}
