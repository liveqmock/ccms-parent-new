package com.yunat.ccms.tradecenter.service.impl;

import com.yunat.ccms.tradecenter.domain.AffairsHandleDomain;
import com.yunat.ccms.tradecenter.repository.AffairRepository;
import com.yunat.ccms.tradecenter.repository.AffairsHandleRepository;
import com.yunat.ccms.tradecenter.service.AffairsHandleService;
import com.yunat.ccms.tradecenter.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * User: weilin.li
 * Date: 13-7-29
 * Time: 下午2:58
 */
@Service("affairsHandleService")
public class AffairsHandleServiceImpl  extends BaseService implements AffairsHandleService{

    @Autowired
    private AffairsHandleRepository affairsHandleRepository;

    @Autowired
    private AffairRepository affairRepository;

    @Override
    @Transactional
    public void handleAffairs(String note, String nextHandler, int status, long affirsId) {

        AffairsHandleDomain affairsHandleDomain = new AffairsHandleDomain();

        //设置用户名
        affairsHandleDomain.setFounder(getLoginName());
        affairsHandleDomain.setNote(note);
        affairsHandleDomain.setNextHandler(nextHandler);
        affairsHandleDomain.setAffairsId(affirsId);
        affairsHandleDomain.setCreated(new Date());
        affairsHandleDomain.setUpdated(new Date());

        //保存
        affairsHandleRepository.save(affairsHandleDomain);

        //保存事务状态和事务当前处理人
        affairRepository.updateAffairsHandlerAndStatus(nextHandler, status, affirsId);
    }

	@Override
	public List<AffairsHandleDomain> findAffairHandles(Long affair_id) {
		return affairsHandleRepository.getByAffairsId(affair_id);
	}
}
