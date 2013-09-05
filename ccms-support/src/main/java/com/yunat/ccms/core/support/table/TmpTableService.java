package com.yunat.ccms.core.support.table;



/**
 * 临时表生命周期管理，负责：
 * </br>1:创建临时表及视图（并注册）
 * </br>2:及时删除临时表及视图
 * 
 * </br> 需要对节点创建临时表的接口
 * @author xiaojing.qu
 *
 */
public interface TmpTableService {
	
	
	
	void clearOnJobSuccess(Long jobId);

}
