package com.yunat.ccms.core.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("rawtypes")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Descriptor {

	String DEFAULT_VALUE = "";

	/**
	 * 节点类型
	 * 
	 * @return
	 */
	public String type();

	/**
	 * 是否统计日志，默认true
	 * 
	 * @return
	 */
	public boolean hasCountLog() default true;

	/**
	 * 是否克隆节点的配置信息，默认true
	 * 
	 * @return
	 */
	public boolean cloneable() default true;

	/**
	 * 执行中的节点是否可以编辑，默认false
	 * 
	 * @return
	 */
	public boolean editableWhileJobExecuting() default false;

	/**
	 * 节点验证，默认值为空
	 * 
	 * @return
	 */
	public Class validatorClass();

	/**
	 * 节点数据处理实现类
	 * 
	 * @return
	 */
	public Class handlerClass();

	/**
	 * 节点的流程处理类，默认值为空
	 * 
	 * @return
	 */
	public Class processorClass();

}
