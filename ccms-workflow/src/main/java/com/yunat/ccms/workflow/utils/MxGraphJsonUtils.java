package com.yunat.ccms.workflow.utils;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.yunat.ccms.workflow.vo.MxGraph;

public class MxGraphJsonUtils {
	private static final String STANDARD_JSON = "/config/workflow/standard.json";
	private static final String RFM_JSON = "/config/workflow/rfm.json";

	private static MxGraph readValueByFile(String path) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			MxGraph mxgraph = mapper.readValue(MxGraphJsonUtils.class.getResourceAsStream(path), MxGraph.class);
			return mxgraph;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static MxGraph standardConvert() {
		return MxGraphJsonUtils.readValueByFile(STANDARD_JSON);
	}

	public static MxGraph rfmConvert() {
		return MxGraphJsonUtils.readValueByFile(RFM_JSON);
	}

	public static MxGraph convert(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			MxGraph mxgraph = (MxGraph)mapper.readValue(jsonString, MxGraph.class);
			return mxgraph;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convert(MxGraph mxgraph) {
		ObjectMapper mapper = new ObjectMapper();
        try {
			String json = mapper.writeValueAsString(mxgraph);
			return json;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
