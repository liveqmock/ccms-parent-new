/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.routines;

/**
 * This class is generated by jOOQ.
 *
 * 返回元数据字典项显示值
 * 参数: 字典ID, 字典项值
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class FuncGetDicViewname extends org.jooq.impl.AbstractRoutine<java.lang.String> {

	private static final long serialVersionUID = 224107213;


	/**
	 * The procedure parameter <code>ccms.func_get_dic_viewname.RETURN_VALUE</code>
	 */
	public static final org.jooq.Parameter<java.lang.String> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.VARCHAR);

	/**
	 * The procedure parameter <code>ccms.func_get_dic_viewname.p_dic_type_id</code>
	 */
	public static final org.jooq.Parameter<java.lang.Integer> P_DIC_TYPE_ID = createParameter("p_dic_type_id", org.jooq.impl.SQLDataType.INTEGER);

	/**
	 * The procedure parameter <code>ccms.func_get_dic_viewname.p_type_value</code>
	 */
	public static final org.jooq.Parameter<java.lang.String> P_TYPE_VALUE = createParameter("p_type_value", org.jooq.impl.SQLDataType.VARCHAR);

	/**
	 * Create a new routine call instance
	 */
	public FuncGetDicViewname() {
		super("func_get_dic_viewname", com.yunat.ccms.jooq.Ccms.CCMS, org.jooq.impl.SQLDataType.VARCHAR);

		setReturnParameter(RETURN_VALUE);
		addInParameter(P_DIC_TYPE_ID);
		addInParameter(P_TYPE_VALUE);
	}

	/**
	 * Set the <code>p_dic_type_id</code> parameter IN value to the routine
	 */
	public void setPDicTypeId(java.lang.Integer value) {
		setValue(com.yunat.ccms.jooq.routines.FuncGetDicViewname.P_DIC_TYPE_ID, value);
	}

	/**
	 * Set the <code>p_dic_type_id</code> parameter to the function
	 * <p>
	 * Use this method only, if the function is called as a {@link org.jooq.Field} in a {@link org.jooq.Select} statement!
	 */
	public void setPDicTypeId(org.jooq.Field<java.lang.Integer> field) {
		setField(P_DIC_TYPE_ID, field);
	}

	/**
	 * Set the <code>p_type_value</code> parameter IN value to the routine
	 */
	public void setPTypeValue(java.lang.String value) {
		setValue(com.yunat.ccms.jooq.routines.FuncGetDicViewname.P_TYPE_VALUE, value);
	}

	/**
	 * Set the <code>p_type_value</code> parameter to the function
	 * <p>
	 * Use this method only, if the function is called as a {@link org.jooq.Field} in a {@link org.jooq.Select} statement!
	 */
	public void setPTypeValue(org.jooq.Field<java.lang.String> field) {
		setField(P_TYPE_VALUE, field);
	}
}
