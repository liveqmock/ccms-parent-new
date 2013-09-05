package com.yunat.ccms.channel.support.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.yunat.ccms.channel.support.cons.EnumBlackList;
import com.yunat.ccms.channel.support.domain.BlackList;

/**
 * 黑名单业务接口
 * 
 * @author kevin.jiang 2013-4-24
 */
public interface BlackListService {

	public BlackList loadBlackList(String value, EnumBlackList type);

	// 新增黑名单
	public void createBlackList(List<String> values, EnumBlackList type) throws Exception;

	// 删除黑名单
	public void deleteBlackList(List<String> values, EnumBlackList type) throws Exception;

	// 获取黑名单-分页-查询
	public Page<BlackList> getBlackListAll(int pageIndex, int pageSize, String filterValue, EnumBlackList type,
			String sortName, String sortOrder);

	// 获取黑名单-下载
	public List<BlackList> getBlackListAll(String filterValue, EnumBlackList type);

	/**
	 * 手动导入数据
	 * 
	 * @param uploadUrl
	 *            上传文件服务器路径
	 * @param type
	 *            类型：邮件EMAIL，手机MOBILE，会员MEMBER
	 * @return
	 */
	public List<Map<String, Object>> uploadBlackList(String uploadUrl, EnumBlackList type) throws Exception;

	// 获取一个文件时间戳
	public Long getNewId();

}
