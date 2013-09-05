package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import com.yunat.ccms.tradecenter.domain.ConfigLogDomain;

/**
 * 配置 日志服务
 *
 * @author shaohui.li
 * @version $Id: CfgLogRepository.java, v 0.1 2013-8-23 下午04:10:38 shaohui.li Exp $
 */
public interface CfgLogRepository {

    /**
     * 根据店铺和类型获取最近2条数据
     *
     * @param dpId
     * @param type
     * @return
     */
    public List<ConfigLogDomain> getConfigListByDpIdAndType(String dpId, int type);
}
