package com.yunat.ccms.tradecenter.support.cons;

public interface AffairCons {

	/**
	 * 事务重要性
	 */
	public final static String[] AFFAIR_IMPORTANT = {"","低","中","高"};

	/**
	 * 事务状态
	 */
	public final static String[] AFFAIR_STATUS = {"","待解决","解决中","已解决","已关闭"};

	/**
	 * 信用等级
	 */
	public final static String[] CREDIT_LEVEL = {"无"
		,"1心","2心","3心","4心","5心"
		,"1钻","2钻","3钻","4钻","5钻"
		,"1皇冠","2皇冠","3皇冠","4皇冠","5皇冠"
		,"1金冠","2金冠","3金冠","4金冠","5金冠"};

	/**
	 * 淘宝交易备注旗帜颜色
	 * http://api.taobao.com/apidoc/api.htm?spm=0.0.0.0.VCbsAz_0.0.0.0.i2YnUp&path=cid:5-apiId:48
	 */
	public final static String[] TRADE_MEMO_FLAG_COLOR = {"gray","red","yellow","green","blue","pink"};
}
