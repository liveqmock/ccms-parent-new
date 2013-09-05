package com.yunat.ccms.dashboard.channel.helper;

import java.util.List;
import java.util.Map;



public abstract class ChannelGuide {
	
	
	private  String channelType;
	
	protected ChannelGuide(String channelType){
		this.channelType = channelType;
	}
	
	public  abstract  List<Map<String, Object>> guideHandler();

	
	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

}
