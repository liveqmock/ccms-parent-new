package com.yunat.ccms.schedule.support;


import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mybaitis实现的dao基类
 * @author xiaojing.qu
 *
 */
public  abstract class MybatisBaseDao extends SqlSessionDaoSupport{
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 得到当前dao对应的StatementId,对应xml文件中sql的唯一标识，命名空间+ID
	 * </br>如在com.huaat.ccms.ABCDao.funcB()访问该方法，应该返回com.huaat.ccms.ABCDao.funcB
	 * </br>即把com.huaat.ccms.ABCDao作为NameSpace，funcB作为Id
	 * @return  SelectId
	 *
	 */
	private  String getStatementId(){
		String subClassName = getClass().getName();
		StackTraceElement[] statcTraces  = Thread.currentThread().getStackTrace();
		for(StackTraceElement set:statcTraces){
			if(subClassName.equals(set.getClassName())){
				return set.getClassName()+"."+set.getMethodName();
			}
		}
		return null;
	}
	
	protected <T> T save(T obj) {
		String statement = getStatementId();
		logExecuteSql4Debug(statement,obj);
		getSqlSession().insert(statement, obj);
		return obj;
	}
	
	protected <T> int  update(T obj) {
		String statement = getStatementId();
		logExecuteSql4Debug(statement,obj);
		return getSqlSession().update(statement, obj);
	}
	
	protected <T> int delete(Object parameter) {
		String statement = getStatementId();
		logExecuteSql4Debug(statement,parameter);
		return getSqlSession().delete(statement, parameter);
	}
	
	/**
	 * 根据传入参数，获取返回类型对应数据
	 * @param 对应语句的参数
	 */
	@SuppressWarnings("unchecked")
	protected <T> T get(Object parameter) {
		String statement = getStatementId();
		logExecuteSql4Debug(statement,parameter);
		return (T) getSqlSession().selectOne(statement, parameter);
	}


	/**
	 * 根据Key以及传入参数，获取返回类型对应数据的List
	 * @param key xml文件中sql的唯一标识，命名空间+ID
	 * @param 对应语句的参数
	 */
	@SuppressWarnings("unchecked")
	protected <T> List<T> list(Object parameter) {
		String statement = getStatementId();
		logExecuteSql4Debug(statement,parameter);
		return getSqlSession().selectList(statement, parameter);
	}
	
	/**
	 * 根据Key以及传入参数，log记录获取要执行的sql
	 * @param key xml文件中sql的唯一标识，命名空间+ID
	 * @param parameter 和xml的key值对应语句的参数
	 * 
	 */
	private void logExecuteSql4Debug(String key, Object parameter){
		if(!logger.isDebugEnabled()){
			return ;
		}
		Configuration configuration = getSqlSession().getConfiguration();
		// 获取key对应的上下文句柄
		MappedStatement ms = configuration.getMappedStatement(key);
		BoundSql boundsql = ms.getBoundSql(parameter);
		String sql = boundsql.getSql();
		if(logger.isDebugEnabled()) {
			logger.debug("key:"+key + " | parameter:"+parameter + " | sql:"+sql);
		}
	}


}
