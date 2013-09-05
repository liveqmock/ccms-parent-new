package com.yunat.ccms.tradecenter.service;

import com.yunat.ccms.tradecenter.domain.ConfigLogDomain;

/**
 * 发送日志服务
 *
 * @author shaohui.li
 * @version $Id: ConfigLogService.java, v 0.1 2013-8-23 下午03:15:05 shaohui.li Exp $
 */
public interface ConfigLogService {

    /**
     *获取前一次操作信息
     *
     * @param dpId
     * @param type
     * @return
     */
    public ConfigLogDomain getPreOpConfig(String dpId,int type);

}
