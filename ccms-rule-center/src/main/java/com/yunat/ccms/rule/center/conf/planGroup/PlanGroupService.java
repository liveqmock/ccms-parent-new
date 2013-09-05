package com.yunat.ccms.rule.center.conf.planGroup;

import java.util.List;

import com.yunat.ccms.rule.center.RuleCenterRuntimeException;

/**
 * 方案组
 * 
 * @author wenjian.liang
 */
public interface PlanGroupService {

	/**
	 * 保存方案组的"默认备注签名"。
	 * 如果方案组不存在,将创建之
	 * 
	 * @param sign
	 * @return 是否成功
	 * @throws RuleCenterRuntimeException
	 */
	public boolean saveSign(String planGroupId, final String sign) throws RuleCenterRuntimeException;

	/**
	 * 预览"默认备注签名"效果。
	 * 如果方案组不存在,将创建之.
	 * 
	 * @return 添加了所有备注和签名的备注。即有可能出现的最"多"的备注
	 * @throws RuleCenterRuntimeException
	 */
	public String previewSign(String planGroupId) throws RuleCenterRuntimeException;

	/**
	 * 根据店铺id获取该店铺的方案组.
	 * 如果该店铺还没有方案组,则创建之(比如首次使用).
	 * 
	 * @param shopId
	 * @return
	 * @throws RuleCenterRuntimeException
	 */
	public PlanGroup planGroupOfShop(String shopId) throws RuleCenterRuntimeException, IllegalArgumentException;

	/**
	 * 获取所有的方案组
	 * 
	 * @return
	 */
	public List<PlanGroup> findAll();
}
