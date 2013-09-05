package com.yunat.ccms.metadata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.metadata.pojo.DatabaseColumn;
import com.yunat.ccms.metadata.repository.DatabaseColumnDao;

/**
 * 元数据基本配置服务
 * 
 * @author kevin.jiang 2013-3-18
 */
@Service
@Transactional
public class MetaBaseConfigService {

	// TODO: 表定义，字段定义，字典，引用，展示索引等基本信息配置的CRUD操作
	@Autowired
	private DatabaseColumnDao databaseColumnDao;
	
	public DatabaseColumn findDatabaseColumnById(Long id) {
		return databaseColumnDao.findOne(id);
	}
}
