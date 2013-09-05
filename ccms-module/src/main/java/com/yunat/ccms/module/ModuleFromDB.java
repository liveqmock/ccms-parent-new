package com.yunat.ccms.module;

import java.util.Collection;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.yunat.ccms.core.support.auth.SupportOp;

/**
 * 模块实例。是用来展现的具体的“模块类型”的一个实例。 参考{@link ModuleType}
 * 
 * @author MaGiCalL
 */
@Entity
@Table(name = "module")
public class ModuleFromDB implements Module, Comparable<ModuleFromDB> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	@Column(name = "key_name")
	private String keyName;
	/**
	 * 所属模块类型的id,参考{@link ModuleType}
	 */
	@Column(name = "module_type_id")
	private Long moduleTypeId;
	/**
	 * 模块实例被另一个模块实例包含,则"另一个模块实例"是container。
	 */
	@Column(name = "container_module_id")
	private Long containerModuleId;

	@Column(name = "url")
	private String url;

	@Column(name = "data_url")
	private String dataUrl;
	/**
	 * 名字。可用于展示时显示。 如果未指定，则应继承ModuleType的。
	 */
	@Column(name = "name")
	private String name;
	/**
	 * 提示文案。可用于展示时作为html标签的title/alt属性、鼠标hover显示。 如果未指定，则应继承ModuleType的。
	 */
	@Column(name = "tip")
	private String tip;

	@Column(name = "lowest_edition_required")
	private Integer lowestEditionRequired;
	/**
	 * 支持的操作 如果未指定，则应继承ModuleType的
	 */
	@Column(name = "support_ops_mask")
	private Integer supportOpsMask;
	/**
	 * 用于排序.小者权重高.若相同,用id字段,同样是小者权重高.若为null,视为权重最低
	 */
	@Column(name = "ranking")
	private Float ranking;
	/**
	 * 备注
	 */
	@Column(name = "memo")
	private String memo;

	/**
	 * 所属模块类型,参考{@link ModuleType}
	 */
	private transient ModuleType moduleType;
	/**
	 * 包含本模块实例的外层模块实例.
	 */
	private transient Module containerModule;
	/**
	 * 本模块实例内包含的模块实例
	 */
	private transient Collection<Module> containingModules = new TreeSet<Module>();

	@Override
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public Long getModuleTypeId() {
		return moduleTypeId;
	}

	public void setModuleTypeId(final Long moduleTypeId) {
		this.moduleTypeId = moduleTypeId;
	}

	@Override
	public Long getContainerModuleId() {
		return containerModuleId;
	}

	public void setContainerModuleId(final Long containerModuleId) {
		this.containerModuleId = containerModuleId;
	}

	@Override
	public ModuleType getModuleType() {
		return moduleType;
	}

	public void setModuleType(final ModuleType moduleType) {
		this.moduleType = moduleType;
	}

	@Override
	public Module getContainerModule() {
		return containerModule;
	}

	public void setContainerModule(final Module containerModule) {
		this.containerModule = containerModule;
	}

	@Override
	public String getName() {
		if (name != null) {
			return name;
		}
		final ModuleType moduleType = getModuleType();
		return moduleType == null ? "" : moduleType.getName();
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getTip() {
		if (tip != null) {
			return tip;
		}
		final ModuleType moduleType = getModuleType();
		return moduleType == null ? "" : moduleType.getTip();
	}

	public void setTip(final String tip) {
		this.tip = tip;
	}

	@Override
	public String getMemo() {
		return memo;
	}

	public void setMemo(final String note) {
		memo = note;
	}

	@Override
	public Collection<Module> getContainingModules() {
		return containingModules;
	}

	@Override
	public Integer getSupportOpsMask() {
		if (supportOpsMask != null) {
			return supportOpsMask;
		}
		final ModuleType moduleType = getModuleType();
		return moduleType == null ? null : moduleType.getSupportOpsMask();
	}

	public void setSupportOpsMask(final Integer supportOpsMask) {
		this.supportOpsMask = supportOpsMask;
	}

	@Override
	public Collection<SupportOp> getSupportOps() {
		final ModuleType moduleType = getModuleType();
		return moduleType == null ? null : moduleType.getSupportOps();
	}

	@Override
	public int getLowestEditionRequired() {
		if (lowestEditionRequired != null) {
			return lowestEditionRequired;
		}
		final ModuleType moduleType = getModuleType();
		return moduleType == null ? 0 : moduleType.getLowestEditionRequired();
	}

	@Override
	public String toString() {
		return "ModuleFromDB [getId()=" + getId() + ", getModuleTypeId()=" + getModuleTypeId()
				+ ", getContainerModuleId()=" + getContainerModuleId() + ", getName()=" + getName() + ", getTip()="
				+ getTip() + ", getNote()=" + getMemo() + ", getSupportOpsMask()=" + getSupportOpsMask()
				+ ", getSupportOps()=" + getSupportOps() + ", getLowestEditionRequired()=" + getLowestEditionRequired()
				+ "]";
	}

	@Override
	public int getLayer() {
		final Module container = getContainerModule();
		return container == null ? 0 : container.getLayer() + 1;
	}

	@Override
	public String getUrl() {
		if (url != null) {
			return url;
		}
		final ModuleType moduleType = getModuleType();
		return moduleType == null ? null : moduleType.getUrl();
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	@Override
	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(final String dataUrl) {
		this.dataUrl = dataUrl;
	}

	public void setLowestEditionRequired(final Integer lowestEditionRequired) {
		this.lowestEditionRequired = lowestEditionRequired;
	}

	@Override
	public String getKeyName() {
		if (keyName != null) {
			return keyName;
		}
		final ModuleType moduleType = getModuleType();
		return moduleType == null ? "" : moduleType.getKeyName();
	}

	public void setKeyName(final String keyName) {
		this.keyName = keyName;
	}

	@Override
	public int compareTo(final ModuleFromDB o) {
		if (ranking == null) {
			if (o.ranking == null) {
				return id.compareTo(o.id);
			} else {
				return -1;
			}
		}
		// ranking!=null
		if (o.ranking == null) {
			return 1;
		}
		final int r = ranking.compareTo(o.ranking);
		return r == 0 ? id.compareTo(o.id) : r;
	}
}
