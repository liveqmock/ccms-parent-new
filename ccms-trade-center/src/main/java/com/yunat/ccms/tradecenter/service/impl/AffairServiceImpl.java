package com.yunat.ccms.tradecenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.controller.vo.AffairsVO;
import com.yunat.ccms.tradecenter.domain.AffairDomain;
import com.yunat.ccms.tradecenter.domain.LoginSubuserRelaDomain;
import com.yunat.ccms.tradecenter.repository.AffairJdbcRepository;
import com.yunat.ccms.tradecenter.repository.AffairRepository;
import com.yunat.ccms.tradecenter.repository.AffairsHandleRepository;
import com.yunat.ccms.tradecenter.service.AffairService;
import com.yunat.ccms.tradecenter.service.BaseService;
import com.yunat.ccms.tradecenter.service.LoginSubuserRelaService;
import com.yunat.ccms.tradecenter.service.queryobject.AffairsQuery;
import com.yunat.ccms.tradecenter.support.util.ListUtil;

@Service
public class AffairServiceImpl extends BaseService implements AffairService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AffairRepository affairRepository;

    @Autowired
    private AffairsHandleRepository affairsHandleRepository;

    @Autowired
	private AffairJdbcRepository affairJdbcRepository;

    @Autowired
    private LoginSubuserRelaService loginSubuserRelaService;

	@Override
	public void addAffair(AffairDomain affairDomain) {
		affairDomain.setCreated(new Date());
		affairDomain.setStatus(1);
//		LoginSubuserRelaDomain lsrd = loginSubuserRelaService.findloginSubuserRela();
//		if(lsrd == null || StringUtils.isEmpty(lsrd.getTaobaoSubuser())){
//			throw new RuntimeException("该用户尚未绑定旺旺子账号，请先绑定！");
//		}else{
			affairDomain.setFounder(loginSubuserRelaService.findloginSubuserRela(affairDomain.getDpId()).getTaobaoSubuser());
			affairRepository.save(affairDomain);
//		}
	}

	@Override
	public void modifyAffair(AffairDomain affairDomain) {
		LoginSubuserRelaDomain lsrd = loginSubuserRelaService.findloginSubuserRela(affairDomain.getDpId());
		if(lsrd == null || StringUtils.isEmpty(lsrd.getTaobaoSubuser())){
			throw new RuntimeException("该用户尚未绑定旺旺子账号，请先绑定！");
		}else{
			affairDomain.setUpdated(new Date());
			affairRepository.saveOrUpdate(affairDomain);
		}
	}

    @Override
    public List<AffairsVO> findAffairs(AffairsQuery affairsQuery) {
        List<AffairsVO> affairsVOList = new ArrayList<AffairsVO>();

        List<AffairDomain> affairDomainList = affairRepository.findAffairs(affairsQuery);

        if (affairDomainList.size() > 0) {
            List<Long> pkids = ListUtil.getPropertiesFromList(affairDomainList, "pkid");

            List<Object> affairToSizeMapList = affairsHandleRepository.countGroupByAffairsId(pkids);
            for (AffairDomain affairDomain : affairDomainList) {
                AffairsVO affairsVO = new AffairsVO();
                affairsVO.setAffairsId(affairDomain.getPkid());
                affairsVO.setTitle(affairDomain.getTitle());
                affairsVO.setFounder(affairDomain.getFounder());
                affairsVO.setExpirationTime(DateUtils.getStringDate(affairDomain.getExpirationTime()));

                for (Object stringStringMap1 : affairToSizeMapList) {
                    Object[] stringStringMap = (Object[])stringStringMap1;
                    Long affairId = (Long)  stringStringMap[0];
                    Long size = (Long) stringStringMap[1];
                    if (affairId == affairDomain.getPkid()) {
                        affairsVO.setHandleSize(size);
                        break;
                    }

                    affairsVO.setHandleSize(0);
                }


                affairsVO.setImportant(affairDomain.getImportant());
                affairsVO.setStatus(affairDomain.getStatus());

                affairsVOList.add(affairsVO);
            }
        }

        return affairsVOList;
    }

	@Override
	public AffairDomain findAffair(Long affair_id) {
		return affairRepository.findByPkid(affair_id);
	}

	@Override
	public Map<String, Object> findOrderInfoByTid(String tid) {
		return affairJdbcRepository.findOrderInfoByTid(tid);
	}

	@Override
	public List<Map<String, Object>> findOrderItemsInfoByTid(String tid) {
		return affairJdbcRepository.findOrderItemsInfoByTid(tid);
	}
}
