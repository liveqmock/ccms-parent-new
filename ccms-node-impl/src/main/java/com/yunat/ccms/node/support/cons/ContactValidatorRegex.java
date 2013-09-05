package com.yunat.ccms.node.support.cons;

public enum ContactValidatorRegex {
	JAVA_REGEX_MOBILE("^(13[0-9]|14[0-9]|15[0-9]|18[0-9]){1}\\d{8}$"), 
	JAVA_REGEX_EMAIL("^[a-zA-Z0-9_\\.\\-]+\\@([a-zA-Z0-9\\-]+\\.)+[a-zA-Z0-9]{2,4}$"), 
	MYSQL_REGEX_MOBILE("'^(1[3,4,5,8]){1}[0-9]{9}$'");
	
	private String regexExpression;

	private ContactValidatorRegex(String _regexExpression) {
		this.regexExpression = _regexExpression;
	}

	public String getRegexExpression() {
		return regexExpression;
	}

	public void setRegexExpression(String regexExpression) {
		this.regexExpression = regexExpression;
	}

}