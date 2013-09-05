package com.yunat.ccms.tradecenter.service.impl;

import com.yunat.ccms.tradecenter.controller.vo.CategoryVO;
import com.yunat.ccms.tradecenter.domain.CategoryDomain;
import com.yunat.ccms.tradecenter.repository.CategoryRepository;
import com.yunat.ccms.tradecenter.service.CategoryService;
import com.yunat.ccms.tradecenter.support.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: weilin.li
 * Date: 13-7-26
 * Time: 下午4:45
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void save(CategoryDomain categoryDomain) {
        categoryRepository.save(categoryDomain);

    }

    @Override
    public void delete(long pkid) {
        categoryRepository.delete(pkid);
    }

    @Override
    public CategoryVO get(long pkid) {
        CategoryDomain categoryDomain = categoryRepository.get(pkid);

        return conversionToCategoryVO(categoryDomain);
    }

    @Override
    public List<CategoryVO> findByParentId(long parentId, String outId, String outType) {
        List<CategoryVO> categoryVOList = new ArrayList<CategoryVO>();
        List<CategoryDomain> categoryDomainList = categoryRepository.findByParentId(parentId, outId, outType);

        for (CategoryDomain categoryDomain : categoryDomainList) {
            CategoryVO categoryVO = conversionToCategoryVO(categoryDomain);
            categoryVOList.add(categoryVO);
        }

        return    categoryVOList;
    }

    @Override
    public List<CategoryVO> getCategoryListWithChildren(long parentId, String outId, String outType) {
        List<CategoryVO> categoryVOList = new ArrayList<CategoryVO>();

        List<CategoryDomain> categoryDomainList = categoryRepository.findByParentId(parentId, outId, outType);

        List<CategoryDomain> categoryDomainLists = new ArrayList<CategoryDomain>();
        if (!categoryDomainList.isEmpty())  {
            List<Long> pkids = ListUtil.getPropertiesFromList(categoryDomainList, "pkid");
            categoryDomainLists = categoryRepository.findByParentIds(pkids, outId, outType);
        }

        for (CategoryDomain categoryDomain : categoryDomainList) {
            CategoryVO categoryVO = conversionToCategoryVO(categoryDomain);

            long pkid = categoryDomain.getPkid();

            List<CategoryDomain> categoryDomainList1 = ListUtil.getObjectsFromList(categoryDomainLists, "parentId", pkid);
            List<CategoryVO> categoryVOs = new ArrayList<CategoryVO>();
            for (CategoryDomain domain : categoryDomainList1) {
                CategoryVO catgory = conversionToCategoryVO(domain);
                categoryVOs.add(catgory);
            }
            categoryVO.setSubList(categoryVOs);

            categoryVOList.add(categoryVO);
        }

        return categoryVOList;
    }

    @Override
    public CategoryVO getCategoryWithParent(long pkid) {
        CategoryVO categoryVO = new CategoryVO();

        CategoryDomain categoryDomain = categoryRepository.get(pkid);
        categoryVO.setName(categoryDomain.getName());
        categoryVO.setValue(categoryDomain.getValue());
        categoryVO.setDescription(categoryDomain.getDescription());
        categoryVO.setId(categoryDomain.getPkid());

        CategoryVO iteaCategoryVO =  categoryVO;
        while (categoryDomain.getParentId() != 0) {
            CategoryDomain parentCategory = categoryRepository.get(categoryDomain.getParentId());

            CategoryVO categoryVO1 = new CategoryVO();
            categoryVO1.setName(parentCategory.getName());
            categoryVO1.setValue(parentCategory.getValue());
            categoryVO1.setDescription(parentCategory.getDescription());
            categoryVO1.setId(parentCategory.getPkid());

            iteaCategoryVO.setParentCategoryVO(categoryVO1);

            iteaCategoryVO = categoryVO1;
            categoryDomain = parentCategory;
        }

        return categoryVO;
    }

    private CategoryVO conversionToCategoryVO(CategoryDomain categoryDomain) {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setId(categoryDomain.getPkid());
        categoryVO.setName(categoryDomain.getName());
        categoryVO.setValue(categoryDomain.getValue());
        categoryVO.setDescription(categoryDomain.getDescription());
        return categoryVO;
    }
}
