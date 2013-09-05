/**
 *
 */
package com.yunat.ccms.tradecenter.urpay.task;

import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.repository.CareConfigRepository;
import com.yunat.ccms.tradecenter.repository.IOrderRepository;
import com.yunat.ccms.tradecenter.repository.MQlogisticsRepository;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.ccms.tradecenter.support.util.ListUtil;
import net.sf.json.JSONObject;
import org.quartz.JobExecutionContext;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-13 下午06:12:58
 */
public class MQLogisticsCareOpenTask extends BaseJob{

	@Autowired
	private RabbitTemplate rabbitTemplate;

    @Autowired
    private CareConfigRepository careConfigRepository;

    @Autowired
    private IOrderRepository iOrderRepository;

	@Override
	public void handle(JobExecutionContext context) {
		logger.info("==开始执行发送获取物流数据的MQ消息任务==");

        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateStr = format.format(new Date());

        //获取店铺配置
        Map<String, Integer> shopToLastConfigMap = new HashMap<String, Integer>();
        List<CareConfigDomain> careConfigDomains = careConfigRepository.getOpenedCareConfigs(Arrays.asList(UserInteractionType.SHIPMENT_CARE.getType(), UserInteractionType.ARRIVED_CARE.getType(), UserInteractionType.DELIVERY_CARE.getType(), UserInteractionType.SIGNED_CARE.getType()));

        if (careConfigDomains.size() == 0) {
            return;
        }

        for (CareConfigDomain careConfigDomain : careConfigDomains) {
            Integer currCareType =  shopToLastConfigMap.get(careConfigDomain.getDpId());

            Integer thisCareType = careConfigDomain.getCareType();

            //此处注意：依赖上述关怀类型的顺序正好是由小到大
            if (currCareType == null || thisCareType > currCareType) {
                shopToLastConfigMap.put(careConfigDomain.getDpId(), thisCareType);
            }
        }

        //获取需要发送的物流消息
        List<OrderDomain> orderDomains = iOrderRepository.findLogisticsCareOpenOrders(shopToLastConfigMap);

        List<List<OrderDomain>> shopsOrderDomains = ListUtil.togetherListByPrperties(orderDomains, "dpId");
        logger.info("获取发货订单数据分组后，总共【"+shopsOrderDomains.size()+"】店铺");

        for (List<OrderDomain> shopOrderDomains : shopsOrderDomains) {
            String dpId = shopOrderDomains.get(0).getDpId();
            List<Long> tids = ListUtil.getPropertiesFromList(shopOrderDomains, "tid");

            logger.info("获取发货的订单数据，店铺【"+dpId+"】，共【"+tids.size()+"】个订单");
            int updatedNum = 1000;
            int num = (tids.size() + (updatedNum - 1)) / updatedNum;
            for(int j = 0; j < num; j++){
                List<Long> listT = tids.subList(j * updatedNum, Math.min(j * updatedNum + updatedNum, tids.size()));
                logger.info("发送获取物流数据的MQ消息，shop_id:" + dpId+",数量：【"+listT.size() +"】,第【"+(j+1)+"】个消息");
                JSONObject json = new JSONObject();
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("shop_id", dpId);
                map.put("task_id", "logistics_"+dateStr+"_"+dpId+"_"+(j+1));
                map.put("tids", listT);
                json.put("message", map);
                rabbitTemplate.convertAndSend(json.toString());
            }
        }
    }

}
