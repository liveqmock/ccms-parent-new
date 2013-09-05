package com.yunat.ccms.metadata.metamodel;

import junit.framework.Assert;

import org.junit.Test;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.metadata.pojo.DatabaseTable;

public class MetaSQLBuildTest extends AbstractJunit4SpringContextBaseTests {

	@Test
	public void buildAttribute() {

		MetaEntityRegister register = new MetaEntityRegister();

		DatabaseTable table = new DatabaseTable();
		table.setDbName("plt_taobao_order");
		MetaEntity entity = new MetaEntity(table);
		MetaAttribute attr = new MetaAttribute(entity, "created");
		System.out.println(attr.toSql(true, register));
		Assert.assertEquals("created", attr.toSql(true, register));
	}

	@Test
	public void buildAttrWithAlias() {

		MetaEntityRegister register = new MetaEntityRegister();
		DatabaseTable table = new DatabaseTable();
		table.setDbName("plt_taobao_order");
		MetaEntity entity = new MetaEntity(table, "a");
		MetaAttribute attr = new MetaAttribute(entity, "created", "cast(? as date)");
		Assert.assertEquals("cast(a.created as date)", attr.toSql(true, register));
	}

	@Test
	public void buildEqCriteria() {

		MetaEntityRegister register = new MetaEntityRegister();
		DatabaseTable table = new DatabaseTable();
		table.setDbName("plt_taobao_order");
		MetaEntity entity = new MetaEntity(table, "a");
		MetaAttribute attr = new MetaAttribute(entity, "created", "cast(? as date)");
		MetaCriteria criteria = new MetaCriteria(attr, MetaOperator.EQ, "{ \"2013-03-13\" }");
		System.out.println(criteria.toSql(true, register));
		Assert.assertEquals("cast(a.created as date) = str_to_date('2013-03-13', '%Y-%m-%d')",
				criteria.toSql(true, register));
	}

	@Test
	public void buildQuery() {

		try {
			DatabaseTable table = new DatabaseTable();
			table.setDbName("plt_taobao_order");
			MetaEntity entity = new MetaEntity(table, "a");

			MetaAttribute attr = new MetaAttribute(entity, "created", "cast(? as date)");
			MetaCriteria criteria = new MetaCriteria(attr, MetaOperator.EQ, "{ \"2013-03-13\" }");

			MetaAttribute attr_2 = new MetaAttribute(entity, "num", null);
			MetaCriteria criteria_2 = new MetaCriteria(attr_2, MetaOperator.GE, "{ \"123\" }");

			MetaQuery query = new MetaQuery();
			query.setMasterEntity(entity);
			query.addAttribute(attr);
			query.addAttribute(attr_2);
			query.addCriteria(criteria);
			query.addCriteria(criteria_2);

			System.out.println(query.toSql(true));
			Assert.assertEquals(
					"select cast(a.created as date), a.num from plt_taobao_order a where cast(a.created as date) = str_to_date('2013-03-13', '%Y-%m-%d') and a.num >= 123",
					query.toSql(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void buildViewFromMultiTables() {

		try {
			DatabaseTable table_a = new DatabaseTable();
			table_a.setDbName("plt_taobao_order");
			MetaEntity entity_a = new MetaEntity(table_a, "a");
			DatabaseTable table_b = new DatabaseTable();
			table_b.setDbName("uni_customer_plat");
			MetaEntity entity_b = new MetaEntity(table_b, "b");

			MetaAttribute attr = new MetaAttribute(entity_a, "created", "cast(? as date)");
			MetaCriteria criteria = new MetaCriteria(attr, MetaOperator.EQ, "{ \"2013-03-13\" }");

			MetaAttribute attr_2 = new MetaAttribute(entity_a, "num", null);
			MetaCriteria criteria_2 = new MetaCriteria(attr_2, MetaOperator.GE, "{123}");

			MetaAttribute attr_3 = new MetaAttribute(entity_b, "plat_priority", null);
			MetaCriteria criteria_3 = new MetaCriteria(attr_3, MetaOperator.EQ, "{1}");

			MetaAttribute fk = new MetaAttribute(entity_a, "customerno", null);
			MetaAttribute pk = new MetaAttribute(entity_b, "customerno", null);

			MetaQuery query = new MetaQuery();
			query.manualJoinEntity(fk, entity_b, pk, EnumTableJoin.INNER);

			query.addAttribute(attr);
			query.addAttribute(attr_2);
			query.addCriteria(criteria);
			query.addCriteria(criteria_2);
			query.addCriteria(criteria_3);
			System.out.println(query.toSql(true));
			Assert.assertEquals(
					"select cast(a.created as date), a.num from plt_taobao_order a inner join uni_customer_plat b on a.customerno = b.customerno where cast(a.created as date) = str_to_date('2013-03-13', '%Y-%m-%d') and a.num >= 123 and b.plat_priority = 1",
					query.toSql(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
