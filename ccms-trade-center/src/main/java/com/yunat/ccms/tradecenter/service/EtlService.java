package com.yunat.ccms.tradecenter.service;

/**
 *
 * Etl服务
 * @author shaohui.li
 * @version $Id: EtlService.java, v 0.1 2013-6-7 上午11:38:54 shaohui.li Exp $
 */
public interface EtlService {

    /**
     * elt数据是否超时
     *
     * @return
     */
    public boolean isEtlTimeOut(String dpId,int offSet,String sessionKey);


    /**
     * etl超时时间
     *
     * @return
     */
    public int getEtlTimeOutMinute();
}
