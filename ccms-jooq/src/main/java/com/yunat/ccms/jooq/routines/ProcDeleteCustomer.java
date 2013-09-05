/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.routines;

/**
 * This class is generated by jOOQ.
 *
 * 删除客户相关平台信息
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class ProcDeleteCustomer extends org.jooq.impl.AbstractRoutine<java.lang.Void> {

	private static final long serialVersionUID = -145083515;


	/**
	 * The procedure parameter <code>ccms.proc_delete_customer.p_plat_code</code>
	 */
	public static final org.jooq.Parameter<java.lang.String> P_PLAT_CODE = createParameter("p_plat_code", org.jooq.impl.SQLDataType.CHAR);

	/**
	 * The procedure parameter <code>ccms.proc_delete_customer.p_customerno</code>
	 */
	public static final org.jooq.Parameter<java.lang.String> P_CUSTOMERNO = createParameter("p_customerno", org.jooq.impl.SQLDataType.VARCHAR);

	/**
	 * Create a new routine call instance
	 */
	public ProcDeleteCustomer() {
		super("proc_delete_customer", com.yunat.ccms.jooq.Ccms.CCMS);

		addInParameter(P_PLAT_CODE);
		addInParameter(P_CUSTOMERNO);
	}

	/**
	 * Set the <code>p_plat_code</code> parameter IN value to the routine
	 */
	public void setPPlatCode(java.lang.String value) {
		setValue(com.yunat.ccms.jooq.routines.ProcDeleteCustomer.P_PLAT_CODE, value);
	}

	/**
	 * Set the <code>p_customerno</code> parameter IN value to the routine
	 */
	public void setPCustomerno(java.lang.String value) {
		setValue(com.yunat.ccms.jooq.routines.ProcDeleteCustomer.P_CUSTOMERNO, value);
	}
}
