package com.yunat.ccms.tradecenter.controller.vo;

import java.util.List;

/**
 * User: weilin.li
 * Date: 13-7-26
 * Time: 下午5:08
 */
public class CategoryVO {
    private long id;
    private String name;
    private String value;
    private String description;
    CategoryVO parentCategoryVO;
    List<CategoryVO> subList;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryVO getParentCategoryVO() {
        return parentCategoryVO;
    }

    public void setParentCategoryVO(CategoryVO parentCategoryVO) {
        this.parentCategoryVO = parentCategoryVO;
    }

    public List<CategoryVO> getSubList() {
        return subList;
    }

    public void setSubList(List<CategoryVO> subList) {
        this.subList = subList;
    }
}
