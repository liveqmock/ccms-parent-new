package com.yunat.ccms.rule.center.drl;

import java.util.List;

import com.mysema.query.types.Order;
import com.yunat.ccms.rule.center.conf.condition.ConditionRelation;
import com.yunat.ccms.rule.center.conf.condition.ConditionType;


/**
 * RuleStatement: 规则语句
 * @author yangtao
 *
 */
public class RuleFragment {
	private String name;
	private int salience;
	private String activationGroup;
	private List<LHSFragment> lhs;
	private RHSFragment rhs; //RHS 默认仅一条表达式
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSalience() {
		return salience;
	}

	public void setSalience(int salience) {
		this.salience = salience;
	}

	public List<LHSFragment> getLhs() {
		return lhs;
	}

	public void setLhs(List<LHSFragment> lhs) {
		this.lhs = lhs;
	}

	public RHSFragment getRhs() {
		return rhs;
	}

	public void setRhs(RHSFragment rhs) {
		this.rhs = rhs;
	}

	public String getActivationGroup() {
		return activationGroup;
	}

	public void setActivationGroup(String activationGroup) {
		this.activationGroup = activationGroup;
	}

	public String toStatement() {
		StringBuilder sb = new StringBuilder();
		sb.append("rule \"").append(name).append("\" ").append("\r\n");
		sb.append("salience ").append(salience).append("\r\n");
		sb.append("activation-group \"").append(activationGroup).append("\"").append("\r\n");
		sb.append("no-loop true").append("\r\n");
		sb.append("when ").append("\r\n");
		sb.append("	").append("$").append(ConditionType.ORDER.name().toLowerCase());
		sb.append(":").append(Order.class.getSimpleName()).append("(");
		
		List<LHSFragment> lhs = getLhs();
		boolean flag = true;
		for (LHSFragment lhsFragment : lhs) {
			if (flag) {
				flag = false;
				sb.append(removeConditionRelationIn(lhsFragment.getExpression()));
			} else {
				sb.append(lhsFragment.getExpression());
			}
		}
		sb.append(")").append(";").append("\r\n");
		
		sb.append("then ").append("\r\n");
		sb.append("	").append(getRhs().getExpression()).append("\r\n");
		sb.append("end").append("\r\n");
		return sb.toString();
	}

	protected String removeConditionRelationIn(String lhsExpression) {
		int andSymbol = lhsExpression.indexOf(ConditionRelation.AND.getSymbol());
		if (andSymbol > -1) {
			return lhsExpression.substring(andSymbol + ConditionRelation.AND.getSymbol().length());
		}
		
		int orSymbol = lhsExpression.indexOf(ConditionRelation.OR.getSymbol());
		if (orSymbol > -1) {
			return lhsExpression.substring(orSymbol + ConditionRelation.OR.getSymbol().length());
		}
		
		return lhsExpression;
	}
	
}