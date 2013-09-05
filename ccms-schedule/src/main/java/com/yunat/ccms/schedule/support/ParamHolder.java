package com.yunat.ccms.schedule.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * 参数暂存
 * @author xiaojing.qu
 *
 */
public class ParamHolder {

	private Map<String,Object> params  = new HashMap<String,Object>();

	public <T> void put(String key,T value){
		if(key!=null&&value!=null){
			params.put(key, value);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key){
		return (T)get(key,false);
	}

	/**
	 * @param key 参数的键
	 * @param required 参数是否是必须的
	 * @return 参数的值
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key,boolean required){
		if(required){
			Assert.isTrue(params.containsKey(key), "key:["+key+"] is not found in params But is required!");
		}
		return (T)params.get(key);
	}

	/**
	 * @param key 参数的键
	 * @param failDefault 如果未找到相应的key，按照提供的默认值返回
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getSafely(String key,T failDefault){
		Assert.notNull(failDefault);
		if(params.containsKey(key)){
			return (T)params.get(key);
		}else{
			return failDefault;
		}
	}

}
