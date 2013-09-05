package com.yunat.ccms.node.biz.sms;

import java.util.Map;

import org.apache.oro.text.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.ccms.channel.support.ChannelType;
import com.yunat.ccms.channel.support.MarkVariableResolve;
import com.yunat.ccms.channel.support.service.GatewayService;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.exception.CcmsBusinessException;
import com.yunat.channel.business.sms.SMSField;
import com.yunat.channel.business.sms.SMSPreviewUtil;

@Controller
public class NodeSMSController {
	private static Logger logger = LoggerFactory.getLogger(NodeSMSController.class);

	@Autowired
	private NodeSMSQuery nodeSMSQuery;

	@Autowired
	private NodeSMSCommand nodeSMSCommand;

	@Autowired
	private AppPropertiesQuery appPropertiesQuery;

	@Autowired
	private GatewayService gatewayService;

	@Autowired
	private MarkVariableResolve markVariableResolve;
	
	@ResponseBody
	@RequestMapping(value = "/node/sms/{id}", method = RequestMethod.GET)
	public Map<String, Object> open(@PathVariable final Long id, @RequestParam final String name) throws Exception {
		try {
			Map<String, Object> result = Maps.newHashMap();
			result.put(Variable.NODE_ID, id);
			result.put(Variable.DELIVERY_CHANNEL_LIST, gatewayService.retrievalAllGateways(
					appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID),
					appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_PASSWORD), ChannelType.SMS,
					PlatEnum.taobao));
			NodeSMS nodeSMS = nodeSMSQuery.findByNodeId(id);
			if (null != nodeSMS) {
				result.put(Variable.NAME, nodeSMS.getName());
				result.put(Variable.UNSUBSCRIBE_ENABLED, nodeSMS.getUnsubscribeEnabled());
				result.put(Variable.BLACKLIST_DISABLED, nodeSMS.getBlacklistDisabled());
				result.put(Variable.REDLIST_ENABLED, nodeSMS.getRedlistEnabled());
				result.put(Variable.DELIVERY_CHANNEL_ID, nodeSMS.getDeliveryChannelId());
				result.put(Variable.TEST_PHONE_STRING, nodeSMS.getTestPhoneString());
				result.put(Variable.MESSAGE_VALUE, nodeSMS.getMessageValue());
			} else {
				result.put(Variable.NAME, name);
				result.put(Variable.UNSUBSCRIBE_ENABLED, false);
				result.put(Variable.BLACKLIST_DISABLED, true); // 默认情况是黑名单选中
				result.put(Variable.REDLIST_ENABLED, false);
				result.put(Variable.DELIVERY_CHANNEL_ID, null);
				result.put(Variable.TEST_PHONE_STRING, "");
				result.put(Variable.MESSAGE_VALUE, "");
			}
			return result;
		} catch (Exception e) {
			logger.info("exception happend : {}", e.getMessage());
			throw new CcmsBusinessException("open sms node happend exception");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/node/sms", method = RequestMethod.POST)
	public Map<String, Object> save(@RequestBody final NodeSMS requestEntity) throws Exception {
		try {
			NodeSMS nodeSMS = nodeSMSQuery.findByNodeId(requestEntity.getNodeId());
			if (null == nodeSMS) {
				nodeSMS = new NodeSMS();
			}
			BeanUtils.copyProperties(requestEntity, nodeSMS);
			nodeSMSCommand.saveOrUpdate(nodeSMS);

			Map<String, Object> result = Maps.newHashMap();
			result.put(Variable.NODE_ID, nodeSMS.getNodeId());
			result.put(Variable.NAME, nodeSMS.getName());
			result.put(Variable.UNSUBSCRIBE_ENABLED, nodeSMS.getUnsubscribeEnabled());
			result.put(Variable.BLACKLIST_DISABLED, nodeSMS.getBlacklistDisabled());
			result.put(Variable.REDLIST_ENABLED, nodeSMS.getRedlistEnabled());
			result.put(Variable.DELIVERY_CHANNEL_ID, nodeSMS.getDeliveryChannelId());
			result.put(Variable.TEST_PHONE_STRING, nodeSMS.getTestPhoneString());
			result.put(Variable.MESSAGE_VALUE, nodeSMS.getMessageValue());
			return result;

		} catch (Exception e) {
			logger.info("exception happend : {}", e.getMessage());
			throw new CcmsBusinessException("save sms node happend exception");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/node/sms/calculate", method = RequestMethod.POST)
	public String[] calculateSMSLenght(@RequestBody final CalculateSMSRequest request) throws Exception {
		try {
			SMSField smsField = new SMSField(request.getMarkLength(), request.getWordsLimit(),
					request.getGatewayType(), request.getIsOrderBack(), request.getBackOrderInfo(), request.getSign());
			SMSPreviewUtil smsPreview = new SMSPreviewUtil(smsField);
			String orignContent = request.getContent();
			Pattern ImgPattern = markVariableResolve.messageImgMarkVariable();
			Pattern spanPattern = markVariableResolve.messageSpanMarkVariable();
			String filterContent = markVariableResolve.messageSubstitute(ImgPattern, orignContent, "$2");
			filterContent = markVariableResolve.messageSubstitute(spanPattern, filterContent, "$2");
			String data[] = smsPreview.smsPreview(filterContent);
			return data;
		} catch (Exception e) {
			logger.info("calculate sms length exception happend : {}", e.getMessage());
			throw new CcmsBusinessException("caculate sms length happend exception");
		}
	}

	private interface Variable {
		String NODE_ID = "nodeId";
		String NAME = "name";
		String UNSUBSCRIBE_ENABLED = "unsubscribeEnabled";
		String BLACKLIST_DISABLED = "blacklistDisabled";
		String REDLIST_ENABLED = "redlistEnabled";
		String DELIVERY_CHANNEL_ID = "deliveryChannelId";
		String TEST_PHONE_STRING = "testPhoneString";
		String MESSAGE_VALUE = "messageValue";
		String DELIVERY_CHANNEL_LIST = "deliveryChannelList";
	}
}