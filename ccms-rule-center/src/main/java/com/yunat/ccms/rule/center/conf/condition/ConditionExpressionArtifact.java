package com.yunat.ccms.rule.center.conf.condition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.utils.ReflectionUtils;
import com.yunat.ccms.rule.center.RuleCenterRuntimeException;
import com.yunat.ccms.rule.center.drl.LHSFragment;
import com.yunat.ccms.rule.center.drl.LHSFragmentBuilder;
import com.yunat.ccms.rule.center.drl.convert.JFieldType;
import com.yunat.ccms.rule.center.drl.convert.LocationConverter;
import com.yunat.ccms.rule.center.drl.convert.PropertyIdConverter;

@Component
public class ConditionExpressionArtifact implements LHSFragmentBuilder<Condition> {
	private final static String BLANK = " ";
	private final static String NULL = "null";

	@Autowired
	private PropertyIdConverter propertyIdConverter;

	@Override
	public LHSFragment toLHS(final Condition condition) {
		final LHSFragment lhs = new LHSFragment();
		lhs.setExpression(bufferBuilder(condition));
		return lhs;
	}

	/**
	 * 生成符合条件的规则串
	 *
	 * @param condition
	 * @return
	 */
	private String bufferBuilder(final Condition condition) {
		final StringBuilder sb = new StringBuilder();
		sb.append(ConditionRelation.valueOfIgnoreCase(condition.getRelation()).getSymbol()).append(BLANK);

		String propertyName = null;
		JFieldType rt = null;
		final ConditionType conditionType = ConditionType.valueOfIgnoreCase(condition.getType());
		if (conditionType.equals(ConditionType.CUSTOMER)) {
			propertyName = propertyIdConverter.propertyId(condition.getPropertyId(),
					ConditionType.CUSTOMER.getTableId());
			rt = JFieldType.valueOf(ReflectionUtils.findField(conditionType.getTypeClass(), propertyName).getType());
			// 重新处理propertyName
			propertyName = ConditionType.CUSTOMER.name().toLowerCase() + "." + propertyName;
		} else if (conditionType.equals(ConditionType.ORDER)) {
			propertyName = propertyIdConverter.propertyId(condition.getPropertyId(), ConditionType.ORDER.getTableId());
			rt = JFieldType.valueOf(ReflectionUtils.findField(conditionType.getTypeClass(), propertyName).getType());
		}

		if (null == propertyName) {
			throw new RuleCenterRuntimeException("propertyName is null, happend exception. type value is "
					+ condition.getType() + ", 值不符合要求。");
		}

		// property != null
		sb.append(propertyName).append(BLANK)//
				.append(ConditionOp.NOT_EQ.getSymbol()).append(BLANK)//
				.append(NULL).append(BLANK);

		// matches change to invoker customer function.
		sb.append(ConditionRelation.AND.getSymbol()).append(BLANK).append("(").append(BLANK);
		if (ConditionOp.valueOfIgnoreCase(condition.getConditionOpName()) != ConditionOp.LIKE) {
			sb.append(propertyName).append(BLANK)//
					.append(ConditionOp.valueOfIgnoreCase(condition.getConditionOpName()).getSymbol()).append(BLANK);
		}

		appendDroolsExpressionValue(condition, rt, propertyName, sb);
		sb.append(")").append(BLANK);
		return sb.toString();
	}

	/**
	 * 根据条件的类型，进行判断值的设置
	 *
	 * @param condition
	 * @param rt
	 * @param propertyName
	 * @param buffer
	 */
	private void appendDroolsExpressionValue(final Condition condition, final JFieldType rt, final String propertyName,
			final StringBuilder buffer) {
		// 操作符后的值
		switch (rt) {
		case STRING_TYPE:
			// matches change to 'LocationConverter.matches(receiverLocation,
			// '吉林')'
			if (ConditionOp.valueOfIgnoreCase(condition.getConditionOpName()) == ConditionOp.LIKE) {
				buffer.append(LocationConverter.class.getSimpleName()).append(".").append("matches(")
						.append(propertyName).append(",'").append(condition.getReferenceValue()).append("')")
						.append(BLANK);
			} else {
				buffer.append("'").append(condition.getReferenceValue()).append("'").append(BLANK);
			}
			break;
		case LONG_TYPE:
		case DOUBLE_TYPE:
			buffer.append(condition.getReferenceValue()).append(BLANK);
			break;
		case DATE_TYPE:// TODO:date可以考虑转化为long处理.
			throw new RuleCenterRuntimeException("class java.util.Date 类型的指标数据还未实现.");
		case LIST_TYPE:
			// TODO 还需要处理List里面的泛型,现在默认为String
			final String referenceValue = condition.getReferenceValue();
			final String[] arr = referenceValue.split(",");

			final String symbol = ConditionOp.valueOfIgnoreCase(condition.getConditionOpName()).getSymbol();
			boolean first = true;
			for (final String value : arr) {
				if (first) {
					first = false;
				} else {
					buffer.append(ConditionRelation.OR.getSymbol()).append(BLANK)//
							.append(propertyName).append(BLANK)//
							.append(symbol).append(BLANK);
				}
				buffer.append("'").append(value).append("'").append(BLANK);
			}
			break;
		default:
			throw new RuleCenterRuntimeException("条件:" + condition.getName() + ",写入LHS分支出现参数不正确");
		}
	}
}