package com.yunat.ccms.core.support.statemachine.state;

import static com.yunat.ccms.core.support.statemachine.trans.CampTransition.CAMP_HANDLE_APPROVE;
import static com.yunat.ccms.core.support.statemachine.trans.CampTransition.CAMP_HANDLE_EXECUTE;
import static com.yunat.ccms.core.support.statemachine.trans.CampTransition.CAMP_HANDLE_FINISH_ERROR;
import static com.yunat.ccms.core.support.statemachine.trans.CampTransition.CAMP_HANDLE_FINISH_SUCCESS;
import static com.yunat.ccms.core.support.statemachine.trans.CampTransition.CAMP_HANDLE_RECOVER;
import static com.yunat.ccms.core.support.statemachine.trans.CampTransition.CAMP_HANDLE_REDESING;
import static com.yunat.ccms.core.support.statemachine.trans.CampTransition.CAMP_HANDLE_STOP;
import static com.yunat.ccms.core.support.statemachine.trans.CampTransition.CAMP_HANDLE_SUBMIT_APPROVE;
import static com.yunat.ccms.core.support.statemachine.trans.CampTransition.CAMP_HANDLE_TEST;

import java.util.HashMap;
import java.util.Map;

import com.yunat.ccms.core.support.statemachine.State;
import com.yunat.ccms.core.support.statemachine.trans.CampTransition;

public enum CampaignState implements State {

	/** 设计中 */
	DESIGN("A1"),
	/** 待审批 */
	WAIT_APPROVE("A2"),
	/** 待执行 */
	WAIT_EXECUTE("A3"),
	/** 正式执行后被中止 */
	STOPED("A4"),
	/** 执行完成 */
	EXECUTED_SUCCESS("A5"),
	/** 执行结束（错误） */
	EXECUTED_ERROR("A6"),
	/** 设计时预执行 */
	TESTING_ON_DESIGN("B1"),
	/** 待审批时预执行 */
	TESTING_BEFORE_APPROVE("B2"),
	/** 执行中 */
	EXECUTING("B3"),

	;// end of define enum

	private String code;

	CampaignState(String code) {
		this.code = code;
	}

	private static final Map<String, CampaignState> _map = new HashMap<String, CampaignState>();
	static {
		for (CampaignState campState : CampaignState.values()) {
			_map.put(campState.getCode(), campState);
		}
	}

	// factory method
	public static CampaignState fromCode(String code) {
		return _map.get(code);
	}

	@Override
	public String getId() {
		return name();
	}

	public String getCode() {
		return code;
	}

	public CampaignState next(CampTransition transition) {
		return transition.afterTransition(this);
	}

	public CampaignState handleTest() {
		return this.next(CAMP_HANDLE_TEST);
	}

	public CampaignState handlExecute() {
		return this.next(CAMP_HANDLE_EXECUTE);
	}

	public CampaignState handleFinishSuccess() {
		return this.next(CAMP_HANDLE_FINISH_SUCCESS);
	}

	public CampaignState handleFinishError() {
		return this.next(CAMP_HANDLE_FINISH_ERROR);
	}

	public CampaignState handleStop() {
		return this.next(CAMP_HANDLE_STOP);
	}

	public CampaignState handleRecover() {
		return this.next(CAMP_HANDLE_RECOVER);
	}

	public CampaignState handleSubmitApprove() {
		return this.next(CAMP_HANDLE_SUBMIT_APPROVE);
	}

	public CampaignState handleApprove() {
		return this.next(CAMP_HANDLE_APPROVE);
	}

	public CampaignState handleReDesign() {
		return this.next(CAMP_HANDLE_REDESING);
	}

	/**
	 * 是否在测试执行
	 * 
	 * @return
	 */
	public boolean isTesting() {
		switch (this) {
		case TESTING_ON_DESIGN:
		case TESTING_BEFORE_APPROVE:
			return true;
		default:
			return false;
		}
	}

	/**
	 * 是否在执行中
	 * 
	 * @return
	 */
	public boolean isRunning() {
		switch (this) {
		case EXECUTING:
		case TESTING_ON_DESIGN:
		case TESTING_BEFORE_APPROVE:
			return true;
		default:
			return false;
		}
	}

	/**
	 * 显示测试执行 or 正式执行
	 * 
	 * @return
	 */
	public boolean showTestJob() {
		switch (this) {
		case DESIGN:
		case TESTING_ON_DESIGN:
		case WAIT_APPROVE:
		case TESTING_BEFORE_APPROVE:
			return true;
		default:
			return false;
		}
	}

	/**
	 * 是否可以执行删除动作（物理/逻辑）
	 * 
	 * @return
	 */
	public boolean supportDelete() {
		switch (this) {
		case DESIGN:
		case STOPED:
		case EXECUTED_ERROR:
		case EXECUTED_SUCCESS:
			return true;
		default:
			return false;
		}
	}

}
