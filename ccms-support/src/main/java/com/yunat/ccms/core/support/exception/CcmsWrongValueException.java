package com.yunat.ccms.core.support.exception;

public class CcmsWrongValueException extends CcmsRuntimeException {

	private static final long serialVersionUID = -4457955676564907830L;

	public final String itemName;

	public CcmsWrongValueException(final String itemName, final Throwable t) {
		super(itemName + "错误。", t);
		this.itemName = itemName;
	}

	public CcmsWrongValueException(final String itemName) {
		this(itemName, null);
	}

	public String getItemName() {
		return itemName;
	}

}
