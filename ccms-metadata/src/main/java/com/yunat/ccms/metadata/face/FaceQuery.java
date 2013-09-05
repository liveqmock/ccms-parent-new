package com.yunat.ccms.metadata.face;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.map.ListOrderedMap;

/**
 * 界面显示和交互的查询定义
 * 
 * @author kevin.jiang 2013-3-15
 */
public class FaceQuery {

	private String id;
	private String code;
	private String name;
	private String plat;
	private String isBuy = "false";
	private String relation;
	private List<FaceAttribute> attrs = new ArrayList<FaceAttribute>();
	private Map<String, FaceCriteria> cons = new ListOrderedMap<String, FaceCriteria>();
	private List<String> delcons = new ArrayList<String>();

	@SuppressWarnings("rawtypes")
	private Map<String, Map<String, List>> options;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlat() {
		return plat;
	}

	public void setPlat(String plat) {
		this.plat = plat;
	}

	public String getIsBuy() {
		return isBuy;
	}

	public void setIsBuy(String isBuy) {
		this.isBuy = isBuy;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public List<FaceAttribute> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<FaceAttribute> attrs) {
		this.attrs = attrs;
	}

	public Map<String, FaceCriteria> getCons() {
		return cons;
	}

	public void setCons(Map<String, FaceCriteria> cons) {
		this.cons = cons;
	}

	public List<String> getDelcons() {
		return delcons;
	}

	public void setDelcons(List<String> delcons) {
		this.delcons = delcons;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Map<String, List>> getOptions() {
		return options;
	}

	@SuppressWarnings("rawtypes")
	public void setOptions(Map<String, Map<String, List>> options) {
		this.options = options;
	}
}
