package com.yunat.ccms.metadata;

/**
 * 可以解析，跟界面交互 前端接口层对象的目的是简化前端交互，规范化前端交互和前后端解耦
 * M: 元数据对象; F: 接口对象
 * @author kevin.jiang 2013-3-13
 */
public interface Faceable<M, F> {

	/**
	 * 把接口对象解析为元数据模型
	 *
	 * @param json
	 * @return
	 */
	M parseFace(F face);

	/**
	 * 生成元数据自身对应的接口对象
	 *
	 * @param metamodel
	 */
	F genFace();

}
