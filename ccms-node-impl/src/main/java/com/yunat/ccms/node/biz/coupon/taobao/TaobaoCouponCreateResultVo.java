package com.yunat.ccms.node.biz.coupon.taobao;

public class TaobaoCouponCreateResultVo {

	public boolean success;
	public String errorMsg;
	public TaobaoCoupon result;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public TaobaoCoupon getResult() {
		return result;
	}

	public void setResult(TaobaoCoupon result) {
		this.result = result;
	}

}
