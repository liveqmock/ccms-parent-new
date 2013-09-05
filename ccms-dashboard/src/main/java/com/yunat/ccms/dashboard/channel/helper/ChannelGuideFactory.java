package com.yunat.ccms.dashboard.channel.helper;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 
 * @author yinwei
 * @version 1.0
 * @see ChannelGuide
 *
 */
@Component
public  class ChannelGuideFactory {
	
	@Autowired
    ChannelTransformHelper channelTransformHelper;
	
	
	
	
	private enum ChannelType{  
		tcommunicateSMS,tcommunicateEDM,tcommunicateEC,tcommunicateMMS,tcommunicateWAP  
	} 
	
	
	
	public ChannelGuide SMSGuideChannelGuide() {
        return new ChannelGuide("sms") {

            @Override
			public List<Map<String, Object>> guideHandler() {
        		return  channelTransformHelper.channelSendInfo(ChannelType.tcommunicateSMS.name());
			}

		};
    }
	
	
	public ChannelGuide EDMGuideChannelGuide(){
		 return  new ChannelGuide("edm"){

			@Override
			public List<Map<String, Object>> guideHandler()  {
				return  channelTransformHelper.channelSendInfo(ChannelType.tcommunicateEDM.name());
			}
			   
		   };
		
	}
	
	
	public ChannelGuide ECGuideChannelGuide(){
		 return  new ChannelGuide("ec"){

			@Override
			public List<Map<String, Object>> guideHandler()  {
				return  channelTransformHelper.channelSendInfo(ChannelType.tcommunicateEC.name());
			}
			   
		   };
	}
	
	
	public ChannelGuide MMSGuideChannelGuide(){
		 return  new ChannelGuide("mms"){

			@Override
			public List<Map<String, Object>> guideHandler()  {
				return  channelTransformHelper.channelSendInfo(ChannelType.tcommunicateMMS.name());
			}
			   
		   };
	}
	
	
	public ChannelGuide WAPGuideChannelGuide(){
		 return  new ChannelGuide("wap"){

			@Override
			public List<Map<String, Object>> guideHandler()  {
				return  channelTransformHelper.channelSendInfo(ChannelType.tcommunicateWAP.name());
			}
			   
		   };
	}
	
	
	
	public ChannelGuide AllGuideChannelGuide(){
		 return  new ChannelGuide("all"){

			@Override
			public List<Map<String, Object>> guideHandler()  {
				 return  channelTransformHelper.channelSendInfo("");
			}
			   
		   };
	}
	
	
}
