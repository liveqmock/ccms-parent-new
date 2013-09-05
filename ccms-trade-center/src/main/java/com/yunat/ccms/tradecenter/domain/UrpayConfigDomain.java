package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yunat.ccms.core.support.json.JsonDateDeserializeParrten;
import com.yunat.ccms.core.support.json.JsonDateSerializerParrten;

/**
 *催付配置表
 *
 * @author shaohui.li
 * @version $Id: UrpayConfigDomain.java, v 0.1 2013-5-30 上午11:20:46 shaohui.li Exp $
 */

@Entity
@Table(name = "tb_tc_urpay_config")
public class UrpayConfigDomain extends BaseConfigDomain{

    /**  */
    private static final long serialVersionUID = 3179227829256337409L;

    /**催付类型(1:自动催付、2:预关闭催付、3:聚划算催付) **/
    @Column(name = "urpay_type")
    private Integer urpayType;

    /**1：实时催付，2：定时催付**/
    @Column(name = "task_type")
    private Integer taskType;

    /**催付时间开始时间**/
    @JsonSerialize(using = JsonDateSerializerParrten.class)
    @JsonDeserialize(using = JsonDateDeserializeParrten.class)
    @Column(name = "urpay_start_time")
    private Date urpayStartTime;

    /**催付时间结束时间 **/
    @JsonSerialize(using = JsonDateSerializerParrten.class)
    @JsonDeserialize(using = JsonDateDeserializeParrten.class)
    @Column(name = "urpay_end_time")
    private Date urpayEndTime;

    /**定时催付时间 **/
    @Column(name = "fix_urpay_time")
    private String fixUrpayTime;

    /** 催付时间间隔 **/
    @Column(name = "offset")
    private Integer offset;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getUrpayType() {
        return urpayType;
    }

    public void setUrpayType(Integer urpayType) {
        this.urpayType = urpayType;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Date getUrpayStartTime() {
        return urpayStartTime;
    }

    public void setUrpayStartTime(Date urpayStartTime) {
        this.urpayStartTime = urpayStartTime;
    }

    public Date getUrpayEndTime() {
        return urpayEndTime;
    }

    public void setUrpayEndTime(Date urpayEndTime) {
        this.urpayEndTime = urpayEndTime;
    }

    public String getFixUrpayTime() {
        return fixUrpayTime;
    }

    public void setFixUrpayTime(String fixUrpayTime) {
        this.fixUrpayTime = fixUrpayTime;
    }

}
