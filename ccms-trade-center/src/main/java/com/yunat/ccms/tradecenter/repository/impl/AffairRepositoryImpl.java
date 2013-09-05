package com.yunat.ccms.tradecenter.repository.impl;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.tradecenter.domain.AffairDomain;
import com.yunat.ccms.tradecenter.repository.AffairRepository;
import com.yunat.ccms.tradecenter.service.queryobject.AffairsQuery;

/**
 * User: weilin.li
 * Date: 13-7-30
 * Time: 上午9:53
 */
@Repository
public class AffairRepositoryImpl implements AffairRepository{
    @PersistenceContext
    private EntityManager em;

    @Transactional
	public void save(AffairDomain affairDomain) {
    	em.persist(affairDomain);
	}

    @Transactional
    public void saveOrUpdate(AffairDomain affairDomain) {
    	AffairDomain affairDomain1 =  em.find(AffairDomain.class, affairDomain.getPkid()) ;
    	affairDomain1.setCurrentHandler(affairDomain.getCurrentHandler());
    	affairDomain1.setExpirationTime(affairDomain.getExpirationTime());
    	affairDomain1.setStatus(affairDomain.getStatus());
    	affairDomain1.setImportant(affairDomain.getImportant());
    	affairDomain1.setNote(affairDomain.getNote());
    	affairDomain1.setSource(affairDomain.getSource());
    	affairDomain1.setSourceId(affairDomain.getSourceId());
    	affairDomain1.setSourceType(affairDomain.getSourceType());
    	affairDomain1.setSourceTypeId(affairDomain.getSourceTypeId());
    	affairDomain1.setStatus(affairDomain.getStatus());
    	affairDomain1.setTitle(affairDomain.getTitle());
    	affairDomain1.setUpdated(new Date());
    	em.merge(affairDomain1);
    }

    @Override
    public AffairDomain findByPkid(Long pkid) {
    	String jpql = "select a from AffairDomain a where a.pkid = :pkid";
    	List<AffairDomain> affairDomains = em.createQuery(jpql).setParameter("pkid", pkid).getResultList();

        if (affairDomains.isEmpty()) {
            return null;
        }

        return affairDomains.get(0);

    }

    @Override
    public void updateAffairsHandlerAndStatus(@Param("currentHandler") String currentHandler, @Param("status") int status, @Param("pkid") long pkid) {
        String jpql = "update AffairDomain a set a.currentHandler = :currentHandler, a.status = :status where a.pkid = :pkid";
        em.createQuery(jpql).setParameter("currentHandler", currentHandler).setParameter("status", status).setParameter("pkid", pkid).executeUpdate();
    }

    @Override
    public List<AffairDomain> findAffairs(AffairsQuery affairsQuery) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> clong = cb.createQuery(Long.class);
        Root<AffairDomain> longRoot = clong.from(AffairDomain.class);

        clong.select(cb.count(longRoot));

        List<Predicate> predicateList = new ArrayList<Predicate>();

        if (affairsQuery.getId() != null) {
            predicateList.add(cb.equal(longRoot.get("pkid"), affairsQuery.getId()));
        }

        if (!StringUtils.isEmpty(affairsQuery.getKeyword())) {
            Expression<String> titlePath  = longRoot.get("title");
            Path<String> founderPath = longRoot.get("founder");
            Path<String> currentHandlerPath = longRoot.get("currentHandler");
            predicateList.add(cb.or(cb.like(titlePath, "%" + affairsQuery.getKeyword() + "%"), cb.like(founderPath, "%" + affairsQuery.getKeyword() + "%"), cb.like(currentHandlerPath, "%" + affairsQuery.getKeyword() + "%")));
        }

        if (!StringUtils.isEmpty(affairsQuery.getCurrentHandler())) {
            predicateList.add(cb.equal(longRoot.get("currentHandler"), affairsQuery.getCurrentHandler()));
        }

        if (!StringUtils.isEmpty(affairsQuery.getFounder())) {
            predicateList.add(cb.equal(longRoot.get("founder"), affairsQuery.getFounder()));
        }

        if (affairsQuery.getImportant() != null) {
            predicateList.add(cb.equal(longRoot.get("important"), affairsQuery.getImportant()));
        }

        if (affairsQuery.getStatus() != null) {
            predicateList.add(cb.equal(longRoot.get("status"), affairsQuery.getStatus()));
        }

        if (affairsQuery.getSourceId() != null) {
            predicateList.add(cb.equal(longRoot.get("sourceId"), affairsQuery.getSourceId()));
        }

        if (affairsQuery.getSourceTypeId() != null) {
            predicateList.add(cb.equal(longRoot.get("sourceTypeId"), affairsQuery.getSourceTypeId()));
        }

        if (StringUtils.isNotEmpty(affairsQuery.getDpId())) {
            Expression<String> path = longRoot.get("dpId");
            predicateList.add(cb.equal(path, affairsQuery.getDpId()));
        }

        clong.where(predicateList.toArray(new Predicate[]{}));

        int totalItem = em.createQuery(clong).getSingleResult().intValue();
        affairsQuery.setTotalItem(totalItem);

        CriteriaQuery<AffairDomain> c = cb.createQuery(AffairDomain.class);
        Root<AffairDomain> affairDomainRoot = c.from(AffairDomain.class);

        c.select(affairDomainRoot);

        predicateList = new ArrayList<Predicate>();

        if (affairsQuery.getId() != null) {
            predicateList.add(cb.equal(longRoot.get("pkid"), affairsQuery.getId()));
        }

