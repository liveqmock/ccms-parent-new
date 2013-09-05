package com.yunat.ccms.dashboard.controller.helper;

import java.util.List;

import org.springframework.stereotype.Component;

import net.sf.json.JSONArray;

@Component
public class JsonFormatWrap {
	
    /**
	 * jsonp  回调处理帮助方法
	 */
	
	public String  jsonpDataFormatWrap(String callbackName, List<?> jsonData){
	     return  callbackName == null?   JSONArray.fromObject(jsonData).toString() :
	    	 callbackName + "(" + JSONArray.fromObject(jsonData).toString() + ")";
	}

}
