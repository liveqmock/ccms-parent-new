package com.yunat.ccms.metadata.metamodel;

import junit.framework.Assert;

import org.junit.Test;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.metadata.support.SetOperationSqlSupport;

public class SetOperationSqlSupportTest extends AbstractJunit4SpringContextBaseTests {

	@Test
	public void unionSetTest() {

		String except = "select a.uni_id, -1 as control_group_type from uni_customer a union select a.uni_id, -1 as control_group_type from vw_taobao_customer a";
		String actual = SetOperationSqlSupport.genUnionSetSql("uni_customer", "vw_taobao_customer");
		System.out.println(actual);
		Assert.assertEquals(except, actual);
	}

	@Test
	public void intersectionSetTest() {

		String except = "select a.uni_id, -1 as control_group_type from uni_customer a  inner join vw_taobao_customer b on a.uni_id = b.uni_id";
		String actual = SetOperationSqlSupport.genIntersectionSetSql("uni_customer", "vw_taobao_customer");
		System.out.println(actual);
		Assert.assertEquals(except, actual);
	}

	@Test
	public void complementarySetTest() {

		String except = "select a.uni_id, -1 as control_group_type from uni_customer a  left join vw_taobao_customer b on a.uni_id = b.uni_id where b.uni_id is null ";
		String actual = SetOperationSqlSupport.genComplementarySetSql("uni_customer", "vw_taobao_customer");
		System.out.println(actual);
		Assert.assertEquals(except, actual);
	}
}
