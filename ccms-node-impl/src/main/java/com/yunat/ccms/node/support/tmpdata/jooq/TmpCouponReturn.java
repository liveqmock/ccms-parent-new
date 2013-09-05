package com.yunat.ccms.node.support.tmpdata.jooq;

import org.jooq.Record;
import org.jooq.TableField;

public class TmpCouponReturn extends AbstractJooqTmplTable {

	private static final long serialVersionUID = -3938611143832673442L;

	public static final TmpCouponReturn TMP_COUPON_RETURN = new TmpCouponReturn();

	public final TableField<Record, String> NICK = createField("nick", org.jooq.impl.SQLDataType.VARCHAR, this);
	
	public final TableField<Record, Long> STATUS = createField("status", org.jooq.impl.SQLDataType.BIGINT, this);


	public TmpCouponReturn() {
		super(TemplateTablePrefix.TMP_COUPON_RETURN, com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TmpCouponReturn(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TmpCouponReturn(java.lang.String alias, TmpCouponReturn table) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, table);
	}

	@Override
	public TmpCouponReturn as(java.lang.String alias) {
		return new TmpCouponReturn(alias, this);
	}

	@Override
	public String getDDL() {
		StringBuffer ddl = new StringBuffer();
		ddl.append(
				"CREATE TABLE tmp_ec_return_? ("
			+	"nick varchar(100) ,"
			+	"status char(5) ,"
			+	"UNIQUE index_tmp_ec_return_nick (nick)"
			+	")"
		);
		return ddl.toString();
	}
}
