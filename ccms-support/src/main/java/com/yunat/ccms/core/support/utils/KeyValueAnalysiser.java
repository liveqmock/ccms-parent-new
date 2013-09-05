package com.yunat.ccms.core.support.utils;

import java.util.HashMap;
import java.util.Map;

public class KeyValueAnalysiser {

	public static Map<String, String> toMap(String src, String delimiter,
			String equalMark) {
		Map<String, String> result = new HashMap<String, String>();
		String[] keyValues = src.split(delimiter);
		for (String kv : keyValues) {
			String[] keyValue = kv.split(equalMark, 2);
			if (keyValue.length == 2) {
				result.put(keyValue[0], keyValue[1]);
			}
		}
		return result;
	}

	public static Map<String, String> toMap(String src) {
		return toMap(src, "[|][,][|]", "[|][=][|]");
	}

}