        if (!StringUtils.isEmpty(affairsQuery.getKeyword())) {
            Expression<String> titlePath  = affairDomainRoot.get("title");
            Path<String> founderPath = affairDomainRoot.get("founder");
            Path<String> currentHandlerPath = affairDomainRoot.get("currentHandler");
            predicateList.add(cb.or(cb.like(titlePath, "%" + affairsQuery.getKeyword() + "%"), cb.like(founderPath, "%" + affairsQuery.getKeyword() + "%"), cb.like(currentHandlerPath, "%" + affairsQuery.getKeyword() + "%")));
        }

        if (!StringUtils.isEmpty(affairsQuery.getCurrentHandler())) {
            //c.where(cb.equal(affairDomainRoot.get("currentHandler"), affairsQuery.getCurrentHandler()));
            predicateList.add(cb.equal(affairDomainRoot.get("currentHandler"), affairsQuery.getCurrentHandler()));
        }

        if (!StringUtils.isEmpty(affairsQuery.getFounder())) {
            //c.where(cb.equal(affairDomainRoot.get("founder"), affairsQuery.getFounder()));
            predicateList.add(cb.equal(affairDomainRoot.get("founder"), affairsQuery.getFounder()));
        }

        if (affairsQuery.getImportant() != null) {
            //c.where(cb.equal(affairDomainRoot.get("important"), affairsQuery.getImportant()));
            predicateList.add(cb.equal(affairDomainRoot.get("important"), affairsQuery.getImportant()));
        }

        if (affairsQuery.getStatus() != null) {
           // c.where(cb.equal(affairDomainRoot.get("status"), affairsQuery.getStatus()));
            predicateList.add(cb.equal(affairDomainRoot.get("status"), affairsQuery.getStatus()));
        }

        if (affairsQuery.getSourceId() != null) {
            //c.where(cb.equal(affairDomainRoot.get("sourceId"), affairsQuery.getSourceId()));
            predicateList.add(cb.equal(affairDomainRoot.get("sourceId"), affairsQuery.getSourceId()));
        }

        if (affairsQuery.getSourceTypeId() != null) {
            //c.where(cb.equal(affairDomainRoot.get("sourceTypeId"), affairsQuery.getSourceTypeId()));
            predicateList.add(cb.equal(affairDomainRoot.get("sourceTypeId"), affairsQuery.getSourceTypeId()));
        }

        if (StringUtils.isNotEmpty(affairsQuery.getDpId())) {
            Expression<String> path = affairDomainRoot.get("dpId");
            predicateList.add(cb.equal(path, affairsQuery.getDpId()));
        }

        c.where(predicateList.toArray(new Predicate[]{}));

        if (!StringUtils.isEmpty(affairsQuery.getFirstOrder())) {
            if ("desc".equals(affairsQuery.getFirstOrderSort())) {
                c.orderBy(cb.desc(affairDomainRoot.get(affairsQuery.getFirstOrder())));
            } else {
                c.orderBy(cb.asc(affairDomainRoot.get(affairsQuery.getFirstOrder())));
            }
        }

        if (!StringUtils.isEmpty(affairsQuery.getSecondOrder())) {
            if ("desc".equals(affairsQuery.getSecondOrderSort())) {
                c.orderBy(cb.desc(affairDomainRoot.get(affairsQuery.getSecondOrder())));
            } else {
                c.orderBy(cb.asc(affairDomainRoot.get(affairsQuery.getSecondOrder())));
            }
        }

        TypedQuery<AffairDomain> q = em.createQuery(c);
        List<AffairDomain> affairDomainList = q.setFirstResult(affairsQuery.getStartRow()).setMaxResults(affairsQuery.getPageSize()).getResultList();

        return affairDomainList;
    }

    @Override
    public AffairDomain getAffairDomainByTid(String tid, long sourceId) {
        List<AffairDomain> affairDomains = em.createQuery("select a from AffairDomain a where a.tid = :tid and a.sourceId = :sourceId").setParameter("tid", tid).setParameter("sourceId", sourceId).getResultList();

        if (affairDomains.isEmpty()) {
            return null;
        }

        return affairDomains.get(0);
    }

    @Override
    public AffairDomain getAffairDomainByOid(String oid, long sourceId) {
        List<AffairDomain> affairDomains= em.createQuery("select a from AffairDomain a where a.oid = :oid and a.sourceId = :sourceId").setParameter("oid", oid).setParameter("sourceId", sourceId).getResultList();

        if (affairDomains.isEmpty()) {
            return null;
        }

        return affairDomains.get(0);
    }

    @Override
    public Map<String, AffairDomain> getAffairDomainMapByOids(List<String> oids, long sourceId) {
        Map<String, AffairDomain> oidAffairMap = new HashMap<String, AffairDomain>();

        if (oids.size() > 0) {
            List<AffairDomain> affairDomains =  em.createQuery("select a from AffairDomain a where a.oid in (:oids) and a.sourceId = :sourceId").setParameter("oids", oids).setParameter("sourceId", sourceId).getResultList();

            for (AffairDomain affairDomain : affairDomains) {
                oidAffairMap.put(affairDomain.getOid(), affairDomain);
            }
        }

        return oidAffairMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AffairDomain> getAffairDomainByOids(List<String> tids, long sourceId) {
        return (List<AffairDomain>)em.createQuery("select a from AffairDomain a where a.tid in (:tids) and a.sourceId = :sourceId").setParameter("tids", tids).setParameter("sourceId", sourceId).getResultList();
    }
}
