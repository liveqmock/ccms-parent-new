package com.yunat.ccms.workflow.utils;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.yunat.ccms.workflow.vo.MxConnect;
import com.yunat.ccms.workflow.vo.MxGraph;
import com.yunat.ccms.workflow.vo.MxNode;

import junit.framework.TestCase;


public class MxGraphJsonUtilsTest extends TestCase {

	@Test
	public void testConvertMxGraph() {
		MxGraph mg = new MxGraph();
		List<MxNode> nodes = Lists.newArrayList();
		MxNode mn1 = new MxNode();
		mn1.setId(1L);
		mn1.setStyle("tflowstart");
		nodes.add(mn1);
		MxNode mn = new MxNode();
		mn.setId(2L);
		mn.setStyle("tflowtime");
		nodes.add(mn);
		List<MxConnect> connects = Lists.newArrayList();
		MxConnect mc = new MxConnect();
		mc.setId(1L);
		mc.setSource(1L);
		mc.setTarget(2L);
		connects.add(mc);
		mg.setConnects(connects);
		mg.setNodes(nodes);

		String jsonString = MxGraphJsonUtils.convert(mg);
		assertNotNull(jsonString);
	}

	@Test
	public void testConvertJsonString() {
		String jsonString = "{\"nodes\":[{\"id\":1, \"type\":null, \"value\":null, \"style\":\"tflowstart\", \"x\":null, \"y\":null, \"vertex\":\"1\", \"width\":52, \"height\":52, \"asT\":\"geometry\"},{\"id\":2, \"type\":null, \"value\":null, \"style\":\"tflowtime\", \"x\":null, \"y\":null, \"vertex\":\"1\", \"width\":52, \"height\":52, \"asT\":\"geometry\"}],\"connects\":[{\"id\":1, \"source\":1, \"target\":2, \"edge\":\"1\", \"relative\":\"1\", \"asT\":\"geometry\"}]}";
		MxGraph mxgraph = MxGraphJsonUtils.convert(jsonString);
		assertEquals(2, mxgraph.getNodes().size());
	}

	@Test
	public void testRfmConvert() {
		MxGraph mxGraph = MxGraphJsonUtils.rfmConvert();
		assertNotNull(mxGraph.getConnects());
	}

	@Test
	public void testStandardConvert() {
		MxGraph mxGraph = MxGraphJsonUtils.standardConvert();
		assertNotNull(mxGraph.getConnects());
	}
}
