package com.yunat.ccms.tradecenter.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.yunat.ccms.tradecenter.domain.ConfigLogDomain;
import com.yunat.ccms.tradecenter.repository.CfgLogRepository;

/**
 *
 *
 * @author shaohui.li
 * @version $Id: CfgLogRepositoryImpl.java, v 0.1 2013-8-23 下午04:13:16 shaohui.li Exp $
 */
@Repository
public class CfgLogRepositoryImpl implements CfgLogRepository{

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    @Override
    public List<ConfigLogDomain> getConfigListByDpIdAndType(String dpId, int type) {
        String sql = "select * from tb_tc_config_log where dp_id = '" + dpId + "' and type = " + type + " order by pkid desc limit 1";
        List<ConfigLogDomain> list = em.createNativeQuery(sql, ConfigLogDomain.class).getResultList();
        return list;
    }
}
