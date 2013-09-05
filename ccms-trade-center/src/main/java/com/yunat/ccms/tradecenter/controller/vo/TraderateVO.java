package com.yunat.ccms.tradecenter.controller.vo;


/**
 * 对应评价事务前台查询条件对象
 * 
 * @author tim.yin
 * 
 */
public class TraderateVO {

	// 店铺ID
	private String shopId;

	// 评价内容
	private String content;

	// 会员等级
	private String memberLevel;

	// 是否关怀
	private Boolean isRegardFlag;

	// 是否解释
	private Boolean isExplainFlag;

	// 商品名称
	private String itemTitle;

	// 评价开始时间
	private String beginCreated;

	// 评价截止时间
	private String endCreated;

	// 评价类型
	private String result;

	// 是否追加回复
	private Boolean isApand;

	// 客户昵称
	private String nick;

	// 当前页数
	private int currPage;
	// 每页条数
	private int pageSize;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(String memberLevel) {
		this.memberLevel = memberLevel;
	}

	public Boolean getIsRegardFlag() {
		return isRegardFlag;
	}

	public void setIsRegardFlag(Boolean isRegardFlag) {
		this.isRegardFlag = isRegardFlag;
	}

	public Boolean getIsExplainFlag() {
		return isExplainFlag;
	}

	public void setIsExplainFlag(Boolean isExplainFlag) {
		this.isExplainFlag = isExplainFlag;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public String getBeginCreated() {
		return beginCreated;
	}

	public void setBeginCreated(String beginCreated) {
		this.beginCreated = beginCreated;
	}

	public String getEndCreated() {
		return endCreated;
	}

	public void setEndCreated(String endCreated) {
		this.endCreated = endCreated;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Boolean getIsApand() {
		return isApand;
	}

	public void setIsApand(Boolean isApand) {
		this.isApand = isApand;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
