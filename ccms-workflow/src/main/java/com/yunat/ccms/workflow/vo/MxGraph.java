package com.yunat.ccms.workflow.vo;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.collect.Lists;

public class MxGraph {
	private List<MxNode> nodes;
	private List<MxConnect> connects;

	public List<MxNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<MxNode> nodes) {
		this.nodes = nodes;
	}

	public List<MxConnect> getConnects() {
		return connects;
	}

	public void setConnects(List<MxConnect> connects) {
		this.connects = connects;
	}

	public static void main(String[] args) {
		try {
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

			ObjectMapper mapper = new ObjectMapper();
	        String json = mapper.writeValueAsString(mg);
			System.out.println(json);

			MxGraph mxgraph = (MxGraph)mapper.readValue(json, MxGraph.class);
			System.out.println(mxgraph.nodes.size());

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}