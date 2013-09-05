package com.yunat.ccms.tradecenter.repository.impl;

import com.yunat.ccms.tradecenter.domain.CategoryDomain;
import com.yunat.ccms.tradecenter.repository.CategoryRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**.
 * User: weilin.li
 * Date: 13-7-25
 * Time: 下午7:07
 */
@Repository
public class CategoryRepositoryImpl implements CategoryRepository{
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(CategoryDomain categoryDomain) {
        if (categoryDomain.getParentId() == null) {
            categoryDomain.setParentId(0l);
        }

        if (categoryDomain.getIsDelete() == null) {
            categoryDomain.setIsDelete(0);
        }
        categoryDomain.setCreated(new Date());
        categoryDomain.setUpdated(new Date());
        em.persist(categoryDomain);
    }

    @Transactional
    public void delete(long pkid) {
        String jpql = "update CategoryDomain c  set c.isDelete = 1 where c.pkid = :pkid";
        em.createQuery(jpql)
                .setParameter("pkid", pkid).executeUpdate();
    }

    @Override
    public CategoryDomain get(long pkid) {
        String jpql = "select c from CategoryDomain c where c.pkid = :pkid";
        CategoryDomain categoryDomain = (CategoryDomain)em.createQuery(jpql)
                .setParameter("pkid", pkid).getSingleResult();
        return categoryDomain;
    }

    public List<CategoryDomain> findByParentId(long parentId, String outId, String outType) {
        List<CategoryDomain> categoryDomainList = new ArrayList<CategoryDomain>();
        if (StringUtils.isEmpty(outId)) {
            String jpql =  "select object(c) from CategoryDomain c where c.outType = :outType and c.parentId = :parentId and c.isDelete = 0";
            categoryDomainList = em.createQuery(jpql)
                    .setParameter("parentId", parentId).setParameter("outType", outType).getResultList();
        } else {
            String jpql =  "select object(c) from CategoryDomain c where (c.outId = :outId or c.outId = '0') and c.outType = :outType and c.parentId = :parentId and c.isDelete = 0";
            categoryDomainList = em.createQuery(jpql)
                    .setParameter("parentId", parentId) .setParameter("outId", outId).setParameter("outType", outType).getResultList();
        }


        return categoryDomainList;
    }

    @Override
    public List<CategoryDomain> findByParentIds(List<Long> parentIds, String outId, String outType) {
        String jpql =  "select object(c) from CategoryDomain c where (c.outId = :outId or c.outId = '0') and c.outType = :outType and c.parentId in (:parentIds) and c.isDelete = 0";
        List<CategoryDomain> categoryDomainList = em.createQuery(jpql)
                .setParameter("parentIds", parentIds) .setParameter("outId", outId).setParameter("outType", outType).getResultList();
        return categoryDomainList;
    }
}
