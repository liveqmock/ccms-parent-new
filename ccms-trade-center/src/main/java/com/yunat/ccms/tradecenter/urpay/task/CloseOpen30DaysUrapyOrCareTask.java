package com.yunat.ccms.tradecenter.urpay.task;

import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;
import com.yunat.ccms.tradecenter.repository.CareConfigRepository;
import com.yunat.ccms.tradecenter.repository.UrpayConfigRepository;

/**
 *
 * 关闭所有用户配置为：开始30天即将过期的催付或者关怀的任务
 *
 * 对于超过30天的都自动关闭
 *
 * 每天执行一次，每天晚上12:00点执行，关闭第二天即将过期的任务
 *
 *
 * @author shaohui.li
 * @version $Id: CloseOpen30DaysUrapyOrCareTask.java, v 0.1 2013-7-12 下午02:27:26 shaohui.li Exp $
 */
public class CloseOpen30DaysUrapyOrCareTask extends BaseJob{

    @Autowired
    private UrpayConfigRepository urpayConfigRepository;

    @Autowired
    private CareConfigRepository careConfigRepository;

    @Override
    public void handle(JobExecutionContext context) {
        logger.info("开始处理催付的30天过期的数据....");
        List<UrpayConfigDomain> urpayList = urpayConfigRepository.getOpen30DaysUrpayConfig();
        //当前时间
        Date curTime = new Date();
        if(urpayList != null && !urpayList.isEmpty()){
            for(UrpayConfigDomain config : urpayList){
                Date endTime = config.getEndDate();
                //当前时间在结束时间之后，即当前时间大于结束时间
                if(endTime != null && curTime.after(endTime)){
                    urpayConfigRepository.updateUrpayStatusByPkId(config.getPkid());
                    logger.info("催付:[" + config.getUrpayType() + "],店铺:[" + config.getDpId() + "],结束日期:[" + DateUtils.getStringDate(endTime) + "],自动过期修改完成");
                }
            }
        }
        logger.info("完成处理催付的30天过期的数据，关怀数据准备处理....");
        List<CareConfigDomain> careList = careConfigRepository.getOpen30DaysCareConfig();
        if(careList != null && !careList.isEmpty()){
            for(CareConfigDomain config : careList){
                Date endTime = config.getEndDate();
                //当前时间在结束时间之后，即当前时间大于结束时间
                if(endTime != null && curTime.after(endTime)){
                    careConfigRepository.updateCareStatusByPkId(config.getPkid());
                    logger.info("关怀:[" + config.getCareType()+ "],店铺:[" + config.getDpId() + "],结束日期:[" + DateUtils.getStringDate(endTime) + "],自动过期修改完成");
                }
            }
        }
        logger.info("完成处理关怀的30天过期的数据 ....");
    }
}
