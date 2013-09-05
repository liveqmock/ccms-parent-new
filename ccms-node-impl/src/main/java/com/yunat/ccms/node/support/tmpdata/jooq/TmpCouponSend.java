package com.yunat.ccms.node.support.tmpdata.jooq;

import org.jooq.Record;
import org.jooq.TableField;

public class TmpCouponSend extends AbstractJooqTmplTable {

	private static final long serialVersionUID = -3938611143832673442L;

	public static final TmpCouponSend TMP_COUPON_SEND = new TmpCouponSend();

	public final TableField<Record, String> UNI_ID = createField("uni_id", org.jooq.impl.SQLDataType.VARCHAR, this);

	public final TableField<Record, String> NICK = createField("nick", org.jooq.impl.SQLDataType.VARCHAR, this);

	public TmpCouponSend() {
		super(TemplateTablePrefix.TMP_COUPON_SEND, com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TmpCouponSend(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TmpCouponSend(java.lang.String alias, TmpCouponSend table) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, table);
	}

	@Override
	public TmpCouponSend as(java.lang.String alias) {
		return new TmpCouponSend(alias, this);
	}

	@Override
	public String getDDL() {
		StringBuffer ddl = new StringBuffer();
		ddl.append(
				"CREATE TABLE tmp_ec_send_? ("
			+	"uni_id varchar(100) ,"
			+	"nick varchar(100) ,"
			+	"UNIQUE index_tmp_ec_nick (uni_id)"
			+	")"
		);
		return ddl.toString();
	}
}
