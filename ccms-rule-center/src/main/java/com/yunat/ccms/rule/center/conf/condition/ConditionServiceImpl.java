package com.yunat.ccms.rule.center.conf.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yunat.ccms.core.support.vo.IdName;
import com.yunat.ccms.metadata.pojo.DatabaseColumn;
import com.yunat.ccms.metadata.pojo.DicType;
import com.yunat.ccms.metadata.pojo.DicTypeValue;
import com.yunat.ccms.metadata.repository.DatabaseColumnDao;
import com.yunat.ccms.metadata.repository.DicTypeValueDao;
import com.yunat.ccms.rule.center.RuleCenterRuntimeException;
import com.yunat.ccms.rule.center.conf.Util;
import com.yunat.ccms.rule.center.conf.rule.Rule;

@Service
public class ConditionServiceImpl implements ConditionService {

	//这两个常量是用来屏蔽 客人等级=未分级 的
	private static final String DIC_VALUE_OF_GRADE0 = "0";
	private static final int DIC_TYPE_ID_OF_GRADE0 = 43;

	@Autowired
	protected ConditionRepository conditionRepository;
	@Autowired
	protected DatabaseColumnDao databaseColumnDao;
	@Autowired
	protected DicTypeValueDao dicTypeValueDao;

	@Override
	public void fillCondition(final Rule... rule) {
		fillCondition(Arrays.asList(rule));
	}

	@Override
	public void fillCondition(final Collection<Rule> rules) {
		if (rules == null || rules.isEmpty()) {
			return;
		}
		final Map<Long, Rule> ruleMap = Maps.newHashMap();
		for (final Rule rule : rules) {
			ruleMap.put(rule.getId(), rule);
			rule.setConditions(new ArrayList<Condition>());
		}
		//取得条件列表
		final List<Condition> conditions = conditionRepository.findByRuleIdIn(ruleMap.keySet());
		final Collection<Long> propertyIds = Sets.newHashSetWithExpectedSize(conditions.size());
		for (final Condition c : conditions) {
			ruleMap.get(c.getRuleId()).getConditions().add(c);
			propertyIds.add(c.getPropertyId());
		}
		//这里是为了setHasProvidedValues.前端需要一个字段来标识这是一个下拉选择的条件还是一个填写的条件.
		final List<DatabaseColumn> databaseColumns = databaseColumnDao.findByColumnIdIn(propertyIds);
		final Map<Long, DatabaseColumn> databaseColumnsMap = Maps.newHashMapWithExpectedSize(databaseColumns.size());
		for (final DatabaseColumn c : databaseColumns) {
			databaseColumnsMap.put(c.getColumnId(), c);
		}
		for (final Condition c : conditions) {
			final DatabaseColumn d = databaseColumnsMap.get(c.getPropertyId());
			c.setHasProvidedValues(d.getDicType() != null);
		}
		//对规则中的条件排序
		for (final Rule r : rules) {
			Collections.sort(r.getConditions(), Util.POSITION_COMPARATOR);
		}
	}

	@Override
	public List<IdName> propertysOfConditionType(final String conditionTypeId) throws RuleCenterRuntimeException {
		Assert.hasText(conditionTypeId, "指标类型不能为空");
		try {
			final ConditionType conditionType = ConditionType.valueOf(conditionTypeId.toUpperCase());
			final long tableId = conditionType.getTableId();
			final List<DatabaseColumn> columns = databaseColumnDao.findByTableTableId(tableId);
			final List<IdName> idNames = Lists.newArrayListWithExpectedSize(columns.size());
			for (final DatabaseColumn column : columns) {
				if (!column.getIsPK()) {
					idNames.add(new IdName(column.getColumnId(), column.getShowName()));
				}
			}
			return idNames;
		} catch (final IllegalArgumentException e) {
			throw new RuleCenterRuntimeException("没有这种指标类型", e);
		}
	}

