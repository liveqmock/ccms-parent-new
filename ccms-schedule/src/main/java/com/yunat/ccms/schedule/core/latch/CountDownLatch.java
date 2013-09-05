package com.yunat.ccms.schedule.core.latch;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.Assert;

/**
 * <h1>任务同步计数器</h1>
 * <p>
 * 用来同步多个异步任务。当计数器减为0，<code>check()</code>方法返回 <code>true</code> ，否则返回
 * <code>false</code>。
 * </p>
 * 
 * 
 * @author xiaojing.qu
 * 
 */
public abstract class CountDownLatch {

	/** Job同步计数器ID的格式{@value} */
	protected static final String FLOW_LATCH_ID_PATTERN = "FLOW_{0,number,#}";
	/** 合并同步计数器ID的格式{@value} */
	protected static final String GATEWAY_LATCH_ID_PATTERN = "FLOW_{0,number,#}_GATEWAY_{1,number,#}";
	/** 节点同步计数器ID的格式{@value} */
	protected static final String NODE_LATCH_ID_PATTERN = "FLOW_{0,number,#}_NODE_{1,number,#}";

	private final String latchId;
	private final AtomicInteger current;
	private final int total;

	protected CountDownLatch(String latchId, int total) {
		this.latchId = latchId;
		this.current = new AtomicInteger(total);
		this.total = total;
	}

	/**
	 * 计数器减一
	 * 
	 * @return 减一动作是否真正发生
	 */
	public boolean countDown() {
		if (current.get() > 0) {
			current.decrementAndGet();
			return true;
		}
		return false;// 如果是0或者小于0，不应该在CountDown
	}

	/**
	 * 检查当前计数器是否为0
	 * 
	 * @return
	 */
	public boolean check() {
		Assert.isTrue(current.get() >= 0);// 应该是正整数
		return current.get() == 0;
	}

	/**
	 * 计数器加一（某个步骤需要redo）
	 * 
	 * @return 是否真正执行了countUp动作
	 */
	public boolean countUp() {
		if (current.get() == total) {
			return false;
		}
		current.incrementAndGet();
		return true;
	}

	public String getLatchId() {
		return latchId;
	}

	public int getCurrent() {
		return current.get();
	}

	public int getTotal() {
		return total;
	}

	public abstract long getJobId();

}
