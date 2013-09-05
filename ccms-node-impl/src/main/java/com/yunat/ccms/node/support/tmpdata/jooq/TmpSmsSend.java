package com.yunat.ccms.node.support.tmpdata.jooq;

import org.jooq.Record;
import org.jooq.TableField;

public class TmpSmsSend extends AbstractJooqTmplTable {

	private static final long serialVersionUID = -3938611143832673442L;

	public static final TmpSmsSend TMP_SMS_SEND = new TmpSmsSend();

	public final TableField<Record, String> UNI_ID = createField("uni_id", org.jooq.impl.SQLDataType.VARCHAR, this);

	public final TableField<Record, String> MOBILE = createField("mobile", org.jooq.impl.SQLDataType.VARCHAR, this);

	public final TableField<Record, String> MESSAGE = createField("message", org.jooq.impl.SQLDataType.VARCHAR, this);

	public TmpSmsSend() {
		super(TemplateTablePrefix.TMP_SMS_SEND, com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TmpSmsSend(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TmpSmsSend(java.lang.String alias, TmpSmsSend table) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, table);
	}

	@Override
	public TmpSmsSend as(java.lang.String alias) {
		return new TmpSmsSend(alias, this);
	}

	@Override
	public String getDDL() {
		StringBuffer ddl = new StringBuffer();
		ddl.append(
				"CREATE TABLE tmp_sms_send_? ("
			+	"uni_id varchar(100) ,"
			+	"mobile varchar(100) ,"
			+	"message varchar(500) "
			+	")"
		);
		return ddl.toString();
	}
}
