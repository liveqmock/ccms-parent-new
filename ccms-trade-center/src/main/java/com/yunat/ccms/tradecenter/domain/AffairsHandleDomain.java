package com.yunat.ccms.tradecenter.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * User: weilin.li
 * Date: 13-7-29
 * Time: 上午11:20
 */
@Entity
@Table(name="tb_tc_affairs_handle")
public class AffairsHandleDomain extends BaseDomain{
    private Long pkid;

    /**
     * 事务id
     */
    private Long affairsId;

    /**
     * 备注
     */
    private String note;

    /**
     * 创建者
     */
    private String founder;

    /**
     * 下一个处理者
     */
    private String nextHandler;

    private Date created;
    private Date updated;

    @Id
    @GeneratedValue
    public Long getPkid() {
        return pkid;
    }

    public void setPkid(Long pkid) {
        this.pkid = pkid;
    }

    @Column(name="affairs_id")
    public Long getAffairsId() {
        return affairsId;
    }

    public void setAffairsId(Long affairsId) {
        this.affairsId = affairsId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    @Column(name="next_handler")
    public String getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(String nextHandler) {
        this.nextHandler = nextHandler;
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
}
