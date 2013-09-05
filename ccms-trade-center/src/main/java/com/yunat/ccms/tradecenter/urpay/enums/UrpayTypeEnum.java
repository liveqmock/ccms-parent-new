package com.yunat.ccms.tradecenter.urpay.enums;


/**
 * 催付类型枚举
 *
 * @author shaohui.li
 * @version $Id: UrpayTypeEnum.java, v 0.1 2013-5-31 上午10:30:18 shaohui.li Exp $
 */
public enum UrpayTypeEnum {

    /** 自动崔付 **/
    AUTO_URPAY(1),

    /** 预关闭崔付 **/
    PRE_CLOSE_URPAY(2),

    /** 聚划算 催付**/
    CHEAP_URPAY(3),
    ;

    /** 类型值**/
    private int typeValue;

    public int getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(int typeValue) {
        this.typeValue = typeValue;
    }

    private UrpayTypeEnum(int typeValue) {
        this.typeValue = typeValue;
    }

    public static UrpayTypeEnum getTypeByValue(int jobValue){
        for(UrpayTypeEnum e :UrpayTypeEnum.values()){
            if(e.getTypeValue() == jobValue){
                return e;
            }
        }
        return null;
    }
}