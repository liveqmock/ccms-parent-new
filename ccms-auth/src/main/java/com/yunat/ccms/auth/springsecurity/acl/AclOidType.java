package com.yunat.ccms.auth.springsecurity.acl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 对付spring security acl的acl_class表的.
 * 我们需要给"有acl的类"做一个缓存,否则对所有类的对象都去找一找acl,数据量太大.这是spring security神坑的地方.
 * 
 * @author wenjian.liang
 */
@Entity
@Table(name = "acl_class")
public class AclOidType {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true)
	private Long id;

	/**
	 * 此字段在acl_class表里叫class.它是java 的关键字,并且,我们并不仅仅用来保留class的名字,可能还是另外的名字,所以改名为typeName
	 */
	@Column(name = "class")
	private String typeName;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(final String typeName) {
		this.typeName = typeName;
	}

}
