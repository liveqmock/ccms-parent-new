package com.yunat.ccms.metadata.metamodel;

import com.yunat.ccms.metadata.ApplicationContextUtil;
import com.yunat.ccms.metadata.Persistable;
import com.yunat.ccms.metadata.Sqlable;
import com.yunat.ccms.metadata.pojo.QueryJoinCriteria;
import com.yunat.ccms.metadata.repository.DatabaseColumnDao;

/**
 * 元数据：表关联关系，对应From语句
 * 
 * @author kevin.jiang 2013-3-14
 */
public class MetaQueryJoin implements Sqlable, Persistable<MetaQueryJoin, QueryJoinCriteria> {

	private MetaAttribute leftAttribute;
	private EnumTableJoin join;
	private MetaAttribute rightAttribute;

	private static DatabaseColumnDao databaseColumnDao;

	static {

		databaseColumnDao = (DatabaseColumnDao) ApplicationContextUtil.getContext().getBean("databaseColumnDao");
	}

	public MetaQueryJoin() {

	}

	public MetaQueryJoin(MetaAttribute leftAttribute, EnumTableJoin join, MetaAttribute rightAttribute) {

		this.leftAttribute = leftAttribute;
		this.join = join;
		this.rightAttribute = rightAttribute;
	}

	protected MetaAttribute getLeftAttribute() {
		return leftAttribute;
	}

	protected MetaAttribute getRightAttribute() {
		return rightAttribute;
	}

	protected void setLeftAttribute(MetaAttribute leftAttribute) {
		this.leftAttribute = leftAttribute;
	}

	protected void setRightAttribute(MetaAttribute rightAttribute) {
		this.rightAttribute = rightAttribute;
	}

	@Override
	public MetaQueryJoin loadFromPojo(QueryJoinCriteria pojo) {

		this.leftAttribute = new MetaAttribute(new MetaEntity(pojo.getLeftColumn().getTable()), pojo.getLeftColumn()
				.getColumnName());
		this.rightAttribute = new MetaAttribute(new MetaEntity(pojo.getRightColumn().getTable()), pojo.getRightColumn()
				.getColumnName());
		this.join = EnumTableJoin.valueOf(pojo.getJoinType());
		return this;
	}

	@Override
	public QueryJoinCriteria genPojo() {

		QueryJoinCriteria queryJoin = new QueryJoinCriteria();
		queryJoin.setLeftColumn(databaseColumnDao.findOne(this.leftAttribute.getColumnId()));
		queryJoin.setRightColumn(databaseColumnDao.findOne(this.rightAttribute.getColumnId()));
		queryJoin.setJoinType(this.join.name());
		return queryJoin;
	}

	public String toSql(Boolean useAlias, MetaEntityRegister register) {

		return (new StringBuilder()).append(" ").append(join.toSql())
				.append(rightAttribute.getMetaEntity().toSql(useAlias, register)).append(" on ")
				.append(leftAttribute.toSql(useAlias, register)).append(" = ")
				.append(rightAttribute.toSql(useAlias, register)).toString();
	}
}
