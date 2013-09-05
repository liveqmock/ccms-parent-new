package com.yunat.ccms.core.support.vo;

import java.util.Date;

import com.yunat.ccms.core.support.utils.DateUtils;

public class ControlerResult {

	/*** 状态 (0代表成功,-1代表失败) */
	private int status;

	/***
	 * 错误代码(可以参考
	 * http://wiki.yunat.com/pages/viewpage.action?pageId=14757693,以后可以做本地化)
	 */
	private String errCode;

	/*** 提示信息 */
	private String message;

	/*** 数据,vo/map/array */
	private Object data;

	/*** 访问时间 */
	private String visit;

	protected ControlerResult(final int status, final String errCode, final String message, final Object resultVo) {
		this.status = status;
		this.errCode = errCode;
		this.message = message;
		visit = DateUtils.getStringDate(new Date());
		data = resultVo;
	}

	/**
	 * 返回成功结果
	 * 
	 * @return
	 */
	public static ControlerResult newSuccess() {
		return new Success(null, null);
	}

	/**
	 * 返回带消息的成功结果
	 * 
	 * @param message
	 *            成功消息
	 * @return
	 */
	public static ControlerResult newSuccess(final String message) {
		return new Success(message, null);
	}

	/**
	 * 返回成功结果
	 * 
	 * @param resultVo
	 *            可以是VO/Map
	 * @return
	 */
	public static ControlerResult newSuccess(final Object resultVo) {
		return new Success(null, resultVo);
	}

	/**
	 * 返回成功结果
	 * 
	 * @param message
	 * @param resultVo
	 *            可以是VO/Map
	 * @return
	 */
	public static ControlerResult newSuccess(final String message, final Object resultVo) {
		return new Success(message, resultVo);
	}

	/**
	 * 返回带默认消息的失败结果。 默认消息："操作失败，请稍后重试。"
	 * 
	 * @return
	 */
	public static ControlerResult newError() {
		return new Error(null, "操作失败，请稍后重试。", null);
	}

	/**
	 * 返回带消息的失败结果
	 * 
	 * @param errorCode
	 * @param message
	 * @return
	 */
	public static ControlerResult newError(final String message) {
		return new Error(null, message, null);
	}

	/**
	 * 返回带代码和消息的失败结果
	 * 
	 * @param errorCode
	 * @param message
	 * @return
	 */
	public static ControlerResult newError(final String errorCode, final String message) {
		return new Error(errorCode, message, null);
	}

	static class Success extends ControlerResult {
		Success(final String message, final Object data) {
			super(0, null, message, data);
		}
	}

	static class Error extends ControlerResult {
		Error(final String errorCode, final String errorMessage, final Object data) {
			super(-1, errorCode, errorMessage, data);
		}
	}

	public int getStatus() {
		return status;
	}

	public String getErrCode() {
		return errCode;
	}

	public String getMessage() {
		return message;
	}

	public Object getData() {
		return data;
	}

	public String getVisit() {
		return visit;
	}

	public ControlerResult setStatus(final int status) {
		this.status = status;
		return this;
	}

	public ControlerResult setErrCode(final String errCode) {
		this.errCode = errCode;
		return this;
	}

	public ControlerResult setMessage(final String message) {
		this.message = message;
		return this;
	}

	public ControlerResult setData(final Object data) {
		this.data = data;
		return this;
	}

	public ControlerResult setVisit(final String visit) {
		this.visit = visit;
		return this;
	}

}
