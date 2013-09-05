package com.yunat.ccms.tradecenter.support.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author 李卫林
 *
 */
public class ListUtil {
	@SuppressWarnings("unchecked")
	public static <T, K> Map<K, List<T>> togetherMapByProperty(List<T> objs, String name) {
		String methodName = getMethodNameForGet(name);

		Map<K, List<T>> sameMap = new HashMap<K, List<T>>();

		try {
			for (T obj : objs) {
				K filed = (K) obj.getClass().getDeclaredMethod(methodName, null).invoke(obj, null);

				if (filed != null) {
					if (sameMap.containsKey(filed)) {
						sameMap.get(filed).add(obj);
					} else {
						List<T> rets = new LinkedList<T>();
						rets.add(obj);
						sameMap.put(filed, rets);
					}
				}
			}
		} catch (IllegalArgumentException e) {

		} catch (SecurityException e) {

		} catch (IllegalAccessException e) {

		} catch (InvocationTargetException e) {

		} catch (NoSuchMethodException e) {
		}

		return sameMap;
	}

	/**
	 * 查找一个指定属性值得对象
	 * @param <T>
	 * @param <K>
	 * @param objs
	 * @param name
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, K> T getObjectFromList(List<T> objs, String name, K value) {
		String start = name.substring(0, 1);
		String methodName = "get" + start.toUpperCase() + name.substring(1);

		T ret = null;
		try {
			for (T obj : objs) {
				K filed = (K) obj.getClass()
						.getDeclaredMethod(methodName, null).invoke(obj, null);

				if (filed.equals(value)) {
					ret = obj;
					break;
				}
			}
		} catch (IllegalArgumentException e) {

		} catch (SecurityException e) {

		} catch (IllegalAccessException e) {

		} catch (InvocationTargetException e) {

		} catch (NoSuchMethodException e) {
		}

		return ret;
	}

	public static <T, K> Map<K, T> toMapByProperty(Collection<T> objs, String name) {
		String methodName = getMethodNameForGet(name);

		Map<K, T> sameMap = new HashMap<K, T>();

		try {
			for (T obj : objs) {
				K filed = (K) obj.getClass().getDeclaredMethod(methodName, null).invoke(obj, null);

				if (filed != null) {
					sameMap.put(filed, obj);
				}
			}
		} catch (IllegalArgumentException e) {

		} catch (SecurityException e) {

		} catch (IllegalAccessException e) {

		} catch (InvocationTargetException e) {

		} catch (NoSuchMethodException e) {
		}

		return sameMap;
	}

	/**
	 * 获得集合中指定属性的集合
	 * 如果为null不加入返回集合
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public static <T, K> List<T> getPropertiesFromList(List<K> objs, String name) {
		String start = name.substring(0, 1);
		String methodName = "get" + start.toUpperCase() + name.substring(1);

		List<T> list = new LinkedList<T>();
		try {
			for (Object obj : objs) {

				T filed = (T) obj.getClass()
						.getDeclaredMethod(methodName, null).invoke(obj, null);

				if (filed != null) {
					list.add(filed);
				}
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return list;
		}
	}

	/**
	 * 获得集合中指定属性的集合
	 * 如果为null不加入返回集合
	 * @return set
	 */
	@SuppressWarnings("unchecked")
	public static <T, K> Set<T> getPropertySetFromList(List<K> objs, String name) {
		String start = name.substring(0, 1);
		String methodName = "get" + start.toUpperCase() + name.substring(1);

		Set<T> set = new HashSet<T>();
		try {
			for (Object obj : objs) {

				T filed = (T) obj.getClass()
						.getDeclaredMethod(methodName, null).invoke(obj, null);

				if (filed != null) {
					set.add(filed);
				}
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return set;
		}
	}

	public static <T> List<List<T>> togetherListByPrperties(List<T> objs, String... propertiesNames) {

		Map<String, List<T>> sameMap = new HashMap<String, List<T>>();
		List<List<T>> sameList = new LinkedList<List<T>>();

		try {
			for (T obj : objs) {
				String key = "";
				for (String propertiesName : propertiesNames) {
					String filed = obj.getClass().getDeclaredMethod(getMethodNameForGet(propertiesName), null).invoke(obj, null).toString();
					if (filed != null) {
						key += filed;
					}
				}

				if (!"".equals(key)) {
					if (sameMap.containsKey(key)) {
						sameMap.get(key).add(obj);
					} else {
						List<T> rets = new LinkedList<T>();
						rets.add(obj);
						sameMap.put(key, rets);

						sameList.add(rets);
					}
				}
			}
		} catch (IllegalArgumentException e) {

		} catch (SecurityException e) {

		} catch (IllegalAccessException e) {

		} catch (InvocationTargetException e) {

		} catch (NoSuchMethodException e) {
		}

		return sameList;
	}

    /**
     * 查找指定属性值的集合
     * @param <T>
     * @param <K>
     * @param objs
     * @param name
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, K> List<T> getObjectsFromList(List<T> objs, String name, K value) {
        String start = name.substring(0, 1);
        String methodName = "get" + start.toUpperCase() + name.substring(1);

        List<T> rets = new LinkedList<T>();
        try {
            for (T obj : objs) {
                K filed = (K) obj.getClass()
                        .getDeclaredMethod(methodName, null).invoke(obj, null);

                if (filed.equals(value)) {
                    rets.add(obj);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        } catch (SecurityException e) {
            System.out.println(e);
        } catch (IllegalAccessException e) {
            System.out.println(e);
        } catch (InvocationTargetException e) {
            System.out.println(e);
        } catch (NoSuchMethodException e) {
            System.out.println(e);
        }

        return rets;
    }

	private static String getMethodNameForGet(String property) {
		String start = property.substring(0, 1);
		String methodName = "get" + start.toUpperCase() + property.substring(1);

		return methodName;
	}
}
