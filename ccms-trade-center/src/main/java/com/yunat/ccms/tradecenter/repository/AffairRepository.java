package com.yunat.ccms.tradecenter.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.AffairDomain;
import com.yunat.ccms.tradecenter.service.queryobject.AffairsQuery;

/**
 * 我的事务接口
 * @author Administrator
 *
 */
public interface AffairRepository{

    /**
     * 保存或更新
     * @param affairDomain
     * @param string
     */
    void saveOrUpdate(AffairDomain affairDomain);

    /**
     * 保存
     * @param affairDomain
     */
    void save(AffairDomain affairDomain);

    AffairDomain findByPkid(Long pkid);

    /**
     * 更新当前处理人和状态
     */
    void updateAffairsHandlerAndStatus(@Param("currentHandler")String currentHandler, @Param("status")int status, @Param("pkid")long pkid);

    /**
     * 分页查找
     * @param affairsQuery
     * @return
     */
    List<AffairDomain> findAffairs(AffairsQuery affairsQuery);

    /**
     * 根据订单id获取事务
     *
     * @param tid
     * @param sourceId
     * @return
     */
    AffairDomain getAffairDomainByTid(String tid, long sourceId);

    /**
     * 获取子订单id获取事务
     *
     *
     * @param oid
     * @param sourceId
     * @return
     */
    AffairDomain getAffairDomainByOid(String oid, long sourceId);

    /**
     * 批量获取事务列表
     * @param oids
     * @param sourceId
     * @return
     */
    Map<String, AffairDomain> getAffairDomainMapByOids(List<String> oids, long sourceId);


    List<AffairDomain> getAffairDomainByOids(List<String> tids, long sourceTypeId);

}
