package com.yunat.ccms.workflow.vo;

public class MxNode {
	private Long id;
	private String type;
	private String value;
	private String style;
	private Long x;
	private Long y;
	private String vertex = "1";
	private Long width = 52L;
	private Long height = 52L;
	private String asT = "geometry";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public Long getX() {
		return x;
	}

	public void setX(Long x) {
		this.x = x;
	}

	public Long getY() {
		return y;
	}

	public void setY(Long y) {
		this.y = y;
	}

	public String getVertex() {
		return vertex;
	}

	public Long getWidth() {
		return width;
	}

	public Long getHeight() {
		return height;
	}

	public String getAsT() {
		return asT;
	}

}