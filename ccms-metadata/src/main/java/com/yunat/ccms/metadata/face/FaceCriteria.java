package com.yunat.ccms.metadata.face;

/**
 * 界面交互的查询条件
 * 
 * @author kevin.jiang 2013-3-15
 */
public class FaceCriteria {

	private String id;
	private String key;
	private String labelName;
	private FaceOperator op;
	private String values;
	private String group = "";
	private String relation;
	private String queryType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public FaceOperator getOp() {
		return op;
	}

	public void setOp(FaceOperator op) {
		this.op = op;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

}
