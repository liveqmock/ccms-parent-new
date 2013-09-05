package com.yunat.ccms.schedule.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

import com.lmax.disruptor.EventTranslator;
import com.yunat.ccms.schedule.core.disruptor.Event;
import com.yunat.ccms.schedule.support.ParamHolder;
import com.yunat.ccms.schedule.support.ScheduleCons;

/**
 * 调度器内的任务,可执行的单元
 * 
 * Task今后不要再实现 序列化接口
 * 
 */
public abstract class Task implements Runnable, EventTranslator<Event> {

	/*** 活动ID */
	protected final long campId;
	/*** 是否是测试执行 */
	protected final boolean isTest;
	/*** 触发器 */
	protected final TaskTrigger trigger;
	/*** 运行时参数 */
	protected final ParamHolder extraParams;

	public Task(long campId, boolean isTest, TaskTrigger trigger, ParamHolder extra) {
		Assert.notNull(trigger);
		this.campId = campId;
		this.isTest = isTest;
		this.trigger = trigger;
		this.extraParams = (extra == null) ? new ParamHolder() : extra;
	}

	public abstract String getTaskId();

	public long getCampId() {
		return campId;
	}

	public boolean isTest() {
		return isTest;
	}

	public TaskTrigger getTrigger() {
		return trigger;
	}

	@SuppressWarnings("unchecked")
	public <T> T getRuntimeParam(String key) {
		return (T) extraParams.get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T getRuntimeParam(String key, T nullSafeValue) {
		T t = (T) extraParams.get(key);
		return t == null ? nullSafeValue : t;
	}

	public <T> void putRuntimeParam(String key, T value) {
		extraParams.put(key, value);
	}

	@Override
	public Event translateTo(Event event, long sequence) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(ScheduleCons.KEY_TASK, this);
		event.setData(data);
		return null;
	}

}
