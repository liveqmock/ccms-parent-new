/**
 *
 */
package com.yunat.ccms.tradecenter.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.repository.CareConfigRepository;
import com.yunat.ccms.tradecenter.repository.IOrderRepository;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.util.WebUtils;

import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.mq.LogisticsMQListener;
import com.yunat.ccms.tradecenter.mq.OrderMQListener;
import com.yunat.ccms.tradecenter.repository.OrderRepository;
import com.yunat.ccms.tradecenter.repository.TransitstepinfoRepository;
import com.yunat.ccms.tradecenter.service.BuyerStatisticService;
import com.yunat.ccms.tradecenter.service.CareService;
import com.yunat.ccms.tradecenter.service.PropertiesConfigManager;

/**
 * 催付统计展示
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-5-30 下午04:48:26
 */

@Controller
public final class TestController extends WebApplicationObjectSupport {

	private static Logger logger = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private BuyerStatisticService buyerStatisticService;

	@Autowired
	private CareService careService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private TransitstepinfoRepository transitstepinfoRepository;

	@Autowired
	private LogisticsMQListener logisticsMQListener;

	@Autowired
	private OrderMQListener orderMQListener;

	@Autowired
	private PropertiesConfigManager propertiesConfigManager;

    @Autowired
    private IOrderRepository iOrderRepository;

    @Autowired
    private CareConfigRepository careConfigRepository;

