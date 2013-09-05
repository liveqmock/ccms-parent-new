package com.yunat.ccms.tradecenter.service;

import com.yunat.ccms.tradecenter.controller.vo.CategoryVO;
import com.yunat.ccms.tradecenter.domain.CategoryDomain;

import java.util.List;

/**
 * User: weilin.li
 * Date: 13-7-26
 * Time: 下午4:43
 */
public interface CategoryService {
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

    CategoryVO get(long pkid);

    /**
     *
     *
     *
     * @param parentId
     * @param outId
     * @param outType
     * @return
     */
    List<CategoryVO> findByParentId(long parentId, String outId, String outType);


    /**
     * 获取迭代类别结构
     *
     * @param parentId
     * @param outId
     * @param outType
     * @return
     */
    List<CategoryVO> getCategoryListWithChildren(long parentId, String outId, String outType);

    /**
     * 获取顶级结构
     *
     *
     * @param pkid
     * @return
     */
    CategoryVO getCategoryWithParent(long pkid);
}
