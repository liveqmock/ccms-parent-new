package com.yunat.ccms.module;

import java.util.Collection;

import com.yunat.ccms.core.support.auth.SupportOp;

/**
 * 模块实例.
 * 关于名字:{@link #getName()}方法理论上返回的是英文名字,前端展示时应依靠本地化进行处理.
 * 
 * @author MaGiCalL
 */
public interface Module {

	Long getId();

	/**
	 * 获取作为key的module的名字.
	 * 其实就是一个字符串型的id.通常是英文名.
	 * 
	 * @return
	 */
	String getKeyName();

	/**
	 * 获取"模块类型"的id
	 * 
	 * @return
	 */
	Long getModuleTypeId();

	/**
	 * 获取"包含模块(外层模块)"的id
	 * 
	 * @return
	 */

	Long getContainerModuleId();

	/**
	 * 获取"模块类型"
	 * 
	 * @return
	 */
	ModuleType getModuleType();

	/**
	 * 获取"包含模块(外层模块)"
	 * 
	 * @return
	 */
	Module getContainerModule();

	/**
	 * 获取模块的名字(短名),可以用于展示.
	 * 如果未指定,则从ModuleType中继承.由于不同Module实例可继承于同一个ModuleType,因此并非唯一.
	 * 注意!!!理论上返回的是英文名字,前端展示时应依靠本地化进行处理.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 提示文案。可用于展示时作为html标签的title/alt属性、鼠标hover显示。
	 * 如果未指定，则应继承ModuleType的。
	 */
	String getTip();

	/**
	 * 备注
	 * 
	 * @return
	 */
	String getMemo();

	/**
	 * 本模块实例内包含的模块实例
	 * 
	 * @return
	 */
	Collection<Module> getContainingModules();

	/**
	 * 支持的操作
	 * 如果未指定，则应继承ModuleType的
	 * 
	 * @return
	 */
	Integer getSupportOpsMask();

	/**
	 * 所支持的操作
	 * 
	 * @return
	 */
	Collection<SupportOp> getSupportOps();

	/**
	 * 要求的最低版本
	 * 
	 * @return
	 */
	int getLowestEditionRequired();

	/**
	 * 模块的层级。是包含模块的层级+1，如果没有包含模块则为0。
	 * 
	 * @return
	 */
	int getLayer();

	/**
	 * 点击模块时跳转的页面
	 * 
	 * @return
	 */
	String getUrl();

	/**
	 * 请求数据的url
	 * 
	 * @return
	 */
	String getDataUrl();
}
