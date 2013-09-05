package com.yunat.ccms.core.support.utils;

import java.util.List;

public class JsUtils {

	public static String getDataJson(int total, int page, List<?> rows){
		StringBuilder result = new StringBuilder();
		result.append("{");
		result.append("\"total\":"+total+",");
		result.append("\"page\":"+page+",");
		result.append("\"rows\":[");

		int size = ((rows == null) ? 0 : rows.size());
		if (size>0){
			for (Object row:rows){
				String cell = row.toString().replaceAll("[\n*\r*]+", "<br/>");
				result.append("{\"cell\":"+cell+"},");
				System.out.println(cell);
			}
			int index = result.length();
			result.delete(index-1, index);
		}
		result.append("]}");
		return result.toString();
	}

}
