package com.yunat.ccms.tradecenter.support.util;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-11
 * Time: 上午11:19
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {

    public static Integer getInt(String str) {
    	if (str == null) {
    		return null;
    	}

        return Integer.parseInt(str);
    }

    public static double getDouble(String str) {
        return Double.parseDouble(str);
    }
}
