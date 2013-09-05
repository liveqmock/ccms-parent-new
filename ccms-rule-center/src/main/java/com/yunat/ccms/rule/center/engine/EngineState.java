package com.yunat.ccms.rule.center.engine;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 规则引擎状态
 * 
 * @author xiaojing.qu
 * 
 */
public class EngineState {

	private static Logger logger = LoggerFactory.getLogger(EngineState.class);

	private static ConcurrentHashMap<String, AtomicBoolean> engineStates = new ConcurrentHashMap<String, AtomicBoolean>();

	/**
	 * 检查规则引擎当前是否可用
	 * 
	 * @param shopId
	 * @return
	 */
	public static boolean avaliable(String shopId) {
		AtomicBoolean state = engineStates.get(shopId);
		if (state == null) {
			logger.warn("规则引擎中Shop:{}对应的规则未初始化", shopId);
			return false;
		}
		boolean active = state.get();
		logger.info("规则引擎中Shop:{}对应的规则状态：{}", shopId, active);
		return active;
	}

	/**
	 * 开启引擎服务
	 * 
	 * @param shopId
	 */
	public static void start(String shopId) {
		AtomicBoolean state = engineStates.get(shopId);
		if (state == null) {
			state = new AtomicBoolean();
		}
		state.set(true);
		engineStates.put(shopId, state);
	}

	/**
	 * 中止引擎服务？
	 * 
	 * @param shopId
	 */
	public static void halt(String shopId) {
		AtomicBoolean state = engineStates.get(shopId);
		if (state == null) {
			state = new AtomicBoolean();
		}
		state.set(false);
		engineStates.put(shopId, state);
	}

}
