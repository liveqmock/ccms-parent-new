package com.yunat.ccms.tradecenter.service;

import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.support.bean.NameValueBean;

public interface PropertiesConfigManager {
	/**
	 * 将属性解析为string数组
	 * 如保存值为"中通,申通,韵达"
	 * @param dpId 可以为null,为null表示直接通过属性值查找
	 * @param name 属性名
	 *
	 * @return
	 */
	String[] getStringArray(String dpId, String name);

	/**
	 * 将属性解析为int数组
	 * 如保存值为"23, 24, 25"
	 * @param dpId 可以为null,为null表示直接通过属性值查找
	 * @param name 属性名
	 *
	 * @return
	 */
	int[] geIntArray(String dpId, String name);

	/**
	 * 获取属性的string值
	 * @param dpId 可以为null,为null表示直接通过属性值查找
	 * @param name
	 * @return
	 */
	String getString(String dpId, String name);

	/**
	 * 获取属性的int值（要能转换为int类型才行）
	 * @param dpId 可以为null,为null表示直接通过属性值查找
	 * @param name
	 * @return
	 */
	Integer getInt(String dpId, String name);


	/**
	 * 保存
	 * @param dpId TODO
	 * @param name 属性名
	 * @param valueArray 属性值[1,2,3]
	 */
	void saveProperties(String dpId, String name, Object[] valueArray);

	/**
	 * 保存
	 * @param dpId TODO
	 * @param name 属性名
	 * @param value 属性值
	 */
	void saveProperties(String dpId, String name, Object value);

	/**
	 * 保存含组名的属性名
	 * @param dpId TODO
	 * @param name 属性名
	 * @param value 属性值
	 * @param groupName 组名
	 */
	void saveProperties(String dpId, String name, Object value, String groupName);


	/**
	 * 获取一组类型用于展示
	 * 主要用于前台展示
	 * @param dpId TODO
	 * @param groupName 组名
	 * @return
	 */
	List<NameValueBean> findNameValueByGroup(String dpId, String groupName);

    /**
     * 根据组名获取name-value键值对
     *
     *
     * @param dpId
     * @param groupName 组名
     * @return
     */
    Map<String, String> getNameValueMap(String dpId, String groupName);

    /**
     * 删除指定键值（名称必须唯一）
     * @param name
     */
    void deleteByName(String name);

    /**
     * 删除整组
     * @param groupName
     */
    void deleteByGroupName(String groupName);

    /**
     *
     * @param dpId
     * @param names
     * @param values
     * @param groupName
     */
    void batchReplace(String dpId, String[] names, String[] values, String groupName);
}
