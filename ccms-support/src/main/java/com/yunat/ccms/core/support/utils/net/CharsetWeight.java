package com.yunat.ccms.core.support.utils.net;

import java.nio.charset.Charset;

public class CharsetWeight {
	private Charset charset;
	private float weight;

	public CharsetWeight() {
		super();
	}

	public CharsetWeight(Charset charset, float weight) {
		super();
		this.charset = charset;
		this.weight = weight;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}
}