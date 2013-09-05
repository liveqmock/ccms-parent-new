package com.yunat.ccms.auth.login.taobao;

public class TopParams {

	private String top_appkey;
	private String top_parameters;
	private String top_session;
	private String sign;
	private String timestamp;
	private String[] itemCode;
	private String top_sign;
	private String shop_id;

	//以下参数是从top_parameters中得到
	private String sub_taobao_user_id;
	private String sub_taobao_user_nick;
	private String visitor_id;
	private String visitor_nick;

	private boolean isSubuser;//是否是子账号

	public TopParams() {
	}

	/**
	 * @return the top_appkey
	 */
	public String getTop_appkey() {
		return top_appkey;
	}

	/**
	 * @param top_appkey the top_appkey to set
	 */
	public void setTop_appkey(final String top_appkey) {
		this.top_appkey = top_appkey;
	}

	/**
	 * @return the top_parameters
	 */
	public String getTop_parameters() {
		return top_parameters;
	}

	/**
	 * @param top_parameters the top_parameters to set
	 */
	public void setTop_parameters(final String top_parameters) {
		this.top_parameters = top_parameters;
	}

	/**
	 * @return the top_session
	 */
	public String getTop_session() {
		return top_session;
	}

	/**
	 * @param top_session the top_session to set
	 */
	public void setTop_session(final String top_session) {
		this.top_session = top_session;
	}

	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * @param sign the sign to set
	 */
	public void setSign(final String sign) {
		this.sign = sign;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(final String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the itemCode
	 */
	public String[] getItemCode() {
		return itemCode;
	}

	/**
	 * @param itemCode the itemCode to set
	 */
	public void setItemCode(final String[] itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * @return the top_sign
	 */
	public String getTop_sign() {
		return top_sign;
	}

	/**
	 * @param top_sign the top_sign to set
	 */
	public void setTop_sign(final String top_sign) {
		this.top_sign = top_sign;
	}

	/**
	 * @return the shop_id
	 */
	public String getShop_id() {
		return shop_id;
	}

	/**
	 * @param shop_id the shop_id to set
	 */
	public void setShop_id(final String shop_id) {
		this.shop_id = shop_id;
	}

	/**
	 * @return the sub_taobao_user_id
	 */
	public String getSub_taobao_user_id() {
		return sub_taobao_user_id;
	}

	/**
	 * @param sub_taobao_user_id the sub_taobao_user_id to set
	 */
	public void setSub_taobao_user_id(final String sub_taobao_user_id) {
		this.sub_taobao_user_id = sub_taobao_user_id;
	}

	/**
	 * @return the sub_taobao_user_nick
	 */
	public String getSub_taobao_user_nick() {
		return sub_taobao_user_nick;
	}

	/**
	 * @param sub_taobao_user_nick the sub_taobao_user_nick to set
	 */
	public void setSub_taobao_user_nick(final String sub_taobao_user_nick) {
		this.sub_taobao_user_nick = sub_taobao_user_nick;
	}

	/**
	 * @return the visitor_id
	 */
	public String getVisitor_id() {
		return visitor_id;
	}

	/**
	 * @param visitor_id the visitor_id to set
	 */
	public void setVisitor_id(final String visitor_id) {
		this.visitor_id = visitor_id;
	}

	/**
	 * @return the visitor_nick
	 */
	public String getVisitor_nick() {
		return visitor_nick;
	}

	/**
	 * @param visitor_nick the visitor_nick to set
	 */
	public void setVisitor_nick(final String visitor_nick) {
		this.visitor_nick = visitor_nick;
	}

	/**
	 * @return the isSubuser
	 */
	public boolean isSubuser() {
		return isSubuser;
	}

	/**
	 * @param isSubuser the isSubuser to set
	 */
	public void setSubuser(final boolean isSubuser) {
		this.isSubuser = isSubuser;
	}

}
