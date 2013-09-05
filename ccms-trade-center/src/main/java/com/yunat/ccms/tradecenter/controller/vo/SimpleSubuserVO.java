package com.yunat.ccms.tradecenter.controller.vo;

/**
 * User: weilin.li
 * Date: 13-8-15
 * Time: 上午10:55
 */
public class SimpleSubuserVO {

    private String fullName;
    private Long isOnline;
    private String nick;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getOnline() {
        return isOnline;
    }

    public void setOnline(Long online) {
        isOnline = online;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
