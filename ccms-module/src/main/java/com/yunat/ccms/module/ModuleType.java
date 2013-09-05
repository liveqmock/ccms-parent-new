package com.yunat.ccms.module;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.yunat.ccms.core.support.auth.SupportOp;

/**
 * <pre>
 * 模块类型，或称为“模块原型”。页面上展现的是其实例。
 * 例：密码输入框就是一种模块类型，它的实例可以出现在登录页面、修改密码页面（此页面甚至可出现两个密码输入框实例）等多处。
 * 参考{@link ModuleFromDB}
 * </pre>
 * 
 * @author MaGiCalL
 */
@Entity
@Table(name = "module_type")
public class ModuleType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	/**
	 * 作为key的名字，通常是英文。
	 */
	@Column(name = "key_name")
	private String keyName;
	/**
	 * 名字，通常是汉字。可用于展示。
	 * 注：实际上跟keyName是相同的，应该依赖于本地化。不过鉴于本地化没做，而且这里是中国，就这样吧。
	 */
	@Column(name = "name")
	private String name;
	/**
	 * 名字附注,给内部看的
	 */
	@Column(name = "name_plus")
	private String namePlus;
	/**
	 * 点击时跳转的地址.
	 */
	@Column(name = "url")
	private String url;
	/**
	 * 请求数据的地址
	 */
	@Column(name = "data_url")
	private String dataUrl;
	/**
	 * 提示.可用于title、鼠标hover的时候显示
	 */
	@Column(name = "tip")
	private String tip;
	/**
	 * 最低版本要求
	 */
	@Column(name = "lowest_edition_required")
	private int lowestEditionRequired;
	/**
	 * 支持的操作
	 */
	@Column(name = "support_ops_mask")
	private int supportOpsMask;
	/**
	 * 备注说明
	 */
	@Column(name = "memo")
	private String memo;

	public Collection<SupportOp> getSupportOps() {
		return SupportOp.supportOps(getSupportOpsMask());
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getNamePlus() {
		return namePlus;
	}

	public void setNamePlus(final String namePlus) {
		this.namePlus = namePlus;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(final String tip) {
		this.tip = tip;
	}

	public int getSupportOpsMask() {
		return supportOpsMask;
	}

	public void setSupportOpsMask(final int supportOpsMask) {
		this.supportOpsMask = supportOpsMask;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getLowestEditionRequired() {
		return lowestEditionRequired;
	}

	public void setLowestEditionRequired(final Integer lowestEditionRequired) {
		this.lowestEditionRequired = lowestEditionRequired == null ? 0 : lowestEditionRequired;
	}

	public void setLowestEditionRequired(final int lowestEditionRequired) {
		this.lowestEditionRequired = lowestEditionRequired;
	}

	@Override
	public String toString() {
		return "ModuleType [getSupportOps()=" + getSupportOps() + ", getName()=" + getName() + ", getNamePlus()="
				+ getNamePlus() + ", getMemo()=" + getMemo() + ", getTip()=" + getTip() + ", getSupportOpsMask()="
				+ getSupportOpsMask() + ", getId()=" + getId() + ", getLowestEditionRequired()="
				+ getLowestEditionRequired() + "]";
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(final String keyName) {
		this.keyName = keyName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(final String memo) {
		this.memo = memo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(final String dataUrl) {
		this.dataUrl = dataUrl;
	}
}
