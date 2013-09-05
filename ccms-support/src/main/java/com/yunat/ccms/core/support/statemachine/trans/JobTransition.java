package com.yunat.ccms.core.support.statemachine.trans;

import static com.yunat.ccms.core.support.statemachine.state.JobState.EXECUTING;
import static com.yunat.ccms.core.support.statemachine.state.JobState.FINISH_ERROR;
import static com.yunat.ccms.core.support.statemachine.state.JobState.FINISH_STOP;
import static com.yunat.ccms.core.support.statemachine.state.JobState.FINISH_SUCCESS;

import com.yunat.ccms.core.support.statemachine.Transition;
import com.yunat.ccms.core.support.statemachine.state.JobState;

public enum JobTransition implements Transition {

	/** 状态进入运行状态 */
	JOB_HANDLE_START {
		public JobState afterTransition(JobState oldState) {
			switch (oldState) {
			case INITIAL:
				return EXECUTING;
			default:
				return oldState;
			}
		}
	},

	/** 状态进入结束状态 */
	JOB_HANDLE_FINISH {
		public JobState afterTransition(JobState oldState) {
			switch (oldState) {
			case EXECUTING:
				return FINISH_SUCCESS;
			case EXECUTING_ERROR:
				return FINISH_ERROR;
			default:
				return oldState;
			}
		}
	},

	/** 状态进入出错状态 */
	JOB_HANDLE_ERROR {
		public JobState afterTransition(JobState oldState) {
			switch (oldState) {
			case EXECUTING:
			case EXECUTING_ERROR:
				return JobState.EXECUTING_ERROR;
			default:
				return oldState;
			}
		}
	},

	/** 状态进入停止状态 */
	JOB_HANDLE_STOP {
		public JobState afterTransition(JobState oldState) {
			switch (oldState) {
			case INITIAL:
			case EXECUTING:
			case EXECUTING_ERROR:
				return FINISH_STOP;
			default:
				return oldState;
			}
		}
	},

	/** 状态进入完结状态 */
	JOB_HANDLE_COMPLETE {
		public JobState afterTransition(JobState oldState) {

			switch (oldState) {
			case EXECUTING:
				return FINISH_SUCCESS;
			case EXECUTING_ERROR:
				return FINISH_ERROR;
			default:
				return oldState;
			}
		}
	},

	/** 恢复活动中部分节点运行,clear bad memory */
	JOB_HANDLE_RECOVER {
		public JobState afterTransition(JobState oldState) {
			switch (oldState) {
			case EXECUTING:
			case EXECUTING_ERROR:
			case FINISH_ERROR:
			case FINISH_STOP:
				return EXECUTING;
			default:
				return oldState;
			}
		}
	},

	;// end of define enum

	public abstract JobState afterTransition(JobState oldState);

	public String getId() {
		return name();
	}

}
