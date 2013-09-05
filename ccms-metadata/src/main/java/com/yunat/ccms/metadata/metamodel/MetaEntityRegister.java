package com.yunat.ccms.metadata.metamodel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MetaEntityRegister {

	private Map<String, MetaEntity> entities = new HashMap<String, MetaEntity>();
	private int charIndex = 97; // from char 'a'

	/**
	 * 获取注册实体（表）的数量
	 * 
	 * @return
	 */
	public int getRegistEntityAmount() {

		return this.entities.size();
	}

	public MetaEntity getOnlyOneRegistEntity() throws Exception {

		if (this.entities.size() != 1) {

			throw new Exception("there is more than one entity in the register, or there is no entity.");
		}
		Iterator<MetaEntity> it = this.entities.values().iterator();
		return it.next();
	}

	/**
	 * @param name
	 *            元数据的表名
	 * @return
	 */
	public MetaEntity findEntityByName(String name) {

		return this.entities.get(name);
	}

	/**
	 * 注册并赋予别名
	 * 
	 * 注意：会修改传入参数的别名属性（这种方式最为简洁）
	 * 
	 * @param entity
	 */
	public void findAndRegistEntityByName(MetaEntity entity) { // 注意：会修改传入参数的别名属性（这种方式最为简洁）

		MetaEntity localEntity = this.entities.get(entity.getTableName());
		if (localEntity == null) {

			registNewMetaEntity(entity);

		} else {

			entity.setTableAlias(localEntity.getTableAlias());
		}
	}

	private void registNewMetaEntity(MetaEntity entity) {

		String alias = String.valueOf((char) this.charIndex);
		entity.setTableAlias(alias);
		this.entities.put(entity.getTableName(), entity);
		this.charIndex++;
	}
}
