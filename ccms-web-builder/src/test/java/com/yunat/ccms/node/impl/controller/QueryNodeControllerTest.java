package com.yunat.ccms.node.impl.controller;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.ResultActions;
import org.springframework.test.web.server.request.DefaultRequestBuilder;
import org.springframework.test.web.server.request.MockMvcRequestBuilders;
import org.springframework.test.web.server.result.StatusResultMatchers;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import com.yunat.ccms.node.biz.query.NodeQueryController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml", "classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class QueryNodeControllerTest {

	@Autowired
	private NodeQueryController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void loadConfig() {

		// 读取
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/node/query/config/{id}", 1L)
				.characterEncoding("UTF-8");
		requestBuilder.accept(new MediaType("application", "json", Charset.forName("utf-8")));
		String dbJson = "{}";
		try {

			ResultActions ra = this.mockMvc.perform(requestBuilder);
			dbJson = ra.andReturn().getResponse().getContentAsString();
			System.out.println(dbJson);
			ra.andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addProperty() {

		DefaultRequestBuilder requestBuilder = null;

		// 新增
		String json = "{'exclude':false,'timeType':1,'plat':'TAOBAO','queries':[{'id':100143,'code':'ORDER','name':'订单总体消费查询','plat':'taobao','isBuy':true,'relation':'AND','attrs':[],'cons':{'OrderQuota':{'id':100624,'key':111,'labelName':'购买金额','op':{'value':'LT','name':'小于'},'values':'1000','group':'','relation':'AND','queryType':'QUOTA'},'OrderStatus':{'id':100627,'key':63,'labelName':'CCMS交易状态','op':{'value':'EQ','name':'等于'},'values':'23','group':'','relation':'AND','queryType':'REFER'},'OrderType':{'id':100622,'key':58,'labelName':'交易类型','op':{'value':'EQ','name':'等于'},'values':'1','group':'','relation':'AND','queryType':'STRING'},'OrderStartTime':{'id':100625,'key':54,'labelName':'交易创建时间','op':{'value':'GE','name':'大于等于'},'values':'{'type':'ABSTIME', 'date':'2013-03-27 12:00:00'}','group':'','relation':'AND','queryType':'DATETIME'},'OrderEndTime':{'id':100623,'key':54,'labelName':'交易创建时间','op':{'value':'LE','name':'小于等于'},'values':'{'type':'ABSTIME', 'date':'2013-03-27 12:00:00'}','group':'','relation':'AND','queryType':'DATETIME'},'OrderTradeFrom':{'id':100626,'key':57,'labelName':'交易来源','op':{'value':'EQ','name':'等于'},'values':'HITAO','group':'','relation':'AND','queryType':'DIC'}},'delcons':[],'options':{'dicvalues':{'57':[{'value':'HITAO','name':'嗨淘'},{'value':'JHS','name':'聚划算'},{'value':'TAOBAO','name':'普通淘宝'},{'value':'TOP','name':'TOP平台'},{'value':'WAP','name':'手机'}],'63':[{'value':'已下单未付款','name':'已下单未付款'},{'value':'有效交易','name':'有效交易'},{'value':'&nbsp;&nbsp;&nbsp;&nbsp;|---已付款未发货','name':'已付款未发货'},{'value':'&nbsp;&nbsp;&nbsp;&nbsp;|---已发货待确认','name':'已发货待确认'},{'value':'&nbsp;&nbsp;&nbsp;&nbsp;|---交易成功','name':'交易成功'},{'value':'交易失败  ','name':'交易失败'},{'value':'&nbsp;&nbsp;&nbsp;&nbsp;|---未付订金','name':'未付订金'},{'value':'&nbsp;&nbsp;&nbsp;&nbsp;|---已付订金未付尾款','name':'已付订金未付尾款'}]},'operators':{'58':[{'value':'EQ','name':'等于'},{'value':'LIKE','name':'包含'},{'value':'NE','name':'不等于'},{'value':'NOTLIKE','name':'不包含'}],'57':[{'value':'EQ','name':'等于'},{'value':'NE','name':'不等于'}],'111':[{'value':'GT','name':'大于'},{'value':'LT','name':'小于'},{'value':'EQ','name':'等于'}],'63':[{'value':'EQ','name':'等于'},{'value':'NE','name':'不等于'}],'54':[{'value':'GT','name':'大于'},{'value':'LT','name':'小于'},{'value':'GE','name':'大于等于'},{'value':'LE','name':'小于等于'},{'value':'EQ','name':'等于'},{'value':'NE','name':'不等于'}]}}},{'id':100144,'code':'CUSTOMER','name':'客户信息','plat':'taobao','isBuy':false,'relation':'AND','attrs':[],'cons':{'3':{'id':100629,'key':17,'labelName':'客户全站等级','op':{'value':'EQ','name':'等于'},'values':'VIP1','group':'','relation':'AND','queryType':'DIC'},'2':{'id':100630,'key':4,'labelName':'性别','op':{'value':'EQ','name':'等于'},'values':'男','group':'','relation':'AND','queryType':'DIC'},'1':{'id':100631,'key':3,'labelName':'姓名','op':{'value':'EQ','name':'等于'},'values':'kevin','group':'','relation':'AND','queryType':'STRING'},'4':{'id':100628,'key':23,'labelName':'会员等级','op':{'value':'GT','name':'大于'},'values':'1','group':'','relation':'AND','queryType':'ORDERED_DIC'}},'delcons':[],'options':{'dicvalues':{'4':[{'value':'m','name':'男'},{'value':'f','name':'女'},{'value':'none','name':'未知'}],'17':[{'value':'c','name':'普通会员'},{'value':'asso_vip','name':'荣誉会员'},{'value':'vip1','name':'VIP1'},{'value':'vip2','name':'VIP2'},{'value':'vip3','name':'VIP3'},{'value':'vip4','name':'VIP4'},{'value':'vip5','name':'VIP5'},{'value':'vip6','name':'VIP6'}],'23':[{'value':'1','name':'普通会员'},{'value':'2','name':'高级会员'},{'value':'3','name':'VIP会员'},{'value':'4','name':'至尊VIP会员'}]},'operators':{'3':[{'value':'EQ','name':'等于'},{'value':'LIKE','name':'包含'},{'value':'NE','name':'不等于'},{'value':'NOTLIKE','name':'不包含'}],'4':[{'value':'EQ','name':'等于'},{'value':'NE','name':'不等于'}],'17':[{'value':'EQ','name':'等于'},{'value':'NE','name':'不等于'}],'23':[{'value':'GT','name':'大于'},{'value':'LT','name':'小于'},{'value':'GE','name':'大于等于'},{'value':'LE','name':'小于等于'},{'value':'EQ','name':'等于'},{'value':'NE','name':'不等于'}]}}}]}";

		requestBuilder = MockMvcRequestBuilders.post("/node/query/config/{id}", 0L).characterEncoding("UTF-8");
		requestBuilder.param("addinfo", json, new String[] {});
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用例包含新增配置，读取配置，以及更改后更新配置三个具体场景
	 */
	@Test
	public void addAndSaveConfig() {

		DefaultRequestBuilder requestBuilder = null;

		// 新增
		String json = "{'exclude':false,'plat':'TAOBAO','queries':[{'attrs':[],'code':'ORDER_ITEM','cons':[{'group':'xx','key':243,'op':{'name':'不包含','value':'NOTLIKE'},'relation':'OR','values':'商品'},{'group':'','key':162,'op':{'name':'等于','value':'EQ'},'relation':'AND','values':{'type':'ABSTIME', 'date':'2013-03-27 12:00:00'}},{'group':'xx','key':243,'op':{'name':'不包含','value':'NOTLIKE'},'relation':'OR','values':'abc'},{'group':'','key':191,'op':{'name':'等于','value':'EQ'},'relation':'AND','values':'100'},{'group':'xx','key':153,'op':{'name':'等于','value':'EQ'},'relation':'OR','values':'10001'},{'group':'','key':197,'op':{'name':'大于','value':'GT'},'relation':'AND','values':{'type':'ABSTIME', 'date':'2013-03-27 00:00:00'}}],'isBuy':false,'name':'订单商品消费查询','plat':'taobao','relation':'OR'},{'attrs':[],'code':'ORDER_ITEM','cons':[{'group':'xx','key':153,'op':{'name':'等于','value':'EQ'},'relation':'OR','values':'10001'},{'group':'','key':197,'op':{'name':'大于','value':'GT'},'relation':'AND','values':{'type':'ABSTIME', 'date':'2013-03-27 00:00:00'}},{'group':'','key':191,'op':{'name':'等于','value':'EQ'},'relation':'AND','values':'100'},{'group':'','key':162,'op':{'name':'等于','value':'EQ'},'relation':'AND','values':{'type':'ABSTIME', 'date':'2013-03-27 12:00:00'}},{'group':'xx','key':243,'op':{'name':'不包含','value':'NOTLIKE'},'relation':'OR','values':'edf'},{'group':'xx','key':243,'op':{'name':'不包含','value':'NOTLIKE'},'relation':'OR','values':'abc'}],'isBuy':false,'name':'订单商品消费查询','plat':'taobao','relation':'OR'}],'timeType':1}";

		requestBuilder = MockMvcRequestBuilders.post("/node/query/config/{id}", 0L).characterEncoding("UTF-8");
		requestBuilder.param("addinfo", json, new String[] {});
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 读取
		requestBuilder = MockMvcRequestBuilders.get("/node/query/config/{id}", 0L).characterEncoding("UTF-8");
		requestBuilder.accept(new MediaType("application", "json", Charset.forName("utf-8")));
		String dbJson = "{}";
		try {

			ResultActions ra = this.mockMvc.perform(requestBuilder);
			dbJson = ra.andReturn().getResponse().getContentAsString();
			ra.andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 更新
		requestBuilder = MockMvcRequestBuilders.put("/node/query/config/{id}", 0L).characterEncoding("UTF-8");
		requestBuilder.param("addinfo", dbJson, new String[] {});
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(dbJson);
	}

	/**
	 * 测试加载商品信息
	 */
	@Test
	public void testLoadProductList() {

		DefaultRequestBuilder requestBuilder = null;

		requestBuilder = MockMvcRequestBuilders.post("/node/query/products").characterEncoding("UTF-8");
		requestBuilder.param("title", "kevin", new String[] {});
		requestBuilder.param("dpId", "", new String[] {});
		requestBuilder.param("outId", "", new String[] {});
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			ResultActions ra = this.mockMvc.perform(requestBuilder);
			System.out.println(ra.andReturn().getResponse().getContentAsString());
			ra.andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
