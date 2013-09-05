package com.yunat.ccms.metadata.metamodel;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunat.ccms.metadata.ApplicationContextUtil;
import com.yunat.ccms.metadata.Faceable;
import com.yunat.ccms.metadata.Persistable;
import com.yunat.ccms.metadata.Sqlable;
import com.yunat.ccms.metadata.face.FaceCriteria;
import com.yunat.ccms.metadata.face.FaceOperator;
import com.yunat.ccms.metadata.pojo.DicType;
import com.yunat.ccms.metadata.pojo.QueryCriteria;
import com.yunat.ccms.metadata.quota.Quota;
import com.yunat.ccms.metadata.quota.QuotaHelper;
import com.yunat.ccms.metadata.repository.QueryCriteriaDao;

/**
 * 元数据：查询条件（属性 + 操作符 + 目标值的三元表达式）
 * 
 * @author kevin.jiang 2013-2-27
 */
public class MetaCriteria implements Sqlable, Faceable<MetaCriteria, FaceCriteria>,
		Persistable<MetaCriteria, QueryCriteria> {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private Long id; // NodeQueryCriteria的主键ID
	private MetaAttribute attribute;
	private MetaOperator operator;
	private String targetValues;
	private String column_expr;
	private EnumQueryType queryType;
	private Quota quotaType;
	private Long key; // QueryCriteria的主键ID
	private String subGroup;
	private EnumRelation relation = EnumRelation.AND;
	private String uiCode;
	private DicType dic;

	private static QueryCriteriaDao queryCriteriaDao;

	static {

		queryCriteriaDao = (QueryCriteriaDao) ApplicationContextUtil.getContext().getBean("queryCriteriaDao");
	}

	public MetaCriteria() {

	}

	public MetaCriteria(MetaAttribute attribute, MetaOperator operator, String targetValues) {
		this.attribute = attribute;
		this.operator = operator;
		this.targetValues = targetValues;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public MetaAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(MetaAttribute attribute) {
		this.attribute = attribute;
	}

	public MetaOperator getOperator() {
		return operator;
	}

	public void setOperator(MetaOperator operator) {
		this.operator = operator;
	}

	public String getTargetValues() {
		return targetValues;
	}

	public void setTargetValues(String targetValues) {
		this.targetValues = targetValues;
	}

	public String getColumn_expr() {
		return column_expr;
	}

	public void setColumn_expr(String column_expr) {
		this.column_expr = column_expr;
	}

	public EnumQueryType getQueryType() {
		return queryType;
	}

	public void setQueryType(EnumQueryType queryType) {
		this.queryType = queryType;
	}

	public Quota getQuotaType() {
		return quotaType;
	}

	public void setQuotaType(Quota quotaType) {
		this.quotaType = quotaType;
	}

	public String getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(String subGroup) {
		this.subGroup = subGroup;
	}

	public EnumRelation getRelation() {
		return relation;
	}

	public void setRelation(EnumRelation relation) {
		this.relation = relation;
	}

	public String getUiCode() {
		return uiCode;
	}

	public void setUiCode(String uiCode) {
		this.uiCode = uiCode;
	}

	@Override
	public MetaCriteria loadFromPojo(QueryCriteria pojo) {

		this.key = pojo.getQueryCriteriaId();
		this.column_expr = pojo.getColumnExpr();
		if (EnumQueryType.QUOTA.name().equals(pojo.getQueryType())) {

			this.queryType = EnumQueryType.QUOTA;

			try {
				this.quotaType = QuotaHelper.convertToEnum(pojo.getQuotaType());
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.attribute = new MetaAttribute(this.quotaType);

		} else {

			this.attribute = new MetaAttribute(new MetaEntity(pojo.getDatabaseColumn().getTable()), pojo
					.getDatabaseColumn().getColumnName());
			this.attribute.setKey(pojo.getDatabaseColumn().getColumnId());
			this.attribute.setShowName(pojo.getDatabaseColumn().getShowName());
			this.dic = pojo.getDatabaseColumn().getDicType();
			this.queryType = EnumQueryType.valueOf(pojo.getQueryType());
		}

		return this;
	}

	@Override
	public QueryCriteria genPojo() {

		return queryCriteriaDao.findOne(this.key);
	}

	@Override
	public MetaCriteria parseFace(FaceCriteria face) {

		this.targetValues = face.getValues();

		QueryCriteria qc = queryCriteriaDao.findOne(Long.valueOf(face.getKey()));
		if (EnumQueryType.QUOTA.name().equals(qc.getQueryType())) {

			try {
				this.quotaType = QuotaHelper.convertToEnum(qc.getQuotaType());
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.attribute = new MetaAttribute(this.quotaType);

		} else {

			MetaEntity entity = new MetaEntity(qc.getDatabaseColumn().getTable().getDbName());
			this.attribute = new MetaAttribute(entity, qc.getDatabaseColumn().getColumnName());
			this.attribute.setColumnId(qc.getDatabaseColumn().getColumnId());
			this.dic = qc.getDatabaseColumn().getDicType();
		}

		this.operator = face.getOp() == null ? null : face.getOp().toMetaOperator();
		this.subGroup = face.getGroup();
		if (!StringUtils.isEmpty(face.getRelation())) {
			this.relation = EnumUtils.isValidEnum(EnumRelation.class, face.getRelation()) ? EnumRelation.valueOf(face
					.getRelation()) : null;
		}

		if (NumberUtils.isDigits(face.getId())) {
			this.id = Long.valueOf(face.getId());
		}
		this.key = Long.valueOf(face.getKey());
		return this;
	}

	@Override
	public FaceCriteria genFace() {

		FaceCriteria con = new FaceCriteria();
		con.setId(String.valueOf(this.id));

		// 属性
		con.setKey(String.valueOf(this.key));
		con.setLabelName(this.attribute.getShowName());

		// 预算符
		FaceOperator faceOper = new FaceOperator();
		faceOper.setValue(this.operator.name());
		faceOper.setName(this.operator.getChineseName());
		con.setOp(faceOper);

		// 目标值
		con.setValues(this.targetValues);
		con.setGroup(this.subGroup);
		con.setRelation(this.relation.name());
		con.setQueryType(this.queryType.name());
		return con;
	}

	public String toSql(Boolean useAlias, MetaEntityRegister register) {

		// 无法通过设计回避的特殊处理，都在这里
		String specialSql = SpecialValueHacker.genSpecialValueSql(this.key, this.queryType, this.dic,
				attribute.toSql(useAlias, register), this.operator, this.targetValues);
		if (specialSql != null) {

			return specialSql;
		}

		/* 生成目标值对应的SQL */

		String finalExpr = this.getQueryType().getSqlExpr();

		// 处理指标
		if (this.getQueryType() == EnumQueryType.QUOTA) {

			finalExpr = this.getQuotaType().getType().getSqlExpr();
		}

		// 处理商品
		if (this.queryType == EnumQueryType.TDS_PRODUCT) {

			register.findAndRegistEntityByName(this.getAttribute().getMetaEntity());
			return this.queryType.parseJsonValue(this.getTargetValues()).replaceFirst("[?]",
					this.getAttribute().getMetaEntity().getTableAlias());
		}

		StringBuilder sql = (new StringBuilder()).append(attribute.toSql(useAlias, register)).append(operator.toSql());

		// 指标条件：值处理
		if (this.queryType == EnumQueryType.QUOTA) {

			finalExpr = finalExpr.replaceFirst("[?]", this.getQuotaType().getType().parseJsonValue(this.targetValues));
			sql.append(finalExpr);
			return sql.toString();
		}
		logger.debug(this.targetValues);

		// 常规条件：值处理
		// 处理LIKE和NOT LIKE
		if (this.getOperator() == MetaOperator.LIKE || this.getOperator() == MetaOperator.NOTLIKE) {

			finalExpr = "'%?%'";
		}

		// 处理ISNULL和NOTNULL
		if (this.getOperator() == MetaOperator.ISNULL || this.getOperator() == MetaOperator.NOTNULL) {

			finalExpr = "";
		}

		finalExpr = finalExpr.replaceFirst("[?]", this.getQueryType().parseJsonValue(this.targetValues));
		sql.append(finalExpr);
		return sql.toString();
	}
}
