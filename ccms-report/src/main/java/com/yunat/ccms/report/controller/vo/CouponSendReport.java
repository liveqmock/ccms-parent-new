package com.yunat.ccms.report.controller.vo;

/**
 * 优惠券发送报告
 * 
 * @author xiaojing.qu
 * 
 */
public class CouponSendReport {

	private boolean showReport;
	private String reportUrl;

	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}

	public boolean isShowReport() {
		return showReport;
	}

	public void setShowReport(boolean showReport) {
		this.showReport = showReport;
	}

}
