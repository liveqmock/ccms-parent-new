package com.yunat.ccms.tradecenter.urpay.enums;


/**
 * 自动催付类型枚举
 *
 * @author shaohui.li
 * @version $Id: UrpayTypeEnum.java, v 0.1 2013-5-31 上午10:30:18 shaohui.li Exp $
 */
public enum AutoUrpayTypeEnum {

    /** 实时崔付 **/
    REALTIME_URPAY(1),

    /** 整点崔付 **/
    WHILE_POINT_URPAY(2),

    ;

    /** 类型值**/
    private int typeValue;

    public int getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(int typeValue) {
        this.typeValue = typeValue;
    }

    private AutoUrpayTypeEnum(int typeValue) {
        this.typeValue = typeValue;
    }

    public static AutoUrpayTypeEnum getTypeByValue(int jobValue){
        for(AutoUrpayTypeEnum e :AutoUrpayTypeEnum.values()){
            if(e.getTypeValue() == jobValue){
                return e;
            }
        }
        return null;
    }
}