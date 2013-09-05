package com.yunat.ccms.rule.center.conf;

import com.yunat.ccms.rule.center.RuleCenterRuntimeException;

public class ContentTooLongException extends RuleCenterRuntimeException {

	private static final long serialVersionUID = -2042732487462440205L;

	public ContentTooLongException(final String itemName, final String content, final int maxLen, final Throwable t) {
		super(itemName + "“" + content + "”" + "过长，请保持在" + maxLen + "个字以内。", t);
	}

	public ContentTooLongException(final String itemName, final String content, final int maxLen) {
		this(itemName, content, maxLen, null);
	}

}
