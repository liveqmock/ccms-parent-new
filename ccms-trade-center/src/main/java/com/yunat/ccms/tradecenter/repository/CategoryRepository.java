package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import com.yunat.ccms.tradecenter.domain.CategoryDomain;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-25
 * Time: 下午6:41
 * To change this template use File | Settings | File Templates.
 */
public interface CategoryRepository {
    /**
     * 保存
     * @param categoryDomain
     */
    void save(CategoryDomain categoryDomain);

    /**
     * 删除指定的类别
     * @param pkid
     */
    void delete(long pkid);


    /**
     * 根据主键获得
     * @param pkid
     * @return
     */
    CategoryDomain get(long pkid);

    /**
     *
     *
     * @param parentId
     * @param outId
     * @param outType
     * @return
     */
    List<CategoryDomain> findByParentId(long parentId, String outId, String outType);

    /**
     *
     *
     *
     * @param outId
     * @param outType
     * @return
     */
    List<CategoryDomain> findByParentIds(List<Long> parentIds, String outId, String outType);
}
