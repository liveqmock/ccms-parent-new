package com.yunat.ccms.metadata.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.metadata.face.FaceCriteria;
import com.yunat.ccms.metadata.face.FaceNode;
import com.yunat.ccms.metadata.face.FaceOperator;
import com.yunat.ccms.metadata.face.FaceQuery;
import com.yunat.ccms.metadata.face.JsonFaceConverter;
import com.yunat.ccms.node.biz.query.NodeQueryDefinedRepository;
import com.yunat.ccms.node.biz.query.QueryNodeConfigService;

public class QueryNodeConfigServiceTest extends AbstractJunit4SpringContextBaseTests {

	@Autowired
	NodeQueryDefinedRepository nodeQueryDefinedRepository;

	@Autowired
	QueryNodeConfigService queryNodeConfigService;

	@Autowired
	MetaQueryConfigService metaQueryConfigService;

	@Test
	public void testDelete() {

		nodeQueryDefinedRepository.delete(100270L);
	}

	private FaceQuery createPropertiesQuery() {

		FaceQuery face_q = new FaceQuery();

		FaceCriteria face_c_1 = new FaceCriteria();
		face_c_1.setKey("3");
		face_c_1.setOp(new FaceOperator("EQ", "等于"));
		face_c_1.setValues("kevin");

		FaceCriteria face_c_2 = new FaceCriteria();
		face_c_2.setKey("4");
		face_c_2.setOp(new FaceOperator("EQ", "等于"));
		face_c_2.setValues("男");

		FaceCriteria face_c_3 = new FaceCriteria();
		face_c_3.setKey("17");
		face_c_3.setOp(new FaceOperator("EQ", "等于"));
		face_c_3.setValues("VIP1");

		FaceCriteria face_c_4 = new FaceCriteria();
		face_c_4.setKey("23");
		face_c_4.setOp(new FaceOperator("GT", "大于"));
		face_c_4.setValues("1");

		face_q.getCons().put("1", face_c_1);
		face_q.getCons().put("2", face_c_2);
		face_q.getCons().put("3", face_c_3);
		face_q.getCons().put("4", face_c_4);
		face_q.setPlat("TAOBAO");
		face_q.setName("TEST");
		face_q.setCode("CUSTOMER");

		return face_q;
	}

	@Test
	public void testUpdatePropertiesWithDeleteCriteria() {

		FaceNode node = queryNodeConfigService.loadConfigForFace(555L);
		node.getQueries().get(0).getDelcons().add("100426");
		JsonFaceConverter.toJson(node);
		queryNodeConfigService.saveConfigFromFace(node, 555L);
	}

	@Test
	public void testUpdatePropertiesWithAddCriteria() {

		FaceNode node = queryNodeConfigService.loadConfigForFace(555L);

		FaceCriteria fc = new FaceCriteria();
		fc.setKey("7");
		fc.setOp(new FaceOperator("EQ", "等于"));
		fc.setValues("{'type':1, 'value':'04-08'}");

		FaceCriteria fc1 = new FaceCriteria();
		fc1.setKey("7");
		fc1.setOp(new FaceOperator("EQ", "等于"));
		fc1.setValues("{'type':2, 'value':'5'}");

		node.getQueries().get(0).getCons().put("1", fc);
		node.getQueries().get(0).getCons().put("2", fc1);

		JsonFaceConverter.toJson(node);
		queryNodeConfigService.saveConfigFromFace(node, 555L);
	}

	@Test
	public void testSaveQueryNode() {

		FaceNode node = new FaceNode();
		node.setExclude("false");
		node.setTimeType("1");
		node.setPlat("TAOBAO");

		FaceQuery face_q = new FaceQuery();

		FaceCriteria face_c_1 = new FaceCriteria();
		face_c_1.setKey("162");
		face_c_1.setOp(new FaceOperator("GE", "等于"));
		face_c_1.setValues("{'type':'RELTIME', 'value':'前1月,第3号 12:12:12'}");

		FaceCriteria face_c_2 = new FaceCriteria();
		face_c_2.setKey("162");
		face_c_2.setOp(new FaceOperator("LE", "等于"));
		face_c_2.setValues("{'type':'RELTIME', 'value':'前60分钟'}");

		FaceCriteria face_c_3 = new FaceCriteria();
		face_c_3.setKey("164");
		face_c_3.setOp(new FaceOperator("EQ", "等于"));
		face_c_3.setValues("HITAO");

		FaceCriteria face_c_4 = new FaceCriteria();
		face_c_4.setKey("165");
		face_c_4.setOp(new FaceOperator("EQ", "等于"));
		face_c_4.setValues("1");

		FaceCriteria face_c_5 = new FaceCriteria();
		face_c_5.setKey("170");
		face_c_5.setOp(new FaceOperator("EQ", "等于"));
		face_c_5.setValues("23");

		FaceCriteria face_c_6 = new FaceCriteria();
		face_c_6.setKey("191");
		face_c_6.setOp(new FaceOperator("LT", "大于"));
		face_c_6.setValues("1000");

		FaceCriteria face_c_7 = new FaceCriteria();
		face_c_7.setKey("161");
		face_c_7.setOp(new FaceOperator("LT", "大于"));
		face_c_7.setValues("{\"type\":\"R\",\"value\":\"F:45L:61559109P:10347207133P:10347916849\"}");

		face_q.getCons().put("OrderStartTime", face_c_1);
		face_q.getCons().put("OrderEndTime", face_c_2);
		face_q.getCons().put("OrderTradeFrom", face_c_3);
		face_q.getCons().put("OrderType", face_c_4);
		face_q.getCons().put("OrderStatus", face_c_5);
		face_q.getCons().put("OrderQuota", face_c_6);
		face_q.getCons().put("OrderProduct", face_c_7);

		face_q.setPlat("TAOBAO");
		face_q.setName("TEST");
		face_q.setCode("ORDER_ITEM");
		face_q.setIsBuy("true");
		face_q.setRelation("AND");
		node.getQueries().add(createPropertiesQuery());
		node.getQueries().add(face_q);
		System.out.println(JsonFaceConverter.toJson(node));
		queryNodeConfigService.saveConfigFromFace(node, 1L);
	}

	@Test
	public void testGenPropertySql() {

		FaceNode node = queryNodeConfigService.loadConfigForFace(2L);
		System.out.println(JsonFaceConverter.toJson(node));
	}

	@Test
	public void testGenConsumeSql() {

		FaceNode node = queryNodeConfigService.loadConfigForFace(1L);
		System.out.println(JsonFaceConverter.toJson(node));
	}
}
