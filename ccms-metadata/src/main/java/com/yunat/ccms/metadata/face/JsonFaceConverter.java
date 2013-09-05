package com.yunat.ccms.metadata.face;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 元数据Json接口支持类：前端json数据结构和Face结构互相转化
 * 
 * Face接口专门用来表达Json数据结构
 * 
 * @author kevin.jiang 2013-3-2
 */
public class JsonFaceConverter {

	private static Logger logger = Logger.getLogger(JsonFaceConverter.class);

	/**
	 * 把Face对象转化为Json
	 * 
	 * @param query
	 * @return
	 */
	public static String toJson(Object obj) {

		return JSONSerializer.toJSON(obj).toString();
	}

	/**
	 * 把Json转换为FaceQuery
	 * 
	 * @param json
	 * @return
	 */
	public static FaceNode JsonToFaceNode(String json) {

		FaceNode node = null;
		try {
			JSONObject aJson = JSONObject.fromObject(json);
			node = (FaceNode) convertMapToObject(FaceNode.class, aJson);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("caused by:", e);
		}
		return node;
	}

	/**
	 * 把Json转换为FaceQuery
	 * 
	 * @param json
	 * @return
	 */
	public static FaceQuery JsonToFaceQuery(String json) {

		FaceQuery query = null;
		try {
			JSONObject aJson = JSONObject.fromObject(json);
			query = (FaceQuery) convertMapToObject(FaceQuery.class, aJson);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("caused by:", e);
		}
		return query;
	}

	/**
	 * 把Json转换为FaceAttribute
	 * 
	 * @param json
	 * @return
	 */
	public static List<FaceAttribute> JsonToFaceAttribute(String json) {

		List<FaceAttribute> attrList = new ArrayList<FaceAttribute>();
		try {
			JSONArray jsons = JSONArray.fromObject(json);
			for (int i = 0; i < jsons.size(); i++) {
				JSONObject aJson = jsons.getJSONObject(i);
				FaceAttribute attr = (FaceAttribute) convertMapToObject(FaceAttribute.class, aJson);
				attrList.add(attr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("caused by:", e);
		}
		return attrList;
	}

	/**
	 * 把Json转换为FaceCondition
	 * 
	 * @param json
	 * @return
	 */
	public static List<FaceCriteria> JsonToFaceCriteria(String json) {

		List<FaceCriteria> attrList = new ArrayList<FaceCriteria>();
		try {
			JSONArray jsons = JSONArray.fromObject(json);
			for (int i = 0; i < jsons.size(); i++) {
				JSONObject aJson = jsons.getJSONObject(i);
				FaceCriteria attr = (FaceCriteria) convertMapToObject(FaceCriteria.class, aJson);
				attrList.add(attr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("caused by:", e);
		}
		return attrList;
	}

	/**
	 * 因为有嵌套关系，所以要递归处理
	 * 
	 * @param type
	 * @param map
	 * @return
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Object convertMapToObject(Class type, Map map) throws IntrospectionException,
			IllegalAccessException, InstantiationException, InvocationTargetException {

		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		Object obj = type.newInstance();
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();

			if (map.containsKey(propertyName)) {

				logger.debug("--->" + propertyName + " : " + map.get(propertyName));

				if ("queries".equals(propertyName)) {

					List<Map> list = (List<Map>) map.get(propertyName);
					List<FaceQuery> querylist = new ArrayList<FaceQuery>();
					for (Map aMap : list) {
						FaceQuery query = (FaceQuery) convertMapToObject(FaceQuery.class, aMap);
						querylist.add(query);
					}
					descriptor.getWriteMethod().invoke(obj, querylist);

				} else if ("cons".equals(propertyName)) {

					Map<Object, Map> aMap = (Map<Object, Map>) map.get(propertyName);
					Map<String, FaceCriteria> conMap = new HashMap<String, FaceCriteria>();
					for (Map.Entry<Object, Map> entry : aMap.entrySet()) {
						FaceCriteria condition = (FaceCriteria) convertMapToObject(FaceCriteria.class, entry.getValue());
						conMap.put((String) entry.getKey(), condition);
					}
					descriptor.getWriteMethod().invoke(obj, conMap);

				} else if ("attrs".equals(propertyName)) {

					List<Map> list = (List<Map>) map.get(propertyName);
					List<FaceAttribute> attrlist = new ArrayList<FaceAttribute>();
					for (Map aMap : list) {
						FaceAttribute attr = (FaceAttribute) convertMapToObject(FaceAttribute.class, aMap);
						attrlist.add(attr);
					}
					descriptor.getWriteMethod().invoke(obj, attrlist);
				} else if ("values".equals(propertyName)) {

					if (isNull(map.get(propertyName).toString())) {

						continue;
					}

					if (JSONUtils.isString(map.get(propertyName)) || JSONUtils.isNumber(map.get(propertyName))) {

						Object[] args = new Object[] { map.get(propertyName) };
						descriptor.getWriteMethod().invoke(obj, args);

					} else {

						JSON value = (JSON) map.get(propertyName);
						if (!value.isEmpty() && !value.isArray()) {
							Map aMap = (Map) map.get(propertyName);
							String aValue = (String) aMap.get("value");
							if (isNull(aValue)) {
								continue;
							}
						}
						descriptor.getWriteMethod().invoke(obj, value.toString());
					}

				} else if ("op".equals(propertyName)) {

					Map opMap = (Map) map.get(propertyName);
					FaceOperator faceOp = (FaceOperator) convertMapToObject(FaceOperator.class, opMap);
					descriptor.getWriteMethod().invoke(obj, faceOp);
				} else {

					if (JSONUtils.isNull(map.get(propertyName))) {

						continue;
					}

					if (isNull(map.get(propertyName).toString())) {

						continue;
					}
					Object[] args = new Object[] { map.get(propertyName) };
					descriptor.getWriteMethod().invoke(obj, args);
				}
			}
		}
		return obj;
	}

	private static Boolean isNull(String value) {

		// 普通空值校验
		if (StringUtils.isEmpty(value)) {

			return true;
		}

		// 前端网页错误传值情形：null值，这些情况都是要表达空值
		if ("null".equals(value) || "\"null\"".equals(value) || "'null'".equals(value)) {

			return true;
		}

		// 前端网页错误传值情形：undefined值，这些情况都是要表达空值
		if ("\"undefined\"".equals(value) || "'undefined'".equals(value)) {

			return true;
		}

		// 这两种情况虽然某些情形下有意义，但是在查询节点不会有意义
		if ("\"\"".equals(value) || "''".equals(value)) {

			return true;
		}

		return false;
	}
}
