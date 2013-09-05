package com.yunat.ccms.metadata;

/**
 * 可以持久化，跟数据库交互
 * M: 元数据对象; P: 持久层实体对象
 * @author kevin.jiang 2013-3-13
 */
public interface Persistable<M, P> {

	/**
	 * 从持久化对象中加载元数据配置
	 *
	 * @param s
	 * @return
	 */
	M loadFromPojo(P pojo);

	/**
	 * 转换到持久化对象中，准备保存到数据库
	 *
	 * @param metamodel
	 */
	P genPojo();
}
