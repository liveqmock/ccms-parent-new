package com.yunat.ccms.tradecenter.service;

import java.util.List;

import com.yunat.ccms.tradecenter.domain.DictDomain;

/**
 * 字典表服务层
 *
 * @author teng.zeng
 * date 2013-5-31 下午07:30:56
 */
public interface DictService {

	DictDomain getByTypeAndCode(Integer type,String code);

	List<DictDomain> getByType(Integer type);

}
