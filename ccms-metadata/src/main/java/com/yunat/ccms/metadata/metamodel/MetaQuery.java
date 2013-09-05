package com.yunat.ccms.metadata.metamodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections15.map.ListOrderedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.yunat.ccms.core.support.utils.HStringUtils;
import com.yunat.ccms.metadata.ApplicationContextUtil;
import com.yunat.ccms.metadata.Faceable;
import com.yunat.ccms.metadata.Persistable;
import com.yunat.ccms.metadata.Sqlable;
import com.yunat.ccms.metadata.face.FaceAttribute;
import com.yunat.ccms.metadata.face.FaceCriteria;
import com.yunat.ccms.metadata.face.FaceQuery;
import com.yunat.ccms.metadata.pojo.Query;
import com.yunat.ccms.metadata.pojo.QueryColumn;
import com.yunat.ccms.metadata.pojo.QueryJoinCriteria;
import com.yunat.ccms.metadata.pojo.QueryTable;
import com.yunat.ccms.metadata.repository.QueryDao;

/**
 * 元数据：查询，对应一个完整的查询语句
 * 
 * @author kevin.jiang 2013-3-13
 */
public class MetaQuery implements Sqlable, Faceable<MetaQuery, FaceQuery>, Persistable<MetaQuery, Query> {

	Logger logger = Logger.getLogger(this.getClass());

	private Long id;
	private String code;
	private String name;
	private String platCode;
	private EnumRelation relation = EnumRelation.AND;

	private List<MetaAttribute> attributes = new ArrayList<MetaAttribute>();
	private MetaEntity masterEntity;
	private List<MetaQueryJoin> joins = new ArrayList<MetaQueryJoin>();
	private List<MetaCriteria> criterias = new ArrayList<MetaCriteria>();
	private Boolean useAlias = false;
	private Boolean distinct = false;

	private static QueryDao queryDao;

	static {

		queryDao = (QueryDao) ApplicationContextUtil.getContext().getBean("queryDao");
	}

	public MetaQuery() {

	}

	public MetaQuery(Boolean distinct) {

		this.distinct = distinct;
	}

