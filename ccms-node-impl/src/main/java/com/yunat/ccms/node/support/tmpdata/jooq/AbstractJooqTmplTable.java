package com.yunat.ccms.node.support.tmpdata.jooq;

import org.jooq.Record;
import org.jooq.impl.SchemaImpl;
import org.jooq.impl.TableImpl;

public abstract class AbstractJooqTmplTable extends org.jooq.impl.TableImpl<Record> {

	private static final long serialVersionUID = -6924125088549324592L;

	/**
	 * 模板表类构造方法
	 * @param alias 实际数据库表名
	 */
	public AbstractJooqTmplTable(java.lang.String alias, SchemaImpl schema) {
		super(alias, schema);
	}
	
	/**
	 * 模板表类构造方法
	 * @param prefix 模板表名前缀枚举值
	 */
	public AbstractJooqTmplTable(TemplateTablePrefix prefix, SchemaImpl schema) {
		super(prefix.name().toLowerCase(), schema);
	}
	
	/**
	 * 模板表类构造方法
	 * @param alias 实际数据库表名
	 * @param schema 实际数据库schema名
	 * @param table jooq模板表类
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractJooqTmplTable(java.lang.String alias, SchemaImpl schema, TableImpl table) {
		super(alias, schema, table);
	}

    /**
     * 获取建立实际表的DDL语句
     * @return sql
     */
    abstract public String getDDL();

}