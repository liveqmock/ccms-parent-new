package com.yunat.ccms.tradecenter.controller.vo;

/**
 * User: weilin.li
 * Date: 13-7-29
 * Time: 下午5:29
 */
public class AffairsVO {
    private long affairsId;
    private String title;
    private String founder;
    private String expirationTime;
    private long handleSize;
    private int important;
    private int status;

    public long getAffairsId() {
        return affairsId;
    }

    public void setAffairsId(long affairsId) {
        this.affairsId = affairsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public long getHandleSize() {
        return handleSize;
    }

    public void setHandleSize(long handleSize) {
        this.handleSize = handleSize;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
