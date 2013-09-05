/**
 *
 */
package com.yunat.ccms.tradecenter.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-9 上午10:07:52
 */
public class BeanUtil {
	private static Logger log = Logger.getLogger(BeanUtil.class); // 日志
	/**
	 * 转换时对map中的key里的字符串会做忽略处理的正则串
	 */
	private static final String OMIT_REG = "";
	/**
	 * 将map集合转换成Bean集合，Bean的属性名与map的key值对应时不区分大小写，并对map中key做忽略OMIT_REG正则处理
	 * @param <E>
	 * @param cla
	 * @param mapList
	 * @return
	 */
	public static <E> List<E> toBeanList(Class<E> cla,List<Map<String, Object>> mapList) {
		List<E> list = new ArrayList<E>(mapList.size());
		for (Map<String, Object> map : mapList) {
			E obj = toBean(cla, map);
			list.add(obj);
		}
		return list;
	}

	/**
	 * 将map转换成Bean，Bean的属性名与map的key值对应时不区分大小写，并对map中key做忽略OMIT_REG正则处理
	 * @param <E>
	 * @param cla
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public static <E> E toBean(Class<E> cla, Map<String, Object> map) {
		// 创建对象
		E obj = null;
		try {
			obj = cla.newInstance();
			if (obj == null) {
				throw new Exception();
			}
		} catch (Exception e) {
			log.error("类型实例化对象失败,类型:" + cla);
			return null;
		}
		// 处理map的key
		Map<String, Object> newmap = new HashMap<String, Object>();
		for (Map.Entry<String, Object> en : map.entrySet()) {
			newmap.put(
			"set_"
			+ en.getKey().trim().replaceAll(OMIT_REG, "")
			.toLowerCase(), en.getValue());
		}
		// 进行值装入
		Method[] ms = cla.getMethods();
		for (Method method : ms) {
			String mname  = propertyToField(method.getName());
			//String mname = method.getName().toLowerCase();
			if (mname.startsWith("set")) {
				Class[] clas = method.getParameterTypes();
				Object v = newmap.get(mname);
				if (v != null && clas.length == 1) {
					if(clas[0]==Long.class&&v.getClass()==Integer.class){
						v = new Long(v.toString());
					}
					if(clas[0]==Double.class&&v.getClass()==BigDecimal.class){
						v = new Double(v.toString());
					}
					try {
						method.invoke(obj, v);
					} catch (Exception e) {
						log.error("属性值装入失败,装入方法：" + cla + "."
						+ method.getName() + ".可装入类型" + clas[0]
						+ ";欲装入值的类型:" + v.getClass());
					}
				}
			}
		}
		return obj;
	}



	public static Map<String, Object> beanToMap(Object obj) {
		Map<String, Object> params = new HashMap<String, Object>(0);
		try {
			PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
			PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
			for (int i = 0; i < descriptors.length; i++) {
				String name = descriptors[i].getName();
				if (!StringUtils.equals(name, "class")) {
					params.put(propertyToField(name),propertyUtilsBean.getNestedProperty(obj, name));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}

	/* 对象属性转换为字段  例如：userName to user_name
	 * @param property 字段名
	 * @return
	 */
	public static String propertyToField(String property) {
	    if (null == property) {
	        return "";
	    }
	    char[] chars = property.toCharArray();
	    StringBuffer sb = new StringBuffer();
	    for (char c : chars) {
	        if (CharUtils.isAsciiAlphaUpper(c)) {
	            sb.append("_" + StringUtils.lowerCase(CharUtils.toString(c)));
	        } else {
	            sb.append(c);
	        }
	    }
	    return sb.toString();
	}





	public static void main(String[] args) {
		/*OrderDomain order  = new OrderDomain();
		order.setConsignTime(new Date());
		order.setCustomerno("林肯郡");
		order.setDpId("112233");
		order.setNum(12);
		order.setPayment(12.12);
		order.setPayTime(new Date());
		Map<String, Object> map2 =  BeanUtil.beanToMap(order);
		System.out.println(map2.get("consign_time")+" "+map2.get("customerno")+" "+map2.get("dp_id")+" "+map2.get("num")+" "+map2.get("payment")+" "+map2.get("pay_time")+" "+map2.get("status")+" "+map2.get("subtask_id"));
*/
		System.out.println(BeanUtil.propertyToField("getDpId"));




	}
}