	@Override
	public ConditionProperty getConditionProperty(final long propertyId) throws RuleCenterRuntimeException {
		final DatabaseColumn column = databaseColumnDao.findOne(propertyId);
		final ConditionProperty cp = new ConditionProperty();
		cp.setId(propertyId);
		cp.setName(column.getShowName());
		final String businessType = column.getBusinessType();
		cp.setType(businessType);

		//支持的操作符
		final Collection<ConditionOp> supportOps = TYPE_CONDITION_OPS_MAPPING.get(businessType.toLowerCase());
		final Collection<Map<String, Object>> supportOpsMaps = Lists.newArrayListWithExpectedSize(supportOps.size());
		for (final ConditionOp op : supportOps) {
			final Map<String, Object> map = Maps.newHashMap();
			map.put("label", op.getLabel());
			map.put("name", op.getName());
			supportOpsMaps.add(map);
		}
		cp.setSupportOps(supportOpsMaps);
		//参考值
		final DicType dicType = column.getDicType();
		if (dicType == null) {
			cp.setProvidedValues(Collections.<ProvidedValues> emptyList());
		} else {
			final List<DicTypeValue> dicValues = dicTypeValueDao.findByDicTypeDicTypeId(dicType.getDicTypeId());
			Collections.sort(dicValues, DIC_TYPE_VALUE_COMPARATOR);

			final List<ProvidedValues> providedValues = Lists.newArrayListWithExpectedSize(dicValues.size());
			for (final DicTypeValue v : dicValues) {
				if (v.getDicType().getDicTypeId() == DIC_TYPE_ID_OF_GRADE0
						&& DIC_VALUE_OF_GRADE0.equals(v.getTypeValue())) {
					//屏蔽会员等级下的"未分级"
				} else {
					final ProvidedValues e = new ProvidedValues();
					e.setId(v.getDicTypeValueId());
					e.setName(v.getShowName());
					e.setValue(v.getTypeValue());
					providedValues.add(e);
				}
			}
			cp.setProvidedValues(providedValues);
		}

		return cp;
	}

	/**
	 * 按主键排序
	 */
	private static final Comparator<DicTypeValue> DIC_TYPE_VALUE_COMPARATOR = new Comparator<DicTypeValue>() {
		@Override
		public int compare(final DicTypeValue o1, final DicTypeValue o2) {
			return o1.getDicTypeValueId().compareTo(o2.getDicTypeValueId());
		}
	};

	private static final Map<String, Collection<ConditionOp>> TYPE_CONDITION_OPS_MAPPING = new HashMap<String, Collection<ConditionOp>>();
	static {
		TYPE_CONDITION_OPS_MAPPING.put("region", EnumSet.of(ConditionOp.LIKE));
		TYPE_CONDITION_OPS_MAPPING.put("string", EnumSet.of(ConditionOp.EQ, ConditionOp.NOT_EQ));
		TYPE_CONDITION_OPS_MAPPING.put("date",
				EnumSet.of(ConditionOp.EQ, ConditionOp.GE, ConditionOp.GT, ConditionOp.LE, ConditionOp.LT));
		TYPE_CONDITION_OPS_MAPPING.put("number",
				EnumSet.of(ConditionOp.EQ, ConditionOp.GE, ConditionOp.GT, ConditionOp.LE, ConditionOp.LT));
		TYPE_CONDITION_OPS_MAPPING.put("product", EnumSet.of(ConditionOp.CONTAINS_ANY));
	}

	@Override
	public Collection<Map<String, Object>> conditionTypeList() {
		final ConditionType[] cts = ConditionType.values();
		final Collection<Map<String, Object>> coll = Lists.newArrayListWithExpectedSize(cts.length);
		for (final ConditionType t : cts) {
			coll.add(t.toMap());
		}
		return coll;
	}

	@Override
	public Collection<ProvidedValues> subRegion(final long regionId) {
		if (regionId <= 0) {
			return Collections.emptyList();
		}
		//咱这里就不检验传进来的regionId是不是表示一个地区了吧?
		//下面这段代码通用.
		final List<DicTypeValue> regions = dicTypeValueDao.findByParentDicTypeValueId(regionId);
		if (regions == null || regions.isEmpty()) {
			return Collections.emptyList();
		}
		final List<ProvidedValues> rt = Lists.newArrayListWithExpectedSize(regions.size());
		for (final DicTypeValue v : regions) {
			final ProvidedValues e = new ProvidedValues();
			e.setId(v.getDicTypeValueId());
			e.setName(v.getShowName());
			e.setValue(v.getTypeValue());
			rt.add(e);
		}
		return rt;
	}
}