	public void addAttribute(MetaAttribute attribute) {
		try {

			if (this.attributes == null) {
				this.attributes = new ArrayList<MetaAttribute>();
			}
			this.attributes.add(attribute);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static MetaQuery getInstance() {

		return new MetaQuery();
	}

	public MetaEntity getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(MetaEntity masterEntity) {
		this.masterEntity = masterEntity;
	}

	public Boolean getUseAlias() {
		return useAlias;
	}

	public void setUseAlias(Boolean useAlias) {
		this.useAlias = useAlias;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EnumRelation getRelation() {
		return relation;
	}

	public void setRelation(EnumRelation relation) {
		this.relation = relation;
	}

	public List<MetaCriteria> getCriterias() {
		return criterias;
	}

	public void setCriterias(List<MetaCriteria> criterias) {
		this.criterias = criterias;
	}

	public void addCriteria(MetaCriteria criteria) {

		if (this.criterias == null) {
			this.criterias = new ArrayList<MetaCriteria>();
		}
		this.criterias.add(criteria);

	}

	/**
	 * 手动连接手工指明连接关系，向Joins里增加对象，在生成SQL时检查关系是否成立
	 * 
	 * @param leftAttr
	 * @param entity
	 * @param rightAttr
	 * @param tableJoin
	 * @throws Exception
	 */
	public void manualJoinEntity(MetaAttribute leftAttr, MetaEntity entity, MetaAttribute rightAttr,
			EnumTableJoin tableJoin) throws Exception {

		MetaEntity leftEntity = leftAttr.getMetaEntity();
		if (leftEntity == null) {
			throw new Exception("table of left attribute can't be null.");
		}
		if (leftEntity.getTableName() == null || "".equals(leftEntity.getTableName())) {
			throw new Exception("table name of left attribute can't be null.");
		}

		if (!rightAttr.getMetaEntity().getTableName().equals(entity.getTableName())) {

			throw new Exception("字段不属于这个表");
		}

		if (this.masterEntity == null) {
			this.masterEntity = leftAttr.getMetaEntity();
		}
		joins.add(new MetaQueryJoin(leftAttr, tableJoin, rightAttr));
	}

	@Override
	public MetaQuery loadFromPojo(Query pojo) {

		this.attributes.clear();
		this.criterias.clear();
		this.joins.clear();

		if (pojo.getColumns() != null) {

			Iterator<QueryColumn> it = pojo.getColumns().iterator();
			while (it.hasNext()) {
				MetaAttribute attribute = ((new MetaAttribute()).loadFromPojo(it.next()));
				this.attributes.add(attribute);
			}
		}

		for (QueryTable table : pojo.getTables()) {

			if (table.getIsMaster()) {

				MetaEntity entity = (new MetaEntity()).loadFromPojo(table);
				this.masterEntity = entity;
				break;
			}
		}

		// 查询是在查询节点的配置Service中，动态添加的

		for (QueryJoinCriteria join : pojo.getJoins()) {
			MetaQueryJoin meta_join = (new MetaQueryJoin()).loadFromPojo(join);
			this.joins.add(meta_join);
		}

		this.code = pojo.getCode();
		this.name = pojo.getShowName();
		this.platCode = pojo.getPlatCode();
		return this;
	}

	@Override
	public Query genPojo() {

		return queryDao.findQueryByCode(this.code);
	}

	@Override
	public MetaQuery parseFace(FaceQuery face) {

		this.attributes.clear();
		this.criterias.clear();
		this.joins.clear();

		// 字段
		for (FaceAttribute faceAttr : face.getAttrs()) {

			MetaAttribute metaAttr = new MetaAttribute();
			metaAttr.parseFace(faceAttr);
			this.attributes.add(metaAttr);
		}

		// 由于界面上相对日期很难计算前后，容易弄混，后台增加一个处理：
		// 比较开始订单开始时间和结束时间，如果开始时间比结束时间晚，则自动更换
		if (face.getCons() != null && face.getCons().get("OrderStartTime") != null
				&& face.getCons().get("OrderEndTime").getValues() != null) {

			String sStartTime = EnumQueryType.DATETIME.parseJsonValue(face.getCons().get("OrderStartTime").getValues());
			String sEndTime = EnumQueryType.DATETIME.parseJsonValue(face.getCons().get("OrderEndTime").getValues());

			if (sStartTime != null && sEndTime != null) {

				Date startTime = HStringUtils.parseDateTime(sStartTime);
				Date endTime = HStringUtils.parseDateTime(sEndTime);
				String tempValue = null;

				if (startTime.getTime() > endTime.getTime()) {
					tempValue = face.getCons().get("OrderStartTime").getValues();
					face.getCons().get("OrderStartTime").setValues(face.getCons().get("OrderEndTime").getValues());
					face.getCons().get("OrderEndTime").setValues(tempValue);
				}
			}
		}

		// 条件
		for (Entry<String, FaceCriteria> entry : face.getCons().entrySet()) {

			FaceCriteria faceCriteria = entry.getValue();

			if (faceCriteria == null || faceCriteria.getOp() == null) {
				continue;
			}

			// 由于前端动态控制是否传递一个条件很麻烦，后端如果发现前端没传字段标识，操作符,或者没传值，就认为这个条件可忽略
			// TODO
			// 这里的代码都需要挪到别的类里面，为了改掉IE8下的BUG，先在这里处理。注意这里的"null"。判断合不合法，应该是MetaCriteria的职责。
			if (StringUtils.isEmpty(faceCriteria.getKey()) || "null".equals(faceCriteria.getKey())) {
				continue;
			}

			if (StringUtils.isEmpty(faceCriteria.getOp().getValue()) || "null".equals(faceCriteria.getOp().getValue())) {
				continue;
			}

			if (StringUtils.isEmpty(faceCriteria.getValues()) || "null".equals(faceCriteria.getValues())) {
				continue;
			}

			MetaCriteria metaCriteria = new MetaCriteria();
			metaCriteria.parseFace(entry.getValue());
			metaCriteria.setUiCode(entry.getKey());
			this.criterias.add(metaCriteria);
		}

		if (face.getId() != null && !"".equals(face.getId().trim())) {
			this.id = Long.valueOf(face.getId());
		}
		this.code = face.getCode();
		this.name = face.getName();
		this.platCode = face.getPlat();
		if (face.getRelation() != null && !"".equals(face.getRelation())) {
			this.relation = EnumRelation.valueOf(face.getRelation());
		}
		return this;
	}

	@Override
	public FaceQuery genFace() {

		FaceQuery faceQuery = new FaceQuery();

		// 字段
		List<FaceAttribute> faceAttrs = new ArrayList<FaceAttribute>();
		for (MetaAttribute metaAttr : this.attributes) {

			faceAttrs.add(metaAttr.genFace());
		}
		faceQuery.setAttrs(faceAttrs);

		// 条件
		Map<String, FaceCriteria> mapCons = new ListOrderedMap<String, FaceCriteria>();
		for (MetaCriteria metaCriteria : this.criterias) {

			mapCons.put(metaCriteria.getUiCode(), metaCriteria.genFace());
		}
		faceQuery.setCons(mapCons);

		faceQuery.setId(String.valueOf(this.id));
		faceQuery.setCode(this.code);
		faceQuery.setName(this.name);
		faceQuery.setPlat(this.platCode);
		faceQuery.setRelation(this.relation.name());
		return faceQuery;
	}

	public String toSql(Boolean useAlias) {

		MetaEntityRegister register = new MetaEntityRegister();

		try {

			StringBuilder builder = new StringBuilder("select ");

			if (this.distinct) {

				builder.append("distinct ");
			}

			for (MetaAttribute attribute : this.attributes) {

				builder.append(attribute.toSql(useAlias, register));
				if (attributes.indexOf(attribute) >= 0 && attributes.indexOf(attribute) < (attributes.size() - 1)) {
					builder.append(", ");
				}
			}

			builder.append(" from ");

			if (this.masterEntity != null) {

				register.findAndRegistEntityByName(this.masterEntity);
				builder.append(this.masterEntity.toSql(useAlias, register));
			}

			if (this.joins != null) {

				for (MetaQueryJoin join : this.joins) {

					builder.append(join.toSql(useAlias, register));
				}
			}

			List<MetaCriteria> quotaCriteriaList = new ArrayList<MetaCriteria>(); // 指标，用来生成having和group
			List<MetaCriteria> criteriaList = new ArrayList<MetaCriteria>(); // 第一层的查询条件（最多支持两层，第二层放在map里）
			Map<String, List<MetaCriteria>> secondLevelMap = new HashMap<String, List<MetaCriteria>>();

			for (MetaCriteria criteria : this.criterias) {

				if (criteria.getAttribute().getIsQuotaColumn()) {
					quotaCriteriaList.add(criteria);
					continue;
				}

				if (criteria.getSubGroup() != null && !"".equals(criteria.getSubGroup())) {

					if (secondLevelMap.get(criteria.getSubGroup()) == null) {

						List<MetaCriteria> list = new ArrayList<MetaCriteria>();
						list.add(criteria);
						secondLevelMap.put(criteria.getSubGroup(), list);
					} else {

						secondLevelMap.get(criteria.getSubGroup()).add(criteria);
					}
					continue;
				}
				criteriaList.add(criteria);
			}

			// 拼接where第一层的条件
			if (criteriaList.size() > 0 || secondLevelMap.size() > 0) {
				builder.append(" where ");
			}

			for (MetaCriteria criteria : criteriaList) {
				builder.append(criteria.toSql(useAlias, register));
				if (criteriaList.indexOf(criteria) >= 0 && criteriaList.indexOf(criteria) < (criteriaList.size() - 1)) {
					builder.append(criteria.getRelation().toSql());
				}
			}

			// 拼接where第二层的条件
			for (Map.Entry<String, List<MetaCriteria>> entry : secondLevelMap.entrySet()) {

				List<MetaCriteria> list = entry.getValue();
				if (list != null && list.size() > 0) {

					builder.append(EnumRelation.AND.toSql()).append("(");
					for (MetaCriteria mc : list) {

						if (list.indexOf(mc) != 0) {
							builder.append(mc.getRelation().toSql());
						}
						builder.append(mc.toSql(useAlias, register));
					}
					builder.append(")");
				}
			}

			Map<String, MetaAttribute> groupMap = new HashMap<String, MetaAttribute>();

			StringBuilder having = new StringBuilder("");

			if (quotaCriteriaList != null && quotaCriteriaList.size() > 0) {

				having.append(" having ");
				for (MetaCriteria quotaCriteria : quotaCriteriaList) {

					String groupExpr = quotaCriteria.getQuotaType().getGroup();
					String[] array = groupExpr.split("[.]");
					String tableExpr = array[0];
					String columnExpr = array[1];
					int tableIndex = Integer.parseInt(tableExpr.substring(tableExpr.length() - 1));
					MetaEntity entity = new MetaEntity(quotaCriteria.getQuotaType().getOrderedTableNameList()
							.get(tableIndex));

					MetaAttribute attr = new MetaAttribute(entity, columnExpr);
					groupMap.put(attr.getColumnName(), attr);

					having.append(quotaCriteria.toSql(useAlias, register));
					if (quotaCriteriaList.indexOf(quotaCriteria) >= 0
							&& quotaCriteriaList.indexOf(quotaCriteria) < (quotaCriteriaList.size() - 1)) {
						having.append(EnumRelation.AND.toSql());
					}
				}
			}

			// Group
			List<MetaAttribute> groups = new ArrayList<MetaAttribute>(groupMap.values());

			if (groups != null && groups.size() > 0) {

				builder.append(" group by ");
				for (MetaAttribute group : groups) {

					builder.append(group.toSql(useAlias, register));
					if (groups.indexOf(group) >= 0 && groups.indexOf(group) < (groups.size() - 1)) {
						builder.append(", ");
					}
				}
			}

			// Having
			builder.append(having.toString());

			logger.info(builder.toString());
			return builder.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
