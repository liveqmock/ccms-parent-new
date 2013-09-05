package com.yunat.ccms.core.support.statemachine.trans;

import static com.yunat.ccms.core.support.statemachine.state.CampaignState.DESIGN;
import static com.yunat.ccms.core.support.statemachine.state.CampaignState.EXECUTED_ERROR;
import static com.yunat.ccms.core.support.statemachine.state.CampaignState.EXECUTED_SUCCESS;
import static com.yunat.ccms.core.support.statemachine.state.CampaignState.EXECUTING;
import static com.yunat.ccms.core.support.statemachine.state.CampaignState.STOPED;
import static com.yunat.ccms.core.support.statemachine.state.CampaignState.TESTING_BEFORE_APPROVE;
import static com.yunat.ccms.core.support.statemachine.state.CampaignState.TESTING_ON_DESIGN;
import static com.yunat.ccms.core.support.statemachine.state.CampaignState.WAIT_APPROVE;
import static com.yunat.ccms.core.support.statemachine.state.CampaignState.WAIT_EXECUTE;

import com.yunat.ccms.core.support.statemachine.Transition;
import com.yunat.ccms.core.support.statemachine.state.CampaignState;

public enum CampTransition implements Transition {

	/** 测试执行 */
	CAMP_HANDLE_TEST {
		@Override
		public CampaignState afterTransition(CampaignState oldState) {
			switch (oldState) {
			case DESIGN:
				return TESTING_ON_DESIGN;
			case WAIT_APPROVE:
				return TESTING_BEFORE_APPROVE;
			default:
				return oldState;
			}
		}
	},

	/** 正式执行 */
	CAMP_HANDLE_EXECUTE {
		@Override
		public CampaignState afterTransition(CampaignState oldState) {
			switch (oldState) {
			case DESIGN:
				return EXECUTING;
			case WAIT_EXECUTE:
				return EXECUTING;
			default:
				return oldState;
			}
		}
	},

	/** 执行成功 */
	CAMP_HANDLE_FINISH_SUCCESS {
		@Override
		public CampaignState afterTransition(CampaignState oldState) {
			switch (oldState) {
			case TESTING_ON_DESIGN:
				return DESIGN;
			case TESTING_BEFORE_APPROVE:
				return WAIT_APPROVE;
			case EXECUTING:
				return EXECUTED_SUCCESS;
			default:
				return oldState;
			}
		}
	},

	/** 执行完成（出错） */
	CAMP_HANDLE_FINISH_ERROR {
		@Override
		public CampaignState afterTransition(CampaignState oldState) {
			switch (oldState) {
			case TESTING_ON_DESIGN:
				return DESIGN;
			case TESTING_BEFORE_APPROVE:
				return WAIT_APPROVE;
			case EXECUTING:
				return EXECUTED_ERROR;
			default:
				return oldState;
			}
		}
	},

	/** 停止执行 */
	CAMP_HANDLE_STOP {
		@Override
		public CampaignState afterTransition(CampaignState oldState) {
			switch (oldState) {
			case TESTING_ON_DESIGN:
				return DESIGN;
			case TESTING_BEFORE_APPROVE:
				return WAIT_APPROVE;
			case EXECUTING:
				return STOPED;
			default:
				return oldState;
			}
		}
	},

	/** 恢复执行 */
	CAMP_HANDLE_RECOVER {
		@Override
		public CampaignState afterTransition(CampaignState oldState) {
			switch (oldState) {
			case EXECUTED_SUCCESS:
			case EXECUTED_ERROR:
			case STOPED:
				return EXECUTING;
			default:
				return oldState;
			}
		}
	},

	/** 提交审核 */
	CAMP_HANDLE_SUBMIT_APPROVE {
		@Override
		public CampaignState afterTransition(CampaignState oldState) {
			switch (oldState) {
			case DESIGN:
				return WAIT_APPROVE;
			default:
				return oldState;
			}
		}
	},

	/** 审核通过 */
	CAMP_HANDLE_APPROVE {
		@Override
		public CampaignState afterTransition(CampaignState oldState) {
			switch (oldState) {
			case WAIT_APPROVE:
				return WAIT_EXECUTE;
			default:
				return oldState;
			}
		}
	},

	/** 返回设计状态，审核不通过 */
	CAMP_HANDLE_REDESING {
		@Override
		public CampaignState afterTransition(CampaignState oldState) {
			switch (oldState) {
			case WAIT_APPROVE:
				return DESIGN;
			default:
				return oldState;
			}
		}
	},

	;// end of define enum

	public abstract CampaignState afterTransition(CampaignState oldState);

	@Override
	public String getId() {
		return name();
	}
}
