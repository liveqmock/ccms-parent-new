package com.yunat.ccms.report.support.cons;

/**
 * 报表文件描述
 * 
 * @author xiaojing.qu
 * 
 */
public class ReportFileSpec {

	/**
	 * 业务类型
	 * 
	 * @author xiaojing.qu
	 * 
	 */
	public enum Category {
		TRADE, ORDER, PRODUCT, CUSTOMER, MAPING_ORDER, EVALUATE_REPORT_CUSTOMER, EVALUATE_REPORT_ORDER, EVALUATE_REPORT_PRODUCT;
	}

	/**
	 * 文件扩展名
	 */
	public enum Extension {
		JSON, CSV, ZIP;
	}

}
