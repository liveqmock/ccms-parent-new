package com.yunat.ccms.tradecenter;

import java.util.ArrayList;
import java.util.List;

import com.yunat.ccms.tradecenter.domain.UrpayStatusDomain;
import com.yunat.ccms.tradecenter.repository.UrpayStatusRepository;

public class UrpayStatusThread extends Thread{

    private UrpayStatusRepository urpayStatusRepository;

    public UrpayStatusThread(UrpayStatusRepository urpayStatusRepository) {
        super();
        this.urpayStatusRepository = urpayStatusRepository;
    }

    @Override
    public void run() {

        List<UrpayStatusDomain> list = new ArrayList<UrpayStatusDomain>();
        for(int i=0;i < 5;i++){
            UrpayStatusDomain d = new UrpayStatusDomain();
            d.setTid(String.valueOf(i));
            d.setAutoUrpayStatus(1);
            d.setAutoUrpayThread("[" + Thread.currentThread().getName() + "]111");
            d.setCloseUrpayStatus(2);
            d.setCloseUrpayThread("222");
            d.setCheapUrpayStatus(5);
            d.setCheapUrpayThread("555");
            list.add(d);
        }
        urpayStatusRepository.insertUrpayStatusBatch(list, "1");
    }
}
