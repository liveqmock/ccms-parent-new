package com.yunat.ccms.tradecenter.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.tradecenter.domain.UrpayStatusDomain;
import com.yunat.ccms.tradecenter.repository.UrpayStatusRepository;
import com.yunat.ccms.tradecenter.service.UrpayStatusService;

/**
 *
 * UrpayStatusService 接口实现类
 *
 * @author shaohui.li
 * @version $Id: UrpayStatusServiceImpl.java, v 0.1 2013-6-4 下午07:52:33 shaohui.li Exp $
 */
@Service("urpayStatusService")
public class UrpayStatusServiceImpl implements UrpayStatusService{

    @Autowired
    private UrpayStatusRepository urpayStatusRepository;

    @Override
    public void insertUrpayStatusBatch(List<UrpayStatusDomain> list, String type) {
        urpayStatusRepository.insertUrpayStatusBatch(list, type);
    }

}
