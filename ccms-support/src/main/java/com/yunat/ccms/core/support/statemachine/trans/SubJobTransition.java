package com.yunat.ccms.core.support.statemachine.trans;

import static com.yunat.ccms.core.support.statemachine.state.SubjobState.COMPLETE_ERROR;
import static com.yunat.ccms.core.support.statemachine.state.SubjobState.COMPLETE_EXCEPTION;
import static com.yunat.ccms.core.support.statemachine.state.SubjobState.COMPLETE_SKIP;
import static com.yunat.ccms.core.support.statemachine.state.SubjobState.COMPLETE_STOP;
import static com.yunat.ccms.core.support.statemachine.state.SubjobState.COMPLETE_SUCCESS;
import static com.yunat.ccms.core.support.statemachine.state.SubjobState.EXECUTING;
import static com.yunat.ccms.core.support.statemachine.state.SubjobState.FINISH_ERROR;
import static com.yunat.ccms.core.support.statemachine.state.SubjobState.FINISH_EXCEPTION;
import static com.yunat.ccms.core.support.statemachine.state.SubjobState.FINISH_STOP;
import static com.yunat.ccms.core.support.statemachine.state.SubjobState.FINISH_SUCCESS;
import static com.yunat.ccms.core.support.statemachine.state.SubjobState.WAITING_RESOURCE;
import static com.yunat.ccms.core.support.statemachine.state.SubjobState.WAITING_TIMER;

import com.yunat.ccms.core.support.statemachine.Transition;
import com.yunat.ccms.core.support.statemachine.state.SubjobState;

public enum SubJobTransition implements Transition {

	/** 状态进入运行状态 */
	SUBJOB_HANDLE_START {
		public SubjobState afterTransition(SubjobState oldState) {
			switch (oldState) {
			case INITIAL:
				return EXECUTING;
			case WAITING_RESOURCE:
				return EXECUTING;
			case WAITING_TIMER:
				return EXECUTING;
			default:
				return oldState;
			}
		}
	},

	/** 状态进入中止状态 */
	SUBJOB_HANDLE_STOP {
		public SubjobState afterTransition(SubjobState oldState) {
			switch (oldState) {
			case INITIAL:
				return FINISH_STOP;
			case EXECUTING:
				return FINISH_STOP;
			case WAITING_RESOURCE:
				return FINISH_STOP;
			default:
				return oldState;
			}
		}
	},

	/** 状态进入执行完成（成功）状态 */
	SUBJOB_HANDLE_FINISH {
		public SubjobState afterTransition(SubjobState oldState) {
			switch (oldState) {
			case EXECUTING:
				return FINISH_SUCCESS;
			default:
				return oldState;
			}
		}
	},

	/** 状态进入执行完成（出错）状态 */
	SUBJOB_HANDLE_ERROR {
		public SubjobState afterTransition(SubjobState oldState) {
			switch (oldState) {
			case EXECUTING:
				return FINISH_ERROR;
			default:
				return oldState;
			}
		}
	},

	/** 状态进入出错待重试状态 */
	SUBJOB_HANDLE_EXCEPTION {
		public SubjobState afterTransition(SubjobState oldState) {
			switch (oldState) {
			case EXECUTING:
				return FINISH_EXCEPTION;
			default:
				return oldState;
			}
		}
	},

	/** 状态进入等待资源状态 */
	SUBJOB_HANDLE_WAIT_RESOURCE {
		public SubjobState afterTransition(SubjobState oldState) {
			switch (oldState) {
			case INITIAL:
			case EXECUTING:
			case WAITING_RESOURCE:
			case WAITING_TIMER:
				return WAITING_RESOURCE;
			default:
				return oldState;
			}
		}
	},

	/** 状态进入等待时间状态 */
	SUBJOB_HANDLE_WAIT_TIMER {
		public SubjobState afterTransition(SubjobState oldState) {
			switch (oldState) {
			default:
				return WAITING_TIMER;
			}
		}
	},

	/** 状态进入跳过状态 */
	SUBJOB_HANDLE_SKIP {
		public SubjobState afterTransition(SubjobState oldState) {
			switch (oldState) {
			case INITIAL:
				return COMPLETE_SKIP;
			default:
				return oldState;
			}
		}
	},

	/** 状态进入完结状态 */
	SUBJOB_HANDLE_COMPLETE {
		public SubjobState afterTransition(SubjobState oldState) {
			switch (oldState) {
			case FINISH_SUCCESS:
				return COMPLETE_SUCCESS;
			case FINISH_ERROR:
				return COMPLETE_ERROR;
			case FINISH_STOP:
				return COMPLETE_STOP;
			case FINISH_SKIP:
				return COMPLETE_SKIP;
			case FINISH_EXCEPTION:
				return COMPLETE_EXCEPTION;
			default:
				return oldState;
			}
		}
	},

	;// end of define enum

	public abstract SubjobState afterTransition(SubjobState oldState);

	public String getId() {
		return name();
	}

}
