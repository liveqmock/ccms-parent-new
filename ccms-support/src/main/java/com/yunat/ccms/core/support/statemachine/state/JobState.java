package com.yunat.ccms.core.support.statemachine.state;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunat.ccms.core.support.statemachine.State;
import com.yunat.ccms.core.support.statemachine.trans.JobTransition;

/**
 * 活动中job的状态，对应twf_log_job中status
 * 
 * @see com.huaat.ccms.workflow.domain.fsm.state.SubjobState
 * @author xiaojing.qu
 * 
 */
public enum JobState implements State {

	/** 新建状态,</br>数据库代码10 */
	INITIAL(10L),
	/** 正在执行,</br>数据库代码11 */
	EXECUTING(11L),
	/** 执行中，有错误，</br>数据库代码12 */
	EXECUTING_ERROR(12L),
	/**
	 * 执行中，收到停止请求，</br>数据库代码13
	 * EXECUTING_STOP(13L),
	 */
	/** job完成（正确），</br>数据库代码21 */
	FINISH_SUCCESS(21L),
	/** job完成（出错），</br>数据库代码22 */
	FINISH_ERROR(22L),
	/** job完成（被停止），</br>数据库代码23 */
	FINISH_STOP(23L),

	;// end of enum define

	private Long code;// 对应数据库中的代码

	JobState(Long code) {
		this.code = code;
	}

	private static final Map<Long, JobState> _map = new HashMap<Long, JobState>();
	static {
		for (JobState jobState : JobState.values()) {
			_map.put(jobState.getCode(), jobState);
		}
	}

	// factory method
	public static JobState fromCode(Long code) {
		return _map.get(code);
	}

	public static List<Long> getRunningJobCode() {
		return Arrays.asList(EXECUTING.getCode(), EXECUTING_ERROR.getCode());
	}

	public static List<Long> getFinishedJobCode() {
		return Arrays.asList(FINISH_SUCCESS.getCode(), FINISH_ERROR.getCode(), FINISH_STOP.getCode());
	}

	/**
	 * @return 本状态在数据库中的代码
	 */
	public Long getCode() {
		return code;
	}

	@Override
	public String getId() {
		return name();
	}

	/** job是否正在执行 */
	public boolean isRunning() {
		switch (this) {
		case EXECUTING:
		case EXECUTING_ERROR:
			return true;
		default:
			return false;
		}
	}

	/**
	 * @return 本状态是否是一个完结状态
	 */
	public boolean isFinished() {
		switch (this) {
		case FINISH_SUCCESS:
		case FINISH_ERROR:
		case FINISH_STOP:
			return true;
		default:
			return false;
		}
	}

	/**
	 * @return 本状态是否是一个错误状态
	 */
	public boolean isError() {
		switch (this) {
		case EXECUTING_ERROR:
		case FINISH_ERROR:
			return true;
		default:
			return false;
		}
	}

	/**
	 * @return 本状态是否是一个工作流完结状态
	 */
	public boolean isComplete() {
		switch (this) {
		case FINISH_SUCCESS:
		case FINISH_ERROR:
		case FINISH_STOP:
			return true;
		default:
			return false;
		}
	}

	/**
	 * @return 本状态是否是一个工作流成功状态
	 */
	public boolean isSuccess() {
		switch (this) {
		case FINISH_SUCCESS:
			return true;
		default:
			return false;
		}
	}

	/**
	 * @return 本状态是否是一个工作流跳过状态
	 */
	public boolean isSkip() {
		return false;
	}

	public JobState next(JobTransition transition) {
		return transition.afterTransition(this);
	}

	public JobState handleStart() {
		return this.next(JobTransition.JOB_HANDLE_START);
	}

	public JobState handleFinish() {
		return this.next(JobTransition.JOB_HANDLE_FINISH);
	}

	public JobState handleError() {
		return this.next(JobTransition.JOB_HANDLE_ERROR);
	}

	public JobState handleStop() {
		return this.next(JobTransition.JOB_HANDLE_STOP);
	}

	public JobState handleComplete() {
		return this.next(JobTransition.JOB_HANDLE_COMPLETE);
	}

	public JobState handleRecover() {
		return this.next(JobTransition.JOB_HANDLE_RECOVER);
	}

}
