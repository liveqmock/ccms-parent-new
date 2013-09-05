package com.yunat.ccms.tradecenter.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.tradecenter.domain.ConfigLogDomain;
import com.yunat.ccms.tradecenter.repository.CfgLogRepository;
import com.yunat.ccms.tradecenter.service.ConfigLogService;

/**
 *
 *
 * @author shaohui.li
 * @version $Id: ConfigLogServiceImpl.java, v 0.1 2013-8-23 下午03:17:37 shaohui.li Exp $
 */
@Service("configLogService")
public class ConfigLogServiceImpl implements ConfigLogService{

    @Autowired
    CfgLogRepository cfgLogRepository;

    /**
     * 获取最近一次配置数据
     * @see com.yunat.ccms.tradecenter.service.ConfigLogService#getPreOpConfig(java.lang.String, int)
     */
    @Override
    public ConfigLogDomain getPreOpConfig(String dpId, int type) {
        List<ConfigLogDomain>  list = cfgLogRepository.getConfigListByDpIdAndType(dpId, type);
        if(list != null && !list.isEmpty()){
            if(list.size() == 1){
                return list.get(0);
            }
        }
        return null;
    }
}
