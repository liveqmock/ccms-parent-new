package com.yunat.ccms.workflow.vo;

public class MxConnect {
	private Long id;
	private Long source;
	private Long target;
	private String edge = "1";
	private String relative = "1";
	private String asT = "geometry";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSource() {
		return source;
	}

	public void setSource(Long source) {
		this.source = source;
	}

	public Long getTarget() {
		return target;
	}

	public void setTarget(Long target) {
		this.target = target;
	}

	public String getEdge() {
		return edge;
	}

	public String getRelative() {
		return relative;
	}

	public String getAsT() {
		return asT;
	}

}