	/**
	 * 编写通用测试方法、传入bean，name和参数
	 */
	@ResponseBody
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ControlerResult test(HttpServletRequest request) {
		Object object = null;

		String beanName = WebUtils.findParameterValue(request, "bean");
		String methodName = WebUtils.findParameterValue(request, "method");

		ApplicationContext context = getApplicationContext();
		Object bean = context.getBean(beanName);
		Class clazz = bean.getClass();

		//如果为代理类，解析出代理对象
		String className = clazz.getName();
		if (bean.getClass().getName().contains("$Proxy")) {
			className = bean.toString();

			int index = className.indexOf("@");
			className = className.substring(0, index);
		}



		try {
			ClassPool pool = ClassPool.getDefault();
			ClassClassPath classPath = new ClassClassPath(this.getClass());
			pool.insertClassPath(classPath);
			CtClass cc = pool.getCtClass(className);

			CtMethod[] ctMethods = cc.getDeclaredMethods();

			for (CtMethod cm : ctMethods) {
				if (cm.getName().equals(methodName)) {
					MethodInfo methodInfo = cm.getMethodInfo();

					CodeAttribute codeAttribute = methodInfo.getCodeAttribute();

					LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
							.getAttribute(LocalVariableAttribute.tag);

					CtClass[] parameterTypes = cm.getParameterTypes();
					String[] paramNames = new String[parameterTypes.length];

					int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
					// pos = 0;
					for (int i = 0; i < paramNames.length; i++) {
						paramNames[i] = attr.variableName(i + pos);
					}

					// Method meth = clazz.getMethod(methodName,
					// parameterTypes.);
					// paramNames即参数名
					Object[] params = new Object[paramNames.length];
					for (int i = 0; i < paramNames.length; i++) {
						String paramName = paramNames[i];
						String type = parameterTypes[i].getName();
						// ms.setAccessible(true);
						if (type.equals("java.lang.String")) {
							params[i] = WebUtils.findParameterValue(request, paramName);
						} else if (type.equals("int") || type.equals("java.lang.Integer")) {
							params[i] = Integer.parseInt(WebUtils.findParameterValue(request, paramName));
						} else if (type.equals("long") || type.equals("java.lang.Long")) {
							params[i] = Long.parseLong(WebUtils.findParameterValue(request, paramName));
						} else if (type.equals("boolean") || type.equals("java.lang.Boolean")) {
							params[i] = Boolean.parseBoolean(WebUtils.findParameterValue(request, paramName));
						} else if (type.equals("java.util.Date")) {
							Date date = new Date();
							if (date != null) {
									params[i] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(WebUtils
											.findParameterValue(request, paramName));

							}

						} else if (parameterTypes[i].isArray()) {

							String[] strArray = request.getParameterValues(paramName);

							if (type.equals("java.lang.Integer[]")) {
								Integer[] intArray = new Integer[strArray.length];
								for (int j = 0; j < strArray.length; j++) {
									intArray[j] = Integer.parseInt(strArray[j]);
								}

								params[i] = intArray;
							} else if (type.equals("int[]")) {
								int[] intArray = new int[strArray.length];
								for (int j = 0; j < strArray.length; j++) {
									intArray[j] = Integer.parseInt(strArray[j]);
								}

								params[i] = intArray;
							} else if (type.equals("java.lang.Long[]")) {
								Long[] longArray = new Long[strArray.length];
								for (int j = 0; j < strArray.length; j++) {
									longArray[j] = Long.parseLong(strArray[j]);
								}

								params[i] = longArray;

							} else if (type.equals("long[]")) {
								long[] longArray = new long[strArray.length];
								for (int j = 0; j < strArray.length; j++) {
									longArray[j] = Long.parseLong(strArray[j]);
								}

								params[i] = longArray;

							} else if (type.equals("java.lang.Double[]")) {
								Double[] doubleArray = new Double[strArray.length];
								for (int j = 0; j < strArray.length; j++) {
									doubleArray[j] = Double.parseDouble(strArray[j]);
								}

								params[i] = doubleArray;
							} else if (type.equals("double[]")) {
								double[] doubleArray = new double[strArray.length];
								for (int j = 0; j < strArray.length; j++) {
									doubleArray[j] = Double.parseDouble(strArray[j]);
								}

								params[i] = doubleArray;
							} else if (type.equals("java.lang.Float[]")) {
								Float[] floatArray = new Float[strArray.length];
								for (int j = 0; j < strArray.length; j++) {
									floatArray[j] = Float.parseFloat(strArray[j]);
								}

								params[i] = floatArray;
							}  else if (type.equals("float[]")) {
								float[] floatArray = new float[strArray.length];
								for (int j = 0; j < strArray.length; j++) {
									floatArray[j] = Float.parseFloat(strArray[j]);
								}

								params[i] = floatArray;
							} else if (type.equals("boolean[]")) {
								boolean[] booleanArray = new boolean[strArray.length];
								for (int j = 0; j < strArray.length; j++) {
									booleanArray[j] = Boolean.parseBoolean(strArray[j]);
								}

								params[i] = booleanArray;
							}  else if (type.equals("java.lang.Boolean[]")) {
								Boolean[] booleanArray = new Boolean[strArray.length];
								for (int j = 0; j < strArray.length; j++) {
									booleanArray[j] = Boolean.parseBoolean(strArray[j]);
								}

								params[i] = booleanArray;
							} else if (type.equals("java.lang.Character[]")) {
								Character[] charArray = new Character[strArray.length];
								for (int j = 0; j < strArray.length; j++) {
									charArray[j] = strArray[j].charAt(0);
								}
								params[i] = charArray;
							} else if (type.equals("char[]")) {
								char[] charArray = new char[strArray.length];
								for (int j = 0; j < strArray.length; j++) {
									charArray[j] = strArray[j].charAt(0);
								}
								params[i] = charArray;
							}  else if (type.equals("java.lang.Short[]")) {
								Short[] shortArray = new Short[strArray.length];
								for (int j = 0; j < strArray.length; j++) {
									shortArray[j] = Short.parseShort(strArray[j]);
								}

								params[i] = shortArray;
							}  else if (type.equals("short[]")) {
								short[] shortArray = new short[strArray.length];
								for (int j = 0; j < strArray.length; j++) {
									shortArray[j] = Short.parseShort(strArray[j]);
								}

								params[i] = shortArray;
							} else {
								params[i] = strArray;
							}
						}
						//非基本类型
						else {
							Class cla = Class.forName(parameterTypes[i].getName());
							Object bean1 = cla.newInstance();
							setFormBean(request, bean1);
							params[i] = bean1;
						}

					}

					Method[] methods = clazz.getDeclaredMethods();
					for (Method method : methods) {
						if (method.getName().equals(methodName)) {
							object = method.invoke(bean, params);
						}
					}
				}
			}

		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ControlerResult.newSuccess(object);
	}

	public static void setFormBean(HttpServletRequest request, Object bean) {
		Class c = bean.getClass();
		Method[] mss = c.getMethods();
		for (int i = 0; i < mss.length; i++) {
			Method ms = mss[i];
			String name = ms.getName();
			if (name.startsWith("set")) {
				Class[] cc = ms.getParameterTypes();
				if (cc.length == 1) {
					String type = cc[0].getName(); // parameter type
					try {
						// get property name:
						String prop = Character.toLowerCase(name.charAt(3)) + name.substring(4);
						// get parameter value:
						String param = request.getParameter(prop);
						if (param != null && !param.equals("")) {
							// ms.setAccessible(true);
							if (type.equals("java.lang.String")) {
								ms.invoke(bean, new Object[] { param });
							} else if (type.equals("int") || type.equals("java.lang.Integer")) {
								ms.invoke(bean, new Object[] { new Integer(param) });
							} else if (type.equals("long") || type.equals("java.lang.Long")) {
								ms.invoke(bean, new Object[] { new Long(param) });
							} else if (type.equals("boolean") || type.equals("java.lang.Boolean")) {
								ms.invoke(bean, new Object[] { Boolean.valueOf(param) });
							} else if (type.equals("java.util.Date")) {
								Date date = new Date();
								if (date != null)
									ms.invoke(bean, new Object[] { date });
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

    /**
     * 编写通用测试方法、传入bean，name和参数
     */
    @ResponseBody
    @RequestMapping(value = "/testLogisticsOpenTask", method = RequestMethod.GET)
    public ControlerResult testLogisticsOpenTask(HttpServletRequest request) {
        //获取店铺配置
        Map<String, Integer> shopToLastConfigMap = new HashMap<String, Integer>();
        List<CareConfigDomain> careConfigDomains = careConfigRepository.getOpenedCareConfigs(Arrays.asList(UserInteractionType.SHIPMENT_CARE.getType(), UserInteractionType.ARRIVED_CARE.getType(), UserInteractionType.DELIVERY_CARE.getType(), UserInteractionType.SIGNED_CARE.getType()));

        if (careConfigDomains.size() == 0) {
            return null;
        }

        for (CareConfigDomain careConfigDomain : careConfigDomains) {
            Integer currCareType =  shopToLastConfigMap.get(careConfigDomain.getDpId());

            Integer thisCareType = careConfigDomain.getCareType();

            if (currCareType == null || thisCareType > currCareType) {
                shopToLastConfigMap.put(careConfigDomain.getDpId(), thisCareType);
            }
        }

        List<OrderDomain> orderDomains = iOrderRepository.findLogisticsCareOpenOrders(shopToLastConfigMap);




        return null;
    }

}
