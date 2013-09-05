package com.yunat.ccms.node.support.validation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.spi.support.ValidateMessage;

/**
 * 流程预执行和提交审批前的validate <br/>
 * 1.要判断时间节点，等待节点，渠道节点的时间配置是否冲突等； <br/>
 * 2.需要检查是否有节点不存在prev节点，整个流程中只允许开始节点不存在prev节点； <br/>
 * 3.需检查整个流程树的叶子节点的类型； <br/>
 * 4.所有节点必须有配置
 * 5.流程至少要包含一个"目标组"节点和"渠道"节点，而且是连接在一起的 <br/>
 * 6.整个流程中只允许存在一个开始节点和一个时间节点 <br/>
 * 7.排重节点前面必须连接至少2个及以上节点，否则预执行时提示"排重节点前必须连接至少2个节点"。 <br/>
 * 8.合并，交集前面必须连接至少2个及以上节点 <br/>
 * 9.排除节点前面必须连接2个节点 <br/>
 * 10.检查目标组节点的配置数据，看前面是否连接的是排重或者拆分节点，再检查连接关系 <br/>
 * 11.检查排除节点中的配置是否与前面节点对应 12.检查排重节点中的配置是否与前面的节点对应 <br/>
 * <br/>
 * 未做的：
 * 
 * @return
 * @author xiaojing.qu
 */
@Component
public class WorkflowValidateService {

	private static Logger logger = LoggerFactory.getLogger(WorkflowValidateService.class);

	@Autowired
	private List<WorkflowValidator> registedValidators;

	public List<ValidateMessage> validate(Long campaignId) {
		List<ValidateMessage> allMessages = new ArrayList<ValidateMessage>();
		if (registedValidators != null && registedValidators.size() > 0) {
			for (WorkflowValidator validator : registedValidators) {
				logger.info("使用流程验证：{}验证活动：{}", validator.getClass().getSimpleName(), campaignId);
				List<ValidateMessage> messages = validator.validate(campaignId);
				logger.info("使用流程验证：{}验证活动：{},结果：{}", new Object[] { validator.getClass().getSimpleName(), campaignId,
						messages });
				if (messages != null) {
					allMessages.addAll(messages);
				}
			}
		}
		return allMessages;
	}
}
