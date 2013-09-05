package com.yunat.ccms.core.support.statemachine.state;

import java.util.HashMap;
import java.util.Map;

import com.yunat.ccms.core.support.statemachine.State;
import com.yunat.ccms.core.support.statemachine.trans.SubJobTransition;

/**
 * 活动中subjob的状态，对应twf_log_subjob中status
 * 
 * @see com.huaat.ccms.workflow.domain.fsm.state.JobState
 * @author xiaojing.qu
 * 
 */
public enum SubjobState implements State {

	/** 新建状态，</br>数据库代码10 */
	INITIAL(10L),
	/** 正在执行，</br>数据库代码11 */
	EXECUTING(11L),
	/** 等待某种资源</br>数据库代码8 */
	WAITING_RESOURCE(8L),
	/** 等待Timer触发</br>数据库代码18 */
	WAITING_TIMER(18L),

	/** 运行完成，正确，</br>数据库代码21 */
	FINISH_SUCCESS(21L),
	/** 运行完成，但有错，</br>数据库代码22 */
	FINISH_ERROR(22L),
	/** 运行时中止，</br>数据库代码23 */
	FINISH_STOP(23L),
	/** 运行时跳过，</br>数据库代码24 */
	FINISH_SKIP(24L),
	/** 运行错误(可恢复)，</br>数据库代码25 */
	FINISH_EXCEPTION(25L),

	/** 运行正确,处理完成，</br>数据库代码51 */
	COMPLETE_SUCCESS(51L),
	/** 运行错误,处理完成，</br>数据库代码52 */
	COMPLETE_ERROR(52L),
	/** 运行中止,处理完成，</br>数据库代码53 */
	COMPLETE_STOP(53L),
	/** 运行跳过,处理完成，</br>数据库代码54 */
	COMPLETE_SKIP(54L),
	/** 运行错误(可恢复)，处理完成，</br>数据库代码55 */
	COMPLETE_EXCEPTION(55L),

	;

	private Long code;// 对应数据库中的代码

	SubjobState(Long code) {
		this.code = code;
	}

	private static final Map<Long, SubjobState> _map = new HashMap<Long, SubjobState>();
	static {
		for (SubjobState subjobState : SubjobState.values()) {
			_map.put(subjobState.getCode(), subjobState);
		}
	}

	// factory method
	public static SubjobState fromCode(Long code) {
		return _map.get(code);
	}

	public boolean isRunning() {
		switch (this) {
		case EXECUTING:
		case WAITING_RESOURCE:
			return true;
		default:
			return false;
		}
	}

	public boolean isFinished() {
		switch (this) {
		case FINISH_SUCCESS:
		case FINISH_ERROR:
		case FINISH_STOP:
		case FINISH_SKIP:
		case FINISH_EXCEPTION:
		case COMPLETE_SUCCESS:
		case COMPLETE_ERROR:
		case COMPLETE_STOP:
		case COMPLETE_SKIP:
		case COMPLETE_EXCEPTION:
			return true;
		default:
			return false;
		}
	}

	public boolean isError() {
		switch (this) {
		case FINISH_ERROR:
		case FINISH_SKIP:
		case FINISH_EXCEPTION:
		case COMPLETE_ERROR:
		case COMPLETE_SKIP:
		case COMPLETE_EXCEPTION:
		case FINISH_STOP:
		case COMPLETE_STOP:
			return true;
		default:
			return false;
		}
	}

	public boolean isComplete() {
		switch (this) {
		case FINISH_SUCCESS:
		case COMPLETE_SUCCESS:
			return true;
		default:
			return false;
		}
	}

	public boolean isSuccess() {
		switch (this) {
		case FINISH_SUCCESS:
		case COMPLETE_SUCCESS:
			return true;
		default:
			return false;
		}
	}

	public boolean isSkip() {
		switch (this) {
		case FINISH_SKIP:
		case COMPLETE_SKIP:
			return true;
		default:
			return false;
		}
	}

	public SubjobState next(SubJobTransition transition) {
		// throw new
		// UnsupportedOperationException("can't perform transition:"+transition.getId()+" on state:"+this.getName());
		// LOGGER.error("can't perform transition:"+transition.getId()+" on state:"+this.getName());
		return transition.afterTransition(this);
	}

	public SubjobState handleStart() {
		return this.next(SubJobTransition.SUBJOB_HANDLE_START);
	}

	public SubjobState handleStop() {
		return this.next(SubJobTransition.SUBJOB_HANDLE_STOP);
	}

	public SubjobState handleFinish() {
		return this.next(SubJobTransition.SUBJOB_HANDLE_FINISH);
	}

	public SubjobState handleError() {
		return this.next(SubJobTransition.SUBJOB_HANDLE_ERROR);
	}

	public SubjobState handleException() {
		return this.next(SubJobTransition.SUBJOB_HANDLE_EXCEPTION);
	}

	public SubjobState handleWaitResource() {
		return this.next(SubJobTransition.SUBJOB_HANDLE_WAIT_RESOURCE);
	}

	public SubjobState handleWaitTimer() {
		return this.next(SubJobTransition.SUBJOB_HANDLE_WAIT_TIMER);
	}

	public SubjobState handleSkip() {
		return this.next(SubJobTransition.SUBJOB_HANDLE_SKIP);
	}

	public SubjobState handleComplete() {
		return this.next(SubJobTransition.SUBJOB_HANDLE_COMPLETE);
	}

	public Long getCode() {
		return code;
	}

	@Override
	public String getId() {
		return name();
	}

}
